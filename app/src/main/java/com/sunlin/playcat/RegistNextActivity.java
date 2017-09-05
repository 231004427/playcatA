package com.sunlin.playcat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageHelp;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.MD5;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.domain.Local;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.json.RESTfulHelp;
import com.sunlin.playcat.json.CityRESTful;
import com.sunlin.playcat.json.UserRESTful;
import com.sunlin.playcat.view.BottomPopView;
import com.sunlin.playcat.view.CircleImageView;
import com.sunlin.playcat.view.LoadingDialog;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class RegistNextActivity extends MyActivtiyToolBar implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, RestTask.ResponseCallback {
    private String TAG = "RegistNextActivity";
    private Toolbar toolbar;
    private CircleImageView imgHead;
    private RadioGroup sexGroup;
    private RadioButton sexSelect;
    private String headImgUrl = "";
    private String phone;
    private int sex = 1;
    private EditText nameEdit;
    private EditText passEdit;
    private Button btnNext;
    private TextView cityText;

    private RelativeLayout relativeLayoutImg;
    private BottomPopView bottomPopView;
    private static String paiImgUrl;
    private Uri headUri;
    Bitmap photoBmp = null;

    private User user;
    private Local userLocal=new Local();
    private boolean isLocal=false;

    private LocationManager locationManager;
    LoadingDialog loadingDialog;
    private UserRESTful userRESTful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgHead = (CircleImageView) findViewById(R.id.imgHead);
        sexGroup = (RadioGroup) findViewById(R.id.rgSex);
        relativeLayoutImg = (RelativeLayout) findViewById(R.id.relativeLayoutImg);
        nameEdit = (EditText) findViewById(R.id.nameEdit);
        passEdit = (EditText) findViewById(R.id.passEdit);
        btnNext = (Button) findViewById(R.id.btnNext);
        cityText=(TextView) findViewById(R.id.cityText);


        userRESTful=new UserRESTful(user);

        //初始化导航栏
        ToolbarBuild("注册", true, false);
        ToolbarBackListense();
        //获取参数
        phone = this.getIntent().getStringExtra("phone");
        if(phone=="")
        {
            ShowMessage.taskShow(this,getString(R.string.error_param));
            return;
        }

        //事件绑定
        sexGroup.setOnCheckedChangeListener(this);
        relativeLayoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开图片上传菜单
                showMenuItem(v);
            }
        });
        btnNext.setOnClickListener(this);

        //设置默认头像
        //Bitmap defaultHead = BitmapFactory.decodeResource(getResources(), CValues.mipmap.boy45);

        //获取地址位置
        userLocal.setUpdateTime(new Date());
        boolean getCity= CityRESTful.GetCityJson(userLocal,this,new RestTask.ResponseCallback() {
            @Override
            public void onRequestSuccess(String response) {
                if(CityRESTful.GetCityFromJson(userLocal,response)){
                    isLocal=true;
                    cityText.setText(userLocal.getCity());
                }else{
                    cityText.setText("位置：定位失败");
                }
            }
            @Override
            public void onRequestError(Exception error) {
                cityText.setText("位置：定位失败");
            }
        });
        if(getCity){
            cityText.setText("位置：定位中...");
        }else{
            cityText.setText("位置：定位失败");
        }
    }

    public void showMenuItem(View parent)
    {
        bottomPopView = new BottomPopView(this, parent) {
            @Override
            public void onTopButtonClick() {
                //拍照
                paiImgUrl= ImageHelp.PaiImg("temp_head.jpg",RegistNextActivity.this);
            }
            @Override
            public void onBottomButtonClick() {
                //本地图片
                Crop.pickImage(RegistNextActivity.this);
            }
        };
        bottomPopView.setTopText("拍照");
        bottomPopView.setBottomText("选择图片");
        // 显示底部菜单
        bottomPopView.show();
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_regist_next;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        // 点击事件获取的选择对象
        sexSelect = (RadioButton) sexGroup
                .findViewById(checkedId);

        switch (checkedId){
            case R.id.rbGirl:
                sex=2;
                if(photoBmp==null){
                    imgHead.setImageResource(R.mipmap.girl45);
                }
                break;
            case R.id.rbBoy:
                sex=1;
                if(photoBmp==null){
                    imgHead.setImageResource(R.mipmap.boy45);
                }
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
    if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
        beginCrop(result.getData());
    }else  if(requestCode==ImageHelp.PAI_BACK){
        beginCrop(Uri.fromFile(new File(paiImgUrl)));
    }
    else if (requestCode == Crop.REQUEST_CROP) {
        handleCrop(resultCode, result);
    }
}
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(RegistNextActivity.this);
    }
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            bottomPopView.dismiss();
            headUri=Crop.getOutput(result);
            imgHead.setImageBitmap(null);
            try {
                photoBmp = ImageHelp.getBitmapFormUri(RegistNextActivity.this, headUri,150,150);;
                imgHead.setImageBitmap(photoBmp);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    private void submit(){

        String nameStr=nameEdit.getText().toString();
        String passStr=passEdit.getText().toString();
        if(TextUtils.isEmpty(nameStr))
        {
            Toast.makeText(getApplicationContext(), "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(passStr))
        {
            Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        }
        String passMD5="";
        try {
            passMD5=MD5.getMD5(passStr);
        } catch (Exception e) {
            LogC.write(e,TAG+":submit");
            Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        //显示加载框
        loadingDialog=new LoadingDialog(this);
        loadingDialog.show();
        //提交服务器
        Date date=new Date();
        user=new User();
        user.setCity(userLocal.getCity());
        user.setLocal(userLocal);
        user.setCount(0);
        user.setCreate(date);
        user.setGold(0);
        user.setLevel(1);
        user.setName(nameStr);
        user.setPassword(passMD5);
        user.setPhone(phone);
        //自定义头像
        if(photoBmp!=null){
            user.setPhoto(ImageHelp.Bitmap2StrByBase64(photoBmp));
        }else{
            user.setPhoto("");
        }
        user.setSex(sex);
        user.setStatus(1);
        user.setUpdate(date);
        user.setZhuan(0);
        user.setId(-1);
        user.setVersion(CValues.getLocalVersionCode(this));
        user.setImei(((TelephonyManager) getSystemService(TELEPHONY_SERVICE)).getDeviceId());
        userRESTful.regist(user,this);
    }
    @Override
    public void onRequestSuccess(String response) {
        try {
        //返回结果
        loadingDialog.dismiss();
        //处理结果
        BaseResult result= RESTfulHelp.getResult(response);
        if(result!=null) {

            if(result.getErrcode()<=0&&result.getType()== ActionType.REGIST)
            {
                //打开下一步页面
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("name",user.getName());
                startActivity(intent);
            }else
            {
                ShowMessage.taskShow(getApplicationContext(), result.getErrmsg());
            }

        }else{
            ShowMessage.taskShow(RegistNextActivity.this,getString(R.string.error_server));
        }
        }catch (Exception e){
            LogC.write(e,TAG);
            ShowMessage.taskShow(RegistNextActivity.this,getString(R.string.error_server));
        }
    }

    @Override
    public void onRequestError(Exception error) {
        //返回结果
        loadingDialog.dismiss();
        ShowMessage.taskShow(this, getString(R.string.error_net));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnNext:
                //
                submit();
                break;
        }
    }
}
