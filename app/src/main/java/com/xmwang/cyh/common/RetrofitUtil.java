package com.xmwang.cyh.common;

import android.content.Context;
import android.util.Log;

import com.xmwang.cyh.api.ApiService;
import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.common.retrofit.ProgressSubscriber;
import com.xmwang.cyh.common.retrofit.SubscriberOnNextListener;
import com.xmwang.cyh.model.DriveInfoModel;


import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
/**
 * Created by xmwang on 2018/1/30.
 */

public class RetrofitUtil {
    public static final int DEFAULT_TIMEOUT = 5;

    private Retrofit mRetrofit;
    private ApiService mApiService;
    private Context mContext;
    private static RetrofitUtil mInstance;

    /**
     * 私有构造方法
     */
    private RetrofitUtil(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(Data.instance.baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public static RetrofitUtil getInstance(){
        if (mInstance == null){
            synchronized (RetrofitUtil.class){
                mInstance = new RetrofitUtil();
            }
        }
        return mInstance;
    }
    public void setContext(Context context){
        mContext = context;
    }
    /**
     * 用于获取用户信息
     * @param subscriber
     */
    public void driveInfo(Subscriber<BaseResponse<List<DriveInfoModel>>> subscriber){
        mApiService.drive_info(Data.instance.AdminId,Data.instance.getUserId())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }
    public void driveInfo1(SubscriberOnNextListener<BaseResponse<List<DriveInfoModel>>> onNextListener){
        ProgressSubscriber<BaseResponse<List<DriveInfoModel>>> subscriber = new ProgressSubscriber<BaseResponse<List<DriveInfoModel>>>(onNextListener,mContext);
        mApiService.drive_info(Data.instance.AdminId,Data.instance.getUserId())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }
}
