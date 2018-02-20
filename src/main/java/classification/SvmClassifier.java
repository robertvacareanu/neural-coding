package classification;

import algorithm.SvmClassification;
import converter.FireRateDataExporter;
import converter.MeanDataExporter;

public class SvmClassifier {

    private static final String arffMeanFile = "src/main/java/data/mean_data.arff";
    private static String arffFireRateFile = "src/main/java/data/fire_rate_data.arff";

    private static void exportMeanDataToArff(String basePath) {

        MeanDataExporter meanDataExporter = new MeanDataExporter();
        meanDataExporter.exportDataToArffFormat(basePath, arffMeanFile);
    }

    private static void trainMeanSvm() {
        new SvmClassification(arffMeanFile).startClassification();
    }

    private static void exportFireRateDataToArff(String basePath) {
        FireRateDataExporter fireRateDataExporter = new FireRateDataExporter();
        fireRateDataExporter.exportDataToArffFormat(basePath, arffFireRateFile);
    }

    private static void trainFireRateSvm() {
        new SvmClassification(arffFireRateFile).startClassification();
    }

    public static void main(String[] args) {

        if (args.length < 1) {
            System.out.println("Please add the base path and the export file name to the arguments !!!");
            return;
        }

        //exportMeanDataToArff(args[0]);
        //trainMeanSvm();

        //exportFireRateDataToArff(args[0]);
        //trainFireRateSvm();
    }

}
