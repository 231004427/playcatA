package com.sunlin.playcat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.sunlin.playcat.R;
import com.sunlin.playcat.common.ScreenUtil;

/** * Created by wnw on 16-5-22. */

public class MyDecoration extends RecyclerView.ItemDecoration{

    private Paint mLinePaint;

    public MyDecoration(Context context){
        super();
        mLinePaint=new Paint(Paint.ANTI_ALIAS_FLAG);

        mLinePaint.setColor(ContextCompat.getColor(context,R.color.line));
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(1);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++){
            final View child = parent.getChildAt(i);

            //获得child的布局信息
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams)child.getLayoutParams();
            final int top = child.getBottom() + params.bottomMargin;
            c.drawLine(left,top,right,top,mLinePaint);
            //Log.d("wnw", left + " " + top + " "+right+"   "+bottom+" "+i);
        }
        super.onDraw(c, parent, state);
    }
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom =1;
    }
}