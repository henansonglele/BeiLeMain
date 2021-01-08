package com.dangdang.gx.ui.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import com.dangdang.gx.ui.log.LogM;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.UUID;

public class ConfigManager {

	private static final String DANG_READER_PREF = "dang_university_library_config";
	public static final String KEY_INIT_PERMANENTID = "init_permanentId";

	private Context context;
	private SharedPreferences pref = null;

	public ConfigManager(Context context) {
		super();
		this.context = context.getApplicationContext();
		pref = context.getSharedPreferences(DANG_READER_PREF,
				Context.MODE_PRIVATE);
	}

	public SharedPreferences getPreferences() {
		return pref;
	}

	public SharedPreferences.Editor getEditor() {
		return pref.edit();
	}

	public static SharedPreferences getPreferences(Context context) {
		return context.getSharedPreferences(DANG_READER_PREF,
				Context.MODE_PRIVATE);
	}

	/**
	 * 获取设备id策略： 1，优先IMEI串号； 2，MAC地址串； 3，ANDROID_ID串号(获取 Android 设备的唯一标识码 )；
	 * 
	 * @return
	 */
	@SuppressLint("MissingPermission")
  public String getDeviceId() {
		String serialNo = pref.getString("device_id", null);
		if (TextUtils.isEmpty(serialNo)) {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String deviceId = null;//
			try {
				deviceId = tm.getDeviceId();
			} catch (Exception e) {
				e.printStackTrace();
			}
			String macAddress = getDeviceMacAddress();
			String deviceAndroidId = getDeviceAndroidId();
			String str = deviceId + macAddress + deviceAndroidId;
			if ("null".equalsIgnoreCase(deviceId)
					&& "null".equalsIgnoreCase(macAddress)
					&& "null".equalsIgnoreCase(deviceAndroidId)) {
				str = "dandangreader" + UUID.randomUUID().toString();
			}
			if (TextUtils.isEmpty(str) || "null".equalsIgnoreCase(str)) {
				str = "dandangreader" + UUID.randomUUID().toString();
			}
			serialNo=MD5Util.getMD5Str(str);
			SharedPreferences.Editor editor = getEditor();
			editor.putString("device_id", serialNo);
			editor.commit();
		}
		return serialNo;
	}

	/**
	 * 获取配置文件中版本号versionName android:versionName="1.0.6"
	 */
	public String getVersionName() {
		String versionName = "";
		try {
			PackageInfo packInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionName = packInfo.versionName;
			LogM.d("versionName=" + versionName);
		} catch (Exception e) {
			LogM.d("Failed to get versionName: " + e.getMessage());
		}
		return versionName;
	}

	public int getVersionCode() {
		int versionCode = 0;
		try {
			PackageInfo packInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			versionCode = packInfo.versionCode;
			LogM.d("versionCode=" + versionCode);
		} catch (Exception e) {
			LogM.d("Failed to get versionCode: " + e.getMessage());
		}
		return versionCode;
	}

	/**
	 * 是否是V4.0版本
	 */
	public boolean IsNewVersion() {
		if (("4.0.0").equals(getVersionName())) {
			return true;
		}
		return false;
	}

	public String getPackageName() {
		return context.getPackageName();
	}

