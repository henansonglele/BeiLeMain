package com.dangdang.gx.ui.utils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;


import com.dangdang.gx.ui.log.LogM;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;

public class DRUiUtility {
	private static DRUiUtility mDRUiUtility = null;
	private static Context mContext;
	private static int mWidth = 0;
	private static int mHeight = 0;
	private static float mDensity = 0;
	private static boolean mScreenIsLarge;
	private static boolean mLargeScreenForSpeed;
	private static final int LargeScreenSize = 7;
	private static final double LargeScreenSizeForSpeed = 8;
	private static double mScreenSize = 0;

	public static int mCoverWidth = 80;
	public static int mCoverHeight = 100;

	private DRUiUtility() {
	}

	public static synchronized DRUiUtility getUiUtilityInstance() {
		if (null == mDRUiUtility) {
			mDRUiUtility = new DRUiUtility();
		}
		return mDRUiUtility;
	}

	public void setContext(Activity ac) {
		if(ac == null)
			return;
		
		mContext = ac.getApplicationContext();
		
		DisplayMetrics dm = new DisplayMetrics();
		Display d = ac.getWindowManager().getDefaultDisplay();
		d.getMetrics(dm);
		mWidth = dm.widthPixels;
		mHeight = dm.heightPixels;
		mDensity = dm.density;

		mCoverWidth = (int) (mCoverWidth * mDensity);
		mCoverHeight = (int) (mCoverHeight * mDensity);

		double diagonalPixels = Math.sqrt(Math.pow(mWidth, 2)
				+ Math.pow(mHeight, 2));
		double screenSize = diagonalPixels / (160 * mDensity);
		// mConfigManager = new ConfigManager(mContext.getApplicationContext());
		initLargeScreen(screenSize);
		mScreenSize = screenSize;
	}

	private void initLargeScreen(double screenSize) {
		mLargeScreenForSpeed = false;
		if (screenSize >= LargeScreenSize) {
			mScreenIsLarge = true;
		} else {
			mScreenIsLarge = false;
		}
		if (screenSize >= LargeScreenSizeForSpeed) {
			mLargeScreenForSpeed = true;
		}
	}

	public void DontKeepContext(Activity ac) {
		if (ac == null)
			return;
		DisplayMetrics dm = new DisplayMetrics();
		Display d = ac.getWindowManager().getDefaultDisplay();
		d.getMetrics(dm);
		mWidth = dm.widthPixels;
		mHeight = dm.heightPixels;
		mDensity = dm.density;

		double diagonalPixels = Math.sqrt(Math.pow(mWidth, 2)
				+ Math.pow(mHeight, 2));
		double screenSize = diagonalPixels / (160 * mDensity);

		initLargeScreen(screenSize);
	}

	public static boolean getPadScreenIsLarge() {
		return mScreenIsLarge;
	}

	public static boolean getLargeScreenSpeed() {
		return mLargeScreenForSpeed;
	}

	public static int getScreenWith() {
		return mWidth;
	}

	public static int getScreenHeight() {
		return mHeight;
	}

	public static float getDensity() {
		return mDensity < 1 ? 1 : mDensity;
	}

	public static double getScreenSize() {
		return mScreenSize;
	}

	public static String getPhoneModel() {
		String model = android.os.Build.MODEL;
		return model;
	}

	public static String getPhoneManufacturer() {
		String manufacturer = android.os.Build.MANUFACTURER;
		return manufacturer;
	}

	public static int getDisplayDPI() {
		return (int) (160 * mDensity);
	}

	public static int getStatusHeight(Context context) {
		int height = 0;
		try {
			@SuppressWarnings("rawtypes")
			Class c = Class.forName("com.android.internal.R$dimen");
			Object obj = c.newInstance();
			Field field = c.getField("status_bar_height");
			int x = Integer.parseInt(field.get(obj).toString());
			
			height = context.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return height;
	}

	public static final String BASE_DIRECTORY = "wallpapers/";

	/*
	 * public Bitmap getBgBitmap() { Bitmap bitmap = null; bitmap =
	 * getBgBitmap("wallpapers/sepia.jpg"); return bitmap; }
	 */

	public Bitmap getBgBitmap(String assestPath) {
		Bitmap bitmap = null;
		if (assestPath == null || "".equals(assestPath)) {
			return null;
		}
		assestPath = BASE_DIRECTORY + assestPath;
		InputStream inStream = null;
		try {
			inStream = mContext.getAssets().open(assestPath);
			final Bitmap fileBitmap = BitmapFactory.decodeStream(inStream);
			final int w = fileBitmap.getWidth();
			final int h = fileBitmap.getHeight();
			final Bitmap wallpaperBmp = Bitmap.createBitmap(2 * w, 2 * h,
					fileBitmap.getConfig());

			final Canvas wallpaperCanvas = new Canvas(wallpaperBmp);
			final Paint wallpaperPaint = new Paint();

			Matrix m = new Matrix();
			wallpaperCanvas.drawBitmap(fileBitmap, m, wallpaperPaint);
			m.preScale(-1, 1);
			m.postTranslate(2 * w, 0);
			wallpaperCanvas.drawBitmap(fileBitmap, m, wallpaperPaint);
			m.preScale(1, -1);
			m.postTranslate(0, 2 * h);
			wallpaperCanvas.drawBitmap(fileBitmap, m, wallpaperPaint);
			m.preScale(-1, 1);
			m.postTranslate(-2 * w, 0);
			wallpaperCanvas.drawBitmap(fileBitmap, m, wallpaperPaint);
			bitmap = wallpaperBmp;

			BitmapUtil.recycle(fileBitmap);

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (inStream != null) {
				try {
					inStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return bitmap;
	}

	/**
	 * @param resId
	 * @return 可能是null
	 */
	public Bitmap getBitmapByRsource(int resId) {

		try {
			return BitmapFactory.decodeResource(mContext.getResources(), resId);
		} catch (Throwable e) {
			LogM.e(getClass().getSimpleName(),
					" create bmp by resource. error: " + e);
		}
		return null;
	}

	public static int getScreenBrightness(Context context) {
		int screenBrightness = 255;
		ContentResolver contentResolver = context.getContentResolver();
		try {
			if (isAutoBrightness(contentResolver)) {
				screenBrightness = Settings.System.getInt(contentResolver,
						Settings.System.SCREEN_BRIGHTNESS,
						Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
			} else {
				screenBrightness = Settings.System.getInt(contentResolver,
						Settings.System.SCREEN_BRIGHTNESS,
						Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
			}
		} catch (Exception e) {
			screenBrightness = 100;
		}
		return screenBrightness;
	}

	public static boolean isAutoBrightness(ContentResolver contentResolver) {
		boolean automicBrightness = false;
		try {
			automicBrightness = Settings.System.getInt(contentResolver,
					Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
		} catch (SettingNotFoundException e) {
			e.printStackTrace();
		}
		return automicBrightness;
	}

	public static void setActivityFullScreenStatus(Activity activity,
			boolean isFullScreen) {
		if (isFullScreen) {
			activity.getWindow().setFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else {
			activity.getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}
	}

}
