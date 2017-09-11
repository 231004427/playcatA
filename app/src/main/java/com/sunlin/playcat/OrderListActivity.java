package com.sunlin.playcat;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sunlin.playcat.fragment.OrderListAdapter;
import com.sunlin.playcat.fragment.OrderListFragmentPageAdapter;

public class OrderListActivity extends MyActivtiyToolBar {
    private String TAG="OrderListActivity";

    String mName;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private OrderListFragmentPageAdapter mAdapter;
    private MyApp myApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ToolbarBuild("订单",true,false);
        ToolbarBackListense();

        mViewPager=(ViewPager)findViewById(R.id.viewPager);
        //1=待支付,2=已支付，3=已发货，4=已完成
        String[] titles=new String[]{"所有", "待支付", "已支付","已发货","已完成"};
        int[] type=new int[]{0,1,2,3,4};
        FragmentManager fm=getSupportFragmentManager();
        mAdapter=new OrderListFragmentPageAdapter(fm,titles,type);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);//设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
        mViewPager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
        //TabLayout
        mTabLayout = (TabLayout)findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_list;
    }
}
