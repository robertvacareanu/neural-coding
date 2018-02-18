package algorithm.extractor.data

import model.Spike
import model.Trial
import model.TrialData
import reader.DataReader

/**
 * Created by robert on 2/16/18.
 * Between stimulus means between event 2 and event 3
 * Example: Trial start (t1), Stim ON (t2), Stim OFF (t3), Trial end (t4)
 * In this case, the data considered is from t2 (inclusive) until t3 (exclusive)
 */
class BetweenStim(private val dataReader: DataReader) : DataExtractor<List<Trial>, List<TrialData>> {

    override fun extractData(data: List<Trial>): List<TrialData> {
        val trialDataSpikeTimestamps = dataReader.readSpikeTimestamps()
        return data.map { trial ->
            val firstLastSpike = mutableMapOf<Int, Pair<Int, Int>>()
            (1..dataReader.numberOfChannels()).forEachIndexed { index, _ ->
                val indexFirstSpike = trialDataSpikeTimestamps[index].indexOfFirst { it > trial.stimOnOffset }
                val indexLast = trialDataSpikeTimestamps[index].indexOfFirst { it > trial.stimOffOffset }
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