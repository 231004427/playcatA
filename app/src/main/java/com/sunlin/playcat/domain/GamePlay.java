package com.sunlin.playcat.domain;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/29.
 */

public class GamePlay
{
    private int id;
    private int user_id;
    private int game_id;
    private int points;
    private int online;
    private int level;
    private int type;
    private Date create_time;
    private Date last_time;
    private int status;
    private String user_name;
    private int user_level;
    private String user_photo;

    public int getUser_level() {
        return user_level;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_photo() {
        return user_photo;
    }

    public void setUser_level(int user_level) {
        this.user_level = user_level;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public void setUser_photo(String user_photo) {
        this.user_photo = user_photo;
    }

    public int getType() {
        return type;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public int getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public Date getLast_time() {
        return last_time;
    }

    public int getGame_id() {
        return game_id;
    }

    public int getLevel() {
        return level;
    }

    public int getOnline() {
        return online;
    }

    public int getPoints() {
        return points;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public void setLast_time(Date last_time) {
        this.last_time = last_time;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

