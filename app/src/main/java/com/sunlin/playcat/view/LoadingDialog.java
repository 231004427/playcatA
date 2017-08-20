package com.sunlin.playcat.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.common.ScreenUtil;

/**
 * Created by sunlin on 2017/6/29.
 */

public class LoadingDialog extends Dialog implements View.OnClickListener {

    private ImageView loading;
    private TextView loadTitle;
    private ImageView loadTitleImg;
    private Button btnAgain;

    private Context mContext;
    private String content;
    private OnClickListener listener;
    private String positiveName;
    private String negativeName;
    private String title="";
    private AnimationDrawable animLoading;
    private TextView cancelTitle;
    private boolean cancelable=false;
    private RelativeLayout backLayout;

    public LoadingDialog(Context context) {
        super(context,R.style.dialog);
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
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    //显示信息
    public void showText(String text, boolean isCancel, Drawable drawable,String btnStr){
        animLoading.stop();
        loading.setVisibility(View.INVISIBLE);
        loadTitle.setText(text);

        if(drawable!=null) {
            loadTitleImg.setBackground(drawable);
        }

        btnAgain.setText(btnStr);
        btnAgain.setVisibility(View.VISIBLE);

        loadTitle.setVisibility(View.VISIBLE);
        loadTitleImg.setVisibility(View.VISIBLE);
        cancelTitle.setVisibility(isCancel ? View.VISIBLE : View.INVISIBLE);
    }
    public void again(boolean isCancel)
    {
        loading.setVisibility(View.VISIBLE);
        btnAgain.setVisibility(View.INVISIBLE);
        loadTitle.setVisibility(View.INVISIBLE);
        loadTitleImg.setVisibility(View.INVISIBLE);
        cancelTitle.setVisibility(isCancel ? View.VISIBLE : View.INVISIBLE);
        animLoading.start();

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
        cancelTitle=(TextView)findViewById(R.id.cancelTitle);
        btnAgain=(Button)findViewById(R.id.btnAgain);
        loadTitleImg=(ImageView)findViewById(R.id.loadTitleImg);
        loadTitle=(TextView)findViewById(R.id.loadTitle);
        backLayout=(RelativeLayout)findViewById(R.id.backLayout);

        loading.setImageResource(R.drawable.loading);
        animLoading = (AnimationDrawable) loading.getDrawable();
        animLoading.start();
        cancelTitle.setOnClickListener(this);
        btnAgain.setOnClickListener(this);



        ViewGroup.LayoutParams lay=backLayout.getLayoutParams();
        lay.height= (int)ScreenUtil.getScreenHeightPixels(getContext());
        lay.width=(int)ScreenUtil.getScreenWidthPixels(getContext());
        //loadTitle.setLayoutParams(lay);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelTitle:
                if(listener != null){
                    listener.onClick(this, 1);
                }
                this.dismiss();
                break;
            case R.id.btnAgain:
                if(listener != null){
                    listener.onClick(this, 2);
                }
                break;
        }
    }
    public void setOnClickListener(LoadingDialog.OnClickListener listener)
    {
        this.listener=listener;
    }

    public interface OnClickListener{
        void onClick(Dialog dialog, int type);
    }
}
