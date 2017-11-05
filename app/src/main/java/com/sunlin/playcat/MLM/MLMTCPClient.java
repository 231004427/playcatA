package com.sunlin.playcat.MLM;

import android.util.Log;

import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.LogC;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by sunlin on 2017/4/25.
 */

public class MLMTCPClient extends MLMClient implements MLMClientInterface {
    private String TAG="MLMTCPClient";
    private Thread serverThreadWrite;
    private Thread serverThreadRead;
    private Socket client;
    private OutputStream output;
    private InputStream input;

    public MLMUser mlmUser;
    public int liveNum=0;
    SocketAddress address;
    private Timer timer;
    private boolean isConnection=false;

    public MLMUser getMlmUser() {
        return mlmUser;
    }
    public MLMTCPClient(int userid,String token){
        super();
        mlmUser = new MLMUser(this, userid, token.getBytes());
        address=new InetSocketAddress(host,port);
    }
    //启动线程链接服务器
    public void Run(){
        liveNum=0;
        writeList.clear();
        serverThreadRead  = new Thread(new MLMTCPClient.ServerThreadRead());
        serverThreadRead.start();
    }
    //重启
    public void Repeat(){
        if(!isConnection) {
            Run();
        }else{
            mlmUser.userRegist();
        }
    }
    //关闭链接
    public void close(boolean isResult){
        try {
            isConnection=false;
            if(client!=null) {
                client.close();
                client=null;
            }
            if(output!=null){
                output.close();
            }
            if(input!=null){
                input.close();
            }
            if(timer!=null){
                timer.cancel();
            }
        }catch (Exception ex){

        }
        finally {
            if(delegate!=null && isResult) {
                delegate.MLMSocketResultError(MLMType.ACTION_SYS_BACK, MLMType.ERROR_SYS_SERVER, "服务关闭");
            }
        }
    }

