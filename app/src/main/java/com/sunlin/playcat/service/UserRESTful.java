package com.sunlin.playcat.service;

import android.os.AsyncTask;
import android.widget.Toast;

import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.RestUtil;

/**
 * Created by sunlin on 2017/6/29.
 */
public class UserRESTful {

    private static final String TAG="UserRESTful";
    private static final String POST_URL="http://10.1.1.224:8080/api/post/playcat/{action}/{version}";
    private static final String GET_URL="http://10.1.1.224:8080/api/playcat/{action}/{version}";

    RestTask.ProgressCallback progressCallback;
    RestTask.ResponseCallback responseCallback;

    public  void setResponseCallback(RestTask.ResponseCallback _responseCallback){
        responseCallback=_responseCallback;
    }
    public  void setProgressCallback(RestTask.ProgressCallback _progressCallback){
        progressCallback=_progressCallback;
    }

    //手机验证
    public  boolean phoneCheck(String phone,String code){

        try{
            //List<NameValuePair> parameters=new ArrayList<NameValuePair>();
            //parameters.add(new NameValuePair("title","Android Recipes"));
            //parameters.add(new NameValuePair("summary","Learn Android Quickly"));
            //
            String jsonStr="{phone:\""+phone+"\",code:\""+code+"\"}";
            String url=POST_URL.replace("{action}","phoneCheck").replace("{version}","1");

            //http://10.1.1.224:8080/api/post/playcat/userInfo/1?id=1
            RestTask postTask=
                    RestUtil.obtainFormPostTaskJson(url,jsonStr);
            postTask.setResponseCallback(responseCallback);
            postTask.setProgressCallback(progressCallback);
            postTask.execute();
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
