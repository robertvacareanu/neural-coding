import algorithm.extractor.feature.MeanAmplitude
import model.Spike
import model.TrialData
import model.metadata.SpikeMetadata
import org.junit.Test
import reader.DataReader
import reader.MetadataReader
import kotlin.math.abs

/**
 * Created by robert on 2/19/18.
 */
class FeatureExtractorTest {

    private val basePath = "../M017"

    infix fun Number.almostEqual(number: Number): Boolean {
        return abs(this.toDouble() - number.toDouble()) < 0.00001
    }

    @Test
    fun meanAmplitude() {
        val mr = MetadataReader(basePath)

        val dr = DataReader(mr.readEPD(), mr.readSSD(), mr.readSPKTWE(), mr.readETI())

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

        //Construct some test data
        val trialData = mutableListOf<TrialData>()

        val t1Channel1Data = arrayOf(
                Spike(1.0f, floatArrayOf(0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f)),
                Spike(2.0f, floatArrayOf(0.0f, 10.0f, 20.0f, 30.0f, 40.0f, 50.0f, 60.0f, 70.0f, 80.0f, 90.0f)),
                Spike(3.0f, floatArrayOf(10.0f, 20.0f, 50.0f, 65.0f, 74.1f, 74.1f, 86.0f, 22.0f, 23.0f, 20.0f))
        )
        val t1Channel2Data = arrayOf(
                Spike(1.0f, floatArrayOf(18.2f, 15.0f, 25.0f, 24.0f, 30.0f, 57.0f, 49.0f, 31.0f, 30.0f, 33.0f)),
                Spike(2.0f, floatArrayOf(-4.0f, 0.0f, -13.0f, -26.0f, -24.0f, -23.0f, -37.0f, 0.0f, 2.0f, -3.0f)),
                Spike(3.0f, floatArrayOf(27.0f, 73.0f, 77.0f, 65.0f, 74.1f, 58.0f, 97.0f, 35.0f, 49.0f, 57.18f))
        )
        val t1Channel3Data = arrayOf(
                Spike(1.0f, floatArrayOf(18.2f, 15.0f, 25.0f, 24.0f, 30.0f, 92.12f, 49.0f, 31.0f, 30.0f, 33.0f)),
                Spike(2.0f, floatArrayOf(-4.0f, 0.0f, -13.0f, -26.0f, -29.0f, -25.36f, -37.0f, 0.0f, 2.0f, -3.0f)),
                Spike(3.0f, floatArrayOf(27.0f, 73.0f, 77.0f, 65.0f, 74.1f, 82.18f, 97.0f, 35.0f, 49.0f, 57.18f))
        )


        val t2Channel1Data = arrayOf(
                Spike(1.0f, floatArrayOf(16.2f, 15.0f, 20.0f, 17.16f, 21.15f, 15.0f, 15.11f, 18.0f, 18.9f, 9.0f)),
                Spike(2.0f, floatArrayOf(7.0f, 10.0f, 20.0f, 30.0f, 40.0f, 50.0f, 60.0f, 70.0f, 80.0f, 90.0f)),
                Spike(3.0f, floatArrayOf(10.0f, 20.0f, 50.0f, 65.0f, 74.1f, 58.0f, 86.0f, 22.0f, 23.0f, 20.0f))
        )
        val t2Channel2Data = arrayOf(
                Spike(1.0f, floatArrayOf(18.2f, 15.0f, 25.0f, 24.0f, 30.0f, 57.0f, 49.0f, 31.0f, 30.0f, 33.0f)),
                Spike(2.0f, floatArrayOf(18.0f, 20.0f, 21.0f, 25.0f, 46.0f, 57.0f, -37.0f, 0.0f, 2.0f, -3.0f)),
                Spike(3.0f, floatArrayOf(27.0f, 73.0f, 77.0f, 65.0f, 74.1f, 58.0f, 97.0f, 35.0f, 49.0f, 57.18f))
        )
        val t2Channel3Data = arrayOf(
                Spike(1.0f, floatArrayOf(18.2f, 15.0f, 25.0f, 24.0f, 30.0f, 57.0f, 49.0f, 31.0f, 30.0f, 33.0f)),
                Spike(2.0f, floatArrayOf(-4.0f, -10.0f, -20.0f, -31.0f, -24.0f, -34.0f, -50.0f, -9.0f, -8.0f, -9.0f)),
                Spike(3.0f, floatArrayOf(27.0f, 73.0f, 77.0f, 65.11f, 84.14f, 68.13f, 107.0f, 51.15f, 75.0f, 57.18f))
        )

        trialData.add(TrialData(1, listOf(t1Channel1Data, t1Channel2Data, t1Channel3Data)))
        trialData.add(TrialData(1, listOf(t2Channel1Data, t2Channel2Data, t2Channel3Data)))

        val feature = featureExtractor.extract(trialData)

        assert(feature[0].first == 1)
        assert(feature[1].first == 1)

        assert(feature[0].second[0] almostEqual 43.0333333333)
        assert(feature[0].second[1] almostEqual 46)
        assert(feature[0].second[2] almostEqual 66.5533333333)
        assert(feature[1].second[0] almostEqual 41)
        assert(feature[1].second[1] almostEqual 57.3333333333)
        assert(feature[1].second[2] almostEqual 53.0433333333)
    }
}