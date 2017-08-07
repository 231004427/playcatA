package com.sunlin.playcat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunlin.playcat.R;

/**
 * Created by sunlin on 2017/7/9.
 */

public class ShopFragment extends Fragment {

    TextView title;
    String mName;
    private Context myContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ShopFragmentPageAdapter mAdapter;

    public static ShopFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name",name);
        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mName = getArguments() != null ? getArguments().getString("name") : "Null";
        //初始化
        myContext=ShopFragment.this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, null);

        title = (TextView) view.findViewById(R.id.toolbar_title);

        title.setText("兑换");

        mViewPager=(ViewPager)view.findViewById(R.id.viewPager);
        String[] titles=new String[]{"所有", "购买钻石", "兑换金币","兑换奖品"};
        int[] type=new int[]{0,1,2,3};
        FragmentManager fm=getChildFragmentManager();
        mAdapter=new ShopFragmentPageAdapter(fm,titles,type);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);//设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
        mViewPager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
        //TabLayout
        mTabLayout = (TabLayout)view.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }
}

