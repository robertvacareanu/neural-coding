import org.junit.Test
import reader.DataReader
import reader.MetadataReader
import kotlin.math.abs

/**
 * Tested having the base path set to folder containing the data. In this case, will be set to ../M017
 */
class ReaderTest {

    private val basePath = "../M017"

    infix fun Number.almostEqual(number: Number): Boolean {
        return abs(this.toDouble() - number.toDouble()) < 0.00001
    }

    @Test
    fun metadataReaderTest() {
        val mr = MetadataReader(basePath)
        val ssd = mr.readSSD()
        val spktwe = mr.readSPKTWE()
        val epd = mr.readEPD()

        assert(epd.eegChannels == 32)
        assert(epd.eegChannelsLabels[10] == "El_11")
        assert(epd.channelsUsedForAverage == 0)
        assert(epd.events == 960)
        assert(epd.channelPaths[30] == "M017_S001_SRCS3L_25,50,100_0001-Ch031.bin")
        assert(epd.timestampPath == "M017_S001_SRCS3L_25,50,100_0001-Event-Timestamps.bin")



        assert(spktwe.version almostEqual 1.0)
        assert(spktwe.storedChannelNames[0] == "El_01")
        assert(spktwe.storedChannelNames[6] == "El_07")
        assert(spktwe.spikesInEachChannel[5] == 11908)
        assert(spktwe.spikeTimesSampleFrequency almostEqual 32000.0)
        assert(spktwe.waveformSpikeOffset == 19)
        assert(spktwe.waveformInternalSamplingFrequency almostEqual 32000.0)
        assert(spktwe.spikeTimestampsPath == "M017_S001_SRCS3L_25,50,100_0001.spiket")


        assert(ssd.version almostEqual 1.0)
        assert(ssd.numberOfUnits == 49)
        assert(ssd.numberOfUnits == ssd.unitsName.size)
        assert(ssd.unitsName[5] == "U06")
        assert(ssd.origins[5] == "El_01")
        assert(ssd.spikesPerUnit[8] == 6738)
        assert(ssd.spikeSamplingFrequency almostEqual 1000.0)
        assert(ssd.waveformInternalSamplingFrequency almostEqual 32000.0)
        assert(ssd.spikeTimestampsPath == "M017_S001_SRCS3L_25,50,100_0001.ssdst")


    }

    @Test
    fun readerTest() {
        val mr = MetadataReader(basePath)
        val dr = DataReader(mr.readEPD(), mr.readSSD(), mr.readSPKTWE())

        val channel1 = dr.readChannelWaveform(1)
        assert(channel1[0] almostEqual 6.793299)
        assert(channel1[1] almostEqual -5.936311)
        assert(channel1[4] almostEqual -7.858675)
        assert(channel1[100] almostEqual 1.325014)
        assert(channel1[151] almostEqual -0.6792054)
        assert(channel1[152] almostEqual 0.5233009)
        assert(channel1[96915] almostEqual -1.82588)
        assert(channel1[6457] almostEqual -10.14443)
        assert(channel1[93238] almostEqual 12.85318)
        assert(channel1[56577] almostEqual 12.05377)
        assert(channel1[15360] almostEqual 0.03045756)
        assert(channel1[38454] almostEqual 6.482371)



        val channel5 = dr.readChannelWaveform(5)
        assert(channel5[0] almostEqual -3.032708)
        assert(channel5[1] almostEqual -0.9409744)
        assert(channel5[4] almostEqual -6.243722)
        assert(channel5[56577] almostEqual 15.826)
        assert(channel5[15360] almostEqual -0.6213912)
        assert(channel5[38454] almostEqual -2.313445)

        val spikeChannel1 = dr.readChannelSpikes(1)
        spikeChannel1[0].waveform[0] almostEqual 0.565429
        spikeChannel1[1].waveform[0] almostEqual 20.09776
        spikeChannel1[1].waveform[1] almostEqual 26.48896

        val spikeChannel5 = dr.readChannelSpikes(5)
        spikeChannel5[0].waveform[0] almostEqual 22.97374
        spikeChannel5[0].waveform[1] almostEqual 25.70727
        spikeChannel5[0].waveform[2] almostEqual 16.16155
        spikeChannel5[0].waveform[3] almostEqual 9.342863
        spikeChannel5[0].waveform[4] almostEqual 4.83482
        spikeChannel5[0].waveform[5] almostEqual 1.805182
        spikeChannel5[0].waveform[6] almostEqual 12.34129

        spikeChannel5[4].waveform[0] almostEqual 11.16329
        spikeChannel5[4].waveform[1] almostEqual 27.94641
        spikeChannel5[4].waveform[2] almostEqual 10.15276
        spikeChannel5[4].waveform[3] almostEqual 15.08644
        spikeChannel5[4].waveform[4] almostEqual -1.438469
        spikeChannel5[4].waveform[5] almostEqual -6.500218
        spikeChannel5[4].waveform[6] almostEqual -3.021892

    }

}