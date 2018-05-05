import argparse
import json
import numpy as np
import pandas as pd

from sklearn import metrics
from sklearn.model_selection import GridSearchCV
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler
from sklearn.svm import SVC

classifier_map = {
    'SVC': [SVC(), [{'kernel': ['rbf'],
                     'gamma': [2 ** x for x in range(-10, 10)],
                     'C': [2 ** x for x in range(-10, 10)]},
                    {'kernel': ['linear'], 'C': [2 ** x for x in range(-10, 10)]},
                    {'kernel': ['poly'], 'C': [2 ** x for x in range(-10, 10)],
                     'degree': np.linspace(1, 13, 7)}]]
}


def find_best_params(path, classifier, orientation1, orientation2, attempts, repetitions):
    data = pd.read_csv(path, header=None)
    X = data.iloc[:, :-1].values
    Y = data.iloc[:, -1].values
    scaler = StandardScaler()
    X_norm = scaler.fit_transform(X)
    new_x = X_norm[np.logical_or(Y == orientation1, Y == orientation2)]
    new_y = Y[np.logical_or(Y == orientation1, Y == orientation2)]
    scores = {}
    for _ in range(0, repetitions):
        X_train, X_test, y_train, y_test = train_test_split(new_x, new_y, test_size=0.1)

        clf = GridSearchCV(classifier_map[classifier][0], classifier_map[classifier][1], cv=5,
                           scoring='accuracy')
        clf.fit(X_train, y_train)
        name = json.dumps(clf.best_params_)
        if name in scores:
            scores[name].append(metrics.accuracy_score(y_test, clf.predict(X_test)))
        else:
            scores[name] = []
            scores[name].append(metrics.accuracy_score(y_test, clf.predict(X_test)))

    res = [(x, sum(scores[x]) / float(len(scores[x])), str(len(scores[x]))) for x in scores]

    res.sort(key=lambda kk: (kk[1], kk[2]), reverse=True)
    if attempts:
        print(res)
    else:
        print(res[0])
        if int(res[0][1]) < repetitions / 2:
            print(res[1])


parser = argparse.ArgumentParser(
    description="Script to find the best params for a classifier. Outputs a list with the classifier parameters and score")
parser.add_argument("-p", "--path", required=True, help="Path to the file with the features")
parser.add_argument("-c", "--classifier", required=False, default='SVC', help="Classifier type")
parser.add_argument("-o", "--orientation", nargs="*", type=int, required=False, default=[0, 90],
                    help="Orientation to plot svm for. Default is 0 and 90")
parser.add_argument("-r", "--repetitions", required=False, type=int, default=25,
                    help="How many times to perform the search for best params")
parser.add_argument("-a", "--attempts", required=False, action='store_true')

result = parser.parse_args()
if result.classifier not in classifier_map:
    parser.error("Please provide one of the expected classifiers")
print(result)
find_best_params(result.path, result.classifier, result.orientation[0], result.orientation[1], result.attempts,
                 result.repetitions)
