package algorithm.extractor.feature

import model.Spike
import model.TrialData
import java.awt.geom.Point2D
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

/**
 * Created by robert on 3/9/18.
 * An extractor which map a channel array of spikes to a single value
 */
sealed class SingleValueExtractor(private val extractor: (Array<Spike>) -> Float) : FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>> {

    override fun extract(data: Collection<TrialData>): List<Pair<Int, FloatArray>> = data.map { Pair(it.orientation, it.spikeData.map(extractor).toFloatArray()) }

}


/**
 * Each value in the result float array represents the mean amplitude for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class MeanAmplitude(private val spikeOffset: Int) : SingleValueExtractor({
    if (it.isNotEmpty()) {
        val nominator = it.fold(BigDecimal.ZERO) { acc, spike ->
            acc + (BigDecimal.valueOf(abs(spike[spikeOffset].toDouble())))
        }
        val denominator = BigDecimal.valueOf(it.size.toDouble())
        nominator.divide(denominator, 6, RoundingMode.HALF_UP).toFloat()
    } else {
        0f
    }
})

/**
 * Each value in the result float array represents the number of spikes per second for a particular trial
 */
class SpikesPerSec(private val waveformInternalSamplingFrequency: Float) : SingleValueExtractor({
    when {
        it.size > 1 -> (it.size / ((it.last().timestamp - it.first().timestamp) / waveformInternalSamplingFrequency)).toFloat()
        it.size == 1 -> 1f
        else -> 0f
    }
})

/**
 * Each value in the result float array represents the mean perimeter for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class MeanPerimeter(waveformInternalSamplingFrequency: Float) : SingleValueExtractor({
    if(it.isNotEmpty()) {
        it.fold(BigDecimal.ZERO) { acc, spike ->
            val points = spike.waveform.mapIndexed { index, float -> Pair((spike.timestamp + index) / waveformInternalSamplingFrequency, float.toDouble()) }
            val perimeterForSpike = mutableListOf<Double>()
            (0 until points.size - 1).mapTo(perimeterForSpike) {
                Point2D.distance(points[it].first, points[it].second, points[it + 1].first, points[it + 1].second)
            }
            acc + (perimeterForSpike.fold(BigDecimal.ZERO) { spikeAcc, data -> spikeAcc + BigDecimal.valueOf(data) })
        }.divide(BigDecimal.valueOf(it.size.toLong()), 6, RoundingMode.HALF_UP).toFloat()
    } else {
        0f
    }
})

/**
 * Each value in the result float array represents the mean area (Integral) for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class MeanArea(waveformInternalSamplingFrequency: Float) : SingleValueExtractor({
    infix operator fun Pair<Double, Double>.times(p: Pair<Double, Double>): Double = first * p.second - second * p.first
    it.fold(BigDecimal.ZERO) { acc, spike ->
        val points = spike.waveform.mapIndexed { index, float -> Pair((spike.timestamp + index) / waveformInternalSamplingFrequency, float.toDouble()) }
        val areaForSpike = mutableListOf<Double>()
        (0 until points.size - 1).mapTo(areaForSpike) {
            points[it] * points[it + 1]
        }
        acc + (areaForSpike.fold(BigDecimal.ZERO) { spikeAcc, data -> spikeAcc + BigDecimal.valueOf(data) })
    }.divide(BigDecimal.valueOf(2), 6, RoundingMode.HALF_UP).abs().toFloat()

})
