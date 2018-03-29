import argparse
import sys

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from scipy.stats.stats import pearsonr


def correlation(paths, save_path, only_pairs):
    data = pd.read_csv(paths[0], header=None)
    x1 = data.iloc[0:, :-1].values

    data = pd.read_csv(paths[1], header=None)
    x2 = data.iloc[:, :-1].values
    x1_n = (x1 - x1.min()) / (x1.max() - x1.min())
    x2_n = (x2 - x2.min()) / (x2.max() - x2.min())
    if only_pairs:
        res = np.zeros(x1_n.shape[1] * x2_n.shape[1]).reshape([x1_n.shape[1], x2_n.shape[1]])
        for x in range(x1_n.shape[1]):
            for y in range(x2_n.shape[1]):
                res[x][y] = (pearsonr(x1_n[:, x], x2_n[:, y])[0])
    else:
        res = np.zeros(x1_n.shape[1]).reshape([x1_n.shape[1], 1])
        for x in range(x1_n.shape[1]):
            res[x] = (pearsonr(x1_n[:, x], x2_n[:, x])[0])

    plt.title("Correlation")
    fig = plt.gcf()
    fig.set_size_inches(15, 15)
    plt.xlabel(paths[0])
    plt.ylabel(paths[1])

    c = plt.pcolor(res, edgecolors='k', linewidths=4, cmap='RdBu', vmin=-1.0, vmax=1.0)
    show_values(c)
    plt.colorbar(c)
    plt.xticks([label + 1 for label in range(x1_n.shape[1])])
    plt.yticks([label + 1 for label in range(x1_n.shape[1])])
    fig.set_size_inches(25, 12, forward=True)
    if save_path is not None:
        plt.savefig(save_path)
    else:
        plt.show()


def show_values(pc, fmt="%.2f", **kw):
    from itertools import izip
    pc.update_scalarmappable()
    ax = pc.axes
    for p, color, value in izip(pc.get_paths(), pc.get_facecolors(), pc.get_array()):
        x, y = p.vertices[:-2, :].mean(0)
        if np.all(color[:3] > 0.0):
            color = (0.0, 0.0, 0.0)
        else:
            color = (1.0, 1.0, 1.0)
        ax.text(x, y, fmt % value, ha="center", va="center", color=color, **kw)


parser = argparse.ArgumentParser(description="Script to plot the correlation")
parser.add_argument("-p", "--paths", nargs="*", required=True, help="Path to the files")
parser.add_argument("-s", "--save", default=None, required=False, help="Path to print the results to")
parser.add_argument("-a", "--all", default=False, required=False,
                    help="Compare every value or only the correspondent ones")
result = parser.parse_args(sys.argv[1:])

correlation(result.paths, result.save, result.all)
