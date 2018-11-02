package algorithm.extractor.value

import model.Spike
import kotlin.math.abs

/**
 * Each value in the result float array represents the mean amplitude for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class Amplitude(private val spikeOffset: Int) : ValueExtractor<Spike, Float> {
    override fun extractValue(values: Spike): Float {
        return abs(values[spikeOffset])
    }
}