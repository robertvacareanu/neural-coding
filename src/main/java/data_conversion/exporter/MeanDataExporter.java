package data_conversion.exporter;

import algorithm.extractor.feature.MeanAmplitude;
import algorithm.extractor.feature.SingleValueFeatureExtractor;
import data_conversion.converter.FileConverterFactory;
import data_conversion.file_utility.FileTypes;
import data_conversion.file_utility.Timestamps;
import kotlin.Pair;
import model.TrialData;

import java.util.List;

public class MeanDataExporter extends DataExporter {

    @Override
    public void exportData(String basePath, String arffMeanFile, FileTypes fileType, Timestamps timestamp) {

        List<TrialData> trialData = super.readTrialDataAccordingToTimestamp(basePath, timestamp);

        SingleValueFeatureExtractor singleValueFeatureExtractor = new SingleValueFeatureExtractor();
        MeanAmplitude entireChannelMeanAmplitude = new MeanAmplitude(super.getSpktweMetadata().getWaveformSpikeOffset());
        List<Pair<Integer, float[]>> extractedData = singleValueFeatureExtractor.extract(trialData, entireChannelMeanAmplitude::extractValue);

        new FileConverterFactory().getFileConverter(fileType).convertData(arffMeanFile, extractedData);
    }
}
