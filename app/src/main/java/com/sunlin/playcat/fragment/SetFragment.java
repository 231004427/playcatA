package com.sunlin.playcat.fragment;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sunlin.playcat.R;

/**
 * Created by sunlin on 2017/7/9.
 */

public class SetFragment extends Fragment {
    String mName;
    public static SetFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name",name);
        SetFragment fragment = new SetFragment();
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
        tv.setText("个人");
        return view;
    }
}
