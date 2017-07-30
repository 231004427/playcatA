package com.sunlin.playcat.domain;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/16.
 */
public class Game {
    private int id;
    private String name;
    private String note;
    private String note2;
    private int type;
    private String ico;
    private String img;
    private String img_big;
    private int online_num;
    private int player_num;
    private int version;
    private String down;
    private Date create_time;
    private int status;
    private int ok_num;
    private int isgood;
    private int sort_num;
    private int collect;

    public int getCollect() {
        return collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public String getImgBig() {
        return img_big;
    }

    public void setImgBig(String imgBig) {
        this.img_big = imgBig;
    }

    public void setNote2(String note2) {
        this.note2 = note2;
    }

    public String getNote2() {
        return note2;
    }

    public void setIsgood(int isgood) {
        this.isgood = isgood;
    }

    public int getIsgood() {
        return isgood;
    }

    public void setOkNum(int okNum) {
        this.ok_num = okNum;
    }

    public int getOkNum() {
        return ok_num;
    }

    public void setSortNum(int sortNum) {
        this.sort_num = sortNum;
    }

    public int getSortNum() {
        return sort_num;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setCreateTime(Date createTime) {
        this.create_time = createTime;
    }

    public Date getCreateTime() {
        return create_time;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public String getDown() {
        return down;
    }

    public void setIco(String ico) {
        this.ico = ico;
    }

    public String getIco() {
        return ico;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getImg() {
        return img;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getNote() {
        return note;
    }

    public void setOnlineNum(int onlineNum) {
        this.online_num = onlineNum;
    }

    public int getOnlineNum() {
        return online_num;
    }

    public void setPlayerNum(int playerNum) {
        this.player_num = playerNum;
    }

    public int getPlayerNum() {
        return player_num;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
