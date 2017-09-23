package com.sunlin.playcat.MLM;

/**
 * Created by sunlin on 2017/9/20.
 */

public class MyData {
    private MyHead myHead;
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public MyHead getMyHead() {
        return myHead;
    }

    public void setMyHead(MyHead myHead) {
        this.myHead = myHead;
    }
}
