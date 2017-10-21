package com.sunlin.playcat;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

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

public class SetSexActivity extends MyActivtiyToolBar implements RadioGroup.OnCheckedChangeListener,RestTask.ResponseCallback  {
    private String TAG="SetSexActivity";
    private RadioGroup sexGroup;
    private int sex = 1;
    private User user;
    private UserRESTful userRESTful;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sexGroup = (RadioGroup) findViewById(R.id.rgSex);
        user=((MyApp)getApplication()).getUser();

        ToolbarBuild("性别",true,true);
        ToolbarBackListense();
        toolSet.setImageResource(R.drawable.save22);
        ToolbarSetListense(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User requestData=new User();
                requestData.setId(user.getId());
                requestData.setSex(sex);
                userRESTful.updateSex(requestData,SetSexActivity.this);
                loadingDialog.show(getSupportFragmentManager(),"loading");
            }
        });

        sexGroup.check((user.getSex()==1)?R.id.rbBoy:R.id.rbGirl);
        sexGroup.setOnCheckedChangeListener(this);

        userRESTful=new UserRESTful(user);
    }
    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        // 点击事件获取的选择对象
        switch (checkedId){
            case R.id.rbGirl:
                sex=2;
                break;
            case R.id.rbBoy:
                sex=1;
                break;
        }
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_sex;
    }
    @Override
    public void onRequestSuccess(String response) {
        try{
            //处理结果
            Gson gson=new Gson();
            BaseResult result= RESTfulHelp.getResult(response);
            if(result!=null) {

                if(result.getErrcode()<=0&&result.getType()== ActionType.USER_UPDATE_SEX)
                {
                    User data=gson.fromJson(result.getData(),User.class);
                    user.setSex(data.getSex());

                    Intent intent=new Intent();
                    intent.putExtra("result",(data.getSex()==1)?"男":"女");
                    setResult(CValues.SET_SEX,intent);
                    finish();
                }
                if(result.getErrcode()>0)
                {
                    ShowMessage.taskShow(getApplicationContext(), result.getErrmsg());
                }

            }else{
                ShowMessage.taskShow(SetSexActivity.this,getString(R.string.error_server));
            }

        }catch (Exception e){
            LogC.write(e,TAG);
            ShowMessage.taskShow(SetSexActivity.this,getString(R.string.error_server));
        }finally {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onRequestError(Exception error) {
        LogC.write(error,TAG);
        ShowMessage.taskShow(SetSexActivity.this,getString(R.string.error_server));
        loadingDialog.dismiss();
    }
}
