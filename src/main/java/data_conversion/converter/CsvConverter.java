package data_conversion.converter;

import data_conversion.file_utility.FileTypes;
import kotlin.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvConverter extends FileConverter {

    @Override
    public void convertData(String exportFileName, List<Pair<Integer, float[]>> extractedData) {

        try {
            File exportFile = new File(exportFileName);
            FileWriter fileWriter = new FileWriter(exportFile);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            if (extractedData.size() > 0) {
                writeCsvData(extractedData, bufferedWriter);
                bufferedWriter.flush();

            } else {
                throw new IOException();
            }

            fileWriter.close();
            bufferedWriter.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    boolean accepts(FileTypes fileType) {
        return fileType.equals(FileTypes.CSV);
    }

}
