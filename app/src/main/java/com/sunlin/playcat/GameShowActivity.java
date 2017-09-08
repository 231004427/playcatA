package com.sunlin.playcat;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.sunlin.playcat.domain.BaseRequest;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Collect;
import com.sunlin.playcat.domain.Comment;
import com.sunlin.playcat.domain.Game;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.json.CollectRESTful;
import com.sunlin.playcat.json.RESTfulHelp;
import com.sunlin.playcat.json.CommentRESTful;
import com.sunlin.playcat.json.GameRESTful;
import com.sunlin.playcat.view.CircleTitleView;
import com.sunlin.playcat.fragment.GameFragmentPageAdapter;
import com.sunlin.playcat.view.LoadingDialog;

import java.util.Date;

public class GameShowActivity extends MyActivtiyToolBar implements View.OnClickListener,RestTask.ResponseCallback,View.OnLayoutChangeListener {
    private String TAG="GameShowActivity";

    private String name;
    private int id;
    private String note;

    private TextView nameText;
    private CircleTitleView onLineText;
    private ImageView gameImg;
    private LinearLayout gBtnTalk;
    private LinearLayout mainLayout;
    private LinearLayout commentLayout;
    private LinearLayout btnLayout;
    private Button commentBtn;
    private EditText commentEdit;
    private ImageView loveImg;


    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private GameFragmentPageAdapter mAdapter;
    private Handler mHandler = new Handler();

    /*
    private Toolbar toolbar;
    private TextView toolText;
    private ImageView toolBack;
    private ImageView toolSet;*/

    private GameRESTful gameRESTful;
    private CommentRESTful commentRESTful;
    private CollectRESTful collectRESTful;

    private AppBarLayout mAppBarLayout;
    private int totalAppBar;
    private boolean isCollect;
    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化对象
        nameText=(TextView)findViewById(R.id.nameText);
        onLineText=(CircleTitleView)findViewById(R.id.onLineText);
        gameImg=(ImageView)findViewById(R.id.gameImg);
        mTabLayout=(TabLayout)findViewById(R.id.tabs);
        mViewPager=(ViewPager)findViewById(R.id.viewPager);
        mAppBarLayout=(AppBarLayout)findViewById(R.id.mAppBarLayout);
        gBtnTalk=(LinearLayout)findViewById(R.id.gBtnTalk);
        mainLayout=(LinearLayout)findViewById(R.id.mainLayout);
        commentLayout=(LinearLayout)findViewById(R.id.commentLayout);
        btnLayout=(LinearLayout)findViewById(R.id.btnLayout);
        commentBtn=(Button) findViewById(R.id.commentBtn);
        commentEdit=(EditText)findViewById(R.id.commentEdit);
        loveImg=(ImageView)findViewById(R.id.loveImg);

        //请求信息
        gameRESTful=new GameRESTful(user);
        commentRESTful=new CommentRESTful(user);
        collectRESTful=new CollectRESTful(user);

        //收藏
        loveImg.setOnClickListener(this);

        //发布评论
        commentBtn.setOnClickListener(this);

        //获取屏幕高度
        screenHeight = (int)ScreenUtil.getScreenHeightDp(this);
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;

        //软件键盘监控
        mainLayout.addOnLayoutChangeListener(this);
        //评论
        gBtnTalk.setOnClickListener(this);

        //获取参数
        name=getIntent().getStringExtra("name");
        id=getIntent().getIntExtra("id",0);
        //name="飞行棋";
        //id=1;
        note=getIntent().getStringExtra("note");
        if(id==0||name=="")
        {
            ShowMessage.taskShow(this,getString(R.string.error_param));
            return;
        }
        //初始化导航栏;
        ToolbarBuild("",true,true);
        toolText = (TextView) findViewById(R.id.toolbar_title);
        toolText.setText(name);
        toolText.setVisibility(View.VISIBLE);
        toolBack.setImageResource(R.drawable.back_w22);
        toolSet.setImageResource(R.drawable.social_w22);

        toolbar.setBackgroundResource(R.drawable.backgournd_tab);
        toolbar.getBackground().setAlpha(0);
        //头部监控
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                //Log.e(TAG,"onOffsetChanged:"+i);
                totalAppBar=mAppBarLayout.getTotalScrollRange();

                int temp=-i/totalAppBar*100;
                //Log.e(TAG,"total:"+totalAppBar);

