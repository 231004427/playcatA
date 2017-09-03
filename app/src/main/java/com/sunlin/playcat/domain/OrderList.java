package com.sunlin.playcat.domain;

import java.util.List;

/**
 * Created by sunlin on 2017/9/3.
 */
public class OrderList {
    int count;
    int start;
    int pageNum;
    int user_id;
    int status;
    List<Order> list;

    public int getCount() {
        return count;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getStart() {
        return start;
    }

    public int getStatus() {
        return status;
    }

    public int getUser_id() {
        return user_id;
    }

    public List<Order> getList() {
        return list;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setList(List<Order> list) {
        this.list = list;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
