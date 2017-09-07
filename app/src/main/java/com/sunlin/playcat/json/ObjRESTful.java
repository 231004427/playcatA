package com.sunlin.playcat.json;

import android.content.Context;

import com.google.gson.Gson;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.common.SharedData;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.User;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/27.
 */

public class ObjRESTful {
    public Gson gson;
    public BaseRequest baseRequest;


    public ObjRESTful(BaseRequest _baseRequest)
    {
        baseRequest=_baseRequest;
        baseRequest.setVesion(1);
        gson=new Gson();
    }
    public ObjRESTful(User _user){
        baseRequest=new BaseRequest();
        baseRequest.setUserid(_user.getId());
        baseRequest.setDateTime(new Date());
        baseRequest.setToken(_user.getToken());
        baseRequest.setVesion(_user.getVersion());
        baseRequest.setAppid(CValues.APP_ID);
        gson=new Gson();
    }
    public void requestPost(int type,String dataStr,RestTask.ResponseCallback responseCallback){
        baseRequest.setData(dataStr);
        baseRequest.setActionType(type);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
}
