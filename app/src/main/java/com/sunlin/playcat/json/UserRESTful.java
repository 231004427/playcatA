package com.sunlin.playcat.json;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.MD5;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.RestUtil;
import com.sunlin.playcat.domain.User;

import org.json.JSONObject;

import java.net.URLEncoder;

/**
 * Created by sunlin on 2017/6/29.
 */
public class UserRESTful {
    private static final String TAG="UserRESTful";
    //处理返回结果
    @Nullable
    public static BaseResult getResult(String json){
        try {
            Gson gson = new Gson();
            BaseResult result = gson.fromJson(json, BaseResult.class);
            return result;
        }catch (Exception e){
            LogC.write(e,TAG+":getResult");
            return  null;
        }
    }
    //手机号密码登录
    public void login(String phone, String password, RestTask.ResponseCallback responseCallback){
        String jsonStr="{phone:\""+phone+"\",password:\""+MD5.getMD5(password)+"\"}";
        ServerTask.Post("phoneLogin",jsonStr,null,responseCallback);
    }
    //注册
    public void regist(User user,RestTask.ResponseCallback responseCallback){
        Gson gson=new Gson();
        String jsonStr=gson.toJson(user);
        ServerTask.Post("registUser",jsonStr,null,responseCallback);
    }
    //手机验证
    public  void phoneCheck(String phone,String code,RestTask.ResponseCallback responseCallback){
        String jsonStr="{phone:\""+phone+"\",code:\""+code+"\"}";
        ServerTask.Post("phoneCheck",jsonStr,null,responseCallback);
    }
}
