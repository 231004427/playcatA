package com.sunlin.playcat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.sunlin.playcat.GameShowActivity;
import com.sunlin.playcat.MyApp;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Game;
import com.sunlin.playcat.domain.GameList;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.GameRESTful;
import com.sunlin.playcat.json.UserRESTful;
import com.sunlin.playcat.view.CircleImageView;
import com.sunlin.playcat.view.CircleTitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlin on 2017/7/11.
 */

public class MyFragment extends Fragment implements GameListAdapter.OnItemClickListener,RestTask.ResponseCallback {
    private String TAG="MyFragment";
    private Context myContext;
    int typeId;

    private RecyclerView mRecyclerView;
    private GameList dataList;
    private boolean isLoading=false;
    private LinearLayoutManager mLayoutManager;
    private GameListAdapter listAdapter;
    private int getType=1;

    private GameRESTful gameRESTful;
    private MyApp myApp;
    private CircleTitleView loadTextView;

    private MaterialRefreshLayout swipe_refresh_widget;

    public static MyFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt("type",type);

        MyFragment fragment = new MyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        myContext=MyFragment.this.getContext();

        typeId = getArguments() != null ? getArguments().getInt("type") : 1;

    }
    public  boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pager_list, null);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        loadTextView = (CircleTitleView) view.findViewById(R.id.netoffText);
        swipe_refresh_widget=(MaterialRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);


        myApp=(MyApp) this.getActivity().getApplication();
        gameRESTful=new GameRESTful(myApp.getUser());

        //点击重试
        loadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuildData();
            }
        });

        //下拉刷新
        //swipe_refresh_widget.setRefreshing(true);
        //swipe_refresh_widget.autoRefresh();
        swipe_refresh_widget.setSunStyle(true);
        swipe_refresh_widget.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                // TODO Auto-generated method stub
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //重新加载
                        getType=2;
                        BuildData();
                    }
                }, 1000);
            }
        });

        //设置网络异常View高度
        /*
        ViewGroup.LayoutParams layoutParams = loadTextView.getLayoutParams();
        int dens = (int) ScreenUtil.getScreenDensity(myContext);
        int screentHeight = (int) ScreenUtil.getScreenHeightDp(myContext);
        int statusHeight = ScreenUtil.getStatusHeight(myContext);
        int viewHeight = (screentHeight - 75 - 44 - 56) * dens - statusHeight;
        layoutParams.height = viewHeight;
        loadTextView.setLayoutParams(layoutParams);*/

        //判断list滚动
        mRecyclerView.setOnTouchListener(mRecyclerViewTouch);

        //判断list点击

        //viewList[i].setBackgroundColor(ContextCompat.getColor(getActivity(), CValues.color.textColor_low));
        dataList=new GameList();
        dataList.setCount(0);
        List<Game> games = new ArrayList<Game>();
        dataList.setGames(games);
        dataList.setType(typeId);
        dataList.setStart(0);
        dataList.setPageNum(4);

        mLayoutManager=new LinearLayoutManager(myContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        listAdapter = new GameListAdapter(dataList.getGames());
        listAdapter.setOnItemClickListener(MyFragment.this);
        setFooterView(mRecyclerView);
        mRecyclerView.setAdapter(listAdapter);


        //初始化加载
        isLoading=false;
        getType=1;
        BuildData();

        return view;
    }
    //滑动监控
    float x1=0,y1=0;
    private View.OnTouchListener  mRecyclerViewTouch=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            //Log.e("isLoading","isLoading:"+isLoading);
            if(isLoading){
                return true;
            }
            //继承了Activity的onTouchEvent方法，直接监听点击事件
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                //当手指按下的时候
                x1 =event.getX();
                x1 = event.getX();
                y1 = event.getY();
            }
            if(event.getAction() == MotionEvent.ACTION_UP) {
                //是否到页尾
                if(isSlideToBottom(mRecyclerView))
                {
                    Log.e("scroll","底部");
                    getType=3;
                    BuildData();

                }
                //当手指离开的时候
                /*
                x2 = event.getX();
                y2 = event.getY();
                if(y1-y2 > 30) {
                    //Log.e("scroll","向上滑动");
                } else if(y2 - y1 > 50) {
                    //Log.e("scroll","向下滑动");
                } else if(x1 - x2 > 50) {
                    //Log.e("scroll","向左滑动");
                } else if(x2 - x1 > 50) {
                    //Log.e("scroll","向右滑动");
                }*/
            }
            return false;
        }
    };
    //加载数据
    private void BuildData()
    {
        //判断是否在加载中
        if(isLoading){
            return;
        }
        isLoading=true;
        //判断到页尾
        if(getType==3){
            if(dataList.getStart()>=dataList.getCount()){
                isLoading=false;
                return;
            }
        }
        if(getType==2){
            dataList.setStart(0);
            dataList.setCount(0);
        }
        //初始加载提示
        if(getType==1){
            loadTextView.setText(myContext.getString(R.string.loading));
            loadTextView.setVisibility(View.VISIBLE);
            //mRecyclerView.setVisibility(View.VISIBLE);
        }
        //
        if(getType==3||getType==2) {
            TextView footText = (TextView) listAdapter.getFooterView().findViewById(R.id.footText);
            footText.setText(myContext.getString(R.string.nextpage));
        }
        //Log.e(TAG,"type:"+dataList.getType()+"");
        gameRESTful.search(dataList,this);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setFooterView(RecyclerView view){
        View footer = LayoutInflater.from(myContext).inflate(R.layout.foot, view, false);
        listAdapter.setFooterView(footer);
    }

    @Override
    public void onItemClick(View view) {
        TextView title=(TextView)view.findViewById(R.id.nameText);
        TextView note=(TextView)view.findViewById(R.id.noteText);
        //Log.e("onItemClick",title.getTag()+"");
        Intent intent = new Intent(myContext,GameShowActivity.class);
        intent.putExtra("name", title.getText());
        intent.putExtra("note",note.getText());
        intent.putExtra("id",(int)title.getTag());

        startActivity(intent);
    }

    @Override
    public void onRequestSuccess(String response) {
        try {
            Gson gson = new Gson();
            //处理结果
            BaseResult result=gson.fromJson(response,BaseResult.class);
            if (result.getErrcode() <= 0 && result.getType() == ActionType.GAME_SEARCH)
            {
                GameList gameList = gson.fromJson(result.getData(), GameList.class);
                if (gameList != null && gameList.getGames().size() > 0) {
                    //初始化数据
                    if(getType==1 ||getType==2) {
                        dataList.setCount(gameList.getCount());
                        dataList.getGames().clear();
                        dataList.getGames().addAll(gameList.getGames());
                        listAdapter.notifyDataSetChanged();
                    }
                    //分页数据
                    if(getType==3)
                    {
                        dataList.getGames().addAll(gameList.getGames());
                        listAdapter.notifyDataSetChanged();
                    }
                    //判断是否到页尾
                    int start=dataList.getStart();
                    int pageNum=dataList.getPageNum();
                    dataList.setStart(start+pageNum);
                    if(dataList.getStart()>=dataList.getCount()){
                        TextView footText=(TextView)listAdapter.getFooterView().findViewById(R.id.footText);
                        footText.setText(myContext.getString(R.string.nodata));
                    }
                    //隐藏加载提示
                    loadTextView.setVisibility(View.GONE);
                }else{
                    loadTextView.setText(myContext.getString(R.string.nodata_r));
                }
            }
            if(result.getErrcode() >0){
                ShowMessage.taskShow(myContext, result.getErrmsg());
            }
        }catch (Exception e)
        {
            LogC.write(e,TAG);
            ShowMessage.taskShow(myContext,getString(R.string.error_server));

        }finally {
            isLoading=false;
            swipe_refresh_widget.finishRefresh();
        }

    }

    @Override
    public void onRequestError(Exception error) {
        //网络异常提示
        loadTextView.setText(myContext.getString(R.string.error_net));
        loadTextView.setVisibility(View.VISIBLE);

        swipe_refresh_widget.finishRefresh();
        isLoading=false;
        LogC.write(error,TAG);
    }
}