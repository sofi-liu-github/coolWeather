//创建三张表
package com.example.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class coolweatherOpenHelper extends SQLiteOpenHelper {

    private Context context;
    // 建省表，注意表名province/city/country
    public static final String CREATE_PROVINCE = "create table province (" + "province_id integer primary key autoincrement,"
            + "province_name text," + "province_code text)";
    // 建市表
    public static final String CREATE_CITY = "create table city ( " + "city_id integer primary key autoincrement,"
            + "city_name text," + "city_code text," + "province_id integer )";
    // 建县表
    public static final String CREATE_COUNTRY = "create table country(" + "country_id integer primary key autoincrement,"
            + "country_name text," + "country_code text," + "city_id integer)";

    public coolweatherOpenHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CITY);//创建城市表
        db.execSQL(CREATE_COUNTRY);//创建乡村表
        db.execSQL(CREATE_PROVINCE);//创建省表
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
