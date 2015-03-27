//将常用数据库操作封装起来，如省份信息的存储，市信息的存储，县信息的存储
package com.example.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.model.City;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class coolweatherDB {

    public static final int Version = 1;
    public static final String DATABASENAME = "coolWeather";
    // 静态
    public static coolweatherDB mCoolweatherDB;

    public SQLiteDatabase db;

    // 构造方法私有化
    private coolweatherDB(Context context) {
        // 创建数据库
        coolweatherOpenHelper mCoolweatherOpenHelper = new coolweatherOpenHelper(context, DATABASENAME, null, Version);
        db = mCoolweatherOpenHelper.getWritableDatabase();
    }

    // 静态 同步锁
    public static synchronized coolweatherDB getInstance(Context context) {
        if (mCoolweatherDB == null) {
            mCoolweatherDB = new coolweatherDB(context);
        }
        return mCoolweatherDB;
    }

    // 存储省份信息 insert()
    public void saveProvince(Province province) {
        if (province != null) {
            ContentValues cv = new ContentValues();
            cv.put("mProvinceName", province.getProvinceName());
            cv.put("mProvinceCode", province.getProvinceCode());
            db.insert("province", null, cv);
        }
    }

    // 读取省份信息 query()
    public List<Province> loadProvinces() {
        List<Province> list = new ArrayList<Province>();
        Cursor cursor = db.query("province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();

                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                province.setId(cursor.getInt(cursor.getColumnIndex(("province_id"))));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));

                list.add(province);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // 存城市信息 insert()
    public void saveCity(City city) {
        if (city != null) {
            ContentValues cv = new ContentValues();
            cv.put("city_name", city.getCityName());
            cv.put("city_code", city.getCityCode());
            cv.put("province_id", city.getProvinceId());
            db.insert("city", null, cv);
        }
    }

    // 读取城市信息 query()
    public List<City> loadCities(int provinceId) {
        List<City> list = new ArrayList<City>();
        Cursor cursor = db.query("city", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("city_id")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
                list.add(city);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // 存乡镇信息 insert()
    public void saveCountry(Country country) {
        if (country != null) {
            ContentValues cv = new ContentValues();
            cv.put("country_id", country.getId());
            cv.put("country_name", country.getCountryName());
            cv.put("country_code", country.getCountryCode());
            cv.put("city_id", country.getCityId());
            db.insert("country", null, cv);
        }
    }

    // 读取乡镇信息 query()
    public List<Country> loadCountries(int cityId) {
        List<Country> list = new ArrayList<Country>();
        Cursor cursor = db.query("country", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Country country = new Country();
                country.setId(cursor.getInt(cursor.getColumnIndex("country_id")));
                country.setCityId(cursor.getInt(cursor.getColumnIndex("city_id")));
                country.setCountryCode(cursor.getString(cursor.getColumnIndex("country_code")));
                country.setCountryName(cursor.getString(cursor.getColumnIndex("country_name")));
                list.add(country);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return null;
    }
}
