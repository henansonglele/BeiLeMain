package com.dangdang.gx.ui.http;

import android.text.TextUtils;

import com.dangdang.gx.ui.http.fastjson.FastJsonConverterFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 用于发起通用的第三方Url地址请求的Retrofit
 * Created by liuzhongtao on 2019/7/15.
 */
public class BaseRetrofit {
    private Retrofit retrofit;

    public BaseRetrofit(String baseUrl, PublicParamsProvider paramsProvider, HeaderProvider headerProvider, ResultHandler resultHandler) {
        if (!TextUtils.isEmpty(baseUrl)) {
            retrofit = new Retrofit.Builder()
                    .client(getOkHttpClient(paramsProvider, headerProvider))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(FastJsonConverterFactory.create(resultHandler))//解析方法
                    .baseUrl(baseUrl)//主机地址
                    .build();
        }
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private OkHttpClient getOkHttpClient(PublicParamsProvider paramsProvider, HeaderProvider headerProvider) {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(30, TimeUnit.SECONDS);
        client.readTimeout(20, TimeUnit.SECONDS);
        client.writeTimeout(20, TimeUnit.SECONDS);
        if (headerProvider != null) {
            client.addInterceptor(new HeaderInterceptor(headerProvider));
        }
        if (paramsProvider != null) {
            client.addInterceptor(new PublicParamsInterceptor(paramsProvider));
        }

        return client.build();
    }

    public interface PublicParamsProvider {
        Map<String, String> getPublicParams(Request original);
    }

    public interface HeaderProvider {
        Map<String, String> getHeaders(Request original);
    }

    public interface ResultHandler {
        void handleResult(Object result);
    }
}
