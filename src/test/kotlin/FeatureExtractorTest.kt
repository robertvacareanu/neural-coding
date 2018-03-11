import algorithm.extractor.feature.*
import model.Spike
import model.TrialData
import org.junit.Test
import java.awt.geom.Point2D
import kotlin.math.abs

/**
 * Created by robert on 2/19/18.
 * Tests for the functionality inside algorithm.extractor.feature
 */
class FeatureExtractorTest {

    private infix fun Number.almostEqual(number: Number): Boolean {
        return abs(this.toDouble() - number.toDouble()) < 0.00001
    }

    private fun constructTrialDataMock(): List<TrialData> {
        //Construct some test data
        val trialData = mutableListOf<TrialData>()

        val t1Channel1Data = arrayOf(
                Spike(1.0, floatArrayOf(0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f)),
                Spike(2.0, floatArrayOf(0.0f, 10.0f, 20.0f, 30.0f, 40.0f, 50.0f, 60.0f, 70.0f, 80.0f, 90.0f)),
                Spike(3.0, floatArrayOf(10.0f, 20.0f, 50.0f, 65.0f, 74.1f, 74.1f, 86.0f, 22.0f, 23.0f, 20.0f))
        )
        val t1Channel2Data = arrayOf(
                Spike(1.0, floatArrayOf(18.2f, 15.0f, 25.0f, 24.0f, 30.0f, 57.0f, 49.0f, 31.0f, 30.0f, 33.0f)),
                Spike(2.0, floatArrayOf(-4.0f, 0.0f, -13.0f, -26.0f, -24.0f, -23.0f, -37.0f, 0.0f, 2.0f, -3.0f)),
                Spike(5.0, floatArrayOf(27.0f, 73.0f, 77.0f, 65.0f, 74.1f, 58.0f, 97.0f, 35.0f, 49.0f, 57.18f))
        )
        val t1Channel3Data = arrayOf(
                Spike(1.0, floatArrayOf(18.2f, 15.0f, 25.0f, 24.0f, 30.0f, 92.12f, 49.0f, 31.0f, 30.0f, 33.0f)),
                Spike(2.0, floatArrayOf(-4.0f, 0.0f, -13.0f, -26.0f, -29.0f, -25.36f, -37.0f, 0.0f, 2.0f, -3.0f)),
                Spike(10.0, floatArrayOf(27.0f, 73.0f, 77.0f, 65.0f, 74.1f, 82.18f, 97.0f, 35.0f, 49.0f, 57.18f))
        )


        val t2Channel1Data = arrayOf(
                Spike(1.0, floatArrayOf(16.2f, 15.0f, 20.0f, 17.16f, 21.15f, 15.0f, 15.11f, 18.0f, 18.9f, 9.0f)),
                Spike(10.0, floatArrayOf(7.0f, 10.0f, 20.0f, 30.0f, 40.0f, 50.0f, 60.0f, 70.0f, 80.0f, 90.0f)),
                Spike(20.0, floatArrayOf(10.0f, 20.0f, 50.0f, 65.0f, 74.1f, 58.0f, 86.0f, 22.0f, 23.0f, 20.0f))
        )
        val t2Channel2Data = arrayOf(
                Spike(1.0, floatArrayOf(18.2f, 15.0f, 25.0f, 24.0f, 30.0f, 57.0f, 49.0f, 31.0f, 30.0f, 33.0f)),
                Spike(20.0, floatArrayOf(18.0f, 20.0f, 21.0f, 25.0f, 46.0f, 57.0f, -37.0f, 0.0f, 2.0f, -3.0f)),
                Spike(40.0, floatArrayOf(27.0f, 73.0f, 77.0f, 65.0f, 74.1f, 58.0f, 97.0f, 35.0f, 49.0f, 57.18f))
        )
        val t2Channel3Data = arrayOf(
                Spike(1.0, floatArrayOf(18.2f, 15.0f, 25.0f, 24.0f, 30.0f, 57.0f, 49.0f, 31.0f, 30.0f, 33.0f)),
                Spike(2.0, floatArrayOf(-4.0f, -10.0f, -20.0f, -31.0f, -24.0f, -34.0f, -50.0f, -9.0f, -8.0f, -9.0f)),
                Spike(10.0, floatArrayOf(27.0f, 73.0f, 77.0f, 65.11f, 84.14f, 68.13f, 107.0f, 51.15f, 75.0f, 57.18f))
        )

        trialData.add(TrialData(1, listOf(t1Channel1Data, t1Channel2Data, t1Channel3Data)))
        trialData.add(TrialData(1, listOf(t2Channel1Data, t2Channel2Data, t2Channel3Data)))

        return trialData.toList()
    }

