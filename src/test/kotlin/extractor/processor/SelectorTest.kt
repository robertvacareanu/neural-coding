package extractor.processor

import algorithm.processor.*
import main.*
import model.Spike
import model.TrialData
import org.junit.Test

/**
 * Created by robert on 4/12/18.
 * Tests for functions that interacts with the structure of the data: units, trials or trials data
 */
class SelectorTest {

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
    fun removeTrialsTest() {
        val features = listOf(
                Pair(1, floatArrayOf(0f, 0f, 0f, 4f, 0f, 6f, 7f, 0f, 9f, 0f)),
                Pair(2, floatArrayOf(1f, 0f, 3f, 0f, 5f, 0f, 7f, 8f, 9f, 10f)),
                Pair(3, floatArrayOf(11f, 12f, 13f, 14f, 15f, 16f, 17f, 18f, 19f, 20f)),
                Pair(4, floatArrayOf(21f, 22f, 23f, 24f, 25f, 26f, 27f, 28f, 29f, 30f)),
                Pair(5, floatArrayOf(31f, 32f, 33f, 34f, 35f, 36f, 37f, 38f, 39f, 40f)),
                Pair(6, floatArrayOf(41f, 42f, 43f, 44f, 45f, 46f, 47f, 48f, 49f, 50f))
        )
        val result1 = removeTrials(features, 5)

        assert(result1.size == 5)
        assert(result1.numberOfUnits == 10)
        assert(result1[0][0] almostEqual 1)
        assert(result1[0][2] almostEqual 3)
        assert(result1[0][4] almostEqual 5)
        assert(result1[1][1] almostEqual 12)
        assert(result1[1][3] almostEqual 14)
        assert(result1[1][4] almostEqual 15)
        assert(result1[2][1] almostEqual 22)
        assert(result1[2][3] almostEqual 24)
        assert(result1[2][4] almostEqual 25)

        val result2 = removeTrials(features, listOf(0, 1, 2, 3))
        assert(result2.size == 2)
        assert(result2.numberOfUnits == 10)
        assert(result2[0][0] almostEqual 31)
        assert(result2[0][1] almostEqual 32)
        assert(result2[0][2] almostEqual 33)
        assert(result2[1][0] almostEqual 41)
        assert(result2[1][1] almostEqual 42)
        assert(result2[1][2] almostEqual 43)
        assert(result2[1][9] almostEqual 50)

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
        assert(result.size == dataSet.size)
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
    fun removeTrialsWithLeastFeaturesTest() {
        val features = listOf(
                Pair(1, floatArrayOf(0f, 0f, 0f, 4f, 0f, 6f, 7f, 0f, 9f, 0f)),
                Pair(2, floatArrayOf(1f, 0f, 3f, 0f, 5f, 0f, 7f, 8f, 9f, 10f)),
                Pair(3, floatArrayOf(11f, 12f, 13f, 14f, 15f, 16f, 17f, 18f, 19f, 20f)),
                Pair(4, floatArrayOf(21f, 22f, 23f, 24f, 25f, 26f, 27f, 28f, 29f, 30f)),
                Pair(5, floatArrayOf(31f, 32f, 33f, 34f, 35f, 36f, 37f, 38f, 39f, 40f)),
                Pair(6, floatArrayOf(41f, 42f, 43f, 44f, 45f, 46f, 47f, 48f, 49f, 50f))
        )
        val result = removeTrialsWithLeastFeatures(features, 2)
        assert(result.numberOfUnits == 10)
        assert(result.size == 4)
        assert(result.contains(features[2]))
        assert(result.contains(features[3]))
        assert(result.contains(features[4]))
        assert(result.contains(features[5]))
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
        val result = filterDataSet(dataSet, listOf<DataPoint.() -> Boolean>({ orientation == 0 }))
        assert(result.size == 3)

    }


}