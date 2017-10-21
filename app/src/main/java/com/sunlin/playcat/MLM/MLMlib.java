package com.sunlin.playcat.MLM;

/**
 * Created by sunlin on 2017/4/26.
 */

public class MLMlib {
    MLMlib() {
        //load();
    }

    private void load() {
        try {
            System.loadLibrary("native-lib");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    public native String stringFromJNI();
    //public native int buildData(byte[] buffer,int t,long from,long to,byte[] data);
    public int buildData(byte[] buff,MyHead myHead,byte[] data,int data_size){

        buff[0]=1;
        buff[1]=(byte)myHead.getT();

        buff[2]=(byte)myHead.getD();
        buff[3]=(byte)myHead.getE();

        buff[4]=(byte)((data_size>>>24)&0xFF);
        buff[5]=(byte)((data_size>>>16)&0xFF);
        buff[6]=(byte)((data_size>>>8)&0xFF);
        buff[7]=(byte)((data_size>>>0)&0xFF);

        buff[8]=(byte)((myHead.getFrom()>>>24)&0xFF);
        buff[9]=(byte)((myHead.getFrom()>>>16)&0xFF);
        buff[10]=(byte)((myHead.getFrom()>>>8)&0xFF);
        buff[11]=(byte)((myHead.getFrom()>>>0)&0xFF);

        buff[12]=(byte)((myHead.getTo()>>>24)&0xFF);
        buff[13]=(byte)((myHead.getTo()>>>16)&0xFF);
        buff[14]=(byte)((myHead.getTo()>>>8)&0xFF);
        buff[15]=(byte)((myHead.getTo()>>>0)&0xFF);

        buff[16]=(byte)((myHead.getS()>>>24)&0xFF);
        buff[17]=(byte)((myHead.getS()>>>16)&0xFF);
        buff[18]=(byte)((myHead.getS()>>>8)&0xFF);
        buff[19]=(byte)((myHead.getS()>>>0)&0xFF);


        //token
        for(int i=1;i<=MLMType.TOKEN_LENGTH;i++){
            buff[19+i]=myHead.getToken()[i-1];
        }
        System.arraycopy(data, 0, buff,MyHead.size, data_size);
        return data_size+MyHead.size;
    }
    /*
    uint8_t v;
    uint8_t t;
    uint16_t d;
    uint32_t l;
    uint32_t from;
    uint32_t to;
     */
    public int getDataHead(byte[] src_data,MyHead head){

        if(src_data.length<MyHead.size){
            return -1;
        }
        head.setV(src_data[0]& 0xFF);
        head.setT(src_data[1]& 0xFF);
        head.setD(src_data[2]& 0xFF);
        head.setE(src_data[3]& 0xFF);
        head.setL(byteToint_u32(src_data,4));
        head.setFrom(byteTolong_u32(src_data,8));
        head.setTo(byteTolong_u32(src_data,12));
        head.setS(byteTolong_u32(src_data,16));
        //token
        byte[] token=new byte[MLMType.TOKEN_LENGTH];
        head.setToken(token);
        for(int i=0;i<MLMType.TOKEN_LENGTH;i++) {
            head.getToken()[i] = src_data[16+i];
        }
        return 1;
    }
    public int byteToint_u16(byte[] b,int offset) {
        return   b[offset+1] & 0xFF |
                (b[offset] & 0xFF) << 8;
    }
    public int byteToint_u32(byte[] b,int offset) {
        return   b[offset+3] & 0xFF |
                (b[offset+2] & 0xFF) << 8 |
                (b[offset+1] & 0xFF) << 16 |
                (b[offset] & 0xFF) << 24;
    }
    public long byteTolong_u32(byte[] b,int offset) {
        return   b[offset+3] & 0xFF |
                (b[offset+2] & 0xFF) << 8 |
                (b[offset+1] & 0xFF) << 16 |
                (b[offset] & 0xFF) << 24;
    }
}
