package algorithm.extractor.value

import model.Spike

/**
 * Created by robert on 4/16/18.
 */
class MeanAmplitudeOfFirstSpike(private val spikeOffset: Int): ValueExtractor<Spike, Float> {
    override fun extractValue(values: Array<Spike>) = values.minBy { it.timestamp }?.get(spikeOffset) ?: 0f
}