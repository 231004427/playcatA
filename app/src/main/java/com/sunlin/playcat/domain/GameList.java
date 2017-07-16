package com.sunlin.playcat.domain;

import java.util.List;

/**
 * Created by sunlin on 2017/7/16.
 */
public class GameList {
    int count;
    int start;
    int pageNum;
    int isGood;
    int type;
    List<Game> games;
    BaseResult result;

    public BaseResult getResult() {
        return result;
    }

    public void setResult(BaseResult result) {
        this.result = result;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public List<Game> getGames() {
        return games;
    }


    public int getIsGood() {
        return isGood;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getStart() {
        return start;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setIsGood(int isGood) {
        this.isGood = isGood;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
