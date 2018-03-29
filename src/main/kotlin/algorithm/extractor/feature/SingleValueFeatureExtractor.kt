package algorithm.extractor.feature

import algorithm.extractor.value.ValueExtractor
import model.Spike
import model.TrialData
import java.awt.geom.Point2D
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.min

/**
 * Created by robert on 3/9/18.
 * An extractor which map a channel array of spikes to a single value
 */
class SingleValueFeatureExtractor : FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>> {

    override fun extract(data: Collection<TrialData>, extractor: (Array<Spike>) -> Float): List<Pair<Int, FloatArray>> = data.map { Pair(it.orientation, it.spikeData.map(extractor).toFloatArray()) }

}

///**
// * @param windowLength expected to be in floats. For example, for a sampling rate of 32000 floats / sec, 0.2 seconds will be 32000/5 = 6400
// * @param overlap in percentage, how much should the windows overlap. For example, a value of 0 would mean that there should be no overlapping,
// * however, a value of 0.5 would mean that if, for example first window goes from 0 to 100, the second one goes from 50 to 150, assuming that
// * the window length is 100
// */
//class WindowValueFeatureExtractor(private val windowLength: Int, private val overlap: Double) : FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>> {
//    override fun extract(data: Collection<TrialData>, extractor: (Array<Spike>) -> Float): List<Pair<Int, FloatArray>> {
//        return data.map {
//            val step = windowLength * overlap
//            val result = mutableListOf<Float>()
//
//            it.spikeData.forEach { array ->
//                var start = it.extractedBetween.first
//                var end = start + windowLength.toDouble()
//                val timestamps = array.map { it.timestamp }
//                val windowValues = mutableListOf<Float>()
//                val last = it.extractedBetween.second
//                while (start < last) {
//                    val from = timestamps.indexOfFirst { it >= start }
//                    var to = timestamps.indexOfFirst { it > end }
//                    if(end > last && to == -1) {
//                        to = timestamps.size
//                    }
//                    if ((from != -1) and (to != -1)) {
//                        println(array.size)
//                        val slice = array.sliceArray(from until to)
//                        val res = extractor(slice)
//                        windowValues.add(res)
//                    } else {
//                        windowValues.add(0f)
//                    }
//                    start += step
//                    end += step
//                }
//                result.addAll(windowValues)
//
//            }
//            println(result.size)
//            Pair(it.orientation, result.toFloatArray())
//        }
//    }
//}

//
///**
// * Each value in the result float array represents the mean amplitude for a particular trial
// * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
// */
//class MeanAmplitude(private val spikeOffset: Int) : ValueExtractor<Spike, Float> {
//    override fun extractValue(values: Array<Spike>): Float =
//            if (values.isNotEmpty()) {
//                val nominator = values.fold(BigDecimal.ZERO) { acc, spike ->
//                    acc + (BigDecimal.valueOf(abs(spike[spikeOffset].toDouble())))
//                }
//                val denominator = BigDecimal.valueOf(values.size.toDouble())
//                nominator.divide(denominator, 6, RoundingMode.HALF_UP).toFloat()
//            } else {
//                0f
//            }
//}
//
///**
// * Each value in the result float array represents the number of spikes per second for a particular trial
// */
//class SpikesPerSec(private val waveformInternalSamplingFrequency: Float) : ValueExtractor<Spike, Float> {
//    override fun extractValue(values: Array<Spike>): Float =
//            when {
//                values.size > 1 -> min((values.size / ((values.last().timestamp - values.first().timestamp) / waveformInternalSamplingFrequency)).toFloat(), values.size.toFloat())
//                values.size == 1 -> 1f
//                else -> 0f
//            }
//}
//
//
///**
// * Each value in the result float array represents the mean perimeter for a particular trial
// * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
// */
//class MeanPerimeter(private val waveformInternalSamplingFrequency: Float, private val spikeOffset: Int) : ValueExtractor<Spike, Float> {
//    override fun extractValue(values: Array<Spike>): Float =
//            if (values.isNotEmpty()) {
//                values.fold(BigDecimal.ZERO) { acc, spike ->
//                    val points = spike.waveform.mapIndexed { index, float -> Pair(spike.timestamp + (index / waveformInternalSamplingFrequency.toDouble()), float.toDouble()) }
//                    val onlySpikes = isolate(points, spikeOffset)
//                    val perimeterForSpike = mutableListOf<Double>()
//                    (0 until onlySpikes.size - 1).mapTo(perimeterForSpike) {
//                        Point2D.distance(onlySpikes[it].first, onlySpikes[it].second, onlySpikes[it + 1].first, onlySpikes[it + 1].second)
//                    }
//                    acc + (perimeterForSpike.fold(BigDecimal.ZERO) { spikeAcc, data -> spikeAcc + BigDecimal.valueOf(data) })
//                }.divide(BigDecimal.valueOf(values.size.toLong()), 6, RoundingMode.HALF_UP).toFloat()
//            } else {
//                0f
//            }
//}
//
///**
// * Each value in the result float array represents the mean area (Integral) for a particular trial
// * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
// */
//class MeanArea(private val waveformInternalSamplingFrequency: Float, private val spikeOffset: Int) : ValueExtractor<Spike, Float> {
//    override fun extractValue(values: Array<Spike>): Float {
//        infix operator fun Pair<Double, Double>.times(p: Pair<Double, Double>): Double = first * p.second - second * p.first
//        return if (values.isNotEmpty()) {
//            var validUnits = 0
//            values.fold(BigDecimal.ZERO) { acc, spike ->
//                val points = spike.waveform.mapIndexed { index, float -> Pair(spike.timestamp + (index / waveformInternalSamplingFrequency.toDouble()), float.toDouble()) }//.filter { values.second < 0 }
//                val onlySpike = isolate(points, spikeOffset)
//                if (onlySpike.size > 2) {
//                    val areaForSpike = mutableListOf<Double>()
//                    (0 until onlySpike.size - 1).mapTo(areaForSpike) {
//                        onlySpike[it] * onlySpike[it + 1]
//                    }
//                    validUnits++
//                    acc + (areaForSpike.fold(BigDecimal.ZERO) { spikeAcc, data -> spikeAcc + BigDecimal.valueOf(data) })
//                } else {
//                    acc
//                }
//            }.divide(BigDecimal.valueOf(2 * validUnits.toLong()), 6, RoundingMode.HALF_UP).abs().toFloat()
//        } else {
//            0f
//        }
//    }
//
//}
//
//private fun isolate(points: List<Pair<Double, Double>>, spikeOffset: Int, baseline: Int = 0): List<Pair<Double, Double>> {
//    val beforeSpike = points.subList(0, spikeOffset)
//    val afterSpike = points.subList(spikeOffset + 1, points.size)
//
//    val lastIndexBeforeSpike = beforeSpike.indexOfLast { it.second > baseline }
//    val firstIndexAfterSpike = afterSpike.indexOfFirst { it.second > baseline }.takeIf { it != -1 } ?: afterSpike.size
//
//    return points.subList(lastIndexBeforeSpike + 1, firstIndexAfterSpike + spikeOffset + 1)
//}