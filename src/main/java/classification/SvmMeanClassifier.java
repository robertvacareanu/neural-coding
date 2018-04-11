package classification;

import algorithm.SvmClassification;

public class SvmMeanClassifier {

    public static void main(String[] args) {
        new SvmClassification().startSmoClassification("data/SSD_MEAN_AMP_BTW_M017.ARFF");
    }

}
