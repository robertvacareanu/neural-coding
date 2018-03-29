package algorithm.extractor.value

/**
 * Some utility functions for extracting important details from spikes
 */
fun isolate(points: List<Pair<Double, Double>>, spikeOffset: Int, baseline: Int = 0): List<Pair<Double, Double>> {
    val beforeSpike = points.subList(0, spikeOffset)
    val afterSpike = points.subList(spikeOffset + 1, points.size)

    val lastIndexBeforeSpike = beforeSpike.indexOfLast { it.second > baseline }
    val firstIndexAfterSpike = afterSpike.indexOfFirst { it.second > baseline }.takeIf { it != -1 } ?: afterSpike.size

    return points.subList(lastIndexBeforeSpike + 1, firstIndexAfterSpike + spikeOffset + 1)
}