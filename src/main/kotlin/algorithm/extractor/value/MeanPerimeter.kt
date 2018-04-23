package algorithm.extractor.value

import model.Spike
import java.awt.geom.Point2D
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Each value in the result float array represents the mean perimeter for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class MeanPerimeter(private val waveformInternalSamplingFrequency: Float, private val spikeOffset: Int, private val emptyValue: Float = 0f) : ValueExtractor<Spike, Float> {
    override fun extractValue(values: Array<Spike>): Float =
            if (values.isNotEmpty()) {
                values.fold(BigDecimal.ZERO) { acc, spike ->
                    val points = spike.waveform.mapIndexed { index, float -> Pair(spike.timestamp + (index / waveformInternalSamplingFrequency.toDouble()), float.toDouble()) }
                    val onlySpikes = isolate(points, spikeOffset)
                    val perimeterForSpike = mutableListOf<Double>()
                    (0 until onlySpikes.size - 1).mapTo(perimeterForSpike) {
                        Point2D.distance(onlySpikes[it].first, onlySpikes[it].second, onlySpikes[it + 1].first, onlySpikes[it + 1].second)
                    }
                    acc + (perimeterForSpike.fold(BigDecimal.ZERO) { spikeAcc, data -> spikeAcc + BigDecimal.valueOf(data) })
                }.divide(BigDecimal.valueOf(values.size.toLong()), 6, RoundingMode.HALF_UP).toFloat()
            } else {
                emptyValue
            }
}