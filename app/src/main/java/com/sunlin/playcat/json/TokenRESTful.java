package com.sunlin.playcat.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.Token;
import com.sunlin.playcat.domain.User;

/**
 * Created by sunlin on 2017/10/26.
 */

public class TokenRESTful extends ObjRESTful {

    public TokenRESTful(User _user) {
        super(_user);
    }

    public void get(Token token,RestTask.ResponseCallback responseCallback){
        String dataStr=gson.toJson(token);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.TOKEN_BUILD);
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
}
