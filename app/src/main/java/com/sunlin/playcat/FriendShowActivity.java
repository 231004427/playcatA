package com.sunlin.playcat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FriendShowActivity extends MyActivtiyToolBar {
    private String TAG="FriendShowActivity";
    private String userName;
    private int userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userId=getIntent().getIntExtra("userId",-1);
        userName=getIntent().getStringExtra("userName");

        ToolbarBuild(userName,true,false);
        ToolbarBackListense();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_friend_show;
    }
}
