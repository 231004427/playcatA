package com.sunlin.playcat.MLM;

import java.nio.charset.Charset;

/**
 * Created by sunlin on 2017/10/4.
 */

public class MLMUser extends MLMBase {
    public MLMUser (MLMClientInterface mlmClient,int _userid,byte[] _token){
        super(mlmClient,_userid,_token);
    }
    //心跳
    public boolean keepLive(){
        MyHead myHead=new MyHead();
        myHead.setFrom(userid);
        myHead.setTo(0);
        myHead.setV(1);
        myHead.setT(MLMType.ACTION_KEEP_LIVE);
        myHead.setToken(token);
        myHead.setD(0);
        myHead.setE(0);
        myHead.setL(1);
        return mlmClient.sendMessage(new byte[]{0x0},myHead);
    }
    //用户注册
    public boolean userRegist(){

        MyHead myHead=new MyHead();
        myHead.setFrom(userid);
        myHead.setTo(0);
        myHead.setV(1);
        myHead.setT(MLMType.ACTION_USER_REGIST);
        myHead.setToken(token);
        myHead.setD(0);
        myHead.setE(0);
        myHead.setL(1);
        return mlmClient.sendMessage(new byte[]{0x0},myHead);
    }
    //文本发送
    public boolean userSendText(String content,int toUserId)
    {
        byte[] rawData=content.getBytes();

        MyHead myHead=new MyHead();
        myHead.setFrom(userid);
        myHead.setTo(toUserId);
        myHead.setV(1);
        myHead.setT(MLMType.ACTION_SEND_SINGLE);
        myHead.setToken(token);
        myHead.setS(0);
        myHead.setD(0);
        myHead.setE(0);
        myHead.setL(rawData.length);

        return mlmClient.sendMessage(rawData,myHead);
    }
    //房间发送
    public boolean userSendRoom(String content,long roomID){
        byte[] rawData=content.getBytes();

        MyHead myHead=new MyHead();
        myHead.setFrom(userid);
        myHead.setTo(roomID);
        myHead.setV(1);
        myHead.setT(MLMType.ACTION_SEND_ROOM);
        myHead.setS(roomID);
        myHead.setToken(token);
        myHead.setD(0);
        myHead.setE(0);
        myHead.setL(rawData.length);

        return mlmClient.sendMessage(rawData,myHead);
    }
    //邀请加入
    public boolean inviteRoom(int toUserId,long roomId){
        MyHead myHead=new MyHead();
        myHead.setFrom(userid);
        myHead.setTo(toUserId);
        myHead.setV(1);
        myHead.setT(MLMType.ACTION_ROOM_INVITE);
        myHead.setS(roomId);
        myHead.setToken(token);
        myHead.setD(0);
        myHead.setE(0);
        myHead.setL(1);

        return mlmClient.sendMessage(new byte[]{0x0},myHead);
    }
    //加入聊天会话
    public boolean joinRoom(long roomId){
        MyHead myHead=new MyHead();
        myHead.setFrom(userid);
        myHead.setTo(roomId);
        myHead.setV(1);
        myHead.setT(MLMType.ACTION_ROOM_INVITE_YES);
        myHead.setS(roomId);
        myHead.setToken(token);
        myHead.setD(0);
        myHead.setE(0);
        myHead.setL(1);
        return mlmClient.sendMessage(new byte[]{0x0},myHead);
    }
    //创建聊天会话
    public boolean createRoom(long roomId){
        MyHead myHead=new MyHead();
        myHead.setFrom(userid);
        myHead.setTo(roomId);
        myHead.setV(1);
        myHead.setT(MLMType.ACTION_ROOM_CREATE);
        myHead.setS(roomId);
        myHead.setToken(token);
        myHead.setD(0);
        myHead.setE(0);
        myHead.setL(1);
        return mlmClient.sendMessage(new byte[]{0x0},myHead);
    }
    //离开会话
    public boolean leaveRoom(long roomId){
        MyHead myHead=new MyHead();
        myHead.setFrom(userid);
        myHead.setTo(roomId);
        myHead.setV(1);
        myHead.setT(MLMType.ACTION_ROOM_LEAVE);
        myHead.setS(roomId);
        myHead.setToken(token);
        myHead.setD(0);
        myHead.setE(0);
        myHead.setL(1);
        return mlmClient.sendMessage(new byte[]{0x0},myHead);
    }
}
