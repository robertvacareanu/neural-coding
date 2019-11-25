import argparse
import math
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from matplotlib import style
from sklearn import metrics
from sklearn.model_selection import train_test_split

style.use("ggplot")
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA


def lda_c(path, orientation1, orientation2, histogram, repetitions, size):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values

    if X.shape[1] == 1 and math.isnan(X[0][0]):
        # print "Only NaN"
        return

    if orientation1 is None or orientation2 is None:
        new_x = X[np.logical_or(Y==225, Y==180)]
        new_x = new_x[:, [12,48]]
        new_y = Y[np.logical_or(Y==225, Y==180)]

    else:
        new_x = X[
            np.logical_or(Y == orientation1, Y == orientation2)
        ]
        new_y = Y[
            np.logical_or(Y == orientation1, Y == orientation2)
        ]


    # print(new_x.shape)
    # print(new_y)
    scores = []
    for _ in range(0, repetitions):
        X_train, X_test, y_train, y_test = train_test_split(new_x, new_y, test_size=size)

        lda = LDA(solver='lsqr', shrinkage='auto')
        lda.fit(X_train, y_train)

        scores.append(metrics.accuracy_score(y_test, lda.predict(X_test)))

    # print(np.mean(scores), np.std(scores))

    if histogram:
        plt.title(path)
        plt.hist(scores, range=[0.0, 1.0], align='mid', bins='auto')
        plt.show()

    return np.mean(scores), np.std(scores)


if __name__ == "__main__":

    parser = argparse.ArgumentParser(description="Script to print the performa")
    parser.add_argument("-p", "--path", required=True, help="Path to the file with the features")
    parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=[None, None],
                        help="Orientation to plot svm for. Default is 0 and 90")
    parser.add_argument("-r", "--repetitions", type=int, required=False, default=10000)
    parser.add_argument("-s", "--size", type=float, required=False, default=0.2)
    parser.add_argument("--histogram", action='store_true',
                        help="Plot histogram with the results")
    result = parser.parse_args()
    print(result)
    print(lda_c(result.path, result.orientation[0], result.orientation[1], result.histogram, result.repetitions,
                   result.size))
