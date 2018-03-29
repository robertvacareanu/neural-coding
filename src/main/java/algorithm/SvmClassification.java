package algorithm;

import javafx.util.Pair;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class SvmClassification {

    private Pair<Instances, Instances> splitData(String dataSourcePath) throws Exception {

        ConverterUtils.DataSource source = new ConverterUtils.DataSource(dataSourcePath);
        System.out.println(String.format("The total number of instances: %d", source.getDataSet().size()));
        Integer nrInstances = source.getDataSet().size();

        Double TRAINING_PERCENTAGE = 0.65;

        Double nrTrainData = (nrInstances * TRAINING_PERCENTAGE);
        Integer nrTestData = nrInstances - nrTrainData.intValue();

        System.out.println(String.format("Number of train instances: %d", nrTrainData.intValue()));
        System.out.println(String.format("Number of test instances: %d", nrTestData));

        Instances trainDataset = new Instances(source.getDataSet(), 0, nrTrainData.intValue());
        Instances testDataset = new Instances(source.getDataSet(), nrTrainData.intValue() + 1, nrTestData-1);
        trainDataset.setClassIndex(trainDataset.numAttributes() - 1);
        testDataset.setClassIndex(testDataset.numAttributes() - 1);

        return new Pair<>(trainDataset, testDataset);

    }

    public void startSmoClassification(String dataSoucePath) {
        try {
            System.out.println("Started the classification");

            Pair<Instances, Instances> dataSet = splitData(dataSoucePath);
            Instances trainDataset = dataSet.getKey();
            Instances testDataset = dataSet.getValue();

            SMO svm = new SMO();
            svm.buildClassifier(trainDataset);

            Evaluation eval2 = new Evaluation(testDataset);
            eval2.evaluateModel(svm, testDataset);
            System.out.println(eval2.toSummaryString());
            System.out.println("Ended the classification");
        }
        catch (Exception e) {
            System.out.println("Could not train the classifier !!");
            e.printStackTrace();
        }
    }

    public void startLibSvmClassification(String dataSourcePath) {

        try {
            ConverterUtils.DataSource source = new ConverterUtils.DataSource(dataSourcePath);

            Pair<Instances, Instances> dataSet = splitData(dataSourcePath);
            Instances trainDataset = dataSet.getKey();
            Instances testDataset = dataSet.getValue();

            LibSVM libsvm = new LibSVM();
            String[] options = new String[8];
            options[0] = "-S";
            options[1] = "0";
            options[2] = "-K";
            options[3] = "2";
            options[4] = "-G";
            options[5] = "1.0";
            options[6] = "-C";
            options[7] = "1.0";
            libsvm.setOptions(options);

            libsvm.buildClassifier(trainDataset);
            Evaluation eval3 = new Evaluation(testDataset);
            eval3.evaluateModel(libsvm, testDataset);
            System.out.println(eval3.toSummaryString());
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Could not train the classifier using libSvm");
        }
    }

}
