package com.sunlin.playcat.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by sunlin on 2017/7/2.
 */

public class RoundImgeView extends View{
    private Bitmap mImage;
    private Paint mBitmapPaint;
    private RectF mBounds;
    private float mRadius=25.0f;

    public RoundImgeView(Context context) {
        super(context);
        init();
    }

    public RoundImgeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundImgeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mBitmapPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        mBounds=new RectF();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int height,width;
        height=width=0;
        //图片内容大小
        int imageHeight,imageWidth;
        if(mImage==null){
            imageHeight=imageWidth=0;
        }else{
            imageHeight=mImage.getHeight();
            imageWidth=mImage.getWidth();
        }
        //获得最佳测量值
        width=getMeasurement(widthMeasureSpec,imageWidth);
        height=getMeasurement(heightMeasureSpec,imageHeight);
        setMeasuredDimension(width,height);
    }

    private int getMeasurement(int measureSpec, int contentSize) {
        int specSize= View.MeasureSpec.getSize(measureSpec);
        switch (View.MeasureSpec.getMode(measureSpec)){
            case View.MeasureSpec.AT_MOST:
                return Math.min(specSize,contentSize);
            case View.MeasureSpec.UNSPECIFIED:
                return contentSize;
            case View.MeasureSpec.EXACTLY:
                return specSize;
            default:
                return 0;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if(w!=oldw||h!=oldh){
            int imageWidth,imageHeight;
            if(mImage==null){
                imageWidth=imageHeight=0;
            }else{
                imageWidth=mImage.getWidth();
                imageHeight=mImage.getHeight();
            }
            int left=(w-imageWidth)/7;
            int top=(h-imageHeight)/7;
            //设置边界偏离圆角矩形
            mBounds.set(left,top,left+imageWidth,top+imageHeight);
            if(mBitmapPaint.getShader()!=null){
                Matrix m=new Matrix();
                m.setTranslate(left,top);
                mBitmapPaint.getShader().setLocalMatrix(m);
            }
        }
    }
    public void setImage(Bitmap bitmap){
        if(mImage!=bitmap){
            mImage=bitmap;
            if(mImage!=null){
                BitmapShader shader=new BitmapShader(mImage, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                mBitmapPaint.setShader(shader);
            }else{
                mBitmapPaint.setShader(null);
            }
            requestLayout();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mBitmapPaint!=null){
            canvas.drawRoundRect(mBounds,mRadius,mRadius,mBitmapPaint);
        }
    }
}
