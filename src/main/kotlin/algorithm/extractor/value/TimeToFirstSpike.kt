package algorithm.extractor.value

import model.Spike

/**
 * Created by robert on 4/16/18.
 *
 */
class TimeToFirstSpike(private val waveformInternalSampling: Float, private val eventStartTimes: List<Int>) : ValueExtractor<Spike, Float> {
    override fun extractValue(values: Array<Spike>) = with(values.minBy { it.timestamp }) {
        if (this != null) {
            ((timestamp.toInt() - eventStartTimes.last { timestamp > it }).toDouble() / waveformInternalSampling.toDouble()).toFloat()
        } else {
            0f
        }
    }
}