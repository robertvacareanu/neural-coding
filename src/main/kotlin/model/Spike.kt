package model

import java.util.*

/**
 * Created by robert on 1/3/18.
 * Represents a spike together with:
 *  - timestamp, which represents the offset in the binary file for waveform[0] of the float (not the byte)
 *  - waveform, which represents the signal for this spike
 */
data class Spike(
        val timestamp: Double,
        val waveform: FloatArray
) {
    operator fun get(index: Int) = waveform[index]

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Spike

        if (timestamp != other.timestamp) return false
        if (!Arrays.equals(waveform, other.waveform)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = timestamp.hashCode()
        result = 31 * result + Arrays.hashCode(waveform)
        return result
    }

}