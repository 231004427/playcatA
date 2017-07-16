package com.sunlin.playcat.domain;

import java.util.Date;

/**
 * Created by sunlin on 2017/6/28.
 */

public class User {
    private int id=-1;
    private String name="";
    private String phone="";
    private String password="";
    private int sex=1;
    private String city="";
    private String photo="";
    private int gold=0;
    private int zhuan=0;
    private int level=1;
    private int count=0;
    private Date create;
    private Date update;
    private int status=1;
    private Local local;

    public void setLocal(Local local) {
        this.local = local;
    }
    public Local getLocal() {
        return local;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
    public void setUpdate(Date update) {
        this.update = update;
    }
    public Date getUpdate() {
        return update;
    }

    public void setCreate(Date create) {
        this.create = create;
    }

    public Date getCreate() {
        return create;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public void setZhuan(int zhuan) {
        this.zhuan = zhuan;
    }

    public int getZhuan() {
        return zhuan;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getGold() {
        return gold;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setCity(String controy) {
        this.city = controy;
    }

    public String getCity() {
        return city;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {return id;}
    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name="+name+"]";
    }
}