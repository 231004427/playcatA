package com.sunlin.playcat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sunlin.playcat.MyActivtiyToolBar;
import com.sunlin.playcat.R;

public class SetMyActivity extends MyActivtiyToolBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarBuild("个人资料",true,false);
        ToolbarBackListense();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_my;
    }
}
