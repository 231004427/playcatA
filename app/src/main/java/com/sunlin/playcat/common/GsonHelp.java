package com.sunlin.playcat.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by sunlin on 2017/11/6.
 */

public class GsonHelp {
    private static Gson gson;
    public static Gson getGsonObj(){
        if(gson==null){
            gson = new GsonBuilder()
                    //序列化null
                    .serializeNulls()
                    // 设置日期时间格式，另有2个重载方法
                    // 在序列化和反序化时均生效
                    .setDateFormat("yyyy-MM-dd HH:mm:ss")
                    // 禁此序列化内部类
                    //.disableInnerClassSerialization()
                    //生成不可执行的Json（多了 )]}' 这4个字符）
                    //.generateNonExecutableJson()
                    //禁止转义html标签
                    .disableHtmlEscaping()
                    //格式化输出
                    .setPrettyPrinting()
                    .create();
        }
        return gson;
    }
}
