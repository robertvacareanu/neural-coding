import algorithm.extractor.data.BetweenStim
import org.junit.Test
import reader.DataReader
import reader.MetadataReader
import kotlin.math.abs

/**
 * Tested having the base path set to folder containing the data. In this case, will be set to ../M017
 * Data is not provided, hence no guarantees that it is tested with the same data it was tested initially
 */
class ReaderTest {

    private val basePath = "../M017"

    private infix fun Number.almostEqual(number: Number): Boolean {
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
        val dr = DataReader(mr.readEPD(), mr.readSSD(), mr.readSPKTWE(), mr.readETI())

        var segment = dr.readChannelWaveform(1, 0 until 100)
        assert(segment.data[0] almostEqual 6.793299)
        assert(segment.data[1] almostEqual -5.936311)
        assert(segment.data[2] almostEqual -3.278323)

        segment = dr.readChannelWaveform(1, 10 until 100)
        assert(segment.data[0] almostEqual -1.06999)
        assert(segment.data[1] almostEqual -2.215068)

        segment = dr.readChannelWaveform(1, 100 until 200)
        assert(segment.data[0] almostEqual 1.325014)
        assert(segment.data[1] almostEqual -8.718767)

        segment = dr.readChannelWaveform(2, 200 until 1000)
        assert(segment.data[0] almostEqual 5.108425)
        assert(segment.data[100] almostEqual -1.276632)

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
        assert(spikeChannel1[0].waveform[0] almostEqual 0.565429)
        assert(spikeChannel1[1].waveform[0] almostEqual 20.09776)
        assert(spikeChannel1[1].waveform[1] almostEqual 26.48896)
        assert(spikeChannel1[17080].waveform[0] almostEqual 8.453665)
        assert(spikeChannel1[17080].waveform[38] almostEqual 22.14343)
        assert(spikeChannel1[0].timestamp almostEqual 25625)
        assert(spikeChannel1[1].timestamp almostEqual 29160)
        assert(spikeChannel1[2].timestamp almostEqual 30044)
        assert(spikeChannel1[17080].timestamp almostEqual 36351520)

        val spikeChannel5 = dr.readChannelSpikes(5)
        assert(spikeChannel5[0].waveform[0] almostEqual 22.97374)
        assert(spikeChannel5[0].waveform[1] almostEqual 25.70727)
        assert(spikeChannel5[0].waveform[2] almostEqual 16.16155)
        assert(spikeChannel5[0].waveform[3] almostEqual 9.342863)
        assert(spikeChannel5[0].waveform[4] almostEqual 4.83482)
        assert(spikeChannel5[0].waveform[5] almostEqual 1.805182)
        assert(spikeChannel5[0].waveform[6] almostEqual 12.34129)
        assert(spikeChannel5[0].timestamp almostEqual 106605)

        assert(spikeChannel5[1].timestamp almostEqual 126545)

        assert(spikeChannel5[4].waveform[0] almostEqual 11.16329)
        assert(spikeChannel5[4].waveform[1] almostEqual 27.94641)
        assert(spikeChannel5[4].waveform[2] almostEqual 10.15276)
        assert(spikeChannel5[4].waveform[3] almostEqual 15.08644)
        assert(spikeChannel5[4].waveform[4] almostEqual -1.438469)
        assert(spikeChannel5[4].waveform[5] almostEqual -6.500218)
        assert(spikeChannel5[4].waveform[6] almostEqual -3.021892)
        assert(spikeChannel5[4].timestamp almostEqual 150036)

        val spikeChannel5Segment = dr.readChannelSpikes(5, 1 until 10)

        assert(spikeChannel5Segment[3].waveform[0] almostEqual 11.16329)
        assert(spikeChannel5Segment[3].waveform[1] almostEqual 27.94641)
        assert(spikeChannel5Segment[3].waveform[2] almostEqual 10.15276)
        assert(spikeChannel5Segment[3].waveform[3] almostEqual 15.08644)
        assert(spikeChannel5Segment[3].waveform[4] almostEqual -1.438469)
        assert(spikeChannel5Segment[3].waveform[5] almostEqual -6.500218)
        assert(spikeChannel5Segment[3].waveform[6] almostEqual -3.021892)
        println(spikeChannel5Segment[3].timestamp)
        assert(spikeChannel5Segment[3].timestamp almostEqual 150036)

        val spikeChannel5Segment1 = dr.readChannelSpikes(5, 2 until 10)

        assert(spikeChannel5Segment1[2].waveform[0] almostEqual 11.16329)
        assert(spikeChannel5Segment1[2].waveform[1] almostEqual 27.94641)
        assert(spikeChannel5Segment1[2].waveform[2] almostEqual 10.15276)
        assert(spikeChannel5Segment1[2].waveform[3] almostEqual 15.08644)
        assert(spikeChannel5Segment1[2].waveform[4] almostEqual -1.438469)
        assert(spikeChannel5Segment1[2].waveform[5] almostEqual -6.500218)
        assert(spikeChannel5Segment1[2].waveform[6] almostEqual -3.021892)

        val trials = dr.readTrials()

        assert(trials[0].trialNumber == 1)
        assert(trials[0].orientation == 0)
        assert(trials[0].trialStartOffset == 124575)
        assert(trials[0].trialEndOffset == 258180)

        assert(trials[1].trialNumber == 2)
        assert(trials[1].orientation == 90)
        assert(trials[1].trialStartOffset == 274748)
        assert(trials[1].trialEndOffset == 408353)

        assert(trials[100].trialNumber == 101)
        assert(trials[100].orientation == 0)
        assert(trials[100].trialStartOffset == 15141871)
        assert(trials[100].stimOnOffset == 15173937)
        assert(trials[100].stimOffOffset == 15259445)
        assert(trials[100].trialEndOffset == 15275477)

    }

}