package com.xmwang.cyh.api;

import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.model.LoveCarModel;
import com.xmwang.cyh.model.OrderModel;
import com.xmwang.cyh.model.UserInfo;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by xmWang on 2018/1/1.
 */

public interface ApiUserService   {
    //发送验证码
    @FormUrlEncoded
    @POST("userinfo/send_code")
    Call<BaseModel> sendCode(@Field("admin_id") String admin_id, @Field("mobile") String mobile);

    //注册
    @FormUrlEncoded
    @POST("userinfo/register")
    Call<BaseModel> register(@Field("admin_id") String admin_id,
                             @Field("mobile") String mobile,
                             @Field("password") String password,
                             @Field("code") String code);

    //找回密码
    @FormUrlEncoded
    @POST("userinfo/forget_pwd")
    Call<BaseModel> forgetPwd(@Field("admin_id") String admin_id,
                              @Field("mobile") String mobile,
                              @Field("password") String password,
                              @Field("code") String code);

    //登录
    @FormUrlEncoded
    @POST("userinfo/login")
    Call<UserInfo> login(@Field("admin_id") String admin_id,
                         @Field("mobile_phone") String mobile_phone,
                         @Field("password") String password);

    //获取用户详情
    @FormUrlEncoded
    @POST("userinfo/getuserinfo")
    Call<UserInfo> getuserinfo(@Field("admin_id") String admin_id, @Field("user_id") String user_id);

    //编辑用户信息
    @FormUrlEncoded
    @POST("userinfo/edit_user")
    Call<BaseModel> editUser(@Field("user_id") String user_id,
                             @Field("headimg") String headimg,
                             @Field("real_name") String realName,
                             @Field("user_name") String userName,
                             @Field("sex") String sex);

    //编辑用户信息
    @FormUrlEncoded
    @POST("userinfo/my_order")
    Call<OrderModel> myOrder(@Field("admin_id") String admin_id,
                             @Field("user_id") String user_id,
                             @Field("order_type") int order_type,
                             @Field("pageSize") int pageSize,
                             @Field("pageIndex") int pageIndex);
    //我的爱车
    @FormUrlEncoded
    @POST("userinfo/user_cars_list")
    Call<LoveCarModel> user_cars_list(@Field("admin_id") String admin_id,
                                      @Field("user_id") String user_id,
                                      @Field("pageSize") int pageSize,
                                      @Field("pageIndex") int pageIndex);

    //
    @FormUrlEncoded
    @POST("userinfo/user_cars_status")
    Call<BaseModel> user_cars_status(@Field("admin_id") String admin_id,
                                      @Field("user_id") String user_id,
                                      @Field("car_id") int car_id,
                                      @Field("type") int type);
}
