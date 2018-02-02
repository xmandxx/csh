package com.xmwang.cyh.api;

import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.model.VersionModel;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by wangxiaoming on 2018/2/2.
 */

public interface ApiBaseService {
    //获取养车花费
    @FormUrlEncoded
    @POST("base/check_version")
    Observable<BaseResponse<VersionModel>> check_version(@Field("admin_id") String admin_id,
                                                       @Field("app_type") int user_id
    );
}
