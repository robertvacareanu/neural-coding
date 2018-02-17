package algorithm.extractor.data

import model.Trial
import model.TrialData
import reader.DataReader

/**
 * Created by robert on 2/16/18.
 * Between stimulus means between event 2 and event 3
 * Example: Trial start (t1), Stim ON (t2), Stim OFF (t3), Trial end (t4)
 * In this case, the data considered is from t2 (inclusive) until t3 (exclusive)
 */
class BetweenStim(val dataReader: DataReader) : DataExtractor<List<Trial>, List<TrialData>> {
    override fun extractData(data: List<Trial>) = data.map { trial ->
        val trialDataChannels = mutableListOf<FloatArray>()

        (1..dataReader.numberOfChannels()).mapTo(trialDataChannels, {
            dataReader.readChannelWaveform(it, trial.stimOnOffset until trial.stimOffOffset).data
        })

        return@map TrialData(trial.orientation, trialDataChannels)
    }


}