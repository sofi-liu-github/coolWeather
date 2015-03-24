package com.example.coolweather.util;

import android.text.TextUtils;

import com.example.coolweather.db.coolweatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;

public class Utility {
    // 解析处理服务器返回的省级信息
    public synchronized static boolean handleProvincesResponse(coolweatherDB db, String response) {
        if (!TextUtils.isEmpty(response)) {
            // TextUtils.isEmpty()它可以一次性进行两种空值的判断。当传入的字符串等于null或者等于空字符串的时候，这个方法都会返回true.
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String i : allProvinces) {
                    String[] allCity = i.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(allCity[0]);
                    province.setProvinceName(allCity[1]);
                    db.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    // 解析处理服务器返回的省级信息
    public synchronized static boolean handleCitiesResponse(coolweatherDB db, String response, int ProvinceId) {
        if (TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String cityInfo : allCities) {
                    City city = new City();
                    String[] allCountry = cityInfo.split("\\|");
                    city.setCityCode(allCountry[0]);
                    city.setCityName(allCountry[1]);
                    city.setProvinceId(ProvinceId);
                    db.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public synchronized static boolean handleCountriesResponse(coolweatherDB db, String response, int CityId) {
        if (TextUtils.isEmpty(response)) {
            String[] allCountries = response.split(",");
            if (allCountries != null && allCountries.length > 0) {
                for (String countriesInfo : allCountries) {
                    String[] s = countriesInfo.split("\\|");
                    Country country = new Country();
                    country.setCityId(CityId);
                    country.setCountryCode(s[0]);
                    country.setCountryName(s[1]);
                    db.saveCountry(country);
                }
                return true;
            }
        }
        return false;
    }
}
