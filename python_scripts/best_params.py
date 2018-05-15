import argparse
import json
import numpy as np
import pandas as pd
import warnings
from sklearn import metrics
from sklearn.decomposition import PCA
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA
from sklearn.ensemble import VotingClassifier
from sklearn.model_selection import RandomizedSearchCV
from sklearn.model_selection import train_test_split
from sklearn.neighbors import KNeighborsClassifier
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC

classifier_map = {
    'SVC': [SVC(), [{'kernel': ['rbf'],
                     'gamma': [2 ** x for x in range(-10, 10)],
                     'C': [2 ** x for x in range(-10, 10)]},
                    {'kernel': ['linear'], 'C': [2 ** x for x in range(-10, 10)]},
                    {'kernel': ['poly'], 'C': [2 ** x for x in range(-10, 10)],
                     'degree': np.linspace(1, 13, 7)}]],
    'LDA': [LDA(), [{'n_components': range(1, 7)}]],
    'Voting1': [VotingClassifier(
        estimators=[('svm', SVC()),
                    ('knn', KNeighborsClassifier())]),
        {
            'svm__kernel': ['rbf'],
            'svm__gamma': [2 ** x for x in range(-10, 10)],
            'svm__C': [2 ** x for x in range(-10, 10)],
            'knn__n_neighbors': [3, 5, 7],
        }
    ]
}


def find_best_params(path, classifier, orientations, attempts, repetitions, export, splitsize, pca_percent):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values
    scaler = StandardScaler()
    X_norm = scaler.fit_transform(X)
    if orientations is not None:
        new_x = X_norm[np.logical_or(Y == orientations[0], Y == orientations[1])]
        new_y = Y[np.logical_or(Y == orientations[0], Y == orientations[1])]
    else:
        new_x = X_norm
        new_y = Y

    if pca_percent is not None:
        pca = PCA(n_components=new_x.shape[1])
        pca.fit(new_x)
        var = np.cumsum(np.round(pca.explained_variance_ratio_, decimals=10) * 100)
        print(var)
        transformed = pca.transform(new_x)
        index = np.where(var <= pca_percent)[0][-1] + 1
        print(index)
        new_x = transformed[:, :index]
        # print(new_x)

    print(new_x.shape)
    scores = {}
    for _ in range(0, repetitions):
        print(classifier_map[classifier][1])
        X_train, X_test, y_train, y_test = train_test_split(new_x, new_y, test_size=splitsize)
        clf = RandomizedSearchCV(classifier_map[classifier][0], classifier_map[classifier][1], cv=5,
                                 scoring='accuracy', n_jobs=-1)
        clf.fit(X_train, y_train)
        name = json.dumps(clf.best_params_)
        if name in scores:
            scores[name].append(metrics.accuracy_score(y_test, clf.predict(X_test)))
        else:
            scores[name] = []
            scores[name].append(metrics.accuracy_score(y_test, clf.predict(X_test)))

    res = [(x, sum(scores[x]) / float(len(scores[x])), str(len(scores[x]))) for x in scores]

    res.sort(key=lambda kk: (kk[1], kk[2]), reverse=True)

    if export is None:
        if attempts:
            print(res)
        else:
            print(res[0])
            how_many = int(res[0][2])
            current = 1
            while how_many < repetitions / 2:
                print(res[current])
                how_many += int(res[current][2])
                current += 1
    else:
        if orientations is not None:
            orientation_string = str(orientations[0]) + ' ' + str(orientations[1])
        else:
            orientation_string = 'all'
        with open(export, "a") as file:
            file.write('For file: ')
            file.write(path + ' with ' + str(repetitions) + ' repetitions, ' + str(
                splitsize) + ' split size and orientations:' + orientation_string + '\n')
            for z in res:
                file.write('\t' + z[0] + ' score: ' + str(z[1]) + ' selected: ' + str(z[2]) + '\n')


warnings.filterwarnings(action='ignore', category=Warning)
parser = argparse.ArgumentParser(
    description="Script to find the best params for a classifier. Outputs a list with the classifier parameters and score")
parser.add_argument("-p", "--path", nargs='*', required=True, help="Path to the file with the features")
parser.add_argument("-c", "--classifier", required=False, default='SVC', help="Classifier type")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=None,
                    help="Orientation to plot svm for. Default is 0 and 90")
parser.add_argument("-r", "--repetitions", required=False, type=int, default=25,
                    help="How many times to perform the search for best params")
parser.add_argument("-a", "--attempts", required=False, action='store_true')
parser.add_argument("-e", "--export", required=False, default=None)
parser.add_argument("-s", "--splitsize", required=False, type=float, default=0.1)
parser.add_argument("--pca", type=float, required=False, default=None, help="Percentage of variance to take")
result = parser.parse_args()
if result.classifier not in classifier_map:
    parser.error("Please provide one of the expected classifiers")
print(result)
for p in result.path:
    find_best_params(p, result.classifier, result.orientation, result.attempts,
                     result.repetitions, result.export, result.splitsize, result.pca)
