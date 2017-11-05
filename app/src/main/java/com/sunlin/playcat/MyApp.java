package com.sunlin.playcat;

import android.app.Application;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.sunlin.playcat.MLM.MLMClient;
import com.sunlin.playcat.MLM.MLMTCPClient;
import com.sunlin.playcat.MLM.MLMSocketDelegate;
import com.sunlin.playcat.MLM.MLMType;
import com.sunlin.playcat.MLM.MLMUDPClient;
import com.sunlin.playcat.MLM.MLMUser;
import com.sunlin.playcat.MLM.MyData;
import com.sunlin.playcat.MLM.MyHead;
import com.sunlin.playcat.domain.Message;
import com.sunlin.playcat.domain.User;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sunlin on 2017/8/9.
 */

public class MyApp extends Application implements MLMSocketDelegate {
    private String TAG="MyAPP";
    //Socket服务
    public MLMTCPClient mlmClient;
    private Handler serverHandler;

    private static MyApp instance = null;
    public static MyApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        mlmClient.close(false);
    }
    private User user;
    public int versionCode;
    public String versionName;
    public int update_code;
    public String update_name;
    public int update_type;
    public String update_url;
    public User getUser() {
        return user;
    }

    public void setUser(User _user) {
        this.user = _user;

    }

    //初始化消息服务
    public void startMLMServer(){
        //TCP链接
        if( mlmClient==null) {
            mlmClient =new MLMTCPClient(user.getId(),user.getToken());
            mlmClient.delegate=this;
            mlmClient.Run();
        }else{
            mlmClient.Repeat();
        }
    }
    //服务事件绑定
    public void setServerHandler(Handler serverHandler) {
        this.serverHandler = serverHandler;
    }
    @Override
    public void MLMSocketResultError(int action, int errorNum, String data) {
        //Log.e(TAG,"action:"+action+" errorNum:"+errorNum+" data:"+data);
        if(serverHandler!=null) {
            MyData myData = new MyData();
            MyHead myHead = new MyHead();
            myHead.setT(action);
            myHead.setD(errorNum);
            myData.setData(data.getBytes());
            myData.setMyHead(myHead);
            android.os.Message message = new android.os.Message();
            message.obj = myData;
            serverHandler.sendMessage(message);
        }
    }
    @Override
    public void MLMGetMessage(MyData myData) {
        if(serverHandler!=null) {
            android.os.Message message = new android.os.Message();
            message.obj = myData;
            serverHandler.sendMessage(message);
        }
    }
    @Override
    public void MLMShowMessage(Message message) {
    }
}
