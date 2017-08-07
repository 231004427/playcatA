package com.sunlin.playcat.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sunlin.playcat.fragment.MyFragment;
import com.sunlin.playcat.fragment.ShopFragmentList;

/**
 * Created by sunlin on 2017/8/5.
 */

public class ShopFragmentPageAdapter extends FragmentPagerAdapter {
    private String[] _titles;
    private int[] _types;
    public ShopFragmentPageAdapter(FragmentManager fm, String[] titles, int[] types) {
        super(fm);
        _titles=titles;
        _types=types;
    }
    @Override
    public Fragment getItem(int position) {
        return ShopFragmentList.newInstance(_types[position]);
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

