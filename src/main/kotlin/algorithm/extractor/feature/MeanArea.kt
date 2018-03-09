package algorithm.extractor.feature

import model.Spike
import model.TrialData
import model.metadata.SpikeMetadata
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

/**
 * Created by robert on 3/8/18.
 * Each value in the result float array represents the mean area (Integral) for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class MeanArea(private val spikeMetadata: SpikeMetadata) : FeatureExtractor<TrialData, List<Pair<Int, DoubleArray>>> {
    override fun extract(data: Collection<TrialData>): List<Pair<Int, DoubleArray>> {
        infix operator fun Pair<Double, Double>.times(p: Pair<Double, Double>): Double = first * p.second - second * p.first

        fun Spike.toPoint(offset: Int): Pair<Double, Double> = Pair((timestamp + offset) / spikeMetadata.waveformInternalSamplingFrequency, waveform[offset].toDouble())

        val result = mutableListOf<Pair<Int, DoubleArray>>()

        data.mapTo(result) { trialData ->
            val floatArray = mutableListOf<Double>()
            trialData.spikeData.forEach { channelSpikes ->
                channelSpikes.forEach { spike ->
                    var area = BigDecimal.ZERO
                    (0 until spike.waveform.size - 1).forEach {
                        area += BigDecimal.valueOf(spike.toPoint(it) * spike.toPoint(it + 1))
                    }
                    area += BigDecimal.valueOf(spike.toPoint(spike.waveform.size - 1) * spike.toPoint(0))
                    floatArray.add(abs(area.divide(BigDecimal.valueOf(2), 20, RoundingMode.HALF_UP).toDouble()))
                }
            }
            Pair(trialData.orientation, floatArray.toDoubleArray())
        }

        return result
    }
}