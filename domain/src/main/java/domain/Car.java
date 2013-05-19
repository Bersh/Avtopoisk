package domain;

/**
 * Represents car
 *
 * @author ibershadskiy <a href="mailto:iBersh20@gmail.com">Ilya Bershadskiy</a>
 * @since 07.10.12
 */
public class Car {
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
}
