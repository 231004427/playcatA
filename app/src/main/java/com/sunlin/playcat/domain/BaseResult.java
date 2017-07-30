package com.sunlin.playcat.domain;

/**
 * Created by sunlin on 2017/7/2.
 */

public class BaseResult {
    private int errcode;
    private String errmsg;
    private int type;
    private String text;
    private String data;

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getErrcode() {
        return errcode;
    }

    public int getType() {
        return type;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public String getText() {
        return text;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
