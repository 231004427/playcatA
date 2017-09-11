package com.sunlin.playcat;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.Address;
import com.sunlin.playcat.domain.AddressList;
import com.sunlin.playcat.domain.Area;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Goods;
import com.sunlin.playcat.domain.Order;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.json.AddressRESTful;
import com.sunlin.playcat.json.ObjRESTful;
import com.sunlin.playcat.json.OrderRESTful;
import com.sunlin.playcat.view.CircleTitleView;
import com.sunlin.playcat.view.LoadingDialog;
import com.sunlin.playcat.view.SelectCityDialog;

import java.util.Date;
import java.util.List;

public class GoodsBuyActivity extends MyActivtiyToolBar implements View.OnClickListener,RestTask.ResponseCallback{
    private String TAG="GoodsBuyActivity";
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
    private TextView valueSetText;
    private Button btnBuy;

    private int buyNum;
    private int payTotal;
    private CircleTitleView priceSum;
    private CircleTitleView priceTotal;

    private AddressRESTful addressRESTful ;
    private OrderRESTful orderRESTful;
    private int addressId=-2;

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
        valueSetText=(TextView)findViewById(R.id.valueSetText);

        rbBoyWeixin=(RadioButton)findViewById(R.id.rbBoyWeixin);
        rbBoyAli=(RadioButton)findViewById(R.id.rbBoyAli);
        rbBoyGold=(RadioButton)findViewById(R.id.rbBoyGold);
        rbBoyZhuan=(RadioButton)findViewById(R.id.rbBoyZhuan);

        subButton=(ImageButton)findViewById(R.id.subButton);
        addButton=(ImageButton)findViewById(R.id.addButton);
        zhuanText=(TextView)findViewById(R.id.zhuanText);
        goldText=(TextView)findViewById(R.id.goldText);
        btnUserValue=(ImageView)findViewById(R.id.btnUserValue);
        btnBuy=(Button)findViewById(R.id.btnBuy);

        //初始化支付方式
        setAllRB(false);

