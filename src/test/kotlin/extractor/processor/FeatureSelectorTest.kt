package extractor.processor

import algorithm.processor.removeIfEmpty
import org.junit.Test

/**
 * Created by robert on 4/12/18.
 */
class FeatureSelectorTest {

    @Test
    fun removeIfEmptyTest() {
        val features = listOf(
                Pair(1, floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7f)),
                Pair(2, floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7f)),
                Pair(3, floatArrayOf(1f, 2f, 3f, 4f, 5f, 6f, 7f)),
                Pair(1, floatArrayOf(0f, 2f, 3f, 4f, 5f, 6f, 7f)),
                Pair(2, floatArrayOf(1f, 0f, 3f, 4f, 0f, 6f, 7f)),
                Pair(3, floatArrayOf(1f, 2f, 0f, 0f, 5f, 6f, 7f))
        )
        val result = removeIfEmpty(features)
        println(result[0].second.size)
        assert(result[0].second.size == 2)
        assert(result[1].second.size == 2)
        assert(result[2].second.size == 2)
        assert(result[3].second.size == 2)
        assert(result[4].second.size == 2)
        assert(result[5].second.size == 2)

    }

}