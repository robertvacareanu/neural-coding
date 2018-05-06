package reader

import main.almostEqual
import model.Trial
import model.metadata.SpikeMetadata
import org.junit.Test
import reader.spikes.DataSpikeReader

/**
 * Created by robert on 3/24/18.
 * Tests for SpikeReader class
 */
class SpikeChannelSpikeReaderTest {

    private val basePath = "../M017"

    @Test
    fun spikeReaderChannelTest() {
        val mr = MetadataReader(basePath)
        val spikeReader = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()))

        assert(spikeReader.numberOfUnits() == 32)


        val trials = spikeReader.readTrials()

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


        val timestamps = spikeReader.readSpikeTimestamps()

        val spikeChannel5 = spikeReader.readSpikes(5, 0 until 5)
        assert(spikeChannel5[0][0] almostEqual 22.97374)
        assert(spikeChannel5[0][1] almostEqual 25.70727)
        assert(spikeChannel5[0][2] almostEqual 16.16155)
        assert(spikeChannel5[0][3] almostEqual 9.342863)
        assert(spikeChannel5[0][4] almostEqual 4.83482)
        assert(spikeChannel5[0][5] almostEqual 1.805182)
        assert(spikeChannel5[0][6] almostEqual 12.34129)
        assert(spikeChannel5[0].timestamp almostEqual 106605)

        assert(spikeChannel5[1].timestamp almostEqual 126545)

        assert(spikeChannel5[4][0] almostEqual 11.16329)
        assert(spikeChannel5[4][1] almostEqual 27.94641)
        assert(spikeChannel5[4][2] almostEqual 10.15276)
        assert(spikeChannel5[4][3] almostEqual 15.08644)
        assert(spikeChannel5[4][4] almostEqual -1.438469)
        assert(spikeChannel5[4][5] almostEqual -6.500218)
        assert(spikeChannel5[4][6] almostEqual -3.021892)
        assert(spikeChannel5[4].timestamp almostEqual 150036)

    }

    @Test
    fun trialConstraintRead() {
        val mr = MetadataReader(basePath)
        val spikeReader = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSPKTWE()), listOf<Trial.() -> Boolean>({ contrast == 100 }))

        assert(spikeReader.readTrials().size == 80)

    }

    @Test
    fun spikeReaderSortedUnitsTest() {
        val mr = MetadataReader(basePath)
        val spikeReader = DataSpikeReader(mr.readETI(), SpikeMetadata(mr.readSSD()))

        assert(spikeReader.numberOfUnits() == 141)


        val trials = spikeReader.readTrials()

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

        val unit1 = spikeReader.readSpikes(1, 0 until 10)

        assert(unit1.size == 10)

        assert(unit1[0][0] almostEqual -13.31647)
        assert(unit1[0][1] almostEqual -12.56869)
        assert(unit1[0][2] almostEqual 1.286901)

        assert(unit1[1][0] almostEqual -3.287496)
        assert(unit1[1][1] almostEqual -4.707586)

        assert(unit1[2][0] almostEqual -9.292385)
        assert(unit1[2][1] almostEqual -2.997954)
        assert(unit1[2][2] almostEqual -3.34142)
        assert(unit1[2][3] almostEqual -4.909719)
        assert(unit1[2][4] almostEqual -2.706753)
        assert(unit1[2][5] almostEqual -1.738809)
        assert(unit1[2][6] almostEqual -6.388046)
        assert(unit1[2][7] almostEqual -17.49254)
        assert(unit1[2][8] almostEqual -2.756405)
        assert(unit1[2][9] almostEqual 16.97679)
        assert(unit1[2][10] almostEqual 10.23431)//352
        assert(unit1[2][19] almostEqual -50.21248)
        assert(unit1[2][20] almostEqual -32.21171)

        assert(unit1[6][0] almostEqual -2.155479)

        assert(unit1[9][0] almostEqual -2.255906)

        val unit2 = spikeReader.readSpikes(2, 0 until 10)

        assert(unit2[0][0] almostEqual -6.331959)
        assert(unit2[0][1] almostEqual 6.120698)
        assert(unit2[0][2] almostEqual -1.98232)

        assert(unit2[1][0] almostEqual -3.312609)
        assert(unit2[1][1] almostEqual 5.342195)

    }

}