package com.sunlin.playcat.MLM;

/**
 * Created by sunlin on 2017/10/17.
 */

public interface MLMClientInterface {
   boolean sendMessage(byte[] data, MyHead myHead);
}
