package com.sunlin.playcat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.domain.Goods;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.view.CircleTitleView;

public class GoodsBuyActivity extends MyActivtiyToolBar implements View.OnClickListener {
    private Goods goods;
    private ImageView goodsImg;
    private TextView goodsTitle;
    private TextView goodsNum;
    private TextView goodsNumTotal;
    private RadioButton rbBoyWeixin;
    private RadioButton rbBoyAli;
    private RadioButton rbBoyGold;
    private RadioButton rbBoyZhuan;
    private TextView phoneText;
    private TextView addressText;
    private LinearLayout payWeixinLayout;
    private LinearLayout payAliLayout;
    private LinearLayout payGoldLayout;
    private LinearLayout payZhuanLayout;
    private LinearLayout toUserTitleLayout;
    private LinearLayout toUserValueLayout;
    private TextView toUserTitle;
    private TextView toUserValue;
    private ImageButton subButton;
    private ImageButton addButton;
    private TextView zhuanText;
    private TextView goldText;
    private ImageView btnUserValue;


    private int buyNum;
    private int payTotal;
    private CircleTitleView priceSum;
    private CircleTitleView priceTotal;
    private User user;


    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        goodsTitle=(TextView)findViewById(R.id.goodsTitle);
        goodsImg=(ImageView)findViewById(R.id.goodsImg);
        goodsNum=(TextView)findViewById(R.id.goodsNum);
        goodsNumTotal=(TextView)findViewById(R.id.goodsNumTotal);
        priceSum=(CircleTitleView)findViewById(R.id.priceSum);
        priceTotal=(CircleTitleView)findViewById(R.id.priceTotal);
        payAliLayout=(LinearLayout)findViewById(R.id.payAliLayout);
        payGoldLayout=(LinearLayout)findViewById(R.id.payGoldLayout);
        payWeixinLayout=(LinearLayout)findViewById(R.id.payWeixinLayout);
        payZhuanLayout=(LinearLayout)findViewById(R.id.payZhuanLayout);
        toUserTitleLayout=(LinearLayout)findViewById(R.id.toUserTitleLayout);
        toUserValueLayout=(LinearLayout)findViewById(R.id.toUserValueLayout);
        toUserTitle=(TextView) findViewById(R.id.toUserTitle);
        toUserValue=(TextView)findViewById(R.id.toUserValue);

        rbBoyWeixin=(RadioButton)findViewById(R.id.rbBoyWeixin);
        rbBoyAli=(RadioButton)findViewById(R.id.rbBoyAli);
        rbBoyGold=(RadioButton)findViewById(R.id.rbBoyGold);
        rbBoyZhuan=(RadioButton)findViewById(R.id.rbBoyZhuan);

        subButton=(ImageButton)findViewById(R.id.subButton);
        addButton=(ImageButton)findViewById(R.id.addButton);
        zhuanText=(TextView)findViewById(R.id.zhuanText);
        goldText=(TextView)findViewById(R.id.goldText);
        btnUserValue=(ImageView)findViewById(R.id.btnUserValue);

        user=((MyApp)getApplication()).getUser();

        //获取传递数据
        Intent intent = this.getIntent();
        goods=(Goods) intent.getSerializableExtra("goods");
        //初始化导航栏
        ToolbarBuild(goods.getTitle(),true,false);
        ToolbarBackListense();

        //商品名称
        goodsTitle.setText(goods.getTitle());
        //购买数量
        buyNum=1;
        setGoodsNum();
        //单价
        priceSum.setText(String.valueOf(payTotal));
        //总价
        payTotal=goods.getPrice()*buyNum;
        priceTotal.setText(String.valueOf(payTotal));
        //商品图
        ImageWorker.loadImage(goodsImg, CValues.SERVER_IMG+goods.getHead_img(),mHandler);

