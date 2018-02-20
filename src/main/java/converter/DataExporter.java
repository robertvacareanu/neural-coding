package converter;

import algorithm.extractor.data.BetweenStim;
import model.Trial;
import model.TrialData;
import model.metadata.SpikeMetadata;
import reader.MetadataReader;

import java.util.List;

public abstract class DataExporter {

    private SpikeMetadata spikeMetadata;

    public List<TrialData> readTrialData(String basePath) {
        MetadataReader metadataReader = new MetadataReader(basePath);
        this.spikeMetadata = metadataReader.readSPKTWE();
        reader.DataReader dataReader = new reader.DataReader(metadataReader.readEPD(),
                metadataReader.readSSD(),
                spikeMetadata,
                metadataReader.readETI());

        List<Trial> trialsList = dataReader.readTrials();
        BetweenStim betweenStim = new BetweenStim(dataReader);
        return betweenStim.extractData(trialsList);
    }

    public abstract void exportDataToArffFormat(String basePath, String fileName);

    public SpikeMetadata getSpikeMetadata() {
        return spikeMetadata;
    }
}
