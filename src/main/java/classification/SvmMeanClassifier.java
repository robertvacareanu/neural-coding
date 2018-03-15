package classification;

import algorithm.SvmClassification;

import static data_conversion.file_utility.FileNamesAndLocations.MEAN_DATA_BETWEEN_M017_ARFF;

public class SvmMeanClassifier {

    public static void main(String[] args) {
        new SvmClassification().startSmoClassification(MEAN_DATA_BETWEEN_M017_ARFF);
    }

}
