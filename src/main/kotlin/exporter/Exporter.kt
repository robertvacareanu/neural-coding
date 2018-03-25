package exporter

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
        ssdTimestamps.mapTo(spikeIndices) {
            spktweTimestamps.indexOf(it)
        }

        val spktweWaveform = readFloatBinary(spktwe.basePath + spktwe.spikeWaveformPath)

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