package com.sunlin.playcat;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sunlin.playcat.common.Check;
import com.sunlin.playcat.common.NameValuePair;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.RestUtil;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.service.UserRESTful;
import com.sunlin.playcat.view.CommomDialog;
import com.sunlin.playcat.view.LoadingDialog;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RegistActivity extends MyActivtiy implements View.OnClickListener,
        RestTask.ProgressCallback,RestTask.ResponseCallback {

    private Toolbar toolbar;

    private Button btnSendCode;
    private Button btnNext;
    private EditText phoneEdit;
    private EditText codeEdit;
    //倒计时秒
    private Timer timer = new Timer();
    private int recnum = 30;

    private UserRESTful userRESTful=new UserRESTful();
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
        //关闭软键盘
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        //产生背景变暗效果
        new LoadingDialog(this, R.style.dialog).show();

            //提交服务器
        /*
        userRESTful.setProgressCallback(this);
        userRESTful.setResponseCallback(this);
        userRESTful.phoneCheck(phone,code);*/

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
    @Override
    public void onRequestSuccess(String response) {
        ShowMessage.taskShow(getApplicationContext(),response);
    }

    @Override
    public void onRequestError(Exception error) {
        ShowMessage.taskShow(getApplicationContext(), this.getString(R.string.error_net));
    }

    @Override
    public void onProgressUpdate(int progress) {

    }
}
