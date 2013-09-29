package ua.avtopoisk.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents car
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 07.10.12
 */
public class Car implements Parcelable {
    private String model;
    private String brand;
    private int mileage;
    private int year;
    private float engineCapacity;
    private long price;  //USD
    private TransmissionType transmissionType;
    private EngineType engineType;
    private String linkToDetails;
    private String imageUrl;
    private String city;
    private String datePosted;
    private String engineDesc; //pure engine description string. As it is on web page


    public Car(String model, String brand, int mileage, int year, float engineCapacity, long price, String linkToDetails, String imageUrl, String city, String datePosted, String engineDesc) {
        this(model, brand, mileage, year, engineCapacity, price, TransmissionType.NA, EngineType.NA, linkToDetails, imageUrl, city, datePosted, engineDesc);
    }

    public Car(String model, String brand, int mileage, int year, float engineCapacity, long price, TransmissionType transmissionType,
               EngineType engineType, String linkToDetails, String imageUrl, String city, String datePosted, String engineDesc) {
        this.model = model;
        this.brand = brand;
        this.mileage = mileage;
        this.year = year;
        this.engineCapacity = engineCapacity;
        this.price = price;
        this.transmissionType = transmissionType;
        this.engineType = engineType;
        this.linkToDetails = linkToDetails;
        this.imageUrl = imageUrl;
        this.city = city;
        this.datePosted = datePosted;
        this.engineDesc = engineDesc;
    }

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public int getMileage() {
        return mileage;
    }

    public int getYear() {
        return year;
    }

    public float getEngineCapacity() {
        return engineCapacity;
    }

    public long getPrice() {
        return price;
    }

    public TransmissionType getTransmissionType() {
        return transmissionType;
    }

    public EngineType getEngineType() {
        return engineType;
    }

    public String getLinkToDetails() {
        return linkToDetails;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getCity() {
        return city;
    }

    public String getDatePosted() {
        return datePosted;
    }

    public String getEngineDesc() {
        return engineDesc;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.model);
        dest.writeString(this.brand);
        dest.writeInt(this.mileage);
        dest.writeInt(this.year);
        dest.writeFloat(this.engineCapacity);
        dest.writeLong(this.price);
        dest.writeParcelable(this.transmissionType, flags);
        dest.writeParcelable(this.engineType, flags);
        dest.writeString(this.linkToDetails);
        dest.writeString(this.imageUrl);
        dest.writeString(this.city);
        dest.writeString(this.datePosted);
        dest.writeString(this.engineDesc);
    }

    private Car(Parcel in) {
        this.model = in.readString();
        this.brand = in.readString();
        this.mileage = in.readInt();
        this.year = in.readInt();
        this.engineCapacity = in.readFloat();
        this.price = in.readLong();
        this.transmissionType = in.readParcelable(TransmissionType.class.getClassLoader());
        this.engineType = in.readParcelable(EngineType.class.getClassLoader());
        this.linkToDetails = in.readString();
        this.imageUrl = in.readString();
        this.city = in.readString();
        this.datePosted = in.readString();
        this.engineDesc = in.readString();
    }

    public static Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        public Car createFromParcel(Parcel source) {
            return new Car(source);
        }

        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
}
