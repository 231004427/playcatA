package com.sunlin.playcat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.sunlin.playcat.R;
import com.sunlin.playcat.view.MyFragment;
import com.sunlin.playcat.view.MyFragmentPageAdapter;
import com.sunlin.playcat.view.MyViewPagerAdapter;
import com.sunlin.playcat.view.WrapContentHeightViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlin on 2017/7/19.
 */

public class IndexFragment  extends Fragment {
    String mName;
    private Context myContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPageAdapter mAdapter;

    public static IndexFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name",name);
        IndexFragment fragment = new IndexFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mName = getArguments() != null ? getArguments().getString("name") : "Null";
        //初始化
        myContext=IndexFragment.this.getActivity();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, null);

        mViewPager=(ViewPager)view.findViewById(R.id.viewPager);
        String[] titles=new String[]{"精选", "在线", "棋牌","益智", "动作", "小游戏"};
        int[] type=new int[]{1,2,3,4,5,6};
        FragmentManager fm=getChildFragmentManager();
        mAdapter=new MyFragmentPageAdapter(fm,titles,type);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);//设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
        mViewPager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));

        //TabLayout
        mTabLayout = (TabLayout)view.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }
}
