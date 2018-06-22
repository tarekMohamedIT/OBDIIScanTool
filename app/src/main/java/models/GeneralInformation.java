package models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by tarek on 3/13/18.
 */

public class GeneralInformation implements Parcelable {
    public static final Creator<GeneralInformation> CREATOR = new Creator<GeneralInformation>() {
        @Override
        public GeneralInformation createFromParcel(Parcel in) {
            return new GeneralInformation(in);
        }

        @Override
        public GeneralInformation[] newArray(int size) {
            return new GeneralInformation[size];
        }
    };
    private int type;
    private String headline;
    private List<Float> values;
    private float maxValue;
    private float usedValue;

    public GeneralInformation(int type, String headline, List<Float> values) {
        this.type = type;
        this.headline = headline;
        this.values = values;
        this.maxValue = -1;
        for (float number : values)
            if (number > this.maxValue)
                this.maxValue = number;

        usedValue = -1;
    }

    protected GeneralInformation(Parcel in) {
        type = in.readInt();
        headline = in.readString();
        maxValue = in.readFloat();
        usedValue = in.readInt();
        values = new ArrayList<>();
        in.readList(values, null);
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

    public float getUsedValue() {
        return usedValue;
    }

    public void setUsedValue(float usedValue) {
        this.usedValue = usedValue;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(type);
        parcel.writeString(headline);
        parcel.writeFloat(maxValue);
        parcel.writeFloat(usedValue);
        parcel.writeList(values);
    }
}
