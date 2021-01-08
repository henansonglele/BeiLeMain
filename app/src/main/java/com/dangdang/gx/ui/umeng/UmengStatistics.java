package com.dangdang.gx.ui.umeng;

import android.content.Context;

import com.dangdang.gx.ui.log.LogM;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.commonsdk.listener.OnGetOaidListener;

import java.util.Map;

public class UmengStatistics {
	private static String APP_KEY = "5ff2c54aadb42d58269ae1e7";
	public static String APP_OPEN = "app_open";

	public static boolean openStatics = false;
	public static String  defaultChannelValue = "30000";


	public static void preInit(Context context, String channel) {
		if (!openStatics) {
			return;
		}
		UMConfigure.preInit(context, APP_KEY, channel);

		LogM.d("UmengSDK", "preInit umeng sdk, channel: " + channel);
	}

	public static void init(Context context, String channel) {
		if (!openStatics) {
			return;
		}

		// 打开调试log
//		UMConfigure.setLogEnabled(true);

		UMConfigure.init(context, APP_KEY, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
		MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO);

		LogM.d("UmengSDK", "init umeng sdk, channel: " + channel);
	}

	public static void onResume(Context context) {

		if (!openStatics) {
			return;
		}

		MobclickAgent.onResume(context);
	}

	public static void onPause(Context context) {

		if (!openStatics) {
			return;
		}

		MobclickAgent.onPause(context);
	}

	public static void onPageStart(String tag) {

		if (!openStatics) {
			return;
		}

		MobclickAgent.onPageStart(tag);
	}

	public static void onPageEnd(String tag) {

		if (!openStatics) {
			return;
		}

		MobclickAgent.onPageEnd(tag);
	}

	// public static void onFail(Context context){
	// if(DangdangConfig.isDevelopEnv())
	// return;
	// MobclickAgent.onFail(context);
	// }
	/**
	 * 退出应用
	 * 
	 * @param context
	 */
	public static void onKillProcess(Context context) {

		if (!openStatics) {
			return;
		}

		MobclickAgent.onKillProcess(context);
	}

	public static void reportError(Context context, String error) {

		if (!openStatics) {
			return;
		}

		MobclickAgent.reportError(context, error);
	}

	public static void onEvent(Context context, String event_id) {

		if (!openStatics) {
			return;
		}
		MobclickAgent.onEvent(context, event_id);
	}

	public static void onEvent(Context context, String event_id,
							   Map<String, String> params) {
		if (!openStatics) {
			return;
		}
		MobclickAgent.onEvent(context, event_id, params);
	}

	public static void getOaid(Context context, OnGetOaidListener listener) {
		if (!openStatics) {
			return;
		}
		UMConfigure.getOaid(context, listener);
	}
}
