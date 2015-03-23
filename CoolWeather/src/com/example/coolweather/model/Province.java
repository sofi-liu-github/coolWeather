package com.example.coolweather.model;

public class Province {
    String mProvinceName;
    String mProvinceCode;
    int mProvinceId;

    public int getId() {
        return mProvinceId;
    }

    public void setId(int id) {
        this.mProvinceId = id;
    }

    public String getProvinceName() {
        return mProvinceName;
    }

    public String getProvinceCode() {
        return mProvinceCode;
    }

    public void setProvinceName(String name) {
        this.mProvinceName = name;
    }

    public void setProvinceCode(String code) {
        this.mProvinceCode = code;
    }
}
