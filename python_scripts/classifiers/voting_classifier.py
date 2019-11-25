import argparse
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import warnings
from sklearn import metrics
from sklearn.ensemble import VotingClassifier
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA


def voting_classifier(path, orientations, histogram, repetitions, size):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values
    scaler = StandardScaler()
    X_norm = scaler.fit_transform(X)

    if orientations is not None:
        new_x = X_norm[np.logical_or(Y == orientations[0], Y == orientations[1])]
        new_y = Y[np.logical_or(Y == orientations[0], Y == orientations[1])]
    else:
        new_x = X_norm
        new_y = Y
    scores = []
    for _ in range(0, repetitions):
        X_train, X_test, y_train, y_test = train_test_split(new_x, new_y, test_size=size)

        eclf = VotingClassifier(
            estimators=[('knn', KNeighborsClassifier(n_neighbors=7)),
                        ('svc', SVC(kernel='rbf', gamma=0.00390625, C=16)),
                        ],
            flatten_transform=True)

        eclf.fit(X_train, y_train)
        scores.append(metrics.accuracy_score(y_test, eclf.predict(X_test)))

    print(np.mean(scores), np.std(scores))

    if histogram:
        plt.title(path)
        plt.hist(scores, range=[0.0, 1.0], align='mid', bins='auto')
        plt.show()


warnings.filterwarnings(action='ignore', category=DeprecationWarning)
parser = argparse.ArgumentParser(description="Script to print the performa")
parser.add_argument("-p", "--path", required=True, help="Path to the file with the features")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=None,
                    help="Orientation to plot svm for. Default is 0 and 90")
parser.add_argument("-r", "--repetitions", type=int, required=False, default=1000)
parser.add_argument("-s", "--size", type=float, required=False, default=0.2)
parser.add_argument("--histogram", action='store_true',
                    help="Plot histogram with the results")
result = parser.parse_args()
print(result)
voting_classifier(result.path, result.orientation, result.histogram, result.repetitions,
                  result.size)
