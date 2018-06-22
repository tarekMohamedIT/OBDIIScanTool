package models;

import android.os.Parcel;
import android.os.Parcelable;

public class FaultCode implements Parcelable {

    public static final Creator<FaultCode> CREATOR= new Creator<FaultCode>() {
        @Override
        public FaultCode createFromParcel(Parcel in) {
            return new FaultCode(in);
        }

        @Override
        public FaultCode[] newArray(int size) {
            return new FaultCode[size];
        }
    };

    private String name;
    private String descrption;
    private String Id;

    public String getName() {
        return name;
    }

    public String getDescrption() {
        return descrption;
    }

    public String getId() {
        return Id;
    }

    public FaultCode(String name, String description, String id) {
        this.name = name;
        this.descrption = description;
        this.Id=id;
    }

    protected FaultCode(Parcel in){

        name=in.readString();
        descrption=in.readString();
        Id=in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel (Parcel parcel, int i){

        parcel.writeString(name);
        parcel.writeString(descrption);
        parcel.writeString(Id);



    }


}
