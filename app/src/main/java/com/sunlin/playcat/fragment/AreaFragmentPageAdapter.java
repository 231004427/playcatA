package com.sunlin.playcat.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.sunlin.playcat.domain.Area;

import java.util.List;

/**
 * Created by sunlin on 2017/8/13.
 */

public class AreaFragmentPageAdapter extends FragmentPagerAdapter {
    private AreaFragmentList mCurrentFragment;
    private List<Area> _areaList;
    private AreaListAdapter.OnItemClickListener _mListener;
    public AreaFragmentPageAdapter(FragmentManager fm, List<Area> areaList,AreaListAdapter.OnItemClickListener mListener) {
        super(fm);
        _mListener=mListener;
        _areaList=areaList;
        _areaList=areaList;
    }
    @Override
    public Fragment getItem(int position){

        return AreaFragmentList.newInstance(_areaList.get(position).getId(),_areaList.get(position).getParent_id(),_mListener);
    }
    @Override
    public int getCount() {
        return _areaList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return _areaList.get(position).getName();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        mCurrentFragment = (AreaFragmentList) object;
        super.setPrimaryItem(container, position, object);
    }
    public AreaFragmentList getCurrentFragment() {
        return mCurrentFragment;
    }
}
