package algorithm.extractor.feature.strategy.mean

import algorithm.extractor.feature.strategy.FeatureExtractorStrategy

/**
 * Created by robert on 8/30/18.
 */
interface WeightedMeanStrategy <INPUT, RESULT> : FeatureExtractorStrategy<INPUT, RESULT> {
    /**
     * The weights must sum to the size of [INPUT].
     */
    fun extractWeighted(input: INPUT, weights: DoubleArray): (INPUT) -> RESULT
}