package com.sunlin.playcat.view;

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
 * Created by sunlin on 2017/7/29.
 */

public class GameTopListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
public static final int TYPE_HEADER = 0;  //说明是带有Header的
public static final int TYPE_FOOTER = 1;  //说明是带有Footer的
public static final int TYPE_NORMAL = 2;  //说明是不带有header和footer的
//获取从Activity中传递过来每个item的数据集合
private List<GamePlay> mDatas;
//HeaderView, FooterView
private View mHeaderView;
private View mFooterView;
private Handler mHandler = new Handler();
//构造函数
public GameTopListAdapter(List<GamePlay> list){
        this.mDatas = list;
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
        return new ListHolder(mHeaderView);
        }
        if(mFooterView != null && viewType == TYPE_FOOTER){
        return new ListHolder(mFooterView);
        }
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_game_top, parent, false);
        return new ListHolder(layout);
        }

//绑定View，这里是根据返回的这个position的类型，从而进行绑定的，   HeaderView和FooterView, 就不同绑定了
@Override
public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(getItemViewType(position) == TYPE_NORMAL){
        if(holder instanceof ListHolder) {
        //这里加载数据的时候要注意，是从position-1开始，因为position==0已经被header占用了
            GamePlay info;
            int num=1;
        if(mHeaderView != null) {
        info=mDatas.get(position - 1);
            num=position;
        }else{
            info=mDatas.get(position);
            num=position+1;
        }
        ((ListHolder) holder).numText.setText(String.valueOf(num));
        ((ListHolder) holder).nameText.setText(info.getUser_name());
        ((ListHolder) holder).textPoints.setText(String.valueOf(info.getPoints()));
        if(1<=info.getLevel()&& info.getLevel()<=10){
        ((ListHolder) holder).imgLevel.setImageResource(R.drawable.leve1_16);
        }
        ImageWorker.loadImage(((ListHolder) holder).imgHead, CValues.SERVER_IMG+info.getUser_photo(),mHandler);

        return;
        }
        return;
        }else if(getItemViewType(position) == TYPE_HEADER){
        return;
        }else{
            return;
        }
}
private OnItemClickListener mListener;
public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
        }
//在这里面加载ListView中的每个item的布局
class ListHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    CircleImageView imgHead;
    ImageView imgLevel;
    TextView numText;
    TextView nameText;
    TextView textPoints;

    public ListHolder(View itemView) {
        super(itemView);
        //如果是headerview或者是footerview,直接返回
        if (itemView == mHeaderView){
            return;
        }
        if (itemView == mFooterView){
            return;
        }
        nameText = (TextView)itemView.findViewById(R.id.nameText);
        imgHead=(CircleImageView)itemView.findViewById(R.id.imgHead);
        imgLevel=(ImageView)itemView.findViewById(R.id.imgLevel);
        textPoints = (TextView)itemView.findViewById(R.id.textPoints);
        numText = (TextView)itemView.findViewById(R.id.numText);

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
