package com.sunlin.playcat.domain;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/16.
 */
public class Game {
    private int id;
    private String name;
    private String note;
    private int type;
    private String ico;
    private String img;
    private int onlineNum;
    private int playerNum;
    private int version;
    private String down;
    private Date createTime;
    private int status;
    private int okNum;
    private int isgood;
    private int sortNum;

    public void setIsgood(int isgood) {
        this.isgood = isgood;
    }

    public int getIsgood() {
        return isgood;
    }

    public void setOkNum(int okNum) {
        this.okNum = okNum;
    }

    public int getOkNum() {
        return okNum;
    }

    public void setSortNum(int sortNum) {
        this.sortNum = sortNum;
    }

    public int getSortNum() {
        return sortNum;
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
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
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
        this.onlineNum = onlineNum;
    }

    public int getOnlineNum() {
        return onlineNum;
    }

    public void setPlayerNum(int playerNum) {
        this.playerNum = playerNum;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }
}
