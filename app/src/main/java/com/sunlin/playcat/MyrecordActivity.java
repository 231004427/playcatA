package com.sunlin.playcat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.MyActivtiyToolBar;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.CollectList;
import com.sunlin.playcat.domain.GamePlay;
import com.sunlin.playcat.domain.GamePlayList;
import com.sunlin.playcat.fragment.LoveListAdpter;
import com.sunlin.playcat.fragment.MyRecordAdapter;
import com.sunlin.playcat.json.CollectRESTful;
import com.sunlin.playcat.json.GamePlayRESTful;
import com.sunlin.playcat.view.CircleTitleView;
import com.sunlin.playcat.view.MyDecoration;

import java.util.ArrayList;
import java.util.List;

public class MyrecordActivity extends MyActivtiyToolBar implements MyRecordAdapter.OnItemClickListener,RestTask.ResponseCallback {
    private String TAG="MyrecordActivity";

    private RecyclerView mRecyclerView;
    private boolean isLoading=false;
    private LinearLayoutManager mLayoutManager;
    private int getType=1;
    private MyApp myApp;
    //需修改
    private GamePlayList dataList;
    //需修改
    private MyRecordAdapter listAdapter;
    private GamePlayRESTful gamePlayRESTful;
    private CircleTitleView loadTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        loadTextView = (CircleTitleView)findViewById(R.id.netoffText);

        ToolbarBuild("战绩",true,false);
        ToolbarBackListense();

        myApp=(MyApp)getApplication();
        gamePlayRESTful=new GamePlayRESTful(myApp.getUser());

        //点击重试
        loadTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuildData();
            }
        });

        //滚动绑定
        mRecyclerView.setOnTouchListener(mRecyclerViewTouch);

        //判断list点击
        //viewList[i].setBackgroundColor(ContextCompat.getColor(getActivity(), CValues.color.textColor_low));
        dataList=new GamePlayList();
        dataList.setCount(0);
        List<GamePlay> listData = new ArrayList<GamePlay>();
        dataList.setGamePlays(listData);
        dataList.setType(1);
        dataList.setId(myApp.getUser().getId());
        dataList.setStart(0);
        dataList.setPageNum(10);
        //使用单行
        mLayoutManager=new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //自定义分割线
        mRecyclerView.addItemDecoration(new MyDecoration(this));
        listAdapter = new MyRecordAdapter(dataList.getGamePlays());
        listAdapter.setOnItemClickListener(this);
        //setFooterView(mRecyclerView);
        mRecyclerView.setAdapter(listAdapter);


        //初始化加载
        isLoading=false;
        getType=1;
        BuildData();
    }
    //判断是否滑动到底部
    public  boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
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
            /*
              if(dataList.getStart()>=dataList.getCount()){
                TextView footText=(TextView)listAdapter.getFooterView().findViewById(R.id.footText);
                footText.setText(myContext.getString(R.string.nodata));
              }*/
        }
        if(getType==2){
            dataList.setStart(0);
            dataList.setCount(0);
        }
        //初始加载提示
        if(getType==1){
            loadTextView.setText(this.getString(R.string.loading));
            loadTextView.setVisibility(View.VISIBLE);
            //mRecyclerView.setVisibility(View.VISIBLE);
        }
        /*
        if(getType==3||getType==2) {
            TextView footText = (TextView) listAdapter.getFooterView().findViewById(R.id.footText);
            footText.setText(myContext.getString(R.string.nextpage));
        }*/

        gamePlayRESTful.searchUser(dataList,this);
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_myrecord;
    }

    @Override
    public void onRequestSuccess(String response) {
        try {
            Gson gson = new Gson();
            //处理结果
            BaseResult result=gson.fromJson(response,BaseResult.class);
            if (result.getErrcode() <= 0 && result.getType() == ActionType.GAME_PLAY_SEARCH_USER)
            {
                GamePlayList list = gson.fromJson(result.getData(), GamePlayList.class);
                if (list != null && list.getGamePlays().size() > 0) {
                    //初始化数据
                    if(getType==1 ||getType==2) {
                        dataList.setCount(list.getCount());
                        dataList.getGamePlays().clear();
                        dataList.getGamePlays().addAll(list.getGamePlays());
                        listAdapter.notifyDataSetChanged();
                    }
                    //分页数据
                    if(getType==3)
                    {
                        dataList.getGamePlays().addAll(list.getGamePlays());
                        listAdapter.notifyDataSetChanged();
                    }
                    //判断是否到页尾
                    int start=dataList.getStart();
                    int pageNum=dataList.getPageNum();
                    dataList.setStart(start+pageNum);
                    //隐藏加载提示
                    loadTextView.setVisibility(View.GONE);
                }else{
                    if(getType==1 ||getType==2) {
                        loadTextView.setVisibility(View.VISIBLE);
                        loadTextView.setText(this.getString(R.string.nodata_r));
                    }
                }
            }
            if(result.getErrcode() >0){
                ShowMessage.taskShow(this, result.getErrmsg());
                loadTextView.setText(getString(R.string.error_server));
            }
        }catch (Exception e)
        {
            LogC.write(e,TAG);
            ShowMessage.taskShow(this,getString(R.string.error_server));
            loadTextView.setText(getString(R.string.error_server));

        }finally {
            isLoading=false;
        }
    }

    @Override
    public void onRequestError(Exception error) {
        //网络异常提示
        loadTextView.setText(this.getString(R.string.error_net));
        loadTextView.setVisibility(View.VISIBLE);
        isLoading=false;
        LogC.write(error,TAG);
    }

    @Override
    public void onItemClick(View view) {

        TextView nameText=(TextView)view.findViewById(R.id.gameName);
        //查看详情
        Intent intent = new Intent(this,GameShowActivity.class);
        intent.putExtra("name", nameText.getText().toString());
        intent.putExtra("id",(int)nameText.getTag());
        startActivity(intent);
    }
}