    @Test
    fun meanAmplitude() {
        val featureExtractor = MeanAmplitude(5)


        val feature = featureExtractor.extract(constructTrialDataMock())

        assert(feature[0].first == 1)
        assert(feature[1].first == 1)

        assert(feature[0].second[0] almostEqual 43.0333333333)
        assert(feature[0].second[1] almostEqual 46)
        assert(feature[0].second[2] almostEqual 66.5533333333)
        assert(feature[1].second[0] almostEqual 41)
        assert(feature[1].second[1] almostEqual 57.3333333333)
        assert(feature[1].second[2] almostEqual 53.0433333333)
    }

    @Test
    fun spikePerSec() {
        val feature = SpikesPerSec(10.0f).extract(constructTrialDataMock())

        assert(feature[0].first == 1)
        assert(feature[1].first == 1)

        assert(feature[0].second[0] almostEqual 15.0)
        assert(feature[0].second[1] almostEqual 7.5)
        assert(feature[0].second[2] almostEqual 3.333333)
        assert(feature[1].second[0] almostEqual 1.578947368)
        assert(feature[1].second[1] almostEqual 0.769230769)
        assert(feature[1].second[2] almostEqual 3.333333)

    }

//    @Test
//    fun meanPerimeter() {
//
//        val t1Channel1Data = arrayOf(
//                Spike(1.0, floatArrayOf(0.0f, -15.0f, -12.0f, -22.0f, -31.0f)),
//                Spike(2.0, floatArrayOf(0.0f, -6.0f, -20.0f, -25.0f, -56.0f)),
//                Spike(3.0, floatArrayOf(-10.0f, -20.0f, -50.0f, -23.0f, -0.5f))
//        )
//
//        val feature: List<Pair<Int, FloatArray>> = MeanPerimeter(1f, 2).extract(listOf(TrialData(1, listOf(t1Channel1Data))))
//
//        var result = Point2D.distance(1.0, t1Channel1Data[0][0].toDouble(), 2.0, t1Channel1Data[0][1].toDouble()) + Point2D.distance(2.0, t1Channel1Data[0][1].toDouble(), 3.0, t1Channel1Data[0][2].toDouble()) +
//                Point2D.distance(3.0, t1Channel1Data[0][2].toDouble(), 4.0, t1Channel1Data[0][3].toDouble()) + Point2D.distance(4.0, t1Channel1Data[0][3].toDouble(), 5.0, t1Channel1Data[0][4].toDouble()) +
//                Point2D.distance(2.0, t1Channel1Data[1][0].toDouble(), 3.0, t1Channel1Data[1][1].toDouble()) + Point2D.distance(3.0, t1Channel1Data[1][1].toDouble(), 4.0, t1Channel1Data[1][2].toDouble()) +
//                Point2D.distance(4.0, t1Channel1Data[1][2].toDouble(), 5.0, t1Channel1Data[1][3].toDouble()) + Point2D.distance(5.0, t1Channel1Data[1][3].toDouble(), 6.0, t1Channel1Data[1][4].toDouble()) +
//                Point2D.distance(3.0, t1Channel1Data[2][0].toDouble(), 4.0, t1Channel1Data[2][1].toDouble()) + Point2D.distance(4.0, t1Channel1Data[2][1].toDouble(), 5.0, t1Channel1Data[2][2].toDouble()) +
//                Point2D.distance(5.0, t1Channel1Data[2][2].toDouble(), 6.0, t1Channel1Data[2][3].toDouble()) + Point2D.distance(6.0, t1Channel1Data[2][3].toDouble(), 7.0, t1Channel1Data[2][4].toDouble())
//
//        println(feature[0].second[0])
//        println(result / 3)
//        assert(result / 3 almostEqual feature[0].second[0])
//
//
//        val t1Channel2Data = arrayOf(
//                Spike(1.0, floatArrayOf(41.12f, 60.27f, 5.07f, 37.87f, 25.13f, 50.28f, 10.58f)),
//                Spike(20.0, floatArrayOf(6.34f, 95.87f, 36.70f, 53.88f, 94.68f, 27.34f, 79.31f)),
//                Spike(300.0, floatArrayOf(27.47f, 52.70f, 48.72f, 85.84f, 11.59f, 40.86f, 74.63f)),
//                Spike(350.0, floatArrayOf(89.00f, 15.04f, 53.94f, 54.81f, 16.80f, 17.30f, 92.71f)),
//                Spike(450.0, floatArrayOf(2.48f, 1.91f, 10.81f, 63.07f, 26.53f, 41.49f, 18.09f))
//        )
//
//
//
//        result = Point2D.distance(1.0 / 10.0f, t1Channel2Data[0][0].toDouble(), 2.0 / 10.0f, t1Channel2Data[0][1].toDouble()) +
//                Point2D.distance(2.0 / 10.0f, t1Channel2Data[0][1].toDouble(), 3.0 / 10.0f, t1Channel2Data[0][2].toDouble()) +
//                Point2D.distance(3.0 / 10.0f, t1Channel2Data[0][2].toDouble(), 4.0 / 10.0f, t1Channel2Data[0][3].toDouble()) +
//                Point2D.distance(4.0 / 10.0f, t1Channel2Data[0][3].toDouble(), 5.0 / 10.0f, t1Channel2Data[0][4].toDouble()) +
//                Point2D.distance(5.0 / 10.0f, t1Channel2Data[0][4].toDouble(), 6.0 / 10.0f, t1Channel2Data[0][5].toDouble()) +
//                Point2D.distance(6.0 / 10.0f, t1Channel2Data[0][5].toDouble(), 7.0 / 10.0f, t1Channel2Data[0][6].toDouble()) +
//
//                Point2D.distance(20.0 / 10.0f, t1Channel2Data[1][0].toDouble(), 21.0 / 10.0f, t1Channel2Data[1][1].toDouble()) +
//                Point2D.distance(21.0 / 10.0f, t1Channel2Data[1][1].toDouble(), 22.0 / 10.0f, t1Channel2Data[1][2].toDouble()) +
//                Point2D.distance(22.0 / 10.0f, t1Channel2Data[1][2].toDouble(), 23.0 / 10.0f, t1Channel2Data[1][3].toDouble()) +
//                Point2D.distance(23.0 / 10.0f, t1Channel2Data[1][3].toDouble(), 24.0 / 10.0f, t1Channel2Data[1][4].toDouble()) +
//                Point2D.distance(24.0 / 10.0f, t1Channel2Data[1][4].toDouble(), 25.0 / 10.0f, t1Channel2Data[1][5].toDouble()) +
//                Point2D.distance(25.0 / 10.0f, t1Channel2Data[1][5].toDouble(), 26.0 / 10.0f, t1Channel2Data[1][6].toDouble()) +
//
//                Point2D.distance(300.0 / 10.0f, t1Channel2Data[2][0].toDouble(), 301.0 / 10.0f, t1Channel2Data[2][1].toDouble()) +
//                Point2D.distance(301.0 / 10.0f, t1Channel2Data[2][1].toDouble(), 302.0 / 10.0f, t1Channel2Data[2][2].toDouble()) +
//                Point2D.distance(302.0 / 10.0f, t1Channel2Data[2][2].toDouble(), 303.0 / 10.0f, t1Channel2Data[2][3].toDouble()) +
//                Point2D.distance(303.0 / 10.0f, t1Channel2Data[2][3].toDouble(), 304.0 / 10.0f, t1Channel2Data[2][4].toDouble()) +
//                Point2D.distance(304.0 / 10.0f, t1Channel2Data[2][4].toDouble(), 305.0 / 10.0f, t1Channel2Data[2][5].toDouble()) +
//                Point2D.distance(305.0 / 10.0f, t1Channel2Data[2][5].toDouble(), 306.0 / 10.0f, t1Channel2Data[2][6].toDouble()) +
//
//                Point2D.distance(350.0 / 10.0f, t1Channel2Data[3][0].toDouble(), 351.0 / 10.0f, t1Channel2Data[3][1].toDouble()) +
//                Point2D.distance(351.0 / 10.0f, t1Channel2Data[3][1].toDouble(), 352.0 / 10.0f, t1Channel2Data[3][2].toDouble()) +
//                Point2D.distance(352.0 / 10.0f, t1Channel2Data[3][2].toDouble(), 353.0 / 10.0f, t1Channel2Data[3][3].toDouble()) +
//                Point2D.distance(353.0 / 10.0f, t1Channel2Data[3][3].toDouble(), 354.0 / 10.0f, t1Channel2Data[3][4].toDouble()) +
//                Point2D.distance(354.0 / 10.0f, t1Channel2Data[3][4].toDouble(), 355.0 / 10.0f, t1Channel2Data[3][5].toDouble()) +
//                Point2D.distance(355.0 / 10.0f, t1Channel2Data[3][5].toDouble(), 356.0 / 10.0f, t1Channel2Data[3][6].toDouble()) +
//
//                Point2D.distance(450.0 / 10.0f, t1Channel2Data[4][0].toDouble(), 451.0 / 10.0f, t1Channel2Data[4][1].toDouble()) +
//                Point2D.distance(451.0 / 10.0f, t1Channel2Data[4][1].toDouble(), 452.0 / 10.0f, t1Channel2Data[4][2].toDouble()) +
//                Point2D.distance(452.0 / 10.0f, t1Channel2Data[4][2].toDouble(), 453.0 / 10.0f, t1Channel2Data[4][3].toDouble()) +
//                Point2D.distance(453.0 / 10.0f, t1Channel2Data[4][3].toDouble(), 454.0 / 10.0f, t1Channel2Data[4][4].toDouble()) +
//                Point2D.distance(454.0 / 10.0f, t1Channel2Data[4][4].toDouble(), 455.0 / 10.0f, t1Channel2Data[4][5].toDouble()) +
//                Point2D.distance(455.0 / 10.0f, t1Channel2Data[4][5].toDouble(), 456.0 / 10.0f, t1Channel2Data[4][6].toDouble())
//
//        assert(result / 5 almostEqual MeanPerimeter(10f, 3).extract(listOf(TrialData(1, listOf(t1Channel2Data))))[0].second[0])
//
//    }
//
//    @Test
//    fun meanArea() {
//        val t1Channel1Data = arrayOf(
//                Spike(1.0, floatArrayOf(0.0f, 15.0f, 12.0f, 22.0f, 31.0f)),
//                Spike(2.0, floatArrayOf(0.0f, 6.0f, 20.0f, 25.0f, 56.0f))
//        )
//
//        val feature = MeanArea(1.0f, 3).extract(listOf(TrialData(1, listOf(t1Channel1Data))))
//
//        val spike1 = t1Channel1Data[0].waveform.mapIndexed { index, float -> Pair((t1Channel1Data[0].timestamp + index) / 1.0f, float.toDouble()) }
//        val spike2 = t1Channel1Data[1].waveform.mapIndexed { index, float -> Pair((t1Channel1Data[1].timestamp + index) / 1.0f, float.toDouble()) }
//
//        var spike1Result = 0.0
//        (0 until spike1.size - 1).forEach {
//            spike1Result += spike1[it].first * spike1[it+1].second - spike1[it].second * spike1[it+1].first
//        }
//
//        var spike2Result = 0.0
//        (0 until spike2.size - 1).forEach {
//            spike2Result += spike2[it].first * spike2[it+1].second - spike2[it].second * spike2[it+1].first
//        }
//
//        assert((spike1Result + spike2Result)/2 almostEqual feature[0].second[0])
//
//    }
}