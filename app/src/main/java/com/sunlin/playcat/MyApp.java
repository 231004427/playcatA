package com.sunlin.playcat;

import android.app.Application;

import com.sunlin.playcat.domain.User;

/**
 * Created by sunlin on 2017/8/9.
 */

public class MyApp extends Application {

    private static MyApp instance = null;

    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
    }

    private User user;
    public int versionCode;
    public String versionName;

    public int update_code;
    public String update_name;
    public int update_type;
    public String update_url;


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
