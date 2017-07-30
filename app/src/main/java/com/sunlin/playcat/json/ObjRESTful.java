package com.sunlin.playcat.json;

import com.google.gson.Gson;
import com.sunlin.playcat.domain.BaseRequest;

/**
 * Created by sunlin on 2017/7/27.
 */

public class ObjRESTful {
    public Gson gson;
    public BaseRequest baseRequest;

    public ObjRESTful(BaseRequest _baseRequest)
    {
        baseRequest=_baseRequest;
        baseRequest.setVesion(1);
        gson=new Gson();
    }
}
