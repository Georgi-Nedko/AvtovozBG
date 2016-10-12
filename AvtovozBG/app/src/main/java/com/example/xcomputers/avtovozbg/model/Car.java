package com.example.xcomputers.avtovozbg.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by svetlio on 12.10.2016 г..
 */

public class Car implements Serializable{
    private String model;
    private String brand;
    private int horsePower;
    private int yearOfManufacture;
    private int kilometers;
    private String color;
    private String description;
    private int price;
    private ArrayList<Bitmap> images;

    public Car(String model, String brand, int horsePower,int price, int yearOfManufacture, String color, int kilometers, String description, ArrayList<Bitmap> images) {
        this.model = model;
        this.brand = brand;
        this.price = price;
        this.horsePower = horsePower;
        this.yearOfManufacture = yearOfManufacture;
        this.color = color;
        this.kilometers = kilometers;
        this.description = description;
        this.images = images;

    }

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

    public int getPrice() {
        return price;
    }

   public ArrayList<Bitmap> getImages(){
       return images;
   }
}
