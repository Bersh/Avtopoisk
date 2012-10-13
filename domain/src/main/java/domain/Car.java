package domain;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Represents car
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 07.10.12
 */
public class Car implements Parcelable {
    private static String MODEL_KEY = "model";
    private static String BARND_KEY = "brand";
    private static String MILLAGE_KEY = "mileage";
    private static String YEAR_KEY = "year";
    private static String ENGINE_CAPACITY_KEY = "engineCapacity";
    private static String PRICE_KEY = "price";
    private static String TRANSMISSION_TYPE_KEY = "transmissionType";
    private static String ENGINE_TYPE_KEY = "engineType";
    private static String LINK_DETAILS_KEY = "linkToDetails";
    private static String IMAGE_URL_KEY = "imageUrl";

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

    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    private Car(Parcel in) {
        Bundle data = in.readBundle();
        this.model = data.getString(MODEL_KEY);
        this.brand = data.getString(BARND_KEY);
        this.mileage = data.getInt(MILLAGE_KEY);
        this.year = data.getInt(YEAR_KEY);
        this.engineCapacity = data.getFloat(ENGINE_CAPACITY_KEY);
        this.price = data.getLong(PRICE_KEY);
        this.transmissionType = (TransmissionType) data.get(TRANSMISSION_TYPE_KEY);
        this.engineType = (EngineType) data.get(ENGINE_TYPE_KEY);
        this.linkToDetails = data.getString(LINK_DETAILS_KEY);
        this.imageUrl = data.getString(IMAGE_URL_KEY);
    }

    public Car(String model, String brand, int mileage, int year, float engineCapacity, long price, String imageUrl) {
        this(model, brand, mileage, year, engineCapacity, price, TransmissionType.NA, EngineType.NA, "", imageUrl);
    }

    public Car(String model, String brand, int mileage, int year, float engineCapacity, long price, TransmissionType transmissionType,
               EngineType engineType, String linkToDetails, String imageUrl) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle data = new Bundle();

        data.putString(MODEL_KEY, model);
        data.putString(BARND_KEY, brand);
        data.putInt(MILLAGE_KEY, mileage);
        data.putInt(YEAR_KEY, year);
        data.putFloat(ENGINE_CAPACITY_KEY, engineCapacity);
        data.putLong(PRICE_KEY, price);
        data.putSerializable(TRANSMISSION_TYPE_KEY, transmissionType);
        data.putSerializable(ENGINE_TYPE_KEY, engineType);
        data.putString(LINK_DETAILS_KEY, linkToDetails);
        data.putString(IMAGE_URL_KEY, imageUrl);

        parcel.writeBundle(data);
    }
}
