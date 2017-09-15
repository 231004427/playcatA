package com.sunlin.playcat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.MyActivtiyToolBar;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.UserRESTful;

public class SetQPActivity extends MyActivtiyToolBar implements RestTask.ResponseCallback {
    private String TAG="SetQPActivity";
    private TextView valueText;
    private EditText valueEdit;
    private MyApp myApp;
    private UserRESTful userRESTful;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        valueText=(TextView)findViewById(R.id.valueText);
        valueEdit=(EditText)findViewById(R.id.valueEdit);
        //获取参数
        type=getIntent().getIntExtra("type",-1);
        String title=(type==3)?"充值QQ":"充值手机";

        //初始化导航栏
        ToolbarBuild(title,true,true);
        ToolbarBackListense();
        toolSet.setImageResource(R.drawable.save22);

        myApp=((MyApp)getApplication());
        ToolbarSetListense(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                String valueStr=valueEdit.getText().toString();
                if(valueStr.isEmpty()){
                    ShowMessage.taskShow(SetQPActivity.this,"请输入内容");
                    return;
                }
                //更新QQ号
                if(type==3){

                    myApp.getUser().setQq(valueStr);
                    userRESTful=new UserRESTful(myApp.getUser());
                    userRESTful.updateQQ(myApp.getUser(),SetQPActivity.this);
                }
                //更新手机号
                if(type==4){
                    myApp.getUser().setPhone2(valueStr);
                    userRESTful=new UserRESTful(myApp.getUser());
                    userRESTful.updatePhone2(myApp.getUser(),SetQPActivity.this);
                }
            }
        });

        valueEdit.setEms(11);
        switch (type){
            case 3:
                valueText.setText("QQ");
                valueEdit.setHint(R.string.tip_qq);
                if(myApp.getUser().getQq()!=null){
                    valueEdit.setText(myApp.getUser().getQq());
                }
                break;
            case 4:
                valueText.setText("手机号");
                valueEdit.setHint(R.string.tip_phone);
                if(myApp.getUser().getPhone2()!=null){
                    valueEdit.setText(myApp.getUser().getPhone2());
                }
                break;
        }
        //绑定请求

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_qp;
    }

    @Override
    public void onRequestSuccess(String response) {
        try{
            loadingDialog.dismiss();
            //处理结果
            Gson gson=new Gson();
            BaseResult result = gson.fromJson(response,BaseResult.class);
            if(result!=null){

                if (result.getErrcode() <= 0) {
                    finish();
                }
                if(result.getErrcode()>0){
                    ShowMessage.taskShow(SetQPActivity.this, result.getErrmsg());
                }
            }else{
                ShowMessage.taskShow(SetQPActivity.this,getString(R.string.error_server));
            }

        }catch (Exception e){
            LogC.write(e,TAG);
            ShowMessage.taskShow(SetQPActivity.this,getString(R.string.error_server));
        }
    }
    @Override
    public void onRequestError(Exception error) {
        loadingDialog.dismiss();
        ShowMessage.taskShow(SetQPActivity.this,getString(R.string.error_net));
    }
}
