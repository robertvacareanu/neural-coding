package algorithm.extractor.value

import model.Spike

/**
 * Created by robert on 4/16/18.
 * The amplitude of first spike, or [emptyValue] if empty
 */
class MeanAmplitudeOfFirstSpike(private val spikeOffset: Int, private val emptyValue: Float = 0f) : ValueExtractor<Spike, Float> {
    override fun extractValue(values: Array<Spike>) = values.minBy { it.timestamp }?.get(spikeOffset) ?: emptyValue
}