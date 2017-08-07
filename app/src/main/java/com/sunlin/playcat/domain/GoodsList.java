package com.sunlin.playcat.domain;

import java.util.List;

/**
 * Created by sunlin on 2017/8/6.
 */
public class GoodsList {
    int count;
    int start;
    int pageNum;
    int type;
    List<Goods> goodsList;

    public void setType(int type) {
        this.type = type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setStart(int start) {
        this.start = start;
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

    public List<Goods> getGoodsList() {
        return goodsList;
    }
}
