package com.dangdang.gx.ui.utils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;


import java.util.List;

public class AppUtil {
	
	private static AppUtil mUtil;
	
	protected Context mContext;	
	private Typeface mTypeface;

	public synchronized static AppUtil getInstance(Context context){
		if(mUtil == null)
			mUtil = new AppUtil(context);
		return mUtil;		
	}
	
	public AppUtil(Context context){
		mContext = context.getApplicationContext();
		init();
	}
	
	public void init() {

	}

	
	/**
	 * 停止服务
	 */
	public void stopService(Class<? extends Object> serviceClass) {
		if (serviceClass == null)
			return;
		ActivityManager aManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> runServiceList = aManager
				.getRunningServices(100);
		for (int i = 0; i < runServiceList.size(); i++) {
			RunningServiceInfo runningServiceInfo = runServiceList.get(i);
			ComponentName serviceCMP = runningServiceInfo.service;

			if (serviceClass.getName().equals(serviceCMP.getClassName())) {
				mContext.stopService(new Intent(mContext, serviceClass));
				break;
			}
		}
	}

	private void release() {
		mUtil = null;
	}

	/**
	 * 退出应用时释放资源、服务
	 */
	public void exitApp(boolean exit) {
		release();
		if (exit)
			System.exit(0);
	}

	public void setTypeface(String path) {
		try {
			mTypeface = Typeface.createFromFile(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Typeface getTypeface() {
		return mTypeface;
	}
}
