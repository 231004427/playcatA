package com.sunlin.playcat;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunlin.playcat.common.Check;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.json.BaseResult;
import com.sunlin.playcat.json.UserRESTful;
import com.sunlin.playcat.view.CommomDialog;
import com.sunlin.playcat.view.LoadingDialog;

import java.util.Timer;
import java.util.TimerTask;

public class RegistActivity extends MyActivtiy implements View.OnClickListener,RestTask.ResponseCallback {
    private String TAG="RegistActivity";

    private Button btnSendCode;
    private Button btnNext;
    private EditText phoneEdit;
    private EditText codeEdit;

    String phone;
    String code;

    //倒计时秒
    private Timer timer = new Timer();
    private int recnum = 30;

    //提交服务器
    private UserRESTful userRESTful=new UserRESTful();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化导航栏
        ToolbarBuild("注册",true,false);
        ToolbarBackListense();

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

        phone=phoneEdit.getText().toString();
        code=codeEdit.getText().toString();

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
        //提交服务器
        loadingDialog.show();
        userRESTful.phoneCheck(phone,code,this);

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
        //返回结果
        loadingDialog.dismiss();
        //处理结果
        BaseResult result= UserRESTful.getResult(response);

        if(result!=null) {
            //ShowMessage.taskShow(getApplicationContext(), result.getText());
            //打开下一步页面
            Intent intent = new Intent(this, RegistNextActivity.class);
            intent.putExtra("phone",phone);
            startActivity(intent);
        }else{
            ShowMessage.taskShow(getApplicationContext(), "服务器错误");
        }

    }

    @Override
    public void onRequestError(Exception error) {
        loadingDialog.dismiss();
        ShowMessage.taskShow(getApplicationContext(), this.getString(R.string.error_net));
        //打开下一步页面
        Intent intent = new Intent(this, RegistNextActivity.class);
        intent.putExtra("phone",phone);
        startActivity(intent);
    }
}
