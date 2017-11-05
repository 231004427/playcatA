package com.sunlin.playcat.json;

import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.Message;
import com.sunlin.playcat.domain.MessageList;
import com.sunlin.playcat.domain.User;

/**
 * Created by sunlin on 2017/8/26.
 */

public class MessageRESTful extends ObjRESTful {
    public static String TAG="MessageRESTful";
    public MessageRESTful(User _user) {
        super(_user);
    }
    public void search(MessageList objList, RestTask.ResponseCallback responseCallback)
    {
        String dataStr=gson.toJson(objList);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.MESSAGE_SEARCH);
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    public void add(Message obj, RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(obj);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.MESSAGE_ADD);
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    public void updateStatus(Message obj, RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(obj);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.MESSAGE_UPDATE_STATUS);
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    public void addFriend(Message obj, RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(obj);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.MESSAGE_ADD_FRIEND);
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
}
