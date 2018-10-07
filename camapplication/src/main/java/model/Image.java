package model;

import android.graphics.Bitmap;

public class Image {
    private String name;
    private Bitmap bitmapImage;
    private String Location;

    public Image() {
    }

    public Image(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public Image(String name, Bitmap bitmapImage, String location) {
        this.name = name;
        this.bitmapImage = bitmapImage;
        Location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmapImage() {
        return bitmapImage;
    }

    public void setBitmapImage(Bitmap bitmapImage) {
        this.bitmapImage = bitmapImage;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }


}
