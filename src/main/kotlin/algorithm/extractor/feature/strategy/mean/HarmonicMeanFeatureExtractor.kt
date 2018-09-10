package algorithm.extractor.feature.strategy.mean

import algorithm.extractor.feature.strategy.FeatureExtractorStrategy
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by robert on 8/19/18.
 */
class HarmonicMeanFeatureExtractor : FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray) = BigDecimal.valueOf(input.size.toDouble()).divide(input.map { BigDecimal.ONE.divide(BigDecimal.valueOf(it.toDouble()), 32, RoundingMode.HALF_UP) }
            .fold(BigDecimal.ZERO) { acc, bigDecimal -> bigDecimal + acc }, 32, RoundingMode.HALF_UP).toDouble()
}