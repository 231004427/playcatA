package com.sunlin.playcat.domain;
import java.util.List;

/**
 * Created by sunlin on 2017/7/25.
 */
public class CommentList {
    int count;
    int start;
    int pageNum;
    int type;
    int sid;
    int status;
    int userId;
    List<Comment> comments;
    BaseResult result;

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

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

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
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

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public void setStart(int start) {
        this.start = start;
    }
}
