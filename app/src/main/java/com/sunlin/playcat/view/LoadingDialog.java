package com.sunlin.playcat.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.domain.BaseRequest;

/**
 * Created by sunlin on 2017/6/29.
 */

public class LoadingDialog extends DialogFragment implements View.OnClickListener {

    private ImageView loading;
    private TextView loadTitle;
    private ImageView loadTitleImg;
    private Button btnAgain;
    private OnClickListener listener;
    private AnimationDrawable animLoading;
    private TextView cancelTitle;

    private Boolean isCancel=false;
    private String title;
    private String btnStr="重试";
    private Drawable loadImg;
    public Boolean isAgain=false;
    public Boolean isShow=false;

    public void setBtnStr(String _btnStr){
        btnStr=_btnStr;
    }
    public void setTitle(String _title){
        title=_title;
    }
    public void setLoadImg(Drawable _loadImg){
        loadImg=_loadImg;
    }
    public void setIsCancel(boolean _isCancel){
        isCancel=_isCancel;
    }
    @Override
    public void show(FragmentManager manager, String tag) {


        if(!isAgain) {
            if(!isShow) {
                super.show(manager, tag);
                isShow = true;
            }

        }else{
            if(tag=="loading"){
                showLoading();
            }else{
                showRepeat();
            }
        }
    }
    public void showRepeat(){
            if(animLoading!=null) {
                animLoading.stop();
                loading.setVisibility(View.GONE);
            }
            if(title!=""){
                loadTitle.setText(title);
                loadTitle.setVisibility(View.VISIBLE);
            }
            if(loadImg!=null) {
                loadTitleImg.setBackground(loadImg);
            }
            loadTitleImg.setVisibility(View.VISIBLE);
            btnAgain.setText(btnStr);
            btnAgain.setVisibility(View.VISIBLE);
            cancelTitle.setVisibility(isCancel ? View.VISIBLE : View.GONE);
    }
    public void showLoading()
    {
        loading.setVisibility(View.VISIBLE);
        btnAgain.setVisibility(View.GONE);
        loadTitle.setVisibility(View.GONE);
        loadTitleImg.setVisibility(View.GONE);
        cancelTitle.setVisibility(View.GONE);
        animLoading.start();

    }

    @Override
    public void dismiss() {
        animLoading.stop();
        super.dismiss();
        isAgain=false;
        isShow=false;
    }
    @Override
    public void onResume() {
        super.onResume();

        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width=(int)(ScreenUtil.getScreenWidthPixels(getActivity())*0.75);
        dialogWindow.setAttributes(lp);

        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        //setCancelable(true);
        //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
        int theme = 0;
        setStyle(DialogFragment.STYLE_NO_TITLE,theme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_loading, container);

        loading=(ImageView)view.findViewById(R.id.loading);
        cancelTitle=(TextView)view.findViewById(R.id.cancelTitle);
        btnAgain=(Button)view.findViewById(R.id.btnAgain);
        loadTitleImg=(ImageView)view.findViewById(R.id.loadTitleImg);
        loadTitle=(TextView)view.findViewById(R.id.loadTitle);

        loading.setImageResource(R.drawable.loading);
        animLoading = (AnimationDrawable) loading.getDrawable();
        animLoading.start();
        cancelTitle.setOnClickListener(this);
        btnAgain.setOnClickListener(this);

        if(getTag()=="repeat"){
            showRepeat();
        }
        //ViewGroup.LayoutParams lay=backLayout.getLayoutParams();
        //lay.height= (int)ScreenUtil.getScreenHeightPixels(getContext())+ScreenUtil.getStatusHeight(getContext());
        //lay.width=(int)ScreenUtil.getScreenWidthPixels(getContext());
        //loadTitle.setLayoutParams(lay);

        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelTitle:
                if(listener != null){
                    listener.onClick(1);
                }
                this.dismiss();
                break;
            case R.id.btnAgain:
                isAgain=true;
                if(listener != null){
                    listener.onClick(2);
                }
                showLoading();
                break;
        }
    }
    public void setOnClickListener(LoadingDialog.OnClickListener listener)
    {
        this.listener=listener;
    }

    public interface OnClickListener{
        void onClick(int type);
    }
}
