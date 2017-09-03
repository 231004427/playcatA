package com.sunlin.playcat;

import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
    private TextView andressText;

    private ImageView btnSet;

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
        andressText=(TextView)findViewById(R.id.cityText);
        btnSet=(ImageView)findViewById(R.id.btnSet);

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
        btnSet.setOnClickListener(this);

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnSet:
                Intent intent=new Intent(this, SetMyActivity.class);
                startActivity(intent);
                break;
        }
    }
}
