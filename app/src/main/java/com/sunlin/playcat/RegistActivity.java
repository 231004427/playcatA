package com.sunlin.playcat;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sunlin.playcat.Common.Check;

import java.util.Timer;
import java.util.TimerTask;

public class RegistActivity extends MyActivtiy implements View.OnClickListener {

    private Toolbar toolbar;

    private Button btnSendCode;
    private Button btnNext;
    private EditText phoneEdit;
    private EditText codeEdit;
    //倒计时秒
    private Timer timer = new Timer();
    private int recnum = 30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化导航栏
        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.mipmap.back22);
        toolbar.setTitle("");
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("注册");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



        //绑定对象
        btnSendCode=(Button) findViewById(R.id.btnSendCode);
        btnNext=(Button) findViewById(R.id.btnNext);
        phoneEdit=(EditText)findViewById(R.id.phoneEdit);
        codeEdit=(EditText)findViewById(R.id.codeEdit);

        btnSendCode.setOnClickListener(this);
        btnNext.setOnClickListener(this);


    }
    //发送数据
    private void sendServer(){

        String phone=phoneEdit.getText().toString();
        String code=codeEdit.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            Toast.makeText(getApplicationContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(code))
        {
            Toast.makeText(getApplicationContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    //发送验证码
    private void sendPhoneCode()
    {
        if(!Check.isMobile(phoneEdit.getText().toString())) {
            Toast.makeText(getApplicationContext(), "请输入有效手机号码", Toast.LENGTH_SHORT).show();
        }else {
            btnSendCode.setEnabled(false);
            timer.schedule(taskSend, 1000, 1000);
        }
    }
    TimerTask taskSend = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {      // UI thread
                @Override
                public void run() {
                    recnum--;
                    btnSendCode.setText("重新发送("+recnum+")s");
                    if(recnum <= 0){
                        timer.cancel();
                        btnSendCode.setText("重新发送");
                        btnSendCode.setEnabled(true);
                    }
                }
            });
        }
    };
    @Override
    protected int getLayoutResId() {
        //onCreate的方法中不需要写setContentView()
        return R.layout.activity_regist;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendCode:
                sendPhoneCode();
                break;
            case R.id.btnNext:
                sendServer();
                break;
        }
    }
}
