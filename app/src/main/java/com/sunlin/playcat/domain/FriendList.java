package com.sunlin.playcat.domain;

import java.util.List;

/**
 * Created by sunlin on 2017/8/19.
 */

public class FriendList {
    int count;
    int start;
    int pageNum;
    int type;
    int group_id;
    int status;
    int user_id;
    List<Friend> list;
    BaseResult result;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public int getStatus() {
        return status;
    }

    public int getGroup_id() {
        return group_id;
    }

    public BaseResult getResult() {
        return result;
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

    public List<Friend> getList() {
        return list;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public void setList(List<Friend> list) {
        this.list = list;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setResult(BaseResult result) {
        this.result = result;
    }

    public void setStart(int start) {
        this.start = start;
    }

}

