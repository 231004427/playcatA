package com.sunlin.playcat.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.common.ScreenUtil;

/**
 * Created by sunlin on 2017/7/3.
 */

public abstract class BottomPopView {
    private Context mContext;
    private View anchor;
    private LayoutInflater mInflater;
    private TextView mTvTop;
    private TextView mTvBottom;
    private TextView mTvCancel;
    private PopupWindow mPopupWindow;
    private LinearLayout backLayout;
    //WindowManager.LayoutParams params;
    WindowManager windowManager;
    Window window;

    /**
     * @param context
     * @param anchor  依附在哪个View下面
     */
    public BottomPopView(Activity context, View anchor) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.anchor = anchor;
        windowManager = context.getWindowManager();
        window = context.getWindow();
        //params = context.getWindow().getAttributes();
        init();
    }

    public void init() {
        View view = mInflater.inflate(R.layout.bottom_pop_window, null);
        //params.dimAmount = 1.0f;
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mTvBottom = (TextView) view.findViewById(R.id.tv_choose_photo);
        mTvTop = (TextView) view.findViewById(R.id.tv_take_photo);
        mTvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        mTvTop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                onTopButtonClick();
            }
        });
        mTvBottom.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                onBottomButtonClick();
            }
        });
        mTvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        mPopupWindow = new PopupWindow(view, (int)ScreenUtil.getScreenWidthDp(mContext), LinearLayout.LayoutParams.WRAP_CONTENT);
        //监听PopupWindow的dismiss，当dismiss时屏幕恢复亮度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //params.alpha = 1.0f;
                //window.setAttributes(params);
                //window.setWindowAnimations(CValues.style.backWindow_animation);
                //ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 1.0F,  0.0F).setDuration(500);//
                //anim.start();
                ObjectAnimator anim = ObjectAnimator.ofFloat(backLayout, "alpha", 0.6F,  0.0F).setDuration(500);
                anim.start();
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        ((ViewGroup)backLayout.getParent()).removeView(backLayout);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });
        mPopupWindow.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        // 动画效果 从底部弹起
        mPopupWindow.setAnimationStyle(R.style.popWindow_animation);


    }

    /**
     * 显示底部对话框
     */
    public void show() {
        mPopupWindow.showAtLocation(anchor, Gravity.BOTTOM, 0, 0);
        //params.alpha = 0.5f;
        //window.setAttributes(params);

        //显示背景
        backLayout = new LinearLayout(mContext);//主布局container
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        backLayout.setLayoutParams(vlp);
        backLayout.setBackgroundColor(mContext.getResources().getColor(R.color.black));
        window.addContentView(backLayout,vlp);

        ObjectAnimator anim = ObjectAnimator.ofFloat(backLayout, "alpha", 0.0F,  0.6F).setDuration(500);
        anim.start();
    }

    /**
     * 第一个按钮被点击的回调
     */
    public abstract void onTopButtonClick(

    );

    /**
     * 第二个按钮被点击的回调
     */
    public abstract void onBottomButtonClick(

    );

    public void setTopText(String text) {
        mTvTop.setText(text);
    }

    public void setBottomText(String text) {
        mTvBottom.setText(text);
    }
    public void dismiss(){
        if(mPopupWindow!=null && mPopupWindow.isShowing()){
            mPopupWindow.dismiss();
        }
    }
}