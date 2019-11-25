import argparse
import numpy as np
import pandas as pd
import sys
from matplotlib import style
import matplotlib.pyplot as plt

style.use("ggplot")
from sklearn import svm
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA
from sklearn.preprocessing import StandardScaler

def plot_svm(path, orientation1, orientation2, kernel, c):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values
    scaler = StandardScaler()
    X_norm = scaler.fit_transform(X)

    lda = LDA(n_components=2)
    lda_transformed = lda.fit_transform(X_norm, Y)

    new_x = lda_transformed[np.logical_or(Y == orientation1, Y == orientation2)]
    new_y = Y[np.logical_or(Y == orientation1, Y == orientation2)]

    clf = svm.SVC(kernel=kernel, C=c)
    clf.fit(new_x, new_y)

    xsvm = pd.DataFrame(new_x)
    plt.scatter(xsvm[new_y == orientation1][0], xsvm[new_y == orientation1][1], label=orientation1, c='red')
    plt.scatter(xsvm[new_y == orientation2][0], xsvm[new_y == orientation2][1], label=orientation2, c='green')

    ax = plt.gca()
    xlim = ax.get_xlim()
    ylim = ax.get_ylim()

    # create grid to evaluate model
    xx = np.linspace(xlim[0], xlim[1], 30)
    yy = np.linspace(ylim[0], ylim[1], 30)
    YY, XX = np.meshgrid(yy, xx)
    xy = np.vstack([XX.ravel(), YY.ravel()]).T
    Z = clf.decision_function(xy).reshape(XX.shape)

    # plot decision boundary and margins
    ax.contour(XX, YY, Z, colors='k', levels=[-1, 0, 1], alpha=0.5,
               linestyles=['--', '-', '--'])
    # plot support vectors
    ax.scatter(clf.support_vectors_[:, 0], clf.support_vectors_[:, 1], s=100,
               linewidth=1, facecolors='none')
    plt.legend()
    plt.show()

parser = argparse.ArgumentParser(description="Script to plot the svm")
parser.add_argument("-p", "--path", nargs='*', required=True, help="Path to the file with the features")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=[0, 90],
                    help="Orientation to plot svm for. Default is 0 and 90")
parser.add_argument("-k", "--kernel", required=False, default='linear', help="Kernel type")
parser.add_argument("-c", required=False, default=1, type=float, help="Kernel type")
result = parser.parse_args(sys.argv[1:])
print(result)
plot_svm(result.path[0], result.orientation[0], result.orientation[1], result.kernel, result.c)