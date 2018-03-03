package model

/**
 * Created by robert on 1/3/18.
 * Represents a spike together with:
 *  - timestamp, which represents the offset in the binary file for waveform[0] of the float (not the byte)
 *  - waveform, which represents the signal for this spike
 */
class Spike(
        val timestamp: Double,
        val waveform: FloatArray
)