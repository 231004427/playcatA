package com.sunlin.playcat.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.DesignViewUtils;
import com.sunlin.playcat.common.DisplayUtil;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.common.Time;
import com.sunlin.playcat.domain.Message;
import com.sunlin.playcat.domain.MessageType;
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

                holder.itemView.setTag(info);

                ImageView imgHead_l=((ListHolder)holder).imgHead_l;
                ImageView imgHead_r=((ListHolder)holder).imgHead_r;
                LinearLayout layout_left=((ListHolder)holder).layout_left;
                layout_left.removeAllViews();
                LinearLayout layout_right=((ListHolder)holder).layout_right;
                layout_right.removeAllViews();
                TextView textTime=((ListHolder)holder).textTime;

                //android:text="你好啊！"
                //android:textColor="@color/textColor"
                //android:textSize="@dimen/text_m"

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
                    layout_left.setVisibility(View.GONE);

                    imgHead_r.setVisibility(View.VISIBLE);
                    layout_right.setVisibility(View.VISIBLE);
                    //显示内容
                    BuildView(layout_right,info);

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
                    layout_right.setVisibility(View.GONE);

                    imgHead_l.setVisibility(View.VISIBLE);
                    layout_left.setVisibility(View.VISIBLE);

                    //显示内容
                    BuildView(layout_left,info);

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
    private void BuildView(LinearLayout rootlayout,Message info){
        //显示内容
        if(info.getType()==MessageType.TEXT) {
            TextView textView = new TextView(mContext);
            LinearLayout.LayoutParams parent_params
                    = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            textView.setText(info.getData());
            textView.setTextColor(ContextCompat.getColor(mContext,R.color.textColor));
            rootlayout.addView(textView,parent_params);
        }else if(info.getType()== MessageType.ADD_FRIEND){

                        /*
                        RelativeLayout relativeLayout=new RelativeLayout(mContext);
                        LinearLayout.LayoutParams relativeLayout_parent_params
                                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);


                        ImageView imageView=new ImageView(mContext);
                        imageView.setId(R.id.imgHead);
                        RelativeLayout.LayoutParams image_parent_params
                                = new RelativeLayout.LayoutParams(DisplayUtil.dip2px(mContext,22),DisplayUtil.dip2px(mContext,22));
                        imageView.setImageResource(R.mipmap.boy45);

                        TextView textView = new TextView(mContext);
                        textView.setText("sunlin 邀请您成为朋友");
                        textView.setTextColor(ContextCompat.getColor(mContext,R.color.textColor));
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
                        RelativeLayout.LayoutParams textView_parent_params
                                = new RelativeLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        //textView_parent_params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                        textView_parent_params.addRule(RelativeLayout.RIGHT_OF,R.id.imgHead);
                        textView_parent_params.setMargins(DisplayUtil.dip2px(mContext,10),0,0,0);

                        relativeLayout.addView(imageView,image_parent_params);
                        relativeLayout.addView(textView,textView_parent_params);

                        layout_left.addView(relativeLayout,relativeLayout_parent_params);*/

            String[] dataFrom=info.getData().split("\\|");

            LayoutInflater inflater =  LayoutInflater.from(mContext);
            View viewInfo = inflater.inflate(R.layout.listview_message_addfriend, null);
            ImageView imageView=(ImageView)viewInfo.findViewById(R.id.imgHead);
            TextView textView = (TextView) viewInfo.findViewById(R.id.nameText);

            //显示头像
            if(dataFrom[2]!="null"){
                ImageWorker.loadImage(imageView,CValues.SERVER_IMG+dataFrom[2],mHandler);
            }else{
                imageView.setImageResource(dataFrom[3].equals("1")?R.mipmap.boy45:R.mipmap.girl45);
            }

            textView.setText(Html.fromHtml("<font color='#F5A623'>"+dataFrom[1]+"</font> 邀请您成为朋友 <font color='#4A90E2'>同意</>" ));
            rootlayout.addView(viewInfo);
        }
    }
    private MessageListAdpter.OnItemClickListener mListener;
    public void setOnItemClickListener(MessageListAdpter.OnItemClickListener listener) {
        mListener = listener;
    }
    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgHead_l,imgHead_r;
        LinearLayout layout_left,layout_right;
        TextView textTime;


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
            layout_right=(LinearLayout)itemView.findViewById(R.id.layout_right);
            layout_left=(LinearLayout)itemView.findViewById(R.id.layout_left);
            textTime=(TextView)itemView.findViewById(R.id.textTime);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mListener!=null){
                mListener.onItemClick(v,(Message)v.getTag());
            }
        }
    }
    public interface OnItemClickListener{
        void onItemClick(View view,Message data);
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

