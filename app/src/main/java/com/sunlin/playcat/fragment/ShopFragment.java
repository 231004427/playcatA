package com.sunlin.playcat.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.view.GameListAdapter;
import com.sunlin.playcat.view.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlin on 2017/7/9.
 */

public class ShopFragment extends Fragment {

    String mName;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.helloworld, null);
        TextView tv = (TextView) view.findViewById(R.id.toolbar_title);
        tv.setText("兑换");
        return view;
    }
}

