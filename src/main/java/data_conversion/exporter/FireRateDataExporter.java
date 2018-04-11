package data_conversion.exporter;

import algorithm.extractor.feature.SingleValueFeatureExtractor;
import algorithm.extractor.value.SpikesPerSec;
import data_conversion.converter.FileConverterFactory;
import data_conversion.file_utility.FileTypes;
import data_conversion.file_utility.SpikesTypes;
import data_conversion.file_utility.Timestamps;
import kotlin.Pair;
import model.TrialData;

import java.util.List;

public class FireRateDataExporter extends DataExporter {

    @Override
    public void exportData(String basePath, String fileName, FileTypes fileType, Timestamps timestamps, SpikesTypes spikesType) {

        List<TrialData> trialData = super.readTrialDataAccordingToTimestamp(basePath, timestamps, spikesType);

        SingleValueFeatureExtractor singleValueFeatureExtractor = new SingleValueFeatureExtractor();
        SpikesPerSec spikesPerSec = new SpikesPerSec(super.getSpikeMetadata().getWaveformInternalSamplingFrequency());
        List<Pair<Integer, float[]>> extractedData = singleValueFeatureExtractor.extract(trialData, spikesPerSec::extractValue);

        new FileConverterFactory().getFileConverter(fileType).convertData(fileName, extractedData);
    }
}
