package com.sunlin.playcat.view;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sunlin.playcat.R;
import com.sunlin.playcat.SetSysActivity;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.DownloadUtil;
import com.sunlin.playcat.common.FileHelp;
import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.common.SharedData;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.User;

import java.io.File;

/**
 * Created by sunlin on 2017/9/11.
 */

public class UpdateDialog extends DialogFragment implements DownloadUtil.OnDownloadListener {

    private String TAG="UpdateDialog";
    private ImageView closeImg;
    private Button btnOK,btnInstall;
    private MyProgressBar myProgressBar;
    private TextView messText,titleText;
    private int versionCode;
    private String title="";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.dialog_update, container);
        messText=(TextView)view.findViewById(R.id.messText);
        titleText=(TextView)view.findViewById(R.id.titleText);
        closeImg=(ImageView)view.findViewById(R.id.closeImg);
        btnOK=(Button)view.findViewById(R.id.btnOK);
        btnInstall=(Button)view.findViewById(R.id.btnInstall);
        myProgressBar=(MyProgressBar)view.findViewById(R.id.progressBar);
        myProgressBar.setProgress(0);
        titleText.setText(title);
        //设置安装按钮
        btnInstall.setVisibility(View.GONE);
        btnInstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //打开安装程序
                File file=new File(Environment.getExternalStorageDirectory() + CValues.DOWN_PATH_APP);
                if(file.exists()) {
                    FileHelp.openAPK(file, getActivity());
                }
            }
        });

        //设置确认按钮
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFile();
            }
        });
        //设置关闭按钮
        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //设置不允许取消
        if(!isCancelable()){
            closeImg.setVisibility(View.GONE);
        }

        //判断是否有下载好的版本
        File file=new File(Environment.getExternalStorageDirectory() + CValues.DOWN_PATH_APP);
        if(file.exists()) {
            //判断是否已经下载最新的安装包
            int loadVesion=SharedData.getDownVesion(getActivity());
            if(loadVesion>=versionCode){
                btnInstall.setVisibility(View.VISIBLE);
                btnOK.setVisibility(View.GONE);
                myProgressBar.setProgress(100);
                return view;
            }
        }
        //是否自动下载
        if(!isCancelable()){
            loadFile();
        }
        return view;
    }
    public void setVersionCode(int code){
        versionCode=code;
    }
    public void setTitle(String titleStr){
        title=titleStr;
    }
    private void loadFile(){
        btnOK.setEnabled(false);
        btnOK.setText("下载中");
        //下载文件
        DownloadUtil.get().download(CValues.UPDATE_URL,CValues.DOWN_PATH, this);
    }
    public void setProgress(int progress){
        myProgressBar.setProgress(progress);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        super.show(manager, tag);
    }

    @Override //在onCreate中设置对话框的风格、属性等
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        //setCancelable(true);
        //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
        int theme = 0;
        setStyle(DialogFragment.STYLE_NO_TITLE,theme);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(mListener!=null) {
            mListener.onItemClick(1);
        }
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
    Handler mHandlerLoad = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    btnInstall.setVisibility(View.VISIBLE);
                    btnOK.setVisibility(View.GONE);
                    SharedData.saveDownVesion(getActivity(),versionCode);
                    break;
                case 1:
                    setProgress((int)msg.obj);
                    break;
                case 2:
                    messText.setVisibility(View.VISIBLE);
                    messText.setText("网络异常,下载失败");
                    btnOK.setEnabled(true);
                    btnOK.setText("重新下载");
                    break;
                default:
                    break;
            }
        }

    };
    @Override
    public void onDownloadSuccess() {
        //ShowMessage.taskShow(SetSysActivity.this,"下载完成");
        Message message = new Message();
        message.what = 0;
        mHandlerLoad.sendMessage(message);
    }

    @Override
    public void onDownloading(int progress) {
        //progressBar.setProgress(progress);
        Message message = new Message();
        message.what = 1;
        message.obj=progress;
        mHandlerLoad.sendMessage(message);

    }

    @Override
    public void onDownloadFailed() {
        //ShowMessage.taskShow(SetSysActivity.this,"网络异常,下载失败");
        Message message = new Message();
        message.what = 2;
        mHandlerLoad.sendMessage(message);
    }

    private OnClickOkListener mListener;
    public void setOnClickOkListener(UpdateDialog.OnClickOkListener listener) {
        mListener = listener;
    }
    public interface OnClickOkListener{
        void onItemClick(int type);
    }
}
