package com.dangdang.gx.ui.http;

import io.reactivex.Observable;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

/**
 * Created by liuzhongtao on 2017/10/19.
 */

public class RetrofitCommonApi {
    public interface CommonApiService {

        @GET("/media/api2.go?gzip=yes")
        Observable<RequestResultNotCheckStatusCode<String>> get(
                @QueryMap Map<String, String> queryMap);

        @GET
        Observable<RequestResultNotCheckStatusCode<String>> getWithPath(@Url String url,
                @QueryMap Map<String, String> queryMap);

        @POST
        Observable<RequestResultNotCheckStatusCode<String>> gateWayPost(@Url String url,
                @QueryMap Map<String, String> queryMap,
                @Header("Content-Type") String contentType);

        @POST("/media/api2.go?gzip=yes")
        Observable<RequestResultNotCheckStatusCode<String>> post(
                @QueryMap Map<String, String> queryMap,
                //@Body Map<String, String> body,
                @HeaderMap Map<String, String> headers
        );
    }
}
