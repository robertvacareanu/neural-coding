import argparse
import sys

import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from matplotlib import style
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA

style.use("ggplot")
from sklearn import svm


def plot_svm(path, orientation1, orientation2, color1, color2, save_path):
    data = pd.read_csv(path)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values
    X_norm = (X - X.min()) / (X.max() - X.min())

    lda = LDA(n_components=2)
    lda_transformed = pd.DataFrame(lda.fit_transform(X_norm, Y))
    d2 = lda.fit_transform(X_norm, Y)

    new_x = d2[np.logical_or(Y == orientation1, Y == orientation2)]
    new_y = Y[np.logical_or(Y == orientation1, Y == orientation2)]

    plt.scatter(lda_transformed[Y == orientation1][0], lda_transformed[Y == orientation1][1], label=orientation1,
                c=color1)
    plt.scatter(lda_transformed[Y == orientation2][0], lda_transformed[Y == orientation2][1], label=orientation2,
                c=color2)

    clf = svm.NuSVC(kernel='rbf')
    clf.fit(new_x, new_y)

    h = .02  # step size in the mesh
    # create a mesh to plot in
    x_min, x_max = X_norm[:, 0].min() - 1, X_norm[:, 0].max() + 1
    y_min, y_max = X_norm[:, 1].min() - 1, X_norm[:, 1].max() + 1
    xx, yy = np.meshgrid(np.arange(x_min, x_max, h),
                         np.arange(y_min, y_max, h))

    # Plot the decision boundary. For that, we will assign a color to each
    # point in the mesh [x_min, m_max]x[y_min, y_max].
    Z = clf.predict(np.c_[xx.ravel(), yy.ravel()])

    # Put the result into a color plot
    Z = Z.reshape(xx.shape)
    plt.contour(xx, yy, Z, cmap=plt.cm.Paired)

    plt.legend()
    if save_path is not None:
        plt.savefig(save_path)
    else:
        plt.show()


parser = argparse.ArgumentParser(description="Script to plot svm after applying lda to data")
parser.add_argument("-p", "--path", required=True, help="Path to the file containing the file with the features")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=[0, 45],
                    help="Orientation to plot svm for. Default is 0 and 45")
parser.add_argument("-c", "--color", nargs="*", required=False, default=['red', 'blue'],
                    help="Colors to plot the points corresponding to the two orientations")
parser.add_argument("-s", "--save", required=False, default=None,
                    help="Path to save the plot. By default only displays the plot")

result = parser.parse_args(sys.argv[1:])
plot_svm(result.path, result.orientation[0], result.orientation[1], result.color[0], result.color[1], result.save)
