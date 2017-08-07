package com.sunlin.playcat;

import android.content.Intent;
import android.os.Bundle;

import com.sunlin.playcat.domain.Goods;

public class GoodsShowActivity extends MyActivtiyToolBar {
    private Goods goods;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //获取传递数据

        Intent intent = this.getIntent();
        goods=(Goods) intent.getSerializableExtra("goods");

        //初始化导航栏
        ToolbarBuild(goods.getTitle(),true,false);
        ToolbarBackListense();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_buy;
    }
}
