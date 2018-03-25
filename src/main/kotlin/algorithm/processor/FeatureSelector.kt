package algorithm.processor

import main.almostEqual
import main.get

/**
 * Created by robert on 3/15/18.
 * An utility function to remove from the data those units that don't contain data in all trials
 */
fun removeIfEmpty(data: List<Pair<Int, FloatArray>>) = with(data) {
    val emptyColumns = mutableSetOf<Int>()
    forEach { unitData ->
        emptyColumns.addAll(unitData.second.indices.filter { unitData[it] almostEqual 0.0 })
    }

    val result = mutableListOf<Pair<Int, FloatArray>>()

    forEach {
        result.add(Pair(it.first, it.second.filterIndexed { index, _ -> !emptyColumns.contains(index) }.toFloatArray()))
    }
    result
}
