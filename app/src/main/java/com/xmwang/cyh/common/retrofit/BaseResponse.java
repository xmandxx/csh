package com.xmwang.cyh.common.retrofit;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by xmwang on 2018/1/30.
 */

public class BaseResponse<T> implements Serializable {
    @SerializedName("code")
    public int code;
    @SerializedName("message")
    public String message;
    @SerializedName("data")
    public T data;
}
