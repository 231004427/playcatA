package com.sunlin.playcat.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunlin.playcat.FriendShowActivity;
import com.sunlin.playcat.MainActivity;
import com.sunlin.playcat.MessageActivity;
import com.sunlin.playcat.MyApp;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.GsonHelp;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Friend;
import com.sunlin.playcat.domain.FriendList;
import com.sunlin.playcat.json.FriendRESTful;
import com.sunlin.playcat.view.AddFriendDialog;
import com.sunlin.playcat.view.CircleTitleView;
import com.sunlin.playcat.view.MyDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlin on 2017/7/9.
 */

public class TalkFragment extends Fragment implements FriendListAdpter.OnItemClickListener,RestTask.ResponseCallback {
    String mName;
    private String TAG="TalkFragment";

    private Context myContext;
    private RecyclerView mRecyclerView;
    private boolean isLoading=false;
    private LinearLayoutManager mLayoutManager;
    private int getType=1;
    private MyApp myApp;

    //需修改
    private FriendList dataList;
    private TalkListAdpter listAdapter;
    private FriendRESTful friendRESTful;
    CircleTitleView loadTextView;

    private MaterialRefreshLayout swipe_refresh_widget;

    public static TalkFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name",name);
        TalkFragment fragment = new TalkFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mName = getArguments() != null ? getArguments().getString("name") : "Null";
        //初始化分页对象
        myApp=(MyApp) getActivity().getApplication();
        myContext=TalkFragment.this.getActivity();
        friendRESTful=new FriendRESTful(myApp.getUser());

        //((MainActivity)getActivity()).hideRed();
    }
    //判断是否滑动到底部
    public  boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_talk, null);
        //获取对象
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        loadTextView = (CircleTitleView) view.findViewById(R.id.netoffText);
        swipe_refresh_widget=(MaterialRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
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
        //滚动绑定
        mRecyclerView.setOnTouchListener(mRecyclerViewTouch);

        //判断list点击
        //viewList[i].setBackgroundColor(ContextCompat.getColor(getActivity(), CValues.color.textColor_low));
        dataList=new FriendList();
        dataList.setUser_id(myApp.getUser().getId());
        dataList.setCount(0);
        List<Friend> listData = new ArrayList<Friend>();
        dataList.setList(listData);
        //使用单行
        mLayoutManager=new LinearLayoutManager(myContext);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //自定义分割线
        mRecyclerView.addItemDecoration(new MyDecoration(getContext()));
        listAdapter = new TalkListAdpter(dataList.getList());
        listAdapter.setOnItemClickListener(this);
        //setFooterView(mRecyclerView);
        mRecyclerView.setAdapter(listAdapter);


        //初始化加载
        return view;
    }
    public void BaseBuild(){
        isLoading=false;
        getType=1;
        BuildData();
    }
    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();
        BaseBuild();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden) {
            BaseBuild();
        }
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
                    //Log.e("scroll","底部");
                    getType=3;
                    BuildData();

                }
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

            dataList.setType(-1);//所有消息
            dataList.setStart(0);
            dataList.setStatus(1);
            dataList.setPageNum(10);

            //loadTextView.setText(myContext.getString(R.string.loading));
            //loadTextView.setVisibility(View.VISIBLE);
            //mRecyclerView.setVisibility(View.VISIBLE);
        }
        /*
        if(getType==3||getType==2) {
            TextView footText = (TextView) listAdapter.getFooterView().findViewById(R.id.footText);
            footText.setText(myContext.getString(R.string.nextpage));
        }*/

        friendRESTful.messageList(dataList,this);
    }

    private void setFooterView(RecyclerView view){
        View footer = LayoutInflater.from(myContext).inflate(R.layout.foot, view, false);
        listAdapter.setFooterView(footer);
    }
    @Override
    public void onItemClick(View view) {

        TextView nameText=(TextView) view.findViewById(R.id.nameText);
        Friend friend=(Friend) nameText.getTag();
        Intent intent=new Intent(getActivity(), MessageActivity.class);
        intent.putExtra("friend",friend);
        startActivity(intent);

    }
    @Override
    public void onRequestSuccess(String response) {
        try {
            Gson gson= GsonHelp.getGsonObj();
            //处理结果
            BaseResult result=gson.fromJson(response,BaseResult.class);
            if (result.getErrcode() <= 0 && result.getType() == ActionType.FRIEND_MESSAGE)
            {
                FriendList list = gson.fromJson(result.getData(), FriendList.class);
                if (list != null && list.getList().size() > 0) {
                    //初始化数据
                    if(getType==1 ||getType==2) {
                        dataList.setCount(list.getCount());
                        dataList.getList().clear();
                        dataList.getList().addAll(list.getList());
                        listAdapter.notifyDataSetChanged();
                    }
                    //分页数据
                    if(getType==3)
                    {
                        dataList.getList().addAll(list.getList());
                        listAdapter.notifyDataSetChanged();
                    }
                    //判断是否到页尾
                    int start=dataList.getStart();
                    int pageNum=dataList.getPageNum();
                    dataList.setStart(start+pageNum);
                            /*
                            if(dataList.getStart()>=dataList.getCount()){
                                TextView footText=(TextView)listAdapter.getFooterView().findViewById(R.id.footText);
                                footText.setText(myContext.getString(R.string.nodata));
                            }*/
                    //隐藏加载提示
                    loadTextView.setVisibility(View.GONE);
                }else{
                    if(dataList.getList().size()>0) {
                        dataList.getList().clear();
                        listAdapter.notifyDataSetChanged();
                    }
                    loadTextView.setVisibility(View.VISIBLE);
                    loadTextView.setText(myContext.getString(R.string.nodata_f));
                }
            }
            if(result.getErrcode() >0){
                ShowMessage.taskShow(myContext, result.getErrmsg());
                loadTextView.setText(getString(R.string.error_server));
            }
        }catch (Exception e)
        {
            LogC.write(e,TAG);
            ShowMessage.taskShow(myContext,getString(R.string.error_server));
            loadTextView.setText(getString(R.string.error_server));

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

