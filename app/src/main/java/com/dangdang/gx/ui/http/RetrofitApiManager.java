package com.dangdang.gx.ui.http;

public class RetrofitApiManager {
    private static RetrofitApiService apiService = RetrofitManager
            .getHttpRetrofit()
            .create(RetrofitApiService.class);

    public static RetrofitApiManager.RetrofitApiService getApiService() {
        return apiService;
    }

    public interface RetrofitApiService {

    }
}
