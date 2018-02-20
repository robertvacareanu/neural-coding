package algorithm.extractor.feature

import model.TrialData
import model.metadata.SpikeMetadata

/**
 * Created by robert on 2/20/18.
 */
class SpikesPerSec(private val spikeMetadata: SpikeMetadata) : FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>> {
    override fun extract(data: Collection<TrialData>): List<Pair<Int, FloatArray>> {
        val result = ArrayList<Pair<Int, FloatArray>>()

        data.mapTo(result) { trial ->
            val spikePerSec = FloatArray(spikeMetadata.storedChannels)
            (0 until spikeMetadata.storedChannels).forEach {
                if(trial.spikeData[it].isNotEmpty()) {
                    spikePerSec[it] = trial.spikeData[it].size / ((trial.spikeData[it][trial.spikeData[it].size-1].timestamp - trial.spikeData[it][0].timestamp)/spikeMetadata.waveformInternalSamplingFrequency)
                }
            }
            Pair(trial.orientation, spikePerSec)
        }

        return result
    }
}