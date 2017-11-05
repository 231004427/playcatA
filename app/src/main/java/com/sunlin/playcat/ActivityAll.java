package com.sunlin.playcat;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.sunlin.playcat.common.LogC;

import java.util.List;

/**
 * Created by sunlin on 2017/10/10.
 */

public class ActivityAll extends AppCompatActivity {
    private String TAG="ActivityAll";
    public boolean isCurrentRunningForeground = true;
    @Override
    protected void onStart() {
        super.onStart();
        if (!isCurrentRunningForeground) {
            LogC.e(TAG, ">>>>>>>>>>>>>>>>>>>切到前台 activity process");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        isCurrentRunningForeground = isRunningForeground();
        if (!isCurrentRunningForeground) {
            LogC.e(TAG,">>>>>>>>>>>>>>>>>>>切到后台 activity process");
        }
    }

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
    public boolean isRunningForeground() {
        ActivityManager activityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();
        // 枚举进程
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(this.getApplicationInfo().processName)) {
                    LogC.e(TAG,"EntryActivity isRunningForeGround");
                    return true;
                }
            }
        }
        LogC.e(TAG, "EntryActivity isRunningBackGround");
        return false;
    }
}
