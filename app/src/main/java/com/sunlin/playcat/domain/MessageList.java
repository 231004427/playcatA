package com.sunlin.playcat.domain;

import java.util.List;

/**
 * Created by sunlin on 2017/8/26.
 */
public class MessageList {
    int count;
    int start;
    int pageNum;
    int from_user;
    int to_user;
    int type;
    List<Message> list;

    public int getFrom_user() {
        return from_user;
    }

    public int getTo_user() {
        return to_user;
    }

    public void setFrom_user(int from_user) {
        this.from_user = from_user;
    }

    public void setTo_user(int to_user) {
        this.to_user = to_user;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setList(List<Message> list) {
        this.list = list;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getStart() {
        return start;
    }

    public List<Message> getList() {
        return list;
    }
}
