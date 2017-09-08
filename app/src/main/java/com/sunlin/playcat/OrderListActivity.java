package com.sunlin.playcat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class OrderListActivity extends MyActivtiyToolBar {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ToolbarBuild("订单",true,false);
        ToolbarBackListense();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_list;
    }
}
