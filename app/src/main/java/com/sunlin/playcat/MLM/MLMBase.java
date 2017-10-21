package com.sunlin.playcat.MLM;

/**
 * Created by sunlin on 2017/10/4.
 */

public class MLMBase {
    public MLMClientInterface mlmClient;
    public int userid;
    public byte[] token;
    public MLMBase(MLMClientInterface _mlmClient,int _userid,byte[] _token){
        mlmClient=_mlmClient;
        userid=_userid;
        token=new byte[32];
        for (int i=0;i<_token.length;i++){
            token[i]=_token[i];
        }
    }
}
