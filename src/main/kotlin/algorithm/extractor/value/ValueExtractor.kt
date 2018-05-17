package algorithm.extractor.value

/**
 * Created by robert on 3/21/18.
 * Maps an array of type [V] to an element of type [E]
 */
interface ValueExtractor<V, E> {
    fun extractValue(values: Array<V>): E
}