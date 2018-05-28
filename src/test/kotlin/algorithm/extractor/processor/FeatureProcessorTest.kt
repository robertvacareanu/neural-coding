package algorithm.extractor.processor

import algorithm.processor.merge
import algorithm.processor.normalize
import main.DataPoint
import main.almostEqual
import main.get
import org.junit.Test

/**
 * Created by robert on 5/2/18.
 * Tests for feature processor functions
 */
class FeatureProcessorTest {
    @Test
    fun normalizeTest() {
        val data = listOf(
                DataPoint(1, floatArrayOf(70.97912f, 72.09378f, 64.28458f, 61.3477f, 60.554615f)),
                DataPoint(90, floatArrayOf(62.117016f, 76.14182f, 59.800716f, 67.48625f, 58.949074f)),
                DataPoint(45, floatArrayOf(73.384834f, 65.63082f, 63.39528f, 72.93173f, 62.8203f))
        )

        val result = normalize(data)

        assert(result[0][0] almostEqual 0.786496)
        assert(result[1][0] almostEqual 0)
        assert(result[2][0] almostEqual 1)

        assert(result[0][1] almostEqual 0.61487584)
        assert(result[1][1] almostEqual 1)
        assert(result[2][1] almostEqual 0)

        assert(result[0][2] almostEqual 1)
        assert(result[1][2] almostEqual 0)
        assert(result[2][2] almostEqual 0.8016666)

        assert(result[0][3] almostEqual 0)
        assert(result[1][3] almostEqual 0.52991489)
        assert(result[2][3] almostEqual 1)

        assert(result[0][4] almostEqual 0.41473709)
        assert(result[1][4] almostEqual 0)
        assert(result[2][4] almostEqual 1)

    }

    @Test
    fun mergeTest() {
        val dataSets = listOf(
                listOf(
                        DataPoint(1, floatArrayOf(1.0f, 2.0f, 3.0f)),
                        DataPoint(2, floatArrayOf(4.0f, 5.0f, 6.0f)),
                        DataPoint(3, floatArrayOf(7.0f, 8.0f, 3.0f)),
                        DataPoint(4, floatArrayOf(10.0f, 11.0f, 12.0f)),
                        DataPoint(5, floatArrayOf(13.0f, 14.0f, 15.0f))
                ),
                listOf(
                        DataPoint(1, floatArrayOf(14.0f, 15.0f, 16.0f)),
                        DataPoint(2, floatArrayOf(17.0f, 18.0f, 19.0f)),
                        DataPoint(3, floatArrayOf(20.0f, 21.0f, 22.0f)),
                        DataPoint(4, floatArrayOf(23.0f, 24.0f, 25.0f)),
                        DataPoint(5, floatArrayOf(26.0f, 27.0f, 28.0f))
                ),
                listOf(
                        DataPoint(1, floatArrayOf(29.0f, 30.0f, 31.0f)),
                        DataPoint(2, floatArrayOf(32.0f, 33.0f, 34.0f)),
                        DataPoint(3, floatArrayOf(35.0f, 36.0f, 37.0f)),
                        DataPoint(4, floatArrayOf(38.0f, 39.0f, 40.0f)),
                        DataPoint(5, floatArrayOf(41.0f, 42.0f, 43.0f))
                )
        )

        val mergeResult = merge(dataSets)

        assert(mergeResult[0][0] almostEqual 44.0f)
        assert(mergeResult[1][1] almostEqual 56.0f)
        assert(mergeResult[4][2] almostEqual 86.0f)
        assert(mergeResult[0][2] almostEqual 50.0f)

    }

}