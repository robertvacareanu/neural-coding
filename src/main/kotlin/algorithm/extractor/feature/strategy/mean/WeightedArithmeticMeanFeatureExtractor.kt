package algorithm.extractor.feature.strategy.mean

import algorithm.extractor.feature.strategy.FeatureExtractorStrategy
import algorithm.join

/**
 * Created by robert on 8/30/18.
 */
class WeightedArithmeticMeanFeatureExtractor(val weights: (Int) -> DoubleArray): FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray): Double  = input.join(weights(input.count())) { x, w -> x * w }.sumByDouble { it.toDouble() }
}