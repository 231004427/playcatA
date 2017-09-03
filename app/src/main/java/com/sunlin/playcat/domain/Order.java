package com.sunlin.playcat.domain;

import java.util.Date;
import java.util.List;

/**
 * Created by sunlin on 2017/8/16.
 */

public class Order {
    private int id;
    private int user_id;
    private int goods_id;
    private int type;
    private int price;
    private int num;
    private Date create_time;
    private String to_value;
    private int status;
    private int goods_value;
    private String goods_title;
    private int pay_way;
    private String pay_code;
    private Date pay_time;
    private String goods_img;

    public String getGoods_img() {
        return goods_img;
    }

    public void setGoods_img(String goods_img) {
        this.goods_img = goods_img;
    }

    public Date getPay_time() {
        return pay_time;
    }

    public int getPay_way() {
        return pay_way;
    }

    public String getPay_code() {
        return pay_code;
    }

    public String getTo_value() {
        return to_value;
    }

    public void setPay_code(String pay_code) {
        this.pay_code = pay_code;
    }

    public void setPay_time(Date pay_time) {
        this.pay_time = pay_time;
    }

    public void setPay_way(int pay_way) {
        this.pay_way = pay_way;
    }

    public void setTo_value(String to_value) {
        this.to_value = to_value;
    }
    public String getGoods_title() {
        return goods_title;
    }

    public void setGoods_title(String goods_title) {
        this.goods_title = goods_title;
    }

    public int getGoods_value() {
        return goods_value;
    }

    public void setGoods_value(int goods_value) {
        this.goods_value = goods_value;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public int getGoods_id() {
        return goods_id;
    }

    public int getNum() {
        return num;
    }

    public int getPrice() {
        return price;
    }

    public int getStatus() {
        return status;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setGoods_id(int goods_id) {
        this.goods_id = goods_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}