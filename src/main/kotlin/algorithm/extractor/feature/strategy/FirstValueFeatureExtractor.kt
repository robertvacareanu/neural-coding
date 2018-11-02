package algorithm.extractor.feature.strategy

/**
 * Created by robert on 8/23/18.
 */
class FirstValueFeatureExtractor : FeatureExtractorStrategy<FloatArray, Double> {
    override fun extract(input: FloatArray) = input.first().toDouble()
}