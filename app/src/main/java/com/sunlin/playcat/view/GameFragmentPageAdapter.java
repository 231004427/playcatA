package com.sunlin.playcat.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by sunlin on 2017/7/23.
 */

public class GameFragmentPageAdapter  extends FragmentPagerAdapter {
    private String[] _titles;
    private int[] _ids;
    public GameFragmentPageAdapter(FragmentManager fm, String[] titles, int[] id) {
        super(fm);
        _titles=titles;
        _ids=id;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 1:
                    return GameFragmentInfo.newInstance(_ids[position]);
                default:
                    return GameFragment.newInstance(_ids[position]);
        }
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

