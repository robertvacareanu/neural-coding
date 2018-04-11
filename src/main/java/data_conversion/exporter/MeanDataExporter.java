package data_conversion.exporter;

import algorithm.extractor.feature.SingleValueFeatureExtractor;
import algorithm.extractor.value.MeanAmplitude;
import algorithm.processor.FeatureSelectorKt;
import algorithm.processor.UnitSelectorKt;
import data_conversion.converter.FileConverterFactory;
import data_conversion.file_utility.FileTypes;
import data_conversion.file_utility.SpikesTypes;
import data_conversion.file_utility.Timestamps;
import kotlin.Pair;
import model.TrialData;

import java.util.List;

public class MeanDataExporter extends DataExporter {

    @Override
    public void exportData(String basePath, String arffMeanFile, FileTypes fileType, Timestamps timestamp, SpikesTypes spikesTypes) {

        List<TrialData> trialData = super.readTrialDataAccordingToTimestamp(basePath, timestamp, spikesTypes);
        trialData = UnitSelectorKt.removeIfNotEnoughSpikes(trialData, 500);

        SingleValueFeatureExtractor singleValueFeatureExtractor = new SingleValueFeatureExtractor();
        MeanAmplitude entireChannelMeanAmplitude = new MeanAmplitude(super.getSpikeMetadata().getWaveformSpikeOffset());
        List<Pair<Integer, float[]>> extractedData = singleValueFeatureExtractor.extract(trialData, entireChannelMeanAmplitude::extractValue);

        List<Pair<Integer, float[]>> cleanData = FeatureSelectorKt.removeIfEmpty(extractedData);

        new FileConverterFactory().getFileConverter(fileType).convertData(arffMeanFile, extractedData);
    }
}