	/**
	 * 获取配置文件中渠道号
	 * 
	 * @return
	 */
	public String getChannelId() {
		final String SPF_KEY_UMENG_CHANNEL = "spf_key_umeng_channel";
		SharedPreferences sf = getPreferences();
		String channelId = sf.getString(SPF_KEY_UMENG_CHANNEL,DangdangConfig.CHANNEL_ID);
//		if(channelId == -1){
//			channelId = getChannelIdFromManifest();
//			SharedPreferences.Editor editor = sf.edit();
//			editor.putInt(SPF_KEY_UMENG_CHANNEL,channelId);
//			editor.commit();
//		}

		return channelId;
	}
	public int getChannelIdFromManifest() {

		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			int channelId = bundle.getInt("UMENG_CHANNEL");
			LogM.d("channelId=" + channelId);
			return channelId;
		} catch (NameNotFoundException e) {
			LogM.d("Failed to load meta-data, NameNotFound: " + e.getMessage());
		} catch (NullPointerException e) {
			LogM.d("Failed to load meta-data, NullPointer: " + e.getMessage());
		}
		return -1;
	}

	public void setChannelId(String channelId) {
		final String SPF_KEY_UMENG_CHANNEL = "spf_key_umeng_channel";
		SharedPreferences sf = getPreferences();
		SharedPreferences.Editor editor = sf.edit();
		editor.putString(SPF_KEY_UMENG_CHANNEL,channelId);
		editor.commit();
	}

	/*
	 * 服务器版本号
	 */
	public String getServerVesion() {
		String vesion = "";
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			vesion = bundle.getString("SERVER_VERSION");
		} catch (NameNotFoundException e) {
			LogM.d("Failed to load meta-data, NameNotFound: " + e.getMessage());
		} catch (NullPointerException e) {
			LogM.d("Failed to load meta-data, NullPointer: " + e.getMessage());
		}
		return vesion;
	}

	/*
	 * 活动id
	 */
	public String getActivityId() {
		String activityId = "";
		int id = 0;
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			id = bundle.getInt("ACTIVITY_ID");
			activityId = String.valueOf(id);
		} catch (NameNotFoundException e) {
			LogM.d("Failed to load meta-data, NameNotFound: " + e.getMessage());
		} catch (NullPointerException e) {
			LogM.d("Failed to load meta-data, NullPointer: " + e.getMessage());
		}
		return activityId;
	}

	/**
	 * 百度push appkey
	 * 
	 * @return
	 */
	public String getBaiduPushAppKey() {
		String appkey = "";
		try {
			ApplicationInfo ai = context.getPackageManager()
					.getApplicationInfo(context.getPackageName(),
							PackageManager.GET_META_DATA);
			Bundle bundle = ai.metaData;
			appkey = bundle.getString("BAIDU_PUSH_APP_KEY");
			LogM.d("appkey=" + appkey);
		} catch (NameNotFoundException e) {
			LogM.d("Failed to load meta-data, NameNotFound: " + e.getMessage());
		} catch (NullPointerException e) {
			LogM.d("Failed to load meta-data, NullPointer: " + e.getMessage());
		}
		return appkey;
	}

	/**
	 * 获取应用名
	 */
	public String getAppName() {
		String appName = "";
		try {
			PackageInfo packInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			appName = packInfo.applicationInfo.toString();
			LogM.d("appName=" + appName);
		} catch (Exception e) {
			LogM.d("Failed to get versionName: " + e.getMessage());
		}
		return appName;
	}

	/**
	 * 获取mac地址作为认证
	 * 
	 * @return
	 */
	public String getDeviceMacAddress() {

		String macAddr = "";
		try {
			WifiManager wifi = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo info = wifi.getConnectionInfo();
			macAddr = info.getMacAddress();
		} catch (Exception e) {
			macAddr = null;
			e.printStackTrace();
		}

		return macAddr;
	}

	/**
	 * 获取设备的唯一标识码
	 */
	public String getDeviceAndroidId() {
		String android_id = Secure.getString(context.getContentResolver(),
				Secure.ANDROID_ID);
		return android_id;
	}

	/**
	 * 获取设备cpu序列号
	 * 
	 * @return CPU序列号(16位) 未使用过 读取失败为"0000000000000000"
	 */
	public static String getCPUSerial() {
		String str = "", cpuAddress = "0000000000000000";
		try {
			// 读取CPU信息
			// Process pp = Runtime.getRuntime().exec("cat /proc/cpuinfo");
			Process pp = Runtime.getRuntime().exec("system/proc/cpuinfo");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			// 查找CPU序列号
			for (int i = 1; i < 500; i++) {
				str = input.readLine();
				if (str != null) {
					// 查找到序列号所在行
					// if (str.indexOf("Serial") > -1) {
					// 提取序列号
					str = str.substring(str.indexOf(":") + 1, str.length());
					// 去空格
					// cpuAddress = strCPU.trim();
					cpuAddress += str.trim();
					// break;
					// }
				} else {
					// 文件结尾
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
		}
		return cpuAddress;
	}

	/**
	 * 手机型号
	 */
	public String getPhoneModel() {
		return Build.MODEL;
	}

	/**
	 * 手机号
	 */
	public String getPhoneNumber() {
		String telNum = "";
		try {
			TelephonyManager mTelephonyMgr;
			mTelephonyMgr = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			telNum = mTelephonyMgr.getLine1Number();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return telNum;
	}

	/**
	 * OS version
	 */
	public String getOSVersion() {
		return Build.VERSION.RELEASE;
	}
}
