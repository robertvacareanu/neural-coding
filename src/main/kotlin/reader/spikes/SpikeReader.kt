package reader.spikes

import model.Spike
import model.Trial

/**
 * Created by robert on 3/23/18.
 * Provides a general interface that can be used to read the spikes
 * from each channel, as well as to read the sorted spikes, from each
 * neuron
 *
 * In this case, a unit means a spike emitter. Can be either from an
 * electrode and therefore reads from channels, or a unit (neuron) and
 * in this case reads from the .ssd
 */
interface SpikeReader {

    /**
     * @param fromUnit number of the channel to read data
     * It is expected that this number corresponds to the ones in metadata
     * Hence the first channel is 1. the second is 2 etc
     */
    fun readSpikes(fromUnit: Int, between: IntRange): List<Spike>

    fun readSpikeTimestamps(): List<IntArray>

    fun readTrials(): List<Trial>

    fun numberOfUnits(): Int

}