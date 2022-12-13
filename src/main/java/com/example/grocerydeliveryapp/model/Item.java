package com.example.grocerydeliveryapp.model;

import java.io.Serializable;

public class Item implements Serializable {
    private int image;
    private String name;
    private double price;

    public Item(int image, String name, double price) {
        this.image = image;
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }

    public double getPrice() {
        return price;
    }
}
