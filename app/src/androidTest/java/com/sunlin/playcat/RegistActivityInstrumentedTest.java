package com.sunlin.playcat;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.gson.Gson;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.json.BaseResult;
import com.sunlin.playcat.json.UserRESTful;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

/**
 * Created by sunlin on 2017/7/2.
 */
@RunWith(AndroidJUnit4.class)
public class RegistActivityInstrumentedTest implements RestTask.ResponseCallback {
    String tag="Test";
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        //assertEquals("com.sunlin.playcat", appContext.getPackageName());
    }

    @Override
    public  void onRequestSuccess(String response) {

        String json=response;
        Log.i(tag,json);
        Gson gson = new Gson();
        BaseResult result= gson.fromJson(json,BaseResult.class);
        Log.i(tag,result.getText());

    }

    @Override
    public void onRequestError(Exception error) {
        Log.i(tag,error.getMessage());

    }
}
