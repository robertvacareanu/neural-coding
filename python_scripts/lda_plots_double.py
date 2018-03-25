import matplotlib.pyplot as plt
import pandas as pd
import sys
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA


# 2 ways of plotting
# 1st - only one plot -> need 5 arguments: source_file_name.csv orientation1 orientation2 plot_title export_folder
# 2nd - two plots -> used when comparing different features -> need 10 arguments:
#                                                          source_file_name1.csv orientation1a orientation2a plot_title1
#                                                          source_file_name2.csv orientation1b orientation2b plot_title2
#                                                          export_folder file_name


def apply_lda(data_file):
    banknotes_data = pd.read_csv(data_file, header=None)
    x = banknotes_data.iloc[:, :-1].values
    y = banknotes_data.iloc[:, -1].values

    x_norm = (x - x.min()) / (x.max() - x.min())

    lda = LDA(n_components=2)
    lda_transformed = pd.DataFrame(lda.fit_transform(x_norm, y))

    return y, lda_transformed


def plot_data_double(lda_transformed1, y1, label_01, label_02, plot_title1,
                     lda_transformed2, y2, label_11, label_12, plot_title2,
                     export_folder, file_name):

    fig, axes = plt.subplots(nrows=1, ncols=2, figsize=(15, 7))

    axes[0].scatter(lda_transformed1[y1 == label_01][0], lda_transformed1[y1 == label_01][1], label=label_01, c='red')
    axes[0].scatter(lda_transformed1[y1 == label_02][0], lda_transformed1[y1 == label_02][1], label=label_02, c='blue')
    axes[0].legend()
    axes[0].set_title(plot_title1)

    axes[1].scatter(lda_transformed2[y2 == label_11][0], lda_transformed2[y2 == label_11][1], label=label_11, c='red')
    axes[1].scatter(lda_transformed2[y2 == label_12][0], lda_transformed2[y2 == label_12][1], label=label_12, c='blue')
    axes[1].legend()
    axes[1].set_title(plot_title2)
    # plt.savefig(export_folder + file_name +".png")
    plt.show()


def plot_data_simple(lda_transformed, y, label1, label2, plot_title, export_folder):

    plt.scatter(lda_transformed[y == label1][0], lda_transformed[y == label1][1], label=label1, c='red')
    plt.scatter(lda_transformed[y == label2][0], lda_transformed[y == label2][1], label=label2, c='blue')
    plt.legend()
    plt.title(plot_title)
    plt.savefig(export_folder + plot_title + ".png")
#    plt.show()


def main(argv):

    if len(argv) == 0:
        print ("Add parameters!")
    elif len(argv) == 5:
        y, lda_transformed = apply_lda(argv[0])
        plot_data_simple(lda_transformed, y, int(argv[1]), int(argv[2]), argv[3], argv[4])
    elif len(argv) == 10:
        y1, lda_transformed1 = apply_lda(argv[0])
        y2, lda_transformed2 = apply_lda(argv[4])

        plot_data_double(lda_transformed1, y1, int(argv[1]), int(argv[2]), argv[3],
                         lda_transformed2, y2, int(argv[5]), int(argv[6]), argv[7],
                         argv[8], argv[9])


if __name__ == "__main__":
    main(sys.argv[1:])
