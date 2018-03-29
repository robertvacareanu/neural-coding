package utility;

import data_conversion.exporter.DataExporter;
import data_conversion.exporter.FireRateDataExporter;
import data_conversion.exporter.MeanDataExporter;
import data_conversion.file_utility.FeatureType;
import data_conversion.file_utility.FileTypes;
import data_conversion.file_utility.Timestamps;

import java.util.HashMap;
import java.util.Map;


class ExportService {

    private MeanDataExporter meanDataExporter;
    private FireRateDataExporter fireRateDataExporter;
    private Map<Enum, DataExporter> exporterMapping;

    ExportService() {
        this.meanDataExporter = new MeanDataExporter();
        this.fireRateDataExporter = new FireRateDataExporter();
        buildExporterMappings();
    }

    private void buildExporterMappings() {
        exporterMapping = new HashMap<>();
        exporterMapping.put(FeatureType.MEAN_AMP, this.meanDataExporter);
        exporterMapping.put(FeatureType.FIRE_RATE, this.fireRateDataExporter);
    }

    public void exportData(String basePath, Timestamps timestamp) {

        for (FeatureType featureType : FeatureType.values()) {
            for (FileTypes fileType : FileTypes.values()) {
                DataExporter dataExporter = exporterMapping.get(featureType);
                String fileName = String.format("data/%s_%s_M017.%s",
                        featureType.getFeatureDesc(),
                        timestamp.getTimeStamp(),
                        fileType.getValue());

                dataExporter.exportData(basePath, fileName, fileType, timestamp);
            }
        }
    }
}

public class ExportDataToFormats {
    public static void main(String[] args) {

        if (args.length < 1) {
            throw new IllegalArgumentException("Not enough arguments");
        }

        ExportService exportService = new ExportService();
        exportService.exportData(args[0], Timestamps.BEFORE);
        exportService.exportData(args[0], Timestamps.BETWEEN);
        exportService.exportData(args[0], Timestamps.AFTER);
        exportService.exportData(args[0], Timestamps.AFTER_STIM_ON_INTERVAL);
        exportService.exportData(args[0], Timestamps.AFTER_STIM_OFF_INTERVAL);

    }

}
