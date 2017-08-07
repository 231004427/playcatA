package com.sunlin.playcat.json;

import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.GoodsList;

import java.util.Date;

/**
 * Created by sunlin on 2017/8/6.
 */

public class GoodsRESTful extends ObjRESTful {
    private static final String TAG="GoodsRESTful";
    public GoodsRESTful(BaseRequest _baseRequest) {
        super(_baseRequest);
    }
    public void search(GoodsList gameList, RestTask.ResponseCallback responseCallback)
    {
        String dataStr=gson.toJson(gameList);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.GOODS_SEARCH);
        baseRequest.setDateTime(new Date());
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }
}
