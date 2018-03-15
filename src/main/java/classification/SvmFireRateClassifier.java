package classification;

import algorithm.SvmClassification;

import static data_conversion.file_utility.FileNamesAndLocations.FIRE_RATE_DATA_BETWEEN_M017_ARFF;

public class SvmFireRateClassifier {

    public static void main(String args[]) {
        new SvmClassification().startSmoClassification(FIRE_RATE_DATA_BETWEEN_M017_ARFF);
    }
}
