package com.sunlin.playcat.domain;

import java.util.List;

/**
 * Created by sunlin on 2017/7/29.
 */

public class GamePlayList {
    int id;
    int count;
    int start;
    int pageNum;
    int type;
    List<GamePlay> gamePlays;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
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

    public List<GamePlay> getGamePlays() {
        return gamePlays;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setGamePlays(List<GamePlay> gamePlays) {
        this.gamePlays = gamePlays;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setStart(int start) {
        this.start = start;
    }
}

