package algorithm.extractor.feature

import algorithm.extractor.feature.strategy.FeatureExtractorStrategy
import algorithm.extractor.feature.strategy.mean.ArithmeticMeanFeatureExtractor
import model.Spike

/**
 * Created by robert on 2/15/18.
 * Every implementation is expected to maintain the order of the collection
 * Transforms the list of @param [Data] into @param [Features] using a @see [FeatureExtractorStrategy], such as [ArithmeticMeanFeatureExtractor] and different rules, depending on the implementation - single value, window value
 */

interface FeatureExtractor<in Data, out Features> {
    fun extract(data: Collection<Data>, extractor: (Array<Spike>) -> Float): Features
}