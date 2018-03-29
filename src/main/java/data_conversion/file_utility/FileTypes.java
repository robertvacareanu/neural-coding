package data_conversion.file_utility;

public enum FileTypes {
    ARFF("ARFF"),
    CSV("CSV");

    private final String description;

    FileTypes(String desc) {
        this.description = desc;
    }

    public String getValue() {
        return description;
    }

}
