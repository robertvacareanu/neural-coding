package algorithm;

import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

public class SvmClassification {

    private String dataSourcePath;

    public SvmClassification(String dataSourcePath) {
        this.dataSourcePath = dataSourcePath;
    }

    public void startClassification() {

        try {
            System.out.println("Started the classification");
            ConverterUtils.DataSource source = new ConverterUtils.DataSource(dataSourcePath);
            System.out.println(source.getDataSet().size());
            Instances trainDataset = new Instances(source.getDataSet(), 0, 220);
            Instances testDataset = new Instances(source.getDataSet(), 214, 25);
            trainDataset.setClassIndex(trainDataset.numAttributes() - 1);

            SMO svm = new SMO();
            svm.buildClassifier(trainDataset);

            testDataset.setClassIndex(testDataset.numAttributes() - 1);
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

}
