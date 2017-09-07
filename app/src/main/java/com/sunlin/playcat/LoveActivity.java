package com.sunlin.playcat;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Collect;
import com.sunlin.playcat.domain.CollectList;
import com.sunlin.playcat.domain.Friend;
import com.sunlin.playcat.domain.FriendList;
import com.sunlin.playcat.fragment.FriendFragment;
import com.sunlin.playcat.fragment.FriendListAdpter;
import com.sunlin.playcat.fragment.LoveListAdpter;
import com.sunlin.playcat.json.CollectRESTful;
import com.sunlin.playcat.json.FriendRESTful;
import com.sunlin.playcat.view.CircleTitleView;
import com.sunlin.playcat.view.MyDecoration;

import java.util.ArrayList;
import java.util.List;

public class LoveActivity extends MyActivtiyToolBar implements LoveListAdpter.OnItemClickListener,RestTask.ResponseCallback {
    private String TAG="LoveActivity";
    private Context myContext;
    private int typeId;
    private RecyclerView mRecyclerView;
    private boolean isLoading=false;
    private LinearLayoutManager mLayoutManager;
    private int getType=1;
    private MyApp myApp;
    //需修改
    private CollectList dataList;
    //需修改
    private LoveListAdpter listAdapter;
    private CollectRESTful collectRESTful;
    private CircleTitleView loadTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mRecyclerView = (RecyclerView)findViewById(R.id.my_recycler_view);
        loadTextView = (CircleTitleView)findViewById(R.id.netoffText);

        ToolbarBuild("收藏",true,false);
        ToolbarBackListense();

        myApp=(MyApp)getApplication();
        collectRESTful=new CollectRESTful(myApp.getUser());

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
        dataList=new CollectList();
        dataList.setUid(myApp.getUser().getId());
        dataList.setCount(0);
        List<Collect> listData = new ArrayList<Collect>();
        dataList.setList(listData);
        dataList.setType(1);
        dataList.setStatus(1);
        dataList.setStart(0);
        dataList.setPageNum(10);
        //使用单行
        mLayoutManager=new LinearLayoutManager(myContext);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //自定义分割线
        mRecyclerView.addItemDecoration(new MyDecoration(this));
        listAdapter = new LoveListAdpter(dataList.getList());
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

        collectRESTful.searchGame(dataList,this);
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_love;
    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onItemDel(View view) {

    }

    @Override
    public void onRequestSuccess(String response) {
        try {
            Gson gson = new Gson();
            //处理结果
            BaseResult result=gson.fromJson(response,BaseResult.class);
            if (result.getErrcode() <= 0 && result.getType() == ActionType.COLLECT_SARCH_GAME)
            {
                CollectList list = gson.fromJson(result.getData(), CollectList.class);
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
        }
    }

    @Override
    public void onRequestError(Exception error) {
        //网络异常提示
        loadTextView.setText(myContext.getString(R.string.error_net));
        loadTextView.setVisibility(View.VISIBLE);
        isLoading=false;
        LogC.write(error,TAG);
    }
}
