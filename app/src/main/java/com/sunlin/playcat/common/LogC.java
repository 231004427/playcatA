package com.sunlin.playcat.common;

import android.util.Log;

/**
 * Created by sunlin on 2017/7/2.
 */
public class LogC {

    public static void  write(Exception e,String TAG){
        if(e!=null) {
            Log.d(TAG, e.toString());
        }
    }
}
