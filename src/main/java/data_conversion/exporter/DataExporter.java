package data_conversion.exporter;

import algorithm.extractor.data.AfterStim;
import algorithm.extractor.data.AfterStimOff;
import algorithm.extractor.data.BeforeStim;
import algorithm.extractor.data.BetweenStim;
import data_conversion.file_utility.FileTypes;
import data_conversion.file_utility.Timestamps;
import javafx.util.Pair;
import model.Trial;
import model.TrialData;
import model.metadata.SpikeMetadata;
import model.metadata.SpktweMetadata;
import reader.spikes.DataSpikeReader;
import reader.MetadataReader;

import java.util.List;

public abstract class DataExporter {

    private SpktweMetadata spktweMetadata;

    private Pair<DataSpikeReader, List<Trial>> readData(String basePath) {
        MetadataReader metadataReader = new MetadataReader(basePath);
        this.spktweMetadata = metadataReader.readSPKTWE();
        DataSpikeReader dataReader = new DataSpikeReader(metadataReader.readETI(), new SpikeMetadata(this.spktweMetadata));

        List<Trial> trialsList = dataReader.readTrials();
        return new Pair<>(dataReader, trialsList);
    }

    private List<TrialData> readBetweenStimTrialData(String basePath) {

        Pair<DataSpikeReader, List<Trial>> readerAndData = readData(basePath);
        BetweenStim betweenStim = new BetweenStim(readerAndData.getKey());
        return betweenStim.extractData(readerAndData.getValue());
    }

    private List<TrialData> readAfterStimTrial(String basePath) {

        Pair<DataSpikeReader, List<Trial>> readerAndData = readData(basePath);
        AfterStim afterStim = new AfterStim(readerAndData.getKey());
        return afterStim.extractData(readerAndData.getValue());
    }

    private List<TrialData> readBeforeStimTrial(String basePath) {

        Pair<DataSpikeReader, List<Trial>> readerAndData = readData(basePath);
        BeforeStim beforeStim = new BeforeStim(readerAndData.getKey());
        return beforeStim.extractData(readerAndData.getValue());
    }

    private List<TrialData> readAfterStimOff(String basePath, int value) {

        Pair<DataSpikeReader, List<Trial>> readerAndData = readData(basePath);
        AfterStimOff afterStimOff = new AfterStimOff(readerAndData.getKey(), value);
        return afterStimOff.extractData(readerAndData.getValue());
    }

    private List<TrialData> readAfterStimOn(String basePath, int value) {
        Pair<DataSpikeReader, List<Trial>> readerAndData = readData(basePath);
        AfterStimOff afterStimOff = new AfterStimOff(readerAndData.getKey(), value);
        return afterStimOff.extractData(readerAndData.getValue());
    }

    List<TrialData> readTrialDataAccordingToTimestamp(String basePath, Timestamps timestamp) {
        if(timestamp.equals(Timestamps.BEFORE)) {
            return readBeforeStimTrial(basePath);
        }
        else if(timestamp.equals(Timestamps.BETWEEN)) {
            return readBetweenStimTrialData(basePath);
        }
        else if(timestamp.equals(Timestamps.AFTER_STIM_OFF_INTERVAL)){
            return readAfterStimOff(basePath, 200);
        }
        else if(timestamp.equals(Timestamps.AFTER_STIM_ON_INTERVAL)) {
            return readAfterStimOn(basePath, 200);
        }
        else {
            return readAfterStimTrial(basePath);
        }
    }

    public abstract void exportData(String basePath, String fileName, FileTypes fileType, Timestamps timestamp);

    SpktweMetadata getSpktweMetadata() {
        return spktweMetadata;
    }
}
