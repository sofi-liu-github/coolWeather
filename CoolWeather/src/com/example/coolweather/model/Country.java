package com.example.coolweather.model;

public class Country {

    String mCountryName;
    String mCountryCode;
    int mCountryId;
    int mCityId;

    public int getCityId() {
        return mCityId;
    }

    public void setCityId(int id) {
        this.mCityId = id;
    }

    public int getId() {
        return mCountryId;
    }

    public void setId(int id) {
        this.mCountryId = id;
    }

    public String getCountryName() {
        return mCountryName;
    }

    public String getCountryCode() {
        return mCountryCode;
    }

    public void setCountryName(String name) {
        this.mCountryName = name;
    }

    public void setCountryCode(String code) {
        this.mCountryCode = code;
    }

}
