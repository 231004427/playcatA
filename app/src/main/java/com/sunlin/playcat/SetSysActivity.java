package com.sunlin.playcat;

import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
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

public class SetSysActivity extends MyActivtiyToolBar implements View.OnClickListener,DownloadUtil.OnDownloadListener{
    private LinearLayout clearLayout;
    private LinearLayout updateLayout;
    private TextView clearText;
    private UpdateDialog updateDialog;
    private int versionCodeNew;
    private TextView vesionName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarBuild("设置",true,false);
        ToolbarBackListense();

        clearLayout=(LinearLayout) findViewById(R.id.clearLayout);
        updateLayout=(LinearLayout)findViewById(R.id.updateLayout);

        clearText=(TextView) findViewById(R.id.clearText);
        vesionName=(TextView)findViewById(R.id.vesionName);

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
        updateLayout.setOnClickListener(this);

        //下载对话框
        versionCodeNew=2;
        updateDialog=new UpdateDialog();
        updateDialog.setCancelable(false);

        //当前版本
        vesionName.setText(AppHelp.getAppVersionName(this));

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
                    updateDialog.show(getSupportFragmentManager(), "UpdateDialog");
                    //下载文件
                    DownloadUtil.get().download(CValues.UPDATE_URL,CValues.DOWN_PATH, this);

                }else {
                    // 当前不可用
                    ShowMessage.taskShow(this,"请确保SD卡可用");
                    return;
                }
                break;
        }
    }
    Handler mHandlerLoad = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    //ShowMessage.taskShow(SetSysActivity.this,"下载完成");
                    updateDialog.dismiss();
                    //下载完成，保存版本信息
                    SharedData.saveDownVesion(SetSysActivity.this,versionCodeNew);
                    //打开安装程序
                    File file=new File(Environment.getExternalStorageDirectory() + CValues.DOWN_PATH_APP);
                    if(file.exists()) {
                        FileHelp.openAPK(file, SetSysActivity.this);
                    }
                    break;
                case 1:
                    updateDialog.setProgress((int)msg.obj);
                    break;
                case 2:
                    ShowMessage.taskShow(SetSysActivity.this,"网络异常,下载失败");
                    updateDialog.dismiss();
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    public void onDownloadSuccess() {
        //ShowMessage.taskShow(SetSysActivity.this,"下载完成");
        Message message = new Message();
        message.what = 0;
        mHandlerLoad.sendMessage(message);
    }

    @Override
    public void onDownloading(int progress) {
        //progressBar.setProgress(progress);
        Message message = new Message();
        message.what = 1;
        message.obj=progress;
        mHandlerLoad.sendMessage(message);

    }

    @Override
    public void onDownloadFailed() {
        //ShowMessage.taskShow(SetSysActivity.this,"网络异常,下载失败");
        Message message = new Message();
        message.what = 2;
        mHandlerLoad.sendMessage(message);
    }
}
