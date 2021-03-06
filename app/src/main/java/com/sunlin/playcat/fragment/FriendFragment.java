package com.sunlin.playcat.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.sunlin.playcat.FriendShowActivity;
import com.sunlin.playcat.MyApp;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Friend;
import com.sunlin.playcat.domain.FriendList;
import com.sunlin.playcat.domain.Goods;
import com.sunlin.playcat.domain.GoodsList;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.FriendRESTful;
import com.sunlin.playcat.json.GoodsRESTful;
import com.sunlin.playcat.view.AddFriendDialog;
import com.sunlin.playcat.view.CircleTitleView;
import com.sunlin.playcat.view.LoadingDialog;
import com.sunlin.playcat.view.MyDecoration;
import com.sunlin.playcat.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlin on 2017/7/9.
 */

public class FriendFragment extends Fragment implements FriendListAdpter.OnItemClickListener,RestTask.ResponseCallback{
    String mName;
    private String TAG="FriendFragment";
    private ImageView addImg;
    private AddFriendDialog addFriendDialog;

    private Context myContext;
    private int typeId;
    private RecyclerView mRecyclerView;
    private boolean isLoading=false;
    private LinearLayoutManager mLayoutManager;
    private int getType=1;
    private MyApp myApp;

    //需修改
    private FriendList dataList;
    private FriendListAdpter listAdapter;
    private FriendRESTful friendRESTful;

    CircleTitleView loadTextView;
    private MaterialRefreshLayout swipe_refresh_widget;


    public static FriendFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name",name);
        FriendFragment fragment = new FriendFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mName = getArguments() != null ? getArguments().getString("name") : "Null";
        myApp=(MyApp) getActivity().getApplication();
        myContext=FriendFragment.this.getActivity();
        friendRESTful=new FriendRESTful(myApp.getUser());


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
        View view = inflater.inflate(R.layout.fragment_friend, null);
        //获取对象
        addImg=(ImageView)view.findViewById(R.id.addImg);

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
        dataList.setType(typeId);
        dataList.setStart(0);
        dataList.setStatus(1);
        dataList.setPageNum(10);
        //使用单行
        mLayoutManager=new LinearLayoutManager(myContext);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //自定义分割线
        mRecyclerView.addItemDecoration(new MyDecoration(getContext()));
        listAdapter = new FriendListAdpter(dataList.getList());
        listAdapter.setOnItemClickListener(this);
        //setFooterView(mRecyclerView);
        mRecyclerView.setAdapter(listAdapter);


        //初始化加载
        isLoading=false;
        getType=1;
        BuildData();


        //添加朋友对话框
        addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFriendDialog=new AddFriendDialog();
                addFriendDialog.setType(1);
                addFriendDialog.show(getFragmentManager(),"AddFriendDialog");

            }
        });

        return view;
    }
    //添加朋友
    public void addFriend(User userFriend){
        //addFriendDialog.dismiss();
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
            loadTextView.setText(myContext.getString(R.string.loading));
            loadTextView.setVisibility(View.VISIBLE);
            //mRecyclerView.setVisibility(View.VISIBLE);
        }
        /*
        if(getType==3||getType==2) {
            TextView footText = (TextView) listAdapter.getFooterView().findViewById(R.id.footText);
            footText.setText(myContext.getString(R.string.nextpage));
        }*/

        friendRESTful.search(dataList,this);
    }

    private void setFooterView(RecyclerView view){
        View footer = LayoutInflater.from(myContext).inflate(R.layout.foot, view, false);
        listAdapter.setFooterView(footer);
    }
    @Override
    public void onItemClick(View view) {

        TextView nameText=(TextView) view.findViewById(R.id.nameText);

        Friend friend=(Friend) nameText.getTag();

        Intent intent=new Intent(getActivity(), FriendShowActivity.class);
        intent.putExtra("friend",friend);
        startActivity(intent);

    }

    @Override
    public void onRequestSuccess(String response) {
        try {
            Gson gson = new Gson();
            //处理结果
            BaseResult result=gson.fromJson(response,BaseResult.class);
            if (result.getErrcode() <= 0 && result.getType() == ActionType.FRIEND_SEARCH)
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
                    if(getType==1 ||getType==2) {
                        loadTextView.setVisibility(View.VISIBLE);
                        loadTextView.setText(myContext.getString(R.string.nodata_f));
                    }
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
