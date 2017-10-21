package com.sunlin.playcat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by sunlin on 2017/10/10.
 */

public class ActivityAll extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 添加Activity到堆栈
        AtyContainer.getInstance().addActivity(this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 结束Activity&从栈中移除该Activity
        AtyContainer.getInstance().removeActivity(this);
    }
}
