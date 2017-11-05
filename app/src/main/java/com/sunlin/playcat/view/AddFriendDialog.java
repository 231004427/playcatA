package com.sunlin.playcat.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sunlin.playcat.MyApp;
import com.sunlin.playcat.R;
import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.ImageWorker;
import com.sunlin.playcat.common.LogC;
import com.sunlin.playcat.common.PhoneFormatCheckUtils;
import com.sunlin.playcat.common.RestTask;
import com.sunlin.playcat.common.ScreenUtil;
import com.sunlin.playcat.common.ShowMessage;
import com.sunlin.playcat.domain.ActionType;
import com.sunlin.playcat.domain.Area;
import com.sunlin.playcat.domain.BaseResult;
import com.sunlin.playcat.domain.Friend;
import com.sunlin.playcat.domain.Message;
import com.sunlin.playcat.domain.MessageType;
import com.sunlin.playcat.domain.User;
import com.sunlin.playcat.fragment.FriendFragment;
import com.sunlin.playcat.json.FriendRESTful;
import com.sunlin.playcat.json.MessageRESTful;
import com.sunlin.playcat.json.UserRESTful;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sunlin on 2017/8/19.
 */

public class AddFriendDialog extends DialogFragment implements RestTask.ResponseCallback {
    private String TAG="AddFriendDialog";
    private ImageView closeImg;
    private EditText inputText;
    private TextView nameText;
    private CircleImageView imgHead;
    private Button btnOK;
    private Button btnNext;
    private Button btnPrev;
    private TextView messText;
    private Handler mHandler = new Handler();

    private int type=1;
    private User friendUser;
    private User myUser;
    private UserRESTful userRESTful;
    private MessageRESTful messageRESTful;

    private Boolean isLoading=false;

    public void setUser(User _friendUser){
        friendUser=_friendUser;
    }

    public void setType(int _type){
        type=_type;
    }
    private void buildAction()
    {
        if(type==1){
            inputText.setVisibility(View.VISIBLE);
            nameText.setVisibility(View.GONE);
            imgHead.setVisibility(View.GONE);
            btnNext.setVisibility(View.VISIBLE);
            btnPrev.setVisibility(View.GONE);
            btnOK.setVisibility(View.GONE);
        }
        //下一步
        if(type==2)
        {
            if(isLoading){return;}
            isLoading=true;
            messText.setVisibility(View.GONE);
            //查询用户信息
            userRESTful.get(friendUser,this);
        }
        // 提交服务器
        if(type==3){
            if(mListener!=null) {
                mListener.onItemClick(friendUser);
            }else {
                if(isLoading)return;
                isLoading=true;
                messText.setVisibility(View.GONE);
                /*
                Friend friend=new Friend();
                friend.setFriend_id(friendUser.getId());
                friend.setUser_id(myUser.getId());
                friend.setCreate_time(new Date());
                friend.setStatus(2);
                friend.setGroup_id(-1);
                friend.setType(-1);
                friendRESTful.insert(friend,this);*/
                Message message = new Message();
                message.setFrom_user(1);
                message.setTo_user(friendUser.getId());
                message.setType(MessageType.ADD_FRIEND);
                message.setFrom_name("");
                message.setVesion(1);
                message.setLength(0);
                message.setData(String.valueOf(myUser.getId()));//邀请人
                message.setStatus(1);//1=未发送2=准备发送3=已发送4=已读
                message.setCreate_time(new Date());
                messageRESTful.addFriend(message,this);

            }
        }

    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_add_friend, container);
        closeImg=(ImageView)view.findViewById(R.id.closeImg);
        inputText=(EditText)view.findViewById(R.id.inputText);
        nameText=(TextView)view.findViewById(R.id.nameText);
        imgHead=(CircleImageView)view.findViewById(R.id.imgHead);
        btnOK=(Button)view.findViewById(R.id.btnOK);
        btnNext=(Button)view.findViewById(R.id.btnNext);
        btnPrev=(Button)view.findViewById(R.id.btnPrev);
        messText=(TextView)view.findViewById(R.id.messText);

        closeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        myUser=((MyApp)getActivity().getApplication()).getUser();
        userRESTful=new UserRESTful(myUser);
        messageRESTful=new MessageRESTful(myUser);

        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=3;
                buildAction();
            }
        });

        friendUser=new User();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //判断输入框
                String inputStr=inputText.getText().toString();
                if(inputStr.isEmpty()){

                    messText.setText("请输入内容");
                    messText.setVisibility(View.VISIBLE);
                    return;
                }
                friendUser.setId(-1);
                friendUser.setPhone("");
                friendUser.setName("");
                if(PhoneFormatCheckUtils.isChinaPhoneLegal(inputStr))
                {
                    friendUser.setPhone(inputStr);
                }else{
                    friendUser .setName(inputStr);
                }
                type=2;
                buildAction();
                InputMethodManager inputMethodManager=(InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                type=1;
                buildAction();
            }
        });


        inputText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if (keyCode == KeyEvent.KEYCODE_ENTER){
                    //业务代码
                    //dismiss();
                }
                return false;
            }
        });

        buildAction();

        return view;
    }
    @Override //在onCreate中设置对话框的风格、属性等
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        setCancelable(true);
        //可以设置dialog的显示风格，如style为STYLE_NO_TITLE，将被显示title。遗憾的是，我没有在DialogFragment中找到设置title内容的方法。theme为0，表示由系统选择合适的theme。
        int theme = 0;
        setStyle(DialogFragment.STYLE_NO_TITLE,theme);
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
    private OnClickOkListener mListener;
    public void setOnClickOkListener(OnClickOkListener listener) {
        mListener = listener;
    }

    @Override
    public void onRequestSuccess(String response) {
        try {
            Gson gson=new Gson();
            BaseResult result=gson.fromJson(response,BaseResult.class);
            if (result.getErrcode() <= 0 && result.getType() == ActionType.USER_GET){
                User user=gson.fromJson(result.getData(),User.class);
                if(user!=null){

                    friendUser=user;

                    if(user.getId()==myUser.getId()){
                        messText.setText("不能添加自己");
                        messText.setVisibility(View.VISIBLE);
                    }else {
                        inputText.setVisibility(View.GONE);
                        btnNext.setVisibility(View.GONE);
                        nameText.setVisibility(View.VISIBLE);
                        imgHead.setVisibility(View.VISIBLE);

                        ImageWorker.loadImage(imgHead, CValues.SERVER_IMG + user.getPhoto(), mHandler);
                        nameText.setText(user.getName());

                        btnPrev.setVisibility(View.VISIBLE);
                        btnOK.setVisibility(View.VISIBLE);
                    }
                }else{
                    messText.setText(getString(R.string.error_server));
                    messText.setVisibility(View.VISIBLE);
                }
            }
            if(result.getErrcode() <= 0 && result.getType() == ActionType.MESSAGE_ADD_FRIEND){

                dismiss();
                ShowMessage.taskShow(getContext(),result.getText());
            }
            if(result.getErrcode() >0){
                messText.setText(result.getErrmsg());
                messText.setVisibility(View.VISIBLE);
            }
        }catch (Exception e)
        {
            LogC.write(e,TAG);
            messText.setText(getString(R.string.error_server));
            messText.setVisibility(View.VISIBLE);

        }finally {
            isLoading=false;
        }
    }

    @Override
    public void onRequestError(Exception error) {
        //网络异常提示
        messText.setText(getString(R.string.error_net));
        messText.setVisibility(View.VISIBLE);
        isLoading=false;
        LogC.write(error,TAG);
    }

    public interface OnClickOkListener{
        void onItemClick(User user);
    }
}
