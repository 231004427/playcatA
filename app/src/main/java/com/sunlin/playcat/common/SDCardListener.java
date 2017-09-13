package com.sunlin.playcat.common;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.io.File;
import java.util.Date;

/**
 * Created by sunlin on 2017/9/12.
 */

public class SDCardListener extends BroadcastReceiver {

    private static boolean sdcardAvailable;
    private static boolean sdcardAvailabilityDetected;
    /**
     *
     * @return SD is available ?synchronized同步
     */
    public static synchronized boolean detectSDCardAvailability() {
        boolean result = false;
        try {
            Date now = new Date();
            long times = now.getTime();
            String fileName = "/sdcard/" + times + ".test";
            File file = new File(fileName);
            result = file.createNewFile();
            file.delete();
        } catch (Exception e) {
            // Can't create file, SD Card is not available
            e.printStackTrace();
        } finally {
            sdcardAvailabilityDetected = true;
            sdcardAvailable = result;
        }
        return result;
    }

    /**
     *
     * @return SD is available ?
     */
    public static boolean isSDCardAvailable() {
        if(!sdcardAvailabilityDetected) {
            sdcardAvailable = detectSDCardAvailability();
            sdcardAvailabilityDetected = true;
        }
        return  sdcardAvailable;
    }

    /* (non-Javadoc)
     * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
     */
    public void onReceive(Context context, Intent intent) {
        sdcardAvailabilityDetected = false;
        sdcardAvailable = detectSDCardAvailability();
        sdcardAvailabilityDetected = true;
    }

}
