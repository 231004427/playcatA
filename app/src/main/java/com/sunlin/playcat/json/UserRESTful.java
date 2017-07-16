package com.sunlin.playcat.json;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.MD5;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.PCode;
import com.sunlin.playcat.domain.User;

/**
 * Created by sunlin on 2017/6/29.
 */
public class UserRESTful {
    private static final String TAG="UserRESTful";
    //发送手机验证码
    public void sendCode(String phone,RestTask.ResponseCallback responseCallback){
        Gson gson=new Gson();

        PCode pCode=new PCode();
        pCode.setPhone(phone);

        String jsonStr=gson.toJson(pCode);
        ServerTask.Post("sendCode",jsonStr,null,responseCallback);
    }
    //登录
    public void login(String phone, String password, RestTask.ResponseCallback responseCallback){
        Gson gson=new Gson();
        //String jsonStr="{phone:\""+phone+"\",password:\""+MD5.getMD5(password)+"\"}";
        User user=new User();
        user.setPhone(phone);
        user.setPassword(MD5.getMD5(password));
        String jsonStr=gson.toJson(user);
        ServerTask.Post("login",jsonStr,null,responseCallback);
    }
    //注册
    public void regist(User user,RestTask.ResponseCallback responseCallback){
        Gson gson=new Gson();
        String jsonStr=gson.toJson(user);
        ServerTask.Post("registUser",jsonStr,null,responseCallback);
    }
    //手机验证
    public  void phoneCheck(String phone,String code,RestTask.ResponseCallback responseCallback){
        Gson gson=new Gson();

        PCode pCode=new PCode();
        pCode.setPhone(phone);
        pCode.setCode(code);

        String jsonStr=gson.toJson(pCode);
        ServerTask.Post("phoneCheck",jsonStr,null,responseCallback);
    }
}
