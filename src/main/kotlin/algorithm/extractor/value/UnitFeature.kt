package algorithm.extractor.value

import model.Spike

/**
 * Created by robert on 7/14/18.
 */
class UnitFeature : ValueExtractor<Spike, Float> {
    override fun extractValue(values: Array<Spike>): Float =
            if(values.isNotEmpty()) {
                1.0f
            } else {
                0.0f
            }
}