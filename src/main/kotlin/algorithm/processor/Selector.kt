package algorithm.processor

import algorithm.multiFilter
import main.*
import model.TrialData

/**
 * Utility functions to remove data: features or data points
 * The ones that takes as a parameter the emptyValue are used, generally, for value extractors that works on a single spike
 */


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

/**
 * Similar with removeIfEmpty, just that it removes trials that do not contains enough units with valid value
 */
fun removeTrials(dataSet: DataSet, unitsWithDataThreshold: Int = 5, emptyValue: Float = 0f): DataSet {
    val result = mutableListOf<DataPoint>()
    val numberOfUnits = dataSet.numberOfUnits
    val threshold = if (unitsWithDataThreshold > numberOfUnits) numberOfUnits else unitsWithDataThreshold
    dataSet.forEach {
        if (it.second.count { it == emptyValue } <= numberOfUnits - threshold) {
            result.add(it)
        }
    }
    return result
}

/**
 * Returns a new list that does not contain the indices inside trialsIndex
 * @param dataSet initial dataset, the one to be filtered
 * @param trialsIndex a list containing the indices to be removed from dataSet
 */
fun removeTrials(dataSet: DataSet, trialsIndex: List<Int>) = dataSet.filterIndexed { index, _ -> !trialsIndex.contains(index) }

/**
 * Removes units that do not have data for at least [trialsWithDataThreshold] trials
 */
fun removeUnits(dataSet: DataSet, trialsWithDataThreshold: Int, emptyValue: Float = 0f): DataSet {
    val removeColumns = IntArray(dataSet.numberOfUnits, { dataSet.size })

    dataSet.forEach {
        it.second.forEachIndexed { index, fl ->
            if (fl == emptyValue) removeColumns[index]--
        }
    }

    println(removeColumns.sorted().joinToString())

    val result = mutableListOf<DataPoint>()

    dataSet.forEach {
        result.add(Pair(it.orientation, it.second.filterIndexed { index, _ -> removeColumns[index] >= trialsWithDataThreshold }.toFloatArray()))
    }

    return result
}

/**
 * Returns a DataSet containing for every orientation the same number of trials
 * Orientations are assumed to be from [0, 45, 90, 135, 180, 225, 270, 315]
 */
fun balance(dataSet: DataSet): DataSet {
    val trials = IntArray(8, { orientation -> dataSet.count { it.orientation == orientation * 45 } })
    println(trials.joinToString())
    val min = trials.filter { it != 0 }.min()!!
    val balancedNumbers = trials.map { if (it != 0) min else 0 }.toIntArray()
    println(balancedNumbers.joinToString())
    val result = mutableListOf<DataPoint>()
    for (dp in dataSet) {
        if (balancedNumbers[dp.orientation / 45] > 0) {
            result.add(dp)
            balancedNumbers[dp.orientation / 45]--
            println(balancedNumbers.joinToString())
        }
    }
    return result
}

/**
 * Destroys relative order of trials
 */
fun removeTrialsWithLeastFeatures(dataSet: DataSet, skip: Int = 5, emptyValue: Float = 0f): DataSet {
    val result = mutableListOf<DataPoint>()
    val indices = mutableListOf<Pair<Int, Int>>()
    dataSet.forEachIndexed { index, datapoint ->
        indices.add(Pair(index, datapoint.second.count { it != emptyValue }))
    }
    indices.sortBy { it.second }
    (0 until indices.count()).forEach {
        if (it >= skip) {
            result.add(dataSet[indices[it].first])
        }
    }
    return result
}

/**
 * Apply filters given as parameter to the dataset and return a new one
 */
fun filterDataSet(dataSet: DataSet, filters: List<DataPoint.() -> Boolean>) = dataSet.multiFilter(filters)