package extractor.data

import algorithm.extractor.data.*
import main.almostEqual
import model.metadata.SpikeMetadata
import org.junit.Test
import reader.MetadataReader
import reader.spikes.DataSpikeReader
import kotlin.math.abs

/**
 * Created by robert on 2/19/18.
 * Tests for the functionality inside algorithm.extractor.data
 */
class DataExtractorTest {

    private val basePath = "../M017"

    @Test
    fun betwenStim() {


        val mr = MetadataReader(basePath)
        val dr = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()))

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

    @Test
    fun afterStim() {


        val mr = MetadataReader(basePath)
        val dr = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()))

        val trials = dr.readTrials()

        val featureExtractor = AfterStim(dr)

        val testData = featureExtractor.extractData(listOf(trials[0], trials[1]))


        assert(testData[0].orientation == 0)
        assert(testData[0].spikeData[0].size == 12)

        assert(testData[0].spikeData[0][0].waveform[0] almostEqual 4.983692)
        assert(testData[0].spikeData[0][0].waveform[1] almostEqual -0.6081506)
        assert(testData[0].spikeData[0][0].waveform[2] almostEqual -0.7723005)


        assert(testData[0].spikeData[0][1].waveform[0] almostEqual 7.627504)
        assert(testData[0].spikeData[0][1].waveform[1] almostEqual 2.46779)
        assert(testData[0].spikeData[0][1].waveform[2] almostEqual -8.105932)

        assert(testData[0].spikeData[0][2].waveform[0] almostEqual 4.512428)
        assert(testData[0].spikeData[0][2].waveform[1] almostEqual -1.764616)
        assert(testData[0].spikeData[0][2].waveform[2] almostEqual 10.56609)
        assert(testData[0].spikeData[0][2].waveform[3] almostEqual 11.07979)
        assert(testData[0].spikeData[0][2].waveform[4] almostEqual 7.695301)

        assert(testData[0].spikeData[1].isEmpty())


        assert(testData[1].orientation == 90)
        assert(testData[1].spikeData[0].size == 9)

        assert(testData[1].spikeData[0][0].waveform[0] almostEqual 5.316537)
        assert(testData[1].spikeData[0][0].waveform[1] almostEqual 12.97253)
        assert(testData[1].spikeData[0][0].waveform[2] almostEqual 7.71985)


        assert(testData[1].spikeData[0][1].waveform[0] almostEqual -0.3676532)
        assert(testData[1].spikeData[0][1].waveform[1] almostEqual -0.1935933)
        assert(testData[1].spikeData[0][1].waveform[2] almostEqual -5.681955)

        assert(testData[1].spikeData[0][2].waveform[0] almostEqual -9.476216)
        assert(testData[1].spikeData[0][2].waveform[1] almostEqual -6.678604)
        assert(testData[1].spikeData[0][2].waveform[2] almostEqual -0.762773)
        assert(testData[1].spikeData[0][2].waveform[3] almostEqual -7.983954)
        assert(testData[1].spikeData[0][2].waveform[4] almostEqual -6.042834)


        assert(testData[1].spikeData[1].size == 3)

        assert(testData[1].spikeData[1][0].waveform[0] almostEqual 20.18396)
        assert(testData[1].spikeData[1][0].waveform[1] almostEqual 4.552)
        assert(testData[1].spikeData[1][0].waveform[2] almostEqual 6.416876)

        assert(testData[1].spikeData[1][1].waveform[0] almostEqual 2.970481)
        assert(testData[1].spikeData[1][1].waveform[1] almostEqual 6.986032)
        assert(testData[1].spikeData[1][1].waveform[2] almostEqual 14.18113)

        assert(testData[1].spikeData[1][2].waveform[0] almostEqual 31.42084)
        assert(testData[1].spikeData[1][2].waveform[1] almostEqual 34.78887)
        assert(testData[1].spikeData[1][2].waveform[2] almostEqual 27.05205)
        assert(testData[1].spikeData[1][2].waveform[3] almostEqual 27.37967)
        assert(testData[1].spikeData[1][2].waveform[4] almostEqual 22.84771)

    }

    @Test
    fun beforeStim() {
        val mr = MetadataReader(basePath)
        val dr = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()))

        val trials = dr.readTrials()

        val featureExtractor = BeforeStim(dr)

        val testData = featureExtractor.extractData(listOf(trials[0], trials[1]))

        assert(testData[0].orientation == 0)
        assert(testData[0].spikeData[0].size == 27)


        assert(testData[0].spikeData[0][0].waveform[0] almostEqual -9.292385)
        assert(testData[0].spikeData[0][0].waveform[1] almostEqual -2.997954)
        assert(testData[0].spikeData[0][0].waveform[2] almostEqual -3.34142)


        assert(testData[0].spikeData[0][1].waveform[0] almostEqual -24.78477)
        assert(testData[0].spikeData[0][1].waveform[1] almostEqual -2.421317)
        assert(testData[0].spikeData[0][1].waveform[2] almostEqual -4.192569)

        assert(testData[0].spikeData[0][2].waveform[0] almostEqual -7.383975)
        assert(testData[0].spikeData[0][2].waveform[1] almostEqual -8.604445)
        assert(testData[0].spikeData[0][2].waveform[2] almostEqual -17.44852)
        assert(testData[0].spikeData[0][2].waveform[3] almostEqual -3.491798)
        assert(testData[0].spikeData[0][2].waveform[4] almostEqual -6.331959)



        assert(testData[0].spikeData[1].size == 2)
        assert(testData[0].spikeData[1][0].waveform[0] almostEqual -13.17614)
        assert(testData[0].spikeData[1][0].waveform[1] almostEqual 7.882262)
        assert(testData[0].spikeData[1][0].waveform[2] almostEqual 4.829584)


        assert(testData[0].spikeData[1][1].waveform[0] almostEqual 16.77263)
        assert(testData[0].spikeData[1][1].waveform[1] almostEqual 16.5097)
        assert(testData[0].spikeData[1][1].waveform[2] almostEqual 20.11362)

        assert(testData[1].orientation == 90)
        assert(testData[1].spikeData[0].isEmpty())
        assert(testData[1].spikeData[1].isEmpty())

    }

    @Test
    fun afterStimOn() {


        val mr = MetadataReader(basePath)
        val spikeMetadata = mr.readSPKTWE()
        val dr = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()))

        val trials = dr.readTrials()

        val featureExtractor = AfterStimOn(dr, (spikeMetadata.waveformInternalSamplingFrequency).toInt()) // 32000 => 1 sec

        val testData = featureExtractor.extractData(listOf(trials[0], trials[1]))

        assert(testData[0].orientation == 0)


        assert(testData[0].spikeData[0][0].waveform[0] almostEqual -12.0737)
        assert(testData[0].spikeData[0][0].waveform[1] almostEqual -5.02141)
        assert(testData[0].spikeData[0][0].waveform[2] almostEqual -6.669167)

        assert(testData[0].spikeData[0][10].waveform[0] almostEqual 18.84047)
        assert(testData[0].spikeData[0][10].waveform[1] almostEqual 14.62682)
        assert(testData[0].spikeData[0][10].waveform[2] almostEqual 10.21731)
        assert(testData[0].spikeData[0][10].waveform[3] almostEqual 10.80765)
        assert(testData[0].spikeData[0][10].waveform[4] almostEqual 31.80307)

        assert(testData[0].spikeData[0][11].waveform[0] almostEqual 5.000644)
        assert(testData[0].spikeData[0][11].waveform[1] almostEqual 1.122699)
        assert(testData[0].spikeData[0][11].waveform[2] almostEqual 6.235658)

        assert(testData[0].spikeData[1].size == 6)

        assert(testData[0].spikeData[1][4].waveform[0] almostEqual 17.37917)
        assert(testData[0].spikeData[1][4].waveform[1] almostEqual 22.45417)
        assert(testData[0].spikeData[1][4].waveform[2] almostEqual 23.62536)

        assert(testData[0].spikeData[1][5].waveform[0] almostEqual 20.78578)
        assert(testData[0].spikeData[1][5].waveform[1] almostEqual 25.88588)
        assert(testData[0].spikeData[1][5].waveform[2] almostEqual 12.94719)
        assert(testData[0].spikeData[1][5].waveform[3] almostEqual 3.852674)
        assert(testData[0].spikeData[1][5].waveform[4] almostEqual -5.354928)



        assert(testData[1].orientation == 90)

        assert(testData[1].spikeData[0][0].waveform[0] almostEqual -18.49579)
        assert(testData[1].spikeData[0][0].waveform[1] almostEqual -11.17014)
        assert(testData[1].spikeData[0][0].waveform[2] almostEqual 9.662333)
        assert(testData[1].spikeData[0][0].waveform[3] almostEqual -0.8046439)
        assert(testData[1].spikeData[0][0].waveform[4] almostEqual 0.3597021)
        assert(testData[1].spikeData[0][0].waveform[5] almostEqual -1.640735)

        assert(testData[1].spikeData[0][15].waveform[0] almostEqual 8.601676)
        assert(testData[1].spikeData[0][15].waveform[1] almostEqual 9.690949)
        assert(testData[1].spikeData[0][15].waveform[2] almostEqual 10.14294)
        assert(testData[1].spikeData[0][15].waveform[3] almostEqual 3.908319)
        assert(testData[1].spikeData[0][15].waveform[4] almostEqual 8.695457)

        assert(testData[1].spikeData[0][16].waveform[0] almostEqual 28.73806)
        assert(testData[1].spikeData[0][16].waveform[1] almostEqual 37.57459)
        assert(testData[1].spikeData[0][16].waveform[2] almostEqual 40.84284)
        assert(testData[1].spikeData[0][16].waveform[3] almostEqual 44.77534)
        assert(testData[1].spikeData[0][16].waveform[4] almostEqual 40.80704)

        assert(testData[1].spikeData[1].size == 4)

        assert(testData[1].spikeData[1][0].waveform[0] almostEqual 31.23073)
        assert(testData[1].spikeData[1][0].waveform[1] almostEqual 22.10519)
        assert(testData[1].spikeData[1][0].waveform[2] almostEqual 25.26988)

        assert(testData[1].spikeData[1][2].waveform[0] almostEqual 5.846882)
        assert(testData[1].spikeData[1][2].waveform[1] almostEqual 12.19574)
        assert(testData[1].spikeData[1][2].waveform[2] almostEqual 22.1124)
        assert(testData[1].spikeData[1][2].waveform[3] almostEqual 18.50346)
        assert(testData[1].spikeData[1][2].waveform[4] almostEqual 27.41499)

        assert(testData[1].spikeData[1][3].waveform[5] almostEqual 20.92064)
        assert(testData[1].spikeData[1][3].waveform[6] almostEqual 8.631038)


    }

    @Test
    fun afterStimOff() {

        val mr = MetadataReader(basePath)
        val spikeMetadata = mr.readSPKTWE()
        val dr = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()))

        val trials = dr.readTrials()

        val featureExtractor = AfterStimOff(dr, (spikeMetadata.waveformInternalSamplingFrequency).toInt()) // 32000 => 1 sec

        val testData = featureExtractor.extractData(listOf(trials[0], trials[1]))



        assert(testData[0].orientation == 0)


        assert(testData[0].spikeData[0][0].waveform[0] almostEqual 4.983692)
        assert(testData[0].spikeData[0][0].waveform[1] almostEqual -0.6081506)

        assert(testData[0].spikeData[0][1].waveform[0] almostEqual 7.627504)
        assert(testData[0].spikeData[0][1].waveform[2] almostEqual -8.105932)

        assert(testData[0].spikeData[0][5].waveform[0] almostEqual -5.403612)
        assert(testData[0].spikeData[0][5].waveform[1] almostEqual 15.64117)

        assert(testData[0].spikeData[1].isEmpty())


        assert(testData[1].orientation == 90)


        assert(testData[1].spikeData[0][0].waveform[0] almostEqual 5.316537)
        assert(testData[1].spikeData[0][0].waveform[1] almostEqual 12.97253)

        assert(testData[1].spikeData[0][3].waveform[0] almostEqual 14.26329)
        assert(testData[1].spikeData[0][3].waveform[2] almostEqual 22.01708)

        assert(testData[1].spikeData[0][5].waveform[0] almostEqual 3.444494)
        assert(testData[1].spikeData[0][5].waveform[1] almostEqual 12.49758)

        assert(testData[1].spikeData[0][8].waveform[0] almostEqual 11.49468)
        assert(testData[1].spikeData[0][8].waveform[1] almostEqual 13.55329)
        assert(testData[1].spikeData[0][8].waveform[2] almostEqual 13.31968)
        assert(testData[1].spikeData[0][8].waveform[3] almostEqual 13.0842)
        assert(testData[1].spikeData[0][8].waveform[4] almostEqual 14.09456)
        assert(testData[1].spikeData[0][8].waveform[5] almostEqual 13.43026)
        assert(testData[1].spikeData[0][8].waveform[11] almostEqual 19.5148)
        assert(testData[1].spikeData[0][8].waveform[12] almostEqual 17.45013)
        assert(testData[1].spikeData[0][8].waveform[17] almostEqual -46.40549)
        assert(testData[1].spikeData[0][8].waveform[18] almostEqual -52.06171)
        assert(testData[1].spikeData[0][8].waveform[19] almostEqual -53.59877)
        assert(testData[1].spikeData[0][8].waveform[20] almostEqual -47.68181)
        assert(testData[1].spikeData[0][8].waveform[26] almostEqual 15.49474)
        assert(testData[1].spikeData[0][8].waveform[31] almostEqual 11.4341)
        assert(testData[1].spikeData[0][8].waveform[33] almostEqual 31.29735)
        assert(testData[1].spikeData[0][8].waveform[37] almostEqual 21.05257)
        assert(testData[1].spikeData[0][8].waveform[38] almostEqual 23.92834)


        assert(testData[1].spikeData[1][0].waveform[0] almostEqual 20.18396)
        assert(testData[1].spikeData[1][0].waveform[1] almostEqual 4.552)

        assert(testData[1].spikeData[1][1].waveform[0] almostEqual 2.970481)
        assert(testData[1].spikeData[1][1].waveform[1] almostEqual 6.986032)

        assert(testData[1].spikeData[1][2].waveform[0] almostEqual 31.42084)
        assert(testData[1].spikeData[1][2].waveform[19] almostEqual -65.84263)

    }

    @Test
    fun randomAfterStimOn() {
        val mr = MetadataReader(basePath)
        val dr = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()))

        val trials = dr.readTrials()

        var randomAfterStimOn = RandomAfterStimOn(dr, 0, 96000)

        var testData = randomAfterStimOn.extractData(listOf(trials[0], trials[1]))

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

        // Ignore first 3872 floats
        randomAfterStimOn = RandomAfterStimOn(dr, 3872, 60000)

        testData = randomAfterStimOn.extractData(listOf(trials[0], trials[1]))

        assert(testData[0].orientation == 0)

        assert(testData[0].spikeData[0][0].waveform[0] almostEqual 66.00766)
        assert(testData[0].spikeData[0][0].waveform[1] almostEqual 65.00742)
        assert(testData[0].spikeData[0][0].waveform[2] almostEqual 53.02333)
        assert(abs(testData[0].spikeData[0][0].waveform[19] - (-170.6389)) < 0.0001)
        assert(abs(testData[0].spikeData[0][0].waveform[20] - (-163.4559)) < 0.0001)
        assert(abs(testData[0].spikeData[0][0].waveform[21] - (-132.0076)) < 0.0001)

        assert(testData[0].spikeData[0][1].waveform[0] almostEqual 4.630025)
        assert(testData[0].spikeData[0][1].waveform[1] almostEqual 2.339633)
        assert(testData[0].spikeData[0][1].waveform[2] almostEqual 14.07165)
        assert(testData[0].spikeData[0][1].waveform[3] almostEqual 12.08744)

        assert(testData[0].spikeData[0][2].waveform[0] almostEqual 33.55904)
        assert(testData[0].spikeData[0][2].waveform[1] almostEqual 41.26452)
        assert(testData[0].spikeData[0][2].waveform[2] almostEqual 57.38702)
        assert(testData[0].spikeData[0][3].waveform[0] almostEqual 20.03858)
        assert(testData[0].spikeData[0][3].waveform[19] almostEqual -60.77736)


        assert(testData[1].orientation == 90)

        assert(testData[1].spikeData[0][0].waveform[0] almostEqual 4.989871)

        assert(testData[1].spikeData[0][0].waveform[1] almostEqual -4.603093)
        assert(testData[1].spikeData[0][0].waveform[2] almostEqual -8.502367)
        assert(testData[1].spikeData[0][0].waveform[19] almostEqual -57.72361)
        assert(testData[1].spikeData[0][0].waveform[20] almostEqual -54.39449)
        assert(testData[1].spikeData[0][0].waveform[21] almostEqual -39.1695)

        randomAfterStimOn = RandomAfterStimOn(dr, 6000, 60000)
        testData = randomAfterStimOn.extractData(listOf(trials[0], trials[1]))

        assert(testData[0].spikeData[0][0].waveform[0] almostEqual 20.03858)
        assert(testData[0].spikeData[0][0].waveform[1] almostEqual 29.60803)
        assert(testData[0].spikeData[0][1].waveform[0] almostEqual 21.91661)
        assert(testData[0].spikeData[0][1].waveform[1] almostEqual 27.95836)

        assert(testData[0].spikeData[1][0].waveform[0] almostEqual 9.352667)
        assert(testData[0].spikeData[1][0].waveform[1] almostEqual 24.95034)

        assert(testData[0].spikeData[1][1].waveform[0] almostEqual 16.00261)
        assert(testData[0].spikeData[1][1].waveform[1] almostEqual 27.40732)


    }

    @Test
    fun randomAfterEvent() {
        val mr = MetadataReader(basePath)
        val dr = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()))

        val trials = dr.readTrials()

        var randomAfterEvent = RandomAfterEvent(dr, { stimOnOffset }, 0, 96000)

        var testData = randomAfterEvent.extractData(listOf(trials[0], trials[1]))

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

        // Ignore first 3872 floats
        randomAfterEvent = RandomAfterEvent(dr, { stimOnOffset }, 3872, 60000)

        testData = randomAfterEvent.extractData(listOf(trials[0], trials[1]))

        assert(testData[0].orientation == 0)

        assert(testData[0].spikeData[0][0].waveform[0] almostEqual 66.00766)
        assert(testData[0].spikeData[0][0].waveform[1] almostEqual 65.00742)
        assert(testData[0].spikeData[0][0].waveform[2] almostEqual 53.02333)
        assert(abs(testData[0].spikeData[0][0].waveform[19] - (-170.6389)) < 0.0001)
        assert(abs(testData[0].spikeData[0][0].waveform[20] - (-163.4559)) < 0.0001)
        assert(abs(testData[0].spikeData[0][0].waveform[21] - (-132.0076)) < 0.0001)

        assert(testData[0].spikeData[0][1].waveform[0] almostEqual 4.630025)
        assert(testData[0].spikeData[0][1].waveform[1] almostEqual 2.339633)
        assert(testData[0].spikeData[0][1].waveform[2] almostEqual 14.07165)
        assert(testData[0].spikeData[0][1].waveform[3] almostEqual 12.08744)

        assert(testData[0].spikeData[0][2].waveform[0] almostEqual 33.55904)
        assert(testData[0].spikeData[0][2].waveform[1] almostEqual 41.26452)
        assert(testData[0].spikeData[0][2].waveform[2] almostEqual 57.38702)
        assert(testData[0].spikeData[0][3].waveform[0] almostEqual 20.03858)
        assert(testData[0].spikeData[0][3].waveform[19] almostEqual -60.77736)


        assert(testData[1].orientation == 90)

        assert(testData[1].spikeData[0][0].waveform[0] almostEqual 4.989871)

        assert(testData[1].spikeData[0][0].waveform[1] almostEqual -4.603093)
        assert(testData[1].spikeData[0][0].waveform[2] almostEqual -8.502367)
        assert(testData[1].spikeData[0][0].waveform[19] almostEqual -57.72361)
        assert(testData[1].spikeData[0][0].waveform[20] almostEqual -54.39449)
        assert(testData[1].spikeData[0][0].waveform[21] almostEqual -39.1695)

        randomAfterEvent = RandomAfterEvent(dr, { stimOnOffset }, 6000, 60000)
        testData = randomAfterEvent.extractData(listOf(trials[0], trials[1]))

        assert(testData[0].spikeData[0][0].waveform[0] almostEqual 20.03858)
        assert(testData[0].spikeData[0][0].waveform[1] almostEqual 29.60803)
        assert(testData[0].spikeData[0][1].waveform[0] almostEqual 21.91661)
        assert(testData[0].spikeData[0][1].waveform[1] almostEqual 27.95836)

        assert(testData[0].spikeData[1][0].waveform[0] almostEqual 9.352667)
        assert(testData[0].spikeData[1][0].waveform[1] almostEqual 24.95034)

        assert(testData[0].spikeData[1][1].waveform[0] almostEqual 16.00261)
        assert(testData[0].spikeData[1][1].waveform[1] almostEqual 27.40732)

        randomAfterEvent = RandomAfterEvent(dr, { trialStartOffset }, 0, 4000)
        testData = randomAfterEvent.extractData(listOf(trials[0], trials[1]))

        assert(testData[0].spikeData[0][0].waveform[0] almostEqual -9.292385)
        assert(testData[0].spikeData[0][0].waveform[1] almostEqual -2.997954)
        assert(testData[0].spikeData[0][1].waveform[0] almostEqual -24.78477)
        assert(testData[0].spikeData[0][1].waveform[1] almostEqual -2.421317)
//        print(testData[0].spikeData[0][0])

    }
}