package com.example.quarantineapp.DataModels;

public class ReportModel {
    String aadhar,mobile,name,status;

    public ReportModel() {
    }

    public ReportModel(String aadhar, String mobile, String name,String status) {
        this.aadhar = aadhar;
        this.mobile = mobile;
        this.name = name;
        this.status=status;
    }

    public String getAadhar() {
        return aadhar;
    }

    public void setAadhar(String aadhar) {
        this.aadhar = aadhar;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
