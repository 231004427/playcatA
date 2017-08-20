package com.sunlin.playcat.json;

import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ServerTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.AreaList;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.User;

import java.util.Date;

/**
 * Created by sunlin on 2017/8/13.
 */

public class AreaRESTful extends ObjRESTful  {
    public static String TAG="AreaRESTful";
    public AreaRESTful(BaseRequest _baseRequest) {
        super(_baseRequest);
    }
    public AreaRESTful(User _user) {
        super(_user);
    }
    public void search(AreaList objList, RestTask.ResponseCallback responseCallback)
    {
        String dataStr=gson.toJson(objList);
        baseRequest.setData(dataStr);
        baseRequest.setActionType(ActionType.AREA_SEARCH);
        ServerTask.Post(gson.toJson(baseRequest),null,responseCallback);
    }

}
