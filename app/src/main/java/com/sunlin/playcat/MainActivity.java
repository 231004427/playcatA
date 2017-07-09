package com.sunlin.playcat;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import com.sunlin.playcat.view.CircleTitleView;

public class MainActivity extends MyActivtiy {

    private CircleTitleView goldText;
    private CircleTitleView zhuanText;
    private CircleTitleView missonText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //绑定对象
        goldText=(CircleTitleView)findViewById(R.id.goldText);
        zhuanText=(CircleTitleView)findViewById(R.id.zhuanText);
        missonText=(CircleTitleView)findViewById(R.id.missonText);

        //显示红点

        //初始化导航栏
        ToolbarBuild("玩猫",false,false);
        ToolbarBackListense();

    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    @Override
    protected int getLayoutResId() {
        //onCreate的方法中不需要写setContentView()
        return R.layout.activity_main;
    }
}
