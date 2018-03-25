package reader

import main.almostEqual
import org.junit.Test

/**
 * Tested having the base path set to folder containing the data. In this case, will be set to ../M017
 * Data is not provided, hence no guarantees that it is tested with the same data it was tested initially
 */
class ChannelSpikeReaderTest {

    private val basePath = "../M017"


    @Test
    fun readerTest() {
        val mr = MetadataReader(basePath)
        val dr = DataChannelReader(mr.readEPD())

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

    }

}