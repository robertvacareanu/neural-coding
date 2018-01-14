package reader

import model.Spike
import model.metadata.SpikeMetadata
import model.metadata.SpikeSortedMetadata
import model.metadata.WaveformMetadata
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartUtilities
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.Color
import java.awt.Paint
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by robert on 1/13/18.
 */
class DataReader(private val waveformMetadata: WaveformMetadata, private val spikeSortedMetadata: SpikeSortedMetadata, private val spikeMetadata: SpikeMetadata) : Reader {


    private fun readFloatBinary(filePath: String): FloatArray {
        val result = arrayListOf<Float>()
        val f = File(filePath)
        f.inputStream().channel.use {
            val bb = ByteBuffer.allocate(1024 * 1024)
            bb.order(ByteOrder.LITTLE_ENDIAN)
            while (it.read(bb) > 0) {
                bb.flip()
                (0 until bb.limit() step 4).mapTo(result) { bb.getFloat(it) }
                bb.clear()
            }
        }
        return result.toFloatArray()
    }

    private fun readIntBinary(filePath: String): IntArray {
        val result = arrayListOf<Int>()

        val f = File(filePath)
        f.inputStream().channel.use {
            val bb = ByteBuffer.allocate(1024 * 1024)
            bb.order(ByteOrder.LITTLE_ENDIAN)
            while (it.read(bb) > 0) {
                bb.flip()
                (0 until bb.limit() step 4).mapTo(result) { bb.getInt(it) }
                bb.clear()
            }
        }
        return result.toIntArray()
    }

    override fun readChannelWaveform(channel: Int): FloatArray = readFloatBinary(waveformMetadata.basePath + waveformMetadata.channelPaths[channel-1])

    private fun channelSpikeCount(channel: Int): Int {
        val label = waveformMetadata.eegChannelsLabels[channel - 1]
        return (0 until spikeSortedMetadata.origins.size).filter { spikeSortedMetadata.origins[it] == label }.map { spikeSortedMetadata.spikesPerUnit[it] }.sum()
    }

    /**
     * @param channel number of the channel to read data
     * It is expected that this number corresponds to the ones in metadata
     * Hence the first channel is 1. the second is 2 etc
     */
    override fun readChannelSpikes(channel: Int): List<Spike> {

        val result = mutableListOf<Spike>()

        val spikew = readFloatBinary(spikeMetadata.basePath + spikeMetadata.spikeWaveformPath)
        val times = readIntBinary(spikeMetadata.basePath + spikeMetadata.spikeTimestampsPath)

        for (float in 0 until spikew.size step 39) {
            val spikeData = mutableListOf<Float>()
            (0 until 39).mapTo(spikeData) {
                spikew[float + it]
            }
            result.add(Spike(times[float / 39] / waveformMetadata.samplingFrequency, spikeData.toFloatArray()))
        }

        return result.toList()
    }
}