package model

import java.util.*

/**
 * Created by robert on 1/22/18.
 * Represents a portion of the signal
 */
data class Segment(val start: Int, val data: FloatArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Segment

        if (start != other.start) return false
        if (!Arrays.equals(data, other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = start
        result = 31 * result + Arrays.hashCode(data)
        return result
    }
}