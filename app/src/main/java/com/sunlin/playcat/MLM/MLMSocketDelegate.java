package com.sunlin.playcat.MLM;

/**
 * Created by sunlin on 2017/4/25.
 */

public interface MLMSocketDelegate{

    //反馈信息
    public void  MLMSocketResultAccess(int action,String data,MLMTCPClient sender);
    public void  MLMSocketResultError(int action,int errorNum,String data,MLMTCPClient sender);
    //数据传输
    public void MLMGetMessage(MyHead myHead, byte[] data, MLMTCPClient sender);
}