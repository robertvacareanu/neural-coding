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
sealed class BetweenTimestamps(private val dataReader: DataReader): DataExtractor<List<Trial>, List<TrialData>> {

    private val trialDataSpikeTimestamps by lazy { dataReader.readSpikeTimestamps() }

    abstract fun eventStart(trial: Trial): Int

    abstract fun eventEnd(trial: Trial): Int

    override fun extractData(data: List<Trial>): List<TrialData> {
        return data.map { trial ->
            val firstLastSpike = mutableMapOf<Int, Pair<Int, Int>>()
            (1..dataReader.numberOfChannels()).forEachIndexed { index, _ ->
                val indexFirstSpike = trialDataSpikeTimestamps[index].indexOfFirst { it > eventStart(trial) }
                val indexLast = trialDataSpikeTimestamps[index].indexOfFirst { it > eventEnd(trial) }
                firstLastSpike[index] = Pair(indexFirstSpike, indexLast - 1)
            }
            val trialDataChannels = mutableListOf<Array<Spike>>()
            (1..dataReader.numberOfChannels()).mapTo(trialDataChannels, {
                dataReader.readChannelSpikes(it, firstLastSpike[it-1]!!.first until firstLastSpike[it-1]!!.second).toTypedArray()
            })
            return@map TrialData(trial.orientation, trialDataChannels)
        }
    }
}



/**
 * Between two events
 * In this case, the data considered is from t2 (inclusive) until t3 (exclusive)
 */
class AfterStim(private val dataReader: DataReader) : BetweenTimestamps(dataReader) {
    override fun eventStart(trial: Trial): Int = trial.stimOffOffset

    override fun eventEnd(trial: Trial): Int = trial.trialEndOffset
}

/**
 * Between two events
 * In this case, the data considered is from t2 (inclusive) until t3 (exclusive)
 */
class BeforeStim(private val dataReader: DataReader): BetweenTimestamps(dataReader) {
    override fun eventStart(trial: Trial): Int = trial.trialStartOffset

    override fun eventEnd(trial: Trial): Int = trial.stimOnOffset
}

/**
 * Between two events
 * In this case, the data considered is from t2 (inclusive) until t3 (exclusive)
 */
class BetweenStim(private val dataReader: DataReader) : BetweenTimestamps(dataReader) {
    override fun eventStart(trial: Trial): Int = trial.stimOnOffset

    override fun eventEnd(trial: Trial): Int = trial.stimOffOffset
}

/**
 * In this case, the data considered is from t2 (inclusive) until t2 + howMany (exclusive)
 */
class AfterStimOn(private val dataReader: DataReader, private val howMany: Int) : BetweenTimestamps(dataReader) {
    override fun eventStart(trial: Trial): Int = trial.stimOnOffset

    override fun eventEnd(trial: Trial): Int = trial.stimOnOffset + howMany
}

/**
 * In this case, the data considered is from t3 (inclusive) until t3 + howMany (exclusive)
 */
class AfterStimOff(private val dataReader: DataReader, private val howMany: Int) : BetweenTimestamps(dataReader) {
    override fun eventStart(trial: Trial): Int = trial.stimOffOffset

    override fun eventEnd(trial: Trial): Int = trial.stimOffOffset + howMany
}