package reader

import model.Segment
import model.Spike
import model.Trial

/**
 * Created by robert on 1/13/18.
 * Provides ways of accessing the data from .epd files
 */
interface ChannelReader {

    fun readChannelWaveform(channel: Int): FloatArray

    fun readChannelWaveform(channel: Int, between: IntRange): Segment

    fun numberOfChannels(): Int

}