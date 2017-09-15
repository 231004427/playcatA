package com.sunlin.playcat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.common.AppHelp;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.SharedData;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Config;
import com.sunlin.playcat.domain.ConfigList;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.ConfigRESTful;
import com.sunlin.playcat.view.LoadingDialog;
import com.sunlin.playcat.view.SelectCityDialog;
import com.sunlin.playcat.view.UpdateDialog;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends MyActivtiyBase implements RestTask.ResponseCallback,UpdateDialog.OnClickOkListener {
    private String TAG="WelcomeActivity";
    private ConfigRESTful configRESTful;
    private UpdateDialog updateDialog;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user= SharedData.getUser(this);
        myApp.setUser(user);
        myApp.versionCode=AppHelp.getAppVersionCode(this);
        myApp.versionName=AppHelp.getAppVersionName(this);

        //版本检查
        configRESTful=new ConfigRESTful(user);
        ConfigList config=new ConfigList();
        config.setType(1);
        configRESTful.getList(config,this);



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
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_welcome;
    }

    /**
     * 跳转到...
     */
    private void redirectTo(){

        Timer timer = new Timer();
        TimerTask tast = new TimerTask() {
            @Override
            public void run() {
                //自动登入
                if(user.getId()==0) {
                    Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }
        };
        timer.schedule(tast, 1000);
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
                    Log.e(TAG,"code:"+myApp.update_code+"|"+myApp.versionCode);
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
            }
        }
        redirectTo();
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
}
