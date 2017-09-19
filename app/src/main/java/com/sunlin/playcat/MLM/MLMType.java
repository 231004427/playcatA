package com.sunlin.playcat.MLM;

/**
 * Created by sunlin on 2017/9/17.
 */

public class MLMType {
    //用户注册
    public static int ACTION_USER_REGIST=10;
    public static int ERROR_USER_REGIST_REPEAT=-12;
    public static int ERROR_USER_REGIST_MAX=-13;
    public static int ERROR_USER_REGIST_SYS=-11;

    //邀请用户加入房间
    public static int ACTION_ROOM_INVITE=50;
    public static int ACTION_ROOM_INVITE_YES=80;
    public static int ACTION_ROOM_INVITE_NO=90;
    public static int ERROR_ROOM_INVITE_NOUSER=-82;//用户未注册
    public static int ERROR_ROOM_INVITE_REPEAT=-83;
    public static int ERROR_ROOM_INVITE_INSERT=-81;
    public static int ERROR_ROOM_INVITE_NOROOM=-84;
    //删除房间
    public static int ACTION_ROOM_DEL=100;
    public static int ERROR_ROOM_DEL_NOROOM=-101;
    //离开房间
    public static int ACTION_ROOM_LEAVE=30;
    public static int ERROR_ROOM_LEAVE_NOUSER=-33;
    public static int ERROR_ROOM_LEAVE_NOROOM=-31;
    public static int ERROR_ROOOM_LEAVE_NOREGIST=-32;
    //创建房间
    public static int ACTION_ROOM_CREATE=20;
    public static int ERROR_ROOM_CREATE_NOUSER=-22;
    public static int ERROR_ROOM_CREATE_MAX=-23;
    public static int ERROR_ROOM_CREATE_REPEAT=-24;
    public static int ERROR_ROOM_CREATE_INIT=-26;//初始化用户列表错误
    public static int ERROR_ROOM_CREATE_NEW=-21;
    public static int ERROR_ROOM_CREATE_INSERT=-27;
    //消息单发
    public static int ACTION_SEND_SINGLE=60;
    public static int ERROR_SEND_SINGLE_NOUSER=-61;
    //消息群发
    public static int ACTION_SEND_MULTI=70;
    public static int ERROR_SEND_MULTI_NOROOM=-71;
    //消息类型
    public static int MESSAGE_TEXT=1;
    public static int MESSAGE_VOICE=2;
    public static int MESSAGE_IMG=3;
    public static int MESSAGE_FILE=4;
    public static int MESSAGE_VIDEO=5;
    //心跳包
    public static int ACTION_KEEP_LIVE=200;
    //系统通知
    public static int ACTION_SYS_BACK=40;
    public static int ACTION_ACCESS=9;
    //系统错误
    public static int ERROR_SYS_SEND=-3;
    public static int ERROR_SYS_SERVER=-2;


}
