package algorithm.extractor.value

import model.Spike

/**
 * Created by robert on 4/24/18.
 * Returns the number of spikes in the given interval. This way no extrapolation is done
 */
class SpikeCount : ValueExtractor<Spike, Float> {
    override fun extractValue(values: Array<Spike>): Float = values.size.toFloat()
}
