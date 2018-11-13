import argparse
import pandas as pd
import numpy as np
from sklearn.preprocessing import MinMaxScaler
from sklearn.preprocessing import minmax_scale
from sklearn.preprocessing import MaxAbsScaler
from sklearn.preprocessing import StandardScaler
from sklearn.preprocessing import RobustScaler
from sklearn.preprocessing import Normalizer
from sklearn.preprocessing import QuantileTransformer
# from sklearn.preprocessing import PowerTransformer
from matplotlib import pyplot as plt


def plot_each_unit(path, orientations, save_path):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values
    scaler = StandardScaler()
    X_norm = scaler.fit_transform(X)
    print(Y)
    print(Y)
    if orientations is None:
        o = sorted(list(set(Y)))
        o.reverse()

        o1 = Y[0]
        o2 = Y[Y!=o1][0]
        if o1 < o2:
            o1, o2 = o2, o1
    else:
        o = orientations
        o = sorted(o)
        o.reverse()
        o1 = orientations[0]
        o2 = orientations[1]

    print(o)
    print(o1, o2)

    new_x = X_norm
    new_y = Y

    figure, ax = plt.subplots(nrows=new_x.shape[1], ncols=1, figsize=(15, 200), dpi=100)
    figure.tight_layout()

    needed_data = []
    for i in range(len(o)):
        needed_data.append(new_x[new_y == o[i]])

    needed_data = np.array(needed_data)
    print(needed_data)
    print(new_x.shape)
    colors = ['b', 'g', 'r', 'c', 'm', 'y', 'k', 'w']

    t1 = new_x[new_y == o1]
    t2 = new_x[new_y == o2]
    for i in range(0, new_x.shape[1]):
        for orientation in range(len(o)):
            ax[i].eventplot(needed_data[orientation][:, i], orientation='horizontal', label=o[orientation],
                            colors=colors[orientation])
        # ax[i].eventplot(t1[:, i], orientation='horizontal', label=o1,
        #                 colors='r')
        # ax[i].eventplot(t2[:, i], orientation='horizontal', label=o2,
        #                 colors='b')

    # ax[0].plot(range(10), [math.sin(x) for x in range(10)])
    # tz = figure.text(0.5,0.975,'The master title',horizontalalignment='center', verticalalignment='top')
    # tz = figure.suptitle('The master title')

    for i in range(new_x.shape[1]):
        ax[i].set_title('Unit ' + str(i))
        ax[i].set_xlabel('Amplitude')
        # ax[i].set_xlim([20, 60])
        # ax[i].set_ylim([20, 60])

    plt.legend()

    plt.subplots_adjust(left=None, bottom=None, right=None, top=None, wspace=None, hspace=0.5)
    plt.savefig(save_path)


if __name__ == "__main__":
    # global root, figure
    parser = argparse.ArgumentParser(
        description="Plot a graph with multiple axes, corresponding to the number of features. For each feature plot in its correspondent axes the datapoints of both conditions with different colors")
    parser.add_argument("-p", "--path", required=True, help="Path to the file with the features")
    parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=None,
                        help="Orientation to plot svm for. Default is 0 and 90")
    parser.add_argument("-s", "--save", type=str, required=False, default=None)

    result = parser.parse_args()

    plot_each_unit(result.path, result.orientation, result.save)
