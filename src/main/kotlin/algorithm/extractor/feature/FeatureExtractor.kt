package algorithm.extractor.feature

/**
 * Created by robert on 2/15/18.
 * Every implementation is expected to maintain the order
 */
interface FeatureExtractor<in Data, out Features> {
    fun extract(data: Collection<Data>): Features
}