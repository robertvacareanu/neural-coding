package data_conversion.exporter;

import algorithm.extractor.feature.ExtractorConstructorKt;
import algorithm.extractor.feature.SingleValueFeatureExtractor;
import algorithm.extractor.value.Amplitude;
import algorithm.processor.SelectorKt;
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
        trialData = SelectorKt.removeIfNotEnoughSpikes(trialData, 500);

        SingleValueFeatureExtractor singleValueFeatureExtractor = new SingleValueFeatureExtractor();
        Amplitude entireChannelMeanAmplitude = new Amplitude(super.getSpikeMetadata().getWaveformSpikeOffset());
        List<Pair<Integer, float[]>> extractedData = singleValueFeatureExtractor.extract(trialData, ExtractorConstructorKt.constructExtractorDefault(entireChannelMeanAmplitude::extractValue));

        List<Pair<Integer, float[]>> cleanData = SelectorKt.removeIfEmpty(extractedData);

        new FileConverterFactory().getFileConverter(fileType).convertData(arffMeanFile, extractedData);
    }
}
