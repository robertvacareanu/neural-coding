package algorithm.extractor.feature.strategy

/**
 * Created by robert on 8/18/18.
 */
class MinFeatureExtractor : FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray) = input.min()!!.toDouble()
}