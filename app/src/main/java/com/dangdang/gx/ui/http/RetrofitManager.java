package com.dangdang.gx.ui.http;

import android.text.TextUtils;

import com.dangdang.gx.ui.log.LogM;
import com.dangdang.gx.ui.utils.MD5;
import com.dangdang.gx.ui.utils.RetrofitParams;

import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Request;
import retrofit2.Retrofit;

public class RetrofitManager {

    private static Retrofit httpRetrofit;

    public static void init(String baseUrl) {
        BaseRetrofit.PublicParamsProvider publicParamsProvider = new BaseRetrofit.PublicParamsProvider() {
            @Override
            public Map<String, String> getPublicParams(Request original) {
                Map<String, String> params = RetrofitParams.getParams();
                String ts = System.currentTimeMillis() / 1000 + "";
                params.put("timestamp", System.currentTimeMillis() / 1000 + "");
                params.put("checksum", getCheckSum(ts));
                return params;
            }
        };

        BaseRetrofit.HeaderProvider headerProvider = new BaseRetrofit.HeaderProvider() {
            @Override
            public Map<String, String> getHeaders(Request original) {
                Map<String, String> params = new HashMap<>();
                params.put("Gaoxiao-Tushuguan-Provider", "DangDang");
                params.put("Dangdang-Token", "");
                params.put("Gaoxiao-Code", "");
                params.put("Gaoxiao-Access-Certify", "");
                return params;
            }
        };

        BaseRetrofit.ResultHandler resultHandler = new BaseRetrofit.ResultHandler() {
            @Override
            public void handleResult(Object result) {
                if (result instanceof RequestResult) {
                    RequestResult requestResult = (RequestResult) result;

                    // 服务器返回的错误，统一转成异常，订阅者在onError处理
                    if (requestResult.status.code != 0 && !(result instanceof RequestResultNotCheckStatusCode)) {
                        if (requestResult.data != null)
                            LogM.e("error", "错误原始数据：" + requestResult.data.getClass().getSimpleName());
                        throw new DangError(requestResult.status.code, requestResult.status.message);
                    }
                }
            }
        };

        httpRetrofit = new BaseRetrofit(baseUrl, publicParamsProvider, headerProvider, resultHandler).getRetrofit();
    }

    public static Retrofit getHttpRetrofit() {
        return httpRetrofit;
    }

    public static String getErrorString(Throwable error) {
        String errString = "连接失败，请检查您的网络";
        if (error instanceof DangError) {
            errString = ((DangError) error).getmReason();
        } else if (error instanceof ConnectException || error instanceof UnknownHostException) {
            errString = "连接失败，请检查您的网络";
        } else if (!TextUtils.isEmpty(error.getMessage())) {
            errString = error.getMessage();
        }

        return errString;
    }

    public static int getErrorCode(Throwable error) {
        if (error instanceof DangError) {
            return ((DangError) error).getmCode();
        } else if (error instanceof ConnectException || error instanceof UnknownHostException) {
            return 9998;
        } else {
            return 408;
        }
    }

    /**
     * 根据Retrofit捕获的异常，生成对应的RequestResult
     *
     * @param error 异常
     * @return RequestResult
     */
    public static RequestResult getErrorResult(Throwable error) {
        RequestResult requestResult = new RequestResult();
        requestResult.status.code = getErrorCode(error);
        requestResult.status.message = getErrorString(error);
        return requestResult;
    }

    private static String getCheckSum(String ts) {
        // TODO: 2020/12/31
        //接口签名设计(32位md5小写)：
        //checksum=md5(token+fromPlatform+deviceType+clientVersionNo+timestamp)

        return MD5.hexdigest(ts);
    }

}
