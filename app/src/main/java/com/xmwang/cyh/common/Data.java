package com.xmwang.cyh.common;

import android.content.Context;
import android.location.Location;

import com.xmwang.cyh.MyApplication;
import com.xmwang.cyh.model.DriveInfo;
import com.xmwang.cyh.model.TempInfo;
import com.xmwang.cyh.model.UserInfo;

import cn.jpush.android.api.JPushInterface;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xmwang on 2017/12/29.
 */

public enum Data {
    instance;
    public String baseUrl = "http://api.che377.com/api/";
    public String baseAdminUrl = "http://admin.che377.com/";
    public final String USER_ID_KEY = "s_user_id";
    public final String WXKEY = "wxa856b974892faaa7";
    public final String ALIPAY_KEY = "wxa856b974892faaa7";
    public String AdminId = "1";
    private boolean isLogin;
    private boolean isOnline;
    private String userId;
    private int chargingId;
    private double percentage;  //里程比例
    private String driverOrderId;
    private DriveInfo.DataBean driveInfo;
    private UserInfo.DataBean userInfo;
    private TempInfo.DataBean tempInfo;
    private Location location;
    private String formatAddress;
    private String tempDestination;//临时存储目的地，非正式不传递给数据库

    public String getTempDestination() {
        return tempDestination;
    }

    public void setTempDestination(String tempDestination) {
        this.tempDestination = tempDestination;
    }
    public Context getContext() {
        if (mContext == null){
            mContext = MyApplication.getContext();
        }
        return mContext;
    }

    private Context mContext;
    public void setFormatAddress(String formatAddress) {
        this.formatAddress = formatAddress;
    }

    public String getFormatAddress() {
        return formatAddress;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }



    public void setTempInfo(TempInfo.DataBean tempInfo) {
        this.tempInfo = tempInfo;
    }
    public TempInfo.DataBean getTempInfo() {
        return tempInfo;
    }

    public boolean getIsLogin(){
        return !getUserId().equals("0");
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean getIsOnline(){
        return isOnline;
    }

    public void setUserId(String userId){
        this.userId = userId;
        if (userId == null){
            SPUtils.instance.remove(USER_ID_KEY);
            JPushInterface.deleteAlias(getContext(),10036);
        }else {
            SPUtils.instance.put(USER_ID_KEY, userId);
            JPushInterface.setAlias(getContext(),10036,userId);
        }
    }
    public String getUserId(){
        if (userId == null){
            userId = SPUtils.instance.get(USER_ID_KEY,"0").toString();
        }
        return this.userId;
    }


    public void setChargingId(int chargingId){
        this.chargingId = chargingId;
    }
    public int getChargingId(){
        return chargingId;
    }


    public void setPercentage(double percentage){
        this.percentage = percentage;
    }
    public double getPercentage(){
        return percentage;
    }

    public String getDriverOrderId() {
        return driverOrderId;
    }

    public void setDriverOrderId(String driverOrderId) {
        this.driverOrderId = driverOrderId;
    }




    public void setDriveInfo(DriveInfo.DataBean driveInfo) {
        this.driveInfo = driveInfo;
        this.setChargingId(driveInfo.getCharging_id());
        this.setPercentage(Double.valueOf(driveInfo.getPercentage()));
    }

    public DriveInfo.DataBean getDriveInfo() {
        return driveInfo;
    }




    public UserInfo.DataBean getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo.DataBean userInfo) {
        this.userInfo = userInfo;
        if (userInfo == null){
            this.setUserId(null);
        }else{
            this.setUserId(String.valueOf(userInfo.getUser_id()));
        }

    }

    public void reUserInfo(){
        if (!this.getIsLogin()){
            return;
        }
        retrofit2.Call<UserInfo> call = RetrofitHelper.instance.getApiUserService().getuserinfo(
                Data.instance.AdminId,
                Data.instance.getUserId()
        );

        call.enqueue(new Callback<UserInfo>() {
            @Override
            public void onResponse(retrofit2.Call<UserInfo> call, Response<UserInfo> response) {

                UserInfo model = response.body();
                if (model.getCode() != RetrofitHelper.instance.SUCCESS_CODE) {
                    return;
                }
                if (model.getData().size() > 0) {
                    Data.instance.setUserInfo(model.getData().get(0));
                }

            }

            @Override
            public void onFailure(retrofit2.Call<UserInfo> call, Throwable t) {

            }
        });
    }
}
