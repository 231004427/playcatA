package com.sunlin.playcat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunlin.playcat.common.SharedData;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.view.LoadingDialog;
import com.sunlin.playcat.view.SelectCityDialog;

public class WelcomeActivity extends MyActivtiyBase {
    private String TAG="WelcomeActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ImageView logo=(ImageView) findViewById(R.id.imageView);

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

        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_welcome;
    }

    /**
     * 跳转到...
     */
    private void redirectTo(){

        SelectCityDialog selectCityDialog=new SelectCityDialog(WelcomeActivity.this);
        selectCityDialog.show();
        /*
        //自动登入
        User user= SharedData.getUser(this);
        if(user.getId()==0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else{
            //保存全局用户信息
            MyApp app = (MyApp) getApplicationContext();
            app.setUser(user);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();*/
    }
}
