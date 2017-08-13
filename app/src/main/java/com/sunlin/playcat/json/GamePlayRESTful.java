package com.sunlin.playcat.json;

import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.GamePlayList;
import com.sunlin.playcat.domain.User;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/29.
 */

public class GamePlayRESTful extends ObjRESTful  {
    private String TAG="GamePlayRESTful";
    public GamePlayRESTful(BaseRequest _baseRequest) {
        super(_baseRequest);
    }
    public GamePlayRESTful(User user){
        super(user);
    }
    public void search(GamePlayList gameList, RestTask.ResponseCallback responseCallback)
    {
        String dataStr=gson.toJson(gameList);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.GAME_PLAY_SEARCH);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
}
