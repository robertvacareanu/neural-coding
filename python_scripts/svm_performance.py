import argparse
import numpy as np
import pandas as pd
import sys
from matplotlib import style
from sklearn.model_selection import StratifiedKFold
from sklearn.multiclass import OneVsOneClassifier

style.use("ggplot")
from sklearn import svm


def svm_comparison(path, orientation1, orientation2):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values
    X_norm = (X - X.min()) / (X.max() - X.min())

    new_x = X_norm[np.logical_or(Y == orientation1, Y == orientation2)]
    new_y = Y[np.logical_or(Y == 0, Y == 90)]

    score = []
    skf = StratifiedKFold(n_splits=2, shuffle=True)
    for x in range(0, 1000):
        for i, (train, test) in enumerate(skf.split(new_x, new_y)):
            xtrain, xval = new_x[train], new_x[test]
            ytrain, yval = new_y[train], new_y[test]
            clf = OneVsOneClassifier(svm.NuSVC(kernel='rbf'))
            clf.fit(xtrain, ytrain)
            score.append(clf.score(xval, yval))

    print(path)
    print(np.mean(score))
    print(np.std(score))


parser = argparse.ArgumentParser(description="Script to plot svm after applying lda to data")
parser.add_argument("-p", "--path", nargs='*', required=True, help="Path to the file with the features")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=[0, 90],
                    help="Orientation to plot svm for. Default is 0 and 90")
result = parser.parse_args(sys.argv[1:])
for p in result.path:
    svm_comparison(p, result.orientation[0], result.orientation[1])
