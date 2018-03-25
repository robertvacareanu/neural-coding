import argparse
import sys

import matplotlib.pyplot as plt
import pandas as pd
from matplotlib import style
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA

style.use("ggplot")


def lda(path, orientation1, orientation2, color1, color2, save_path):
    data = pd.read_csv(path, header=None)
    x = data.iloc[:, :-1].values
    y = data.iloc[:, -1].values

    x_norm = (x - x.min()) / (x.max() - x.min())

    lda = LDA(n_components=2)
    lda_transformed = pd.DataFrame(lda.fit_transform(x_norm, y))

    plt.scatter(lda_transformed[y == orientation1][0], lda_transformed[y == orientation1][1], label=orientation1,
                c=color1)
    plt.scatter(lda_transformed[y == orientation2][0], lda_transformed[y == orientation2][1], label=orientation2,
                c=color2)

    plt.legend()
    if save_path is not None:
        plt.savefig(save_path)
    else:
        plt.show()


parser = argparse.ArgumentParser(description="Script to plot svm after applying lda to data")
parser.add_argument("-p", "--path", required=True, help="Path to the file with the features")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=[0, 45],
                    help="Orientation to plot lda for. Default is 0 and 45")
parser.add_argument("-c", "--color", nargs="*", required=False, default=['red', 'blue'],
                    help="Colors to plot the points corresponding to the two orientations. Defaults are red and blue")
parser.add_argument("-s", "--save", required=False, default=None,
                    help="Path to save the plot. By default only displays the plot")

result = parser.parse_args(sys.argv[1:])
lda(result.path, result.orientation[0], result.orientation[1], result.color[0], result.color[1], result.save)
