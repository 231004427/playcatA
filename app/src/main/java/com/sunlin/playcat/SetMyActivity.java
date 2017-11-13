package com.sunlin.playcat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.soundcloud.android.crop.Crop;
import com.sunlin.playcat.MyActivtiyToolBar;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageHelp;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.SharedData;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.Area;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.RESTfulHelp;
import com.sunlin.playcat.json.UserRESTful;
import com.sunlin.playcat.view.BottomPopView;
import com.sunlin.playcat.view.CircleImageView;
import com.sunlin.playcat.view.SelectCityDialog;

import java.io.File;
import java.io.IOException;

public class SetMyActivity extends MyActivtiyToolBar implements SelectCityDialog.OnResultListener,RestTask.ResponseCallback {
    private String TAG="SetMyActivity";

    private User user;
    private Handler mHandler = new Handler();

    private CircleImageView imgHead;
    private TextView nameText;
    private TextView phoneText;
    private TextView sexText;
    private TextView cityText;

    private LinearLayout btnHeadImg;
    private LinearLayout btnName;
    private LinearLayout btnSex;
    private LinearLayout btnCity;
    private LinearLayout btnPass;
    private Button btnOut;

    private Area[] selectAreas;

    private BottomPopView bottomPopView;
    private static String paiImgUrl;
    private Uri headUri;
    Bitmap photoBmp = null;

    private UserRESTful userRESTful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarBuild("个人资料",true,false);
        ToolbarBackListense();

        imgHead=(CircleImageView)findViewById(R.id.imgHead);
        nameText=(TextView)findViewById(R.id.nameText);
        phoneText=(TextView)findViewById(R.id.phoneText);
        sexText=(TextView)findViewById(R.id.sexText);
        cityText=(TextView)findViewById(R.id.cityText);
        btnHeadImg=(LinearLayout)findViewById(R.id.btnHeadImg);
        btnName=(LinearLayout)findViewById(R.id.btnName);
        btnSex=(LinearLayout)findViewById(R.id.btnSex);
        btnCity=(LinearLayout)findViewById(R.id.btnCity);
        btnOut=(Button)findViewById(R.id.btnOut);
        btnPass=(LinearLayout)findViewById(R.id.btnPass);

        //获取用户信息
        user=((MyApp)getApplication()).getUser();
        //服务接口
        userRESTful=new UserRESTful(user);
        //绑定数据
        nameText.setText(user.getName());
        sexText.setText(user.getSex()==1?"男":"女");
        if(user.getPhoto()!=null&&!user.getPhoto().isEmpty()){
            ImageWorker.loadImage(imgHead, CValues.SERVER_IMG+user.getPhoto(),mHandler);
        }else{
            imgHead.setImageResource(user.getSex()==1?R.mipmap.boy45:R.mipmap.girl45);
        }
        cityText.setText(String.valueOf(user.getCity()));

        //修改图片
        btnHeadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMenuItem(v);
            }
        });
        //修改用户名
        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SetMyActivity.this,SetNameActivity.class);
                startActivityForResult(intent,CValues.SET);
            }
        });
        //修改性别
        btnSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(SetMyActivity.this,SetSexActivity.class);
                startActivityForResult(intent,CValues.SET);
            }
        });
        //修改城市
        btnCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCityDialog selectCityDialog=new SelectCityDialog();
                selectCityDialog.setLevel(2);
                selectCityDialog.setOnResultListener(SetMyActivity.this);
                selectCityDialog.show(getSupportFragmentManager(),"CityDialog");
            }
        });
        //退出登录
        btnOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedData.removeUser(SetMyActivity.this);
                Intent intent = new Intent(SetMyActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        //修改密码
        btnPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SetMyActivity.this, SetPassActivity.class);
                startActivity(intent);
            }
        });

    }
    public void showMenuItem(View parent)
    {
        bottomPopView = new BottomPopView(this, parent) {
            @Override
            public void onTopButtonClick() {
                //拍照
                paiImgUrl= ImageHelp.PaiImg("temp_head.jpg",SetMyActivity.this);
            }
            @Override
            public void onBottomButtonClick() {
                //本地图片
                Crop.pickImage(SetMyActivity.this);
            }
        };
        bottomPopView.setTopText("拍照");
        bottomPopView.setBottomText("选择图片");
        // 显示底部菜单
        bottomPopView.show();
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
        if(resultCode==CValues.SET_NAME){
            nameText.setText(result.getExtras().getString("result"));
        }
        if(resultCode==CValues.SET_SEX){
            sexText.setText(result.getExtras().getString("result"));
        }
    }
    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).asSquare().start(SetMyActivity.this);
    }
    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {

            bottomPopView.dismiss();
            headUri=Crop.getOutput(result);
            imgHead.setImageBitmap(null);
            try {
                photoBmp = ImageHelp.getBitmapFormUri(SetMyActivity.this, headUri,150,150);;
                imgHead.setImageBitmap(photoBmp);

                //保存图片
                User requestData=new User();
                requestData.setId(user.getId());
                requestData.setPhoto(ImageHelp.Bitmap2StrByBase64(photoBmp));
                userRESTful.updatePhoto(requestData,this);
                loadingDialog.show(getSupportFragmentManager(),"loading");


            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            //Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_my;
    }
    //设置地区选择框
    @Override
    public void onCityResult(Area[] _selectAreas) {
        selectAreas=_selectAreas;
        String cityStr=selectAreas[1].getName()+" "+selectAreas[2].getName();
        cityText.setText(cityStr);

        //保存图片
        User requestData=new User();
        requestData.setId(user.getId());
        requestData.setCity(cityStr);
        userRESTful.updateCity(requestData,this);
        loadingDialog.show(getSupportFragmentManager(),"loading");
    }
    @Override
    public void onRequestSuccess(String response) {
        try{
            //处理结果
            BaseResult result= RESTfulHelp.getResult(response);
            if(result!=null) {

                if(result.getErrcode()<=0&&result.getType()== ActionType.USER_UPDATE_PHOTO)
                {
                    //全局修改头像
                    User data=gson.fromJson(result.getData(),User.class);
                    user.setPhoto(data.getPhoto());

                }
                if(result.getErrcode()>0)
                {
                    ShowMessage.taskShow(getApplicationContext(), result.getErrmsg());
                }else{
                    ShowMessage.taskShow(getApplicationContext(), result.getText());
                }

            }else{
                ShowMessage.taskShow(SetMyActivity.this,getString(R.string.error_server));
            }

        }catch (Exception e){
            LogC.write(e,TAG);
            ShowMessage.taskShow(SetMyActivity.this,getString(R.string.error_server));
        }finally {
            loadingDialog.dismiss();
        }
    }

    @Override
    public void onRequestError(Exception error) {
        LogC.write(error,TAG);
        ShowMessage.taskShow(SetMyActivity.this,getString(R.string.error_server));
        loadingDialog.dismiss();
    }
}
