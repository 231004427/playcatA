package com.sunlin.playcat.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.sunlin.playcat.domain.User;

/**
 * Created by sunlin on 2017/8/9.
 */

public class SharedData {

    public static void saveUser(User user,Context context){
        //保存用户数据
        SharedPreferences sp = context.getSharedPreferences(CValues.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("userid", user.getId());
        editor.putString("phone",user.getPhone());
        editor.putString("imei",user.getImei());
        editor.putString("name", user.getName());
        editor.putString("token",user.getToken());
        editor.putString("photo",user.getPhoto());
        editor.putInt("sex",user.getSex());
        editor.commit();
    }
    public static void removeUser(Context context){
        SharedPreferences sp = context.getSharedPreferences(CValues.APP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("userid", 0);
        editor.putString("phone","");
        editor.putString("imei","");
        editor.putString("name", "");
        editor.putString("token","");
        editor.putString("photo","");
        editor.putInt("sex",0);
        editor.commit();
    }
    public static User getUser(Context context){
        User user=new User();
        //保存用户数据
        SharedPreferences sp = context.getSharedPreferences(CValues.APP_NAME, Context.MODE_PRIVATE);
        user.setId(sp.getInt("userid",0));
        user.setPhone(sp.getString("phone",""));
        user.setImei(sp.getString("imei",""));
        user.setName(sp.getString("name",""));
        user.setToken(sp.getString("token",""));
        user.setPhoto(sp.getString("photo",""));
        user.setSex(sp.getInt("sex",0));
        return user;
    }

}
