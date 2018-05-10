import argparse
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from matplotlib import style
from sklearn import metrics
from sklearn.svm import SVC
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler

style.use("ggplot")
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA

def svm_comparison(path, orientation1, orientation2, histogram, lda):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values
    scaler = StandardScaler()
    X_norm = scaler.fit_transform(X)

    new_x = X_norm[np.logical_or(Y == orientation1, Y == orientation2)]
    new_y = Y[np.logical_or(Y == orientation1, Y == orientation2)]

    if lda:
        lda = LDA()
        lda_transformed = pd.DataFrame(lda.fit_transform(new_x, new_y))
        new_x = np.append(new_x, lda_transformed, axis=1)

    scores1 = []
    scores2 = []
    for _ in range(0, 1000):
        X_train, X_test, y_train, y_test = train_test_split(new_x, new_y, test_size=0.1)
        classifier = SVC(kernel='rbf', gamma=0.03125, C=2)
        classifier.fit(X_train, y_train)
        scores1.append(metrics.accuracy_score(y_test, classifier.predict(X_test)))
        scores2.append(metrics.accuracy_score(new_y, classifier.predict(new_x)))
    print(np.mean(scores1), np.mean(scores2))
    print(np.std(scores1), np.std(scores2))
    print(np.min(scores1), scores1.count(np.min(scores1)), np.max(scores1), scores1.count(np.max(scores1)))
    print(np.min(scores2), np.max(scores2))

    if histogram:
        plt.title(path)
        plt.hist(scores1, range=[0.0, 1.0], align='mid', bins=20)
        plt.show()


parser = argparse.ArgumentParser(description="Script to print the performa")
parser.add_argument("-p", "--path", required=True, help="Path to the file with the features")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=[0, 90],
                    help="Orientation to plot svm for. Default is 0 and 90")
parser.add_argument("--histogram", action='store_true',
                    help="Plot histogram with the results")
parser.add_argument("-l", "--lda", required=False, action='store_true', help="Apply lda on data")
result = parser.parse_args()
svm_comparison(result.path, result.orientation[0], result.orientation[1], result.histogram, result.lda)
