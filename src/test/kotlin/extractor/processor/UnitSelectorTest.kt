package extractor.processor

import algorithm.processor.removeIfNotEnoughSpikes
import model.Spike
import model.TrialData
import org.junit.Test

/**
 * Created by robert on 4/12/18.
 */
class SelectorTest {

    @Test
    fun removeIfNotEnoughSpikes() {
        val trials = listOf(
                TrialData(
                        orientation = 1,
                        spikeData = listOf(
                                arrayOf(
                                        Spike(1.0, floatArrayOf()),
                                        Spike(2.0, floatArrayOf()),
                                        Spike(3.0, floatArrayOf()),
                                        Spike(4.0, floatArrayOf()),
                                        Spike(5.0, floatArrayOf()),
                                        Spike(6.0, floatArrayOf()),
                                        Spike(7.0, floatArrayOf())
                                ),
                                arrayOf(
                                        Spike(9.0, floatArrayOf()),
                                        Spike(10.0, floatArrayOf()),
                                        Spike(11.0, floatArrayOf()),
                                        Spike(12.0, floatArrayOf())
                                )
                        ),
                        extractedBetween = Pair(1.0, 3.0)
                ),
                TrialData(
                        orientation = 2,
                        spikeData = listOf(
                                arrayOf(
                                        Spike(1.0, floatArrayOf()),
                                        Spike(2.0, floatArrayOf()),
                                        Spike(3.0, floatArrayOf()),
                                        Spike(4.0, floatArrayOf())
                                ),
                                arrayOf(
                                        Spike(1.0, floatArrayOf()),
                                        Spike(2.0, floatArrayOf())
                                )
                        ),
                        extractedBetween = Pair(3.0, 6.0)
                )
        )

        var result = removeIfNotEnoughSpikes(trials, 6)
        assert(result[0].spikeData.size == 1)

        result = removeIfNotEnoughSpikes(trials, 5)
        assert(result[0].spikeData.size == 2)
    }
}