package data_conversion.file_utility;

public enum SpikesTypes {

    SORTED_SPIKES("SSD"),
    UNSORTED_SPIKES("USD");

    private final String spikeDesc;


    SpikesTypes(String spikeDesc) {
        this.spikeDesc = spikeDesc;
    }

    public String getDescription() {
        return spikeDesc;
    }
}
