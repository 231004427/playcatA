package com.sunlin.playcat.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by sunlin on 2017/7/11.
 */

public class MyFragmentPageAdapter extends FragmentStatePagerAdapter {
    private String[] titles = new String[]{"Tab1", "Tab2", "Tab3","Tab4", "Tab5", "Tab6"};
    public MyFragmentPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return MyFragment.newInstance(position);
            case 1:
                return MyFragment.newInstance(position);
            case 2:
                return MyFragment.newInstance(position);
            case 3:
                return MyFragment.newInstance(position);
            case 4:
                return MyFragment.newInstance(position);
            case 5:
                return MyFragment.newInstance(position);
            default:return null;
        }
    }

    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
