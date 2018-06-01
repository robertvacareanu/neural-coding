package exporter

import algorithm.cumulativeSum
import main.orientation
import reader.MetadataReader
import reader.readFloatBinary
import reader.readIntBinary
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by robert on 2/25/18.
 * Export the data in a csv manner. The results are put into a separate file
 */
fun exportCSV(data: List<Pair<Int, FloatArray>>, name: String) {
    File(name).bufferedWriter().use { br ->
        data.forEach {
            br.append(it.second.joinToString(",").plus(","))
            br.appendln(it.orientation.toString())
        }
    }
}

/**
 * Create a helper file similar to .spktwe, but for sorted data set
 */
fun exportSpikeSortedWaveform(mr: MetadataReader) {
    val ssd = mr.readSSD()
    val resultFile = File("${ssd.basePath + ssd.spikeTimestampsPath.dropLast(1)}w")
    if (!resultFile.exists()) {
        val spktwe = mr.readSPKTWE()
        val ssdTimestamps = readIntBinary(ssd.basePath + ssd.spikeTimestampsPath)
        val spktweTimestamps = readIntBinary(spktwe.basePath + spktwe.spikeTimestampsPath)
        val spikeIndices = arrayListOf<Int>()

        //Split spikes based on channel
        val cumulativeSumUnsorted = spktwe.spikesInEachChannel.cumulativeSum()
        val spktweTimestampsSplit = mutableMapOf<String, IntArray>()
        spktweTimestampsSplit[spktwe.storedChannelNames[0]] = spktweTimestamps.sliceArray(0 until cumulativeSumUnsorted[0])
        (0 until cumulativeSumUnsorted.size - 1).forEach {
            spktweTimestampsSplit[spktwe.storedChannelNames[it + 1]] = spktweTimestamps.sliceArray(cumulativeSumUnsorted[it] until cumulativeSumUnsorted[it + 1])
        }

        val cumulativeSumSorted = ssd.spikesPerUnit.cumulativeSum()
        val spktweWaveform = readFloatBinary(spktwe.basePath + spktwe.spikeWaveformPath)

        (0 until ssdTimestamps.size).forEach { timestampIndex ->
            val unit = cumulativeSumSorted.indexOfFirst { timestampIndex < it }
            val originName = ssd.origins[unit]
            val channel = spktwe.storedChannelNames.indexOf(originName)

            if (channel == 0) {
                spikeIndices.add(spktweTimestampsSplit[originName]!!.indexOf(ssdTimestamps[timestampIndex]))
            } else {
                spikeIndices.add(cumulativeSumUnsorted[channel - 1] + spktweTimestampsSplit[originName]!!.indexOf(ssdTimestamps[timestampIndex]))

            }
        }

        val waveformLength = ssd.waveformLength
        val bb = ByteBuffer.allocate(waveformLength * 4)
        bb.order(ByteOrder.LITTLE_ENDIAN)

        resultFile.outputStream().use { os ->
            (0 until spikeIndices.size).forEach {
                bb.asFloatBuffer().put(spktweWaveform.slice((waveformLength * spikeIndices[it]) until (waveformLength * (spikeIndices[it] + 1))).toFloatArray())
                os.write(bb.array())
                bb.clear()

            }
        }
    }
}