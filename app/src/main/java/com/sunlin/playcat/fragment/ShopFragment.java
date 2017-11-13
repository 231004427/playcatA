package com.sunlin.playcat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sunlin.playcat.MyApp;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.GsonHelp;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.RESTfulHelp;
import com.sunlin.playcat.json.UserRESTful;
import com.sunlin.playcat.view.CircleTitleView;

/**
 * Created by sunlin on 2017/7/9.
 */

public class ShopFragment extends Fragment implements RestTask.ResponseCallback {
    private String TAG="ShopFragment";

    TextView title;
    String mName;
    private CircleTitleView goldText;
    private CircleTitleView zhuanText;
    private Context myContext;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ShopFragmentPageAdapter mAdapter;
    private UserRESTful userRESTful;
    private MyApp myApp;

    public static ShopFragment newInstance(String name) {

        Bundle args = new Bundle();
        args.putString("name",name);
        ShopFragment fragment = new ShopFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mName = getArguments() != null ? getArguments().getString("name") : "Null";
        //初始化
        myContext=ShopFragment.this.getActivity();


    }

    @Override
    public void onResume() {
        super.onResume();
        userRESTful.get(myApp.getUser(),this);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, null);

        title = (TextView) view.findViewById(R.id.toolbar_title);
        goldText=(CircleTitleView)view.findViewById(R.id.goldText);
        zhuanText=(CircleTitleView)view.findViewById(R.id.zhuanText);

        title.setText("兑换");

        mViewPager=(ViewPager)view.findViewById(R.id.viewPager);
        String[] titles=new String[]{"所有", "购买钻石", "兑换金币","Q币充值","手机充值","兑换奖品"};
        int[] type=new int[]{0,1,2,3,4,5};
        FragmentManager fm=getChildFragmentManager();
        mAdapter=new ShopFragmentPageAdapter(fm,titles,type);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(1);//设置缓存view 的个数（实际有3个，缓存2个+正在显示的1个）
        mViewPager.setPageMargin((int)getResources().getDimensionPixelOffset(R.dimen.ui_10_dip));
        //TabLayout
        mTabLayout = (TabLayout)view.findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mViewPager);


        //更新用户信息
        myApp=(MyApp) this.getActivity().getApplication();
        userRESTful=new UserRESTful(myApp.getUser());
        userRESTful.get(myApp.getUser(),this);
        return view;
    }

    @Override
    public void onRequestSuccess(String response) {
        //处理结果
        Gson gson= GsonHelp.getGsonObj();
        BaseResult result= gson.fromJson(response,BaseResult.class);
        try {
            if(result!=null) {
                if(result.getErrcode()<=0&&result.getType()== ActionType.USER_GET)
                {
                    User user=gson.fromJson(result.getData(),User.class);
                    myApp.setUser(user);
                    zhuanText.setText(String.valueOf(user.getZhuan()));
                    goldText.setText(String.valueOf(user.getGold()));

                }else
                {
                    ShowMessage.taskShow(getContext(), result.getErrmsg());
                }
            }else{
                ShowMessage.taskShow(getContext(),getString(R.string.error_server));
            }
        }catch (Exception e){
            LogC.write(e,TAG);
            ShowMessage.taskShow(getContext(),getString(R.string.error_server));
        }
    }

    @Override
    public void onRequestError(Exception error) {
        ShowMessage.taskShow(getContext(),getString(R.string.error_server));
    }
}

