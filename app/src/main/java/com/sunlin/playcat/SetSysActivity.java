package com.sunlin.playcat;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunlin.playcat.common.AppHelp;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.DownloadUtil;
import com.sunlin.playcat.common.FileHelp;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.SDCardListener;
import com.sunlin.playcat.common.SharedData;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.view.UpdateDialog;

import java.io.File;

public class SetSysActivity extends MyActivtiyToolBar implements View.OnClickListener{
    private LinearLayout clearLayout;
    private LinearLayout updateLayout;
    private TextView clearText;
    private UpdateDialog updateDialog;
    private int versionCode;
    private String versionName;
    private TextView vesionText,redSet;
    private ImageView btnVesion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarBuild("设置",true,false);
        ToolbarBackListense();

        clearLayout=(LinearLayout) findViewById(R.id.clearLayout);
        updateLayout=(LinearLayout)findViewById(R.id.updateLayout);
        btnVesion=(ImageView)findViewById(R.id.btnVesion);

        clearText=(TextView) findViewById(R.id.clearText);
        vesionText=(TextView)findViewById(R.id.vesionName);
        redSet=(TextView)findViewById(R.id.redSet);

        //获取缓存使用大小
        File file=new File(ImageWorker.bitmapPath);

        long size=FileHelp.getFolderSize(file);

        if(size>0){
            clearText.setText(FileHelp.getFormatSize(size));
        }else{
            clearText.setText("已使用:0KB");
        }
        //绑定清理事件
        clearLayout.setOnClickListener(this);

        //显示更新对话框
        //是否有新版本
        //版本更新1，强制，2提示更新
        if(myApp.update_code>myApp.versionCode){
            redSet.setVisibility(View.VISIBLE);
            updateDialog = new UpdateDialog();
            updateDialog.setTitle(myApp.update_name);
            updateDialog.setVersionCode(myApp.update_code);
            updateLayout.setOnClickListener(this);
            btnVesion.setVisibility(View.VISIBLE);
        }
        //当前版本
        vesionText.setText(AppHelp.getAppVersionName(this));
        //判断新版本

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_sys;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.clearLayout:
                //清理缓存
                FileHelp.deleteFolderFile(ImageWorker.bitmapPath,true);
                clearText.setText("已使用:0KB");
                ShowMessage.taskShow(this,"清理成功");
                break;
            case R.id.updateLayout:
                //判断SD可用
                if (SDCardListener.isSDCardAvailable()) {
                    //显示对话框
                    updateDialog.setCancelable(true);
                    updateDialog.show(getSupportFragmentManager(), "UpdateDialog");

                }else {
                    // 当前不可用
                    ShowMessage.taskShow(this,"请确保SD卡可用");
                    return;
                }
                break;
        }
    }
}
