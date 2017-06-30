package com.sunlin.playcat.common;

import android.util.Base64;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by sunlin on 2017/6/28.
 */
public class RestUtil {
    public static RestTask obtainGetTask(String url)
            throws MalformedURLException,IOException {
        HttpURLConnection connection=
                (HttpURLConnection)(new URL(url))
                        .openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setDoInput(true);
        RestTask task=new RestTask(connection);
        return task;
    }

    public static RestTask obtainAuthenticatedGetTask(String url,
                                                      String username,String password) throws
            MalformedURLException,IOException{
        HttpURLConnection connection=(HttpURLConnection)(new URL(url))
                .openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setDoInput(true);
        attachBasicAuthentication(connection,username,password);
        RestTask task=new RestTask(connection);
        return task;
    }
    public static RestTask obtainFormPostTaskJson(String url,String json)
            throws MalformedURLException,IOException{
        HttpURLConnection connection=
                (HttpURLConnection)(new URL(url))
                        .openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setDoOutput(true);//默认为post
        //connection.setRequestMethod()；其他请求动作

        RestTask task=new RestTask(connection);
        task.setmContentType("application/json");
        task.setmFormBody(json);

        return task;
    }
    public static RestTask obtainFormPostTask(String url,
                                              List<NameValuePair> formData)
            throws MalformedURLException,IOException{
        HttpURLConnection connection=
                (HttpURLConnection)(new URL(url))
                        .openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setDoOutput(true);//默认为post
        //connection.setRequestMethod()；其他请求动作

        RestTask task=new RestTask(connection);
        task.setmFormBody(formData);

        return task;
    }

    public static RestTask obtainAuthenticatedFormPostTask(String url,
                                                           List<NameValuePair> formData,
                                                           String username,
                                                           String password)
            throws MalformedURLException,IOException{
        HttpURLConnection connection=
                (HttpURLConnection)(new URL(url))
                        .openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        connection.setDoOutput(true);//默认为post

        attachBasicAuthentication(connection,username,password);

        RestTask task=new RestTask(connection);
        task.setmFormBody(formData);

        return task;
    }

    public static RestTask obtainMultiPartPostTask(
            String url, List<NameValuePair> formPart,
            File file, String fileName)
            throws MalformedURLException,IOException{
        HttpURLConnection connection=
                (HttpURLConnection)(new URL(url))
                        .openConnection();
        connection.setReadTimeout(10000);
        connection.setConnectTimeout(15000);
        //connection.setChunkedStreamingMode();块上传机制
        connection.setDoOutput(true);

        RestTask task=new RestTask(connection);
        task.setmFormBody(formPart);
        task.setmUploadFile(file,fileName);
        return task;
    }

    private static void attachBasicAuthentication(HttpURLConnection connection, String username, String password) {
        //添加基本授权头
        String userpassword=username+":"+password;
        String encodeAuthorization=
                Base64.encodeToString(userpassword.getBytes(),Base64.NO_WRAP);
        connection.setRequestProperty("Authorization","Basic "+
                encodeAuthorization);
    }
}

