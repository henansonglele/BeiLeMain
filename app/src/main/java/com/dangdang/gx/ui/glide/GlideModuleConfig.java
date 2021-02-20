package com.dangdang.gx.ui.glide;

import android.content.Context;
import android.os.Environment;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.engine.cache.DiskLruCacheFactory;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.dangdang.gx.ui.utils.DangdangFileManager;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import okhttp3.OkHttpClient;
//import okhttp3.OkHttpClient;

/**
 *
 * 1 设置glide的缓存
 * 2 使用 okhttpClient
 * 3 忽略https url图片 验证
 *
 * */

// 注意这个注解一定要加上，HttpGlideModule是自定义的名字  ,通过注解 自动配置到glide上。
@GlideModule
public class GlideModuleConfig extends AppGlideModule {

    //外部路径
    private String sdRootPath = Environment.getExternalStorageDirectory().getPath();
    private String appRootPath = null;



    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int diskCacheSizeBytes = 1024 * 1024 * 100; // 100 MB
        //手机app路径
        //appRootPath = context.getCacheDir().getPath();

        builder.setDiskCache(new DiskLruCacheFactory( getStorageDirectory(context)+"/GlideDisk", diskCacheSizeBytes ));
        super.applyOptions(context,builder);

        ///还有以下属性可以自定义设置 ，缓存策略， 图片分辨率
        /**
         *
         * long maxMemory = Runtime.getRuntime().maxMemory();
        //设置缓存的大小
        long cacheSize = maxMemory / 8;
        // Default empty impl.
        //设置Bitmap的缓存池
        builder.setBitmapPool(LruBitmapPool(30));
        //设置内存缓存
        builder.setMemoryCache(LruResourceCache(cacheSize));
        //设置磁盘缓存
        builder.setDiskCache(InternalCacheDiskCacheFactory(context));
        //设置读取不在缓存中资源的线程
        builder.setSourceExecutor(GlideExecutor.newSourceExecutor());
        //设置读取磁盘缓存中资源的线程
        builder.setDiskCacheExecutor(GlideExecutor.newDiskCacheExecutor());
        //设置日志级别
        builder.setLogLevel(Log.VERBOSE);
        //设置全局选项
        long requestOptions = RequestOptions().format(DecodeFormat.PREFER_RGB_565);
        builder.setDefaultRequestOptions(requestOptions);
         */

    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide,
            @NonNull Registry registry) {
        //Glide 默认的请求网络框架是 HttpURLConnection ，我们想改成 OkHttp 就需要用到 registerComponents 函数
        ///OkHttpClient 替换 HttpURLConnection
        // 注意这里用我们刚才现有的Client实例传入即可
        //registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(new OkHttpClient()));
        super.registerComponents(context, glide, registry);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(getHttpClient()/*new OkHttpClient()*/));
    }


    //获取路径
    private String getStorageDirectory(Context context) {

        return  DangdangFileManager.getUserHeadPortraitsTakePhotoPath(context);
        //return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? sdRootPath : appRootPath;
    }




    //设置ssl
    public static OkHttpClient getHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .sslSocketFactory(getSSLSocketFactory())
                .hostnameVerifier(getHostnameVerifier());
        return builder.build();
    }

    /**
     * getSSLSocketFactory、getTrustManagers、getHostnameVerifier
     * 使OkHttpClient支持自签名证书，避免Glide加载不了Https图片
     */
    private static SSLSocketFactory getSSLSocketFactory() {
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, getTrustManagers(), new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static TrustManager[] getTrustManagers() {
        return new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};
    }
    // 直接返回true，默认verify通过
    private static HostnameVerifier getHostnameVerifier() {
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true; // 直接返回true，默认verify通过
            }
        };
    }


}