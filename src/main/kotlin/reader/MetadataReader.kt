package reader

import model.metadata.SpikeMetadata
import model.metadata.SpikeSortedMetadata
import model.metadata.WaveformMetadata
import java.io.File

/**
 * Created by robert on 1/13/18.
 * Expects path to folder containing the files and every path
 * contained in a metadata file to be valid relative to the
 * metadata path.
 * @link SpikeMetadata
 * and
 * @link SpikeSortedMetadata
 * are expected to have their files together
 *
 */
class MetadataReader {

    private val ssdPath: String
    private val spktwePath: String
    private val epdPath: String


    constructor(basepath: String) {
        val file = File(basepath)
        val paths = mutableListOf<String>()
        file.walkTopDown().filter { it.name.endsWith(".epd") or it.name.endsWith(".spktwe") or it.name.endsWith("ssd") }.map { it.canonicalPath }.toCollection(paths)

        ssdPath = paths.first { it.endsWith(".ssd") }
        spktwePath = paths.first { it.endsWith("spktwe") }
        epdPath = paths.first { it.endsWith("epd") }

    }

    fun readSPKTWE(): SpikeMetadata {
        val f = File(spktwePath)
        val lines = f.readLines()
        val start = 2
        val offset = 3


        val version = lines[start].toFloat()
        val dataChannels = lines[start + offset].toInt()

        val storedChannelsNames = mutableListOf<String>()
        ((start + 2 * offset) until (start + 2 * offset + dataChannels)).mapTo(storedChannelsNames) { lines[it] }


        val spikesInEachChanel = mutableListOf<Int>()
        ((start + 3 * offset + (dataChannels - 1)) until (start + 3 * offset + (2 * dataChannels - 1))).mapTo(spikesInEachChanel) { lines[it].toInt() }

        val spikeTimeSampling = lines[(start + 4 * offset + (2 * dataChannels - 2))].toFloat()

        val recordLength = lines[(start + 5 * offset + (2 * dataChannels - 2))].toInt()

        val waveformInternalSampling = lines[(start + 6 * offset + (2 * dataChannels - 2))].toFloat()


        val waveformLength = lines[(start + 7 * offset + (2 * dataChannels - 2))].toInt()

        val waveformSpikeOffset = lines[(start + 8 * offset + (2 * dataChannels - 2))].toInt()

        val eventsSamplingFrequency = lines[(start + 9 * offset + (2 * dataChannels - 2))].toFloat()

        val events = lines[(start + 10 * offset + (2 * dataChannels - 2))].toInt()

        val spikeTimestampsPath = lines[(start + 11 * offset + (2 * dataChannels - 2))]
        val spikeWaveformsPath = lines[(start + 12 * offset + (2 * dataChannels - 2))]
        val eventTimestampPath = lines[(start + 13 * offset + (2 * dataChannels - 2))]
        val codeEventsPath = lines[(start + 14 * offset + (2 * dataChannels - 2))]



        return SpikeMetadata(basePath(spktwePath),
                version, dataChannels, storedChannelsNames, spikesInEachChanel, spikeTimeSampling,
                recordLength, waveformInternalSampling, waveformLength, waveformSpikeOffset, eventsSamplingFrequency,
                events, spikeTimestampsPath, spikeWaveformsPath, eventTimestampPath, codeEventsPath)
    }

    fun readEPD(): WaveformMetadata {
        val f = File(epdPath)
        val lines = f.readLines()

        val start = 2
        val offset = 3

        val version = lines[start].toFloat()
        val eegChannels = lines[start + offset].toInt()
        val frequency = lines[start + 2 * offset].toFloat()
        val samples = lines[start + 3 * offset].toInt()
        val channelPaths = mutableListOf<String>()

        ((start + 4 * offset) until (start + 4 * offset + eegChannels)).mapTo(channelPaths) { lines[it] }

        val timestampPath = lines[start + 5 * offset + (eegChannels - 1)]

        val eventCodePath = lines[start + 6 * offset + (eegChannels - 1)]

        val events = lines[start + 7 * offset + (eegChannels - 1)].toInt()


        val eegChannelsLabel = mutableListOf<String>()

        ((start + 8 * offset + (eegChannels - 1)) until ((start + 8 * offset + (2 * eegChannels - 1)))).mapTo(eegChannelsLabel) { lines[it] }

        val averageChannelsUsed = lines[(start + 9 * offset + (2 * eegChannels - 2))].toInt()

        val averageChannelsUsedNames = mutableListOf<String>()

        if (averageChannelsUsed != 0) {
            ((start + 10 * offset + (2 * eegChannels - 2)) until ((start + 10 * offset + (2 * eegChannels - 2)) + averageChannelsUsed)).mapTo(averageChannelsUsedNames) { lines[it] }
        }

        return WaveformMetadata(basePath(epdPath), version, eegChannels, frequency, samples, channelPaths, timestampPath, eventCodePath, events, eegChannelsLabel, averageChannelsUsed, averageChannelsUsedNames)

    }

    fun readSSD(): SpikeSortedMetadata {
        val f = File(ssdPath)

        val lines = f.readLines()//.filter { it != "" }
        val start = 2
        val offset = 3

        val version = lines[start].toFloat()
        val numberOfUnits = lines[start + offset].toInt()

        val unitNames = mutableListOf<String>()
        ((start + 2 * offset) until (start + 2 * offset + numberOfUnits)).mapTo(unitNames) { lines[it] }

        val unitOriginator = mutableListOf<String>()
        ((start + 3 * offset + (numberOfUnits - 1)) until ((start + 3 * offset + (2 * numberOfUnits - 1)))).mapTo(unitOriginator) { lines[it] }

        val spikesInEachUnit = mutableListOf<Int>()
        ((start + 4 * offset + (2 * numberOfUnits - 2)) until ((start + 4 * offset + (3 * numberOfUnits - 2)))).mapTo(spikesInEachUnit) { lines[it].toInt() }

        val spikeTimeSamplingFrequency = lines[(start + 5 * offset + (3 * numberOfUnits - 3))].toFloat()

        val waveformInternalSamplingFrequency = lines[(start + 6 * offset + (3 * numberOfUnits - 3))].toFloat()

        val waveformLengthSamples = lines[(start + 7 * offset + (3 * numberOfUnits - 3))].toInt()

        val waveformSpikeAlignOffset = lines[(start + 8 * offset + (3 * numberOfUnits - 3))].toInt()

        val eventSize = lines[(start + 9 * offset + (3 * numberOfUnits - 3))].toInt()

        val spikeTimestampPath = lines[(start + 10 * offset + (3 * numberOfUnits - 3))]

        val unitStatisticsPath = lines[(start + 11 * offset + (3 * numberOfUnits - 3))]

        val eventTimestampPath = lines[(start + 12 * offset + (3 * numberOfUnits - 3))]

        val eventCodesPath = lines[(start + 13 * offset + (3 * numberOfUnits - 3))]


        return SpikeSortedMetadata(basePath(spktwePath), version, numberOfUnits, unitNames, unitOriginator, spikesInEachUnit,
                spikeTimeSamplingFrequency, waveformInternalSamplingFrequency, waveformLengthSamples, waveformSpikeAlignOffset,
                eventSize, spikeTimestampPath, unitStatisticsPath, eventTimestampPath, eventCodesPath)
    }


    private fun basePath(relativeTo: String) = relativeTo.replaceAfterLast(File.separator, "")

}