package com.sunlin.playcat.domain;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/8.
 */

public class Local {
    private int uid=-1;
    private String country="";
    private String province="";
    private String city="";
    private String political="";
    private String route="";
    private String street="";
    private Date updateTime;
    private double latitude = 0;
    private double longitude = 0;
    private int status=1;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPolitical(String political) {
        this.political = political;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public int getUid() {
        return uid;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getPolitical() {
        return political;
    }

    public String getProvince() {
        return province;
    }

    public String getRoute() {
        return route;
    }

    public String getStreet() {
        return street;
    }
}
