package algorithm.extractor.feature.strategy.mean

import algorithm.extractor.feature.strategy.FeatureExtractorStrategy
import algorithm.join
import algorithm.sumAndDiv

/**
 * Created by robert on 8/17/18.
 */
class ArithmeticMeanFeatureExtractor : FeatureExtractorStrategy<FloatArray, Double> {

    override fun extract(input: FloatArray): Double = input.toList().sumAndDiv(input.count().toDouble())

//    override fun extractWeighted(input: FloatArray, weights: DoubleArray): (FloatArray) -> Double = { extract(it.join(weights) { fl, d -> fl * d } ) }

    //    override fun extractWeighted(input: FloatArray, weights: DoubleArray): Double = extract(input.join(weights) { fl, d -> fl * d })
    //            {
//        return if(input.isNotEmpty()) {
//            val nominator = input.fold(BigDecimal.ZERO) { acc, fl ->
//                acc + BigDecimal.valueOf(fl.toDouble())
//            }
//            val denominator = BigDecimal.valueOf(input.size.toDouble())
//            nominator.divide(denominator, 32, RoundingMode.HALF_UP).toDouble()
//        } else {
//            emptyValue
//        }
//    }
}