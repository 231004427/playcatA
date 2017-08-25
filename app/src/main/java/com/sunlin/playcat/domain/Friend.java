package com.sunlin.playcat.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by sunlin on 2017/8/19.
 */

public class Friend implements Serializable {
    private int id;
    private int user_id;
    private int friend_id;
    private Date create_time;
    private int status;
    private int group_id;
    private int type;

    private String name;
    private int sex;
    private String city;
    private String photo;
    private int level;
    private int online;
    private int play_game_id;
    private String play_name;
    private int play_status;
    private String play_game_ico;
    private String phone;
    private int no_read;
    private String last_info;
    private Date last_time;

    public String getPlay_name() {
        return play_name;
    }

    public void setPlay_name(String play_name) {
        this.play_name = play_name;
    }

    public Date getLast_time() {
        return last_time;
    }

    public int getNo_read() {
        return no_read;
    }

    public String getLast_info() {
        return last_info;
    }

    public void setLast_info(String last_info) {
        this.last_info = last_info;
    }

    public void setLast_time(Date last_time) {
        this.last_time = last_time;
    }

    public void setNo_read(int no_read) {
        this.no_read = no_read;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPlay_game_ico(String play_game_ico) {
        this.play_game_ico = play_game_ico;
    }

    public String getPlay_game_ico() {
        return play_game_ico;
    }

    public int getPlay_game_id() {
        return play_game_id;
    }

    public int getPlay_status() {
        return play_status;
    }

    public void setPlay_game_id(int play_game_id) {
        this.play_game_id = play_game_id;
    }

    public void setPlay_status(int play_status) {
        this.play_status = play_status;
    }
    public int getOnline() {
        return online;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public String getPhoto() {
        return photo;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public int getFriend_id() {
        return friend_id;
    }

    public int getGroup_id() {
        return group_id;
    }

    public int getLevel() {
        return level;
    }

    public int getSex() {
        return sex;
    }

    public int getStatus() {
        return status;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getCity() {
        return city;
    }

    public String getName() {
        return name;
    }

    public void setOnline(int online) {
        this.online = online;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setFriend_id(int friend_id) {
        this.friend_id = friend_id;
    }

    public void setGroup_id(int group_id) {
        this.group_id = group_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

