package com.example.coolweather.model;

public class City {
    String mCityName;
    String mCityCode;
    int mCityId;
    int mProvinceId;

    public int getProvinceId() {
        return mProvinceId;
    }

    public void setProvinceId(int id) {
        this.mProvinceId = id;
    }

    public int getId() {
        return mCityId;
    }

    public void setId(int id) {
        this.mCityId = id;
    }

    public String getCityName() {
        return mCityName;
    }

    public String getCityCode() {
        return mCityCode;
    }

    public void setCityName(String name) {
        this.mCityName = name;
    }

    public void setCityCode(String code) {
        this.mCityCode = code;
    }
}
