package algorithm.extractor.feature.strategy.mean

import algorithm.extractor.feature.strategy.FeatureExtractorStrategy
import algorithm.join
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by robert on 8/30/18.
 */
class WeightedHarmonicMeanFeatureExtactor(val weights: (Int) -> DoubleArray) : FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray): Double = BigDecimal.ONE.divide(input.join(weights(input.count())) { x, w -> w / x }.sumByDouble { it.toDouble() }.toBigDecimal(), 32, RoundingMode.HALF_UP).toDouble()
}