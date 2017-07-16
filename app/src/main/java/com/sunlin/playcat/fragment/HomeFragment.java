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

import com.google.gson.Gson;
import com.sunlin.playcat.LoginActivity;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.GameList;
import com.sunlin.playcat.json.ActionType;
import com.sunlin.playcat.json.BaseRESTful;
import com.sunlin.playcat.json.GameRESTful;
import com.sunlin.playcat.view.GameListAdapter;
import com.sunlin.playcat.view.MyViewPagerAdapter;

import static android.content.ContentValues.TAG;

/**
 * Created by sunlin on 2017/7/9.
 */

public class HomeFragment extends Fragment {
    String mName;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private View[] viewList=new View[6];
    private GameList[] dataList=new GameList[6];

    int tabIndex=0;

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private GameListAdapter listAdapter;
    private MyViewPagerAdapter tabAdapter;


    public static HomeFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name",name);
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mName = getArguments() != null ? getArguments().getString("name") : "Null";
        LayoutInflater lf = getActivity().getLayoutInflater();

        //获取分类数据
        String[] titles=new String[]{"精选", "在线", "棋牌","益智", "动作", "小游戏"};

        for(int i=0;i<6;i++) {
            viewList[i] = lf.inflate(R.layout.fragment_pager_list, null);
            //viewList[i].setBackgroundColor(ContextCompat.getColor(getActivity(), CValues.color.textColor_low));
            dataList[i]=new GameList();
            dataList[i].setCount(0);
            dataList[i].setType(i);
            dataList[i].setStart(0);
            dataList[i].setPageNum(10);
        }
        tabAdapter=new MyViewPagerAdapter(viewList,titles);

    }
    private void tabSelect(int selectIndex)
    {
        tabIndex=selectIndex;
        GameRESTful gameRESTful=new GameRESTful();
        gameRESTful.search(dataList[tabIndex], new RestTask.ResponseCallback() {
            @Override
            public void onRequestSuccess(String response) {
                try {
                    Gson gson = new Gson();
                    //处理结果
                    GameList gameList = gson.fromJson(response, GameList.class);
                    if (gameList != null) {
                        BaseResult result = gameList.getResult();
                        if (result.getErrcode() <= 0 && result.getType() == ActionType.GAME_SEARCH) {

                            if (gameList != null && gameList.getGames().size() > 0) {

                                //列表数据
                                mRecyclerView = (RecyclerView) viewList[tabIndex].findViewById(R.id.my_recycler_view);
                                //创建默认的线性LayoutManager
                                mLayoutManager = new LinearLayoutManager(HomeFragment.this.getContext());
                                mRecyclerView.setLayoutManager(mLayoutManager);
                                //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
                                mRecyclerView.setHasFixedSize(true);//创建并设置Adapter
                                listAdapter = new GameListAdapter(gameList.getGames());
                                mRecyclerView.setAdapter(listAdapter);
                                setFooterView(mRecyclerView);

                                dataList[tabIndex] = gameList;

                            } else {
                                ShowMessage.taskShow(HomeFragment.this.getContext(), "暂无数据");
                            }
                        }
                    } else {
                        ShowMessage.taskShow(HomeFragment.this.getContext(), getString(R.string.error_server));
                    }
                }catch (Exception e)
                {
                    LogC.write(e,TAG);
                    ShowMessage.taskShow(HomeFragment.this.getContext(),getString(R.string.error_server));
                }

            }
            @Override
            public void onRequestError(Exception error) {

            }
        });
    }

    private void setFooterView(RecyclerView view){
        View footer = LayoutInflater.from(this.getContext()).inflate(R.layout.foot, view, false);
        listAdapter.setFooterView(footer);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        mViewPager=(ViewPager)view.findViewById(R.id.viewPager);
        mViewPager.setAdapter(tabAdapter);

        //TabLayout
        mTabLayout = (TabLayout)view.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        tabSelect(0);
        return view;
    }
}
