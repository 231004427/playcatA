package com.sunlin.playcat.json;

import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.Friend;
import com.sunlin.playcat.domain.FriendList;
import com.sunlin.playcat.domain.User;

/**
 * Created by sunlin on 2017/8/19.
 */

public class FriendRESTful extends ObjRESTful {
    public static String TAG="FriendRESTful";
    public FriendRESTful(BaseRequest _baseRequest) {
        super(_baseRequest);
    }

    public FriendRESTful(User _user) {
        super(_user);
    }
    public void search(FriendList objList, RestTask.ResponseCallback responseCallback)
    {
        String dataStr=gson.toJson(objList);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.FRIEND_SEARCH);
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    public void insert(Friend obj,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(obj);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.FRIEND_ADD);
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    public void noRead(Friend obj, RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(obj);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.FRIEND_NO_READ);
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    public void messageList(FriendList objList, RestTask.ResponseCallback responseCallback)
    {
        String dataStr=gson.toJson(objList);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.FRIEND_MESSAGE);
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    public void setAllRead(Friend obj, RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(obj);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.FRIEND_SET_ALL_READ);
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
}
