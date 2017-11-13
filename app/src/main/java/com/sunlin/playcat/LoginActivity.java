package com.sunlin.playcat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunlin.playcat.MLM.MLMType;
import com.sunlin.playcat.MLM.MLMUser;
import com.sunlin.playcat.MLM.MyData;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.MD5;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.SharedData;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Message;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.RESTfulHelp;
import com.sunlin.playcat.json.UserRESTful;
import com.sunlin.playcat.view.LoadingDialog;

import java.nio.charset.Charset;
import java.util.ArrayList;

public class LoginActivity extends ActivityAll implements View.OnClickListener,RestTask.ResponseCallback {
    private String TAG="LoginActivity";
    private TextView registBtn,forget;
    private Button btnLogin;
    private EditText phoneEdit;
    private EditText passEdit;

    private String phoneStr;
    private String passStr;

    //提交服务器
    LoadingDialog loadingDialog;
    private UserRESTful userRESTful;
    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myApp=(MyApp) getApplication();
        //绑定服务器事件
        myApp.setServerHandler(socketHandler);

        registBtn=(TextView)findViewById(R.id.regist);
        forget=(TextView)findViewById(R.id.forget);
        btnLogin=(Button)findViewById(R.id.btnLogin);
        phoneEdit=(EditText)findViewById(R.id.phoneEdit);
        passEdit=(EditText)findViewById(R.id.passEdit);

        //请求信息
        userRESTful=new UserRESTful(myApp.getUser());

        registBtn.setClickable(true);
        registBtn.setFocusable(true);

        //事件绑定
        registBtn.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
        forget.setOnClickListener(this);

        //消息绑定
        myApp.setServerHandler(socketHandler);
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
        loadingDialog=new LoadingDialog();
        loadingDialog.show(getSupportFragmentManager(),"loading");

        User user=new User();
        user.setImei(((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId());
        user.setPhone(phoneStr);
        user.setPassword(MD5.getMD5(passStr));

        userRESTful.login(user,this);
    }
    private void regist()
    {
        //打开注册页面
        Intent intent = new Intent(this, RegistActivity.class);
        startActivity(intent);
    }
    private void forget(){
        Intent intent = new Intent(this, ForgetPassActivity.class);
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
                forget();
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
    private Handler socketHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            MyData myData=(MyData)msg.obj;
            int type=myData.getMyHead().getT();
            int dNum=myData.getMyHead().getD();
            if(type== MLMType.ACTION_USER_REGIST){
                if(dNum==MLMType.ACTION_ACCESS){
                    //请求加入对方会话
                    //server.inRoom(friend.getId());
                    //进入首页
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }else{
                    ShowMessage.taskShow(LoginActivity.this,"服务异常");
                }
            }
            if(type==MLMType.ACTION_SYS_BACK){
                if(loadingDialog.isShow) {
                    loadingDialog.dismiss();
                }
                //服务器错误
                ShowMessage.taskShow(LoginActivity.this,"服务无响应");
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onRequestSuccess(String response) {
        try {
            //处理结果
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    .create();
            BaseResult result = gson.fromJson(response,BaseResult.class);
            if(result!=null){
                if (result.getErrcode() <= 0 && result.getType() == ActionType.LOGIN) {

                    User user=gson.fromJson(result.getData(),User.class);
                    //保存登入状态
                    SharedData.saveUser(user,LoginActivity.this);
                    //全局保存
                    myApp.setUser(user);
                    //消息服务启动
                    myApp.startMLMServer();

                }
                if(result.getErrcode()>0&&result.getType()==ActionType.LOGIN){
                    loadingDialog.dismiss();
                    ShowMessage.taskShow(LoginActivity.this, result.getErrmsg());
                }
            }else{
                loadingDialog.dismiss();
                ShowMessage.taskShow(LoginActivity.this,getString(R.string.error_server));
            }
        }catch (Exception e){
            loadingDialog.dismiss();
            LogC.write(e,TAG);
            ShowMessage.taskShow(LoginActivity.this,getString(R.string.error_server));
        }
    }

    @Override
    public void onRequestError(Exception error) {
        loadingDialog.dismiss();
        ShowMessage.taskShow(LoginActivity.this,getString(R.string.error_net));
    }
    //返回键
    @Override
    public void onBackPressed() {
        AtyContainer.getInstance().finishAllActivity();
        super.onBackPressed();
    }
}

