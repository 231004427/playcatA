package com.sunlin.playcat;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;

import com.sunlin.playcat.MLM.MLMSocketDelegate;
import com.sunlin.playcat.MLM.MLMType;
import com.sunlin.playcat.MLM.MyData;
import com.sunlin.playcat.view.LoadingDialog;

import java.nio.charset.Charset;

/**
 * Created by sunlin on 2017/10/9.
 */

public class ActivityObj extends ActivityAll {
    public MyApp myApp;
    //提交服务器
    public LoadingDialog loadingDialog;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //用户信息初始化
        myApp = (MyApp)this.getApplicationContext();
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
                    //注册用户
                    myApp.mlmClient.Repeat();
                    myApp.registUser();
                }
                if(type==1)
                {
                    //AtyContainer.getInstance().finishAllActivity();
                    AtyContainer.getInstance().removeActivity(ActivityObj.this);

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
                if(mlmSocketUdpDelegate!=null) {
                    mlmSocketUdpDelegate.MLMGetMessage(myData);
                }
            }
            super.handleMessage(msg);
        }
    };
}
