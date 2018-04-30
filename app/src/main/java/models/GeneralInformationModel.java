package models;

import java.util.List;
import java.util.Random;

/**
 * Created by tarek on 3/13/18.
 */

public class GeneralInformationModel {
    private int type;
    private String headline;
    private List<Float> values;
    private float maxValue;
    private int usedValue;

    public GeneralInformationModel(int type, String headline, List<Float> values) {
        this.type = type;
        this.headline = headline;
        this.values = values;
        this.maxValue = -1;
        for (float number : values)
            if (number > this.maxValue)
                this.maxValue = number;

        usedValue = -1;
    }

    public int getType() {
        return type;
    }

    public String getHeadline() {
        return headline;
    }

    public void addValue(float value) {
        this.values.add(value);
        if (value > maxValue)
            maxValue = value;
    }

    public float getValueAt(int position) {
        return values.get(position);
    }

    public void setValueAt(float value, int position) {
        this.values.set(position, value);
        if (value > maxValue)
            this.maxValue = value;
    }

    public List<Float> getValues() {
        return values;
    }

    public void setValues(List<Float> values) {
        this.values = values;
        this.maxValue = -1;
        for (float number : values)
            if (number > this.maxValue)
                this.maxValue = number;
    }

    public int getUsedValue() {
        return usedValue;
    }

    public float generateValue() {
        Random random = new Random();
        int position = random.nextInt(values.size() - 1);
        usedValue = position;
        return values.get(position);
    }

    public float getMaxValue() {
        return maxValue;
    }
}
