package algorithm.extractor.value

import model.Spike
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Each value in the result float array represents the mean area (Integral) for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class MeanArea(private val waveformInternalSamplingFrequency: Float, private val spikeOffset: Int, private val emptyValue: Float = 0f) : ValueExtractor<Spike, Float> {
    override fun extractValue(values: Array<Spike>): Float {
        infix operator fun Pair<Double, Double>.times(p: Pair<Double, Double>): Double = first * p.second - second * p.first
        return if (values.isNotEmpty()) {
            var validUnits = 0
            val result = values.fold(BigDecimal.ZERO) { acc, spike ->
                val points = spike.waveform.mapIndexed { index, float -> Pair(index / waveformInternalSamplingFrequency.toDouble(), float.toDouble()) }
                val onlySpike = isolate(points, spikeOffset)
                if (onlySpike.size > 2) {
                    val areaForSpike = mutableListOf<Double>()
                    (0 until onlySpike.size - 1).mapTo(areaForSpike) {
                        onlySpike[it] * onlySpike[it + 1]
                    }
                    areaForSpike.add(onlySpike.last() * onlySpike.first())
                    validUnits++
                    acc + (areaForSpike.fold(BigDecimal.ZERO) { spikeAcc, data -> spikeAcc + BigDecimal.valueOf(data) })
                } else {
                    acc
                }

            }
            if(validUnits == 0) {
                emptyValue
            } else {
                result.divide(BigDecimal.valueOf(2 * validUnits.toLong()), 32, RoundingMode.HALF_UP).abs().toFloat()
            }
        } else {
            emptyValue
        }
    }

}