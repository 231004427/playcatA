package com.sunlin.playcat.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.Time;
import com.sunlin.playcat.domain.Message;
import com.sunlin.playcat.domain.User;

import java.util.Date;
import java.util.List;

/**
 * Created by sunlin on 2017/8/26.
 */

public class MessageListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    //获取从Activity中传递过来每个item的数据集合
    private List<Message> mDatas;
    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    private Context mContext;
    private Handler mHandler = new Handler();
    private User myUser;
    //构造函数
    public MessageListAdpter(List<Message> list,User _myUser){
        this.mDatas = list;
        this.myUser=_myUser;
    }
    //构造函数
    public MessageListAdpter(List<Message> list, Context context,User _myUser){
        this.mDatas = list;
        this.mContext=context;
        this.myUser=_myUser;
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
            return new MessageListAdpter.ListHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new MessageListAdpter.ListHolder(mFooterView);
        }
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_talk, parent, false);
        return new MessageListAdpter.ListHolder(layout);
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof MessageListAdpter.ListHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                Message info;
                int getNum=0;
                if(mHeaderView != null) {
                    getNum=position-1;
                }else{
                    getNum=position;
                }
                info=mDatas.get(getNum);


                ImageView imgHead_l=((ListHolder)holder).imgHead_l;
                ImageView imgHead_r=((ListHolder)holder).imgHead_r;
                TextView textView_r=((ListHolder)holder).textView_r;
                TextView textView_l=((ListHolder)holder).textView_l;
                TextView textTime=((ListHolder)holder).textTime;

                long diff=0;
                if(getNum!=0) {
                    diff=mDatas.get(getNum-1).getCreate_time().getTime()-info.getCreate_time().getTime();
                }else{
                    diff=new Date().getTime()-info.getCreate_time().getTime();
                }
                if(diff>(1000*60*5)) {
                    textTime.setVisibility(View.VISIBLE);
                    textTime.setText(Time.getTimeTalk(info.getCreate_time(), new Date()));
                }else{
                    textTime.setVisibility(View.GONE);
                }

                if(info.getFrom_user()==myUser.getId()){
                    imgHead_l.setVisibility(View.GONE);
                    textView_l.setVisibility(View.GONE);

                    imgHead_r.setVisibility(View.VISIBLE);
                    textView_r.setVisibility(View.VISIBLE);

                    //显示内容
                    textView_r.setText(info.getData());
                    //绑定头像
                    if(info.getFrom_user()==1){
                        imgHead_r.setImageResource(R.drawable.sys_m_45);
                    }else{
                        if(myUser.getPhoto()!=null&&!myUser.getPhoto().isEmpty()){
                            ImageWorker.loadImage(imgHead_r,CValues.SERVER_IMG+myUser.getPhoto(),mHandler);
                        }else{
                            imgHead_r.setImageResource(myUser.getSex()==1?R.mipmap.boy45:R.mipmap.girl45);
                        }
                    }

                }else{
                    imgHead_r.setVisibility(View.GONE);
                    textView_r.setVisibility(View.GONE);

                    imgHead_l.setVisibility(View.VISIBLE);
                    textView_l.setVisibility(View.VISIBLE);

                    //显示内容
                    if(info.getType()==1) {
                        textView_l.setText(info.getData());
                    }else if(info.getType()==2){
                        textView_l.setText(
                                Html.fromHtml(
                                        "<b>邀请好友:</b>  Text with a " +
                                                "<a href=\"http://www.google.com\">link</a> " +
                                                "created in the Java source code using HTML."));
                    }

                    //绑定头像
                    if(info.getFrom_user()==1){
                        imgHead_l.setImageResource(R.drawable.sys_m_45);
                    }else{
                        if(info.getFrom_photo()!=null){
                            ImageWorker.loadImage(imgHead_l,CValues.SERVER_IMG+info.getFrom_photo(),mHandler);
                        }else{
                            imgHead_l.setImageResource(myUser.getSex()==1?R.mipmap.boy45:R.mipmap.girl45);
                        }
                    }
                }

                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER){
            return;
        }else{
            return;
        }
    }
    private FriendListAdpter.OnItemClickListener mListener;
    public void setOnItemClickListener(FriendListAdpter.OnItemClickListener listener) {
        mListener = listener;
    }
    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgHead_l,imgHead_r;
        TextView textView_l,textView_r,textTime;

        public ListHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView){
                return;
            }
            if (itemView == mFooterView){
                return;
            }

            imgHead_l=(ImageView)itemView.findViewById(R.id.imgHead_l);
            imgHead_r=(ImageView)itemView.findViewById(R.id.imgHead_r);
            textView_r=(TextView)itemView.findViewById(R.id.textView_r);
            textView_l=(TextView)itemView.findViewById(R.id.textView_l);
            textTime=(TextView)itemView.findViewById(R.id.textTime);

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

