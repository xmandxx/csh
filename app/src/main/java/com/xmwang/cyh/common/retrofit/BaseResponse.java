package com.xmwang.cyh.common.retrofit;

import com.google.gson.annotations.SerializedName;
import com.xmwang.cyh.common.RetrofitHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xmwang on 2018/1/30.
 */

public class BaseResponse<T> implements Serializable {
    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public List<T> data;
//    @SerializedName("dataInfo")
    public T dataInfo;
    public T getDataInfo(){
        if (data == null){
            return null;
        }
        if (data.size() > 0){
            return data.get(0);
        }
        return null;
    }
//    @SerializedName("dataList")
    public List<T> dataList;
    public List<T> getDataList(){
        if (data == null){
            return new ArrayList<>();
        }
        return data;
    }
    public boolean isSuccess(){
        return code == RetrofitHelper.instance.SUCCESS_CODE;
    }
}
