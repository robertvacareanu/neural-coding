package reader

import model.Segment
import model.Spike
import model.Trial
import model.metadata.EtiMetadata
import model.metadata.SpktweMetadata
import model.metadata.SsdMetadata
import model.metadata.EpdMetadata
import java.io.File

/**
 * Created by robert on 1/13/18.
 * Able to read retrieve the necessary data for performing data and feature extraction
 */
class DataChannelReader(private val epdMetadata: EpdMetadata) : ChannelReader {


    override fun readChannelWaveform(channel: Int): FloatArray = readFloatBinary(epdMetadata.basePath + epdMetadata.channelPaths[channel - 1])

    /**
     * @param channel from 1 to the size defined in metadata
     */
    override fun readChannelWaveform(channel: Int, between: IntRange): Segment = Segment(between.first, readFloatBinary(epdMetadata.basePath + epdMetadata.channelPaths[channel - 1], between))


    override fun numberOfChannels() = epdMetadata.eegChannels

}
