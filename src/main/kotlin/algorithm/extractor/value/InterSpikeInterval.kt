package algorithm.extractor.value

import model.Spike

/**
 * Created by robert on 8/23/18.
 */
class InterSpikeInterval(val extractedBetween: Pair<Double, Double>) : ValueExtractor<Array<Spike>, FloatArray> {
    override fun extractValue(values: Array<Spike>): FloatArray = values.mapIndexed { index: Int, spike: Spike ->
        if (index > 0) spike.timestamp - values[index - 1].timestamp else spike.timestamp - extractedBetween.first
    }
            .map { it.toFloat() }
            .toFloatArray()
}