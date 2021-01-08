package com.dangdang.gx.ui.http.fastjson;

import com.alibaba.fastjson.JSON;
import com.dangdang.gx.ui.http.BaseRetrofit;
import com.dangdang.gx.ui.http.RequestResultNotCheckStatusCode;

import java.io.IOException;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * Created by luchenghao on 2017/4/25.
 */

public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Type type;
    private BaseRetrofit.ResultHandler resultHandler;

    FastJsonResponseBodyConverter(Type type, BaseRetrofit.ResultHandler resultHandler) {
        this.type = type;
        this.resultHandler = resultHandler;
    }

    /*
    * 转换方法
    */
    @Override
    public T convert(@NonNull ResponseBody value)  throws IOException {
        BufferedSource bufferedSource = Okio.buffer(value.source());
        String tempStr = "";
        tempStr = bufferedSource.readUtf8();
        bufferedSource.close();
        T t = JSON.parseObject(tempStr, type);

        if (t instanceof RequestResultNotCheckStatusCode) {
            ((RequestResultNotCheckStatusCode) t).jsonStr = tempStr;
        }

        if (resultHandler != null) {
            resultHandler.handleResult(t);
        }
        return t;
    }
}