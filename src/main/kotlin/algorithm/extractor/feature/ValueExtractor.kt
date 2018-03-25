package algorithm.extractor.feature

/**
 * Created by robert on 3/21/18.
 * While SingleValue and MultiValue
 */
interface ValueExtractor<V, E> {
    fun extractValue(values: Array<V>): E
}