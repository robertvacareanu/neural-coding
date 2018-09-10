package algorithm.extractor.value

/**
 * Created by robert on 3/21/18.
 * Maps the type [V] to type [E]
 */
interface ValueExtractor<V, E> {
    fun extractValue(values: V): E
}