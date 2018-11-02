package algorithm.extractor.feature.strategy

/**
 * Created by robert on 8/18/18.
 */
class MaxFeatureExtractor : FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray) = input.max()!!.toDouble()
}