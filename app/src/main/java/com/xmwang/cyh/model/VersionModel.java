package com.xmwang.cyh.model;

/**
 * Created by wangxiaoming on 2018/2/2.
 */

public class VersionModel {


    /**
     * version_name : 1.0.1
     * introduce : 1.很厉害
     * 2.超级厉害
     * force_update : 0
     * version_code : 1
     */

    private String version_name;
    private String introduce;
    private int force_update;
    private int version_code;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String url;
    public String getVersion_name() {
        return version_name;
    }

    public void setVersion_name(String version_name) {
        this.version_name = version_name;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public int getForce_update() {
        return force_update;
    }

    public void setForce_update(int force_update) {
        this.force_update = force_update;
    }

    public int getVersion_code() {
        return version_code;
    }

    public void setVersion_code(int version_code) {
        this.version_code = version_code;
    }

}
