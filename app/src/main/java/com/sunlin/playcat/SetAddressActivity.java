package com.sunlin.playcat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.sunlin.playcat.view.SelectCityDialog;

public class SetAddressActivity extends MyActivtiyToolBar {
    private LinearLayout selectCityLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        selectCityLayout=(LinearLayout)findViewById(R.id.selectCityLayout);

        //初始化导航栏
        ToolbarBuild("设置地址",true,true);
        ToolbarBackListense();
        toolSet.setImageResource(R.drawable.save22);
        //不弹出软键盘
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        //地区设置
        selectCityLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectCityDialog selectCityDialog=new SelectCityDialog(SetAddressActivity.this);
                selectCityDialog.show();
            }
        });


    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_address;
    }
}
