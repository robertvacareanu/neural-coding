package utility;

import data_conversion.exporter.FireRateDataExporter;
import data_conversion.exporter.MeanDataExporter;
import data_conversion.file_utility.FileNamesAndLocations;
import data_conversion.file_utility.FileTypes;
import data_conversion.file_utility.Timestamps;


class ExportService {

    private MeanDataExporter meanDataExporter;
    private FireRateDataExporter fireRateDataExporter;

    ExportService() {
        this.meanDataExporter = new MeanDataExporter();
        this.fireRateDataExporter = new FireRateDataExporter();
    }

    public void exportData(String basePath, Timestamps timestamp) {

        if(timestamp.equals(Timestamps.BETWEEN)) {
            meanDataExporter.exportData(basePath, FileNamesAndLocations.MEAN_DATA_BETWEEN_M017_ARFF, FileTypes.ARFF, timestamp);
            meanDataExporter.exportData(basePath, FileNamesAndLocations.MEAN_DATA_BETWEEN_M017_CSV, FileTypes.CSV, timestamp);
            fireRateDataExporter.exportData(basePath, FileNamesAndLocations.FIRE_RATE_DATA_BETWEEN_M017_ARFF, FileTypes.ARFF, timestamp);
            fireRateDataExporter.exportData(basePath, FileNamesAndLocations.FIRE_RATE_DATA_BETWEEN_M017_CSV, FileTypes.CSV, timestamp);
        }
        else if(timestamp.equals(Timestamps.BEFORE)) {
            meanDataExporter.exportData(basePath, FileNamesAndLocations.MEAN_DATA_BEFORE_M017_ARFF, FileTypes.ARFF, timestamp);
            meanDataExporter.exportData(basePath, FileNamesAndLocations.MEAN_DATA_BEFORE_M017_CSV, FileTypes.CSV, timestamp);
            fireRateDataExporter.exportData(basePath, FileNamesAndLocations.FIRE_RATE_DATA_BEFORE_M017_ARFF, FileTypes.ARFF, timestamp);
            fireRateDataExporter.exportData(basePath, FileNamesAndLocations.FIRE_RATE_DATA_BEFORE_M017_CSV, FileTypes.CSV, timestamp);
        }
        else if(timestamp.equals(Timestamps.AFTER_STIM_ON_INTERVAL)) {
            meanDataExporter.exportData(basePath, FileNamesAndLocations.MEAN_DATA_AFTER_ON_TIME_M017_ARFF, FileTypes.ARFF, timestamp);
            meanDataExporter.exportData(basePath, FileNamesAndLocations.MEAN_DATA_AFTER_ON_TIME_M017_CSV, FileTypes.CSV, timestamp);
            fireRateDataExporter.exportData(basePath, FileNamesAndLocations.FIRE_RATE_DATA_AFTER_ON_TIME_M017_ARFF, FileTypes.ARFF, timestamp);
            fireRateDataExporter.exportData(basePath, FileNamesAndLocations.FIRE_RATE_DATA_AFTER_ON_TIME_M017_CSV, FileTypes.CSV, timestamp);
        }
        else if(timestamp.equals(Timestamps.AFTER_STIM_OFF_INTERVAL)) {
            meanDataExporter.exportData(basePath, FileNamesAndLocations.MEAN_DATA_AFTER_OFF_TIME_M017_ARFF, FileTypes.ARFF, timestamp);
            meanDataExporter.exportData(basePath, FileNamesAndLocations.MEAN_DATA_AFTER_OFF_TIME_M017_CSV, FileTypes.CSV, timestamp);
            fireRateDataExporter.exportData(basePath, FileNamesAndLocations.FIRE_RATE_DATA_AFTER_OFF_TIME_M017_ARFF, FileTypes.ARFF, timestamp);
            fireRateDataExporter.exportData(basePath, FileNamesAndLocations.FIRE_RATE_DATA_AFTER_OFF_TIME_M017_CSV, FileTypes.CSV, timestamp);
        }
        else {
            meanDataExporter.exportData(basePath, FileNamesAndLocations.MEAN_DATA_AFTER_M017_ARFF, FileTypes.ARFF, timestamp);
            meanDataExporter.exportData(basePath, FileNamesAndLocations.MEAN_DATA_AFTER_M017_CSV, FileTypes.CSV, timestamp);
            fireRateDataExporter.exportData(basePath, FileNamesAndLocations.FIRE_RATE_DATA_AFTER_M017_ARFF, FileTypes.ARFF, timestamp);
            fireRateDataExporter.exportData(basePath, FileNamesAndLocations.FIRE_RATE_DATA_AFTER_M017_CSV, FileTypes.CSV, timestamp);
        }
    }
}

public class ExportDataToFormats {
    public static void main(String[] args) {

        if(args.length < 1) {
            throw new IllegalArgumentException("Not enough arguments");
        }

        ExportService exportService = new ExportService();
        //exportService.exportData(args[0], Timestamps.BEFORE);
        //exportService.exportData(args[0], Timestamps.BETWEEN);
        //exportService.exportData(args[0], Timestamps.AFTER);
        exportService.exportData(args[0], Timestamps.AFTER_STIM_ON_INTERVAL);
        exportService.exportData(args[0], Timestamps.AFTER_STIM_OFF_INTERVAL);

    }

}
