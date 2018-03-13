package data_conversion.exporter;

import algorithm.extractor.feature.SpikesPerSec;
import data_conversion.converter.FileConverterFactory;
import data_conversion.file_utility.FileTypes;
import kotlin.Pair;
import model.TrialData;

import java.util.List;

public class FireRateDataExporter extends DataExporter {

    @Override
    public void exportData(String basePath, String fileName, FileTypes fileType) {

        List<TrialData> trialData = super.readBetweenStimTrialData(basePath);

        SpikesPerSec spikesPerSec = new SpikesPerSec(super.getSpikeMetadata().getWaveformInternalSamplingFrequency());
        List<Pair<Integer, float[]>> extractedData = spikesPerSec.extract(trialData);

        new FileConverterFactory().getFileConverter(fileType).convertData(fileName, extractedData);
    }
}
