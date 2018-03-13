package models;

/**
 * Created by tarek on 3/13/18.
 */

public class GeneralInformationModel {
    private int type;
    private String headline;
    private float value;
    private float maxValue;

    public GeneralInformationModel(int type, String headline, float value, float maxValue) {
        this.type = type;
        this.headline = headline;
        this.value = value;
        this.maxValue = maxValue;
    }

    public int getType() {
        return type;
    }

    public String getHeadline() {
        return headline;
    }

    public float getValue() {
        return value;
    }

    public float getMaxValue() {
        return maxValue;
    }
}
