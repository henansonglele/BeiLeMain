package com.dangdang.gx.ui.http;

/**
 * 特殊类型的RequestResult
 * 在Retrofit处理返回结果时不会检查到status.code!=0就抛出异常
 * 用于处理用错误码表示不同场景的api
 * Created by liuzhongtao on 2018/4/11.
 */

public class RequestResultNotCheckStatusCode<T> extends RequestResult<T> {
    public String jsonStr;
}
