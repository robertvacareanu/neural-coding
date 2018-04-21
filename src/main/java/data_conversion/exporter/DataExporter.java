package data_conversion.exporter;

import algorithm.extractor.data.AfterStim;
import algorithm.extractor.data.AfterStimOff;
import algorithm.extractor.data.BeforeStim;
import algorithm.extractor.data.BetweenStim;
import data_conversion.file_utility.FileTypes;
import data_conversion.file_utility.SpikesTypes;
import data_conversion.file_utility.Timestamps;
import javafx.util.Pair;
import model.Trial;
import model.TrialData;
import model.metadata.SpikeMetadata;
import reader.MetadataReader;
import reader.spikes.DataSpikeReader;

import java.util.LinkedList;
import java.util.List;

public abstract class DataExporter {

    private SpikeMetadata spikeMetadata;

    private Pair<DataSpikeReader, List<Trial>> readData(String basePath, SpikesTypes spikesType) {
        MetadataReader metadataReader = new MetadataReader(basePath);

        // needed in order to get the sorted spikes waveforms
        //ExporterKt.exportSpikeSortedWaveform(metadataReader);

        if(spikesType.equals(SpikesTypes.SORTED_SPIKES)) {
            this.spikeMetadata = new SpikeMetadata(metadataReader.readSSD());
        }
        else {
            this.spikeMetadata = new SpikeMetadata(metadataReader.readSPKTWE());
        }

        DataSpikeReader dataReader = new DataSpikeReader(metadataReader.readETI(), spikeMetadata, new LinkedList<>());

        List<Trial> trialsList = dataReader.readTrials();
        return new Pair<>(dataReader, trialsList);
    }

    private List<TrialData> readBetweenStimTrialData(String basePath, SpikesTypes spikesType) {

        Pair<DataSpikeReader, List<Trial>> readerAndData = readData(basePath, spikesType);
        BetweenStim betweenStim = new BetweenStim(readerAndData.getKey());
        return betweenStim.extractData(readerAndData.getValue());
    }

    private List<TrialData> readAfterStimTrial(String basePath, SpikesTypes spikesType) {

        Pair<DataSpikeReader, List<Trial>> readerAndData = readData(basePath, spikesType);
        AfterStim afterStim = new AfterStim(readerAndData.getKey());
        return afterStim.extractData(readerAndData.getValue());
    }

    private List<TrialData> readBeforeStimTrial(String basePath, SpikesTypes spikesType) {

        Pair<DataSpikeReader, List<Trial>> readerAndData = readData(basePath, spikesType);
        BeforeStim beforeStim = new BeforeStim(readerAndData.getKey());
        return beforeStim.extractData(readerAndData.getValue());
    }

    private List<TrialData> readAfterStimOff(String basePath, int value, SpikesTypes spikesType) {

        Pair<DataSpikeReader, List<Trial>> readerAndData = readData(basePath, spikesType);
        AfterStimOff afterStimOff = new AfterStimOff(readerAndData.getKey(), value);
        return afterStimOff.extractData(readerAndData.getValue());
    }

    private List<TrialData> readAfterStimOn(String basePath, int value, SpikesTypes spikesType) {
        Pair<DataSpikeReader, List<Trial>> readerAndData = readData(basePath, spikesType);
        AfterStimOff afterStimOff = new AfterStimOff(readerAndData.getKey(), value);
        return afterStimOff.extractData(readerAndData.getValue());
    }

    List<TrialData> readTrialDataAccordingToTimestamp(String basePath, Timestamps timestamp, SpikesTypes spikesType) {
        if(timestamp.equals(Timestamps.BEFORE)) {
            return readBeforeStimTrial(basePath, spikesType);
        }
        else if(timestamp.equals(Timestamps.BETWEEN)) {
            return readBetweenStimTrialData(basePath, spikesType);
        }
        else if(timestamp.equals(Timestamps.AFTER_STIM_OFF_INTERVAL)){
            return readAfterStimOff(basePath, 200, spikesType);
        }
        else if(timestamp.equals(Timestamps.AFTER_STIM_ON_INTERVAL)) {
            return readAfterStimOn(basePath, 200, spikesType);
        }
        else {
            return readAfterStimTrial(basePath, spikesType);
        }
    }

    public abstract void exportData(String basePath,
                                    String fileName,
                                    FileTypes fileType,
                                    Timestamps timestamp,
                                    SpikesTypes spikesType);

    SpikeMetadata getSpikeMetadata() {
        return spikeMetadata;
    }
}
