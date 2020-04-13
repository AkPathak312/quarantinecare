package com.example.quarantineapp;

public class UserModel {

    public  String uid,email,name,age,aadhar,lattitude,longitude,request,lastpiculpoaded;

    public UserModel(String uid, String email, String name, String age, String aadhar, String lattitude, String longitude, String request, String lastpiculpoaded) {
        this.uid = uid;
        this.email = email;
        this.name = name;
        this.age = age;
        this.aadhar = aadhar;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.request = request;
        this.lastpiculpoaded = lastpiculpoaded;
    }
}
