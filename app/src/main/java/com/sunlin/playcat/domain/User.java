package com.sunlin.playcat.domain;

import java.util.Date;
import java.util.Date;

/**
 * Created by sunlin on 2017/5/16.
 */
public class User {
    private int id=-1;
    private String name;
    private String phone;
    private String password;
    private int sex=1;
    private String city;
    private String photo;
    private int gold=0;
    private int zhuan=0;
    private int level=0;
    private int count=0;
    private Date create;
    private Date update;
    private int status=0;
    private Local local;
    private String phone2;
    private String token;
    private String imei;
    private int version;
    private String qq;
    private String address;
    private String weixin;
    private int online;
    private int no_read;
    private int loveNum;
    private int orderNum;

    public int getLoveNum() {
        return loveNum;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setLoveNum(int loveNum) {
        this.loveNum = loveNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public int getNo_read() {
        return no_read;
    }

    public void setNo_read(int no_read) {
        this.no_read = no_read;
    }

    public int getOnline() {
        return online;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public String getWeixin() {
        return weixin;
    }

    public void setWeixin(String weixin) {
        this.weixin = weixin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImei() {
        return imei;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getPhone2() {
        return phone2;
    }

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
