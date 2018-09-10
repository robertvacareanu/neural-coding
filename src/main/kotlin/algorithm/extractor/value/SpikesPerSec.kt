package algorithm.extractor.value

import model.Spike
import kotlin.math.min

/**
 * Each value in the result float array represents the number of spikes per second for a particular trial
 */
class SpikesPerSec(private val waveformInternalSamplingFrequency: Float) : ValueExtractor<Array<Spike>, Float> {
    override fun extractValue(values: Array<Spike>): Float =
            when {
                values.size > 1 -> min((values.size / ((values.last().timestamp - values.first().timestamp) / waveformInternalSamplingFrequency)).toFloat(), values.size.toFloat())
                values.size == 1 -> 1f
                else -> 0f
            }
}
