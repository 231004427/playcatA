package com.sunlin.playcat.view;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunlin.playcat.R;

/**
 * Created by sunlin on 2017/7/11.
 */

public abstract class TabFragment extends Fragment {

    public Toolbar toolbar;
    public TextView toolText;
    public ImageView toolBack;
    public ImageView toolSet;


    //初始化导航栏
    public void ToolbarBuild(String title, boolean isBack, boolean isSet)
    {
        toolText.setText(title);
        if(!isSet){toolbar.removeView(toolSet);}else{
            ToolbarBackListense();
        }
        if(!isBack){toolbar.removeView(toolBack);}

    }
    //设置按钮事件
    public void ToolbarSetListense(View.OnClickListener l){
        toolSet.setOnClickListener(l);
    }
    //返回按钮事件
    public void ToolbarBackListense(){
        toolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResId(), null);

        toolbar=(Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolText = (TextView) view.findViewById(R.id.toolbar_title);
        toolBack=(ImageView) view.findViewById(R.id.btnBack);
        toolSet=(ImageView)view.findViewById(R.id.btnSet);

        return view;
    }
    abstract protected int getLayoutResId();
}
