package com.sunlin.playcat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SelectCityActivity extends MyActivtiyToolBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化导航栏
        ToolbarBuild("选择地区",true,false);
        ToolbarBackListense();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_select_city;
    }
}
