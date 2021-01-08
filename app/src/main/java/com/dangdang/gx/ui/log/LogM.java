package com.dangdang.gx.ui.log;

import android.util.Log;

import java.util.Locale;

public class LogM {

	private static String TAG_ORDER = "com.universitylibrary";

	/**
	 * print log
	 */
	private static boolean isPrintLogV = true;
	private static boolean isPrintLogD = true;
	private static boolean isPrintLogI = true;
	private static boolean isPrintLogW = true;
	private static boolean isPrintLogE = true;

	private boolean order = false;

	private String tag;

	/**
	 * 设置log等级输出
	 * 
	 * @param v
	 * @param d
	 * @param i
	 * @param w
	 * @param e
	 */
	public static void initLogLevel(boolean v, boolean d, boolean i, boolean w, boolean e) {
		isPrintLogV = v;
		isPrintLogD = d;
		isPrintLogI = i;
		isPrintLogW = w;
		isPrintLogE = e;
	}

	private LogM(Class<?> clazz) {
		tag = clazz.getSimpleName();
	}

	public static LogM getLog(Class<?> clazz) {
		if (clazz == null) {
			throw new NullPointerException("[clazz can't null]");
		}
		return new LogM(clazz);
	}

	public void setOrderTag(String tag) {
		TAG_ORDER = tag;
	}

	public boolean isOrder() {
		return order;
	}

	public void i(boolean order, String msg) {
		if (isPrintLogI) {
			if (order) {
				Log.i(TAG_ORDER, "[" + tag + "] " + msg);
			} else {
				Log.i(TAG_ORDER, "[" + tag + "] " + msg);
			}
		}
	}

	public void w(boolean order, String msg) {
		if (isPrintLogW) {
			if (order) {
				Log.w(TAG_ORDER, "[" + tag + "] " + msg);
			} else {
				Log.w(TAG_ORDER, "[" + tag + "] " + msg);
			}
		}
	}

	public void v(boolean order, String msg) {
		if (isPrintLogV) {
			if (order) {
				Log.v(TAG_ORDER, "[" + tag + "] " + msg);
			} else {
				Log.v(TAG_ORDER, "[" + tag + "] " + msg);
			}
		}
	}

	public static void v(String format, Object... args) {
		if (isPrintLogV) {
			Log.v(TAG_ORDER, buildMessage(format, args));
		}
	}

	public void d(boolean order, String msg) {
		if (isPrintLogD) {
			if (order) {
				Log.d(TAG_ORDER, "[" + tag + "] " + msg);
			} else {
				Log.d(TAG_ORDER, "[" + tag + "] " + msg);
			}
		}
	}

	public void e(boolean order, String msg) {
		if (isPrintLogE) {
			if (order) {
				Log.e(TAG_ORDER, "[" + tag + "] " + msg);
			} else {
				Log.e(TAG_ORDER, "[" + tag + "] " + msg);
			}
		}
	}

	// --- static --- //
	public static void d(String msg) {
		if (isPrintLogD) {
			d("LogM", msg);
		}
	}

	public static void l(String msg) {
		if (isPrintLogD) {
			e("tagg", msg);
		}
	}

	public static void i(String tag, String msg) {
		if (isPrintLogI) {
			Log.i(TAG_ORDER, "[" + tag + "] " + msg);
		}
	}

	public static void w(String tag, String msg) {
		if (isPrintLogW) {
			Log.w(TAG_ORDER, "[" + tag + "] " + msg);
		}
	}

	public static void v(String tag, String msg) {
		if (isPrintLogV) {
			Log.v(TAG_ORDER, "[" + tag + "] " + msg);
		}
	}

	public static void d(String tag, String msg) {
		if (isPrintLogD) {
			Log.d(TAG_ORDER, "[" + tag + "] " + msg);
		}
	}

	public static void e(String tag, String msg) {
		if (isPrintLogE) {
			Log.e(TAG_ORDER, "[" + tag + "] " + msg);
		}
	}

	public static void e(String msg) {
		if (isPrintLogE) {
			Log.e(TAG_ORDER, "[" + null + "] " + msg);
		}
	}

	// 保存错误日记
	/*
	 * public static void f(boolean isAddTime, String message){ if(isPrintLogE){
	 * DangdangFileManager.saveErrorMessage(message+"\r\n", isAddTime); } }
	 */

	private static String buildMessage(String format, Object... args) {
		String msg = (args == null) ? format : String.format(Locale.US, format, args);
		StackTraceElement[] trace = new Throwable().fillInStackTrace().getStackTrace();

		String caller = "<unknown>";
		// Walk up the stack looking for the first caller outside of VolleyLog.
		// It will be at least two frames up, so start there.
		for (int i = 2; i < trace.length; i++) {
			Class<?> clazz = trace[i].getClass();
			if (!clazz.equals(LogM.class)) {
				String callingClass = trace[i].getClassName();
				callingClass = callingClass.substring(callingClass.lastIndexOf('.') + 1);
				callingClass = callingClass.substring(callingClass.lastIndexOf('$') + 1);

				caller = callingClass + "." + trace[i].getMethodName();
				break;
			}
		}
		return String.format(Locale.US, "[%d] %s: %s", Thread.currentThread().getId(), caller, msg);
	}
}