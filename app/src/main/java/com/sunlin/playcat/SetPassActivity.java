package com.sunlin.playcat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.MD5;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.UserRESTful;

public class SetPassActivity extends MyActivtiyToolBar implements RestTask.ResponseCallback {
    private String TAG="SetPassActivity";
    private EditText oldPass;
    private EditText newPass;
    private MyApp myApp;
    private UserRESTful userRESTful;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarBuild("修改密码",true,true);
        ToolbarBackListense();
        toolSet.setImageResource(R.drawable.save22);

        oldPass=(EditText)findViewById(R.id.oldPass);
        newPass=(EditText)findViewById(R.id.newPass);

        myApp=((MyApp)getApplication());
        userRESTful=new UserRESTful(myApp.getUser());
        ToolbarSetListense(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldStr=oldPass.getText().toString();
                String newStr=newPass.getText().toString();
                if(oldStr.isEmpty()){
                    ShowMessage.taskShow(SetPassActivity.this,"请输入原密码");
                    return;
                }
                if(newStr.isEmpty()){
                    ShowMessage.taskShow(SetPassActivity.this,"请输入新密码");
                    return;
                }
                if(newStr.length()<6){
                    ShowMessage.taskShow(SetPassActivity.this,"密码至少需要6位");
                    return;
                }
                loadingDialog.show(getSupportFragmentManager(),"loading");
                User user=new User();
                user.setId(myApp.getUser().getId());
                user.setPassword(MD5.getMD5(oldStr));
                user.setAddress(MD5.getMD5(newStr));
                userRESTful.updatePassword(user,SetPassActivity.this);

            }
        });

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_pass;
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
                    ShowMessage.taskShow(this, result.getErrmsg());
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
        loadingDialog.dismiss();
        ShowMessage.taskShow(this,getString(R.string.error_net));
    }
}
