package model

/**
 * Created by robert on 2/16/18.
 * Holds a trial information and the necessary offsets for the spikes
 */
data class Trial(val trialNumber: Int, val orientation: Int, val trialStartOffset: Int, val stimOnOffset: Int, val stimOffOffset: Int, val trialEndOffset: Int, val contrast: Int)