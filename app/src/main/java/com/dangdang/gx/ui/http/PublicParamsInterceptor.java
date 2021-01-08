package com.dangdang.gx.ui.http;

import java.io.IOException;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by liuzhongtao on 2019/7/15.
 */
public class PublicParamsInterceptor implements Interceptor {
    private BaseRetrofit.PublicParamsProvider paramsProvider;

    public PublicParamsInterceptor(BaseRetrofit.PublicParamsProvider paramsProvider) {
        this.paramsProvider = paramsProvider;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

        Map<String, String> map = paramsProvider.getPublicParams(original);
        if (map.isEmpty()) {
            throw new DangError(-1, "公共参数不能为空");
        }

        // 添加公共参数
        HttpUrl.Builder urlBuilder = original.url().newBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            urlBuilder.addQueryParameter(entry.getKey(), entry.getValue());
        }

        HttpUrl url = urlBuilder.build();
        Request request = original.newBuilder()
                .method(original.method(), original.body())
                .url(url)
                .build();
        return chain.proceed(request);
    }
}
