package com.sunlin.playcat.domain;

import java.util.Date;

/**
 * Created by sunlin on 2017/8/9.
 */
public class Address {
    private int id;
    private int user_id;
    private String name;
    private String phone;
    private String post_code;
    private String address;
    private Date create_time;
    private int status;
    private int country_id;
    private int province_id;
    private int city_id;
    private int district_id;
    private String area_name;

    public int getCity_id() {
        return city_id;
    }

    public int getCountry_id() {
        return country_id;
    }

    public int getDistrict_id() {
        return district_id;
    }

    public int getProvince_id() {
        return province_id;
    }

    public String getArea_name() {
        return area_name;
    }

    public void setArea_name(String area_name) {
        this.area_name = area_name;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public void setCountry_id(int country_id) {
        this.country_id = country_id;
    }

    public void setDistrict_id(int district_id) {
        this.district_id = district_id;
    }

    public void setProvince_id(int province_id) {
        this.province_id = province_id;
    }

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public String getPhone() {
        return phone;
    }

    public int getStatus() {
        return status;
    }

    public String getPost_code() {
        return post_code;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPost_code(String post_code) {
        this.post_code = post_code;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}