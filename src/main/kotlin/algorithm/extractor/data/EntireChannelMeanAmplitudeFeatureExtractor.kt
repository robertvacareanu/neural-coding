package algorithm.extractor.feature

import java.math.BigDecimal

/**
 * Created by robert on 2/15/18.
 * Each value in the result float array represents the mean amplitude for a particular trial
 *
 */
class EntireChannelMeanAmplitudeFeatureExtractor(portion: FloatArray) : FeatureExtractor<FloatArray, FloatArray> {
    override fun extract(data: Collection<FloatArray>): FloatArray {
        val result = ArrayList<Float>(data.size)

        data.mapTo(result, { floats: FloatArray ->
            floats.fold(BigDecimal.ZERO, { acc, number ->
                acc.add(BigDecimal.valueOf(number.toDouble()))
            }).divide(BigDecimal.valueOf(floats.size.toDouble())).toFloat() })

        return result.toFloatArray()
    }
}