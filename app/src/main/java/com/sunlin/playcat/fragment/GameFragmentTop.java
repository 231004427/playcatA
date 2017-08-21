package com.sunlin.playcat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.sunlin.playcat.MyApp;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.GamePlay;
import com.sunlin.playcat.domain.GamePlayList;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.GamePlayRESTful;
import com.sunlin.playcat.view.CircleTitleView;
import com.sunlin.playcat.view.MyDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlin on 2017/7/29.
 */

public class GameFragmentTop extends Fragment implements GameTopListAdapter.OnItemClickListener {
    private String TAG="GameFragmentTop";
    int id;

    boolean isLoading=false;
    boolean isEnd=false;
    int getType=1;
    private CircleTitleView loadTextView;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private GameTopListAdapter listAdapter;
    private GamePlayList dataList;
    private GamePlayRESTful gamePlayRESTful;
    private User myUser;


    public static GameFragmentTop newInstance(int id) {

        Bundle args = new Bundle();
        args.putInt("id",id);
        GameFragmentTop fragment = new GameFragmentTop();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments() != null ? getArguments().getInt("id") : 0;

        myUser=((MyApp)getActivity().getApplication()).getUser();
        dataList=new GamePlayList();
        List<GamePlay> datas = new ArrayList<GamePlay>();
        dataList.setGamePlays(datas);
        dataList.setType(1);//排行榜数据
        dataList.setId(id);
        dataList.setStart(0);
        dataList.setPageNum(10);
        gamePlayRESTful=new GamePlayRESTful(myUser);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_top, null);

        //获取对象
        mRecyclerView=(RecyclerView)view.findViewById(R.id.mRecyclerView);
        loadTextView=(CircleTitleView)view.findViewById(R.id.loadTextView);

        //初始化列表
        mLayoutManager=new LinearLayoutManager(this.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        listAdapter = new GameTopListAdapter(dataList.getGamePlays());
        listAdapter.setOnItemClickListener(GameFragmentTop.this);
        //View footer = LayoutInflater.from(getContext()).inflate(R.layout.foot, mRecyclerView, false);
        //listAdapter.setFooterView(footer);
        mRecyclerView.setAdapter(listAdapter);
        //自定义分隔线
        mRecyclerView.addItemDecoration(new MyDecoration(getContext()));

        //初始化加载
        BuildData();

        return view;
    }

    private void BuildData()
    {
        if(isEnd || isLoading){return;}
        isLoading=true;
        //初始加载提示
        if(getType==1){
            loadTextView.setText(getContext().getString(R.string.loading));
            loadTextView.setVisibility(View.VISIBLE);
        }
        //重新加载
        if(getType==2){
            dataList.setStart(0);
            dataList.setCount(0);
        }
        //设置下拉刷新提示
        if(getType==3||getType==2) {
            //TextView footText = (TextView) listAdapter.getFooterView().findViewById(R.id.footText);
            //footText.setText(getContext().getString(R.string.nextpage));
        }
        gamePlayRESTful.search(dataList, new RestTask.ResponseCallback() {
            @Override
            public void onRequestSuccess(String response) {
                try {
                    Gson gson = new Gson();
                    //处理结果
                    BaseResult result=gson.fromJson(response,BaseResult.class);
                    if (result.getErrcode() <= 0 && result.getType() == ActionType.GAME_PLAY_SEARCH)
                    {
                        GamePlayList gameList = gson.fromJson(result.getData(), GamePlayList.class);
                        if (gameList != null && gameList.getGamePlays().size() > 0) {
                            //初始化数据
                            if(getType==1 || getType==2) {
                                dataList.setCount(gameList.getCount());
                                dataList.getGamePlays().clear();
                                dataList.getGamePlays().addAll(gameList.getGamePlays());
                                listAdapter.notifyDataSetChanged();
                            }
                            //分页数据
                            if(getType==3)
                            {
                                dataList.getGamePlays().addAll(gameList.getGamePlays());
                                listAdapter.notifyDataSetChanged();
                            }
                            //判断页尾
                            int start=dataList.getStart();
                            int pageNum=dataList.getPageNum();
                            dataList.setStart(start+pageNum);
                            if(dataList.getStart()>=dataList.getCount()){
                                //TextView footText=(TextView)listAdapter.getFooterView().findViewById(R.id.footText);
                                //footText.setText(getContext().getString(R.string.nodata));
                                isEnd=true;
                            }
                            //隐藏加载提示
                            loadTextView.setVisibility(View.GONE);
                        }else{
                            loadTextView.setText(getContext().getString(R.string.nodata_r));
                        }
                    }
                    if(result.getErrcode() >0){
                        ShowMessage.taskShow(getContext(), result.getErrmsg());
                    }
                }catch (Exception e)
                {
                    LogC.write(e,TAG);
                    ShowMessage.taskShow(getContext(),getString(R.string.error_server));

                }finally {
                    isLoading=false;
                }

            }
            @Override
            public void onRequestError(Exception error) {
                //网络异常提示
                loadTextView.setText(getContext().getString(R.string.error_net));
                loadTextView.setVisibility(View.VISIBLE);

                isLoading=false;
                LogC.write(error,TAG);
            }
        });
    }
    @Override
    public void onItemClick(View view) {

    }
}
