package com.sunlin.playcat.fragment;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.domain.Order;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by sunlin on 2017/9/8.
 */

public class OrderListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    //获取从Activity中传递过来每个item的数据集合
    private List<Order> mDatas;
    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    private Context mContext;
    private Handler mHandler = new Handler();
    //构造函数
    public OrderListAdapter(List<Order> list){
        this.mDatas = list;
    }
    //构造函数
    public OrderListAdapter(List<Order> list, Context context){
        this.mDatas = list;
        this.mContext=context;
    }

    //HeaderView和FooterView的get和set函数
    public View getHeaderView() {
        return mHeaderView;
    }
    public void setHeaderView(View headerView) {
        mHeaderView = headerView;
        notifyItemInserted(0);
    }
    public View getFooterView() {
        return mFooterView;
    }
    public void setFooterView(View footerView) {
        mFooterView = footerView;
        notifyItemInserted(getItemCount()-1);
    }

    /** 重写这个方法，很重要，是加入Header和Footer的关键，我们通过判断item的类型，从而绑定不同的view    * */
    @Override
    public int getItemViewType(int position) {
        if (mHeaderView == null && mFooterView == null){
            return TYPE_NORMAL;
        }
        if (mHeaderView != null && position == 0){
            //第一个item应该加载Header
            return TYPE_HEADER;
        }
        if (mFooterView != null && position == getItemCount()-1){
            //最后一个,应该加载Footer
            return TYPE_FOOTER;
        }
        return TYPE_NORMAL;
    }

    //创建View，如果是HeaderView或者是FooterView，直接在Holder中返回
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(mHeaderView != null && viewType == TYPE_HEADER) {
            return new OrderListAdapter.ListHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new OrderListAdapter.ListHolder(mFooterView);
        }
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_order, parent, false);
        return new OrderListAdapter.ListHolder(layout);
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof OrderListAdapter.ListHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                Order info;
                if(mHeaderView != null) {
                    info=mDatas.get(position - 1);
                }else{
                    info=mDatas.get(position);
                }
                ImageView goodsImg=((ListHolder) holder).goodsImg;
                ImageView imgPay=((ListHolder) holder).imgPay;
                TextView orderNum = ((ListHolder) holder).orderNum;
                TextView orderStatus = ((ListHolder) holder).orderStatus;
                TextView goodsTitle = ((ListHolder) holder).goodsTitle;
                TextView goodsNum = ((ListHolder) holder).goodsNum;
                TextView payTotal = ((ListHolder) holder).payTotal;
                TextView timeText = ((ListHolder) holder).timeText;

                ImageWorker.loadImage(goodsImg, CValues.SERVER_IMG+info.getGoods_img(),mHandler);
                switch (info.getPay_way()){
                    case 1:imgPay.setImageResource(R.drawable.cny_16);
                        DecimalFormat decimalFormat=new DecimalFormat(".00");
                        payTotal.setText(decimalFormat.format(info.getPrice()));
                        break;
                    case 2:
                        imgPay.setImageResource(R.drawable.zhuan16);
                        payTotal.setText(String.valueOf((int)info.getPrice()));
                        break;
                    case 3:
                    case 4:
                    case 5:
                        imgPay.setImageResource(R.drawable.gold_2_16);
                        payTotal.setText(String.valueOf((int)info.getPrice()));
                        break;
                }
                orderNum.setText("订单编号 "+String.valueOf(info.getId()));
                //1=待支付,2=已支付，3=已发货，4=已完成
                String statusStr="";
                switch (info.getStatus()){
                    case 1:statusStr="待支付";break;
                    case 2:statusStr="已支付";break;
                    case 3:statusStr="已发货";break;
                    case 4:statusStr="已完成";break;
                }
                orderStatus.setText(statusStr);
                goodsTitle.setText(info.getGoods_title());
                goodsNum.setText("数量 "+String.valueOf(info.getNum()));
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                timeText.setText(dateFormat.format(info.getCreate_time()));

                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER){
            return;
        }else{
            return;
        }
    }
    private OrderListAdapter.OnItemClickListener mListener;
    public void setOnItemClickListener(OrderListAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView goodsImg,imgPay;
        TextView orderNum,orderStatus,goodsTitle,goodsNum,payTotal,timeText;

        public ListHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView){
                return;
            }
            if (itemView == mFooterView){
                return;
            }
            goodsImg=(ImageView)itemView.findViewById(R.id.goodsImg);
            imgPay=(ImageView)itemView.findViewById(R.id.imgPay);
            orderNum = (TextView)itemView.findViewById(R.id.orderNum);
            orderStatus = (TextView)itemView.findViewById(R.id.orderStatus);
            goodsTitle = (TextView)itemView.findViewById(R.id.goodsTitle);
            goodsNum = (TextView)itemView.findViewById(R.id.goodsNum);
            payTotal = (TextView)itemView.findViewById(R.id.payTotal);
            timeText=(TextView)itemView.findViewById(R.id.timeText);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.onItemClick(v);
            }
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view);
    }
    //返回View中Item的个数，这个时候，总的个数应该是ListView中Item的个数加上HeaderView和FooterView
    @Override
    public int getItemCount() {
        if(mHeaderView == null && mFooterView == null){
            return mDatas.size();
        }else if(mHeaderView == null && mFooterView != null){
            return mDatas.size() + 1;
        }else if (mHeaderView != null && mFooterView == null){
            return mDatas.size() + 1;
        }else {
            return mDatas.size() + 2;
        }
    }
}
