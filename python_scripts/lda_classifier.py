import argparse
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from matplotlib import style
from sklearn import metrics
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

style.use("ggplot")
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA


def lda_classifier(path, orientation1, orientation2, histogram, repetitions, size):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values
    scaler = StandardScaler()
    X_norm = scaler.fit_transform(X)

    new_x = X_norm[np.logical_or(Y == orientation1, Y == orientation2)]
    new_y = Y[np.logical_or(Y == orientation1, Y == orientation2)]

    scores = []
    for _ in range(0, repetitions):
        X_train, X_test, y_train, y_test = train_test_split(new_x, new_y, test_size=size)

        lda = LDA()
        lda.fit(X_train, y_train)

        scores.append(metrics.accuracy_score(y_test, lda.predict(X_test)))

    print(np.mean(scores), np.std(scores))

    if histogram:
        plt.title(path)
        plt.hist(scores, range=[0.0, 1.0], align='mid', bins='auto')
        plt.show()


parser = argparse.ArgumentParser(description="Script to print the performa")
parser.add_argument("-p", "--path", required=True, help="Path to the file with the features")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=[0, 90],
                    help="Orientation to plot svm for. Default is 0 and 90")
parser.add_argument("-r", "--repetitions", type=int, required=False, default=1000)
parser.add_argument("-s", "--size", type=float, required=False, default=0.2)
parser.add_argument("--histogram", action='store_true',
                    help="Plot histogram with the results")
result = parser.parse_args()
lda_classifier(result.path, result.orientation[0], result.orientation[1], result.histogram, result.repetitions,
               result.size)
