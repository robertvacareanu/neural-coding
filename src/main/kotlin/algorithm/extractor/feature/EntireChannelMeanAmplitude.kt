package algorithm.extractor.feature

import model.TrialData
import model.metadata.SpikeMetadata
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs

/**
 * Created by robert on 2/15/18.
 * Each value in the result float array represents the mean amplitude for a particular trial
 * The Collection from Collection<FloatArray> represents all the channels
 *
 */
class EntireChannelMeanAmplitude(private val spikeMetadata: SpikeMetadata) : FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>> {
    override fun extract(data: Collection<TrialData>): List<Pair<Int, FloatArray>> {
        val result = ArrayList<Pair<Int, FloatArray>>()
        data.mapTo(result) { trial ->
            val means = FloatArray(spikeMetadata.storedChannels)
            (0 until spikeMetadata.storedChannels).forEach {
                if(trial.spikeData[it].isNotEmpty()) {
                    val nominator = trial.spikeData[it].fold(BigDecimal.ZERO) { acc, spike ->
                        acc.add(BigDecimal.valueOf(abs(spike.waveform[spikeMetadata.waveformSpikeOffset].toDouble())))
                    }
                    val denominator = BigDecimal.valueOf(trial.spikeData[it].size.toDouble())
                    means[it] = nominator.divide(denominator, 6, RoundingMode.HALF_UP).toFloat()
                } else {
                    means[it] = 0.0f
                }
            }
            Pair(trial.orientation, means)
        }


        return result
    }
}