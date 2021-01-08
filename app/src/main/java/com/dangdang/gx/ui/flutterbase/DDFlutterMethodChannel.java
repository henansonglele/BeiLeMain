package com.dangdang.gx.ui.flutterbase;

import com.idlefish.flutterboost.FlutterBoost;

import java.util.HashMap;
import java.util.Map;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;

/**
 * Created by liuzhongtao on 2019/9/18.
 */
class DDFlutterMethodChannel {
    private static final String COMMON_CHANNEL_NAME = "ddreader/common_channel";

    private static MethodChannel methodChannel;
    private static final Map<String, MethodChannel.MethodCallHandler> methodCallHandlers = new HashMap<>();

    static void init() {
        methodChannel = new MethodChannel(FlutterBoost.instance().engineProvider().getDartExecutor(), COMMON_CHANNEL_NAME);

        methodChannel.setMethodCallHandler(new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                MethodChannel.MethodCallHandler callHandler;
                synchronized (methodCallHandlers) {
                    callHandler = methodCallHandlers.get(methodCall.method);
                }
                if (callHandler != null) {
                    callHandler.onMethodCall(methodCall, result);
                } else {
                    result.notImplemented();
                }
            }
        });
    }

    static void invokeMethod(final String name, Object args, MethodChannel.Result result){
        methodChannel.invokeMethod(name, args, result);
    }

    static void setMethodCallHandler(String methodName, MethodChannel.MethodCallHandler handler) {
        synchronized (methodCallHandlers) {
            methodCallHandlers.put(methodName, handler);
        }
    }

    static void removeMethodCallHandler(String methodName) {
        synchronized (methodCallHandlers) {
            methodCallHandlers.remove(methodName);
        }
    }
}
