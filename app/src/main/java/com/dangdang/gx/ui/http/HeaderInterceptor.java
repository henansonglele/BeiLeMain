package com.dangdang.gx.ui.http;

import java.io.IOException;
import java.util.Map;

import androidx.annotation.NonNull;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HeaderInterceptor implements Interceptor {
    private final BaseRetrofit.HeaderProvider headerProvider;

    public HeaderInterceptor(BaseRetrofit.HeaderProvider headerProvider) {
        this.headerProvider = headerProvider;
    }

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request original = chain.request();

        Map<String, String> map = headerProvider.getHeaders(original);

        // 添加header
        Request.Builder urlBuilder = original.newBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            urlBuilder.addHeader(entry.getKey(), entry.getValue());
        }

        Request request = urlBuilder.build();
        return chain.proceed(request);
    }
}