        //赠减数量
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNum=buyNum-1;
                if(buyNum==0) {
                    buyNum = 1;
                    return;
                }
                setGoodsNum();
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buyNum=buyNum+1;
                if(goods.getStock()>0&&buyNum>goods.getStock()){
                    buyNum=buyNum-1;
                    return;
                }
                setGoodsNum();
            }
        });


        //支付选择事件
        rbBoyWeixin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rbBoyAli.setChecked(!isChecked);
            }
        });
        rbBoyAli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rbBoyWeixin.setChecked(!isChecked);
            }
        });

        //初始化布局
        toUserValue.setText("请设置");
        switch (goods.getType())
        {
            case 1://购买钻石
                priceSum.setmLeftBtnImage(R.drawable.cny_16);
                priceTotal.setmLeftBtnImage(R.drawable.cny_16);
                payGoldLayout.setVisibility(View.GONE);
                payZhuanLayout.setVisibility(View.GONE);
                break;
            case 2://兑换金币
                priceSum.setmLeftBtnImage(R.drawable.zhuan16);
                priceTotal.setmLeftBtnImage(R.drawable.zhuan16);
                payAliLayout.setVisibility(View.GONE);
                payWeixinLayout.setVisibility(View.GONE);
                payGoldLayout.setVisibility(View.GONE);
                zhuanText.setText("(剩余:"+String.valueOf(user.getZhuan())+")");
                break;
            case 3://充值Q币
                priceSum.setmLeftBtnImage(R.drawable.gold_2_16);
                priceTotal.setmLeftBtnImage(R.drawable.gold_2_16);
                payAliLayout.setVisibility(View.GONE);
                payWeixinLayout.setVisibility(View.GONE);
                payZhuanLayout.setVisibility(View.GONE);
                toUserTitleLayout.setVisibility(View.VISIBLE);
                toUserValueLayout.setVisibility(View.VISIBLE);
                toUserTitle.setText("充值QQ号");
                goldText.setText("(剩余:"+String.valueOf(user.getGold()+")"));
                break;
            case 4://充值手机
                priceSum.setmLeftBtnImage(R.drawable.gold_2_16);
                priceTotal.setmLeftBtnImage(R.drawable.gold_2_16);
                payAliLayout.setVisibility(View.GONE);
                payWeixinLayout.setVisibility(View.GONE);
                payZhuanLayout.setVisibility(View.GONE);
                toUserTitleLayout.setVisibility(View.VISIBLE);
                toUserValueLayout.setVisibility(View.VISIBLE);
                toUserTitle.setText("充值手机号");
                goldText.setText("(剩余:"+String.valueOf(user.getGold()+")"));
                break;
            case 5://兑换实物
                priceSum.setmLeftBtnImage(R.drawable.gold_2_16);
                priceTotal.setmLeftBtnImage(R.drawable.gold_2_16);
                payAliLayout.setVisibility(View.GONE);
                payWeixinLayout.setVisibility(View.GONE);
                payZhuanLayout.setVisibility(View.GONE);
                toUserTitleLayout.setVisibility(View.VISIBLE);
                toUserValueLayout.setVisibility(View.VISIBLE);
                toUserTitle.setText("收货地址");
                goldText.setText("(剩余:"+String.valueOf(user.getGold()+")"));
                break;
        }
        //设置收货值
        btnUserValue.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //更新收货人信息
        if(goods.getType()==3){
            if(user.getQq()!=null) {
                toUserValue.setText(user.getQq());
            }else{
                toUserValue.setTextColor(ContextCompat.getColor(this,R.color.textColor_low));
            }
        }
        if(goods.getType()==4){
            if(user.getPhone2()!=null) {
                toUserValue.setText(user.getPhone2());
            }else{
                toUserValue.setTextColor(ContextCompat.getColor(this,R.color.textColor_low));
            }
        }
        if(goods.getType()==5){
            if(user.getAddress()!=null) {
                toUserValue.setText(user.getAddress());
            }else{
                toUserValue.setTextColor(ContextCompat.getColor(this,R.color.textColor_low));
            }
        }

    }

    //购买数量设置
    private void setGoodsNum (){
        goodsNum.setText(String.valueOf(buyNum));
        goodsNumTotal.setText(String.valueOf(buyNum));

        payTotal=goods.getPrice()*buyNum;
        priceSum.setText(String.valueOf(payTotal));
        priceTotal.setText(String.valueOf(payTotal));

    }
    @Override
    protected int getLayoutResId() {
        return R.layout.activity_goods_buy;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnUserValue:
                //设置
                if(goods.getType()==3||goods.getType()==4){
                    Intent intent= new Intent(this, SetQPActivity.class);
                    intent.putExtra("type",goods.getType());
                    startActivity(intent);
                }else if(goods.getType()==5){
                    Intent intent= new Intent(this,SetAddressActivity.class);
                    intent.putExtra("type",goods.getType());
                    startActivity(intent);
                }
                break;
        }
    }
}
