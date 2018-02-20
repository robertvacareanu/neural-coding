package converter;

import algorithm.extractor.feature.SpikesPerSec;
import kotlin.Pair;
import model.TrialData;

import java.util.List;

public class FireRateDataExporter extends DataExporter {

    @Override
    public void exportDataToArffFormat(String basePath, String fileName) {

        List<TrialData> trialData = super.readTrialData(basePath);

        SpikesPerSec spikesPerSec = new SpikesPerSec(super.getSpikeMetadata());
        List<Pair<Integer, float[]>> extractedData = spikesPerSec.extract(trialData);

        ArffConverter arffConverter = new ArffConverter(fileName);
        arffConverter.convertData(extractedData);
    }
}
