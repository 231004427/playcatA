package com.sunlin.playcat.json;

import com.google.gson.Gson;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Game;
import com.sunlin.playcat.domain.GameList;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/16.
 */

public class GameRESTful extends ObjRESTful {
    private static final String TAG="GameRESTful";
    public GameRESTful(BaseRequest _baseRequest) {
        super(_baseRequest);
    }

    public void search(GameList gameList, RestTask.ResponseCallback responseCallback)
    {
        String dataStr=gson.toJson(gameList);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.GAME_SEARCH);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
    public void get(int id, RestTask.ResponseCallback responseCallback)
    {
        Game game= new Game();
        game.setId(id);
        String dataStr=gson.toJson(game);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.GAME_GET);
        baseRequest.setDateTime(new Date());

        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
}
