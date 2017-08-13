package com.sunlin.playcat.json;

import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.Address;
import com.sunlin.playcat.domain.AddressList;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.User;

import java.util.Date;

/**
 * Created by sunlin on 2017/8/9.
 */

public class AddressRESTful extends ObjRESTful {
    public static String TAG="AddressRESTful";
    public AddressRESTful(BaseRequest _baseRequest) {
        super(_baseRequest);
    }
    public AddressRESTful(User user){
        super(user);
    }
    //添加
    public void add(Address address,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(address);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.ADDRESS_ADD);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    //删除
    public void del(Address address, RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(address);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.ADDRESS_DEL);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    //获取
    public void get(Address address,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(address);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.ADDRESS_GET);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    //搜索
    public void search(AddressList addressList,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(addressList);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.ADDRESS_SEARCH);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    //更新
    public void update(Address address,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(address);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.ADDRESS_UPDATE);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }

}
