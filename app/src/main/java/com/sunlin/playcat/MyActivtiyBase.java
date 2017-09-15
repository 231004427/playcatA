package com.sunlin.playcat;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.domain.User;

/**
 * Created by sunlin on 2017/8/6.
 */

public abstract class MyActivtiyBase extends AppCompatActivity {
    public MyApp myApp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        //用户信息初始化
        myApp = (MyApp)this.getApplicationContext();
        //透明状态栏
        LinearLayout statusLayout=(LinearLayout)findViewById(R.id.statusLayout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if(statusLayout!=null) {
                statusLayout.getLayoutParams().height = ScreenUtil.getStatusHeight(this);
            }
        }else{
            if(statusLayout!=null){
                statusLayout.setVisibility(View.GONE);
            }
        }
        //默认竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    abstract protected int getLayoutResId();
}
