package model.metadata

/**
 * Created by robert on 1/4/18.
 * Specific for .epd files
 */
data class WaveformMetadata(val basePath: String,
                            val version: Float,
                            val eegChannels: Int,
                            val samplingFrequency: Float,
                            val samples: Int,
                            val channelPaths: List<String>,
                            val timestampPath: String,
                            val eventCodePath: String,
                            val events: Int,
                            val eegChannelsLabels: List<String>,
                            val channelsUsedForAverage: Int,
                            val channelsUsedForAverageNames: List<String>
)