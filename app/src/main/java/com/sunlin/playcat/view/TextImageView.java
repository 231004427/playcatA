package com.sunlin.playcat.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by sunlin on 2017/7/8.
 */

public class TextImageView extends RelativeLayout {
    private ImageView imageView;
    private TextView textView;
    public TextImageView(Context context) {
        this(context,null);
    }

    public TextImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TextImageView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);
        //创建子视图
        imageView=new ImageView(context,attrs,defStyleAttr);
        textView=new TextView(context,attrs,defStyleAttr);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER);

        //添加视图

        this.addView(imageView,params);
        this.addView(textView,params);

    }
    public void setText(CharSequence text)
    {
        textView.setText(text);
    }
    public void setImageResource(int resId){
        imageView.setImageResource(resId);
    }
    public void setImageDrawable(Drawable drawable){
        imageView.setImageDrawable(drawable);
    }
}
