package com.sunlin.playcat;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by sunlin on 2017/6/24.
 */

public abstract class MyActivtiy extends AppCompatActivity {
    public Toolbar toolbar;
    public TextView toolText;
    public ImageView toolBack;
    public ImageView toolSet;
    //初始化导航栏
    public void ToolbarBuild(String title, boolean isBack, boolean isSet)
    {
        toolText.setText(title);
        if(!isSet){toolbar.removeView(toolSet);}else{
            ToolbarBackListense();
        }
        if(!isBack){toolbar.removeView(toolBack);}

    }
    //设置按钮事件
    public void ToolbarSetListense(View.OnClickListener l){
        toolSet.setOnClickListener(l);
    }
    //返回按钮事件
    public void ToolbarBackListense(){
        toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutResId());//把设置布局文件的操作交给继承的子类

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolText = (TextView) findViewById(R.id.toolbar_title);
        toolBack=(ImageView)findViewById(R.id.btnBack);
        toolSet=(ImageView)findViewById(R.id.btnSet);
        setSupportActionBar(toolbar);


/*
        //这一行注意！看本文最后的说明！！！！
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        ViewGroup contentFrameLayout = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
        View parentView = contentFrameLayout.getChildAt(0);
        if (parentView != null && Build.VERSION.SDK_INT >= 14) {
            parentView.setFitsSystemWindows(true);
        }

        getSupportActionBar().hide();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //透明导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }*/
    }

    /**
     * 返回当前Activity布局文件的id
     *
     * @return
     */
    abstract protected int getLayoutResId();
}
