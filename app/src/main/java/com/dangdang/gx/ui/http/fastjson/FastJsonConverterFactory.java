package com.dangdang.gx.ui.http.fastjson;


import com.dangdang.gx.ui.http.BaseRetrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by luchenghao on 2017/4/25.
 */

public class FastJsonConverterFactory extends Converter.Factory {
    private BaseRetrofit.ResultHandler resultHandler;

    private FastJsonConverterFactory(BaseRetrofit.ResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    public static FastJsonConverterFactory create(BaseRetrofit.ResultHandler resultHandler) {
        return new FastJsonConverterFactory(resultHandler);
    }

    /**
     * 需要重写父类中responseBodyConverter，该方法用来转换服务器返回数据
     */
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new FastJsonResponseBodyConverter<>(type, resultHandler);
    }

    /**
     * 需要重写父类中responseBodyConverter，该方法用来转换发送给服务器的数据
     */
    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new FastJsonRequestBodyConverter<>();
    }

}