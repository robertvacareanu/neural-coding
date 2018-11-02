package algorithm.extractor.value

import model.Spike

/**
 * Created by robert on 7/14/18.
 * Returns 1 if there are spikes, 0 otherwise
 */
class UnitFeature : ValueExtractor<Array<Spike>, Float> {
    override fun extractValue(values: Array<Spike>): Float =
            if (values.isNotEmpty()) {
                1.0f
            } else {
                0.0f
            }
}