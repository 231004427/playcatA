package com.sunlin.playcat.common;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.sunlin.playcat.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sunlin on 2017/7/16.
 */

public class ImageWorker {
    private static final int lruSizeMaxSize = 10*1024*1024;// 图片内存缓存最大空间10M
    public final static LruCache<String,Bitmap> mLruCache = new LruCache<String,Bitmap>(lruSizeMaxSize);
    private static ExecutorService mExecutorService = null;// 下载图片的线程管理类
    private static final int threadPoolMaxSize = 6;// 同时下载图片的 最大线程数
    private ImageWorker() {
    }
    private static ImageWorker mImageWorker = null;
    private static String bitmapPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/playcat/temp/";// 图片在硬盘中的存储地址
    /**
     * 初始化，使用单例获得ImageWorker1的对象
     */
    public static synchronized ImageWorker getImageWorkerInstance(){
        if(mImageWorker == null){
            mImageWorker = new ImageWorker();
            mExecutorService = Executors.newFixedThreadPool(threadPoolMaxSize);
        }
        return mImageWorker;
    }

    /**
     * （简单来说）加载图片：
     *  1 先从内存缓存中查找，如果没有则执行下一步。
     *  2 从磁盘中查找，如果没有执行下一步。
     *  3从网络下载
     */
    /**
     * 1、首先判断内存缓存是否为空（不为空直接显示）
     * 2、内存缓存为空时，再从磁盘中读取后，将图片添加到内存缓存中（然后显示）
     * 3、磁盘为空时，只能重新网络加载图片（下载下来的图片，会在内存缓存、磁盘中各存储一份，然后显示）
     * @param mImageView
     * @param imgUrl
     * @param mHandler
     */
    public static void loadImage(ImageView mImageView, String imgUrl,
                                 Handler mHandler) {
        getImageWorkerInstance();
        Bitmap bitmapCache = getBitmapFromCache(imgUrl);
        if(bitmapCache!=null){
            mImageView.setImageBitmap(bitmapCache);
        }else {
            Bitmap bitmapDisk = getBitmapFromDisk(imgUrl);
            if(bitmapDisk!=null){
                addBitmapToLruCache(imgUrl,bitmapDisk);
                mImageView.setImageBitmap(bitmapDisk);
            }else{
                //从网络下载图片
                loadImageFromNetwork(mImageView,imgUrl,mHandler,mLruCache,bitmapPath);
            }
        }
    }


    private static void loadImageFromNetwork(ImageView mImageView, String imgUrl, Handler mHandler, LruCache<String, Bitmap> mLruCache, String bitmapPath) {
        mExecutorService.submit(new DownloadImageFromNetwork(mImageView,imgUrl,mHandler,mLruCache,bitmapPath));
    }



    private static void addBitmapToLruCache(String imgUrl, Bitmap bitmapDisk) {
        mLruCache.put(imgUrl,bitmapDisk);
    }

    private static Bitmap getBitmapFromDisk(String imgUrl) {
        InputStream inputStream = null;
        String[] fileNames = imgUrl.split("/");
        String fileName = fileNames[fileNames.length-1];
        try {
            inputStream = new FileInputStream(bitmapPath+fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        return bitmap;
    }



    /**
     * 第一步：尝试从缓存中获取图片
     * @param imgUrl
     * @return
     */
    private static Bitmap getBitmapFromCache(String imgUrl) {
        Bitmap bitmap = mLruCache.get(imgUrl);
        return bitmap;
    }
}