                toolbar.getBackground().setAlpha(temp);
                if(temp>50){
                    toolBack.setImageResource(R.drawable.back22);
                    toolSet.setImageResource(R.drawable.social22);
                    toolText.setVisibility(View.VISIBLE);


                }else {
                    toolBack.setImageResource(R.drawable.back_w22);
                    toolSet.setImageResource(R.drawable.social_w22);
                    toolText.setVisibility(View.GONE);
                }

            }
        });

        //显示对话框
        loadingDialog.setCancelable(true);
        loadingDialog.show();
        loadingDialog.setOnClickListener(new LoadingDialog.OnClickListener(){
            @Override
            public void onClick(Dialog dialog, int type) {
                if(type==2)
                {
                    loadingDialog.again(false);
                    gameRESTful.get(id,GameShowActivity.this);
                }
                if(type==1)
                {
                    loadingDialog.dismiss();
                    finish();
                }
            }
        });
        //加载数据
       gameRESTful.get(id,this);
    }
    //显示键盘
    private void  showInputMenthod()
    {
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_game_show;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.loveImg:
                //收藏
                Collect();
                break;
            case R.id.gBtnTalk:
                //添加评论
                showInputMenthod();
                break;
            case R.id.commentBtn:
                //提交评论
                showInputMenthod();
                String commnetStr=commentEdit.getText().toString();
                if(commnetStr.isEmpty()){
                    ShowMessage.taskShow(this,getResources().getString(R.string.input_comment_hint));
                }

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                //提交服务器
                Comment comment=new Comment();
                comment.setSid(id);
                comment.setRid(-1);
                comment.setUserId(user.getId());
                comment.setType(1);
                comment.setContent(commnetStr);
                comment.setGoodNum(0);
                comment.setReplyNum(0);
                comment.setCreateTime(new Date());
                comment.setStatus(1);

                commentRESTful.add(comment, new RestTask.ResponseCallback() {
                    @Override
                    public void onRequestSuccess(String response) {

                        RESTfulHelp.simpleWork(GameShowActivity.this,
                                response,ActionType.COMMENT_ADD,
                                GameShowActivity.this.getString(R.string.success_add),
                                GameShowActivity.this.getString(R.string.error_net));
                    }
                    @Override
                    public void onRequestError(Exception error) {
                        //数据错误
                        ShowMessage.taskShow(getApplicationContext(), GameShowActivity.this.getString(R.string.error_net));
                    }
                });
                break;
        }
    }
    private void Collect(){
        Collect collect=new Collect();
        collect.setUid(user.getId());
        collect.setSid(id);
        loadingDialog.show();
        if(isCollect){
            collectRESTful.del(collect,this);
            isCollect=false;
        }else{
            collect.setType(1);
            collect.setStatus(1);
            collect.setCreate_time(new Date());
            collectRESTful.add(collect,this);
            isCollect=true;
        }
    }
    @Override
    public void onRequestSuccess(String response) {

        try {
            //处理结果
            Gson gson = new Gson();
            BaseResult result=gson.fromJson(response, BaseResult.class);
            if(result!=null){
                //获取游戏信息
                if (result.getErrcode() <= 0 && result.getType() == ActionType.GAME_GET){
                    //显示图片
                    Game gameResult=gson.fromJson(result.getData(),Game.class);
                    ImageWorker.loadImage(gameImg, CValues.SERVER_IMG+gameResult.getImgBig(),mHandler);
                    onLineText.setText(gameResult.getOnlineNum()+"人");
                    //Tabs初始化
                    String[] titles=new String[]{"介绍", "排行榜","规则"};
                    FragmentManager fm=getSupportFragmentManager();
                    mAdapter=new GameFragmentPageAdapter(fm,titles,id,gameResult.getNote2());
                    mViewPager.setAdapter(mAdapter);
                    mViewPager.setOffscreenPageLimit(1);//设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
                    mViewPager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
                    mTabLayout.setupWithViewPager(mViewPager);
                    nameText.setText(name);
                    isCollect=(gameResult.getCollect()==0)?false:true;
                    loveImg.setImageResource(isCollect?R.drawable.love22_2:R.drawable.love22_1);
                }
                //收藏成功
                if (result.getErrcode() <= 0 && result.getType() == ActionType.COLLECT_ADD){
                    loveImg.setImageResource(R.drawable.love22_2);
                }
                //取消收藏
                if (result.getErrcode() <= 0 && result.getType() == ActionType.COLLECT_DEL){
                    loveImg.setImageResource(R.drawable.love22_1);
                }
                if(result.getErrcode()>0)
                {
                    //返回错误
                    ShowMessage.taskShow(GameShowActivity.this,result.getText());
                }

            }else{
                //数据错误
                ShowMessage.taskShow(GameShowActivity.this,getString(R.string.error_server));
            }

        }catch (Exception e){
        LogC.write(e,TAG);
        ShowMessage.taskShow(GameShowActivity.this,getString(R.string.error_server));}
        finally {
            loadingDialog.dismiss();
        }

    }

    @Override
    public void onRequestError(Exception error) {
        loadingDialog.showText(this.getString(R.string.error_net),true,null,"重新链接");
        //ShowMessage.taskShow(getApplicationContext(), this.getString(R.string.error_net));
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        //old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
        //Log.e(TAG,oldLeft + " " + oldTop +" " + oldRight + " " + oldBottom);
        // System.out.println(left + " " + top +" " + right + " " + bottom);
        //现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){

            //ShowMessage.taskShow(GameShowActivity.this,"监听到软键盘弹起...");
            commentLayout.setVisibility(View.VISIBLE);
            btnLayout.setVisibility(View.GONE);

        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){

            //ShowMessage.taskShow(GameShowActivity.this,"监听到软键盘关闭...");
            commentLayout.setVisibility(View.GONE);
            btnLayout.setVisibility(View.VISIBLE);

        }
    }
}
