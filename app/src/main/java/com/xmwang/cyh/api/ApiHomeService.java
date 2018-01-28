package com.xmwang.cyh.api;

import com.xmwang.cyh.model.Banner;
import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.model.CSTelModel;
import com.xmwang.cyh.model.CarFee;
import com.xmwang.cyh.model.MyCouponsModel;
import com.xmwang.cyh.model.NewGoodsList;
import com.xmwang.cyh.model.QiangCouponsModel;
import com.xmwang.cyh.model.TopNews;
import com.xmwang.cyh.model.Utilities;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by xmwang on 2018/1/5.
 */

public interface ApiHomeService {

    //获取首页头条
    @FormUrlEncoded
    @POST("home/top_news")
    Call<TopNews> top_news(@Field("admin_id") String admin_id);

    //获取滚动图
    @FormUrlEncoded
    @POST("home/banner")
    Call<Banner> banner(@Field("admin_id") String admin_id);

    //获取功能图标
    @FormUrlEncoded
    @POST("home/utilities")
    Call<Utilities> utilities(@Field("admin_id") String admin_id);

    //获取底部最新优惠数据
    @FormUrlEncoded
    @POST("home/new_goods_list")
    Call<NewGoodsList> new_goods_list(@Field("admin_id") String admin_id);

    //获取我的优惠券
    @FormUrlEncoded
    @POST("home/my_coupons")
    Call<MyCouponsModel> my_coupons(@Field("admin_id") String admin_id, @Field("user_id") String user_id);

    //获取优惠券
    @FormUrlEncoded
    @POST("home/get_coupons")
    Call<QiangCouponsModel> get_coupons(@Field("admin_id") String admin_id, @Field("user_id") String user_id);

    //获取我的已过期优惠券
    @FormUrlEncoded
    @POST("home/exceed_coupons")
    Call<MyCouponsModel> exceed_coupons(@Field("admin_id") String admin_id, @Field("user_id") String user_id);

    //添加养车花费
    @FormUrlEncoded
    @POST("home/add_car_fee")
    Call<BaseModel> add_car_fee(@Field("admin_id") String admin_id,
                                @Field("user_id") String user_id,
                                @Field("fee_type") int fee_type,
                                @Field("money") String money,
                                @Field("t_value") String t_value,
                                @Field("d_value") String d_value,
                                @Field("add_time") String add_time
    );

    //获取养车花费
    @FormUrlEncoded
    @POST("home/get_car_fee")
    Call<CarFee> get_car_fee(@Field("admin_id") String admin_id,
                             @Field("user_id") String user_id,
                             @Field("search_time") String search_time
    );

    //获取客服电话
    @FormUrlEncoded
    @POST("home/get_cstel")
    Call<CSTelModel> get_cstel(@Field("admin_id") String admin_id);

    //添加道路救援
    @FormUrlEncoded
    @POST("home/add_roadescue")
    Call<BaseModel> add_roadescue(@Field("admin_id") String admin_id,
                                  @Field("user_id") String user_id,
                                  @Field("rescue_info") String rescue_info,
                                  @Field("tel") String tel,
                                  @Field("longitude") String longitude,
                                  @Field("latitude") String latitude,
                                  @Field("address") String address
    );

    //油卡充值
    @FormUrlEncoded
    @POST("home/fuelcard")
    Call<BaseModel> fuelcard(@Field("admin_id") String admin_id,
                             @Field("user_id") String user_id,
//                             @Field("phone") String phone,
                             @Field("car_number") String car_number,
                             @Field("original_price") String original_price,
                             @Field("allmoney") String allmoney
    );
}
