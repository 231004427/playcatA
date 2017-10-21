package com.sunlin.playcat.MLM;

/**
 * Created by sunlin on 2017/9/17.
 */

public class MLMType {
    //TOKEN长度
    public static int TOKEN_LENGTH=32;
    public static int MAX_READ_BUF=10240;
    public static int MAX_DATA_BUF=10240;
    //用户注册
    public static int ACTION_USER_REGIST=1;
    //邀请用户加入房间
    public static int ACTION_ROOM_INVITE=2;
    public static int ACTION_ROOM_INVITE_YES=3;
    public static int ACTION_ROOM_INVITE_NO=4;
    //删除房间
    public static int ACTION_ROOM_DEL=5;
    public static int ERROR_ROOM_DEL_NOROOM=8;
    //离开房间
    public static int ACTION_ROOM_LEAVE=6;
    //创建房间
    public static int ACTION_ROOM_CREATE=7;
    //消息单发
    public static int ACTION_SEND_SINGLE=8;
    //消息群发
    public static int ACTION_SEND_MULTI=9;
    //心跳包
    public static int ACTION_KEEP_LIVE=10;
    //系统通知
    public static int ACTION_SYS_BACK=11;
    //操作成功
    public static int ACTION_ACCESS=12;
    //错误信息
    public static int ERROR_USER_REGIST_REPEAT=51;
    public static int ERROR_USER_REGIST_MAX=52;
    public static int ERROR_USER_REGIST_SYS=53;
    public static int ERROR_INVITE_NOUSER=54;//用户未注册
    public static int ERROR_ROOM_INVITE_REPEAT=55;
    public static int ERROR_ROOM_INVITE_INSERT=56;
    public static int ERROR_ROOM_INVITE_NOROOM=57;
    public static int ERROR_ROOM_LEAVE_NOUSER=59;
    public static int ERROR_ROOM_LEAVE_NOROOM=60;
    public static int ERROR_ROOOM_LEAVE_NOREGIST=61;
    public static int ERROR_ROOM_CREATE_NOUSER=62;
    public static int ERROR_ROOM_CREATE_MAX=63;
    public static int ERROR_ROOM_CREATE_REPEAT=64;
    public static int ERROR_ROOM_CREATE_INIT=65;//初始化用户列表错误
    public static int ERROR_ROOM_CREATE_NEW=66;
    public static int ERROR_ROOM_CREATE_INSERT=67;
    public static int ERROR_SEND_SINGLE_NOUSER=68;
    public static int ERROR_SEND_MULTI_NOROOM=69;
    public static int ERROR_SYS_SEND=70;//发送服务器失败
    public static int ERROR_SYS_SERVER=71;//服务器断线
    public static int ERROR_SYS_TOKEN=72;//服务器断线
    public static int ERROR_SYS_DATA=73;//数据异常
    public static int ERROR_SYS_NOREGIST=74;
    //消息内容类型
    public static int MESSAGE_TEXT=101;
    public static int MESSAGE_VOICE=102;
    public static int MESSAGE_IMG=103;
    public static int MESSAGE_FILE=104;
    public static int MESSAGE_VIDEO=105;
}
