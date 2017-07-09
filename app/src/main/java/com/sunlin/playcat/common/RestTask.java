package com.sunlin.playcat.common;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.ref.WeakReference;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

/**
 * Created by sunlin on 2017/6/28.
 */

public class RestTask extends AsyncTask<Void,Integer,Object> {
    private static final String TAG="RestTask";
    public interface ResponseCallback{
        public void onRequestSuccess(String response);
        public void onRequestError(Exception error);
    }
    public interface ProgressCallback{
        public void onProgressUpdate(int progress);
    }
    private HttpURLConnection mConnection;
    private String mFormBody;
    private File mUploadFile;
    private String mUploadFileName;
    private String contentType;
    private String charset="UTF-8";;
    private WeakReference<ResponseCallback> mResponseCallback;
    private WeakReference<ProgressCallback> mProgressCallback;

    public RestTask(HttpURLConnection connection){
        mConnection=connection;
    }
    public void setmContentType(String contentType_){
        contentType=contentType_;
    }

    public void setmFormBody(List<NameValuePair> formData){
        if(formData==null){
            mFormBody=null;
            return;
        }
        StringBuilder sb=new StringBuilder();
        try {
        for(int i=0;i<formData.size();i++){
            NameValuePair item=formData.get(i);

                sb.append(URLEncoder.encode(item.getName(),charset));

            sb.append("=");
            sb.append(URLEncoder.encode(item.getValue(),charset));
            if(i!=(formData.size()-1)){
                sb.append("&");
            }
        }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mFormBody=sb.toString();
    }
    public void setmFormBody(String json){
        if(json == null){
            mFormBody=null;
            return;
        }
        try {
            mFormBody=URLEncoder.encode(json,charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    public void setmUploadFile(File file,String fileName){
        mUploadFile=file;
        mUploadFileName=fileName;
    }
    public void setResponseCallback(ResponseCallback callback){
        mResponseCallback=new WeakReference<ResponseCallback>(callback);
    }
    public void setProgressCallback(ProgressCallback callback){
        mProgressCallback=new WeakReference<ProgressCallback>(callback);
    }
    private void writeMultipart(String boundary, String charset,
                                OutputStream output, boolean writeContent) throws IOException {
        BufferedWriter writer=null;
        try{
            writer=new BufferedWriter(new OutputStreamWriter(output, Charset.forName(charset)),8192);
            //发送表单数据
            if(mFormBody!=null){
                writer.write("--"+boundary);
                writer.write("\r\n");
                writer.write(
                        "Content-Disposition:form-data;name=\"parameters\"");
                writer.write("\r\n");
                writer.write("Content-type:text/plain;charset="+charset);
                writer.write("\r\n");
                writer.write("\r\n");
                if(writeContent){
                    writer.write(mFormBody);
                }
                writer.write("\r\n");
                writer.flush();
            }
            //发送二进制文件
            writer.write("--"+boundary);
            writer.write("\r\n");
            writer.write("Content-Disposition:form-data;name=\""
                    +mUploadFileName+"\";filename=\""
                    +mUploadFile.getName()+"\"");
            writer.write("\r\n");
            writer.write("Content-Type:"
                    + URLConnection.guessContentTypeFromName(mUploadFile.getName()));
            writer.write("\r\n");
            writer.write("Content-Transfer-Encoding:binary");
            writer.write("\r\n");
            writer.write("\r\n");
            writer.flush();
            if(writeContent){
                InputStream input=null;
                try{
                    input=new FileInputStream(mUploadFile);
                    byte[] buffer=new byte[1024];
                    for(int length=0;(length=input.read(buffer))>0;){
                        output.write(buffer,0,length);
                    }
                    //不要关闭OutputStream
                    output.flush();
                }catch (IOException e){
                    Log.w(TAG,e);
                }finally {
                    if(input!=null){
                        try {
                            input.close();
                        }catch (IOException e){

                        }
                    }
                }
                //二进制块结束
                writer.write("\r\n");
                writer.flush();
                //multipart/form-data的结束
                writer.write("--"+boundary+"--");
                writer.write("\r\n");
                writer.flush();
            }

        }finally {
            if(writer!=null){
                writer.close();
            }
        }
    }
    private void writeFormData(String charset,OutputStream output) throws IOException{
        try{
            output.write(mFormBody.getBytes(charset));
            output.flush();
        }finally {
            if(output!=null){
                output.close();
            }
        }
    }

    @Override
    protected Object doInBackground(Void... params) {
        //生成用来标识界限的随机字符
        String boundary=Long.toHexString(System.currentTimeMillis());

        try{
            if(mUploadFile!=null){
                //复合请求
                mConnection.setRequestProperty("Content-Type",
                        "multipart/form-data;boundary="+boundary);
                //计算extra元数据大小
                ByteArrayOutputStream bos=new ByteArrayOutputStream();
                writeMultipart(boundary,charset,bos,false);
                byte[] extra=bos.toByteArray();
                int contentLength=extra.length;
                //文件的大小加到length上
                contentLength+=mUploadFile.length();
                //如果存在表单主体，把它加到length 上
                if(mFormBody!=null){
                    contentLength+=mFormBody.length();
                }
                mConnection.setFixedLengthStreamingMode(contentLength);

            }else if(mFormBody!=null){
                //只发送表单数据
                if(contentType==null||contentType.isEmpty()){
                    contentType="application/x-www-form-urlencoded";
                }
                mConnection.setRequestProperty("Content-Type",
                        contentType+";charset="+charset);
                mConnection.setFixedLengthStreamingMode(mFormBody.length());
            }
            //这是第一个调用URLConnection,它会真正执行网络IO操作
            //openConnection()执行的还是本地操作
            mConnection.connect();

            //如果可以的话（对POST），创建输出流
            if(mUploadFile!=null){
                OutputStream out=mConnection.getOutputStream();
                writeMultipart(boundary,charset,out,true);
            }else if(mFormBody!=null){
                OutputStream out=mConnection.getOutputStream();
                writeFormData(charset,out);
            }


            //获取响应数据
            int status=mConnection.getResponseCode();
            if(status>=300){
                String message=mConnection.getResponseMessage();
                return new HttpRetryException(message,status);
            }

            InputStream in=mConnection.getInputStream();
            String encoding=mConnection.getContentEncoding();
            int contentLength=mConnection.getContentLength();
            if(encoding==null){
                encoding="UTF-8";
            }
            byte[] buffer=new byte[1024];
            int length=contentLength>0?contentLength:0;
            ByteArrayOutputStream out=new ByteArrayOutputStream(length);

            int downloadedBytes=0;
            int read;
            while ((read=in.read(buffer))!=-1){
                downloadedBytes+=read;
                publishProgress((downloadedBytes*100)/contentLength);
                out.write(buffer,0,read);
            }
            return new String(out.toByteArray(),encoding);

        } catch (Exception e) {
            Log.w(TAG,e);
            return e;
        }finally {
            if(mConnection!=null){
                mConnection.disconnect();
            }
        }
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        //更新进度UI
        if(mProgressCallback!=null&&mProgressCallback.get()!=null){
            mProgressCallback.get().onProgressUpdate(values[0]);
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        if(mResponseCallback!=null&&mResponseCallback.get()!=null){
            if(o instanceof String){
                mResponseCallback.get().onRequestSuccess((String)o);
            }else if(o instanceof Exception){
                mResponseCallback.get().onRequestError((Exception)o);
            }else{
                mResponseCallback.get().onRequestError(new IOException("Unknown Error Contacting Host"));
            }
        }
    }
}

