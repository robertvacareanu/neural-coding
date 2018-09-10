package algorithm.extractor.feature.strategy.mean

import algorithm.extractor.feature.strategy.FeatureExtractorStrategy
import java.math.BigDecimal
import kotlin.math.pow

/**
 * Created by robert on 8/18/18.
 */
class GeometricMeanFeatureExtractor : FeatureExtractorStrategy<FloatArray, Double> {

    override fun extract(input: FloatArray): Double = input.fold(BigDecimal.ONE) { acc, fl -> acc * fl.toDouble().pow((1.0 / input.size)).toBigDecimal() }.toDouble()
//    override fun extractWeighted(input: FloatArray, weights: DoubleArray): Double = 0.0
    //    {
//            var result = 1.0
//            (0 until input.size).forEach {
//                result *= input[it].toDouble().pow(1.0 / input.size)
//            }
//    }
}