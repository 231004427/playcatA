package com.sunlin.playcat.json;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.domain.BaseResult;

/**
 * Created by sunlin on 2017/7/13.
 */

public class BaseRESTful {

    //处理返回结果
    @Nullable
    public static BaseResult getResult(String json){
        Gson gson = new Gson();
        BaseResult result = gson.fromJson(json, BaseResult.class);
        return result;
    }
}
