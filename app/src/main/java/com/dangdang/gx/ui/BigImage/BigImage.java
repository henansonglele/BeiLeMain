package com.dangdang.gx.ui.BigImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Scroller;
import java.io.IOException;
import java.io.InputStream;

/**
巨图加载工具

到加载巨图的需求，如何加载一个大图而不产生OOM呢，可以使用系统提供的BitmapRegionDecoder这个类。

BitmapRegionDecoder：区域解码器，可以用来解码一个矩形区域的图像，有了这个我们就可以自定义一块矩形的区域，然后根据手势来移动矩形区域的位置就能看到整张图片了。

下面就来完成一个加载大图并支持拖动查看，双击放大，手势缩放的的自定义View。


*/
public class BigImage extends View implements GestureDetector.OnGestureListener,
                                              ScaleGestureDetector.OnScaleGestureListener {
    BitmapFactory.Options mOptions;
    Scroller mScroller;
    Matrix mMatrix;
    GestureDetector mGestureDetector;
    ScaleGestureDetector mScaleGestureDetector;
    BitmapRegionDecoder mRegionDecoder;
    int mImageWidth;
    int mImageheight;
    int mViewWidth;
    int mViewHeight;
    Rect mRect;
    int mScale;
    int mCurrentScale;
    Bitmap mBitmap;

    public BigImage(Context context) {
        super(context);
    }

    private void init(){
        mOptions = new BitmapFactory.Options();
        //滑动器
        mScroller = new Scroller(getContext());
        //缩放器
        mMatrix = new Matrix();
        //手势识别
        mGestureDetector = new GestureDetector(getContext(),this);
        mScaleGestureDetector = new ScaleGestureDetector(getContext(),this);
    }


   public void setImage(InputStream is){
        mOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is,null,mOptions);
        mImageWidth = mOptions.outWidth;
        mImageheight = mOptions.outHeight;
        mOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mOptions.inJustDecodeBounds = false;
        try{
            mRegionDecoder = BitmapRegionDecoder.newInstance(is,false);
        }catch (IOException e){
            e.printStackTrace();
        }
   }
    @Override
   protected void onSizeChanged(int w,int h,int oldw,int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        mViewWidth = w;
        mViewHeight = h;
        mRect.top = 0;
        mRect.left = 0;
        mRect.right = mViewWidth;
        mRect.bottom = mViewHeight;
        mScale = mViewWidth/mImageWidth;
        mCurrentScale = mScale;
   }

   @Override
   protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        if(mRegionDecoder == null){
            return;
        }
        //复用内存
        mOptions.inBitmap = mBitmap;
        mBitmap = mRegionDecoder.decodeRegion(mRect,mOptions);
        mMatrix.setScale(mCurrentScale,mCurrentScale);
        canvas.drawBitmap(mBitmap,mMatrix,null);
   }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        mGestureDetector.onTouchEvent(event);
        mScaleGestureDetector.onTouchEvent(event);
        return  true;


    }




///后续手势添加  https://www.jianshu.com/p/4db6be1e68ae


    @Override
    public boolean onDown(MotionEvent e) {
        if(!mScroller.isFinished()){//正在滑动 停止
            mScroller.forceFinished(true);
        }
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }
}
