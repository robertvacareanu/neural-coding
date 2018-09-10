package algorithm.extractor.value

import model.Spike

/**
 * Created by robert on 4/16/18.
 * Calculates time to first spike by considering when the event and the spike happened
 */
class TimeToFirstSpike(private val waveformInternalSampling: Float, private val eventStartTimes: List<Int>, private val emptyValue: Float = 0f) : ValueExtractor<Array<Spike>, Float> {
    override fun extractValue(values: Array<Spike>) = with(values.minBy { it.timestamp }) {
        if (this != null) {
            ((timestamp.toInt() - eventStartTimes.last { timestamp > it }).toDouble() / waveformInternalSampling.toDouble()).toFloat()
        } else {
            emptyValue
        }
    }
}
