package com.sunlin.playcat.json;

import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.BaseResult;

/**
 * Created by sunlin on 2017/7/13.
 */

public class RESTfulHelp {
    private static String TAG="RESTfulHelp";

    //处理返回结果
    @Nullable
    public static BaseResult getResult(String json){
        Gson gson = new Gson();
        BaseResult result = gson.fromJson(json, BaseResult.class);
        return result;
    }
    public static boolean simpleWork(Context context,String response,
                                     int actionType,String successStr,String errorStr)
    {
        //处理结果
        try {
            BaseResult result = RESTfulHelp.getResult(response);
            if (result != null) {
                if (result.getErrcode() <= 0 && result.getType() == actionType) {
                    ShowMessage.taskShow(context, successStr);
                    return true;
                }
                if (result.getErrcode() > 0 && result.getType() == actionType) {
                    ShowMessage.taskShow(context, result.getErrmsg());
                }
            } else {
                ShowMessage.taskShow(context, errorStr);
            }
        }catch (Exception e)
        {
            LogC.write(e,TAG);
            ShowMessage.taskShow(context,errorStr);
            return  false;
        }
        return  false;
    }
}
