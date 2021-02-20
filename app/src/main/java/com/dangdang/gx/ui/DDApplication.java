package com.dangdang.gx.ui;

import android.app.Application;
import android.content.Context;
import com.dangdang.gx.BuildConfig;
import com.dangdang.gx.ui.flutter.DDFlutter2NativeUtils;
import com.dangdang.gx.ui.flutterbase.DDFlutterManager;
import com.dangdang.gx.ui.http.RetrofitManager;
import com.dangdang.gx.ui.log.LogM;
import com.dangdang.gx.ui.umeng.UmengStatistics;
import com.dangdang.gx.ui.utils.DangdangFileManager;
import com.dangdang.gx.ui.utils.ForegroundV2;
import com.dangdang.gx.ui.utils.RetrofitParams;
import com.idlefish.flutterboost.FlutterBoost;
import com.idlefish.flutterboost.Platform;
import com.idlefish.flutterboost.interfaces.INativeRouter;
import com.meituan.android.walle.WalleChannelReader;
import io.flutter.embedding.android.FlutterView;
import io.flutter.view.FlutterMain;
import java.util.HashMap;
import java.util.Map;

public class DDApplication extends Application {
    private static DDApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        if (mApp == null)
            mApp = this;
        initRetrofitParams();
        initFlutter();
        ForegroundV2.init(this);
        initUMeng();
        DangdangFileManager.initSdkMode(this);

        //然后在你的Application的onCreate加入
        //Glide.get(this).register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(new OkHttpClient()));
    }
    public static  DDApplication getInstance(){
        return  mApp;
    }
    private void initRetrofitParams() {
        // TODO: 2020/12/31 公共参数
        RetrofitParams.setParam(RetrofitParams.CHANNEL_ID, "");
        RetrofitParams.setParam(RetrofitParams.DEVICE_TYPE, "");
        RetrofitParams.setParam(RetrofitParams.FROM_PLATFORM, "");
        RetrofitParams.setParam(RetrofitParams.LIBRARY_TYPE, "");
        RetrofitParams.setParam(RetrofitParams.SCHOOL_LEVEL, "");
        RetrofitParams.setParam(RetrofitParams.SCHOOL_CODE, "");
        RetrofitParams.setParam(RetrofitParams.TOKEN, "");
        RetrofitParams.setParam(RetrofitParams.CLIENT_VERSION_NO, "");
        RetrofitManager.init("http://e.dangdang.com");
    }

    private void initUMeng() {
        UmengStatistics.openStatics = true;//!BuildConfig.DEBUG;//debug版本不统计友盟
        String channel = WalleChannelReader.getChannel(this.getApplicationContext(), UmengStatistics.defaultChannelValue);
        UmengStatistics.preInit(this, channel);
        LogM.d("UmengSDK", "preInit umeng sdk in Application onCreate, channel: " + channel);

        // TODO: 2021-01-04
        // 已同意隐私条款，直接初始化友盟
        //if (!FirstGuideManager.getInstance(this).isPrivacyPolicyFirst())
        {
            LogM.d("UmengSDK", "init umeng sdk in Application onCreate, channel:" + channel);
            UmengStatistics.init(this, channel);
        }

        //新增app启动 自定义事件
        UmengStatistics.onEvent(this,UmengStatistics.APP_OPEN);
    }



    private void initFlutter() {
        FlutterMain.startInitialization(this);

        INativeRouter router = new INativeRouter() {
            @Override
            public void openContainer(Context context, String url, Map<String, Object> urlParams, int requestCode, Map<String, Object> exts) {
                LogM.d("openContainer", "url:" + url);
                // 跳转Flutter页面
                if (url.startsWith("ddflutter://")) {
                    DDFlutterManager.launch(ForegroundV2.getInstance().getTopActivity(), url, urlParams, requestCode);
                } else if (url.startsWith("ddreader://")) {//跳转原生界面
                    Map<String, String> params = new HashMap<>();
                    for (Map.Entry<String, Object> entry : urlParams.entrySet()) {
                        if (entry.getValue() instanceof String) {
                            params.put(entry.getKey(), (String) entry.getValue());
                        } else {
                            params.put(entry.getKey(), entry.getValue().toString());
                        }
                    }
                    DDFlutter2NativeUtils.flutterLaunchNavite(ForegroundV2.getInstance().getTopActivity(), url, params);
                }
            }
        };

        Platform platform= new FlutterBoost
                .ConfigBuilder(this,router)
                .isDebug(BuildConfig.DEBUG)
                //.dartEntrypoint() //dart入口，默认为main函数
                .whenEngineStart(FlutterBoost.ConfigBuilder.IMMEDIATELY)
                .lifecycleListener(new FlutterBoost.BoostLifecycleListener() {
                    @Override
                    public void beforeCreateEngine() {

                    }

                    @Override
                    public void onEngineCreated() {
                        DDFlutterManager.init();
                        DDFlutter2NativeUtils.regFlutterHandler();
                    }

                    @Override
                    public void onPluginsRegistered() {

                    }

                    @Override
                    public void onEngineDestroy() {

                    }
                })
                .renderMode(FlutterView.RenderMode.texture)
                .build();

        FlutterBoost.instance().init(platform);
    }




}
