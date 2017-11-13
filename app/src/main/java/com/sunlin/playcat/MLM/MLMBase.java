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
        setToken(_token);
    }
    public void setUserid(int userid) {
        this.userid = userid;
    }

    public void setToken(byte[] _token) {
        for (int i=0;i<token.length;i++){
            if(i<_token.length) {
                token[i] = _token[i];
            }else{
                token[i]=0;
            }
        }
    }
}
