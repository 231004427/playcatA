package com.sunlin.playcat.json;

import com.google.gson.Gson;
import com.sunlin.playcat.common.MD5;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.PCode;
import com.sunlin.playcat.domain.User;

import java.util.Date;

/**
 * Created by sunlin on 2017/6/29.
 */
public class UserRESTful extends ObjRESTful {
    private static final String TAG="UserRESTful";

    public UserRESTful(BaseRequest _baseRequest) {
        super(_baseRequest);
    }
    public UserRESTful(User user){
        super(user);
    }

    public void updateWeixin(User user,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(user);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.USER_UPDATE_WEIXIN);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    public void updatePhone2(User user,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(user);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.USER_UPDATE_PHONE2);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }

    public void updateQQ(User user,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(user);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.USER_UPDATE_QQ);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }

    //获取用户数据
    public void get(User user,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(user);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.USER_GET);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    //发送手机验证码
    public void sendCode(String phone,RestTask.ResponseCallback responseCallback){

        PCode pCode=new PCode();
        pCode.setPhone(phone);

        String dataStr=gson.toJson(pCode);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.SEND_CODE);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    //登录
    public void login(User user, RestTask.ResponseCallback responseCallback){
        //String jsonStr="{phone:\""+phone+"\",password:\""+MD5.getMD5(password)+"\"}";
        String dataStr=gson.toJson(user);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.LOGIN);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    //注册
    public void regist(User user,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(user);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.REGIST);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    //手机验证
    public  void phoneCheck(String phone,String code,RestTask.ResponseCallback responseCallback){

        PCode pCode=new PCode();
        pCode.setPhone(phone);
        pCode.setCode(code);
        String dataStr=gson.toJson(pCode);

        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.PHONE_CHECK);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
}
