package com.sunlin.playcat.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.sunlin.playcat.R;
import com.sunlin.playcat.common.ScreenUtil;

/**
 * Created by sunlin on 2017/8/12.
 */

public class SelectCityDialog extends Dialog implements View.OnClickListener {

    private TabLayout tabs;
    private Window window = null;
    private LinearLayout dialogBack;

    public SelectCityDialog(@NonNull Context context) {
        super(context,R.style.dialog);
    }

    public SelectCityDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected SelectCityDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int width = ScreenUtil.getScreenWidthPixels(this.getContext());
        int height = ScreenUtil.getScreenHeightPixels(this.getContext());
        //设置dialog的宽高为屏幕的宽高
        ViewGroup.LayoutParams layoutParams = new  ViewGroup.LayoutParams(width, height);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View viewDialog = inflater.inflate(R.layout.dialog_city, null);
        setContentView(viewDialog,layoutParams);
        setCanceledOnTouchOutside(true);
        //
        dialogBack=(LinearLayout) findViewById(R.id.dialogBack);
        tabs=(TabLayout)findViewById(R.id.tabs);
        dialogBack.setOnClickListener(this);
    }

    @Override
    public void show() {

        showDialog();
        super.show();
    }

    public void showDialog(){
        windowDeploy();
    }
    //设置窗口显示
    public void windowDeploy(){
        window = getWindow(); //得到对话框
        window.setWindowAnimations(R.style.dialogBottomAnim); //设置窗口弹出动画
        WindowManager.LayoutParams wl = window.getAttributes();
        //根据x，y坐标设置窗口需要显示的位置
        //            wl.alpha = 0.6f; //设置透明度
        //            wl.gravity = Gravity.BOTTOM; //设置重力
        window.setAttributes(wl);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.dialogBack:
                dismiss();
                break;
        }
    }
}
