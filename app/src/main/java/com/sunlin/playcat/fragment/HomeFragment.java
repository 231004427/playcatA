package com.sunlin.playcat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.sunlin.playcat.LoginActivity;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.GameList;
import com.sunlin.playcat.json.ActionType;
import com.sunlin.playcat.json.BaseRESTful;
import com.sunlin.playcat.json.GameRESTful;
import com.sunlin.playcat.view.GameListAdapter;
import com.sunlin.playcat.view.MyViewPagerAdapter;

import org.w3c.dom.Text;

import java.util.Random;

import static android.content.ContentValues.TAG;

/**
 * Created by sunlin on 2017/7/9.
 */

public class HomeFragment extends Fragment implements TabLayout.OnTabSelectedListener{
    private Context myContext;
    String mName;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    private NestedScrollView myMainScrollView;
    private CoordinatorLayout coordinatorLayout;
    private AppBarLayout appBarLayout;
    private MaterialRefreshLayout swipe_refresh_widget;

    int tabIndex=0;

    private View[] viewList=new View[6];
    private GameList[] dataList=new GameList[6];
    int[] getTypes;
    boolean[] baseBuild;
    LinearLayoutManager[] mLayoutManagers;
    private GameListAdapter[] listAdapters;

    boolean stopScroll=false;
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

        myContext=HomeFragment.this.getContext();

        mName = getArguments() != null ? getArguments().getString("name") : "Null";
        LayoutInflater lf = getActivity().getLayoutInflater();

        //获取分类数据
        String[] titles=new String[]{"精选", "在线", "棋牌","益智", "动作", "小游戏"};
        getTypes= new int[]{1,1,1,1,1,1};
        baseBuild=new boolean[]{false,false,false,false,false,false};
        listAdapters=new GameListAdapter[6];
        mLayoutManagers=new LinearLayoutManager[6];

