import pandas as pd
import tensorflow as tf
import time
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import StandardScaler


def get_input_fn(data_set, features, label, num_epochs=None, shuffle=True):
    #print(data_set[label].values)
    return tf.estimator.inputs.pandas_input_fn(
        x=pd.DataFrame({k: data_set[k].values for k in features}),
        y=pd.Series(data_set[label].values),
        num_epochs=num_epochs,
        shuffle=shuffle)


names = []
for k in range(0, 25):
    names.append("unit_%s" % k)
names.append("orientation")

features = names[:25]
label = names[-1]


data = pd.read_csv('SSD_FIRE_RATE_BTW_M017_C100_001.csv', names=names)

scaler = StandardScaler()
scaler.fit(data.drop('orientation', axis=1))
scaled_features = scaler.fit_transform(data.drop('orientation', axis=1))
df_feat = pd.DataFrame(scaled_features, columns=data.columns[:-1])

x = data
y = data['orientation']
y = divmod(y, 45)[0]
x['orientation'] = pd.Series(y, index=x.index)

# split data into training and test
X_train, X_test = train_test_split(x, test_size=0.3)

start_time = time.time()

feature_cols = [tf.feature_column.numeric_column(k) for k in features]
classifier = tf.estimator.DNNClassifier(hidden_units=[16, 16, 16, 16],
                                        n_classes=8,
                                        feature_columns=feature_cols,
                                        model_dir="C:\school\licenta\python_in_science\Deep-Learning\model")


classifier.train(input_fn=get_input_fn(X_train, features, label), steps=5000)

print("--- %d seconds ---" % (time.time() - start_time))

evaluate = classifier.evaluate(input_fn=get_input_fn(X_test, features, label, num_epochs=1, shuffle=False))
print("Test Accuracy: {0:f}".format(evaluate["accuracy"]))
print("Loss: {0:f}".format(evaluate["loss"]))
