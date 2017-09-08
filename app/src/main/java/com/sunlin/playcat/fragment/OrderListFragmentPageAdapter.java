package com.sunlin.playcat.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sunlin on 2017/9/8.
 */

public class OrderListFragmentPageAdapter extends FragmentPagerAdapter {
    private String[] _titles;
    private int[] _types;
    public OrderListFragmentPageAdapter(FragmentManager fm, String[] titles, int[] types) {
        super(fm);
        _titles=titles;
        _types=types;
    }
    @Override
    public Fragment getItem(int position) {
        return OrderListFragment.newInstance(_types[position]);
    }
    @Override
    public int getCount() {
        return _titles.length;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return _titles[position];
    }
}
