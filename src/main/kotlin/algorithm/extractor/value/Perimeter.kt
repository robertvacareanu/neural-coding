package algorithm.extractor.value

import model.Spike
import java.awt.geom.Point2D
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Each value in the result float array represents the mean perimeter for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class Perimeter(private val waveformInternalSamplingFrequency: Float, private val spikeOffset: Int, private val emptyValue: Float = 0f) : ValueExtractor<Spike, Float> {
    override fun extractValue(spike: Spike): Float {
        val points = spike.waveform.mapIndexed { index, float -> Pair(spike.timestamp + (index / waveformInternalSamplingFrequency.toDouble()), float.toDouble()) }
        val onlySpikes = isolate(points, spikeOffset)
        val perimeterForSpike = mutableListOf<Double>()
        (0 until onlySpikes.size - 1).mapTo(perimeterForSpike) {
            Point2D.distance(onlySpikes[it].first, onlySpikes[it].second, onlySpikes[it + 1].first, onlySpikes[it + 1].second)
        }
        return perimeterForSpike.sum().toFloat()

    }
}