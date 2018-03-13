package data_conversion.exporter;

import algorithm.extractor.data.AfterStim;
import algorithm.extractor.data.BeforeStim;
import algorithm.extractor.data.BetweenStim;
import data_conversion.file_utility.FileTypes;
import javafx.util.Pair;
import model.Trial;
import model.TrialData;
import model.metadata.SpikeMetadata;
import reader.DataReader;
import reader.MetadataReader;

import java.util.List;

public abstract class DataExporter {

    private SpikeMetadata spikeMetadata;

    private Pair<DataReader, List<Trial>> readData(String basePath) {
        MetadataReader metadataReader = new MetadataReader(basePath);
        this.spikeMetadata = metadataReader.readSPKTWE();
        reader.DataReader dataReader = new reader.DataReader(metadataReader.readEPD(),
                metadataReader.readSSD(),
                spikeMetadata,
                metadataReader.readETI());

        List<Trial> trialsList = dataReader.readTrials();
        return new Pair<>(dataReader, trialsList);
    }

    protected List<TrialData> readBetweenStimTrialData(String basePath) {

        Pair<DataReader, List<Trial>> readerAndData = readData(basePath);
        BetweenStim betweenStim = new BetweenStim(readerAndData.getKey());
        return betweenStim.extractData(readerAndData.getValue());
    }

    protected List<TrialData> readAfterStimTrial(String basePath) {

        Pair<DataReader, List<Trial>> readerAndData = readData(basePath);
        AfterStim afterStim = new AfterStim(readerAndData.getKey());
        return afterStim.extractData(readerAndData.getValue());
    }

    protected List<TrialData> readBeforeStimTrial(String basePath) {

        Pair<DataReader, List<Trial>> readerAndData = readData(basePath);
        BeforeStim beforeStim = new BeforeStim(readerAndData.getKey());
        return beforeStim.extractData(readerAndData.getValue());
    }

    public abstract void exportData(String basePath, String fileName, FileTypes fileType);

    SpikeMetadata getSpikeMetadata() {
        return spikeMetadata;
    }
}
