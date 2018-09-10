package algorithm.extractor.feature.strategy.mean

import algorithm.extractor.feature.strategy.FeatureExtractorStrategy
import ch.obermuhlner.math.big.BigDecimalMath
import java.math.BigDecimal
import java.math.MathContext

/**
 * Created by robert on 8/30/18.
 */
class WeightedGeometricMeanFeatureExtractor(val weightsGenerator: (Int) -> DoubleArray) : FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray): Double {
        val weights = weightsGenerator(input.size)
        return input.mapIndexed { index, fl -> BigDecimalMath.pow(fl.toBigDecimal(), weights[index].toBigDecimal(), MathContext.DECIMAL128) }.fold(BigDecimal.ONE) { acc, fl -> acc * fl }.toDouble()
    }
}