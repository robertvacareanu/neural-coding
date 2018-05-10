package algorithm.processor

import main.DataPoint
import main.DataSet
import main.almostEqual
import main.get
import model.TrialData

/**
 * Created by robert on 3/15/18.
 * An utility function to remove from the data those units that don't contain data in all trials
 * Unit selector - those that have data in all the trials
 */
fun removeIfEmpty(dataSet: DataSet) = with(dataSet) {
    val emptyColumns = mutableSetOf<Int>()
    forEach { unitData ->
        emptyColumns.addAll(unitData.second.indices.filter { unitData[it] almostEqual 0.0 })
    }

    val result = mutableListOf<DataPoint>()

    forEach {
        result.add(Pair(it.first, it.second.filterIndexed { index, _ -> !emptyColumns.contains(index) }.toFloatArray()))
    }
    result
}

/**
 * An utility function that removes units having below
 * Unit selector - those that have at least 500 spikes in the trials
 * Assumes that every trial has the same number of units and that there is at least one trial inside trialData
 */
fun removeIfNotEnoughSpikes(trialData: List<TrialData>, threshold: Int = 500) = with(trialData) {

    //Assumes that every trial has the same number of units and that there is at least one trial inside trialData
    val x = (0 until first().spikeData.size).filter { unit ->
        fold(0) { acc, trial ->
            acc + trial[unit].size
        } > threshold
    }

    val result = map {
        TrialData(it.orientation, it.spikeData.filterIndexed { index, _ -> x.contains(index) }, it.extractedBetween)
    }

    result
}

