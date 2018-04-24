package algorithm.extractor.feature

import model.Spike
import model.TrialData

/**
 * Created by robert on 3/9/18.
 * An extractor which map a channel array of spikes to a single value
 */
class SingleValueFeatureExtractor : FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>> {

    override fun extract(data: Collection<TrialData>, extractor: (Array<Spike>) -> Float): List<Pair<Int, FloatArray>> = data.map { Pair(it.orientation, it.spikeData.map(extractor).toFloatArray()) }

}
