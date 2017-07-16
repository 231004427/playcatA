package com.sunlin.playcat;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;

import com.sunlin.playcat.fragment.FriendFragment;
import com.sunlin.playcat.fragment.HomeFragment;
import com.sunlin.playcat.fragment.SetFragment;
import com.sunlin.playcat.fragment.ShopFragment;
import com.sunlin.playcat.fragment.TalkFragment;
import com.sunlin.playcat.view.CircleTitleView;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tb_home;
    private TextView tb_shop;
    private TextView tb_friend;
    private TextView tb_talk;
    private TextView tb_set;
    private int sIndex;

    private  String fragment1Tag="Home";
    private  String fragment2Tag="Shop";
    private  String fragment3Tag="Friend";
    private  String fragment4Tag="Talk";
    private  String fragment5Tag="Set";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //绑定对象
        tb_home=(TextView)findViewById(R.id.tab_home);
        tb_shop=(TextView)findViewById(R.id.tab_shop);
        tb_friend=(TextView)findViewById(R.id.tab_friend);
        tb_talk=(TextView)findViewById(R.id.tab_talk);
        tb_set=(TextView)findViewById(R.id.tab_set);

        tb_home.setOnClickListener(this);
        tb_shop.setOnClickListener(this);
        tb_friend.setOnClickListener(this);
        tb_talk.setOnClickListener(this);
        tb_set.setOnClickListener(this);


        //显示红点


        sIndex=0;
        switchFrgment(0);//设置默认显示Fragment
    }
    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */

    private void switchFrgment(int i){
        resetTabState();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment fragment1 = fm.findFragmentByTag(fragment1Tag);
        Fragment fragment2 = fm.findFragmentByTag(fragment2Tag);
        Fragment fragment3 = fm.findFragmentByTag(fragment3Tag);
        Fragment fragment4 = fm.findFragmentByTag(fragment4Tag);
        Fragment fragment5 = fm.findFragmentByTag(fragment5Tag);
        if (fragment1 != null) {
            ft.hide(fragment1);
        }
        if (fragment2 != null) {
            ft.hide(fragment2);
        }
        if (fragment3 != null) {
            ft.hide(fragment3);
        }
        if (fragment4 != null) {
            ft.hide(fragment4);
        }
        if (fragment5 != null) {
            ft.hide(fragment5);
        }
        switch (i)
        {
            case 0:
                setTabState(tb_home, R.drawable.home25,R.color.black);
                if(fragment1==null){
                    Fragment fragment = HomeFragment.newInstance("Home");
                    ft.add(R.id.layFrame, fragment,fragment1Tag);
                }else{
                    ft.show(fragment1);
                }
                break;
            case 1:
                setTabState(tb_shop, R.drawable.shop25,R.color.black);
                if(fragment2==null){
                    Fragment fragment = ShopFragment.newInstance("Shop");
                    ft.add(R.id.layFrame, fragment,fragment2Tag);
                }else{
                    ft.show(fragment2);
                }
                break;
            case 2:
                setTabState(tb_friend, R.drawable.friendly25,R.color.black);
                if(fragment3==null){
                    Fragment fragment = FriendFragment.newInstance("Friend");
                    ft.add(R.id.layFrame, fragment,fragment3Tag);
                }else{
                    ft.show(fragment3);
                }
                break;
            case 3:
                setTabState(tb_talk, R.drawable.chat25,R.color.black);
                if(fragment4==null){
                    Fragment fragment = TalkFragment.newInstance("Talk");
                    ft.add(R.id.layFrame, fragment,fragment4Tag);
                }else{
                    ft.show(fragment4);
                }
                break;
            case 4:
                setTabState(tb_set, R.drawable.profile25,R.color.black);
                if(fragment5==null){
                    Fragment fragment = SetFragment.newInstance("Set");
                    ft.add(R.id.layFrame, fragment,fragment5Tag);
                }else{
                    ft.show(fragment5);
                }
                break;
        }
        sIndex=i;
        ft.commit();
    }
    private void setTabState(TextView tabView,int image,int color){
        Drawable nav_up= ContextCompat.getDrawable(this,image);
        nav_up.setBounds(0, 0, nav_up.getMinimumWidth(), nav_up.getMinimumHeight());
        tabView.setCompoundDrawables(null, nav_up,null , null);
        tabView.setTextColor(ContextCompat.getColor(this,color));
    }
    private void resetTabState(){
        switch (sIndex){
            case 0:
                setTabState(tb_home, R.drawable.homeb25,R.color.textColor_low);
                break;
            case 1:
                setTabState(tb_shop, R.drawable.shopb25,R.color.textColor_low);
                break;
            case 2:
                setTabState(tb_friend, R.drawable.friendlyb25,R.color.textColor_low);
                break;
            case 3:
                setTabState(tb_talk, R.drawable.chatb25,R.color.textColor_low);
                break;
            case 4:
                setTabState(tb_set, R.drawable.profileb25,R.color.textColor_low);
                break;
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_home:
                switchFrgment(0);//切换Fragment
                break;
            case R.id.tab_shop:
                switchFrgment(1);//切换Fragment
                break;
            case R.id.tab_friend:
                switchFrgment(2);//切换Fragment
                break;
            case R.id.tab_talk:
                switchFrgment(3);//切换Fragment
                break;
            case R.id.tab_set:
                switchFrgment(4);//切换Fragment
                break;
        }
    }
}
