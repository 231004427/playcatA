package com.sunlin.playcat.MLM;

import android.util.Log;

import com.sunlin.playcat.common.CValues;
import com.sunlin.playcat.common.LogC;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sunlin on 2017/10/2.
 */

public class MLMUDPClient extends MLMClient implements MLMClientInterface {
    private String TAG="MLMUDPClient";
    private DatagramSocket socket;
    private InetAddress address;
    private Thread serverThreadRead;
    private Thread serverThreadWrite;
    public MLMUDPClient(){
        super();
    }
    //启动服务器
    public void Run(){
        try {
            //readSocket =new DatagramSocket(port);
            //readSocket.setSoTimeout(0);//1s
            isConnection=true;
            socket =new DatagramSocket();
            socket.setSoTimeout(0);//3s
            address = InetAddress.getByName(host);
            serverThreadRead = new Thread(new ServerThreadRead());
            serverThreadRead.start();
            serverThreadWrite=new Thread(new ServerThreadWrite());
            serverThreadWrite.start();
        } catch (Exception e) {
            isConnection=false;
            e.printStackTrace();
            if(delegate!=null) {
                delegate.MLMSocketResultError(MLMType.ERROR_SYS_SERVER, 0, "服务器错误");
            }
        }

    }
    //重启服务
    public void Repeat(){
        close();
        writeList.clear();
        Run();
    }
    //关闭链接
    public void close(){
        if(socket!=null){
            socket.close();
            socket=null;
        }
        isConnection = false;
    }
    private void readMain(){
        MyHead rec_head=new MyHead();
        int j=0,z=0;
        rec_head.setL(0);
        rec_head.setFrom(0);
        rec_head.setTo(0);
        rec_head.setT(0);
        rec_head.setD(0);
        rec_head.setV(0);
        rec_head.setS(0);
        rec_head.setToken(new byte[MLMType.TOKEN_LENGTH]);
        byte[] buffRead = new byte[MLMType.MAX_READ_BUF];
        DatagramPacket dp =new DatagramPacket(buffRead, buffRead.length);
        while (isConnection){
            try {
                socket.receive(dp);
                int rec=dp.getLength();
                if(rec<0){
                    if(delegate!=null) {
                        delegate.MLMSocketResultError(MLMType.ACTION_SYS_BACK,MLMType.ERROR_SYS_SERVER, "服务器错误");
                    }
                    isConnection=false;
                    break;
                }
                if(mlib.getDataHead(dp.getData(),rec_head)< 0){
                    LogC.write("readMain：getDataHead error",TAG);
                    if(delegate!=null) {
                        delegate.MLMSocketResultError(MLMType.ACTION_SYS_BACK, MLMType.ERROR_SYS_DATA, "服务器错误");
                    }
                    continue;
                }
                //数据处理
                showMessage(rec_head,dp.getData());
                //重置
                rec_head.setL(0);
                rec_head.setFrom(0);
                rec_head.setTo(0);
                rec_head.setT(0);
                rec_head.setD(0);
                rec_head.setV(0);
                rec_head.setS(0);
            }
            catch (Exception e) {
                e.printStackTrace();
                if(delegate!=null) {
                    delegate.MLMSocketResultError(MLMType.ACTION_SYS_BACK, MLMType.ERROR_SYS_SERVER, "服务器错误");
                }
                close();
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
    private void showMessage(MyHead myHead,byte[] buffRead) {
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
    }
    private class ServerThreadWrite implements  Runnable{
        @Override
        public void run() {
            while (isConnection){
                if(writeList.size()>0) {
                    try {
                        int size = writeList.get(0).length;
                        DatagramPacket dp = new DatagramPacket(writeList.get(0), size, address, port);
                        socket.send(dp);
                        writeList.remove(0);
                        //Log.e("send", "(" + size + ")");
                        Thread.sleep(500);

                    } catch (Exception e) {
                        close();
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
            //服务器链接
            readMain();
        }
    }
}