        for(int i=0;i<6;i++) {
            viewList[i] = lf.inflate(R.layout.fragment_pager_list, null);
            //设置网络异常提示框
            /*
            TextView netoff = (TextView) viewList[i].findViewById(R.id.netoffText);
            ViewGroup.LayoutParams layoutParams = netoff.getLayoutParams();
            int dens = (int) ScreenUtil.getScreenDensity(myContext);
            int screentHeight = (int) ScreenUtil.getScreenHeightDp(myContext);
            int statusHeight = ScreenUtil.getStatusHeight(myContext);
            int viewHeight = (screentHeight - 44 - 75 - 44 - 56) * dens - statusHeight;
            layoutParams.height = viewHeight;
            netoff.setLayoutParams(layoutParams);*/
            //viewList[i].setBackgroundColor(ContextCompat.getColor(getActivity(), CValues.color.textColor_low));
            dataList[i]=new GameList();
            dataList[i].setCount(0);
            dataList[i].setType(i);
            dataList[i].setStart(0);
            dataList[i].setPageNum(4);
        }
        tabAdapter=new MyViewPagerAdapter(viewList,titles);

    }
    private void tabSelect(int type)
    {
        getTypes[tabIndex]=type;
        //判断到页尾
        if(type==3){
            if(dataList[tabIndex].getStart()>=dataList[tabIndex].getCount()){
                return;
            }
        }
        if(type==2){
            dataList[tabIndex].setStart(0);
            dataList[tabIndex].setCount(0);
        }
        if(type==1)
        {
            swipe_refresh_widget.autoRefresh();
        }
        //网络异常提示
        /*
        TextView netoff = (TextView) viewList[tabIndex].findViewById(R.id.netoffText);
        netoff.setVisibility(View.GONE);*/

        stopScroll=false;

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

                                //初始化数据
                                if(getTypes[tabIndex]==1) {
                                    dataList[tabIndex]=gameList;
                                    RecyclerView mRecyclerView = (RecyclerView) viewList[tabIndex].findViewById(R.id.my_recycler_view);
                                    mLayoutManagers[tabIndex]=new LinearLayoutManager(myContext);
                                    mRecyclerView.setLayoutManager(mLayoutManagers[tabIndex]);
                                    mRecyclerView.setHasFixedSize(true);
                                    listAdapters[tabIndex] = new GameListAdapter(dataList[tabIndex].getGames());
                                    setFooterView(mRecyclerView);
                                    mRecyclerView.setAdapter(listAdapters[tabIndex]);
                                    baseBuild[tabIndex]=true;
                                }
                                //重头更新数据
                                if(getTypes[tabIndex]==2)
                                {
                                    dataList[tabIndex].setCount(gameList.getCount());
                                    dataList[tabIndex].getGames().clear();
                                    dataList[tabIndex].getGames().addAll(gameList.getGames());
                                    listAdapters[tabIndex].notifyDataSetChanged();
                                    TextView footText=(TextView)listAdapters[tabIndex].getFooterView().findViewById(R.id.footText);
                                    footText.setText(myContext.getString(R.string.nextpage));

                                }
                                //分页数据
                                if(getTypes[tabIndex]==3)
                                {
                                    dataList[tabIndex].getGames().addAll(gameList.getGames());
                                    listAdapters[tabIndex].notifyDataSetChanged();
                                }
                                //判断是否到页尾
                                int start=dataList[tabIndex].getStart();
                                int pageNum=dataList[tabIndex].getPageNum();
                                dataList[tabIndex].setStart(start+pageNum);
                                if(dataList[tabIndex].getStart()>=dataList[tabIndex].getCount()){
                                    TextView footText=(TextView)listAdapters[tabIndex].getFooterView().findViewById(R.id.footText);
                                    footText.setText(myContext.getString(R.string.nodata));
                                }

                            } else {
                                ShowMessage.taskShow(myContext, "数据错误");
                            }
                        }
                    } else {
                        ShowMessage.taskShow(myContext, getString(R.string.error_server));
                    }
                }catch (Exception e)
                {

                    LogC.write(e,TAG);
                    ShowMessage.taskShow(myContext,getString(R.string.error_server));

                }finally {
                    swipe_refresh_widget.finishRefresh();
                }

            }
            @Override
            public void onRequestError(Exception error) {
                if(dataList[tabIndex].getStart()==0) {
                    //网络异常提示
                    /*
                    TextView netoff = (TextView) viewList[tabIndex].findViewById(R.id.netoffText);
                    netoff.setVisibility(View.VISIBLE);*/
                    //ShowMessage.taskShow(myContext,getString(R.string.error_net));
                    stopScroll = true;

                }
                swipe_refresh_widget.finishRefresh();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setFooterView(RecyclerView view){
        View footer = LayoutInflater.from(myContext).inflate(R.layout.foot, view, false);
        listAdapters[tabIndex].setFooterView(footer);
    }
    private View.OnTouchListener coordToouchListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if(!stopScroll)
            {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
            else {
                return true;
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, null);

        myMainScrollView=(NestedScrollView) view.findViewById(R.id.myMainScrollView);
        coordinatorLayout=(CoordinatorLayout)view.findViewById(R.id.coordinatorLayout);
        appBarLayout=(AppBarLayout)view.findViewById(R.id.appBarLayout);

        swipe_refresh_widget=(MaterialRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);

        mTabLayout = (TabLayout)view.findViewById(R.id.tabs);

        //滑动监控
        myMainScrollView.setOnTouchListener(coordToouchListener);
        coordinatorLayout.setOnTouchListener(coordToouchListener);
        myMainScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY > oldScrollY) {
                    // 向下滑动
                    swipe_refresh_widget.setEnabled(false);
                }

                if (scrollY < oldScrollY) {
                    // 向上滑动
                    swipe_refresh_widget.setEnabled(false);
                }

                if (scrollY == 0) {
                    // 顶部
                    Log.e("scroll","顶部");
                }
                int temp=v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight();
                if (scrollY == temp) {
                    // 底部
                    Log.e("scroll","底部");
                    //显示下一页数据
                    tabSelect(3);
                }
            }
        });
        //下拉刷新
        //swipe_refresh_widget.setRefreshing(true);
        //swipe_refresh_widget.autoRefresh();
        swipe_refresh_widget.setSunStyle(true);
        swipe_refresh_widget.setLoadMore(false);
        swipe_refresh_widget.setIsOverLay(true);
        swipe_refresh_widget.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                // TODO Auto-generated method stub
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tabSelect(baseBuild[tabIndex]?2:1);

                    }
                }, 2000);
                /*
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {

                        swipe_refresh_widget.setRefreshing(false);
                    }
                });*/
            }
        });
        //判断是否到头部，启动下拉刷新
        if (appBarLayout != null)
            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    if (swipe_refresh_widget == null) return;
                    if(verticalOffset >= 0) {
                        swipe_refresh_widget.setEnabled(true);
                    }else{
                        swipe_refresh_widget.setEnabled(false);
                    }
                }
            });

        //绑定Tab
        //myMainScrollView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
        mViewPager=(ViewPager)view.findViewById(R.id.viewPager);
        mViewPager.setAdapter(tabAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(this);
        tabSelect(1);
        return view;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        tabIndex=tab.getPosition();
        //判断是否是第一次加载
        if(!baseBuild[tabIndex])
        {
            tabSelect(1);
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
