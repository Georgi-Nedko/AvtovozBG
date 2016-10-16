package com.example.xcomputers.avtovozbg.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by svetlio on 12.10.2016 г..
 */

public class Car implements Parcelable {
    private String model;
    private String brand;
    private int horsePower;
    private int yearOfManufacture;
    private int kilometers;
    private String color;
    private String description;

    private String price;
    private ArrayList<Bitmap> images;
    private String imageUrls;


    public Car(String model, String brand, int horsePower,String price, int yearOfManufacture, String color, int kilometers, String description, ArrayList<Bitmap> images, String imageUrls) {

        this.model = model;
        this.brand = brand;
        this.price = price;
        this.horsePower = horsePower;
        this.yearOfManufacture = yearOfManufacture;
        this.color = color;
        this.kilometers = kilometers;
        this.description = description;
        //this.images = images;
        //this.imageUrls = imageUrls;
    }

    protected Car(Parcel in) {
        model = in.readString();
        brand = in.readString();
        horsePower = in.readInt();
        yearOfManufacture = in.readInt();
        kilometers = in.readInt();
        color = in.readString();
        description = in.readString();
        price = in.readString();
        images = in.createTypedArrayList(Bitmap.CREATOR);
        imageUrls = in.readString();

    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    public String getModel() {
        return model;
    }

    public String getBrand() {
        return brand;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public int getKilometers() {
        return kilometers;
    }

    public int getYearOfManufacture() {
        return yearOfManufacture;
    }

    public String getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public ArrayList<Bitmap> getImages() {
        return images;
    }

    public String getImageUrls() {
        return this.imageUrls;
   }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(model);
        dest.writeString(brand);
        dest.writeInt(horsePower);
        dest.writeInt(yearOfManufacture);
        dest.writeInt(kilometers);
        dest.writeString(color);
        dest.writeString(description);

        dest.writeString(price);
        dest.writeTypedList(images);
        dest.writeString(imageUrls);

    }
}
