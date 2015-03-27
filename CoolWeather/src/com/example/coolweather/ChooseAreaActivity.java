//用于遍历省/市/县数据的Activity
package com.example.coolweather;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.db.coolweatherDB;
import com.example.coolweather.db.coolweatherOpenHelper;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Country;
import com.example.coolweather.model.Province;
import com.example.coolweather.util.HttpCallbackListener;
import com.example.coolweather.util.Utility;
import com.example.coolweather.util.httpUtil;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAreaActivity extends Activity implements OnItemClickListener {

    ListView listView;
    TextView titleTextView;
    ArrayAdapter<String> adapter;
    private coolweatherDB mcoolweatherDB;
    private ArrayList<String> data = new ArrayList<String>();

    private Context context;

    private int currentLevel;
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTRY = 2;

    // 选中的省/市/镇
    private Province selectedProvince;
    private City selectedCity;

    // 显示省列表/市列表/县列表
    private List<Province> provinceList;
    private List<City> cityList;
    private List<Country> countryList;

    private static final int equalProvince = 2001;
    private static final int equalCity = 2002;
    private static final int equalCountry = 2003;
    private static final int error = 2004;

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case equalProvince:
                queryProvince();
                break;
            case equalCity:
                queryCity();
                break;
            case equalCountry:
                queryCountry();
                break;
            case error:
                Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
            default:
                break;
            }
        }
    };

    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        listView = (ListView) findViewById(R.id.list_view);
        titleTextView = (TextView) findViewById(R.id.title_txt);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
        mcoolweatherDB = coolweatherDB.getInstance(this);
        listView.setOnItemClickListener(this);
        context = this;

        queryProvince();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (currentLevel == LEVEL_PROVINCE) {
            selectedProvince = provinceList.get(position);
            queryCity();
        } else if (currentLevel == LEVEL_CITY) {
            selectedCity = cityList.get(position);
            queryCountry();
        }
    }

    // 查询全国的所有省,优先去数据库查询，如果没有查到就去服务器查询
    private void queryProvince() {
        provinceList = mcoolweatherDB.loadProvinces();
        if (provinceList.size() > 0) {
            data.clear();
            for (Province p : provinceList) {
                data.add(p.getProvinceName());
                // 用于通知数据发生变化时，这样新增的一条消息才能在Listview中显示
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                titleTextView.setText("中国");
                currentLevel = LEVEL_PROVINCE;
            }
        } else {
            queryFromServer(null, "province");
        }
    }

    // 查询全国的所有市,优先去数据库查询，如果没有查到就去服务器查询
    private void queryCity() {
        cityList = mcoolweatherDB.loadCities(selectedProvince.getId());
        if (cityList.size() > 0) {
            data.clear();
            for (City city : cityList) {
                data.add(city.getCityName());
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                titleTextView.setText(selectedProvince.getProvinceName());
                currentLevel = LEVEL_CITY;
            }
        } else {
            queryFromServer(selectedProvince.getProvinceCode(), "city");
        }
    }

    // 查询全国的所有镇,优先去数据库查询，如果没有查到就去服务器查询
    private void queryCountry() {
        countryList = mcoolweatherDB.loadCountries(selectedCity.getId());
        if (countryList.size() > 0) {
            data.clear();
            for (Country country : countryList) {
                data.add(country.getCountryName());
                adapter.notifyDataSetChanged();
                listView.setSelection(0);
                titleTextView.setText(selectedCity.getCityName());
                currentLevel = LEVEL_COUNTRY;
            }
        } else {
            queryFromServer(selectedCity.getCityCode(), "country");
        }
    }

    // 根据传入的代号和类型查询服务器的省/市/县数据
    private void queryFromServer(final String code, final String type) {
        // http://www.weather.com.cn/data/list3/city.xml
        // 01|北京,02|上海,03|天津,04|重庆,05|黑龙江,06|吉林,07|辽宁,08|内蒙古,
        // 09|河北,10|山西,11|陕西,12|山东,13|新疆,14|西藏,15|青海,16|甘肃,17|宁夏,18|河南,19|江苏,20|湖北,21|浙江,22|安徽,23|福建,24|江西,25|湖南,26|贵州,27|四川,28|广东,29|云南,30|广西,31|海南,32|香港,33|澳门,34|台湾
        // http://www.weather.com.cn/data/list3/city19.xml
        // 1901|南京,1902|无锡,1903|镇江,1904|苏州
        if (!TextUtils.isEmpty(code)) {
            address = "http://www.weather.com.cn/data/list3/city" + code + ".xml";
        } else {
            address = "http://www.weather.com.cn/data/list3/city.xml";
        }
        // 调用HttpUtil文件工具类
        httpUtil.sendHttpRequest(address, new HttpCallbackListener() {

            @Override
            public void onFinish(String response) {
                Boolean r = false;
                if ("province".equals(type)) {
                    r = Utility.handleProvincesResponse(mcoolweatherDB, response);
                    mHandler.sendEmptyMessage(equalProvince);
                } else if ("city".equals(type)) {
                    r = Utility.handleCitiesResponse(mcoolweatherDB, response, selectedProvince.getId());
                    mHandler.sendEmptyMessage(equalCity);
                } else if ("country".equals(type)) {
                    r = Utility.handleCountriesResponse(mcoolweatherDB, response, selectedCity.getId());
                    mHandler.sendEmptyMessage(equalCountry);
                }
            }

            @Override
            public void onError(Exception e) {
                mHandler.sendEmptyMessage(error);
            }
        });
    }
}
