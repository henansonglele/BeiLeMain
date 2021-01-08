package com.dangdang.gx.ui.flutter;

import java.util.HashMap;
import java.util.Map;

// flutter与native类映射表
public class FlutterToNativeJsonFactory {
    private static Map<String, String> factoryList = new HashMap<>();

    public static void init() {
        factoryList.put("UserInfo", "com.dangdang.reader.personal.domain.UserInfo");
        factoryList.put("CodeData", "com.dangdang.reader.personal.domain.CodeData");
    }

    public static String getName(String simpleName) {
        return factoryList.get(simpleName);
    }

}
