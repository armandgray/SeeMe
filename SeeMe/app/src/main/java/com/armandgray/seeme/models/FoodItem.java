package com.armandgray.seeme.models;

import android.os.Parcel;
import android.os.Parcelable;

public class FoodItem implements Parcelable {

    private String itemName;
    private String category;
    private String description;
    private int sort;
    private double price;
    private String image;

    public FoodItem() {
    }

    public FoodItem(String itemName, String category, String description, int sort, double price, String image) {
        super();
        this.itemName = itemName;
        this.category = category;
        this.description = description;
        this.sort = sort;
        this.price = price;
        this.image = image;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.itemName);
        dest.writeString(this.category);
        dest.writeString(this.description);
        dest.writeInt(this.sort);
        dest.writeDouble(this.price);
        dest.writeString(this.image);
    }

    protected FoodItem(Parcel in) {
        this.itemName = in.readString();
        this.category = in.readString();
        this.description = in.readString();
        this.sort = in.readInt();
        this.price = in.readDouble();
        this.image = in.readString();
    }

    public static final Parcelable.Creator<FoodItem> CREATOR = new Parcelable.Creator<FoodItem>() {
        @Override
        public FoodItem createFromParcel(Parcel source) {
            return new FoodItem(source);
        }

        @Override
        public FoodItem[] newArray(int size) {
            return new FoodItem[size];
        }
    };
}
