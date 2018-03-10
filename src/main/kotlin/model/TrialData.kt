package model

/**
 * Created by robert on 2/17/18.
 * Holds the necessary data for any type of feature extraction
 * spikeData represents the spikes recorded by a recording entity
 */
data class TrialData(val orientation: Int, val spikeData: List<Array<Spike>>) {
    operator fun get(index: Int) = spikeData[index]
}