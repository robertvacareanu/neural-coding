package data_conversion.file_utility;

public enum FeatureType {
    MEAN_AMP("MEAN_AMP"),
    FIRE_RATE("FIRE_RATE");


    private String featureDesc;

    FeatureType(String featureDesc) {
        this.featureDesc = featureDesc;
    }

    public String getFeatureDesc() {
        return this.featureDesc;
    }
}
