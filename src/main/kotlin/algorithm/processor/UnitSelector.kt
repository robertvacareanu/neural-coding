package algorithm.processor

import model.TrialData

fun removeIfNotEnoughSpikes(trialData: List<TrialData>, nrSpikesPerUnitTresshold: Int = 500) = with(trialData) {

    val x = (0 until trialData.first().spikeData.size).filter { unit ->
        trialData.fold(0) { acc, trial ->
            acc + trial[unit].size
        } > nrSpikesPerUnitTresshold
    }

    val result = trialData.map {
        TrialData(it.orientation, it.spikeData.filterIndexed { index, _ -> x.contains(index) }, it.extractedBetween)
    }

    result
}