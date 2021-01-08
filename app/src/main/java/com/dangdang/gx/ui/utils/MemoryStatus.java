package com.dangdang.gx.ui.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;

public class MemoryStatus {
	
	protected final static int ERROR = -1;

	public final static int MIN_SPACE = 1024*1024*10;
	
	/**
	 * 外部存储是否可用
	 * 
	 * @return
	 */
	public static boolean externalMemoryAvailable() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}
	
	public static boolean isSdcardAndMemAvailable(long value){
		if(externalMemoryAvailable()){
			return getAvailableExternalMemorySize() >= value;
		}
		return getAvailableInternalMemorySize() >= value;
	}
	

	/**
	 * 获取手机内部可用空间大小
	 * 
	 * @return  byte size
	 */
	public static long getAvailableInternalMemorySize() {
		long blockSize = 0;
		long availableBlocks = 0;
		try {
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			blockSize = stat.getBlockSize();
			availableBlocks = stat.getAvailableBlocks();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return availableBlocks * blockSize;
	}

	/**
	 * 获取手机内部空间大小
	 * 
	 * @return  byte size
	 */
	public static long getTotalInternalMemorySize() {
		long blockSize = 0;
		long totalBlocks = 0;
		try {
			File path = Environment.getDataDirectory();
			StatFs stat = new StatFs(path.getPath());
			blockSize = stat.getBlockSize();
			totalBlocks = stat.getBlockCount();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return totalBlocks * blockSize;
	}

	/**
	 * 获取手机外部可用空间大小
	 * 
	 * @return  byte size
	 */
	public static long getAvailableExternalMemorySize() {
		if (externalMemoryAvailable()) {
			long blockSize = 0;
			long availableBlocks = 0;
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				blockSize = stat.getBlockSize();
				availableBlocks = stat.getAvailableBlocks();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return availableBlocks * blockSize;
		} else {
			return ERROR;
		}
	}

	/**
	 * 获取手机外部空间大小
	 * 
	 * @return byte size
	 */
	public static long getTotalExternalMemorySize() {
		if (externalMemoryAvailable()) {
			long blockSize = 0;
			long totalBlocks = 0;
			try {
				File path = Environment.getExternalStorageDirectory();
				StatFs stat = new StatFs(path.getPath());
				blockSize = stat.getBlockSize();
				totalBlocks = stat.getBlockCount();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return totalBlocks * blockSize;
		} else {
			return ERROR;
		}
	}
	
	/**
	 * 
	 * @param filesize  byte size
	 * @param faultTolerantSize  byte size
	 * @return
	 */
	public static boolean hasAvailable(int filesize, int faultTolerantSize){
		
		boolean ret = false;
		long availableSize = getAvailableExternalMemorySize();
		if((availableSize - faultTolerantSize) 
				> filesize){
			ret = true;
		}
//		Log.i("MemoryStatus", "hasAvailable[" + filesize + "=" + availableSize + "]");
		return ret;
	}
	
	public static boolean hasMemAvailable(){
		
		return getAvailableInternalMemorySize() >= MIN_SPACE;
	}

	/**
	 * 
	 * @param size  byte size
	 * @return
	 */
	public static String formatSize(long size) {
		String suffix = null;

		if (size >= 1024) {
			suffix = "KiB";
			size /= 1024;
			if (size >= 1024) {
				suffix = "MiB";
				size /= 1024;
			}
		}

		StringBuilder resultBuffer = new StringBuilder(Long.toString(size));
		int commaOffset = resultBuffer.length() - 3;
		while (commaOffset > 0) {
			resultBuffer.insert(commaOffset, ',');
			commaOffset -= 3;
		}

		if (suffix != null)
			resultBuffer.append(suffix);
		return resultBuffer.toString();
	}
}