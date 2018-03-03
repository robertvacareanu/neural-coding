package reader

import model.Segment
import model.Spike
import model.Trial
import model.metadata.EtiMetadata
import model.metadata.SpikeMetadata
import model.metadata.SpikeSortedMetadata
import model.metadata.WaveformMetadata
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import kotlin.math.absoluteValue

/**
 * Created by robert on 1/13/18.
 * Able to read retrieve the necessary data for performing data and feature extraction
 */
class DataReader(private val waveformMetadata: WaveformMetadata, private val spikeSortedMetadata: SpikeSortedMetadata, val spikeMetadata: SpikeMetadata, private val etiMetadata: EtiMetadata) : Reader {


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

    private fun readFloatBinary(filePath: String, between: IntRange): FloatArray {
        val result = arrayListOf<Float>()
        val f = File(filePath)
        f.inputStream().channel.use {
            val bb = ByteBuffer.allocate(4 * (between.last - between.first + 1))
            it.position((between.first * 4).toLong())
            bb.order(ByteOrder.LITTLE_ENDIAN)
            while (it.read(bb) > 0 && it.position() <= 4 * between.last + 4) {
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

    private fun readIntBinary(filePath: String, between: IntRange): IntArray {
        val result = arrayListOf<Int>()

        val f = File(filePath)
        f.inputStream().channel.use {
            val bb = ByteBuffer.allocate(4 * (between.last - between.first + 1))
            it.position((between.first * 4).toLong())
            bb.order(ByteOrder.LITTLE_ENDIAN)
            while (it.read(bb) > 0 && it.position() <= 4 * between.last + 4) {
                bb.flip()
                (0 until bb.limit() step 4).mapTo(result) { bb.getInt(it) }
                bb.clear()
            }
        }
        return result.toIntArray()
    }

    override fun readChannelWaveform(channel: Int): FloatArray = readFloatBinary(waveformMetadata.basePath + waveformMetadata.channelPaths[channel - 1])

    private fun channelSpikeCountUntil(channel: Int): Int {
        return if (channel > 0 && channel <= spikeMetadata.spikesInEachChannel.size) {
            spikeMetadata.spikesInEachChannel.take(channel - 1).sum()
        } else if (channel > spikeMetadata.spikesInEachChannel.size) {
            spikeMetadata.spikesInEachChannel.sum()
        } else {
            0
        }
    }

    /**
     * @param channel number of the channel to read data
     * It is expected that this number corresponds to the ones in metadata
     * Hence the first channel is 1. the second is 2 etc
     */
    override fun readChannelSpikes(channel: Int): List<Spike> {

        val result = mutableListOf<Spike>()

        val spikeCountUntil = channelSpikeCountUntil(channel)
        val spikeCountOffset = spikeMetadata.waveformLength * spikeCountUntil
        val spikew = readFloatBinary(spikeMetadata.basePath + spikeMetadata.spikeWaveformPath, spikeCountOffset until (spikeCountOffset + spikeMetadata.waveformLength * spikeMetadata.spikesInEachChannel[channel - 1]))
        val times = readIntBinary(spikeMetadata.basePath + spikeMetadata.spikeTimestampsPath, spikeCountUntil until (spikeCountUntil + spikeMetadata.spikesInEachChannel[channel - 1]))
        for (spikeIndex in 0 until spikew.size step spikeMetadata.waveformLength) {
            val spikeData = mutableListOf<Float>()
            (0 until spikeMetadata.waveformLength).mapTo(spikeData) {
                spikew[spikeIndex + it]
            }
            if(times[spikeIndex / spikeMetadata.waveformLength] == 19232492) {
                println("HER123132E")
            }
            result.add(Spike(times[spikeIndex / spikeMetadata.waveformLength].toDouble(), spikeData.toFloatArray()))
        }
        return result.toList()
    }

    /**
     * @param channel from 1 to the size defined in metadata
     */
    override fun readChannelWaveform(channel: Int, between: IntRange): Segment = Segment(between.first, readFloatBinary(waveformMetadata.basePath + waveformMetadata.channelPaths[channel - 1], between))


    override fun readChannelSpikes(channel: Int, between: IntRange): List<Spike> {
        val result = mutableListOf<Spike>()

        if (between.last - between.first > 0) {

            val spikeCountUntil = channelSpikeCountUntil(channel)
            val spikeCount = spikeMetadata.waveformLength * (spikeCountUntil + between.first)
            val spikew = readFloatBinary(spikeMetadata.basePath + spikeMetadata.spikeWaveformPath,
                    spikeCount until (spikeCount + spikeMetadata.waveformLength * (between.last - between.first + 1)))

            val times = readIntBinary(spikeMetadata.basePath + spikeMetadata.spikeTimestampsPath, spikeCountUntil + between.first until (spikeCountUntil + between.last + 1))
////            if((spikew[1].absoluteValue - (-11.35969).absoluteValue) < 0.0000001) {
////                println("$channel HERE ${between.first}, ${between.last}, ${spikeCount}, ${spikeCount + spikeMetadata.waveformLength * (between.last - between.first + 1)}, ${spikew[1]}, ${(spikew[1].absoluteValue - (-11.35969).absoluteValue)}")
////            }
//            if((times.size == 2) and (channel == 7)) {
//                println("${times[0]}, ${times[1]}, ${spikew[0]}, ${spikew[1]}")
//            }
            for (spikeIndex in 0 until spikew.size step spikeMetadata.waveformLength) {
                val spikeData = mutableListOf<Float>()
                (0 until spikeMetadata.waveformLength).mapTo(spikeData) {
                    spikew[spikeIndex + it]
                }
                result.add(Spike(times[spikeIndex / spikeMetadata.waveformLength].toDouble(), spikeData.toFloatArray()))
//                if((times.size == 2) and (channel == 7)) {
//                    println("${times[0]}, ${times[1]}, ${spikew[0]}, ${spikew[1]} ${result.last().timestamp} ${result.first().timestamp} ${19232491.toFloat()}")
//                }
            }
        }
        return result.toList()
    }

//    override fun readChannelSpikes(channel: Int, between: IntRange): List<Spike> {
//        val result = mutableListOf<Spike>()
//
//        if (between.last - between.first > 0) {
//
//            val spikeCountUntil = channelSpikeCountUntil(channel)
//            val spikeCount = spikeMetadata.waveformLength * (spikeCountUntil + between.first)
//            val spikew = readFloatBinary(spikeMetadata.basePath + spikeMetadata.spikeWaveformPath,
//                    spikeCount until (spikeCount + spikeMetadata.waveformLength * (between.last - between.first + 1)))
//
//            val times = readIntBinary(spikeMetadata.basePath + spikeMetadata.spikeTimestampsPath)
//            for (spikeIndex in 0 until spikew.size step spikeMetadata.waveformLength) {
//                val spikeData = mutableListOf<Float>()
//                (0 until spikeMetadata.waveformLength).mapTo(spikeData) {
//                    spikew[spikeIndex + it]
//                }
//                result.add(Spike(times[spikeIndex / spikeMetadata.waveformLength].toFloat(), spikeData.toFloatArray()))
//            }
//        }
//        return result.toList()
//    }

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

    override fun numberOfChannels() = waveformMetadata.eegChannels

    override fun readSpikeTimestamps(): List<IntArray> = (1..spikeMetadata.storedChannels).map {
        readIntBinary(spikeMetadata.basePath + spikeMetadata.spikeTimestampsPath, channelSpikeCountUntil(it) until channelSpikeCountUntil(it + 1))
    }
}
