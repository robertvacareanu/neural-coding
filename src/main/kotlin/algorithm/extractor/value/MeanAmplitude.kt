package algorithm.extractor.value

import model.Spike
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

/**
 * Each value in the result float array represents the mean amplitude for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class MeanAmplitude(private val spikeOffset: Int, private val emptyValue: Float = 0f) : ValueExtractor<Spike, Float> {
    override fun extractValue(values: Array<Spike>): Float =
            if (values.isNotEmpty()) {
                val nominator = values.fold(BigDecimal.ZERO) { acc, spike ->
                    acc + (BigDecimal.valueOf(abs(spike[spikeOffset].toDouble())))
                }
                val denominator = BigDecimal.valueOf(values.size.toDouble())
                nominator.divide(denominator, 32, RoundingMode.HALF_UP).toFloat()
            } else {
                emptyValue
            }
}