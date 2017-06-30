package com.sunlin.playcat.common;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by sunlin on 2017/6/29.
 */

public class ShowMessage {
   public static void taskShow(Context context,String messgae){
       Toast.makeText(context, messgae, Toast.LENGTH_SHORT).show();
    }
}
