package com.sunlin.playcat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.MyApp;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.Area;
import com.sunlin.playcat.domain.AreaList;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.json.AreaRESTful;
import com.sunlin.playcat.view.CircleTitleView;
import com.sunlin.playcat.view.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlin on 2017/8/13.
 */

public class AreaFragmentList extends Fragment implements RestTask.ResponseCallback,AreaListAdapter.OnItemClickListener {

    private String TAG="AreaFragmentList";
    private Context myContext;
    int typeId;
    int selectId;
    RecyclerView mRecyclerView;

    private AreaList dataList;
    private LinearLayoutManager mLayoutManager;
    int getType=1;
    private AreaRESTful areaRESTful;
    private Handler mHandler = new Handler();
    boolean isLoading=false;
    boolean isEnd=false;
    int loadType=0;
    CircleTitleView loadTextView;
    private AreaListAdapter listAdapter;
    AreaListAdapter.OnItemClickListener _mListener;

    public static AreaFragmentList newInstance(int type,int select,AreaListAdapter.OnItemClickListener mListener) {

        Bundle args = new Bundle();
        args.putInt("_type",type);
        args.putInt("_select",select);
        AreaFragmentList fragment = new AreaFragmentList();
        fragment._mListener=mListener;
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typeId = getArguments() != null ? getArguments().getInt("_type") : -1;
        selectId = getArguments() != null ? getArguments().getInt("_select") : -1;
        //初始化
        myContext=AreaFragmentList.this.getActivity();

        //创建请求
        MyApp app = (MyApp) myContext.getApplicationContext();
        areaRESTful=new AreaRESTful(app.getUser());
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_city_list, null);
        mRecyclerView=(RecyclerView) view.findViewById(R.id.mRecyclerView);
        loadTextView=(CircleTitleView)view.findViewById(R.id.loadTextView);

        //初始化数据
        dataList=new AreaList();
        dataList.setCount(0);
        List<Area> list = new ArrayList<Area>();
        dataList.setList(list);
        dataList.setParent_id(typeId);
        dataList.setStart(0);
        dataList.setPageNum(20);

        //判断list滚动
        mRecyclerView.setOnTouchListener(mRecyclerViewTouch);
        mLayoutManager=new LinearLayoutManager(myContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        listAdapter = new AreaListAdapter(dataList.getList(),selectId);
        listAdapter.setOnItemClickListener(AreaFragmentList.this);
        setFooterView(mRecyclerView);
        mRecyclerView.setAdapter(listAdapter);

        //第一次加载
        isLoading=false;
        loadType=1;
        BuildData();

        return view;
    }
    public void Reload(int parentId)
    {
        if(parentId!=typeId) {
            typeId = parentId;
            dataList.setParent_id(typeId);
            getType = 2;
            BuildData();
        }
        return;
    }
    private void  BuildData()
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
        areaRESTful.search(dataList,this);
    }
    @Override
    public void onRequestSuccess(String response) {
        try {
            Gson gson = new Gson();
            //处理结果
            BaseResult result=gson.fromJson(response,BaseResult.class);
            if (result.getErrcode() <= 0 && result.getType() == ActionType.AREA_SEARCH)
            {
                AreaList list = gson.fromJson(result.getData(), AreaList.class);
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
                    if(dataList.getStart()>=dataList.getCount()){
                        TextView footText=(TextView)listAdapter.getFooterView().findViewById(R.id.footText);
                        listAdapter.getFooterView().setVisibility(View.GONE);
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
        }
    }
    private void setFooterView(RecyclerView view){
        View footer = LayoutInflater.from(myContext).inflate(R.layout.foot, view, false);
        listAdapter.setFooterView(footer);
    }
    @Override
        public void onRequestError(Exception error) {
        //网络异常提示
        loadTextView.setText(myContext.getString(R.string.error_net));
        loadTextView.setVisibility(View.VISIBLE);
        isLoading=false;
        LogC.write(error,TAG);
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
    public  boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
    private ImageView oldSelcetView;
    @Override
    public void onItemClick(View view) {
        if(oldSelcetView!=null){
            oldSelcetView.setVisibility(View.INVISIBLE);
        }
        ImageView selectImg=(ImageView) view.findViewById(R.id.selectImg);
        selectImg.setVisibility(View.VISIBLE);
        oldSelcetView=selectImg;
        _mListener.onItemClick(view);
    }

}