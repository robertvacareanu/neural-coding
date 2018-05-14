package algorithm.extractor.value

import model.Spike
import java.awt.geom.Point2D
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Created by robert on 4/30/18.
 * Each value in the result float array represents the mean width of the spikes for a particular trial
 * The Collection from Collection<TrialData> represents all the trials having each one the recorded data for each channel
 */
class MeanWidth(private val waveformInternalSamplingFrequency: Float, private val spikeOffset: Int, private val emmptyValue: Float = 0f) : ValueExtractor<Spike, Float> {
    private val Pair<Double, Double>.x: Double
        get() = first
    private val Pair<Double, Double>.y: Double
        get() = second

    override fun extractValue(values: Array<Spike>): Float {

        var used = 0

        val result = values.fold(BigDecimal.ZERO) { acc, spike ->
            val points = spike.waveform.mapIndexed { index, float -> Pair(index / waveformInternalSamplingFrequency.toDouble(), float.toDouble()) }
            val beforeSpike = points.subList(0, spikeOffset)
            val afterSpike = points.subList(spikeOffset + 1, points.size)

            val firstIndex = beforeSpike.indexOfLast { it.second >= 0 }
            val p1 = if (firstIndex != -1) {
                val p11 = beforeSpike[firstIndex]
                val p12 = if (firstIndex == spikeOffset - 1) points[spikeOffset] else beforeSpike[firstIndex + 1]
                Pair(((p11.x - p12.x) * p11.y) / (p12.y - p11.y) + p11.x, 0.0)
            } else {
                val localMinimum = beforeSpike.indexOf(beforeSpike.minBy { it.second })


                val beforeFirstUnderIndex = (localMinimum downTo 1).firstOrNull {
                    val lookAhead = if (localMinimum - it > 4 || it <= 2) {
                        1
                    } else {
                        2
                    }
                    beforeSpike[it] == beforeSpike.slice((it - lookAhead) until it).maxBy { it.second }
                }
                if (beforeFirstUnderIndex != null) {
                    val p11 = beforeSpike[beforeFirstUnderIndex]
                    val p12 = beforeSpike[beforeFirstUnderIndex + 1]

                    Pair(((p11.x - p12.x) * p11.y) / (p12.y - p11.y) + p11.x, 0.0)
                } else null
            }


            val secondIndex = afterSpike.indexOfFirst { it.second > 0 }
            val p2 = if (secondIndex != -1) {
                val p21 = afterSpike[secondIndex]
                val p22 = if (secondIndex == 0) afterSpike[secondIndex + 1] else afterSpike[secondIndex - 1]

                Pair(((p21.x - p22.x) * p21.y) / (p22.y - p21.y) + p21.x, 0.0)
            } else {
                val localMinimum = afterSpike.indexOf(afterSpike.minBy { it.second })

                val afterFirstUnder = (localMinimum until (afterSpike.size)).firstOrNull {
                    val lookAhead = if (it - localMinimum > 4 || afterSpike.size - it <= 2) {
                        1
                    } else {
                        2
                    }
                    afterSpike[it] == afterSpike.slice(it until (it + lookAhead)).maxBy { it.second }
                }
                if (afterFirstUnder != null) {
                    val p21 = afterSpike[afterFirstUnder]
                    val p22 = afterSpike[afterFirstUnder + 1]
                    Pair(((p21.x - p22.x) * p21.y) / (p22.y - p21.y) + p21.x, 0.0)
                } else null
            }
            if (p1 != null && p2 != null) {
                used++
                acc + BigDecimal.valueOf(Point2D.distance(p1.x, p1.y, p2.x, p2.y))
            } else {
                acc
            }
        }

        return if (used > 0) {
            return result.divide(BigDecimal.valueOf(used.toLong()), 32, RoundingMode.HALF_UP).toFloat()
        } else {
            emmptyValue
        }
    }
}