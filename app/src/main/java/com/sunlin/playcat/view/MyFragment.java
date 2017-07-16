package com.sunlin.playcat.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunlin.playcat.R;

/**
 * Created by sunlin on 2017/7/11.
 */

public class MyFragment extends Fragment {
    int mNum;

    public static MyFragment newInstance(int num) {

        Bundle args = new Bundle();
        args.putInt("num",num);

        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager_list, null);
        TextView tv = (TextView) view.findViewById(R.id.text);
        tv.setText("fragment+" + mNum);
        return view;
    }
}