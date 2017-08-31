package com.sunlin.playcat.domain;

import java.util.Date;

/**
 * Created by sunlin on 2017/8/23.
 */
public class Message {
    private int id;
    private int from_user;
    private int to_user;
    private int vesion;
    private int type;
    private int length;
    private String data;
    private int status;
    private Date create_time;

    private String from_name;
    private String from_photo;
    private int from_sex;

    public int getFrom_sex() {
        return from_sex;
    }

    public String getFrom_name() {
        return from_name;
    }

    public String getFrom_photo() {
        return from_photo;
    }

    public void setFrom_name(String from_name) {
        this.from_name = from_name;
    }

    public void setFrom_photo(String from_photo) {
        this.from_photo = from_photo;
    }

    public void setFrom_sex(int from_sex) {
        this.from_sex = from_sex;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getVesion() {
        return vesion;
    }

    public int getFrom_user() {
        return from_user;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public int getLength() {
        return length;
    }

    public int getStatus() {
        return status;
    }

    public int getTo_user() {
        return to_user;
    }

    public String getData() {
        return data;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setVesion(int vesion) {
        this.vesion = vesion;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setFrom_user(int from_user) {
        this.from_user = from_user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTo_user(int to_user) {
        this.to_user = to_user;
    }
}