        //下单
        btnBuy.setOnClickListener(this);

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
                rbBoyAli.setChecked(false);
            }
        });
        rbBoyAli.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rbBoyWeixin.setChecked(false);
            }
        });

        //初始化布局
        switch (goods.getType())
        {
            case 1://购买钻石
                rbBoyAli.setChecked(true);
                priceSum.setmLeftBtnImage(R.drawable.cny_16);
                priceTotal.setmLeftBtnImage(R.drawable.cny_16);
                payGoldLayout.setVisibility(View.GONE);
                payZhuanLayout.setVisibility(View.GONE);
                break;
            case 2://兑换金币
                rbBoyZhuan.setChecked(true);
                priceSum.setmLeftBtnImage(R.drawable.zhuan16);
                priceTotal.setmLeftBtnImage(R.drawable.zhuan16);
                payAliLayout.setVisibility(View.GONE);
                payWeixinLayout.setVisibility(View.GONE);
                payGoldLayout.setVisibility(View.GONE);
                zhuanText.setText("(剩余:"+String.valueOf(user.getZhuan())+")");
                break;
            case 3://充值Q币
                rbBoyGold.setChecked(true);
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
                rbBoyGold.setChecked(true);
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
                rbBoyGold.setChecked(true);
                priceSum.setmLeftBtnImage(R.drawable.gold_2_16);
                priceTotal.setmLeftBtnImage(R.drawable.gold_2_16);
                payAliLayout.setVisibility(View.GONE);
                payWeixinLayout.setVisibility(View.GONE);
                payZhuanLayout.setVisibility(View.GONE);
                toUserTitleLayout.setVisibility(View.VISIBLE);
                toUserValueLayout.setVisibility(View.VISIBLE);
                toUserTitle.setText("收货地址");
                goldText.setText("(剩余:"+String.valueOf(user.getGold()+")"));
                getAddress();
                break;
        }
        //收货地址设置事件
        toUserValueLayout.setOnClickListener(this);
    }
    //
    private void setAllRB(boolean isCheck){
        //初始化支付方式
        rbBoyWeixin.setChecked(isCheck);
        rbBoyAli.setChecked(isCheck);
        rbBoyGold.setChecked(isCheck);
        rbBoyZhuan.setChecked(isCheck);
    }
    //获取收货地址
    private void getAddress()
    {

        addressRESTful=new AddressRESTful(user);
        AddressList addressList=new AddressList();
        addressList.setUser_id(user.getId());
        addressList.setStatus(1);
        addressList.setStart(0);
        addressList.setPageNum(1);
        loadingDialog.show();
        addressRESTful.search(addressList,this);

    }
    @Override
    protected void onResume() {
        super.onResume();
        //更新QQ、手机号信息
        if(goods.getType()==3){
            setValueAddress(user.getQq());
        }
        if(goods.getType()==4){
            setValueAddress(user.getPhone2());
        }
    }
    private void setValueAddress(String value){
        if(value!=null && !value.isEmpty()) {
            toUserValue.setText(value);
            valueSetText.setVisibility(View.GONE);
        }else{
            toUserValue.setText("");
            valueSetText.setVisibility(View.VISIBLE);
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
        switch (v.getId()) {
            case R.id.toUserValueLayout:
                //设置
                if (goods.getType() == 3 || goods.getType() == 4) {
                    Intent intent = new Intent(this, SetQPActivity.class);
                    intent.putExtra("type", goods.getType());
                    startActivity(intent);
                } else if (goods.getType() == 5 && addressId>=-1) {
                    Intent intent = new Intent(this, SetAddressActivity.class);
                    intent.putExtra("addressId", addressId);
                    startActivityForResult(intent, CValues.SET_ADDRESS);
                }
                break;
            case R.id.btnBuy:
                save();
                break;
        }
    }
    //
    //提交订单
    private void save(){

        String to_value=toUserValue.getText().toString();

        if(to_value=="") {
            if (goods.getType() == 3) {
                ShowMessage.taskShow(this, "请设置充值QQ号");
                return;
            }
            if (goods.getType() == 4) {
                ShowMessage.taskShow(this, "请设置充值QQ号");
                return;
            }
            if(goods.getType()==5){
                ShowMessage.taskShow(this,"请设置收货地址");
                return;
            }
        }
        Order order=new Order();
        order.setTo_value(to_value);
        order.setUser_id(user.getId());
        order.setGoods_id(goods.getId());
        order.setType(goods.getType());
        order.setNum(buyNum);
        order.setCreate_time(new Date());
        //在线支付方式,
        if(rbBoyAli.isChecked()){order.setPay_way(1);}
        if(rbBoyWeixin.isChecked()){order.setPay_way(2);}
        if(rbBoyGold.isChecked()){order.setPay_way(3);}
        if(rbBoyZhuan.isChecked()){order.setPay_way(4);}
        if(goods.getType()==1)
        {
            order.setTo_value("");

        }else{
            order.setTo_value(to_value);
        }

        orderRESTful=new OrderRESTful(user);
        orderRESTful.Add(order,this);
        loadingDialog.show();

    }
    //返回值
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //获取收货地址
        if (requestCode == CValues.SET_ADDRESS && data!=null)
        {
            addressId=data.getIntExtra("addressId",-1);
            setValueAddress(data.getStringExtra("address"));
        }

    }
    @Override
    public void onRequestSuccess(String response) {

        try{
            Gson gson=new Gson();
            BaseResult result=gson.fromJson(response, BaseResult.class);

            if(result!=null) {

                //获取到收货地址
                if (result.getErrcode() <= 0 && result.getType() == ActionType.ADDRESS_SEARCH){
                    AddressList addressList=gson.fromJson(result.getData(),AddressList.class);
                    if(addressList.getCount()>0) {
                        addressId=addressList.getAddressList().get(0).getId();
                        setValueAddress(addressList.getAddressList().get(0).getName()+
                                " "+addressList.getAddressList().get(0).getPhone()+
                                " "+addressList.getAddressList().get(0).getArea_name()+
                                " "+addressList.getAddressList().get(0).getAddress()+
                                " "+addressList.getAddressList().get(0).getPost_code());
                    }else{
                        addressId=-1;
                        setValueAddress("");
                    }
                    loadingDialog.dismiss();
                }
                if(result.getErrcode()<=0 && result.getType()==ActionType.ORDER_ADD){

                    //下单成功
                    loadingDialog.showText("下单成功",false,ContextCompat.getDrawable(this,R.drawable.smile),"返回");
                    loadingDialog.setOnClickListener(new LoadingDialog.OnClickListener() {
                        @Override
                        public void onClick(Dialog dialog, int type) {
                            if(type==2){
                                finish();
                            }
                        }
                    });
                }
                if(result.getErrcode()>0)
                {
                    //返回错误
                    ShowMessage.taskShow(this,result.getText());
                    loadingDialog.dismiss();
                }

            }else{
                //数据错误
                ShowMessage.taskShow(this,getString(R.string.error_server));
            }

        }catch (Exception e){
            LogC.write(e,TAG);
            ShowMessage.taskShow(this,getString(R.string.error_server));
        }
        }
    @Override
    public void onRequestError(Exception error) {
        ShowMessage.taskShow(this,this.getString(R.string.error_net));
    }
}
