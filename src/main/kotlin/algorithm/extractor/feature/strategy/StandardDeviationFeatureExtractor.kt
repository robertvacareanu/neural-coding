package algorithm.extractor.feature.strategy

import algorithm.std

/**
 * Created by robert on 8/18/18.
 */
class StandardDeviationFeatureExtractor : FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray) = input.toList().std()
}