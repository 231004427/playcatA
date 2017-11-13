package com.sunlin.playcat.fragment;

import android.content.Context;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.domain.Friend;

import java.util.List;

/**
 * Created by sunlin on 2017/8/19.
 */

public class FriendListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    //获取从Activity中传递过来每个item的数据集合
    private List<Friend> mDatas;
    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    private Context mContext;
    private Handler mHandler = new Handler();
    //构造函数
    public FriendListAdpter(List<Friend> list){
        this.mDatas = list;
    }
    //构造函数
    public FriendListAdpter(List<Friend> list, Context context){
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
            return new FriendListAdpter.ListHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new FriendListAdpter.ListHolder(mFooterView);
        }
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_friend, parent, false);
        return new FriendListAdpter.ListHolder(layout);
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof FriendListAdpter.ListHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                Friend info;
                if(mHeaderView != null) {
                    info=mDatas.get(position - 1);
                }else{
                    info=mDatas.get(position);
                }

                ImageView imgHead=((ListHolder)holder).imgHead;
                TextView nameText=((ListHolder)holder).nameText;
                TextView playStatus=((ListHolder)holder).playStatus;
                ImageView imgGameIco=((ListHolder)holder).imgGameIco;

                nameText.setText(info.getName());
                nameText.setTag(info);
                playStatus.setText(info.getPlay_status()==2?"正在玩":" 组队中");
                ColorMatrix matrix = new ColorMatrix();
                if(info.getOnline()==0) {
                    matrix.setSaturation(0);//饱和度 0灰色 100过度彩色，50正常
                }else{
                    matrix.setSaturation(1);//饱和度 0灰色 100过度彩色，50正常
                }
                ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
                imgHead.setColorFilter(filter);
                //绑定头像
                if(info.getPhoto()!=null||!info.getPhoto().isEmpty()){
                    ImageWorker.loadImage(imgHead, CValues.SERVER_IMG+info.getPhoto(),mHandler);
                }else{
                    imgHead.setImageResource(info.getSex()==1?R.mipmap.boy45:R.mipmap.girl45);
                }
                if(info.getPlay_status()==1){
                    playStatus.setText("");
                    imgGameIco.setVisibility(View.GONE);
                }else if(info.getPlay_status()==2)
                {
                    playStatus.setText("正在组队 "+info.getPlay_name());
                    ImageWorker.loadImage(imgGameIco, CValues.SERVER_IMG+info.getPlay_game_ico(),mHandler);
                }else if(info.getPlay_status()==3)
                {
                    playStatus.setText("正在玩 "+info.getPlay_name());
                    ImageWorker.loadImage(imgGameIco, CValues.SERVER_IMG+info.getPlay_game_ico(),mHandler);
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
        ImageView imgHead;
        TextView nameText,playStatus;
        ImageView imgGameIco;

        public ListHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView){
                return;
            }
            if (itemView == mFooterView){
                return;
            }

            imgHead=(ImageView)itemView.findViewById(R.id.imgHead);
            nameText=(TextView)itemView.findViewById(R.id.nameText);
            playStatus=(TextView)itemView.findViewById(R.id.playStatus);
            imgGameIco=(ImageView) itemView.findViewById(R.id.imgGameIco);

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
