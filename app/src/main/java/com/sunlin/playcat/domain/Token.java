package com.sunlin.playcat.domain;
import java.util.Date;

/**
 * Created by sunlin on 2017/10/25.
 */
public class Token {
    private int id;
    private int user_id;
    private String phone;
    private String password;
    private String token_data;
    private Date expire_time;
    private Date create_time;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public Date getExpire_time() {
        return expire_time;
    }

    public int getUser_id() {
        return user_id;
    }

    public String getToken_data() {
        return token_data;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public void setExpire_time(Date expire_time) {
        this.expire_time = expire_time;
    }

    public void setToken_data(String token_data) {
        this.token_data = token_data;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}

