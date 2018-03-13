package data_conversion.converter;

import data_conversion.file_utility.FileTypes;

public class FileConverterFactory {

    public FileConverter getFileConverter(FileTypes fileType) {

        if (fileType.equals(FileTypes.ARFF)) {
            return new ArffConverter();
        } else {
            return new CsvConverter();
        }
    }
}
