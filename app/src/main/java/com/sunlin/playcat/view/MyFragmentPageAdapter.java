package com.sunlin.playcat.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sunlin.playcat.fragment.MyFragment;

/**
 * Created by sunlin on 2017/7/11.
 */

public class MyFragmentPageAdapter extends FragmentPagerAdapter {
    private String[] _titles;
    private int[] _types;
    public MyFragmentPageAdapter(FragmentManager fm,String[] titles,int[] types) {
        super(fm);
        _titles=titles;
        _types=types;
    }
    @Override
    public Fragment getItem(int position) {
        return MyFragment.newInstance(_types[position]);
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
