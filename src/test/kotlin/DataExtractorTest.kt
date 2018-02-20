import algorithm.extractor.data.BetweenStim
import algorithm.extractor.feature.SpikesPerSec
import org.junit.Test
import reader.DataReader
import reader.MetadataReader
import kotlin.math.abs

/**
 * Created by robert on 2/19/18.
 */
class DataExtractorTest {

    private val basePath = "../M017"

    private infix fun Number.almostEqual(number: Number): Boolean {
        return abs(this.toDouble() - number.toDouble()) < 0.00001
    }


    @Test
    fun betwenStim() {


        val mr = MetadataReader(basePath)
        val dr = DataReader(mr.readEPD(), mr.readSSD(), mr.readSPKTWE(), mr.readETI())

        val trials = dr.readTrials()

        val betweenStim = BetweenStim(dr)

        val testData = betweenStim.extractData(listOf(trials[0], trials[1]))

        assert(testData[0].orientation == 0)

        assert(testData[0].spikeData[0][0].waveform[0] almostEqual -12.0737)
        assert(testData[0].spikeData[0][0].waveform[1] almostEqual -5.02141)
        assert(testData[0].spikeData[0][1].waveform[0] almostEqual 66.00766)
        assert(testData[0].spikeData[0][2].waveform[0] almostEqual 4.630025)
        assert(testData[0].spikeData[0][3].waveform[0] almostEqual 33.55904)
        assert(testData[0].spikeData[0][4].waveform[0] almostEqual 20.03858)
        assert(testData[0].spikeData[0][5].waveform[0] almostEqual 21.91661)
        assert(testData[0].spikeData[0][6].waveform[0] almostEqual 7.803526)
        assert(testData[0].spikeData[0][60].waveform[0] almostEqual 29.15911)
        assert(testData[0].spikeData[0][106].waveform[0] almostEqual -2.86156)
        assert(testData[0].spikeData[0][106].waveform[1] almostEqual 0.2091425)
        assert(testData[0].spikeData[0][106].waveform[2] almostEqual 0.5920082)
        assert(testData[0].spikeData[0][106].waveform[3] almostEqual 15.99877)
        assert(testData[0].spikeData[0][106].waveform[4] almostEqual 13.9175)
        assert(testData[0].spikeData[0][106].waveform[5] almostEqual 33.73422)
        assert(testData[0].spikeData[0][106].waveform[6] almostEqual 16.05228)
        assert(testData[0].spikeData[0][106].waveform[7] almostEqual 24.42965)
        assert(testData[0].spikeData[0][106].waveform[32] almostEqual 26.06567)
        assert(testData[0].spikeData[0][106].waveform[33] almostEqual 15.64846)
        assert(testData[0].spikeData[0][106].waveform[34] almostEqual 24.88918)
        assert(testData[0].spikeData[0][106].waveform[35] almostEqual 14.19616)
        assert(testData[0].spikeData[0][106].waveform[36] almostEqual 13.16541)
        assert(testData[0].spikeData[0][106].waveform[37] almostEqual 15.13039)
        assert(testData[0].spikeData[0][106].waveform[38] almostEqual 20.92668)

        assert(testData[0].spikeData[1][0].waveform[0] almostEqual -0.03298917)

        assert(testData[1].orientation == 90)
        assert(testData[1].spikeData[0][0].waveform[0] almostEqual -18.49579)
        assert(testData[1].spikeData[0][0].waveform[1] almostEqual -11.17014)
        assert(testData[1].spikeData[0][1].waveform[0] almostEqual 4.989871)
        assert(testData[1].spikeData[0][1].waveform[1] almostEqual -4.603093)
        assert(testData[1].spikeData[1][0].waveform[0] almostEqual 31.23073)
        assert(testData[1].spikeData[1][0].waveform[1] almostEqual 22.10519)
        assert(testData[1].spikeData[1][1].waveform[0] almostEqual -6.620437)
        assert(testData[1].spikeData[1][1].waveform[1] almostEqual -17.91105)


    }
}