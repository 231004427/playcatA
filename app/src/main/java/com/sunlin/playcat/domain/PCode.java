package com.sunlin.playcat.domain;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/12.
 */
public class PCode {
    int id;
    String phone;
    String code;
    Date create_time;
    int status;

    public int getId() {
        return id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public int getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getPhone() {
        return phone;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