    //链接服务器TCP
    public boolean connectServer(){
        try {
            client=new Socket();
            client.setTcpNoDelay(true);
            client.connect(address, 1500);
            output = client.getOutputStream();
            input = client.getInputStream();
            isConnection=true;
            //开启心跳
            keepLive();
            //开启写线程
            mlmUser.userRegist();
            serverThreadWrite=new Thread(new MLMTCPClient.ServerThreadWrite());
            serverThreadWrite.start();
            //开启读线程
            readMain();
        }  catch (Exception e) {
            close(true);
            e.printStackTrace();
            return false;
        }
        return true;
    }
    //处理消息
    private void showMessage(MyHead myHead,byte[] buffRead){
        if(myHead.getT()== MLMType.ACTION_KEEP_LIVE){
            if(myHead.getD()==MLMType.ACTION_ACCESS){
                liveNum=0;
            }
            return;
        }
        //反馈信息
        if(delegate!=null) {
            MyHead headObj = new MyHead();
            headObj.setTo(myHead.getTo());
            headObj.setT(myHead.getT());
            headObj.setV(myHead.getV());
            headObj.setFrom(myHead.getFrom());
            headObj.setL(myHead.getL());
            headObj.setS(myHead.getS());
            headObj.setD(myHead.getD());
            headObj.setE(myHead.getE());
            headObj.setToken(myHead.getToken());
            MyData myData = new MyData();
            myData.setMyHead(headObj);
            if(myHead.getL()!=0){
                byte[] data=new byte[myHead.getL()];
                for(int i=0;i<myHead.getL();i++){
                    data[i]=buffRead[MyHead.size+i];
                }
                myData.setData(data);
            }
            delegate.MLMGetMessage(myData);
        }
        /*
            //message
            String message = new String(data,Charset.forName("utf-8"));
            try{
            JsonParser parser = new JsonParser();
            JsonElement root = parser.parse(message);
            JsonObject element = root.getAsJsonObject();
            JsonPrimitive nJson = element.getAsJsonPrimitive("n");
            JsonPrimitive tJson = element.getAsJsonPrimitive("t");
            JsonPrimitive cJson = element.getAsJsonPrimitive("c");

        }*/
    }
    //心跳检查
    private void keepLive(){
            timer = new Timer();
            TimerTask tast = new TimerTask() {
                @Override
                public void run() {
                    //检查是否断开
                    if(client.isClosed()){
                        timer.cancel();
                    }
                    if (liveNum > 2) {
                        close(true);
                        timer.cancel();
                    } else {
                        liveNum++;
                        //发送心跳包
                        mlmUser.keepLive();
                    }
                }
            };
            timer.schedule(tast, 3000, 3000);
    }
    //读取数据
    private void readMain(){
        MyHead rec_head=new MyHead();
        int j=0,z=0;
        int head_size=MyHead.size;
        rec_head.setL(0);
        rec_head.setFrom(0);
        rec_head.setTo(0);
        rec_head.setT(0);
        rec_head.setD(0);
        rec_head.setV(0);
        rec_head.setS(0);
        rec_head.setToken(new byte[MLMType.TOKEN_LENGTH]);
        byte[] response=new byte[MLMType.MAX_DATA_BUF];
        byte[] buffRead=new byte[MLMType.MAX_READ_BUF];
        while (isConnection){
            try {
                int rec=input.read(response);
                if(rec<0){
                    close(true);
                    break;
                }
                for(int i=0;i<rec;i++){

                    if(rec_head.getL()==0){
                        buffRead[j]=response[i];
                        j+=1;
                        if(j==head_size){
                            if(mlib.getDataHead(buffRead,rec_head)< 0){
                                LogC.write("readMain：getDataHead error",TAG);
                                if(delegate!=null) {
                                    delegate.MLMSocketResultError(MLMType.ACTION_SYS_BACK, MLMType.ERROR_SYS_DATA, "服务器错误");
                                }
                                continue;
                            }
                            //如果数据为空
                            if(rec_head.getL()==0){
                                //显示信息
                                showMessage(rec_head,null);
                                //重置
                                rec_head.setL(0);
                                rec_head.setFrom(0);
                                rec_head.setTo(0);
                                rec_head.setT(0);
                                rec_head.setD(0);
                                rec_head.setV(0);
                                rec_head.setS(0);
                                z=0;
                                j=0;
                            }
                        }
                    }else {
                        buffRead[j]=response[i];
                        j += 1;
                        z += 1;
                        if(z == rec_head.getL())
                        {
                            //收取包完成
                            //显示信息
                            showMessage(rec_head,buffRead);
                            //重置
                            rec_head.setL(0);
                            rec_head.setFrom(0);
                            rec_head.setTo(0);
                            rec_head.setT(0);
                            rec_head.setD(0);
                            rec_head.setV(0);
                            rec_head.setS(0);
                            z=0;
                            j=0;

                        }
                    }
                }
                Thread.sleep(500);
            } catch (Exception e) {
                e.printStackTrace();
                close(true);
                break;
            }
        }

    }

    @Override
    public boolean sendMessage(byte[] data, MyHead myHead){
        byte[] buffWrite = new byte[data.length+MyHead.size];
        mlib.buildData(buffWrite,myHead,data,data.length);
        writeList.add(buffWrite);
        return  true;
    }

    private class ServerThreadWrite implements  Runnable{
        @Override
        public void run() {
            while (isConnection){
                if(writeList.size()>0) {
                    try {
                        int size = writeList.get(0).length;
                        output.write(writeList.get(0),0,size);
                        output.flush();
                        writeList.remove(0);
                        //LogC.e("send", "(" + size + ")");
                        Thread.sleep(500);

                    } catch (Exception e) {
                        close(true);
                        e.printStackTrace();
                        if (delegate != null) {
                            delegate.MLMSocketResultError(MLMType.ACTION_SYS_BACK,MLMType.ERROR_SYS_SEND, "发送失败");
                        }
                    }
                }
            }
        }
    }
    private class ServerThreadRead implements Runnable {
        @Override
        public void run() {
            try {
                //服务器链接
                connectServer();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
