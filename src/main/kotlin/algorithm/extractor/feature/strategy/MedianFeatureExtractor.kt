package algorithm.extractor.feature.strategy

import algorithm.median

/**
 * Created by robert on 8/18/18.
 */
class MedianFeatureExtractor : FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray) = input.toList().median()
}