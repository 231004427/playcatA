package com.sunlin.playcat.view;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.domain.Message;
import com.sunlin.playcat.domain.MessageType;

/**
 * Created by sunlin on 2017/10/22.
 */

public class ConfirmDialog extends DialogFragment implements View.OnClickListener {
    private LoadingDialog.OnClickListener listener;
    private CircleImageView imgHead;
    private TextView nameText;
    private TextView messText;
    private ImageView btnOk;
    private ImageView btnClose;
    private String messStr;
    private Handler mHandler = new Handler();

    public void setMessStr(String messStr) {
        this.messStr = messStr;
    }

    public String getMessStr() {
        return messStr;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width=(int)(ScreenUtil.getScreenWidthPixels(getActivity())*0.55);
        dialogWindow.setAttributes(lp);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int theme = 0;
        setStyle(DialogFragment.STYLE_NO_TITLE,theme);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_confirm, container);

        messText=(TextView)view.findViewById(R.id.messText);
        btnOk=(ImageView)view.findViewById(R.id.btnOk);
        btnClose=(ImageView)view.findViewById(R.id.btnClose);

        btnOk.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        if(!messStr.equals("")) {
            messText.setText(messStr);
        }
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOk:
                if(listener != null){
                    listener.onClick(2);
                }
                this.dismiss();
                break;
            case R.id.btnClose:
                if(listener != null){
                    listener.onClick(1);
                }
                this.dismiss();
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
