package com.xmwang.cyh.common;

import com.xmwang.cyh.api.ApiHomeService;
import com.xmwang.cyh.api.ApiPayService;
import com.xmwang.cyh.api.ApiService;
import com.xmwang.cyh.api.ApiUserService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xmWang on 2017/12/28.
 */

public enum RetrofitHelper {

    instance;

//    private static RetrofitHelper instance = null;
    public String baseUrl = "http://api.che377.com/api/";
    public String baseAdminUrl = "http://admin.che377.com/";
    public int SUCCESS_CODE = 200;
    private Retrofit retrofit;
    public Retrofit getRetrofit(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    private ApiService apiService;
    public ApiService getApiService() {
        if (apiService == null){
            apiService = getRetrofit().create(ApiService.class);
        }
        return apiService;
    }

    private ApiUserService apiUserService;
    public ApiUserService getApiUserService(){
        if (apiUserService == null){
            apiUserService = getRetrofit().create(ApiUserService.class);
        }
        return apiUserService;
    }

    private ApiHomeService apiHomeService;
    public ApiHomeService getApiHomeService(){
        if (apiHomeService == null){
            apiHomeService = getRetrofit().create(ApiHomeService.class);
        }
        return apiHomeService;
    }

    private ApiPayService apiPayService;
    public ApiPayService getApiPayService(){
        if (apiPayService == null){
            apiPayService = getRetrofit().create(ApiPayService.class);
        }
        return apiPayService;
    }
    public void enqueueEx(){

    }
//    private RetrofitHelper(){
//        retrofit = new Retrofit.Builder()
//                .baseUrl(baseUrl)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//    }

//    public static synchronized RetrofitHelper getInstance() {
//
//            if (instance == null) {
//                instance = new RetrofitHelper();
//            }
//
//        return instance;
//    }

//    public ApiService getApiService(){
//
//
//        ApiService
//        return apiService;
//    }

}
