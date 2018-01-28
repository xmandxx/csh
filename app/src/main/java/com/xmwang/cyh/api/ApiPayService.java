package com.xmwang.cyh.api;

import com.xmwang.cyh.model.AlipayModel;
import com.xmwang.cyh.model.WXPayModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by xmwang on 2018/1/17.
 */

public interface ApiPayService {
    //
    @FormUrlEncoded
    @POST("pay/weixin_pay")
    Call<WXPayModel> weixin_pay(@Field("admin_id") String admin_id,
                                @Field("user_id") String user_id,
                                @Field("order_type") int order_type,
                                @Field("allmoney") String allmoney);
    //油卡充值
    @FormUrlEncoded
    @POST("pay/weixin_pay")
    Call<WXPayModel>weixin_pay(@Field("admin_id") String admin_id,
                                @Field("user_id") String user_id,
                                @Field("order_type") int order_type,
//                                @Field("phone") String phone,
                                @Field("car_number") String car_number,
                                @Field("original_price") String original_price,
                                @Field("allmoney") String allmoney);

    @FormUrlEncoded
    @POST("pay/alipay_pay")
    Call<AlipayModel> alipay_pay(@Field("admin_id") String admin_id,
                                 @Field("user_id") String user_id,
                                 @Field("order_type") int order_type,
                                 @Field("allmoney") String allmoney);
    //油卡充值
    @FormUrlEncoded
    @POST("pay/alipay_pay")
    Call<AlipayModel> alipay_pay(@Field("admin_id") String admin_id,
                                 @Field("user_id") String user_id,
                                 @Field("order_type") int order_type,
//                                 @Field("phone") String phone,
                                 @Field("car_number") String car_number,
                                 @Field("original_price") String original_price,
                                 @Field("allmoney") String allmoney);
}
