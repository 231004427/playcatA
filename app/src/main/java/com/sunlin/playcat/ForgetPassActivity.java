package com.sunlin.playcat;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sunlin.playcat.common.Check;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.MD5;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.RESTfulHelp;
import com.sunlin.playcat.json.UserRESTful;

import java.util.Timer;
import java.util.TimerTask;

public class ForgetPassActivity extends MyActivtiyToolBar implements View.OnClickListener,RestTask.ResponseCallback {
    private String TAG="ForgetPassActivity";

    private Button btnSendCode;
    private Button btnSubmit;
    private EditText phoneEdit;
    private EditText codeEdit;
    private EditText passEdit;

    String phone;
    String code;
    String pass;

    //倒计时秒
    private Timer timer;
    private int recnum = 60;

    //提交服务器
    private UserRESTful userRESTful;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarBuild("忘记密码",true,false);
        ToolbarBackListense();

        //绑定对象
        btnSendCode=(Button) findViewById(R.id.btnSendCode);
        btnSubmit=(Button) findViewById(R.id.btnSubmit);
        phoneEdit=(EditText)findViewById(R.id.phoneEdit);
        codeEdit=(EditText)findViewById(R.id.codeEdit);
        passEdit=(EditText)findViewById(R.id.passEdit);

        userRESTful=new UserRESTful(new BaseRequest());

        btnSendCode.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
    }
    //发送数据
    private void sendServer(){

        phone=phoneEdit.getText().toString();
        code=codeEdit.getText().toString();
        pass=passEdit.getText().toString();

        if(TextUtils.isEmpty(phone))
        {
            ShowMessage.taskShow(this, "请输入手机号码");
            return;
        }
        if(TextUtils.isEmpty(code))
        {
            ShowMessage.taskShow(this, "请输入验证码");
            return;
        }
        if(TextUtils.isEmpty(pass))
        {
            ShowMessage.taskShow(this, "请输入新密码");
            return;
        }
        if(pass.length()<6){
            ShowMessage.taskShow(this, "密码至少需要6位");
            return;
        }

        //关闭软键盘
        InputMethodManager imm =  (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
        //提交服务器
        loadingDialog.show(getSupportFragmentManager(),"loading");
        User  userData=new User();
        userData.setPhone(phone);
        userData.setPhone2(code);
        userData.setPassword(MD5.getMD5(pass));
        userRESTful.updatePassPhone(userData,this);

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
        return R.layout.activity_forget_pass;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSendCode:
                sendPhoneCode();
                break;
            case R.id.btnSubmit:
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
                //密码修改成功
                if(result.getErrcode()<=0 && result.getType()== ActionType.USER_UPDATE_PASS_PHONE) {
                    ShowMessage.taskShow(this, result.getText());
                    //设置成功
                    Intent intent = new Intent(this, LoginActivity.class);
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
                    ShowMessage.taskShow(this, result.getText());
                }
                //验证码错误
                if(result.getErrcode()>0){
                    //手机验证码发送失败
                    if(result.getType()==ActionType.SEND_CODE){
                        btnSendCode.setEnabled(true);
                        ShowMessage.taskShow(this, result.getErrmsg());
                    }else {
                        ShowMessage.taskShow(this, result.getErrmsg());
                    }
                }


            }else{
                ShowMessage.taskShow(this,getString(R.string.error_server));
            }
        }catch (Exception e){
            LogC.write(e,TAG);
            ShowMessage.taskShow(this,getString(R.string.error_server));
        }
    }

    @Override
    public void onRequestError(Exception error) {
        btnSendCode.setEnabled(true);
        loadingDialog.dismiss();
        ShowMessage.taskShow(getApplicationContext(), this.getString(R.string.error_net));
    }
}
