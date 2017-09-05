package com.sunlin.playcat;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.Address;
import com.sunlin.playcat.domain.Area;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.json.AddressRESTful;
import com.sunlin.playcat.view.SelectCityDialog;

import java.util.Date;

public class SetAddressActivity extends MyActivtiyToolBar implements RestTask.ResponseCallback,SelectCityDialog.OnResultListener  {
    private String TAG="SetAddressActivity";
    private LinearLayout selectCityLayout;
    private TextView areaText;
    private EditText valueName;
    private EditText valuePhone;
    private EditText valueAddress;
    private EditText postCode;
    private AddressRESTful addressRESTful ;
    private Area[] selectAreas;
    private int addressId=-1;
    private Address address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectCityLayout=(LinearLayout)findViewById(R.id.selectCityLayout);
        areaText=(TextView)findViewById(R.id.areaText);
        valueName=(EditText)findViewById(R.id.valueName);
        valuePhone=(EditText)findViewById(R.id.valuePhone);
        valueAddress=(EditText)findViewById(R.id.valueAddress);
        postCode=(EditText)findViewById(R.id.postCode);

        //初始化导航栏
        ToolbarBuild("设置地址",true,true);
        ToolbarBackListense();
        toolSet.setImageResource(R.drawable.save22);
        toolSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        //不弹出软键盘
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //地区设置
        selectCityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCityDialog selectCityDialog=new SelectCityDialog();
                selectCityDialog.setOnResultListener(SetAddressActivity.this);
                selectCityDialog.show(getSupportFragmentManager(),"CityDialog");
            }
        });

        addressId=getIntent().getIntExtra("addressId",-1);
        //获取已有地址信息
        address=new Address();
        addressRESTful=new AddressRESTful(user);
        if(addressId>0) {
            get();
        }

        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }
    private void get(){
        loadingDialog.show();
        address.setId(addressId);
        addressRESTful.get(address,this);
    }
    private void save(){
        String nameStr=valueName.getText().toString();
        String phoneStr=valuePhone.getText().toString();
        String addressStr=valueAddress.getText().toString();
        String postCodeStr=postCode.getText().toString();

        if(nameStr.isEmpty()){
            ShowMessage.taskShow(this,"名字不能为空");
            return;
        }
        if(phoneStr.isEmpty()){
            ShowMessage.taskShow(this,"手机号不能为空");
            return;
        }
        if(selectAreas==null && addressId<0){
            ShowMessage.taskShow(this,"请选择地区");
            return;
        }
        if(addressStr.isEmpty()){
            ShowMessage.taskShow(this,"详细地址不能空");
            return;
        }
        loadingDialog.show();

        address.setUser_id(user.getId());
        address.setName(nameStr);
        address.setPhone(phoneStr);
        address.setPost_code(postCodeStr);

        if(addressId<0) {
            address.setCountry_id(selectAreas[0].getId());
            address.setProvince_id(selectAreas[1].getId());
            address.setCity_id(selectAreas[2].getId());
            address.setDistrict_id(selectAreas[3].getId());
        }

        address.setArea_name(areaText.getText().toString());
        address.setAddress(addressStr);
        address.setCreate_time(new Date());
        address.setStatus(1);
        if(addressId>0){
        addressRESTful.update(address,this);
        }else{
            addressRESTful.add(address,this);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_address;
    }

    //设置地区选择框
    @Override
    public void onCityResult(Area[] _selectAreas) {
        selectAreas=_selectAreas;
        areaText.setText(selectAreas[1].getName()+" "+selectAreas[2].getName()+" "+selectAreas[3].getName());
        areaText.setTextColor(ContextCompat.getColor(this,R.color.black));
    }
    @Override
    public void onRequestSuccess(String response) {

        try{
            Gson gson=new Gson();
            BaseResult result=gson.fromJson(response, BaseResult.class);

            if(result!=null) {

                if (result.getErrcode() <= 0 && result.getType() == ActionType.ADDRESS_GET){
                    address=gson.fromJson(result.getData(),Address.class);
                    if(address!=null) {
                        valueName.setText(address.getName());
                        valuePhone.setText(address.getPhone());
                        areaText.setText(address.getArea_name());
                        postCode.setText(address.getPost_code());
                        valueAddress.setText(address.getAddress());
                        areaText.setTextColor(ContextCompat.getColor(this,R.color.black));

                    }
                }
                if (result.getErrcode() <= 0 && result.getType() == ActionType.ADDRESS_UPDATE){

                    back(addressId);
                }
                if(result.getErrcode()<=0&& result.getType()==ActionType.ADDRESS_ADD){
                    address=gson.fromJson(result.getData(),Address.class);
                    addressId=address.getId();
                    back(addressId);
                }
                if(result.getErrcode()>0)
                {
                    //返回错误
                    ShowMessage.taskShow(this,result.getText());
                }

            }else{
                //数据错误
                ShowMessage.taskShow(this,getString(R.string.error_server));
            }

        }catch (Exception e){
            LogC.write(e,TAG);
            ShowMessage.taskShow(this,getString(R.string.error_server));}
        finally {
            loadingDialog.dismiss();
        }
    }
    private void back(int id)
    {
        String myresult=address.getName()+
                " "+address.getPhone()+
                " "+address.getArea_name()+
                " "+address.getAddress()+
                " "+address.getPost_code();
        Intent intent = new Intent();
        intent.putExtra("address", myresult);
        intent.putExtra("addressId",id);
        setResult(CValues.SET_ADDRESS, intent);
        finish();
    }
    @Override
    public void onRequestError(Exception error) {
        ShowMessage.taskShow(this,this.getString(R.string.error_net));
        loadingDialog.dismiss();
    }
}
