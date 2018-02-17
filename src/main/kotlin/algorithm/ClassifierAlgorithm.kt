package algorithm

/**
 * Created by robert on 2/15/18.
 */
interface ClassifierAlgorithm<in Features, out Classifier> {

    fun train(data: Features): Classifier

}