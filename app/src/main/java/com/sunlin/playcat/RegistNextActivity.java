package com.sunlin.playcat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soundcloud.android.crop.Crop;
import com.sunlin.playcat.MyActivtiy;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.ImageHelp;
import com.sunlin.playcat.view.BottomPopView;
import com.sunlin.playcat.view.CircleImageView;
import com.sunlin.playcat.view.RoundImgeView;

import java.io.File;
import java.io.IOException;

public class RegistNextActivity extends MyActivtiy implements RadioGroup.OnCheckedChangeListener {
    private String TAG="RegistNextActivity";
    private Toolbar toolbar;
    private CircleImageView imgHead;
    private RadioGroup sexGroup;
    private RadioButton sexSelect;
    private String headImgUrl="";
    private int sex=1;

    private RelativeLayout relativeLayoutImg;
    private BottomPopView bottomPopView;
    private static String paiImgUrl;
    private Uri headUri;
    Bitmap photoBmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgHead=(CircleImageView)findViewById(R.id.imgHead);
        sexGroup=(RadioGroup) findViewById(R.id.rgSex);
        relativeLayoutImg=(RelativeLayout)findViewById(R.id.relativeLayoutImg);

        //事件绑定
         sexGroup.setOnCheckedChangeListener(this);
        relativeLayoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开图片上传菜单
                showMenuItem(v);
            }
        });

        //初始化导航栏
        ToolbarBuild("注册",true,false);
        ToolbarBackListense();

        //设置默认头像
        //Bitmap defaultHead = BitmapFactory.decodeResource(getResources(), R.mipmap.boy45);

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
                if(headImgUrl.isEmpty()){
                    imgHead.setImageResource(R.mipmap.girl45);
                }
                break;
            case R.id.rbBoy:
                sex=1;
                if(headImgUrl.isEmpty()){
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
                photoBmp = ImageHelp.getBitmapFormUri(RegistNextActivity.this, headUri);
                imgHead.setImageBitmap(photoBmp);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
