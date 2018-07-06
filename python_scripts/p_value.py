import argparse
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from matplotlib import style
from sklearn.model_selection import StratifiedKFold
from sklearn.model_selection import permutation_test_score
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC

style.use("ggplot")


def compute_p_value(path, orientations, repetitions, kernel, cost, gamma, degree):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values

    if orientations is not None:
        new_x = X[np.logical_or(Y == orientations[0], Y == orientations[1])]
        new_y = Y[np.logical_or(Y == orientations[0], Y == orientations[1])]
    else:
        new_x = X
        new_y = Y

    cv = StratifiedKFold(5)
    pipeline = Pipeline([('scaler', StandardScaler()), ('SVM', SVC(kernel=kernel, C=cost, gamma=gamma, degree=degree))])
    score, permutation_scores, pvalue = permutation_test_score(
        pipeline, new_x, new_y, scoring="accuracy", cv=cv, n_permutations=repetitions, n_jobs=-1)

    plt.hist(permutation_scores, 20, label='Permutation scores',
             edgecolor='black')
    ylim = plt.ylim()
    plt.plot(2 * [score], ylim, '--g', linewidth=3,
             label='Classification Score'
                   ' (pvalue %s)' % pvalue)
    plt.plot(2 * [1. / np.unique(Y).shape[0]], ylim, '--k', linewidth=3, label='Luck')

    plt.ylim(ylim)
    plt.legend()
    plt.xlabel('Score')
    plt.show()


parser = argparse.ArgumentParser(description="Script to print the performa")
parser.add_argument("-p", "--path", required=True, help="Path to the file with the features")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=None,
                    help="Orientation to plot svm for. Default is 0 and 90")
parser.add_argument("-r", "--repetitions", type=int, required=False, default=1000)
parser.add_argument("-k", "--kernel", required=False, default='linear')
parser.add_argument("-c", "--cost", type=float, required=False, default=4.0)
parser.add_argument("-k1", "--gamma", type=float, required=False, default=1.0)
parser.add_argument("-k2", "--degree", type=float, required=False, default=1.0)
result = parser.parse_args()
print(result)
compute_p_value(result.path, result.orientation, result.repetitions, result.kernel, result.cost, result.gamma,
                result.degree)
