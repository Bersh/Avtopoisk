package domain;

import java.awt.image.BufferedImage;

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
    private BufferedImage image;

    public Car(String model, String brand, int mileage, int year, float engineCapacity, long price) {
        this(model, brand, mileage, year, engineCapacity, price, TransmissionType.NA, EngineType.NA, "", null);
    }

    public Car(String model, String brand, int mileage, int year, float engineCapacity, long price, TransmissionType transmissionType,
        EngineType engineType, String linkToDetails, BufferedImage image) {
        this.model = model;
        this.brand = brand;
        this.mileage = mileage;
        this.year = year;
        this.engineCapacity = engineCapacity;
        this.price = price;
        this.transmissionType = transmissionType;
        this.engineType = engineType;
        this.linkToDetails = linkToDetails;
        this.image = image;
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

    public BufferedImage getImage() {
        return image;
    }
}
