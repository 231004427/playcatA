package com.sunlin.playcat;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunlin.playcat.MLM.MLMSocketDelegate;
import com.sunlin.playcat.MLM.MLMType;
import com.sunlin.playcat.MLM.MyData;
import com.sunlin.playcat.common.Check;
import com.sunlin.playcat.common.GsonHelp;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.domain.Comment;
import com.sunlin.playcat.domain.Message;
import com.sunlin.playcat.domain.MessageType;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.MessageRESTful;
import com.sunlin.playcat.view.AddFriendDialog;
import com.sunlin.playcat.view.LoadingDialog;

import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by sunlin on 2017/10/9.
 */

public class ActivityObj extends ActivityAll {
    private String TAG="ActivityObj";
    public MyApp myApp;
    public Gson gson;
    private MessageRESTful messageBaseRESTful;
    //提交服务器
    public LoadingDialog loadingDialog;
    private AddFriendDialog addFriendDialog;
    private MLMSocketDelegate mlmSocketUdpDelegate;

    public void setMlmSocketUdpDelegate(MLMSocketDelegate _mlmSocketDelegate){
        mlmSocketUdpDelegate =_mlmSocketDelegate;
    }
    @Override
    protected void onStart() {
        super.onStart();
        myApp.setServerHandler(socketHandler);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!isCurrentRunningForeground) {
            myApp.setServerHandler(null);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //用户信息初始化
        myApp = (MyApp)this.getApplicationContext();
        messageBaseRESTful=new MessageRESTful(myApp.getUser());
        gson= GsonHelp.getGsonObj();
        //开启消息服务

        loadingDialog=new LoadingDialog();
        loadingDialog.setTitle(ActivityObj.this.getString(R.string.error_server));
        loadingDialog.setIsCancel(true);
        loadingDialog.setBtnStr("重试");
        loadingDialog.setOnClickListener(new LoadingDialog.OnClickListener(){
            @Override
            public void onClick(int type) {
                if(type==2)
                {
                    //消息服务启动
                    myApp.startMLMServer();
                }
                if(type==1)
                {
                    AtyContainer.getInstance().finishAllActivity();
                    //AtyContainer.getInstance().removeActivity(ActivityObj.this);

                }
            }
        });
        //默认竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    private Handler socketHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            MyData myData=(MyData)msg.obj;
            int type=myData.getMyHead().getT();
            int dNum=myData.getMyHead().getD();
            if(type==MLMType.ACTION_SYS_BACK){
                //服务器错误
                if(getSupportFragmentManager()!=null){

                    loadingDialog.setTitle(ActivityObj.this.getString(R.string.error_server)+"["+dNum+"]");
                    loadingDialog.show(getSupportFragmentManager(),"repeat");

                    if(mlmSocketUdpDelegate!=null) {
                        mlmSocketUdpDelegate.MLMSocketResultError(type, dNum, new String(myData.getData(), Charset.forName("utf-8")));
                    }
                }
            }else{
                //用户注册处理
                if(type== MLMType.ACTION_USER_REGIST ||type== MLMType.ERROR_SYS_NOREGIST){
                    if(dNum==MLMType.ACTION_ACCESS){
                        //请求加入对方会话
                        //server.inRoom(friend.getId());
                        //进入首页
                        loadingDialog.dismiss();
                    }else{
                        if(getSupportFragmentManager()!=null) {

                            loadingDialog.show(getSupportFragmentManager(), "repeat");
                        }
                    }
                }
                if(type==MLMType.ACTION_SEND_SINGLE||type==MLMType.ACTION_SEND_ROOM){
                    if(dNum==0){
                        String dataStr = new String(myData.getData(), Charset.forName("utf-8"));
                        //转换消息
                        try {
                            Message message = gson.fromJson(dataStr, Message.class);

                            //普通文本消息
                            if(message.getType()==MessageType.TEXT) {

                                if (mlmSocketUdpDelegate != null) {
                                    mlmSocketUdpDelegate.MLMShowMessage(message);
                                }
                            }
                            //邀请好友
                            if(message.getType()== MessageType.ADD_FRIEND){
                                String[] dataFrom=message.getData().split("\\|");

                                User friendUser=new User();
                                friendUser.setId(Integer.parseInt(dataFrom[0]));
                                friendUser.setName(dataFrom[1]);
                                friendUser.setPhoto(dataFrom[2]);
                                friendUser.setSex(Integer.parseInt(dataFrom[3]));

                                addFriendDialog=new AddFriendDialog();
                                addFriendDialog.setFriendUser(friendUser);
                                addFriendDialog.setTitleStr("邀请好友");
                                addFriendDialog.setType(4);
                                addFriendDialog.show(getSupportFragmentManager(),"AddFriendDialog");
                            }
                            //置成已读
                            message.setStatus(4);
                            messageBaseRESTful.updateStatus(message, new RestTask.ResponseCallback() {
                                @Override
                                public void onRequestSuccess(String response) {
                                }
                                @Override
                                public void onRequestError(Exception error) {
                                }
                            });

                        }catch (Exception e){
                            LogC.write(e, TAG);
                        }
                    }
                }
                if(mlmSocketUdpDelegate!=null) {
                    mlmSocketUdpDelegate.MLMGetMessage(myData);
                }
            }
            super.handleMessage(msg);
        }
    };
}
