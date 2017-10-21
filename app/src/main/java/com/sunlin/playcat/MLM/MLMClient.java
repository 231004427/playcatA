package com.sunlin.playcat.MLM;

import com.sunlin.playcat.common.CValues;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunlin on 2017/10/4.
 */

public class MLMClient {
    private String TAG="MLMClient";
    protected List<byte[]> writeList;
    protected String host;
    protected int port;
    public boolean isConnection=false;
    protected MLMlib mlib=new MLMlib();
    public MLMSocketDelegate delegate;

    public MLMClient(){
        host = CValues.MLM_HOST;//"192.168.43.149"192.168.2.122
        port = CValues.MLM_PORT;
        writeList=new ArrayList<byte[]>();
    }
}
