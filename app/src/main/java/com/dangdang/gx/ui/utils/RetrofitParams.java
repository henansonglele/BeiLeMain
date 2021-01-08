package com.dangdang.gx.ui.utils;

import java.util.HashMap;
import java.util.Map;


public class RetrofitParams {

    /**
     * 公共参数设计
     * fromPlatform，channelId，deviceType，libraryType , schoolLevel，schoolCode，token，clientVersionNo，checksum，timestamp
     * 接口签名设计(32位md5小写)：
     * checksum=md5(token+fromPlatform+deviceType+clientVersionNo+timestamp)
     *
     * @return
     */

    public final static String CHANNEL_ID = "channelId";
    public final static String DEVICE_TYPE = "deviceType";
    public final static String FROM_PLATFORM = "fromPlatform";
    public final static String LIBRARY_TYPE = "libraryType";
    public final static String SCHOOL_LEVEL = "schoolLevel";
    public final static String SCHOOL_CODE = "schoolCode";
    public final static String TOKEN = "token";
    public final static String CLIENT_VERSION_NO = "clientVersionNo";
    public final static String CHECK_SUM = "checksum";
    public final static String TIME_STAMP = "timestamp";

    private static Map<String, String> publicParams = new HashMap<>();

    public static Map<String, String> getParams() {
        return publicParams;
    }

    public static void setParam(String key, String value) {
        publicParams.put(key, value);
    }
}
