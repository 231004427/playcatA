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
import com.sunlin.playcat.domain.Collect;
import com.sunlin.playcat.domain.Friend;
import com.sunlin.playcat.view.SlideLayout;

import java.util.List;

/**
 * Created by sunlin on 2017/9/6.
 */

public class LoveListAdpter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_HEADER = 0;  //说明是带有Header的
    public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
    public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
    //获取从Activity中传递过来每个item的数据集合
    private List<Collect> mDatas;
    //HeaderView, FooterView
    private View mHeaderView;
    private View mFooterView;
    private Context mContext;
    private Handler mHandler = new Handler();
    //构造函数
    public LoveListAdpter(List<Collect> list){
        this.mDatas = list;
    }
    //构造函数
    public LoveListAdpter(List<Collect> list, Context context){
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
            return new LoveListAdpter.ListHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
            return new LoveListAdpter.ListHolder(mFooterView);
        }
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_love, parent, false);
        return new LoveListAdpter.ListHolder(layout);
    }

    //绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
            if(holder instanceof LoveListAdpter.ListHolder) {
                //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
                final Collect info;
                if(mHeaderView != null) {
                    info=mDatas.get(position - 1);
                }else{
                    info=mDatas.get(position);
                }

                ImageView imgHead=((ListHolder)holder).imgHead;
                TextView nameText=((ListHolder)holder).nameText;
                TextView noteText=((ListHolder)holder).noteText;
                TextView menu=((ListHolder)holder).menu;

                ImageWorker.loadImage(imgHead, CValues.SERVER_IMG+info.getGame_ico(),mHandler);
                nameText.setText(info.getGame_name());
                noteText.setText(info.getGame_note());

                menu.setTag(info);
                menu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatas.remove(info);
                        notifyDataSetChanged();
                        //
                        mListener.onItemDel(v);
                    }
                });

                SlideLayout slideLayout = (SlideLayout) ((ListHolder)holder).itemView;
                slideLayout.setOnStateChangeListener(new MyOnStateChangeListener());

                return;
            }
            return;
        }else if(getItemViewType(position) == TYPE_HEADER){
            return;
        }else{
            return;
        }
    }
    private LoveListAdpter.OnItemClickListener mListener;

    public void setOnItemClickListener(LoveListAdpter.OnItemClickListener listener) {
        mListener = listener;
    }
    //在这里面加载ListView中的每个item的布局
    class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView imgHead;
        TextView nameText,noteText;
        TextView menu;

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
            noteText=(TextView)itemView.findViewById(R.id.noteText);
            menu=(TextView)itemView.findViewById(R.id.menu);

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
        void onItemDel(View view);
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
    //侧滑控制
    public SlideLayout slideLayout = null;
    class MyOnStateChangeListener implements SlideLayout.OnStateChangeListener
    {

        @Override
        public void onOpen(SlideLayout layout) {

            slideLayout = layout;
        }

        @Override
        public void onMove(SlideLayout layout) {
            if (slideLayout != null && slideLayout !=layout)
            {
                slideLayout.closeMenu();
            }
        }

        @Override
        public void onClose(SlideLayout layout) {
            if (slideLayout == layout)
            {
                slideLayout = null;
            }
        }
    }
}
