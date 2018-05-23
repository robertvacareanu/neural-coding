package extractor.processor

import algorithm.processor.balance
import algorithm.processor.filterDataset
import algorithm.processor.removeIfNotEnoughSpikes
import algorithm.processor.removeUnits
import main.*
import model.Spike
import model.TrialData
import org.junit.Test

/**
 * Created by robert on 4/12/18.
 */
class SelectorTest {

    @Test
    fun removeIfNotEnoughSpikes() {
        val trials = listOf(
                TrialData(
                        orientation = 1,
                        spikeData = listOf(
                                arrayOf(
                                        Spike(1.0, floatArrayOf()),
                                        Spike(2.0, floatArrayOf()),
                                        Spike(3.0, floatArrayOf()),
                                        Spike(4.0, floatArrayOf()),
                                        Spike(5.0, floatArrayOf()),
                                        Spike(6.0, floatArrayOf()),
                                        Spike(7.0, floatArrayOf())
                                ),
                                arrayOf(
                                        Spike(9.0, floatArrayOf()),
                                        Spike(10.0, floatArrayOf()),
                                        Spike(11.0, floatArrayOf()),
                                        Spike(12.0, floatArrayOf())
                                )
                        ),
                        extractedBetween = Pair(1.0, 3.0)
                ),
                TrialData(
                        orientation = 2,
                        spikeData = listOf(
                                arrayOf(
                                        Spike(1.0, floatArrayOf()),
                                        Spike(2.0, floatArrayOf()),
                                        Spike(3.0, floatArrayOf()),
                                        Spike(4.0, floatArrayOf())
                                ),
                                arrayOf(
                                        Spike(1.0, floatArrayOf()),
                                        Spike(2.0, floatArrayOf())
                                )
                        ),
                        extractedBetween = Pair(3.0, 6.0)
                )
        )

        var result = removeIfNotEnoughSpikes(trials, 6)
        assert(result[0].spikeData.size == 1)

        result = removeIfNotEnoughSpikes(trials, 5)
        assert(result[0].spikeData.size == 2)
    }

    @Test
    fun balanceTest() {
        val dataSet = listOf(
                Pair(0, floatArrayOf(1f, 2f, 3f)),
                Pair(45, floatArrayOf(4f, 5f, 6f)),
                Pair(90, floatArrayOf(7f, 8f, 9f)),
                Pair(0, floatArrayOf(10f, 11f, 12f)),
                Pair(45, floatArrayOf(13f, 14f, 15f)),
                Pair(90, floatArrayOf(16f, 17f, 18f)),
                Pair(0, floatArrayOf(19f, 20f, 21f))
        )
        val result = balance(dataSet)
        assert(result.size == 6)
        assert(result.count { it.orientation == 0 } == 2)
        assert(result.count { it.orientation == 45 } == 2)
        assert(result.count { it.orientation == 90 } == 2)
    }

    @Test
    fun filterDataSetTest() {
        val dataSet = listOf(
                Pair(0, floatArrayOf(1f, 2f, 3f)),
                Pair(45, floatArrayOf(4f, 5f, 6f)),
                Pair(90, floatArrayOf(7f, 8f, 9f)),
                Pair(0, floatArrayOf(10f, 11f, 12f)),
                Pair(45, floatArrayOf(13f, 14f, 15f)),
                Pair(90, floatArrayOf(16f, 17f, 18f)),
                Pair(0, floatArrayOf(19f, 20f, 21f))
        )
        val result = filterDataset(dataSet, listOf<DataPoint.() -> Boolean>({ orientation == 0 }))
        assert(result.size == 3)

    }

    @Test
    fun removeUnitsTest() {
        val dataSet = listOf(
                Pair(0, floatArrayOf(1f, 0f, 0f)),
                Pair(45, floatArrayOf(4f, 0f, 0f)),
                Pair(90, floatArrayOf(7f, 0f, 9f)),
                Pair(0, floatArrayOf(10f, 0f, 12f)),
                Pair(45, floatArrayOf(13f, 0f, 15f)),
                Pair(90, floatArrayOf(16f, 17f, 18f)),
                Pair(0, floatArrayOf(19f, 20f, 21f))
        )
        val result = removeUnits(dataSet, 5)

        assert(result.numberOfUnits == 2)
        assert(result[0][0] almostEqual 1.0)
        assert(result[0][1] almostEqual 0.0)
        assert(result[1][0] almostEqual 4.0)
        assert(result[1][1] almostEqual 0.0)
        assert(result[2][0] almostEqual 7.0)
        assert(result[2][1] almostEqual 9.0)
        assert(result[3][0] almostEqual 10.0)
        assert(result[3][1] almostEqual 12.0)
        assert(result[4][0] almostEqual 13.0)
        assert(result[4][1] almostEqual 15.0)
        assert(result[5][0] almostEqual 16.0)
        assert(result[5][1] almostEqual 18.0)
        assert(result[6][0] almostEqual 19.0)
        assert(result[6][1] almostEqual 21.0)

    }

}