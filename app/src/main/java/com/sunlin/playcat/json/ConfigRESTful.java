package com.sunlin.playcat.json;

import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.Config;
import com.sunlin.playcat.domain.ConfigList;
import com.sunlin.playcat.domain.User;

/**
 * Created by sunlin on 2017/9/13.
 */

public class ConfigRESTful extends ObjRESTful {

    private static final String TAG="ConfigRESTful";
    public ConfigRESTful(BaseRequest _baseRequest) {
        super(_baseRequest);
    }
    public ConfigRESTful(User _user) {
        super(_user);
    }
    public void getList(ConfigList configList, RestTask.ResponseCallback responseCallback)
    {
        String dataStr=gson.toJson(configList);
        requestPost(ActionType.CONFIG_GET,dataStr,responseCallback);
    }
}
