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

        val spikeChannel5 = dr.readChannelSpikes(5)
        assert(spikeChannel5[0].waveform[0] almostEqual 22.97374)
        assert(spikeChannel5[0].waveform[1] almostEqual 25.70727)
        assert(spikeChannel5[0].waveform[2] almostEqual 16.16155)
        assert(spikeChannel5[0].waveform[3] almostEqual 9.342863)
        assert(spikeChannel5[0].waveform[4] almostEqual 4.83482)
        assert(spikeChannel5[0].waveform[5] almostEqual 1.805182)
        assert(spikeChannel5[0].waveform[6] almostEqual 12.34129)

        assert(spikeChannel5[4].waveform[0] almostEqual 11.16329)
        assert(spikeChannel5[4].waveform[1] almostEqual 27.94641)
        assert(spikeChannel5[4].waveform[2] almostEqual 10.15276)
        assert(spikeChannel5[4].waveform[3] almostEqual 15.08644)
        assert(spikeChannel5[4].waveform[4] almostEqual -1.438469)
        assert(spikeChannel5[4].waveform[5] almostEqual -6.500218)
        assert(spikeChannel5[4].waveform[6] almostEqual -3.021892)


        val spikeChannel5Segment = dr.readChannelSpikes(5, 1 until 10)

        assert(spikeChannel5Segment[3].waveform[0] almostEqual 11.16329)
        assert(spikeChannel5Segment[3].waveform[1] almostEqual 27.94641)
        assert(spikeChannel5Segment[3].waveform[2] almostEqual 10.15276)
        assert(spikeChannel5Segment[3].waveform[3] almostEqual 15.08644)
        assert(spikeChannel5Segment[3].waveform[4] almostEqual -1.438469)
        assert(spikeChannel5Segment[3].waveform[5] almostEqual -6.500218)
        assert(spikeChannel5Segment[3].waveform[6] almostEqual -3.021892)

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