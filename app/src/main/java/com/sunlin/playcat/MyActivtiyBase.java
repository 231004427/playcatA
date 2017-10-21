package com.sunlin.playcat;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.sunlin.playcat.MLM.MLMClient;
import com.sunlin.playcat.MLM.MLMTCPClient;
import com.sunlin.playcat.MLM.MLMSocketDelegate;
import com.sunlin.playcat.MLM.MLMType;
import com.sunlin.playcat.MLM.MLMUser;
import com.sunlin.playcat.MLM.MyData;
import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.common.ShowMessage;

/**
 * Created by sunlin on 2017/8/6.
 */

public abstract class MyActivtiyBase extends ActivityObj {
    private String TAG="MyActivtiyBase";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
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
    }

    abstract protected int getLayoutResId();

}
