package com.sunlin.playcat;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.Gson;
import com.sunlin.playcat.MLM.MLMSocketDelegate;
import com.sunlin.playcat.MLM.MLMTCPClient;
import com.sunlin.playcat.MLM.MLMType;
import com.sunlin.playcat.MLM.MyHead;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Friend;
import com.sunlin.playcat.domain.FriendList;
import com.sunlin.playcat.domain.Message;
import com.sunlin.playcat.domain.MessageList;
import com.sunlin.playcat.fragment.MessageListAdpter;
import com.sunlin.playcat.fragment.TalkListAdpter;
import com.sunlin.playcat.json.MessageRESTful;
import com.sunlin.playcat.view.CircleImageView;
import com.sunlin.playcat.view.CircleTitleView;
import com.sunlin.playcat.view.LinearLayoutView;
import com.sunlin.playcat.view.MyDecoration;
import com.sunlin.playcat.view.MyLinearLayout;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageActivity extends MyActivtiyBase implements
        View.OnClickListener,RestTask.ResponseCallback,View.OnLayoutChangeListener,MLMSocketDelegate {
    private String TAG="MessageActivity";
    private Friend friend;
    private Handler myHandle;
    private CircleImageView imgHead;
    private ImageView btnSet;
    private ImageView btnBack;
    private TextView toolbar_title;
    private EditText commentEdit;
    private LinearLayout commentLayout;

    private RecyclerView mRecyclerView;
    private boolean isLoading=false;
    private LinearLayoutManager mLayoutManager;
    private int getType=1;
    private MyApp myApp;
    private int start=0;
    private MyLinearLayout root_layout;

    //需修改
    private MessageList dataList;
    private MessageListAdpter listAdapter;
    private MessageRESTful messageRESTful;
    private CircleTitleView loadTextView;

    //屏幕高度
    private int screenHeight = 0;
    //软件盘弹起后所占高度阀值
    private int keyHeight = 0;

    //TCP
    private MLMTCPClient server;
    private int buffSize=50000;
    private boolean isConnection=false;
    private Thread serverThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        imgHead=(CircleImageView)findViewById(R.id.imgHead);
        btnSet=(ImageView)findViewById(R.id.btnSet);
        btnBack=(ImageView)findViewById(R.id.btnBack);
        toolbar_title=(TextView)findViewById(R.id.toolbar_title);
        root_layout = (MyLinearLayout) findViewById(R.id.root_layout);
        commentEdit=(EditText)findViewById(R.id.commentEdit);
        commentLayout=(LinearLayout)findViewById(R.id.commentLayout);

        //输入框监控
        commentEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch (actionId){
                    case EditorInfo.IME_ACTION_SEND:
                        sendText();
                        break;
                }
                return false;
            }
        });

        //获取屏幕高度
        screenHeight = (int) ScreenUtil.getScreenHeightDp(this);
        //阀值设置为屏幕高度的1/3
        keyHeight = screenHeight/3;
        //软件键盘监控
        commentLayout.addOnLayoutChangeListener(this);

        //获取用户数据
        friend=(Friend) getIntent().getSerializableExtra("friend");

        toolbar_title.setText(friend.getName());
        //右侧头像显示
        if(friend.getPhoto()!=null) {
            ImageWorker.loadImage(imgHead, CValues.SERVER_IMG + friend.getPhoto(),myHandle);
        }else{
            imgHead.setImageResource(friend.getSex()==1?R.mipmap.boy45:R.mipmap.girl45);
        }
        imgHead.setVisibility(View.VISIBLE);
        btnSet.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        //初始化分页对象
        myApp=(MyApp) this.getApplication();
        messageRESTful=new MessageRESTful(myApp.getUser());
        //获取对象
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        loadTextView = (CircleTitleView) findViewById(R.id.netoffText);
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
        dataList=new MessageList();
        dataList.setFrom_user(myApp.getUser().getId());
        dataList.setTo_user(myApp.getUser().getId());
        dataList.setCount(0);
        List<Message> listData = new ArrayList<Message>();
        dataList.setList(listData);
        //使用单行
        mLayoutManager=new LinearLayoutManager(this, OrientationHelper.VERTICAL,true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        //自定义分割线
        //mRecyclerView.addItemDecoration(new MyDecoration(this));
        listAdapter = new MessageListAdpter(dataList.getList(),myApp.getUser());
        //listAdapter.setOnItemClickListener(this);
        //setFooterView(mRecyclerView);
        mRecyclerView.setAdapter(listAdapter);


        //初始化加载
        isLoading=false;

        //连接服务器
        startMLMThread();
    }

    private void startMLMThread() {
        serverThread = new Thread(new ServerThread());
        serverThread.start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        getType=1;
        BuildData();
    }

    //发送内容
    public void sendText(){
        if(isLoading){
            return ;
        }
        String text=commentEdit.getText().toString();

        if(text.isEmpty()){
            return;
        }
        isLoading=true;
        //发送聊天内容
        Message message=new Message();
        message.setFrom_user(myApp.getUser().getId());
        message.setTo_user(10013);
        message.setVesion(1);
        message.setType(1);//文本
        message.setLength(text.length());
        message.setData(text);
        message.setStatus(1);//1=未发送2=准备发送3=已发送4=已读
        message.setCreate_time(new Date());
        messageRESTful.add(message,this);
        commentEdit.setText("");

        //实时发送数据
        if(isConnection) {
            server.sendByUserIdStr("hello", 2);
        }

    }
    //判断是否滑动到底部
    public  boolean isSlideToBottom(RecyclerView recyclerView) {
        //Log.e(TAG,""+recyclerView.computeVerticalScrollExtent()+"-"+recyclerView.computeVerticalScrollOffset()+"-"+recyclerView.computeVerticalScrollRange());
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
    public boolean isSlideToTop(RecyclerView recyclerView){
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollOffset()==0)
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
                    Log.e("scroll","底部");
                    //getType=3;
                    //BuildData();
                }
                if(isSlideToTop(mRecyclerView))
                {
                    Log.e("scroll","顶部");
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
            if(start>=dataList.getCount()){
                isLoading=false;
                return;
            }
            dataList.setPageNum(5);
        }
        if(getType==2){
            dataList.setStart(0);
            dataList.setCount(0);
        }
        //初始加载提示
        if(getType==1){
            dataList.setStart(0);
            dataList.setCount(0);
            dataList.setPageNum(10);
            loadTextView.setText(this.getString(R.string.loading));
            loadTextView.setVisibility(View.VISIBLE);
            //mRecyclerView.setVisibility(View.VISIBLE);
        }
        /*
        if(getType==3||getType==2) {
            TextView footText = (TextView) listAdapter.getFooterView().findViewById(R.id.footText);
            footText.setText(myContext.getString(R.string.nextpage));
        }*/

        messageRESTful.search(dataList,this);
    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_message;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBack:
                finish();break;
            case R.id.btnSet:
                Intent intent=new Intent(this, FriendShowActivity.class);
                intent.putExtra("friend",friend);
                startActivity(intent);
                break;
        }
    }
    @Override
    public void onRequestSuccess(String response) {
        try {
            Gson gson = new Gson();
            //处理结果
            BaseResult result=gson.fromJson(response,BaseResult.class);
            if (result.getErrcode() <= 0 && result.getType() == ActionType.MESSAGE_SEARCH)
            {
                MessageList list = gson.fromJson(result.getData(), MessageList.class);
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
                        //int lastPostition=getLastVisiblePosition();
                        dataList.getList().addAll(list.getList());
                        listAdapter.notifyDataSetChanged();

                        //mRecyclerView.smoothScrollBy(0,500);
                        //lastPostition=list.getList().size()+lastPostition;
                        //mRecyclerView.smoothScrollToPosition(lastPostition);

                    }
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
                        loadTextView.setText(this.getString(R.string.nodata_r));
                    }
                }
            }
            if(result.getErrcode()<=0 && result.getType()==ActionType.MESSAGE_ADD){

                //发布成功
                Message message=gson.fromJson(result.getData(),Message.class);
                if(message !=null){
                    ArrayList listData  =(ArrayList)dataList.getList();
                    listData.add(0,message);
                    listAdapter.notifyDataSetChanged();
                    mRecyclerView.smoothScrollToPosition(dataList.getCount()-1);
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
    public int getLastVisiblePosition() {
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
            int lastItemPosition = linearManager.findLastVisibleItemPosition();
            return lastItemPosition;

        }

        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            int lastItemPosition = gridLayoutManager.findLastVisibleItemPosition();
            return lastItemPosition;

        }

        if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            int first[] = new int[staggeredGridLayoutManager.getSpanCount()];
            staggeredGridLayoutManager.findLastVisibleItemPositions(first);

            ArrayList<Integer> list = new ArrayList<>(first.length);
            if (list == null || list.size() == 0) {
                return -1;
            }
            return list.get(list.size() - 1);
        }

        return -1;

    }
    @Override
    public void onRequestError(Exception error) {
        //网络异常提示
        loadTextView.setText(this.getString(R.string.error_net));
        loadTextView.setVisibility(View.VISIBLE);
        isLoading=false;
        LogC.write(error,TAG);
    }
    //显示键盘
    private void  showInputMenthod()
    {
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        if(oldBottom != 0 && bottom != 0 &&(oldBottom - bottom > keyHeight)){

            ShowMessage.taskShow(this,"监听到软键盘弹起...");

        }else if(oldBottom != 0 && bottom != 0 &&(bottom - oldBottom > keyHeight)){

            ShowMessage.taskShow(this,"监听到软键盘关闭...");
        }
    }
    @Override
    public void MLMSocketResultAccess(int action, String data, MLMTCPClient sender) {

        if(action== MLMType.ACTION_USER_REGIST){

        }
    }
    @Override
    public void MLMSocketResultError(int action, int errorNum, String data, MLMTCPClient sender) {
        //服务器错误断开连接
        if(action==MLMType.ERROR_SYS_SERVER||
                action==MLMType.ERROR_SYS_SEND) {
            isConnection = false;
        }
        Log.e(TAG,"action:"+action+" errorNum:"+errorNum+" data:"+data);
    }
    @Override
    public void MLMGetMessage(MyHead myHead, byte[] data, MLMTCPClient sender) {
        try {
            String res = new String(data,"UTF-8");
            Log.e(TAG,"From("+myHead.from+"):"+res);
            //server.sendByUserIdStr("Hello too!",2);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private class ServerThread implements Runnable {
        @Override
        public void run() {
            try {
                //decodeLoop();
                server=new MLMTCPClient("user1",2);
                server.delegate=MessageActivity.this;
                server.connectServer(buffSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
