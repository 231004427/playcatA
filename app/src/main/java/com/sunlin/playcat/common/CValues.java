package com.sunlin.playcat.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by sunlin on 2017/7/16.
 */

public class CValues {
    public static final String SERVER_IMG="http://10.1.1.4:8080";
    public static final String POST_URL= "http://10.1.1.4:8080/api/playcat/post";
    public static final String GET_URL= "http://10.1.1.4:8080/api/playcat/get";
    public static final String APP_NAME="playcat";
    public static final int APP_ID=1;

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
