package com.dangdang.gx.ui.utils;

import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关工具类
 * 
 * @author xiaruri
 * 
 */
public class StringUtil {
	public abstract static class CommonClickableSpan extends ClickableSpan {
		@Override
	    public void updateDrawState(TextPaint textPaint) {
	        textPaint.setUnderlineText(false);
	    }
	}
	
	private final static int BOOK_STATUS_HAS_DOWNLOAD = 5;

	/**
	 * 将字符串解析为int型数
	 * 
	 * @param str
	 *            待解析字符串
	 * @param defaultValue
	 *            解析失败返回的默认值
	 * @return 解析后的数值
	 */
	public static int parseInt(String str, int defaultValue) {
		int result = defaultValue;
		try {
			result = Integer.valueOf(str.trim());
		} catch (Exception e) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * 将字符串解析为long型数.
	 * 
	 * @param str
	 *            待解析字符串
	 * @param defaultValue
	 *            解析失败返回的默认值
	 * @return 解析后的数值
	 */
	public static long parseLong(String str, long defaultValue) {
		long result = defaultValue;
		try {
			result = Long.valueOf(str.trim());
		} catch (Exception e) {
			result = defaultValue;
		}
		return result;
	}

	public static long parseLong(String str) {
		return parseLong(str, -1);
	}

	/**
	 * 将字符串解析为floag型数
	 * 
	 * @param str
	 *            待解析字符串
	 * @param defaultValue
	 *            解析失败返回的默认值
	 * @return 解析后的数值
	 */
	public static float parseFloat(String str, float defaultValue) {
		float result = defaultValue;
		try {
			result = Float.valueOf(str.trim());
		} catch (Exception e) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * 将字符串解析为double型数
	 * 
	 * @param str
	 *            待解析字符串
	 * @param defaultValue
	 *            解析失败返回的默认值
	 * @return 解析后的数值
	 */
	public static double parseDouble(String str, double defaultValue) {
		double result = defaultValue;
		try {
			result = Double.valueOf(str.trim());
		} catch (Exception e) {
			result = defaultValue;
		}
		return result;
	}

	/**
	 * 判断字符串是否为空
	 * 
	 * @param str
	 *            字符串
	 * @return 当字符串为null或者为"null"或者length为0时返回true，否则返回false
	 */
	public static boolean isEmpty(String str) {
		if (TextUtils.isEmpty(str) || "null".equals(str)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 通过长度限制截取字符串，截取完以后会在字符串末尾加上endFlag
	 * 
	 * @param targetStr
	 *            需要截取的字符串
	 * @param lenLimit
	 *            截取的长度
	 * @param endFlag
	 *            末尾字符串
	 * @return
	 */
	public static String subStringByLenWithEndFlag(String targetStr,
			int lenLimit, String endFlag) {
		String newStr = "";

		if (!TextUtils.isEmpty(targetStr) && lenLimit > 0) {
			if (targetStr.length() > lenLimit) {
				newStr = targetStr.substring(0, lenLimit) + endFlag;
			} else {
				newStr = targetStr;
			}
		}

		return newStr;
	}

	/**
	 * @param str
	 * @return 把全角字符转换成半角
	 */
	public static String ToDBC(String str) {
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	/**
	 * 对包含token的字符串进行染色 colorSpanList的size必须和text中包含token的对数一致，否则返回空字符串
	 * 
	 * @param text
	 *            需要设置不同颜色的字符串
	 * @param colorSpanList
	 *            染色的颜色列表
	 * @param token
	 *            染色标记
	 * @return
	 */
	public static CharSequence colorToText(CharSequence text,
			List<ForegroundColorSpan> colorSpanList, String token) {
		try {
			if (colorSpanList != null) {
				for (int i = 0; i < colorSpanList.size(); i++) {
					text = setSpanBetweenTokens(text, token,
							colorSpanList.get(i));
				}
			}
		} catch (Exception e) {
			return "";
		}
		return text;
	}

	private static CharSequence setSpanBetweenTokens(CharSequence text,
			String token, CharacterStyle... cs) {
		// Start and end refer to the points where the span will apply
		int tokenLen = token.length();
		int start = text.toString().indexOf(token) + tokenLen;
		int end = text.toString().indexOf(token, start);

		if (start > -1 && end > -1) {
			// Copy the spannable string to a mutable spannable string
			SpannableStringBuilder ssb = new SpannableStringBuilder(text);
			for (CharacterStyle c : cs)
				ssb.setSpan(c, start, end, 0);

			// Delete the tokens before and after the span
			ssb.delete(end, end + tokenLen);
			ssb.delete(start - tokenLen, start);

			text = ssb;
		}

		return text;
	}

	public static boolean isNumeric(String str) {
		if (TextUtils.isEmpty(str)) {
			return false;
		}

		Pattern pattern = Pattern.compile("-?[0-9]*");
		return pattern.matcher(str).matches();
	}

	/**
	 * 
	 * Description: 手机正则表达式
	 * 
	 * @Version1.0 2015-1-19 下午9:34:53 by 王哲（wangzhejs2@dangdang.com）创建
	 * @param mobiles
	 * @return
	 */
	public static boolean isMobileNO(String mobiles) {
		if(TextUtils.isEmpty(mobiles))
			return false;
		return mobiles.length() == 11 && mobiles.matches("[0-9]+");
		
//		Pattern p = Pattern
//				.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
//		Matcher m = p.matcher(mobiles);
//
//		return m.matches();
	}

	/**
	 * 
	 * Description: 邮箱正则表达式
	 * 
	 * @Version1.0 2015-1-19 下午9:35:04 by 王哲（wangzhejs2@dangdang.com）创建
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
//		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		String str = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[A-Za-z0-9]+((\\.|-|_)*[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}
	
	/**
	 * 截取文件扩展名
	 * 
	 * @param path
	 * @return
	 */
	public static String getExpName(String path) {
		String expName = "";
		try {
			int dIndex = path.lastIndexOf(".");
			expName = path.substring(dIndex + 1, path.length());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expName;
	}
	
}
