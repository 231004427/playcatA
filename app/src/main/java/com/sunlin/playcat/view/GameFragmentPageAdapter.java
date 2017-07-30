package com.sunlin.playcat.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.sunlin.playcat.fragment.GameFragment;
import com.sunlin.playcat.fragment.GameFragmentInfo;
import com.sunlin.playcat.fragment.GameFragmentRule;
import com.sunlin.playcat.fragment.GameFragmentTop;

/**
 * Created by sunlin on 2017/7/23.
 */

public class GameFragmentPageAdapter  extends FragmentPagerAdapter {
    private String[] _titles;
    private String _note;
    private int _id;
    public GameFragmentPageAdapter(FragmentManager fm, String[] titles, int id,String note) {
        super(fm);
        _titles=titles;
        _id=id;
        _note=note;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return GameFragmentInfo.newInstance(_id,_note);
            case 1:
                return GameFragmentTop.newInstance(_id);
            case 2:
                return GameFragmentRule.newInstance(_id);
                default:
                    return GameFragment.newInstance(_id);
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

