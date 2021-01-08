package com.dangdang.gx.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.dangdang.gx.R;
import com.dangdang.gx.ui.view.DDTextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * ui相关工具类
 * 
 * @author xiaruri
 * 
 */
public class UiUtil {
	private static long mLastClickTime;
//	private static Toast mToast;
//	private static TextView mTextView;

	/**
	 * 显示一个toast
	 * 
	 * @param msg
	 */
	public static void showToast(Context context, String msg) {
		showToast(context, msg, Toast.LENGTH_SHORT);
	}

	/**
	 * 显示一个toast
	 * 
	 * @param msg
	 */
	public static void showToast(Context context, String msg, int duration) {
		if(context == null)
			return;
		context = context.getApplicationContext();
//		if (mToast == null || mTextView == null) {
			Toast mToast = new Toast(context);
			DDTextView mTextView = new DDTextView(context);
			mTextView.setGravity(Gravity.CENTER);
			mTextView.setTextColor(context.getResources().getColor(R.color.white));
			try{
				mTextView.setBackgroundResource(getToastBg());
			}catch(Throwable e){
				e.printStackTrace();
			}
			mToast.setView(mTextView);
//		}
		mTextView.setText(msg);
		mToast.setDuration(duration);
		mToast.show();
	}

	/**
	 * 显示一个toast
	 * 
	 * @param resId
	 */
	public static void showToast(Context context, int resId) {
		showToast(context, resId, Toast.LENGTH_SHORT);
	}

	/**
	 * 显示一个toast
	 * 
	 * @param resId
	 */
	public static void showToast(Context context, int resId, int duration) {
		if(context == null)
			return;
		context = context.getApplicationContext();
		String str = context.getString(resId);
		showToast(context, str, duration);
	}

	/**
	 * 扩展。区分不同应用
	 * 
	 * @return
	 */
	private static int getToastBg() {
		return R.drawable.toast_frame;

	}

	/**
	 * 根据view弹出软键盘
	 * 
	 * @param view
	 */
	public static void showSoftInput(final View view) {
		try {
			if (view != null) {
				view.requestFocus();
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					public void run() {
						InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
						inputManager.showSoftInput(view, 0);
					}
				}, 200);
			}
		} catch (Exception e) {
	//		e.printStackTrace();
		}
	}

	/**
	 * 根据view隐藏软键盘
	 * 
	 * @param view
	 */
	public static void hideSoftInput(View view) {
		try {
			if (view != null) {
				InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

	/**
	 * 根据上下文对象隐藏软键盘
	 * 
	 * @param context
	 */
	public static void hideInput(Context context) {
		try {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive())
				imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (Exception e) {
//			e.printStackTrace();
		}
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		if (context != null) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dpValue * scale + 0.5f);
		}
		return (int) dpValue;
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		if (context != null) {
			final float scale = context.getResources().getDisplayMetrics().density;
			return (int) (pxValue / scale + 0.5f);
		}
		return (int) pxValue;
	}

	/**
	 * 是否是快速点击，防止快速点击某个按钮打开多个页面
	 * 
	 * @return 如果点击距上次点击时间小于500毫秒，则返回true，否则返回false
	 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - mLastClickTime;
		if (0 < timeD && timeD < 500) {
			return true;
		}
		mLastClickTime = time;
		return false;
	}

}
