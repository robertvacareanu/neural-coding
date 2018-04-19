import argparse
import matplotlib.pyplot as plt
import numpy as np
import os
import pandas as pd
import sys
from matplotlib import style
from sklearn.decomposition import PCA
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA
from sklearn.model_selection import StratifiedKFold
from sklearn.multiclass import OneVsOneClassifier

style.use("ggplot")
from sklearn import svm


def compare_svm(paths, orientation1, orientation2, save_path, aliases, lda_comp, pca_comp, title, c, samplingRate=32000):

    if aliases is None:
        text = paths
    else:
        text = aliases


    # colors = []
    fig = plt.figure()
    ax1 = fig.add_subplot(111)
    for index, path in enumerate(paths):
        alabala=0
        for dirpath, _, file_paths in os.walk(path):
            label_names = []
            s = []
            t = []
            file_paths.sort(lambda x, y: cmp(int(x.split('_')[-1]), int(y.split('_')[-1])))
            for fp in file_paths:
                file_path = os.path.join(dirpath, fp)
                data = pd.read_csv(file_path, header=None)
                raw_data = data.iloc[:, :-1].values
                Y = data.iloc[:, -1].values

                if raw_data.shape[1] != 1:
                    if lda_comp is not None:
                        lda = LDA(n_components=lda_comp)
                        X = lda.fit_transform(raw_data, Y)
                    elif pca_comp is not None:
                        pca = PCA(n_components=pca_comp)
                        X = pca.fit(raw_data)
                    else:
                        X = raw_data

                    interval_splits = [int(y) for y in file_path.split('_')[-2:]]
                    for i in range(0, len(interval_splits), 2):
                        first = interval_splits[i]/float(samplingRate)
                        second = interval_splits[i + 1]/float(samplingRate)
                        if interval_splits[i+1] == 85507:
                            label_names.append(''.join([str('{0:.3f}'.format(first)), 's', '\n-\n', str('{0:.3f}'.format(second)), 's', '\nStim OFF']))
                        elif interval_splits[i] == 0:
                            label_names.append(''.join([str('{0:.3f}'.format(first)), 's', '\n-\n', str('{0:.3f}'.format(second)), 's', '\nStim ON']))
                        elif interval_splits[i+1] == 101539:
                            label_names.append(''.join([str('{0:.3f}'.format(first)), 's', '\n-\n', str('{0:.3f}'.format(second)), 's', '\nTrial END']))
                        else:
                            label_names.append(''.join([str('{0:.3f}'.format(first)), 's', '\n-\n', str('{0:.3f}'.format(second)), 's']))

                    X_norm = (X - X.min()) / (X.max() - X.min())
                    new_x = X_norm[np.logical_or(Y == orientation1, Y == orientation2)]
                    new_y = Y[np.logical_or(Y == orientation1, Y == orientation2)]

                    score = 0
                    time = 0
                    skf = StratifiedKFold(n_splits=2, shuffle=True)
                    for x in range(0, 1000):
                        for i, (train, test) in enumerate(skf.split(new_x, new_y)):
                            xtrain, xval = new_x[train], new_x[test]
                            ytrain, yval = new_y[train], new_y[test]
                            clf = OneVsOneClassifier(svm.NuSVC(kernel='linear'))
                            clf.fit(xtrain, ytrain)
                            time += 1
                            score += clf.score(xval, yval)

                    s.append(score / time)
                    t.append(alabala)

            if c is None:
                color = np.random.rand(3)
                ax1.plot(label_names, s, marker='o', label=text[index], color=color)
            else:
                ax1.plot(label_names, s, marker='o', label=text[index], color=c[index])


    plt.xlabel("Data set number")
    plt.ylabel("Accuracy")
    ax1.set_ylim(0.3, 1.0)

    plt.legend()
    fig.set_size_inches(28, 12, forward=True)
    if title is not None:
        ax1.set_title(title)
    if save_path is not None:
        plt.savefig(save_path)
    else:
        plt.show()


parser = argparse.ArgumentParser(description="Script to plot the results of applying svm to the provided files")
parser.add_argument("-p", "--path", required=True, nargs="*", help="Path to the folders containing with the features")
parser.add_argument("-a", "--alias", required=False, default=None, nargs="*",
                    help="Alias for the path files. Match in order")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=[0, 90],
                    help="Orientation to plot svm for. Default is 0 and 90")
parser.add_argument("-s", "--save", required=False, default=None,
                    help="Path to save the plot. By default only displays the plot")
parser.add_argument("-c", "--color", nargs="*", required=False, default=['red', 'blue', 'green', 'yellow'],
                    help="Colors to plot the points corresponding to the two orientations. Defaults are red and blue")
parser.add_argument("--pca", required=False, type=int, default=None, help="Apply PCA or not")
parser.add_argument("--lda", required=False, type=int, default=None, help="Apply LDA or not")
parser.add_argument("-t", "--title", required=False, default=None, help="Title of the plot")

result = parser.parse_args(sys.argv[1:])
compare_svm(result.path, result.orientation[0], result.orientation[1], result.save, result.alias, result.lda,
            result.pca, result.title, result.color)
