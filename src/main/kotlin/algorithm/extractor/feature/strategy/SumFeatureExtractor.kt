package algorithm.extractor.feature.strategy

/**
 * Created by robert on 8/23/18.
 */
class SumFeatureExtractor : FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray) = input.sumByDouble { it.toDouble() }
}