package algorithm.processor

import model.TrialData

/**
 * An utility function that removes units having below
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