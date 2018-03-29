package algorithm.extractor.value

import model.Spike
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Each value in the result float array represents the mean area (Integral) for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class MeanArea(private val waveformInternalSamplingFrequency: Float, private val spikeOffset: Int) : ValueExtractor<Spike, Float> {
    override fun extractValue(values: Array<Spike>): Float {
        infix operator fun Pair<Double, Double>.times(p: Pair<Double, Double>): Double = first * p.second - second * p.first
        return if (values.isNotEmpty()) {
            var validUnits = 0
            values.fold(BigDecimal.ZERO) { acc, spike ->
                val points = spike.waveform.mapIndexed { index, float -> Pair(spike.timestamp + (index / waveformInternalSamplingFrequency.toDouble()), float.toDouble()) }//.filter { values.second < 0 }
                val onlySpike = isolate(points, spikeOffset)
                if (onlySpike.size > 2) {
                    val areaForSpike = mutableListOf<Double>()
                    (0 until onlySpike.size - 1).mapTo(areaForSpike) {
                        onlySpike[it] * onlySpike[it + 1]
                    }
                    validUnits++
                    acc + (areaForSpike.fold(BigDecimal.ZERO) { spikeAcc, data -> spikeAcc + BigDecimal.valueOf(data) })
                } else {
                    acc
                }
            }.divide(BigDecimal.valueOf(2 * validUnits.toLong()), 6, RoundingMode.HALF_UP).abs().toFloat()
        } else {
            0f
        }
    }

}