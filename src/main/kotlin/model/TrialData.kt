package model

/**
 * Created by robert on 2/17/18.
 * Holds the necessary data for any type of feature extraction
 */
data class TrialData(val orientation: Int, val spikeData: List<Array<Spike>>)