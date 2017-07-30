package com.sunlin.playcat.domain;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/25.
 */
public class Comment {
    private int id;
    private int sid;
    private int rid;
    private int user_id;
    private int type;
    private String content;
    private int good_num;
    private int reply_num;
    private Date create_time;
    private int status;
    private String photo;
    private String name;
    private int level;

    public int getReplyNum() {
        return reply_num;
    }

    public void setReplyNum(int reply_num) {
        this.reply_num = reply_num;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setCreateTime(Date create_time) {
        this.create_time = create_time;
    }

    public void setRid(int fid) {
        this.rid = fid;
    }

    public void setGoodNum(int good_num) {
        this.good_num = good_num;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUserId(int user_id) {
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public Date getCreateTime() {
        return create_time;
    }

    public int getRid() {
        return rid;
    }

    public int getGoodNum() {
        return good_num;
    }

    public int getSid() {
        return sid;
    }

    public int getStatus() {
        return status;
    }

    public int getUserId() {
        return user_id;
    }

    public String getContent() {
        return content;
    }
}

