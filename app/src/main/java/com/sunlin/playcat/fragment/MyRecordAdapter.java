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
import com.sunlin.playcat.domain.GamePlay;

import java.util.List;

/**
 * Created by sunlin on 2017/9/8.
 */

public class MyRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    //获取从Activity中传递过来每个item的数据集合
    private List<GamePlay> mDatas;
    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    private Context mContext;
    private Handler mHandler = new Handler();
    //构造函数
    public MyRecordAdapter(List<GamePlay> list){
        this.mDatas = list;
    }
    //构造函数
    public MyRecordAdapter(List<GamePlay> list, Context context){
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
            return new MyRecordAdapter.ListHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new MyRecordAdapter.ListHolder(mFooterView);
        }
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_user_game, parent, false);
        return new MyRecordAdapter.ListHolder(layout);
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof MyRecordAdapter.ListHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                GamePlay item;
                if(mHeaderView != null) {
                    item=mDatas.get(position - 1);
                }else{
                    item=mDatas.get(position);
                }
                ImageView gameImg=((ListHolder)holder).gameImg;
                ImageView imgLevel=((ListHolder)holder).imgLevel;
                TextView gameName=((ListHolder)holder).gameName;
                TextView levelName=((ListHolder)holder).levelName;
                TextView textPoints=((ListHolder)holder).textPoints;

                gameName.setTag(item.getGame_id());

                ImageWorker.loadImage(gameImg, CValues.SERVER_IMG+item.getGame_ico(),mHandler);
                gameName.setText(item.getGame_name());
                if(0<=item.getLevel()&& item.getLevel()<=10){
                    imgLevel.setImageResource(R.drawable.leve1_16);
                    levelName.setText("青铜("+item.getLevel()+")");
                }else if(11<=item.getLevel()&& item.getLevel()<=20){
                    imgLevel.setImageResource(R.drawable.leve2_16);
                    levelName.setText("黄金("+item.getLevel()+")");
                }else if(21<=item.getLevel()&& item.getLevel()<=30){
                    imgLevel.setImageResource(R.drawable.leve3_16);
                    levelName.setText("白银("+item.getLevel()+")");
                }else if(31<=item.getLevel()){
                    imgLevel.setImageResource(R.drawable.leve4_16);
                    levelName.setText("铂金("+item.getLevel()+")");
                }
                textPoints.setText("积分 "+item.getPoints());

                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER){
            return;
        }else{
            return;
        }
    }
    private MyRecordAdapter.OnItemClickListener mListener;
    public void setOnItemClickListener(MyRecordAdapter.OnItemClickListener listener) {
        mListener = listener;
    }
    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView gameImg,imgLevel;
        TextView gameName,levelName,textPoints;

        public ListHolder(View itemView) {
            super(itemView);
            //如果是headerview或者是footerview,直接返回
            if (itemView == mHeaderView){
                return;
            }
            if (itemView == mFooterView){
                return;
            }

            gameImg=(ImageView) itemView.findViewById(R.id.gameImg);
            gameName=(TextView)itemView.findViewById(R.id.gameName);
            imgLevel=(ImageView)itemView.findViewById(R.id.imgLevel);
            levelName=(TextView)itemView.findViewById(R.id.levelName);
            textPoints=(TextView)itemView.findViewById(R.id.textPoints);

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
