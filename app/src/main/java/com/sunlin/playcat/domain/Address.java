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
    private String country;
    private String province;
    private String city;
    private String address;
    private Date create_time;
    private int status;

    public int getId() {
        return id;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public String getCountry() {
        return country;
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

    public String getProvince() {
        return province;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setCity(String city) {
        this.city = city;
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

    public void setProvince(String province) {
        this.province = province;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
