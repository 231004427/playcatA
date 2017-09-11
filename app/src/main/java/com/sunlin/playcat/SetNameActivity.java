package com.sunlin.playcat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.RESTfulHelp;
import com.sunlin.playcat.json.UserRESTful;

public class SetNameActivity extends MyActivtiyToolBar implements RestTask.ResponseCallback {
    private String TAG="SetNameActivity";
    private EditText valueEdit;
    private User user;
    private UserRESTful userRESTful;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        valueEdit=(EditText)findViewById(R.id.valueEdit);

        user=((MyApp)getApplication()).getUser();
        ToolbarBuild("用户名",true,true);
        ToolbarBackListense();
        toolSet.setImageResource(R.drawable.save22);
        ToolbarSetListense(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result=valueEdit.getText().toString().trim();
                if(result.isEmpty()){
                    ShowMessage.taskShow(SetNameActivity.this,"不能为空");
                    return;
                }
                User requestData=new User();
                requestData.setId(user.getId());
                requestData.setName(result);
                userRESTful.updateName(requestData,SetNameActivity.this);
                loadingDialog.show();

            }
        });
        userRESTful=new UserRESTful(user);
        valueEdit.setText(user.getName());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_name;
    }

    @Override
    public void onRequestSuccess(String response) {
        try{
            //处理结果
            Gson gson=new Gson();
            BaseResult result= RESTfulHelp.getResult(response);
            if(result!=null) {

                if(result.getErrcode()<=0&&result.getType()== ActionType.USER_UPDATE_NAME)
                {
                    User data=gson.fromJson(result.getData(),User.class);
                    user.setName(data.getName());
                    Intent intent=new Intent();
                    intent.putExtra("result",data.getName());
                    setResult(CValues.SET_NAME,intent);
                    finish();
                }
                if(result.getErrcode()>0)
                {
                    ShowMessage.taskShow(getApplicationContext(), result.getErrmsg());
                }

            }else{
                ShowMessage.taskShow(SetNameActivity.this,getString(R.string.error_server));
            }

        }catch (Exception e){
            LogC.write(e,TAG);
            ShowMessage.taskShow(SetNameActivity.this,getString(R.string.error_server));
        }finally {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onRequestError(Exception error) {
        LogC.write(error,TAG);
        ShowMessage.taskShow(SetNameActivity.this,getString(R.string.error_server));
        loadingDialog.dismiss();
    }
}
