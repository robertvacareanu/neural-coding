import algorithm.extractor.feature.MeanAmplitude
import algorithm.extractor.feature.MeanPerimeter
import algorithm.extractor.feature.SpikesPerSec
import model.Spike
import model.TrialData
import model.metadata.SpikeMetadata
import org.junit.Test
import java.awt.geom.Point2D
import kotlin.math.abs

/**
 * Created by robert on 2/19/18.
 * Tests for the functionality inside algorithm.extractor.feature
 */
class FeatureExtractorTest {

    infix fun Number.almostEqual(number: Number): Boolean {
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
        val spikeMetadataDummy = SpikeMetadata(
                basePath = "",
                version = 1.0f,
                storedChannels = 3,
                storedChannelNames = listOf(),
                spikesInEachChannel = listOf(),
                spikeTimesSampleFrequency = 0.0f,
                recordingLength = 0,
                waveformInternalSamplingFrequency = 0.0f,
                waveformLength = 0,
                waveformSpikeOffset = 5,
                eventsSamplingFrequency = 0.0f,
                events = 0,
                spikeTimestampsPath = "",
                spikeWaveformPath = "",
                eventTimestampsPath = "",
                eventCodesPath = ""
        )

        val featureExtractor = MeanAmplitude(spikeMetadataDummy)


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
        val spikeMetadataDummy = SpikeMetadata(
                basePath = "",
                version = 1.0f,
                storedChannels = 3,
                storedChannelNames = listOf(),
                spikesInEachChannel = listOf(),
                spikeTimesSampleFrequency = 0.0f,
                recordingLength = 0,
                waveformInternalSamplingFrequency = 10.0f,
                waveformLength = 0,
                waveformSpikeOffset = 5,
                eventsSamplingFrequency = 0.0f,
                events = 0,
                spikeTimestampsPath = "",
                spikeWaveformPath = "",
                eventTimestampsPath = "",
                eventCodesPath = ""
        )

        val feature = SpikesPerSec(spikeMetadataDummy).extract(constructTrialDataMock())

        assert(feature[0].first == 1)
        assert(feature[1].first == 1)

        assert(feature[0].second[0] almostEqual 15.0)
        assert(feature[0].second[1] almostEqual 7.5)
        assert(feature[0].second[2] almostEqual 3.333333)
        assert(feature[1].second[0] almostEqual 1.578947368)
        assert(feature[1].second[1] almostEqual 0.769230769)
        assert(feature[1].second[2] almostEqual 3.333333)

    }

    @Test
    fun meanPerimeter() {
        val spikeMetadataDummy = SpikeMetadata(
                basePath = "",
                version = 1.0f,
                storedChannels = 1,
                storedChannelNames = listOf(),
                spikesInEachChannel = listOf(),
                spikeTimesSampleFrequency = 0.0f,
                recordingLength = 0,
                waveformInternalSamplingFrequency = 1.0f,
                waveformLength = 0,
                waveformSpikeOffset = 5,
                eventsSamplingFrequency = 0.0f,
                events = 0,
                spikeTimestampsPath = "",
                spikeWaveformPath = "",
                eventTimestampsPath = "",
                eventCodesPath = ""
        )

        val t1Channel1Data = arrayOf(
                Spike(1.0, floatArrayOf(0.0f, 15.0f, 12.0f, 22.0f, 31.0f)),
                Spike(2.0, floatArrayOf(0.0f, 6.0f, 20.0f, 25.0f, 56.0f)),
                Spike(3.0, floatArrayOf(10.0f, 20.0f, 50.0f, 23.0f, 0.5f))
        )

        val feature = MeanPerimeter(spikeMetadataDummy).extract(listOf(TrialData(1, listOf(t1Channel1Data))))

        val result = Point2D.distance(1.0, t1Channel1Data[0][0].toDouble(), 2.0, t1Channel1Data[0][1].toDouble()) + Point2D.distance(2.0, t1Channel1Data[0][1].toDouble(), 3.0, t1Channel1Data[0][2].toDouble()) +
                Point2D.distance(3.0, t1Channel1Data[0][2].toDouble(), 4.0, t1Channel1Data[0][3].toDouble()) + Point2D.distance(4.0, t1Channel1Data[0][3].toDouble(), 5.0, t1Channel1Data[0][4].toDouble()) +
                Point2D.distance(2.0, t1Channel1Data[1][0].toDouble(), 3.0, t1Channel1Data[1][1].toDouble()) + Point2D.distance(3.0, t1Channel1Data[1][1].toDouble(), 4.0, t1Channel1Data[1][2].toDouble()) +
                Point2D.distance(4.0, t1Channel1Data[1][2].toDouble(), 5.0, t1Channel1Data[1][3].toDouble()) + Point2D.distance(5.0, t1Channel1Data[1][3].toDouble(), 6.0, t1Channel1Data[1][4].toDouble()) +
                Point2D.distance(3.0, t1Channel1Data[2][0].toDouble(), 4.0, t1Channel1Data[2][1].toDouble()) + Point2D.distance(4.0, t1Channel1Data[2][1].toDouble(), 5.0, t1Channel1Data[2][2].toDouble()) +
                Point2D.distance(5.0, t1Channel1Data[2][2].toDouble(), 6.0, t1Channel1Data[2][3].toDouble()) + Point2D.distance(6.0, t1Channel1Data[2][3].toDouble(), 7.0, t1Channel1Data[2][4].toDouble())

        assert(result / 3 almostEqual feature[0].second[0])


    }

}