package model.metadata

/**
 * Created by robert on 3/23/18.
 * A data class that helps in treating the same both
 * sorted and unsorted spikes because even though they hold
 * data from different sources, they hold the same data: spike information
 */
data class SpikeMetadata(
        val basePath: String,
        val numberOfUnits: Int,
        val spikesPerUnit: List<Int>,
        val spikeSamplingFrequency: Float,
        val waveformInternalSamplingFrequency: Float,
        val waveformLength: Int,
        val waveformSpikeOffset: Int,
        val spikeTimestampsPath: String,
        val eventTimestampsPath: String,
        val spikeWaveformPath: String
        ) {

    constructor(unsorted: SpktweMetadata): this(
            basePath = unsorted.basePath,
            numberOfUnits = unsorted.storedChannels,
            spikesPerUnit = unsorted.spikesInEachChannel,
            spikeSamplingFrequency = unsorted.spikeTimesSampleFrequency,
            waveformInternalSamplingFrequency = unsorted.waveformInternalSamplingFrequency,
            waveformLength = unsorted.waveformLength,
            waveformSpikeOffset = unsorted.waveformSpikeOffset,
            spikeTimestampsPath = unsorted.spikeTimestampsPath,
            eventTimestampsPath = unsorted.eventTimestampsPath,
            spikeWaveformPath = unsorted.spikeWaveformPath
            )

    constructor(sorted: SsdMetadata): this(
            basePath = sorted.basePath,
            numberOfUnits = sorted.numberOfUnits,
            spikesPerUnit = sorted.spikesPerUnit,
            spikeSamplingFrequency = sorted.spikeSamplingFrequency,
            waveformInternalSamplingFrequency = sorted.waveformInternalSamplingFrequency,
            waveformLength = sorted.waveformLength,
            waveformSpikeOffset = sorted.waveformSpikeOffset,
            spikeTimestampsPath = sorted.spikeTimestampsPath,
            eventTimestampsPath = sorted.eventTimestampsPath,
            spikeWaveformPath = sorted.spikeWaveformPath
            )

}