package com.sunlin.playcat.view;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by sunlin on 2017/7/11.
 */

public class MyViewPagerAdapter extends PagerAdapter {
    private View[] mListViews;
    private String[] titles;
    public MyViewPagerAdapter(View[] mListViews,String[] _titles) {
        this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。
        titles=_titles;
    }
    //直接继承PagerAdapter，至少必须重写下面的四个方法，否则会报错
    @Override
    public void destroyItem(ViewGroup container, int position, Object object)     {
        container.removeView(mListViews[position]);//删除页卡
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        //这个方法用来实例化页卡
        container.addView(mListViews[position], 0);//添加页卡
        return mListViews[position];
    }
    @Override
    public int getCount() {
        return  mListViews.length;//返回页卡的数量
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0==arg1;//官方提示这样写
    }
}