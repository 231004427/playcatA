package com.sunlin.playcat.json;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.domain.MapsGoogle;
import com.sunlin.playcat.domain.MapsGoogleResultAddress;
import com.sunlin.playcat.domain.Local;

/**
 * Created by sunlin on 2017/7/7.
 */

public class City {
    public static String TAG="City";
    public static boolean GetCityJson(Local reustlCity, Activity activity, RestTask.ResponseCallback responseCallback)
    {
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            double latitude = 0;
            double longitude = 0;

            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                latitude = location.getLatitude(); // 经度
                longitude = location.getLongitude(); // 纬度
                reustlCity.setLatitude(latitude);
                reustlCity.setLongitude(longitude);
                //发出获取城市请求
                String url="http://maps.google.cn/maps/api/geocode/json?latlng="+latitude+","+longitude+"&sensor=true&language=zh-CN";
                ServerTask.Get(url,null,responseCallback);
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }
    }
    public static boolean GetCityFromJson(Local reustlCity, String json){
        try {
            Gson gson = new GsonBuilder().create();
            //address_components
            MapsGoogle mapsObj = gson.fromJson(json, MapsGoogle.class);
            if(mapsObj.getStatus().equals("OK")){
                if(mapsObj.getResults().length>1)
                {
                    int count=mapsObj.getResults()[0].getAddress_components().length;

                    MapsGoogleResultAddress address=new MapsGoogleResultAddress();
                    for(int i=0;i<count;i++){
                        address=mapsObj.getResults()[0].getAddress_components()[i];
                        //获取国家
                        if(address.getTypes()[0].equals("country"))
                        {
                            reustlCity.setCountry(address.getLong_name());
                        }
                        //获取省
                        if(address.getTypes()[0].equals("administrative_area_level_1"))
                        {
                            reustlCity.setProvince(address.getLong_name());
                        }
                        //获取区
                        if(address.getTypes()[0].equals("political"))
                        {
                            reustlCity.setPolitical(address.getLong_name());
                        }
                        //获取街道
                        if(address.getTypes()[0].equals("route"))
                        {
                            reustlCity.setRoute(address.getLong_name());
                        }
                        //获取门牌号
                        if(address.getTypes()[0].equals("street_number"))
                        {
                            reustlCity.setStreet(address.getLong_name());
                        }
                        //获取市
                        if(address.getTypes()[0].equals("locality"))
                        {
                            reustlCity.setCity(address.getLong_name());
                        }
                    }
                    return true;
                }
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            LogC.write(e,TAG+":GetCityFromJson");
            return false;
        }

    }
}
