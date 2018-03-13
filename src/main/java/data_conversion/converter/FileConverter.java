package data_conversion.converter;

import data_conversion.file_utility.FileTypes;
import kotlin.Pair;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public abstract class FileConverter {

    void writeCsvData(List<kotlin.Pair<Integer, float[]>> extractedData, BufferedWriter bufferedWriter) {
        extractedData.forEach(data -> {
            try {
                float[] floatValues = data.getSecond();
                for (float floatValue : floatValues) {
                    bufferedWriter.write(String.format("%.2f,", floatValue));
                }
                bufferedWriter.write(String.format("%d\n", data.getFirst()));
            } catch (IOException e) {
                System.out.println("Could not write in the arff file!!");
            }
        });
    }

    public abstract void convertData(String exportFileName, List<Pair<Integer, float[]>> extractedData);

    abstract boolean accepts(FileTypes fileType);

}
