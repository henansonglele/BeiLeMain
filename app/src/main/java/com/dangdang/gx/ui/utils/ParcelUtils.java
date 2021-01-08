package com.dangdang.gx.ui.utils;

import android.os.Parcel;

/**
 * 邮包化相关工具类
 * @author xiaruri
 *
 */
public class ParcelUtils {

	/**
     * 序列化布尔值
     * @param out 	序列化对象
     * @param v 	布尔值
     */
    public static void writeBooleanToParcel(Parcel out, boolean v) {
        out.writeInt(v ? 1 : 0);
    }
    
    /**
     * 反序列化布尔值
     * @param in 	序列化兑现
     * @return 		布尔值
     */
    public static boolean readBooleanFromParcel(Parcel in) {
        return in.readInt() > 0 ? true : false;
    }
}
