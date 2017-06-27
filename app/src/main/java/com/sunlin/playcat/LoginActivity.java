package com.sunlin.playcat;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.io.Console;

public class LoginActivity extends MyActivtiy implements View.OnClickListener {
    private String logName="LoginActivity";
    private TextView registBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registBtn=(TextView)findViewById(R.id.regist);
        registBtn.setClickable(true);
        registBtn.setFocusable(true);
        //事件绑定
        registBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Log.d(logName,""+v.getId());
        switch (v.getId()){
            case R.id.regist:
                //打开注册页面
                Intent intent = new Intent(this, RegistActivity.class);
                startActivity(intent);
                break;
            case R.id.forget:
                break;
            case R.id.phonelogin:
                break;
            case R.id.qqlogin:
                break;
            case R.id.weibologin:
                break;
            case R.id.weixinlogin:
                break;
        }
    }
    @Override
    protected int getLayoutResId() {
        //onCreate的方法中不需要写setContentView()
        return R.layout.activity_login;
    }
}
