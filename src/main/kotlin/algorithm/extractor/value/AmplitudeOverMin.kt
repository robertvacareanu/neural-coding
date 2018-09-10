package algorithm.extractor.value

import model.Spike
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

/**
 * Created by robert on 8/1/18.
 */

/**
 * Each value in the result float array represents the mean amplitude for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class AmplitudeOverMin(private val spikeOffset: Int, private val emptyValue: Float = 0f) : ValueExtractor<Spike, Float> {
    override fun extractValue(spike: Spike): Float {
        val slice = spike.waveform.slice((spikeOffset - 2) until spike.waveform.size)
        val v1 = spike[spikeOffset].toDouble()
        val v2 = slice.max()!!.toDouble()
        val max = max(v1, v2)
        val min = min(v1, v2)
        return abs(max-min).toFloat()
    }

}