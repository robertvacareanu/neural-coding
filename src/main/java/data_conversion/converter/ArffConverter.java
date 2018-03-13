package data_conversion.converter;

import data_conversion.file_utility.FileTypes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ArffConverter extends FileConverter {

    private void createFileMetadata(Integer nrChannels, BufferedWriter bufferedWriter) throws IOException {

        bufferedWriter.write("@relation mean_amplitude\n");

        for (int i = 0; i < nrChannels; i++) {
            bufferedWriter.write(String.format("@attribute ch_%d REAL\n", i));
        }
        bufferedWriter.write("@attribute orientation_classes {0,45,90,180,135,225,315,270}\n");
    }

    public void convertData(String exportFileName, List<kotlin.Pair<Integer, float[]>> extractedData) {
        try {
            File exportFile = new File(exportFileName);
            FileWriter fileWriter = new FileWriter(exportFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            if (extractedData.size() > 0) {
                createFileMetadata(extractedData.get(0).getSecond().length, bufferedWriter);

                bufferedWriter.write("@data\n");
                writeCsvData(extractedData, bufferedWriter);
                bufferedWriter.flush();

            } else {
                throw new IOException();
            }

            fileWriter.close();
            bufferedWriter.close();

        } catch (IOException e) {
            System.out.println("Could not generate the arff file");
        }
    }

    @Override
    boolean accepts(FileTypes fileType) {
        return fileType.equals(FileTypes.ARFF);
    }
}
