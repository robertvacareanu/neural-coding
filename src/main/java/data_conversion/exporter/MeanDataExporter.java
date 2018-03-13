package data_conversion.exporter;

import algorithm.extractor.feature.MeanAmplitude;
import data_conversion.converter.FileConverterFactory;
import data_conversion.file_utility.FileTypes;
import kotlin.Pair;
import model.TrialData;

import java.util.List;

public class MeanDataExporter extends DataExporter {

    @Override
    public void exportData(String basePath, String arffMeanFile, FileTypes fileType) {

        List<TrialData> trialData = super.readBetweenStimTrialData(basePath);

        MeanAmplitude entireChannelMeanAmplitude = new MeanAmplitude(super.getSpikeMetadata().getWaveformSpikeOffset());
        List<Pair<Integer, float[]>> extractedData = entireChannelMeanAmplitude.extract(trialData);

        new FileConverterFactory().getFileConverter(fileType).convertData(arffMeanFile, extractedData);
    }
}
