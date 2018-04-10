import argparse
import sys

import numpy as np
import pandas as pd
from scipy.stats.stats import pearsonr


def chance_level(paths, orientations):
    data = pd.read_csv(paths[0], header=None)
    x1 = data.iloc[:, :-1].values
    y1 = data.iloc[:, -1].values

    data = pd.read_csv(paths[1], header=None)
    x2 = data.iloc[:, :-1].values
    y2 = data.iloc[:, -1].values

    x1_n = (x1 - x1.min()) / (x1.max() - x1.min())
    x2_n = (x2 - x2.min()) / (x2.max() - x2.min())

    print(x2.shape)

    results = []
    for x in range(x1_n.shape[1]):
        results.append([])

    for o1 in orientations:
        for o2 in orientations:
            if o1 != o2:
                for x in range(x1_n.shape[1]):
                    results[x].append(pearsonr(x1_n[y1 == o1][:, x], x2_n[y2 == o2][:, x])[0])

    for c in range(0, len(results)):
        print(c + 1, 'Mean: ', np.mean(results[c], axis=0), 'Standard deviation: ', np.std(results[c], axis=0))


parser = argparse.ArgumentParser(description="Script to plot the correlation")
parser.add_argument("-p", "--paths", nargs="*", required=True, help="Path to the files")
parser.add_argument("-o", "--orientations", nargs="*", required=False, type=int,
                    default=[0, 45, 90, 135, 180, 225, 270, 315])
result = parser.parse_args(sys.argv[1:])

chance_level(result.paths, result.orientations)
