package com.xmwang.cyh.api;

import com.xmwang.cyh.common.retrofit.BaseResponse;
import com.xmwang.cyh.model.BaseModel;
import com.xmwang.cyh.model.BestNews;
import com.xmwang.cyh.model.Charging;
import com.xmwang.cyh.model.DriveInfo;
import com.xmwang.cyh.model.DriveInfoModel;
import com.xmwang.cyh.model.DriveOrderInfo;
import com.xmwang.cyh.model.OtherDriver;
import com.xmwang.cyh.model.TempInfo;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by xmwang on 2017/12/19.
 */

public interface ApiService{

    @FormUrlEncoded
    @POST("drive/drive_info")
//    Call<BaseResponse<List<DriveInfoModel>>> drive_info(@Field("admin_id") String admin_id, @Field("user_id") String userId);
    Observable<BaseResponse<List<DriveInfoModel>>> drive_info(@Field("admin_id") String admin_id, @Field("user_id") String userId);

    //获取司机信息
    @FormUrlEncoded
    @POST("drive/drive_info")
    Call<DriveInfo> driveInfo(@Field("admin_id") String admin_id, @Field("user_id") String userId);


    //获取代驾计费模板
    @FormUrlEncoded
    @POST("drive/get_temp_list")
    Call<Charging> charging(@Field("admin_id") String admin_id, @Field("user_id") String user_id);

    //获取计费模板详情
    @FormUrlEncoded
    @POST("drive/get_temp_info")
    Call<TempInfo> getTempInfo(@Field("user_id") String user_id, @Field("charging_id") int charging_id);


    //司机发布代价订单
    @FormUrlEncoded
    @POST("drive/add_drive_order")
    Call<OtherDriver> addDriveOrder(@Field("admin_id") String adminId,
                                    @Field("customer_phone") String customerPhone,
                                    @Field("user_id") String userId,
                                    @Field("charging_id") int chargingId,
                                    @Field("origination") String origination,
                                    @Field("destination") String destination,
                                    @Field("longitude") String longitude,
                                    @Field("latitude") String latitude,
                                    @Field("percentage") String percentage
    );

    //司机发起提交订单
    @FormUrlEncoded
    @POST("drive/submit_drive_order")
    Call<BaseModel> submitDriveOrder(@Field("admin_id") String adminId,
                                     @Field("user_id") String user_id,
                                     @Field("order_id") String order_id,
                                     @Field("order_amount") String order_amount,
                                     @Field("destination") String destination,
                                     @Field("running_kilometre") String running_kilometre,
                                     @Field("wait_time") String wait_time,
                                     @Field("wait_money") String wait_money,
                                     @Field("running_money") String running_money
    );

    //代驾轨迹记录
    @FormUrlEncoded
    @POST("drive/add_trajectory")
    Call<BaseModel> addTrajectory(@Field("admin_id") String admin_id,
                                  @Field("user_id") String user_id,
                                  @Field("order_id") String order_id,
                                  @Field("trajectory") String trajectory
    );

    //获取订单详情
    @FormUrlEncoded
    @POST("drive/get_drive_order_list")
    Call<DriveOrderInfo> getDriveOrderList(@Field("admin_id") String admin_id,
                                           @Field("user_id") String user_id
    );

    //获取订单列表
    @FormUrlEncoded
    @POST("drive/get_drive_order_info")
    Call<DriveOrderInfo> getDriveOrderInfo(@Field("admin_id") String admin_id,
                                           @Field("user_id") String user_id,
                                           @Field("order_id") String order_id
    );

    //更新代驾数据
//    @FormUrlEncoded
//    @POST("updDriverInfo")
//    Call<> updDriverInfo();

    //
    @FormUrlEncoded
    @POST("chargingInfo")
    Call<Charging> chargingInfo(@Field("admin_id") String admin_id, @Field("driver_id") String driver_id);

    //更新在线数据
    @FormUrlEncoded
    @POST("drive/online")
    Call<BaseModel> online(@Field("admin_id") String admin_id,
                           @Field("user_id") String user_id,
                           @Field("driver_status") String driver_status,
                           @Field("longitude") String longitude,
                           @Field("latitude") String latitude);

    //更新坐标
    @FormUrlEncoded
    @POST("drive/update_coordinate")
    Observable<BaseResponse> update_coordinate(@FieldMap Map<String, String> options);
//    Observable<BaseResponse> update_coordinate(@Field("admin_id") String admin_id,
//                                      @Field("user_id") String user_id,
//                                      @Field("longitude") String longitude,
//                                      @Field("latitude") String latitude);
//    Call<BaseModel> update_coordinate(@Field("admin_id") String admin_id,
//                                      @Field("user_id") String user_id,
//                                      @Field("longitude") String longitude,
//                                      @Field("latitude") String latitude);
}
