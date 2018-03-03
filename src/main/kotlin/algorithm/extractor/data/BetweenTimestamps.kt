package algorithm.extractor.data

import model.Spike
import model.Trial
import model.TrialData
import reader.DataReader

/**
 * Created by robert on 2/24/18.
 * An abstraction over the DataExtractor, where the common operation: iteration over the whole list of trials as well
 * as over the channels is in one common method
 * Make us of the fact that there are 4 events:
 * Trial start (t1), Stim ON (t2), Stim OFF (t3), Trial end (t4)
 *
 */
sealed class BetweenTimestamps(private val dataReader: DataReader, val startOffset: Trial.() -> Int, val endOffset: Trial.() -> Int) : DataExtractor<List<Trial>, List<TrialData>> {

    private val trialDataSpikeTimestamps by lazy { dataReader.readSpikeTimestamps() }

    override fun extractData(data: List<Trial>): List<TrialData> {
        return data.map { trial ->
            val firstLastSpike = mutableMapOf<Int, Pair<Int, Int>>()
            (1..dataReader.numberOfChannels()).forEachIndexed { index, _ ->
                val indexFirstSpike = trialDataSpikeTimestamps[index].indexOfFirst { it > startOffset(trial) }
                val indexLastSpike = trialDataSpikeTimestamps[index].indexOfFirst { it > endOffset(trial) }
                firstLastSpike[index] = Pair(indexFirstSpike, indexLastSpike)
            }
            val trialDataChannels = mutableListOf<Array<Spike>>()
            (1..dataReader.numberOfChannels()).mapTo(trialDataChannels, {
                dataReader.readChannelSpikes(it, firstLastSpike[it - 1]!!.first until firstLastSpike[it - 1]!!.second).toTypedArray()
            })
            return@map TrialData(trial.orientation, trialDataChannels)
        }
    }
}


/**
 * Between two events
 * In this case, the data considered is from t3 until t4
 */
class AfterStim(dataReader: DataReader) : BetweenTimestamps(dataReader, {stimOffOffset}, {trialEndOffset})

/**
 * Between two events
 * In this case, the data considered is from t1 until t2
 */
class BeforeStim(dataReader: DataReader) : BetweenTimestamps(dataReader, startOffset = { trialStartOffset }, endOffset = { stimOnOffset })

/**
 * Between two events
 * In this case, the data considered is from t2  until t3
 */
class BetweenStim(dataReader: DataReader) : BetweenTimestamps(dataReader, { stimOnOffset }, { stimOffOffset })

/**
 * In this case, the data considered is from t2 until t2 + howMany
 */
class AfterStimOn(dataReader: DataReader, private val howMany: Int) : BetweenTimestamps(dataReader, { stimOnOffset }, { stimOnOffset + 4 * howMany })

/**
 * In this case, the data considered is from t3 until t3 + howMany
 */
class AfterStimOff(dataReader: DataReader, private val howMany: Int) : BetweenTimestamps(dataReader, { stimOffOffset }, { stimOffOffset + 4 * howMany })

class RandomAfterStimOn(dataReader: DataReader, private val howMannyAfterFirst: Int, private val howManyAfterSecond: Int) : BetweenTimestamps(dataReader, { stimOnOffset + 4 * howManyAfterSecond }, { stimOnOffset + 4 * (howMannyAfterFirst + howManyAfterSecond) })