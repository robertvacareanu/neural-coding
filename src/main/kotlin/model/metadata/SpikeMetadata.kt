package model.metadata

/**
 * Created by robert on 1/13/18.
 * Specific for .spktwe files
 */
data class SpikeMetadata(val basePath: String,
                         val version: Float,
                         val storedChannels: Int,
                         val storedChannelNames: List<String>,
                         val spikesInEachChannel: List<Int>,
                         val spikeTimesSampleFrequency: Float,
                         val recordingLength: Int,
                         val waveformInternalSamplingFrequency: Float,
                         val waveformLength: Int,
                         val waveformSpikeOffset: Int,
                         val eventsSamplingFrequency: Float,
                         val events: Int,
                         val spikeTimestampsPath: String,
                         val spikeWaveformPath: String,
                         val eventTimestampsPath: String,
                         val eventCodesPath: String
                    )