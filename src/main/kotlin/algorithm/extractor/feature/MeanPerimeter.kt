package algorithm.extractor.feature

import model.TrialData
import model.metadata.SpikeMetadata
import java.awt.geom.Point2D
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by robert on 3/4/18.
 * Each value in the result float array represents the mean perimeter for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class MeanPerimeter(private val spikeMetadata: SpikeMetadata) : FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>> {
    override fun extract(data: Collection<TrialData>): List<Pair<Int, FloatArray>> {
        val result = mutableListOf<Pair<Int, FloatArray>>()
        data.forEach {
            val orientation = it.orientation
            val channelsMeanPerimeter = mutableListOf<Float>()
            it.spikeData.mapTo(channelsMeanPerimeter) { channelSpikeArray ->
                channelSpikeArray.fold(BigDecimal.ZERO) { acc, spike ->
                    val perimeterForSpike = mutableListOf<Double>()
                    (0 until (spike.waveform.size - 1)).mapTo(perimeterForSpike) {
                        val timestampPoint1 = (spike.timestamp + it) / spikeMetadata.waveformInternalSamplingFrequency
                        val timestampPoint2 = (spike.timestamp + it + 1) / spikeMetadata.waveformInternalSamplingFrequency

                        Point2D.distance(timestampPoint1, spike.waveform[it].toDouble(), timestampPoint2, spike.waveform[it + 1].toDouble())
                    }
                    acc.add(BigDecimal.valueOf(perimeterForSpike.fold(BigDecimal.ZERO) { spikeAcc, data ->
                        spikeAcc.add(BigDecimal.valueOf(data))
                    }.divide(BigDecimal.valueOf(perimeterForSpike.size.toLong()), 6, RoundingMode.HALF_UP).toDouble()))
                }.toFloat()
            }
            result.add(Pair(orientation, channelsMeanPerimeter.toFloatArray()))
        }
        return result
    }
}