package com.sunlin.playcat.domain;

import java.util.List;

/**
 * Created by sunlin on 2017/8/11.
 */
public class AreaList {
    int count;
    int start;
    int pageNum;
    int parent_id;
    int status;
    List<Area> list;

    public int getStatus() {
        return status;
    }

    public int getParent_id() {
        return parent_id;
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

    public List<Area> getList() {
        return list;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setList(List<Area> list) {
        this.list = list;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
