//package com.universitylibrary.ui.view;
//
//import android.content.Context;
//import android.util.DisplayMetrics;
//import android.view.Gravity;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import com.universitylibrary.ui.log.LogM;
//import pl.droidsonroids.gif.GifDrawable;
//import pl.droidsonroids.gif.GifImageView;
//
//public class GifLoadingView extends LoadingView {
//
//	private GifImageView mGifView;
//	private GifDrawable mGifDrawable;
//	private int mGifRawId = -1;
//
//	public GifLoadingView(Context context) {
//		super(context);
//	}
//
//	@Override
//	public void init(Context context) {
//		mLoadingView = new RelativeLayout(context);
//		LinearLayout l = new LinearLayout(context);
//		l.setOrientation(LinearLayout.VERTICAL);
//		l.setGravity(Gravity.CENTER);
//		LinearLayout.LayoutParams lparam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//		mGifView = new GifImageView(context);
//		l.addView(mGifView, lparam);
//		mMessageTV = new TextView(context);
//		l.addView(mMessageTV, lparam);
//
//		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//		params.addRule(RelativeLayout.CENTER_IN_PARENT);
//		((ViewGroup)mLoadingView).addView(l, params);
//
//		ViewGroup.LayoutParams param = mLoadingView.getLayoutParams();
//		if(param != null){
//			DisplayMetrics dm = context.getResources().getDisplayMetrics();
//			param.width = dm.widthPixels;
//			param.height = dm.heightPixels;
//			mLoadingView.setLayoutParams(param);
//		}
//		mLoadingView.setBackgroundColor(0x88000000);
//		mLoadingView.setClickable(true);
//	}
//
//	@Override
//	public void reset() {
//		try {
//			if (mGifDrawable != null) {
//				mGifDrawable.recycle();
//				mGifDrawable = null;
//			}
//			mGifView.destroyDrawingCache();
//		} catch (Exception e) {
//
//		}
//	}
//
//	public GifImageView getGifView() {
//		return mGifView;
//	}
//
//	public GifDrawable getGifDrawable() {
//		return mGifDrawable;
//	}
//
//	public void startGif() {
//		try {
//			if (mGifDrawable != null)
//				mGifDrawable.start();
//		} catch (OutOfMemoryError e) {
//			LogM.e(e.toString());
//		} catch (Exception e) {
//			LogM.e(e.toString());
//		}
//	}
//
//	public void stopGif() {
//		try {
//			if (mGifDrawable != null)
//				mGifDrawable.stop();
//		} catch (OutOfMemoryError e) {
//			LogM.e(e.toString());
//		} catch (Exception e) {
//			LogM.e(e.toString());
//		}
//	}
//
//	public TextView getMessageTv() {
//		return mMessageTV;
//	}
//
//	public int getGifViewSrc() {
//		return mGifRawId;
//	}
//
//	public void setGifViewSrc(int gifRawId) {
//		try {
//			if (mGifDrawable != null) {
//				mGifDrawable.recycle();
//				mGifDrawable = null;
//			}
//			mGifDrawable = new GifDrawable(mContext.getResources(), gifRawId);
//			mGifView.setImageDrawable(mGifDrawable);
//			mGifRawId = gifRawId;
//		} catch (Exception e) {
//			LogM.e(e.toString());
//		} catch (OutOfMemoryError e) {
//			LogM.e(e.toString());
//		}
//	}
//
//	public void setOnClickListener(OnClickListener onClickListener) {
//		mGifView.setOnClickListener(onClickListener);
//		mMessageTV.setOnClickListener(onClickListener);
//	}
//}
