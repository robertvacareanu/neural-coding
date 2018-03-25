package reader.spikes

import model.Spike
import model.Trial
import model.metadata.EtiMetadata
import model.metadata.SpikeMetadata
import reader.readFloatBinary
import reader.readIntBinary
import java.io.File

/**
 * Created by robert on 3/23/18.
 * Provides the same reader for accessing both sorted and unsorted data sets
 */
class DataSpikeReader(private val etiMetadata: EtiMetadata, private val spikeMetadata: SpikeMetadata) : SpikeReader {

    private fun spikeCountUntil(channel: Int): Int =
        if (channel > 0 && channel <= spikeMetadata.spikesPerUnit.size) {
            spikeMetadata.spikesPerUnit.take(channel - 1).sum()
        } else if (channel > spikeMetadata.spikesPerUnit.size) {
            spikeMetadata.spikesPerUnit.sum()
        } else {
            0
        }

    override fun readSpikes(fromUnit: Int, between: IntRange): List<Spike> {
        val result = mutableListOf<Spike>()

        if (between.last - between.first > 0) {

            val spikeCountUntil = spikeCountUntil(fromUnit)
            val spikeCount = spikeMetadata.waveformLength * (spikeCountUntil + between.first)
            val spikew = readFloatBinary(spikeMetadata.basePath + spikeMetadata.spikeWaveformPath,
                    spikeCount until (spikeCount + spikeMetadata.waveformLength * (between.last - between.first + 1)))

            val times = readIntBinary(spikeMetadata.basePath + spikeMetadata.spikeTimestampsPath, spikeCountUntil + between.first until (spikeCountUntil + between.last + 1))
            for (spikeIndex in 0 until spikew.size step spikeMetadata.waveformLength) {
                val spikeData = mutableListOf<Float>()
                (0 until spikeMetadata.waveformLength).mapTo(spikeData) {
                    spikew[spikeIndex + it]
                }
                result.add(Spike(times[spikeIndex / spikeMetadata.waveformLength].toDouble(), spikeData.toFloatArray()))
            }
        }
        return result.toList()
    }

    override fun readSpikeTimestamps(): List<IntArray> = (1..spikeMetadata.numberOfUnits).map {
        readIntBinary(spikeMetadata.basePath + spikeMetadata.spikeTimestampsPath, spikeCountUntil(it) until spikeCountUntil(it + 1))
    }

    override fun readTrials(): List<Trial> {
        val result = mutableListOf<Trial>()
        val eti = File(etiMetadata.path)
        val timestamps = readIntBinary(spikeMetadata.basePath + spikeMetadata.eventTimestampsPath)
        eti.readLines().drop(4).mapIndexedTo(result,
                { index, line ->
                    val strings = line.split(",")
                    val timestampIndex = 4 * index
                    Trial(trialNumber = strings[0].toInt(),
                            orientation = strings[3].split(" ").last().toInt(),
                            trialStartOffset = timestamps[timestampIndex],
                            stimOnOffset = timestamps[timestampIndex + 1],
                            stimOffOffset = timestamps[timestampIndex + 2],
                            trialEndOffset = timestamps[timestampIndex + 3])
                }
        )
        return result
    }

    override fun numberOfUnits(): Int = spikeMetadata.numberOfUnits
}