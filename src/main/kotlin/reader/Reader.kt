package reader

import model.Spike

/**
 * Created by robert on 1/13/18.
 */
interface Reader {

    fun readChannelWaveform(channel: Int): FloatArray

    fun readChannelSpikes(channel: Int): List<Spike>

}