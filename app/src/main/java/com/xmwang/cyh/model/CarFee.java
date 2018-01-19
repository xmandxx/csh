package com.xmwang.cyh.model;

import java.util.List;

/**
 * Created by xmWang on 2018/1/16.
 */

public class CarFee {

    /**
     * code : 200
     * message : 获取成功
     * data : [{"date":"01-16","fee_type":1,"fee_title":"维修","fee_money":"130.00"},{"date":"01-16","fee_type":2,"fee_title":"保险","fee_money":"3600.27"}]
     */

    private int code;
    private String message;
    private List<DataBean> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * date : 01-16
         * fee_type : 1
         * fee_title : 维修
         * fee_money : 130.00
         */

        private String date;
        private int fee_type;
        private String fee_title;
        private String fee_money;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getFee_type() {
            return fee_type;
        }

        public void setFee_type(int fee_type) {
            this.fee_type = fee_type;
        }

        public String getFee_title() {
            return fee_title;
        }

        public void setFee_title(String fee_title) {
            this.fee_title = fee_title;
        }

        public String getFee_money() {
            return fee_money;
        }

        public void setFee_money(String fee_money) {
            this.fee_money = fee_money;
        }
    }
}
