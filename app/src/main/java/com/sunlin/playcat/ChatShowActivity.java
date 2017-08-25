package com.sunlin.playcat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ChatShowActivity extends MyActivtiyToolBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarBuild("查看消息",true,false);
        ToolbarBackListense();

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_chat;
    }
}
