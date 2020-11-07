package com.example.quarantineapp.DataModels;

public class ManageReportModel {
    String name,date,image_link;

    public ManageReportModel() {
    }

    public ManageReportModel(String name, String date, String image_link) {
        this.name = name;
        this.date = date;
        this.image_link = image_link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
