package com.sunlin.playcat.domain;

import java.util.List;

/**
 * Created by sunlin on 2017/9/6.
 */

public class CollectList {
    int count;
    int start;
    int pageNum;
    int sid;
    int type;
    int status;
    int uid;
    List<Collect> list;
    BaseResult result;

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public int getStart() {
        return start;
    }

    public int getSid() {
        return sid;
    }

    public int getCount() {
        return count;
    }

    public int getPageNum() {
        return pageNum;
    }

    public BaseResult getResult() {
        return result;
    }

    public int getUid() {
        return uid;
    }

    public List<Collect> getList() {
        return list;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setList(List<Collect> list) {
        this.list = list;
    }

    public void setResult(BaseResult result) {
        this.result = result;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
