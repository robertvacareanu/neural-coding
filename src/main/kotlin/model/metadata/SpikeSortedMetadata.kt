package model.metadata

/**
 * Created by robert on 1/5/18.
 * For .ssd files
 */
data class SpikeSortedMetadata(val basePath: String,
                               val version: Float,
                               val numberOfUnits: Int,
                               val unitsName: List<String>,
                               val origins: List<String>,
                               val spikesPerUnit: List<Int>,
                               val spikeSamplingFrequency: Float,
                               val waveformInternalSamplingFrequency: Float,
                               val waveformLength: Int,
                               val waveformSpikeOffset: Int,
                               val events: Int,
                               val spikeTimestampsPath: String,
                               val unitStatisticsPath: String,
                               val eventTimestampsPath: String,
                               val codeEventsPath: String) {
}