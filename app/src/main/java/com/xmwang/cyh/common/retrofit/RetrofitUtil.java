package com.xmwang.cyh.common.retrofit;

import android.content.Context;

import com.xmwang.cyh.api.ApiService;
import com.xmwang.cyh.api.ApiUserService;
import com.xmwang.cyh.common.Data;
import com.xmwang.cyh.model.DriveInfoModel;
import com.xmwang.cyh.model.Reckon;
import com.xmwang.cyh.model.UserInfo;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private Observable<Class> observable;
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
    public Map<String, String> baseParam(){
        Map<String, String> param = new HashMap<>();
        param.put("admin_id", Data.instance.AdminId);
        param.put("user_id", Data.instance.getUserId());
        return param;
    }
    public Retrofit getmRetrofit() {
        return mRetrofit;
    }
    public void setContext(Context context){
        mContext = context;
    }
    public RetrofitUtil setObservable(Observable observable) {
        this.observable = observable;
        return RetrofitUtil.getInstance();
    }
    public <T> T create(final Class<T> service) {

        return (T)RetrofitUtil.getInstance();
    }
    public void base(SubscriberOnNextListener onNextListener){
//        Observable ob = mRetrofit.create(ApiUserService.class).getuserinfo(Data.instance.AdminId,Data.instance.getUserId());
        if (observable != null) {
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ProgressSubscriber(onNextListener));
        }
    }
    public void base(SubscriberOnNextListener onNextListener,Context context){
        if (observable != null) {
            observable.subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new ProgressSubscriber(onNextListener, context));
        }
    }
}
