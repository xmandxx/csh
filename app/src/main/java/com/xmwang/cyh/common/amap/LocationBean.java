package com.xmwang.cyh.common.amap;

import java.io.Serializable;

/**
 * Created by xmwang on 2018/1/27.
 */

public class LocationBean implements Serializable {

    private double longitude;
    private double latitude;
    private String title;
    private String content;

    public LocationBean(double longitude,double latitude,String title,String content){
        this.longitude = longitude;
        this.latitude = latitude;
        this.title = title;
        this.content = content;
    }

    /**
     * 经度
     * @return
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * 纬度
     * @return
     */
    public double getLatitude() {
        return latitude;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
