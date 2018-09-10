package algorithm.extractor.feature.strategy

import algorithm.extractor.value.ValueExtractor

/**
 * Created by robert on 8/17/18.
 * Classes that implement this interface are responsible for transforming a list of [INPUT], which is generally the result of
 * applying [ValueExtractor.extractValue] to a single value [RESULT]
 */
interface FeatureExtractorStrategy<INPUT, RESULT> {
    fun extract(input: INPUT): RESULT
}