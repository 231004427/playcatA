package com.sunlin.playcat;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Friend;
import com.sunlin.playcat.domain.GamePlay;
import com.sunlin.playcat.domain.GamePlayList;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.GamePlayRESTful;
import com.sunlin.playcat.view.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class FriendShowActivity extends MyActivtiyToolBar {
    private String TAG="FriendShowActivity";
    private Friend friend;
    private Handler mHandler = new Handler();

    private CircleImageView imgHead;
    private TextView nameText;
    private TextView cityText;
    private ImageView sexImg;
    private LinearLayout playTitleLayout;
    private LinearLayout playGameLayout;
    private GamePlayRESTful gamePlayRESTful;
    private User myUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化对象
        imgHead=(CircleImageView)findViewById(R.id.imgHead);
        nameText=(TextView)findViewById(R.id.nameText);
        cityText=(TextView)findViewById(R.id.cityText);
        sexImg=(ImageView)findViewById(R.id.sexImg);
        playTitleLayout=(LinearLayout)findViewById(R.id.playTitleLayout);
        playGameLayout=(LinearLayout)findViewById(R.id.playGameLayout);

        friend=(Friend) getIntent().getSerializableExtra("friend");
        ToolbarBuild(friend.getName(),true,false);
        ToolbarBackListense();

        //绑定数据
        nameText.setText(friend.getName());
        sexImg.setBackgroundResource(friend.getSex()==1?R.drawable.sex_1_16:R.drawable.sex_2_16);
        if(friend.getPhoto()!=null&&!friend.getPhoto().isEmpty()){
            ImageWorker.loadImage(imgHead, CValues.SERVER_IMG+friend.getPhoto(),mHandler);
        }else{
            imgHead.setImageResource(friend.getSex()==1?R.mipmap.boy45:R.mipmap.girl45);
        }
        if(friend.getCity()!=null&&!friend.getCity().isEmpty()){
            cityText.setText("城市: "+friend.getCity());
        }

        //加载战绩数据
        myUser=((MyApp)getApplication()).getUser();
        gamePlayRESTful=new GamePlayRESTful(myUser);
        loadingDialog.show();
        BuildData();
    }
    private void BuildData(){

        GamePlayList dataList;
        dataList=new GamePlayList();
        List<GamePlay> datas = new ArrayList<GamePlay>();
        dataList.setGamePlays(datas);
        dataList.setType(1);
        dataList.setId(friend.getFriend_id());
        dataList.setStart(0);
        dataList.setPageNum(5);
        gamePlayRESTful.searchUser(dataList, new RestTask.ResponseCallback() {
            @Override
            public void onRequestSuccess(String response) {
                try {
                    Gson gson = new Gson();
                    //处理结果
                    BaseResult result=gson.fromJson(response,BaseResult.class);
                    if (result.getErrcode() <= 0 && result.getType() == ActionType.GAME_PLAY_SEARCH_USER)
                    {
                        GamePlayList gameList = gson.fromJson(result.getData(), GamePlayList.class);
                        if (gameList != null && gameList.getGamePlays().size() > 0) {

                            playTitleLayout.setVisibility(View.VISIBLE);
                            playGameLayout.setVisibility(View.VISIBLE);
                            for(int i=0;i<gameList.getGamePlays().size();i++){
                                GamePlay item=gameList.getGamePlays().get(i);
                                //绑定数据
                                ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                                        ViewGroup.LayoutParams.MATCH_PARENT,
                                        (int) ScreenUtil.getScreenDensity(FriendShowActivity.this)*44);
                                LayoutInflater inflater = LayoutInflater.from(FriendShowActivity.this);
                                View view =inflater.inflate(R.layout.listview_user_game,null);
                                ImageView gameImg=(ImageView) view.findViewById(R.id.gameImg);
                                TextView gameName=(TextView)view.findViewById(R.id.gameName);
                                ImageView imgLevel=(ImageView)view.findViewById(R.id.imgLevel);
                                TextView levelName=(TextView)view.findViewById(R.id.levelName);
                                TextView textPoints=(TextView)view.findViewById(R.id.textPoints);

                                ImageWorker.loadImage(gameImg, CValues.SERVER_IMG+item.getGame_ico(),mHandler);
                                gameName.setText(item.getGame_name());
                                if(0<=item.getLevel()&& item.getLevel()<=10){
                                    imgLevel.setImageResource(R.drawable.leve1_16);
                                    levelName.setText("青铜("+item.getLevel()+")");
                                }else if(11<=item.getLevel()&& item.getLevel()<=20){
                                    imgLevel.setImageResource(R.drawable.leve2_16);
                                    levelName.setText("黄金("+item.getLevel()+")");
                                }else if(21<=item.getLevel()&& item.getLevel()<=30){
                                    imgLevel.setImageResource(R.drawable.leve3_16);
                                    levelName.setText("白银("+item.getLevel()+")");
                                }else if(31<=item.getLevel()){
                                    imgLevel.setImageResource(R.drawable.leve4_16);
                                    levelName.setText("铂金("+item.getLevel()+")");
                                }
                                textPoints.setText("积分 "+item.getPoints());

                                view.setLayoutParams(vlp);
                                playGameLayout.addView(view);
                            }

                        }
                    }
                    if(result.getErrcode() >0){
                        ShowMessage.taskShow(FriendShowActivity.this, result.getErrmsg());
                    }
                }catch (Exception e)
                {
                    LogC.write(e,TAG);
                    ShowMessage.taskShow(FriendShowActivity.this,getString(R.string.error_server));
                }finally {
                    loadingDialog.dismiss();
                }
            }
            @Override
            public void onRequestError(Exception error) {
                ShowMessage.taskShow(FriendShowActivity.this,getString(R.string.error_net));
            }
        });
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_friend_show;
    }
}
