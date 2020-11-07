package com.example.quarantineapp.DataModels;

public class UploadImageModel {
    String date,image_link,location;

    public UploadImageModel() {

    }

    public UploadImageModel(String date, String image_link, String location) {
        this.date = date;
        this.image_link = image_link;
        this.location = location;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
