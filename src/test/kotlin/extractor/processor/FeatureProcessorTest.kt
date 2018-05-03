package extractor.processor

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
                DataPoint(1, floatArrayOf(70.97912f,72.09378f,64.28458f,61.3477f,60.554615f)),
                DataPoint(90, floatArrayOf(62.117016f,76.14182f,59.800716f,67.48625f,58.949074f)),
                DataPoint(45, floatArrayOf(73.384834f,65.63082f,63.39528f,72.93173f,62.8203f))
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
}