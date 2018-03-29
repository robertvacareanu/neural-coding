package data_conversion.file_utility;

public enum Timestamps {
    BEFORE("BEF"),
    BETWEEN("BTW"),
    AFTER("AFT"),
    AFTER_STIM_OFF_INTERVAL("AFT_OFF"),
    AFTER_STIM_ON_INTERVAL("AFT_ON");

    private final String timeStamp;

    Timestamps(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getTimeStamp() {
        return this.timeStamp;
    }
}
