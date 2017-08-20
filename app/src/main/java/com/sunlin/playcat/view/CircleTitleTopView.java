package com.sunlin.playcat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunlin.playcat.R;

/**
 * Created by sunlin on 2017/7/27.
 */

public class CircleTitleTopView  extends RelativeLayout {

    // 返回按钮控件
    private ImageView mTopBtn;
    // 标题Tv
    private TextView mTitleTv;

    public CircleTitleTopView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.text_image_top, this);


        // 获取控件
        mTopBtn = (ImageView)findViewById(R.id.texImage_img);
        mTitleTv = (TextView)findViewById(R.id.texImage_txt);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleTitleView);
        if (attributes != null) {
            //先处理按钮
            int leftButtonDrawable = attributes.getResourceId(R.styleable.CircleTitleView_drawable_top,-1);
            if (leftButtonDrawable != -1) {
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), leftButtonDrawable, null);
                mTopBtn.setBackground(drawable);
            }else{
                mTopBtn.setVisibility(View.INVISIBLE);
            }
            //文本
            String leftButtonText = attributes.getString(R.styleable.CircleTitleView_text);
            //设置按钮文字颜色
            int textColor = attributes.getColor(R.styleable.CircleTitleView_text_color, Color.WHITE);
            mTitleTv.setTextColor(textColor);
            if (!TextUtils.isEmpty(leftButtonText)) {
                mTitleTv.setText(leftButtonText);
                float textSize = attributes.getDimension(R.styleable.CircleTitleView_text_size,0);
                mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
            }

        }
    }

    public void setLeftButtonListener(OnClickListener listener) {
        mTopBtn.setOnClickListener(listener);
    }
    //设置图片
    public void setImage(Drawable drawable){
        mTopBtn.setBackground(drawable);
    }
    // 设置标题的方法
    public void setText(String title) {
        mTitleTv.setText(title);
    }
}
