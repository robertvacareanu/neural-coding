import matplotlib.pyplot as plt
import pandas as pd
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA
import sys


def apply_lda(data_file):
    banknotes_data = pd.read_csv(data_file)
    x = banknotes_data.iloc[:, :-1].values
    y = banknotes_data.iloc[:, -1].values

    x_norm = (x - x.min()) / (x.max() - x.min())

    lda = LDA(n_components=2)
    lda_transformed = pd.DataFrame(lda.fit_transform(x_norm, y))

    return y, lda_transformed


def plot_data(lda_transformed, y):
    plt.scatter(lda_transformed[y == 0][0], lda_transformed[y == 0][1], label='0', c='red')
    plt.scatter(lda_transformed[y == 45][0], lda_transformed[y == 45][1], label='45', c='blue')

    plt.title("LDA Reduction")
    plt.legend()
    plt.show()


def main(argv):

    if len(argv) == 0:
        print "No input file provided as parameter!"
    else:
        y, lda_transformed = apply_lda(argv[0])
        plot_data(lda_transformed, y)


if __name__ == "__main__":
    main(sys.argv[1:])
