package com.sunlin.playcat;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.view.GameFragmentPageAdapter;
import com.sunlin.playcat.view.MyFragmentPageAdapter;

import static java.security.AccessController.getContext;

public class GameShowActivity extends MyActivtiy implements View.OnClickListener,RestTask.ResponseCallback{
    private String TAG="GameShowActivity";

    private String name;
    private int id;
    private String note;

    private TextView noteText;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private GameFragmentPageAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化对象
        noteText=(TextView)findViewById(R.id.noteText);
        mTabLayout=(TabLayout)findViewById(R.id.tabs);
        mViewPager=(ViewPager)findViewById(R.id.viewPager);

        //获取参数
        name=getIntent().getStringExtra("name");
        id=getIntent().getIntExtra("id",0);
        note=getIntent().getStringExtra("note");
        if(id==0||name=="")
        {
            ShowMessage.taskShow(this,getString(R.string.error_param));
            return;
        }
        noteText.setText(note);
        //初始化导航栏
        ToolbarBuild(name,true,false);
        ToolbarBackListense();

        //Tabs初始化
        String[] titles=new String[]{"介绍", "评论", "排行榜","规则"};
        int[] type=new int[]{1,2,3,4};
        FragmentManager fm=getSupportFragmentManager();
        mAdapter=new GameFragmentPageAdapter(fm,titles,type);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);//设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
        mViewPager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
        mTabLayout.setupWithViewPager(mViewPager);

    }

    @Override
    protected int getLayoutResId() {
        //onCreate的方法中不需要写setContentView()
        return R.layout.activity_game_show;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRequestSuccess(String response) {

    }

    @Override
    public void onRequestError(Exception error) {

    }
}
