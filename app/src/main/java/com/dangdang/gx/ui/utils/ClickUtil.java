package com.dangdang.gx.ui.utils;

public class ClickUtil {
	private static long mLastClickTime;

	public static boolean checkFastClick() {
		long time = System.currentTimeMillis();
		long timeD = time - mLastClickTime;
		if (0 < timeD && timeD < 500) {
			mLastClickTime = time;
			return true;
		}
		mLastClickTime = time;
		return false;
	}

}
