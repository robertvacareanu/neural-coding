package algorithm.extractor.feature

import model.Spike

/**
 * Created by robert on 2/15/18.
 * Every implementation is expected to maintain the order of the collection
 */
interface FeatureExtractor<in Data, out Features> {
    fun extract(data: Collection<Data>, extractor: (Array<Spike>) -> Float): Features
}