package extractor.processor

import algorithm.processor.removeIfEmpty
import algorithm.processor.removeTrials
import main.almostEqual
import main.get
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

        assert(result[0].second.size == 2)
        assert(result[1].second.size == 2)
        assert(result[2].second.size == 2)
        assert(result[3].second.size == 2)
        assert(result[4].second.size == 2)
        assert(result[5].second.size == 2)

    }

    @Test
    fun removeTrialsTest() {
        val features = listOf(
                Pair(1, floatArrayOf(0f, 0f, 0f, 4f, 0f, 6f, 7f, 0f, 9f, 0f)),
                Pair(2, floatArrayOf(1f, 0f, 3f, 0f, 5f, 0f, 7f, 8f, 9f, 10f)),
                Pair(3, floatArrayOf(11f, 12f, 13f, 14f, 15f, 16f, 17f, 18f, 19f, 20f)),
                Pair(4, floatArrayOf(21f, 22f, 23f, 24f, 25f, 26f, 27f, 28f, 29f, 30f)),
                Pair(5, floatArrayOf(31f, 32f, 33f, 34f, 35f, 36f, 37f, 38f, 39f, 40f)),
                Pair(6, floatArrayOf(41f, 42f, 43f, 44f, 45f, 46f, 47f, 48f, 49f, 50f))
        )
        val result = removeTrials(features, 5)

        assert(result.size == 5)
        assert(result[0][0] almostEqual 1)
        assert(result[0][1] almostEqual 3)
        assert(result[0][2] almostEqual 5)
        assert(result[1][1] almostEqual 13)
        assert(result[1][3] almostEqual 17)
        assert(result[1][4] almostEqual 18)
        assert(result[2][1] almostEqual 23)
        assert(result[2][3] almostEqual 27)
        assert(result[2][4] almostEqual 28)
    }

}