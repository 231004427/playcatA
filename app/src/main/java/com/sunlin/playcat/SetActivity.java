package com.sunlin.playcat;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.view.CircleImageView;

public class SetActivity extends MyActivtiyToolBar implements View.OnClickListener {

    private User user;
    private Handler mHandler = new Handler();

    private CircleImageView imgHead;
    private TextView nameText;
    private ImageView sexImg;

    private TextView goldText;
    private TextView zuanText;
    private TextView loveText;
    private TextView orderText;
    private TextView cityText;

    private ImageView btnSet;
    private ImageView btnZuan;
    private ImageView btnLove;

    private RelativeLayout btnSetLayout;
    private LinearLayout payGoldLayout;
    private LinearLayout payZhuanLayout;
    private LinearLayout loveLayout;
    private LinearLayout fightLayout;
    private LinearLayout shopLayout;
    private LinearLayout addressLayout;
    private LinearLayout sysLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolbarBuild("我",true,false);
        ToolbarBackListense();

        imgHead=(CircleImageView)findViewById(R.id.imgHead);
        nameText=(TextView)findViewById(R.id.nameText);
        sexImg=(ImageView)findViewById(R.id.sexImg);
        goldText=(TextView)findViewById(R.id.goldText);
        zuanText=(TextView)findViewById(R.id.zuanText);
        loveText=(TextView)findViewById(R.id.loveText);
        orderText=(TextView)findViewById(R.id.orderText);
        cityText=(TextView)findViewById(R.id.cityText);

        btnSet=(ImageView)findViewById(R.id.btnSet);
        btnZuan=(ImageView)findViewById(R.id.btnZuan);
        btnLove=(ImageView)findViewById(R.id.btnLove);

        btnSetLayout=(RelativeLayout)findViewById(R.id.btnSetLayout);
        payGoldLayout=(LinearLayout)findViewById(R.id.payGoldLayout);
        payZhuanLayout=(LinearLayout)findViewById(R.id.payZhuanLayout);
        loveLayout=(LinearLayout)findViewById(R.id.loveLayout);
        fightLayout=(LinearLayout)findViewById(R.id.fightLayout);
        shopLayout=(LinearLayout)findViewById(R.id.shopLayout);
        addressLayout=(LinearLayout)findViewById(R.id.addressLayout);
        sysLayout=(LinearLayout)findViewById(R.id.sysLayout);

        //获取用户信息
        user=((MyApp)getApplication()).getUser();
        //绑定数据
        nameText.setText(user.getName());
        sexImg.setBackgroundResource(user.getSex()==1?R.drawable.sex_1_16:R.drawable.sex_2_16);
        if(user.getPhoto()!=null&&!user.getPhoto().isEmpty()){
            ImageWorker.loadImage(imgHead, CValues.SERVER_IMG+user.getPhoto(),mHandler);
        }else{
            imgHead.setImageResource(user.getSex()==1?R.mipmap.boy45:R.mipmap.girl45);
        }
        goldText.setText("("+user.getGold()+")");
        zuanText.setText("("+user.getZhuan()+")");
        loveText.setText(String.valueOf(user.getLoveNum()));
        orderText.setText(String.valueOf(user.getOrderNum()));
        cityText.setText(String.valueOf(user.getCity()));

        //绑定事件
        btnSetLayout.setOnClickListener(this);
        payZhuanLayout.setOnClickListener(this);
        loveLayout.setOnClickListener(this);
        fightLayout.setOnClickListener(this);
        shopLayout.setOnClickListener(this);
        addressLayout.setOnClickListener(this);
        sysLayout.setOnClickListener(this);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSetLayout:
                Intent intent;
                intent=new Intent(this, SetMyActivity.class);
                startActivity(intent);
                break;
            case R.id.payZhuanLayout:
                Intent intent2;
                intent2=new Intent(this,MainActivity.class);
                intent2.putExtra("sIndex",1);
                startActivity(intent2);
                break;
            case R.id.loveLayout:
                Intent intent3=new Intent(this,LoveActivity.class);
                startActivity(intent3);
                break;
            case R.id.fightLayout:
                Intent intent4=new Intent(this,MyrecordActivity.class);
                startActivity(intent4);
                break;
            case R.id.shopLayout:
                Intent intent5=new Intent(this,OrderListActivity.class);
                startActivity(intent5);
                break;
            case R.id.addressLayout:
                Intent intent6=new Intent(this,SetAddressActivity.class);
                startActivity(intent6);
                break;
            case R.id.sysLayout:
                Intent intent7=new Intent(this,SetSysActivity.class);
                startActivity(intent7);
        }
    }
}
