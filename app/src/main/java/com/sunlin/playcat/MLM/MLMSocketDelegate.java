package com.sunlin.playcat.MLM;

import com.sunlin.playcat.domain.Message;

/**
 * Created by sunlin on 2017/4/25.
 */

public interface MLMSocketDelegate{

    //发送错误
    public void  MLMSocketResultError(int action,int errorNum,String data);
    //成功获取
    public void MLMGetMessage(MyData myData);
    public void MLMShowMessage(Message message);
}