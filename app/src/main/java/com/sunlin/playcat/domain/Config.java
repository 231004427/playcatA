package com.sunlin.playcat.domain;

/**
 * Created by sunlin on 2017/9/13.
 */
public class Config {
    private int id;
    private int type;
    private String name;
    private String value;
    private int status;

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}

