package algorithm.extractor.feature

import model.Spike
import model.TrialData

/**
 * @param windowLength expected to be in floats. For example, for a sampling rate of 32000 floats / sec, 0.2 seconds will be 32000/5 = 6400
 * @param overlap in percentage, how much should the windows overlap. For example, a value of 0 would mean that there should be no overlapping,
 * however, a value of 0.5 would mean that if, for example first window goes from 0 to 100, the second one goes from 50 to 150, assuming that
 * the window length is 100
 */
class WindowValueFeatureExtractor(private val windowLength: Int, private val overlap: Double) : FeatureExtractor<TrialData, List<Pair<Int, FloatArray>>> {
    override fun extract(data: Collection<TrialData>, extractor: (Array<Spike>) -> Float): List<Pair<Int, FloatArray>> {
        return data.map {
            val step = windowLength * overlap
            val result = mutableListOf<Float>()

            it.spikeData.forEach { array ->
                var start = it.extractedBetween.first
                var end = start + windowLength.toDouble()
                val timestamps = array.map { it.timestamp }
                val windowValues = mutableListOf<Float>()
                val last = it.extractedBetween.second
                while (start < last) {
                    val from = timestamps.indexOfFirst { it >= start }
                    var to = timestamps.indexOfFirst { it > end }
                    if(end > last && to == -1) {
                        to = timestamps.size
                    }
                    if ((from != -1) and (to != -1)) {
                        println(array.size)
                        val slice = array.sliceArray(from until to)
                        val res = extractor(slice)
                        windowValues.add(res)
                    } else {
                        windowValues.add(0f)
                    }
                    start += step
                    end += step
                }
                result.addAll(windowValues)

            }
            println(result.size)
            Pair(it.orientation, result.toFloatArray())
        }
    }
}