package com.sunlin.playcat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sunlin.playcat.common.Check;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.json.RESTfulHelp;
import com.sunlin.playcat.json.UserRESTful;

import java.util.Timer;
import java.util.TimerTask;

public class RegistActivity extends MyActivtiyToolBar implements View.OnClickListener,RestTask.ResponseCallback {
    private String TAG="RegistActivity";

    private Button btnSendCode;
    private Button btnNext;
    private EditText phoneEdit;
    private EditText codeEdit;

    String phone;
    String code;

    //倒计时秒
    private Timer timer;
    private int recnum = 60;

    //提交服务器
    private UserRESTful userRESTful;

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

        userRESTful=new UserRESTful(myApp.getUser());

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
        loadingDialog.show(getSupportFragmentManager(),"loading");
        userRESTful.phoneCheck(phone,code,this);

    }
    //发送验证码
    private void sendPhoneCode()
    {
        if(!Check.isMobile(phoneEdit.getText().toString())) {
            Toast.makeText(getApplicationContext(), "请输入有效手机号码", Toast.LENGTH_SHORT).show();
        }else {
            btnSendCode.setEnabled(false);
            phone=phoneEdit.getText().toString();
            //提交服务器
            loadingDialog.show(getSupportFragmentManager(),"loading");
            userRESTful.sendCode(phone,this);


        }
    }
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
        try {
            //返回结果
            loadingDialog.dismiss();
            //处理结果
            BaseResult result= RESTfulHelp.getResult(response);
            if(result!=null) {
                //手机验证码认证成功
                if(result.getErrcode()<=0 && result.getType()== ActionType.PHONE_CHECK) {
                    Intent intent = new Intent(this, RegistNextActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                    return;
                }
                //手机发送验证码成功
                if(result.getErrcode()<=0&&result.getType()==ActionType.SEND_CODE){
                    timer= new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {      // UI thread
                                @Override
                                public void run() {
                                    recnum--;
                                    btnSendCode.setText("重新发送("+recnum+")s");
                                    if(recnum <= 0){
                                        timer.cancel();
                                        recnum=60;
                                        btnSendCode.setText("重新发送");
                                        btnSendCode.setEnabled(true);
                                    }
                                }
                            });
                        }
                    }, 1000, 1000);
                    ShowMessage.taskShow(getApplicationContext(), result.getText());
                }
                if(result.getErrcode()>0&&result.getType()==ActionType.PHONE_CHECK){
                    ShowMessage.taskShow(getApplicationContext(), result.getErrmsg());
                }
                //手机验证码发送失败
                if(result.getErrcode()>0&& result.getType()==ActionType.SEND_CODE){
                    btnSendCode.setEnabled(true);
                    ShowMessage.taskShow(getApplicationContext(), result.getErrmsg());
                }
            }else{
                ShowMessage.taskShow(RegistActivity.this,getString(R.string.error_server));
            }
        }catch (Exception e){
            LogC.write(e,TAG);
            ShowMessage.taskShow(RegistActivity.this,getString(R.string.error_server));
        }
    }

    @Override
    public void onRequestError(Exception error) {
        btnSendCode.setEnabled(true);
        loadingDialog.dismiss();
        ShowMessage.taskShow(getApplicationContext(), this.getString(R.string.error_net));
    }
}
