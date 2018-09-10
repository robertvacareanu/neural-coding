package algorithm.extractor.value

import model.Spike
import java.lang.Math.abs
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Each value in the result float array represents the mean area (Integral) for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class Area(private val waveformInternalSamplingFrequency: Float, private val spikeOffset: Int, private val emptyValue: Float = 0f) : ValueExtractor<Spike, Float> {
    override fun extractValue(spike: Spike): Float {
        infix operator fun Pair<Double, Double>.times(p: Pair<Double, Double>): Double = first * p.second - second * p.first
        val points = spike.waveform.mapIndexed { index, float -> Pair(index / waveformInternalSamplingFrequency.toDouble(), float.toDouble()) }
        val onlySpike = isolate(points, spikeOffset)
        return if (onlySpike.size > 2) {
            val areaForSpike = mutableListOf<Double>()
            (0 until onlySpike.size - 1).mapTo(areaForSpike) {
                onlySpike[it] * onlySpike[it + 1]
            }
            areaForSpike.add(onlySpike.last() * onlySpike.first())
            abs(areaForSpike.sum().toFloat())
        } else{
            emptyValue
        }
    }

}