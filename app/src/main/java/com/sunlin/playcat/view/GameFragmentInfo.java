package com.sunlin.playcat.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunlin.playcat.R;

/**
 * Created by sunlin on 2017/7/23.
 */

public class GameFragmentInfo extends Fragment {
    int id;
    public static GameFragmentInfo newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("id",id);
        GameFragmentInfo fragment = new GameFragmentInfo();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments() != null ? getArguments().getInt("id") : 0;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_info, null);
        return view;
    }
}