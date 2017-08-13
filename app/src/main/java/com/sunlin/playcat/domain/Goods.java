package com.sunlin.playcat.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sunlin on 2017/8/6.
 */
public class Goods implements Serializable {
    private int id;
    private String title;
    private String note;
    private String head_img;
    private int price;
    private int type;
    private int order;
    private Date create_time;
    private int status;
    private int stock;
    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public int getOrder() {
        return order;
    }

    public int getPrice() {
        return price;
    }

    public int getStatus() {
        return status;
    }

    public String getHead_img() {
        return head_img;
    }

    public String getNote() {
        return note;
    }

    public String getTitle() {
        return title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setHead_img(String head_img) {
        this.head_img = head_img;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
