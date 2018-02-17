package algorithm.extractor.data

import model.metadata.SpikeMetadata
import reader.DataReader
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by robert on 2/16/18.
 * Between stimulus means between event 2 and event 3
 */
class BetweenStim(val spikeMetadata: SpikeMetadata, val dataReader: DataReader): DataExtractor<FloatArray> {
    override fun extractData(): FloatArray {
        val eventTimestampsFile = File(spikeMetadata.eventTimestampsPath)

        /**
         * Assuming that the 4 events codes doesn't change with channels
         */

        val result = arrayListOf<Float>()
        eventTimestampsFile.inputStream().channel.use {
            val bb = ByteBuffer.allocate(1024 * 1024)
            bb.order(ByteOrder.LITTLE_ENDIAN)
            while (it.read(bb) > 0) {
                bb.flip()
                (0 until bb.limit() step 4).mapTo(result) { bb.getFloat(it) }
                bb.clear()
            }
        }

        return result.toFloatArray()
   }
}