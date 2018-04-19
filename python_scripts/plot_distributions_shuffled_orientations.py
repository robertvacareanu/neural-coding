import argparse
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import random
import sys
from scipy.stats import norm
from scipy.stats.stats import pearsonr


def normalize_array(x):
    return (x - x.min()) / (x.max() - x.min())


def get_data(data):
    return data[:, :-1]


def get_labels(data):
    return data[:, -1]


def chance_level(paths, orientation, nr_run_time):
    data1 = pd.read_csv(paths[0], header=None).values
    data2 = pd.read_csv(paths[1], header=None).values

    results = []

    for n in range(nr_run_time):
        x1 = normalize_array(get_data(data1))
        x2 = normalize_array(get_data(data2))

        y1 = get_labels(data1)
        y2 = get_labels(data2)

        np.random.shuffle(x1)
        np.random.shuffle(x2)

        selected_data1 = x1[y1 == orientation][:, :]
        selected_data2 = x2[y2 == orientation][:, :]

        sample1 = random.choice(selected_data1)
        sample2 = random.choice(selected_data2)

        results.append(pearsonr(sample1, sample2)[0])

    x = np.array(results)
    x.sort()

    f = plt.figure()
    ax = f.add_subplot(111)
    plt.text(0.05, 0.97, "Mean: %f" % np.mean(x), ha='left', va='top', transform=ax.transAxes)
    plt.text(0.05, 0.94, "Std: %f" % np.std(x), ha='left', va='top', transform=ax.transAxes)

    fit = norm.pdf(x, np.mean(x), np.std(x))
    plt.plot(x, fit, color='red')

    plt.axvline(x.mean(), color='magenta', linewidth=1, label='Mean')
    plt.hist(x, bins='auto', normed=True)
    plt.title("Correlation shuffled labels FR_MA orientation: %d" % orientation[0])
    plt.savefig("../plots/correlation_FR_MA_shuffled_labels_%s.png" % orientation[0])
    plt.show()


parser = argparse.ArgumentParser(description="Script to plot the correlation")
parser.add_argument("-p", "--paths", nargs="*", required=True, help="Path to the files")
parser.add_argument("-o", "--orientation", nargs=1, required=True, type=int)
parser.add_argument("-n", "--run_times", required=False, type=int, default=10000)
result = parser.parse_args(sys.argv[1:])

chance_level(result.paths, result.orientation, result.run_times)
