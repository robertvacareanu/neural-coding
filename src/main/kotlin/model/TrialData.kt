package model

/**
 * Created by robert on 2/17/18.
 * Holds the necessary data for any type of feature extraction
 * spikeData represents the spikes recorded by a recording entity
 */
data class TrialData(val orientation: Int, val spikeData: List<Array<Spike>>, val extractedBetween: Pair<Double, Double>) {

    /**
     * @param unit = the unit for which to return the spikes
     * @return An array of spikes corresponding to [unit]
     */
    operator fun get(unit: Int) = spikeData[unit]

    /**
     * @param unit - which unit
     * @param spike - which spike
     * @return The spike corresponding to [unit] and [spike]
     */
    operator fun get(unit: Int, spike: Int) = spikeData[unit][spike]

    /**
     * @param unit - which unit
     * @param spike - which spike
     * @param value - which index inside [Spike.waveform]
     * @return - The float corresponding to [unit], [spike] and [value]
     */
    operator fun get(unit: Int, spike: Int, value: Int) = spikeData[unit][spike][value]

}