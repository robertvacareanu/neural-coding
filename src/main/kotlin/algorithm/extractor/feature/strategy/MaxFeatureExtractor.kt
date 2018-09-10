package algorithm.extractor.feature.strategy

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by robert on 8/18/18.
 */
class MaxFeatureExtractor : FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray) = input.max()!!.toDouble()
}