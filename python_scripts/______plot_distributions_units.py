import argparse
import matplotlib.patches as mpatches
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import sys
from scipy.stats import norm
from scipy.stats.stats import pearsonr


def normalize_array(x):
    return (x - x.min()) / (x.max() - x.min())


def get_data(data):
    return data[:, :-1]


def get_labels(data):
    return data[:, -1]


def row_has_zero_values(x):
    y = filter(lambda it: it == 0, x)
    return np.array(y).size > 0


def chance_level(paths, unit, nr_run_time):
    data1 = pd.read_csv(paths[0], header=None).values
    data2 = pd.read_csv(paths[1], header=None).values

    data1 = np.array(data1)
    data2 = np.array(data2)

    results = []

    # remove the trials that contain 0

    indexes_to_remove = np.array([])

    for i in range(data2.shape[0]):
        if row_has_zero_values(data2[i]):
            indexes_to_remove = np.append(indexes_to_remove, i)

    data2 = np.delete(data2, indexes_to_remove, axis=0)
    data1 = np.delete(data1, indexes_to_remove, axis=0)

    y1 = get_labels(data1)
    y2 = get_labels(data2)

    # for i in range(8):
    #     o = i * 45
    #     print o
    #     print data1[y1 == o].shape

    x1 = get_data(data1)
    x2 = get_data(data2)

    unit_fr = x1[:, unit]
    unit_ma = x2[:, unit]

    init_correlation = pearsonr(unit_fr, unit_ma)[0]

    for n in range(nr_run_time):
        np.random.shuffle(data1)
        np.random.shuffle(data2)

        x1 = get_data(data1)
        x2 = get_data(data2)

        unit_fr = x1[:, unit]
        unit_ma = x2[:, unit]

        results.append(pearsonr(unit_fr, unit_ma)[0])

    x = np.array(results)
    x.sort()

    f = plt.figure()
    ax = f.add_subplot(111)
    plt.text(0.05, 0.97, "Mean: %f" % np.mean(x), ha='left', va='top', transform=ax.transAxes)
    plt.text(0.05, 0.94, "Std: %f" % np.std(x), ha='left', va='top', transform=ax.transAxes)

    magenta_patch = mpatches.Patch(color='cyan', label='Mean')
    green_patch = mpatches.Patch(color='yellow', label='Initial correlation')
    plt.legend(handles=[magenta_patch, green_patch])

    fit = norm.pdf(x, np.mean(x), np.std(x))
    plt.plot(x, fit, color='red')

    plt.axvline(x.mean(), color='cyan', linewidth=1.5, label='Mean')
    plt.axvline(init_correlation, color='yellow', linewidth=1.5, label='Init_correlation')
    plt.hist(x, bins='auto', normed=True)
    plt.title("Correlation FR_MA_UNIT: %d" % unit)
    figure = plt.gcf()
    figure.set_size_inches(15, 9)
    #plt.show()
    plt.savefig("correlation_FR_MA_UNIT_%s.png" % unit, dpi=100)


parser = argparse.ArgumentParser(description="Script to plot the correlation")
parser.add_argument("-p", "--paths", nargs="*", required=True, help="Path to the files")
parser.add_argument("-u", "--unit", nargs=1, required=True, type=int, help="The unit for which correlation is compared")
parser.add_argument("-n", "--run_times", required=False, type=int, default=10000)
result = parser.parse_args(sys.argv[1:])

chance_level(result.paths, result.unit[0], result.run_times)