package reader

import main.almostEqual
import model.metadata.SpikeMetadata
import org.junit.Test

/**
 * Created by robert on 3/23/18.
 */
class MetadataChannelSpikeReaderTest {

    private val basePath = "../M017"

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
    fun spikeMetadataTest() {
        val mr = MetadataReader(basePath)
        val ssd = mr.readSSD()
        val spktwe = mr.readSPKTWE()

        var spikeMetadata = SpikeMetadata(ssd)

        assert(spikeMetadata.basePath == ssd.basePath)
        assert(spikeMetadata.numberOfUnits == ssd.numberOfUnits)
        assert(spikeMetadata.spikesPerUnit.joinToString() == ssd.spikesPerUnit.joinToString())
        assert(spikeMetadata.spikeSamplingFrequency == ssd.spikeSamplingFrequency)
        assert(spikeMetadata.waveformInternalSamplingFrequency == ssd.waveformInternalSamplingFrequency)
        assert(spikeMetadata.waveformLength == ssd.waveformLength)
        assert(spikeMetadata.waveformSpikeOffset == ssd.waveformSpikeOffset)
        assert(spikeMetadata.spikeTimestampsPath == ssd.spikeTimestampsPath)
        assert(spikeMetadata.eventTimestampsPath == ssd.eventTimestampsPath)
        assert(spikeMetadata.spikeWaveformPath == ssd.spikeWaveformPath)


        spikeMetadata = SpikeMetadata(spktwe)

        assert(spikeMetadata.basePath == spktwe.basePath)
        assert(spikeMetadata.numberOfUnits == spktwe.storedChannels)
        assert(spikeMetadata.spikesPerUnit.joinToString() == spktwe.spikesInEachChannel.joinToString())
        assert(spikeMetadata.spikeSamplingFrequency == spktwe.spikeTimesSampleFrequency)
        assert(spikeMetadata.waveformInternalSamplingFrequency == spktwe.waveformInternalSamplingFrequency)
        assert(spikeMetadata.waveformLength == spktwe.waveformLength)
        assert(spikeMetadata.waveformSpikeOffset == spktwe.waveformSpikeOffset)
        assert(spikeMetadata.spikeTimestampsPath == spktwe.spikeTimestampsPath)
        assert(spikeMetadata.eventTimestampsPath == spktwe.eventTimestampsPath)
        assert(spikeMetadata.spikeWaveformPath == spktwe.spikeWaveformPath)

    }

}