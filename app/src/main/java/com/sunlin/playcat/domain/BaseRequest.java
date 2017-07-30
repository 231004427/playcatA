package com.sunlin.playcat.domain;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/27.
 */

public class BaseRequest {
    private int userid;
    private String token;
    private Date dateTime;
    private int actionType;
    private int appid;
    private String data;
    private int vesion;

    public int getVesion() {
        return vesion;
    }
    public void setVesion(int vesion) {
        this.vesion = vesion;
    }
    public String getData() {
        return data;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public int getActionType() {
        return actionType;
    }

    public int getAppid() {
        return appid;
    }

    public int getUserid() {
        return userid;
    }

    public String getToken() {
        return token;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setActionType(int actionType) {
        this.actionType = actionType;
    }

    public void setAppid(int appid) {
        this.appid = appid;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }
}
