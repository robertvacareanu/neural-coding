import argparse
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from sklearn.decomposition import PCA

# style.use("ggplot")
from sklearn.preprocessing import StandardScaler


def plot_pca(path, orientations):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    scaler = StandardScaler()
    X_norm = scaler.fit_transform(X)
    if orientations is not None:
        Y = data.iloc[:, -1].values
        new_x = X_norm[np.logical_or(Y == orientations[0], Y == orientations[1])]
    else:
        new_x = X_norm
    print(new_x.shape)
    pca = PCA(n_components=new_x.shape[1])
    pca.fit(new_x)
    var = np.cumsum(np.round(pca.explained_variance_ratio_, decimals=6) * 100)
    plt.ylabel('% Variance Explained')
    plt.xlabel('# of Features')
    plt.title('PCA Analysis')

    plt.ylim(30, 100.5)
    plt.xlim(1, X_norm.shape[1])
    plt.style.context('seaborn-whitegrid')

    print(var)
    plt.plot(range(1, var.shape[0] + 1), var)
    plt.show()


parser = argparse.ArgumentParser(description="Script to plot the svm")
parser.add_argument("-p", "--path", nargs='*', required=True, help="Path to the file with the features")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=None,
                    help="Orientation to plot svm for. Default is 0 and 90")
result = parser.parse_args()
print(result)
plot_pca(result.path[0], result.orientation)
