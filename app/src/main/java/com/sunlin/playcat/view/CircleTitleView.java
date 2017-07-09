package com.sunlin.playcat.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunlin.playcat.R;

/**
 * Created by sunlin on 2017/7/9.
 */

public class CircleTitleView extends RelativeLayout {

    // 返回按钮控件
    private ImageView mLeftBtn;
    // 标题Tv
    private TextView mTitleTv;

    public CircleTitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.text_image, this);

        // 获取控件
        mLeftBtn = (ImageView)findViewById(R.id.texImage_img);
        mTitleTv = (TextView)findViewById(R.id.texImage_txt);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.CircleTitleView);
        if (attributes != null) {
            //先处理左边按钮
            int leftButtonDrawable = attributes.getResourceId(R.styleable.CircleTitleView_drawable_left,-1);
            if (leftButtonDrawable != -1) {
                Drawable drawable = ResourcesCompat.getDrawable(getResources(), leftButtonDrawable, null);
                mLeftBtn.setBackground(drawable);
            }else{
                mLeftBtn.setVisibility(View.INVISIBLE);
            }
            //文本
            String leftButtonText = attributes.getString(R.styleable.CircleTitleView_text);
            if (!TextUtils.isEmpty(leftButtonText)) {
                mTitleTv.setText(leftButtonText);
                //设置左边按钮文字颜色
                int textColor = attributes.getColor(R.styleable.CircleTitleView_text_color, Color.WHITE);
                mTitleTv.setTextColor(textColor);
                float textSize = attributes.getDimension(R.styleable.CircleTitleView_text_size,0);
                mTitleTv.setTextSize(TypedValue.COMPLEX_UNIT_PX,textSize);
            }
        }
    }

    // 为左侧返回按钮添加自定义点击事件
    public void setLeftButtonListener(OnClickListener listener) {
        mLeftBtn.setOnClickListener(listener);
    }

    // 设置标题的方法
    public void setTitleText(String title) {
        mTitleTv.setText(title);
    }
}
