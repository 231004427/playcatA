package com.sunlin.playcat.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by sunlin on 2017/7/16.
 */

public class CValues {
    public static final String DOWN_PATH="/playcat/down/";
    public static final String DOWN_PATH_APP="/playcat/down/playcat.apk";
    public static final String ip="169.254.97.239";
    public static final String MLM_HOST="169.254.97.239";
    public static final int MLM_PORT=8792;
    public static final String UPDATE_URL="http://"+ip+":8080/down/playcat.apk";
    public static final String SERVER_IMG="http://"+ip+":8080";
    public static final String POST_URL= "http://"+ip+":8080/api/playcat/post";
    public static final String GET_URL= "http://"+ip+":8080/api/playcat/get";
    public static final String APP_NAME="playcat";
    public static final int APP_ID=1;
    public static final int SET=100;
    public static final int SET_NAME=1001;
    public static final int SET_SEX=1002;
    public static final int SET_CITY=1003;
    public static final int SET_ADDRESS=1004;

    /**
     * 获取本地软件版本号名称
     */
    public static int getLocalVersionCode(Context ctx) {
        int localVersion = -1;
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return localVersion;
        }
        return localVersion;
    }
}
