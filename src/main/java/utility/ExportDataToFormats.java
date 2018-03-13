package utility;

import data_conversion.exporter.FireRateDataExporter;
import data_conversion.exporter.MeanDataExporter;
import data_conversion.file_utility.FileNamesAndLocations;
import data_conversion.file_utility.FileTypes;

public class ExportDataToFormats {

    public static void main(String[] args) {

        if(args.length < 1) {
            throw new IllegalArgumentException("Not enough arguments");
        }

        MeanDataExporter meanDataExporter = new MeanDataExporter();
        FireRateDataExporter fireRateDataExporter = new FireRateDataExporter();

        meanDataExporter.exportData(args[0], FileNamesAndLocations.MEAN_DATA_M017_ARFF, FileTypes.ARFF);
        meanDataExporter.exportData(args[0], FileNamesAndLocations.MEAN_DATA_M017_CSV, FileTypes.CSV);
        fireRateDataExporter.exportData(args[0], FileNamesAndLocations.FIRE_RATE_DATA_M017_ARFF, FileTypes.ARFF);
        fireRateDataExporter.exportData(args[0], FileNamesAndLocations.FIRE_RATE_DATA_M017_CSV, FileTypes.CSV);
    }

}
