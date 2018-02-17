package reader

import model.Segment
import model.Spike
import model.Trial

/**
 * Created by robert on 1/13/18.
 */
interface Reader {

    fun readChannelWaveform(channel: Int): FloatArray

    fun readChannelWaveform(channel: Int, between: IntRange): Segment

    fun readChannelSpikes(channel: Int): List<Spike>

    fun readChannelSpikes(channel: Int, between: IntRange): List<Spike>

    fun readTrials(): List<Trial>

}