package com.sunlin.playcat.domain;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/27.
 */

public class Collect {
    private int id;
    private int uid;
    private int sid;
    private int type;
    private int status;
    private Date create_time;
    private String rule;

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public int getSid() {
        return sid;
    }

    public int getStatus() {
        return status;
    }

    public int getId() {
        return id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public int getType() {
        return type;
    }

    public int getUid() {
        return uid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}

