package com.sunlin.playcat.json;

import com.google.gson.Gson;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.Collect;
import com.sunlin.playcat.domain.CollectList;
import com.sunlin.playcat.domain.User;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/27.
 */

public class CollectRESTful extends ObjRESTful{
    public static String TAG="CollectRESTful";

    public CollectRESTful(BaseRequest _baseRequest) {
        super(_baseRequest);
    }
    public CollectRESTful(User user){
        super(user);
    }

    public void searchGame(CollectList collectList, RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(collectList);
        super.requestPost(ActionType.COLLECT_SARCH_GAME,dataStr,responseCallback);
    }
    //获取收藏信息
    public void get(Collect collect,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(collect);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.COLLECT_GET);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    //删除收藏
    public void del(Collect collect,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(collect);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.COLLECT_DEL);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    //添加收藏
    public void add(Collect collect,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(collect);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.COLLECT_ADD);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
}
