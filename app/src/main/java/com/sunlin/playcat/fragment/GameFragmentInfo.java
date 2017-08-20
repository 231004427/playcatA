package com.sunlin.playcat.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.MyApp;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.common.Time;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Comment;
import com.sunlin.playcat.domain.CommentList;
import com.sunlin.playcat.json.CommentRESTful;
import com.sunlin.playcat.view.CircleImageView;
import com.sunlin.playcat.view.CircleTitleView;

import java.util.Date;

/**
 * Created by sunlin on 2017/7/23.
 */

public class GameFragmentInfo extends Fragment implements View.OnClickListener,RestTask.ResponseCallback {
    private String TAG="GameFragmentInfo";
    int id;
    String node;

    private TextView showMore;
    private TextView noteText;
    private Boolean showTag = true;
    private LinearLayout mListView;
    private NestedScrollView scrollView;
    private CommentList dataList;
    private CommentRESTful commentRESTful;
    private CircleTitleView nodataView;
    private Handler mHandler = new Handler();
    boolean isLoading=false;
    boolean isEnd=false;
    int loadType=0;
    public static GameFragmentInfo newInstance(int id,String node) {
        Bundle args = new Bundle();
        args.putInt("id",id);
        args.putString("note",node);
        GameFragmentInfo fragment = new GameFragmentInfo();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments() != null ? getArguments().getInt("id") : 0;
        node = getArguments() != null ? getArguments().getString("note") : "";

        //初始化
        MyApp app = (MyApp) this.getActivity().getApplicationContext();
        commentRESTful=new CommentRESTful(app.getUser());

    }

    private void  BuildData()
    {
        if(isEnd || isLoading){return;}
        isLoading=true;
        if(loadType==1){
            dataList=new CommentList();
            dataList.setSid(id);
            dataList.setUserId(0);
            dataList.setType(1);
            dataList.setStatus(1);
            dataList.setPageNum(3);
            dataList.setStart(0);
        }
        nodataView.setVisibility(View.VISIBLE);
        nodataView.setText(getContext().getString(R.string.loading));
        commentRESTful.search(dataList,this);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_info, null);
        //初始化对象
        showMore=(TextView)view.findViewById(R.id.showMore);
        noteText=(TextView)view.findViewById(R.id.noteText);
        mListView=(LinearLayout)view.findViewById(R.id.mListView);
        nodataView=(CircleTitleView)view.findViewById(R.id.nodataView);
        scrollView=(NestedScrollView)view.findViewById(R.id.scrollView);

        //显示介绍
        noteText.setText(node);
        showMore.setOnClickListener(this);
        nodataView.setOnClickListener(this);




        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                View view = (View)scrollView.getChildAt(scrollView.getChildCount()-1);
                int d = view.getBottom();
                d -= (scrollView.getHeight()+scrollView.getScrollY());
                if(d==0)
                {
                    //you are at the end of the list in scrollview
                    //Log.e(TAG,"到底部");
                    loadType=2;
                    BuildData();
                }
            }
        });

        //绑定事件
        isEnd=false;
        loadType=1;
        BuildData();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.showMore:
                ShowNote();
                break;
        }
    }
    private void ShowNote(){
        if(showTag){
            showTag = false;
            noteText.setEllipsize(null); // 展开
            noteText.setSingleLine(showTag);
        }else{
            showTag = true;
            noteText.setEllipsize(TextUtils.TruncateAt.END); // 收缩
            noteText.setLines(3);
        }
    }

    @Override
    public void onRequestSuccess(String response) {
        try{
            //处理结果
            Gson gson = new Gson();
            CommentList commentList=gson.fromJson(response, CommentList.class);
            if(commentList!=null){
                BaseResult result = commentList.getResult();
                if (result.getErrcode() <= 0 && result.getType() == ActionType.COMMENT_SEARCH){
                    dataList=commentList;
                    if(dataList.getComments().size()>0){
                        /*
                    mLayoutManager=new LinearLayoutManager(GameFragmentInfo.this.getContext());
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setHasFixedSize(true);
                    listAdapter = new CommentListAdapter(dataList.getComments());
                    mRecyclerView.setAdapter(listAdapter);*/
                        Comment item;
                        for (int i = 0; i < dataList.getComments().size(); i++) {
                            item=dataList.getComments().get(i);
                            ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT);
                            LayoutInflater inflater = LayoutInflater.from(getContext());
                            View view =inflater.inflate(R.layout.listview_item_comment,null);
                            TextView nameText=(TextView)view.findViewById(R.id.nameText);
                            TextView contentText=(TextView)view.findViewById(R.id.contentText);
                            TextView timeText=(TextView)view.findViewById(R.id.timeText);
                            ImageView imgLevel=(ImageView)view.findViewById(R.id.imgLevel);
                            CircleImageView imgHead=(CircleImageView)view.findViewById(R.id.imgHead);

                            nameText.setText(item.getName());
                            contentText.setText(item.getContent());

                            timeText.setText(Time.getTimeDifference(item.getCreateTime(),new Date()));

                            if(1<=item.getLevel()&& item.getLevel()<=10){
                                imgLevel.setImageResource(R.drawable.leve1_16);
                            }
                            ImageWorker.loadImage(imgHead, CValues.SERVER_IMG+item.getPhoto(),mHandler);
                            view.setLayoutParams(vlp);
                            mListView.addView(view);
                        }
                        //判断处理下一页
                        int start=dataList.getStart();
                        int pageNum=dataList.getPageNum();
                        dataList.setStart(start+pageNum);
                        if(dataList.getStart()>=dataList.getCount()){
                            nodataView.setVisibility(View.VISIBLE);
                            nodataView.setText(getContext().getString(R.string.nodata));
                            isEnd=true;

                        }
                    }else{
                        nodataView.setVisibility(View.GONE);
                    }
                }
            }

        }catch (Exception e)
        {
            LogC.write(e,TAG);
            ShowMessage.taskShow(getContext(),"系统错误");
        }finally {
            isLoading=false;
        }
    }
    @Override
    public void onRequestError(Exception error) {
        isLoading=false;
    }
}