package converter;

import algorithm.extractor.feature.MeanAmplitude;
import kotlin.Pair;
import model.TrialData;

import java.util.List;

public class MeanDataExporter extends DataExporter {

    @Override
    public void exportDataToArffFormat(String basePath, String arffMeanFile) {

        List<TrialData> trialData = super.readTrialData(basePath);

        MeanAmplitude entireChannelMeanAmplitude = new MeanAmplitude(super.getSpikeMetadata());
        List<Pair<Integer, float[]>> extractedData = entireChannelMeanAmplitude.extract(trialData);

        ArffConverter arffConverter = new ArffConverter(arffMeanFile);
        arffConverter.convertData(extractedData);
    }
}
