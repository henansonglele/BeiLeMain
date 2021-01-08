package com.dangdang.gx.ui.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.dangdang.gx.ui.log.LogM;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CommonUtil {

	// 获取指定区间的随机数
	public static int getRandomInt(int start, int end) {
		return (int) (Math.random() * (end - start + 1)) + start;
	}
	
	public static String getPermanentId(Context context) {
		String permanentId = "";
		ConfigManager configManager = new ConfigManager(context.getApplicationContext());
		SharedPreferences sp = configManager.getPreferences();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.US);
		String year = dateFormat.format(new java.util.Date());
		LogM.i("getPermanentId", "getPermanentId.year="+year);
		if( year.equals(sp.getString(ConfigManager.KEY_INIT_PERMANENTID, year))==true){
			SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmssSSS", Locale.US);
			String date = sDateFormat.format(new java.util.Date());
			permanentId = date+getRandomInt(100000,900000)+getRandomInt(100000,900000)+getRandomInt(100000,900000);
			LogM.d("LogM", "permanentId=:" + permanentId);
			
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(ConfigManager.KEY_INIT_PERMANENTID, permanentId);
			editor.commit();
		}else{
			permanentId = sp.getString(ConfigManager.KEY_INIT_PERMANENTID, year);
		}
		return permanentId;
	}
}
