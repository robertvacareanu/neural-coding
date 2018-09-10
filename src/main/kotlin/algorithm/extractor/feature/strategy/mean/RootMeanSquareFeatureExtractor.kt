package algorithm.extractor.feature.strategy.mean

import algorithm.extractor.feature.strategy.FeatureExtractorStrategy
import java.lang.Math.sqrt

/**
 * Created by robert on 8/19/18.
 */
class RootMeanSquareFeatureExtractor : FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray) = if(input.isNotEmpty()) sqrt(input.map { it.toDouble() * it.toDouble() }.sum().div(input.size)) else 0.0

}