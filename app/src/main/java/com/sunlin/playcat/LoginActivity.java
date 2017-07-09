package com.sunlin.playcat;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.json.UserRESTful;
import com.sunlin.playcat.view.LoadingDialog;

import java.io.Console;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private String TAG="LoginActivity";
    private TextView registBtn;
    private Button btnLogin;
    private EditText phoneEdit;
    private EditText passEdit;

    private String phoneStr;
    private String passStr;

    //提交服务器
    LoadingDialog loadingDialog;
    private UserRESTful userRESTful=new UserRESTful();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registBtn=(TextView)findViewById(R.id.regist);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        phoneEdit=(EditText)findViewById(R.id.phoneEdit);
        passEdit=(EditText)findViewById(R.id.passEdit);


        registBtn.setClickable(true);
        registBtn.setFocusable(true);

        //事件绑定
        registBtn.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }
    public void loginServer()
    {
        //判断数据有效性
        phoneStr=phoneEdit.getText().toString();
        passStr=passEdit.getText().toString();

        if(phoneStr.isEmpty()){
            ShowMessage.taskShow(this,getString(R.string.tip_phone));
            return;
        }
        if(passStr.isEmpty()){
            ShowMessage.taskShow(this,getString(R.string.tip_pass));
            return;
        }

        //提交服务器
        loadingDialog=new LoadingDialog(this,R.style.dialog);
        loadingDialog.show();
        userRESTful.login(phoneStr, passStr, new RestTask.ResponseCallback() {
            @Override
            public void onRequestSuccess(String response) {
                loadingDialog.dismiss();
                //进入首页
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }

            @Override
            public void onRequestError(Exception error) {
                loadingDialog.dismiss();
                ShowMessage.taskShow(LoginActivity.this,getString(R.string.error_net));
                //进入首页
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    public void regist()
    {
        //打开注册页面
        Intent intent = new Intent(this, RegistActivity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        Log.d(TAG,""+v.getId());
        switch (v.getId()){
            case R.id.regist:
                regist();
                break;
            case R.id.forget:
                break;
            case R.id.codelogin:
                break;
            case R.id.btnLogin:
                loginServer();
                break;
            case R.id.qqlogin:
                break;
            case R.id.weibologin:
                break;
            case R.id.weixinlogin:
                break;
        }
    }
}
