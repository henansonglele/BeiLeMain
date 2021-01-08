package com.dangdang.gx.ui.flutterbase;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import com.dangdang.gx.ui.DDApplication;
import com.dangdang.gx.ui.http.RequestResultNotCheckStatusCode;
import com.dangdang.gx.ui.http.RetrofitCommonApi;
import com.dangdang.gx.ui.http.RetrofitManager;
import com.dangdang.gx.ui.log.LogM;
import com.dangdang.gx.ui.umeng.UmengStatistics;
import com.dangdang.gx.ui.utils.DangdangFileManager;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by liuzhongtao on 2019/9/12.
 */
public class DDFlutterManager {

    public static void init() {
        DDFlutterMethodChannel.init();
        // 注册Flutter接口调用
        //regCallHApiGetHandler();
        regCallHApiPostHandler();
        regCrashHandler();
        regGetFontPath();
        retBIStaticHandler();
        regCallHApiGetPathHandler();
    }



    private static void regCallHApiGetPathHandler() {
        addMethodCallHandler("ddreader/callGXApiGet", new MethodChannel.MethodCallHandler() {
            @SuppressLint("CheckResult")
            @Override
            public void onMethodCall(MethodCall methodCall, final MethodChannel.Result callResult) {
                if (!(methodCall.arguments instanceof Map)) {
                    callResult.error("-1", "HApi arguments must be Map<String, String>", null);
                    return;
                }

                Map<String, Object> arguments = (Map) methodCall.arguments;
                String path = (String) arguments.get("path");
                Map<String, String> params = new HashMap<>();
                if (arguments.get("params") != null && arguments.get("params") instanceof Map) {
                    params = (Map<String, String>) (arguments.get("params"));
                }
                //noinspection ResultOfMethodCallIgnored,unchecked
                RetrofitManager.getHttpRetrofit().create(RetrofitCommonApi.CommonApiService.class)
                        .getWithPath(path, params)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<RequestResultNotCheckStatusCode<String>>() {
                            @Override
                            public void accept(RequestResultNotCheckStatusCode<String> result) {
                                callResult.success(result.jsonStr);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                throwable.printStackTrace();

                                callResult.error(String.valueOf(RetrofitManager.getErrorCode(throwable)),
                                        RetrofitManager.getErrorString(throwable),
                                        null);
                            }
                        });
            }
        });
    }

    private static void regCallHApiPostHandler() {
        addMethodCallHandler("ddreader/callGXApiPost", new MethodChannel.MethodCallHandler() {
            @SuppressLint("CheckResult")
            @Override
            public void onMethodCall(MethodCall methodCall, final MethodChannel.Result callResult) {
                if (!(methodCall.arguments instanceof Map)) {
                    callResult.error("-1", "HApi arguments must be Map<String, String>", null);
                    return;
                }
                Map<String, String> params = new HashMap<>();
                Map<String, String> headers = new HashMap<>();
                if (((Map) methodCall.arguments).get("params") != null && ((Map) methodCall.arguments).get("params") instanceof Map) {
                    params = (Map<String, String>) ((Map) methodCall.arguments).get("params");
                }

                if (((Map) methodCall.arguments).get("headers") != null && ((Map) methodCall.arguments).get("headers") instanceof Map) {
                    headers = (Map<String, String>) ((Map) methodCall.arguments).get("headers");
                }
                RetrofitManager.getHttpRetrofit().create(RetrofitCommonApi.CommonApiService.class)
                        .post(params, headers)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<RequestResultNotCheckStatusCode<String>>() {
                            @Override
                            public void accept(RequestResultNotCheckStatusCode<String> result) {
                                callResult.success(result.jsonStr);
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) {
                                throwable.printStackTrace();

                                callResult.error(String.valueOf(RetrofitManager.getErrorCode(throwable)),
                                        RetrofitManager.getErrorString(throwable),
                                        null);
                            }
                        });
            }
        });
    }



    // 上传Flutter传来的崩溃数据到友盟
    private static void regCrashHandler() {
        addMethodCallHandler("ddreader/reportError", new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                Log.e("Flutter Crash", (String) methodCall.arguments);
                UmengStatistics.reportError(DDApplication.getInstance(),
                        (String) methodCall.arguments);
            }
        });
    }


    private static void regGetFontPath() {
        addMethodCallHandler("ddreader/getTTFPath", new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                String path = DangdangFileManager.getPreSetTTF();
                if (!TextUtils.isEmpty(path) && new File(path).exists()) {
                    result.success(path);
                } else {
                    result.error("Font file not exist", "-1", null);
                }
            }
        });
    }

    private static void retBIStaticHandler() {
        addMethodCallHandler("ddreader/insertBIStaticsEvent", new MethodChannel.MethodCallHandler() {
            @Override
            public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                if (!(methodCall.arguments instanceof Map)) {
                    LogM.e("BIStatics arguments must be Map");
                    return;
                }

                try {
                    Map map = (Map) methodCall.arguments;

                    String pageId = (String) map.get("pageId");
                    String eventId = (String) map.get("eventId");
                    String biFloor = "";
                    String pid = "";
                    long startTime = System.currentTimeMillis();
                    if (map.containsKey("startTime")) {
                        startTime = (long) map.get("startTime");
                    }
                    if (map.containsKey("pid")) {
                        pid = (String) map.get("pid");
                    }
                    if (map.containsKey("biFloor")) {
                        biFloor = (String) map.get("biFloor");
                    }

                    //String type;
                    //if (PVCode.equals(eventId) || PV_TIME_EVENT_ID.equals(eventId)) {
                    //    type = BIConstants.TYPE_PV;
                    //} else {
                    //    type = BIConstants.TYPE_ClICL;
                    //}
                    //
                    //
                    //BIStatisticsUtils.insertEntity(pageId, eventId, pid, startTime, "", biFloor, "", "", type, "", accountManager.getCustId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }




    public static void launch(Activity activity, String pageUrl, Map<String, Object> params) {
        launch(activity, pageUrl, params, 0);
    }

    /**
     * 跳转Flutter页面
     *
     * @param activity from
     * @param pageUrl  ddreader://PAGE_PATH
     * @param params   参数
     */
    public static void launch(Activity activity, String pageUrl, Map<String, Object> params, int requestCode) {
        DDFlutterActivity.launch(activity, pageUrl, params, requestCode);
    }

    public static void invokeMethod(final String name, Object args, MethodChannel.Result result) {
        DDFlutterMethodChannel.invokeMethod(name, args, result);
    }

    public static void addMethodCallHandler(String methodName, MethodChannel.MethodCallHandler handler) {
        DDFlutterMethodChannel.setMethodCallHandler(methodName, handler);
    }

    public static void removeMethodCallHandler(String methodName) {
        DDFlutterMethodChannel.removeMethodCallHandler(methodName);
    }
}
