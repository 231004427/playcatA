package com.sunlin.playcat.common;

/**
 * Created by sunlin on 2017/6/28.
 */
public class NameValuePair {
    private String mName;
    private String mValue;
    public NameValuePair(String name,String value){
        mName=name;
        mValue=value;
    }
    public String getName()
    {
        return mName;
    }
    public String getValue(){
        return mValue;
    }
}
