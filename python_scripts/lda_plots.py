import matplotlib.pyplot as plt
import pandas as pd

from numpy import genfromtxt
from sklearn.discriminant_analysis import LinearDiscriminantAnalysis as LDA

data = genfromtxt('data', delimiter=',')
X = data
X_norm = (X-X.min())/(X.max()-X.min())
y = genfromtxt('results', delimiter=',')

#cols = ['c2', 'c2', 'c3', 'c4', 'c5', 'c6', 'c7', 'c8', 'c9', 'c10', 'c11', 'c12', 'c13', 'c14', 'c15', 'c16', 'c17', 'c18', 'c19', 'c20', 'c21', 'c22', 'c23', 'c24', 'c25', 'c26', 'c27', 'c28', 'c29', 'c30', 'c31', 'c32']

lda = LDA(n_components=2)
lda_transformed = pd.DataFrame(lda.fit_transform(X_norm, y))

plt.scatter(lda_transformed[y==0][0], lda_transformed[y==0][1], label='0', c='red')
plt.scatter(lda_transformed[y==1][0], lda_transformed[y==1][1], label='45', c='blue')

plt.legend()
plt.show()

