package com.sunlin.playcat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunlin.playcat.R;

/**
 * Created by sunlin on 2017/7/23.
 */

public class GameFragment extends Fragment {
    int id;
    public static GameFragment newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("id",id);
        GameFragment fragment = new GameFragment();
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
        View view = inflater.inflate(R.layout.fragment_game, null);
        return view;
    }
}
