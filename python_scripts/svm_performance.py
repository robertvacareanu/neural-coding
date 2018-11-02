import argparse
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from matplotlib import style
from sklearn import metrics
from sklearn.model_selection import train_test_split
from sklearn.pipeline import Pipeline
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC

style.use("ggplot")
from sklearn.decomposition import PCA


def svm_comparison(path, orientations, histogram, repetitions, size, pca_percent, kernel, cost, gamma, degree):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values

    if orientations is not None:
        new_x = X[np.logical_or(Y == orientations[0], Y == orientations[1])]
        new_y = Y[np.logical_or(Y == orientations[0], Y == orientations[1])]
    else:
        new_x = X
        new_y = Y

    if pca_percent is not None:
        pca = PCA(n_components=new_x.shape[1])
        pca.fit(new_x)
        var = np.cumsum(np.round(pca.explained_variance_ratio_, decimals=10) * 100)
        transformed = pca.transform(new_x)
        index = np.where(var <= pca_percent)[0][-1] + 1
        new_x = transformed[:, :index]
        print(new_x)
        print(new_y)

    scores1 = []
    for _ in range(0, repetitions):
        X_train, X_test, y_train, y_test = train_test_split(new_x, new_y, test_size=size)
        pipeline = Pipeline(
            [('scaler', StandardScaler()), ('SVM', SVC(kernel=kernel, C=cost, gamma=gamma, degree=degree))])
        pipeline.fit(X_train, y_train)
        scores1.append(metrics.accuracy_score(y_test, pipeline.predict(X_test)))
    print(np.mean(scores1))
    print(np.std(scores1))

    if histogram:
        plt.title('SVM performance for single features')
        plt.xlabel('Accuracy')
        plt.ylabel('Total')
        plt.hist(scores1, range=[0.0, 1.0], align='mid', bins=20)
        plt.show()


parser = argparse.ArgumentParser(description="Script to print the performa")
parser.add_argument("-p", "--path", required=True, help="Path to the file with the features")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=None,
                    help="Orientation to plot svm for. Default is 0 and 90")
parser.add_argument("--histogram", action='store_true',
                    help="Plot histogram with the results")
parser.add_argument("-r", "--repetitions", type=int, required=False, default=1000)
parser.add_argument("-s", "--size", type=float, required=False, default=0.1)
parser.add_argument("-k", "--kernel", required=False, default='linear')
parser.add_argument("-c", "--cost", type=float, required=False, default=4.0)
parser.add_argument("-k1", "--gamma", type=float, required=False, default=1.0)
parser.add_argument("-k2", "--degree", type=float, required=False, default=1.0)
parser.add_argument("--pca", type=float, required=False, default=None, help="Percentage of variance to take")
result = parser.parse_args()
print(result)
svm_comparison(result.path, result.orientation, result.histogram,
               result.repetitions,
               result.size, result.pca, result.kernel, result.cost, result.gamma, result.degree)
