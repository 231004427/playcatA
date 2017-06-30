package com.sunlin.playcat.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunlin.playcat.R;

/**
 * Created by sunlin on 2017/6/29.
 */

public class LoadingDialog extends Dialog implements View.OnClickListener {

    private ImageView loading;

    private Context mContext;
    private String content;
    private CommomDialog.OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;
    private AnimationDrawable animLoading;

    public LoadingDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    public LoadingDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
    }

    public LoadingDialog(Context context, int themeResId, CommomDialog.OnCloseListener listener) {
        super(context, themeResId);
        this.mContext = context;
        this.listener = listener;
    }

    protected LoadingDialog(Context context, boolean cancelable, DialogInterface.OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.mContext = context;
    }

    public LoadingDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public LoadingDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public LoadingDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    public void dismiss() {
        animLoading.stop();
        super.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
        setCanceledOnTouchOutside(false);
        initView();
    }

    private void initView(){

        loading=(ImageView)findViewById(R.id.loading);

        loading.setImageResource(R.drawable.loading);
        animLoading = (AnimationDrawable) loading.getDrawable();
        animLoading.start();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                if(listener != null){
                    listener.onClick(this, false);
                }
                this.dismiss();
                break;
            case R.id.submit:
                if(listener != null){
                    listener.onClick(this, true);
                }
                break;
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, boolean confirm);
    }
}
