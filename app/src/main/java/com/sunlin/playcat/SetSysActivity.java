package com.sunlin.playcat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SetSysActivity extends MyActivtiyToolBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarBuild("设置",true,false);
        ToolbarBackListense();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_sys;
    }
}
