package algorithm.extractor.data

import model.Spike
import model.Trial
import model.TrialData
import reader.spikes.SpikeReader

/**
 * Created by robert on 2/24/18.
 * An abstraction over the [DataExtractor], where the common operation: iteration over the whole list of trials as well
 * as over the channels is in one common method
 * Make use of the fact that there are 4 events:
 * Trial start (t1), Stim ON (t2), Stim OFF (t3), Trial end (t4)
 */
sealed class BetweenTimestamps(private val dataReader: SpikeReader, val startOffset: Trial.() -> Int, val endOffset: Trial.() -> Int) : DataExtractor<List<Trial>, List<TrialData>> {

    private val trialDataSpikeTimestamps by lazy { dataReader.readSpikeTimestamps() }

    override fun extractData(data: List<Trial>): List<TrialData> {
        return data.map { trial ->
            val firstLastSpike = mutableMapOf<Int, Pair<Int, Int>>()
            (1..dataReader.numberOfUnits()).forEachIndexed { index, _ ->
                val indexFirstSpike = trialDataSpikeTimestamps[index].indexOfFirst { it >= trial.startOffset() }
                val indexLastSpike = trialDataSpikeTimestamps[index].indexOfFirst { it > trial.endOffset() }
                firstLastSpike[index] = Pair(indexFirstSpike, indexLastSpike)
            }
            val trialDataChannels = mutableListOf<Array<Spike>>()
            (1..dataReader.numberOfUnits()).mapTo(trialDataChannels, {
                dataReader.readSpikes(it, firstLastSpike[it - 1]!!.first until firstLastSpike[it - 1]!!.second).toTypedArray()
            })
            return@map TrialData(trial.orientation, trialDataChannels, Pair(trial.startOffset().toDouble(), trial.endOffset().toDouble()))
        }
    }
}


/**
 * Between two events
 * In this case, the data considered is from t3 until t4
 */
class AfterStim(dataReader: SpikeReader) : BetweenTimestamps(dataReader, { stimOffOffset }, { trialEndOffset })

/**
 * Between two events
 * In this case, the data considered is from t1 until t2
 */
class BeforeStim(dataReader: SpikeReader) : BetweenTimestamps(dataReader, startOffset = { trialStartOffset }, endOffset = { stimOnOffset })

/**
 * Between two events
 * In this case, the data considered is from t2  until t3
 */
class BetweenStim(dataReader: SpikeReader) : BetweenTimestamps(dataReader, { stimOnOffset }, { stimOffOffset })

/**
 * In this case, the data considered is from t2 until t2 + howMany
 */
class AfterStimOn(dataReader: SpikeReader, private val howMany: Int) : BetweenTimestamps(dataReader, { stimOnOffset }, { stimOnOffset + howMany })

/**
 * In this case, the data considered is from t3 until t3 + howMany
 */
class AfterStimOff(dataReader: SpikeReader, private val howMany: Int) : BetweenTimestamps(dataReader, { stimOffOffset }, { stimOffOffset + howMany })

/**
 * In this case, read data after stim on between specified points
 */
class RandomAfterStimOn(dataReader: SpikeReader, private val howMannyAfterFirst: Int, private val howManyAfterSecond: Int) : BetweenTimestamps(dataReader, { stimOnOffset + howMannyAfterFirst }, { stimOnOffset + howManyAfterSecond }) {
    override fun toString(): String = "RASOn_${howMannyAfterFirst}_$howManyAfterSecond"
}

/**
 * In this case, read data between howManyAfterFirst and howManyAfter
 */
class RandomAfterEvent(dataReader: SpikeReader, private val event: Trial.() -> Int, private val howMannyAfterFirst: Int, private val howManyAfterSecond: Int) : BetweenTimestamps(dataReader, { event() + howMannyAfterFirst }, { event() + howManyAfterSecond }) {
    override fun toString(): String = "RAE_${howMannyAfterFirst}_$howManyAfterSecond"
}
