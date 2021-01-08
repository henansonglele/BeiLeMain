package com.dangdang.gx.ui.flutter;

import android.app.Activity;
import com.dangdang.gx.ui.flutterbase.DDFlutterManager;
import com.dangdang.gx.ui.utils.LaunchUtils;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import java.util.Map;



// flutter界面跳转 原生界面 工具类
public class DDFlutter2NativeUtils {
    public static void flutterLaunchNavite(Activity activity, String pageUrl, Map<String, String> params) {
        switch (pageUrl) {
            case "ddreader://bookdetail":
                LaunchUtils.launchStoreNormalHtmlActivity(activity, params.get("title"),params.get("url"));
                break;
             case "ddreader://personal/my_foot":
                LaunchUtils.launchStoreNormalHtmlActivity(activity, "足迹list","https://www.baidu.com");
                break;
            case "ddreader://login":
                LaunchUtils.launchSmsLoginActivity(activity);
                break;
        }
    }

    public static void regFlutterHandler() {
        addMethodCallHandler("ddreader/appointedPlatformShare", new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                try {
                    Map arguments = (Map) methodCall.arguments;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }



    private static void addMethodCallHandler(String methodName, MethodChannel.MethodCallHandler handler) {
        DDFlutterManager.addMethodCallHandler(methodName, handler);
    }

}