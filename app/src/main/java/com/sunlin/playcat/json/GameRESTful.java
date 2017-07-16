package com.sunlin.playcat.json;

import com.google.gson.Gson;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.domain.Game;
import com.sunlin.playcat.domain.GameList;

/**
 * Created by sunlin on 2017/7/16.
 */

public class GameRESTful {
    private static final String TAG="GameRESTful";
    Gson gson=new Gson();
    public void search(GameList gameList, RestTask.ResponseCallback responseCallback)
    {
        String jsonStr=gson.toJson(gameList);
        ServerTask.Post("gameList",jsonStr,null,responseCallback);
    }
}
