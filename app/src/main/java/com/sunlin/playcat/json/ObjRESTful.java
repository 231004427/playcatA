package com.sunlin.playcat.json;

import android.content.Context;

import com.google.gson.Gson;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.SharedData;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.User;

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
        baseRequest.setToken(_user.getToken());
        baseRequest.setVesion(_user.getVersion());
        baseRequest.setAppid(CValues.APP_ID);
        gson=new Gson();
    }
}
