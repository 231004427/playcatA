package com.sunlin.playcat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.MainActivity;
import com.sunlin.playcat.MyApp;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.UserRESTful;
import com.sunlin.playcat.view.CircleImageView;
import com.sunlin.playcat.view.CircleTitleView;

/**
 * Created by sunlin on 2017/7/19.
 */

public class IndexFragment  extends Fragment implements RestTask.ResponseCallback {
    String TAG="IndexFragment";
    String mName;
    private Context myContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyFragmentPageAdapter mAdapter;

    private CircleTitleView goldText;
    private CircleTitleView zhuanText;
    private CircleImageView imgHead;
    private TextView nameText;
    private UserRESTful userRESTful;
    private MyApp myApp;
    private Handler myHandle=new Handler();

    public static IndexFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name",name);
        IndexFragment fragment = new IndexFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mName = getArguments() != null ? getArguments().getString("name") : "Null";
        //初始化
        myContext=IndexFragment.this.getActivity();

    }

    @Override
    public void onResume() {
        super.onResume();
        //用户信息更新
        userRESTful.get(myApp.getUser(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, null);

        goldText=(CircleTitleView)view.findViewById(R.id.goldText);
        zhuanText=(CircleTitleView)view.findViewById(R.id.zhuanText);
        imgHead=(CircleImageView)view.findViewById(R.id.imgHead);
        nameText=(TextView)view.findViewById(R.id.nameText);

        myApp=(MyApp)getActivity().getApplication();
        userRESTful=new UserRESTful(myApp.getUser());

        mViewPager=(ViewPager)view.findViewById(R.id.viewPager);
        String[] titles=new String[]{"精选", "在线", "棋牌","益智", "动作", "小游戏"};
        int[] type=new int[]{1,2,3,4,5,6};
        FragmentManager fm=getChildFragmentManager();
        mAdapter=new MyFragmentPageAdapter(fm,titles,type);

        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(0);//设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
        mViewPager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));

        //TabLayout
        mTabLayout = (TabLayout)view.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    @Override
    public void onRequestSuccess(String response) {
        //处理结果
        Gson gson = new Gson();
        BaseResult result= gson.fromJson(response,BaseResult.class);
        try {
            if(result!=null) {
                if(result.getErrcode()<=0&&result.getType()== ActionType.USER_GET)
                {
                    User user=gson.fromJson(result.getData(),User.class);
                    myApp.setUser(user);//全局保存
                    zhuanText.setText(String.valueOf(user.getZhuan()));
                    goldText.setText(String.valueOf(user.getGold()));
                    nameText.setText(user.getName());

                    //绑定头像
                    if(user.getPhoto()!=null||!user.getPhoto().isEmpty()){
                        ImageWorker.loadImage(imgHead, CValues.SERVER_IMG+user.getPhoto(),myHandle);
                    }else{
                        imgHead.setImageResource(user.getSex()==1?R.mipmap.boy45:R.mipmap.girl45);
                    }

                }else
                {
                    ShowMessage.taskShow(myContext, result.getErrmsg());
                }
            }else{
                ShowMessage.taskShow(myContext,getString(R.string.error_server));
            }
        }catch (Exception e){
            LogC.write(e,TAG);
            ShowMessage.taskShow(myContext,getString(R.string.error_server));
        }
    }

    @Override
    public void onRequestError(Exception error) {
        ShowMessage.taskShow(myContext,getString(R.string.error_server));
    }
}
