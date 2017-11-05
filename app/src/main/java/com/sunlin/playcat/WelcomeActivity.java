package com.sunlin.playcat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.sunlin.playcat.MLM.MLMType;
import com.sunlin.playcat.MLM.MyData;
import com.sunlin.playcat.common.AppHelp;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.common.SharedData;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Config;
import com.sunlin.playcat.domain.ConfigList;
import com.sunlin.playcat.domain.Token;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.ConfigRESTful;
import com.sunlin.playcat.json.TokenRESTful;
import com.sunlin.playcat.view.LoadingDialog;
import com.sunlin.playcat.view.UpdateDialog;

public class WelcomeActivity extends ActivityAll implements RestTask.ResponseCallback,UpdateDialog.OnClickOkListener {
    private String TAG="WelcomeActivity";
    private ConfigRESTful configRESTful;
    private UpdateDialog updateDialog;
    private TokenRESTful tokenRESTful;
    private LoadingDialog loadingDialog;
    private User user;
    private MyApp myApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        //透明状态栏
        LinearLayout statusLayout=(LinearLayout)findViewById(R.id.statusLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if(statusLayout!=null) {
                statusLayout.getLayoutParams().height = ScreenUtil.getStatusHeight(this);
            }
        }else{
            if(statusLayout!=null){
                statusLayout.setVisibility(View.GONE);
            }
        }

        myApp=(MyApp) getApplication();
        user= SharedData.getUser(this);
        myApp.setUser(user);
        myApp.versionCode=AppHelp.getAppVersionCode(this);
        myApp.versionName=AppHelp.getAppVersionName(this);

        //TOKEn
        tokenRESTful=new TokenRESTful(user);
        //版本检查
        configRESTful=new ConfigRESTful(user);
        ConfigList config=new ConfigList();
        config.setType(1);
        configRESTful.getList(config,this);

        //初始化对话框
        loadingDialog=new LoadingDialog();
        loadingDialog.setTitle(WelcomeActivity.this.getString(R.string.error_server));
        loadingDialog.setIsCancel(true);
        loadingDialog.setBtnStr("重试");
        loadingDialog.setOnClickListener(new LoadingDialog.OnClickListener(){
            @Override
            public void onClick(int type) {
            if(type==2)
            {
                //开启消息服务
                myApp.startMLMServer();
            }
            if(type==1)
            {
                AtyContainer.getInstance().finishAllActivity();
            }
            }
        });
        /*
        loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK )
                {
                    finish();
                }
                return false;
            }
        });
        */
        /*logo=(ImageView) findViewById(R.id.imageView);
        //渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.3f,1.0f);
        aa.setDuration(2000);
        logo.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener()
        {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationStart(Animation animation) {}

        });*/
        //消息绑定
        myApp.setServerHandler(socketHandler);
    }

    /**
     * 跳转到...
     */
    private void redirectTo(){
        //自动登入
        if(user.getId()<=0) {
            Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }else{
            //获取TOKEN
            Token token=new Token();
            token.setPhone(user.getPhone());
            token.setPassword(user.getPassword());
            tokenRESTful.get(token,this);
        }
    }
    @Override
    public void onRequestSuccess(String response) {
        Gson gson=new Gson();
        BaseResult result = gson.fromJson(response,BaseResult.class);
        if(result!=null){
            if (result.getErrcode() <= 0 && result.getType() == ActionType.CONFIG_GET) {
                ConfigList configList=gson.fromJson(result.getData(),ConfigList.class);
                if(configList!=null){
                    for( int i = 0 ; i < configList.getList().size() ; i++) {
                        Config config=configList.getList().get(i);
                        if(config.getName().equals("APP_NAME")){
                            myApp.update_name=config.getValue();
                        }
                        if(config.getName().equals("APP_TYPE")){
                            myApp.update_type=Integer.parseInt(config.getValue());
                        }
                        if(config.getName().equals("APP_URL")){
                            myApp.update_url=config.getValue();
                        }
                        if(config.getName().equals("APP_CODE")){
                            myApp.update_code=Integer.parseInt(config.getValue());
                        }
                    }
                    //版本更新1，强制，2提示更新
                    //Log.e(TAG,"code:"+myApp.update_code+"|"+myApp.versionCode);
                    if(myApp.update_code>myApp.versionCode){
                        //提醒用户更新
                        if(myApp.update_type==1||myApp.update_type==2) {
                            updateDialog = new UpdateDialog();
                            updateDialog.setTitle(myApp.update_name);
                            updateDialog.setVersionCode(myApp.update_code);
                            updateDialog.setCancelable(!(myApp.update_type==1));
                            updateDialog.show(getSupportFragmentManager(), "update");
                            updateDialog.setOnClickOkListener(WelcomeActivity.this);
                            return;
                        }
                    }
                }
                redirectTo();
            }
            if(result.getErrcode()<=0&&result.getType()==ActionType.TOKEN_BUILD){
                Token token=gson.fromJson(result.getData(),Token.class);
                //开启消息服务
                myApp.getUser().setToken(token.getToken_data());
                myApp.startMLMServer();
            }
            if(result.getErrcode()>0){
                //ShowMessage.taskShow(this,getString(R.string.error_server));
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
            }
        }else{
            //ShowMessage.taskShow(this,getString(R.string.error_server));
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

    }
    @Override
    public void onRequestError(Exception error) {
        ShowMessage.taskShow(this,getString(R.string.error_server));
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    @Override
    public void onItemClick(int type) {
        if(type==1) {
            redirectTo();
        }
    }
    private Handler socketHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            MyData myData=(MyData)msg.obj;
            int type=myData.getMyHead().getT();
            int dNum=myData.getMyHead().getD();
            if(type== MLMType.ACTION_USER_REGIST ||type== MLMType.ERROR_SYS_NOREGIST){
                if(dNum==MLMType.ACTION_ACCESS){
                    //请求加入对方会话
                    //server.inRoom(friend.getId());
                    //进入首页
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    loadingDialog.show(getSupportFragmentManager(),"repeat");
                }
            }
            if(type==MLMType.ACTION_SYS_BACK){
                //服务器错误
                loadingDialog.show(getSupportFragmentManager(),"repeat");
            }
            super.handleMessage(msg);
        }
    };
}
