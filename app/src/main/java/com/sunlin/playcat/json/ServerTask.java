package com.sunlin.playcat.json;

import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.RestUtil;

import java.io.IOException;

/**
 * Created by sunlin on 2017/7/6.
 */

public class ServerTask {
    private static String TAG="ServerTask";
    private static final String POST_URL="http://10.1.1.224:8080/api/post/playcat/{action}/{version}";
    private static final String GET_URL="http://10.1.1.224:8080/api/playcat/{action}/{version}";

    public static void  Post(String apiName,
                       String jsonStr,
                       RestTask.ProgressCallback progressCallback,
                       RestTask.ResponseCallback responseCallback){
        String url=POST_URL.replace("{action}",apiName).replace("{version}","1");
        try{
            //List<NameValuePair> parameters=new ArrayList<NameValuePair>();
            //parameters.add(new NameValuePair("title","Android Recipes"));
            //parameters.add(new NameValuePair("summary","Learn Android Quickly"));
        RestTask postTask=
                RestUtil.obtainFormPostTaskJson(url,jsonStr);
        postTask.setResponseCallback(responseCallback);
        postTask.setProgressCallback(progressCallback);
        postTask.execute();
        }catch (IOException e){
            LogC.write(e,TAG+":Post");
        }
    }
    //
    public static void Get(String url,
                           RestTask.ProgressCallback progressCallback,
                           RestTask.ResponseCallback responseCallback){
        try{
            RestTask postTask=
                    RestUtil.obtainGetTask(url);
            postTask.setResponseCallback(responseCallback);
            postTask.setProgressCallback(progressCallback);
            postTask.execute();
        }catch(IOException e){
            LogC.write(e,TAG+":Post");
        }
    }
}
