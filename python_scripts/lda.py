import argparse
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import sys
from matplotlib import style
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA
from sklearn.preprocessing import StandardScaler

style.use("ggplot")


def perform_lda(path, orientation1, orientation2, color1, color2, save_path, title):
    data = pd.read_csv(path, header=None)
    x = data.iloc[:, :-1].values
    y = data.iloc[:, -1].values
    scaler = StandardScaler()

    x_norm = scaler.fit_transform(x)
    new_x = x_norm[np.logical_or(y == orientation1, y == orientation2)]
    new_y = y[np.logical_or(y == orientation1, y == orientation2)]

    lda = LDA()
    lda_transformed = pd.DataFrame(lda.fit_transform(new_x, new_y))

    plt.eventplot(lda_transformed[new_y == orientation1][0], orientation='horizontal', label=orientation1,
                  colors=color1)
    plt.eventplot(lda_transformed[new_y == orientation2][0], orientation='horizontal', label=orientation2,
                  colors=color2)

    plt.title(title)
    plt.legend()
    if save_path is not None:
        plt.savefig(save_path)
    else:
        plt.show()


parser = argparse.ArgumentParser(description="Script to plot svm after applying lda to data")
parser.add_argument("-p", "--path", required=True, help="Path to the file with the features")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=[0, 90],
                    help="Orientation to plot lda for. Default is 0 and 90")
parser.add_argument("-c", "--color", nargs="*", required=False, default=['red', 'blue'],
                    help="Colors to plot the points corresponding to the two orientations. Defaults are red and blue")
parser.add_argument("-s", "--save", required=False, default=None,
                    help="Path to save the plot. By default only displays the plot")
parser.add_argument("-t", "--title", required=True,
                    help="Path to save the plot. By default only displays the plot")

result = parser.parse_args(sys.argv[1:])
perform_lda(result.path, result.orientation[0], result.orientation[1], result.color[0], result.color[1], result.save,
            result.title)
