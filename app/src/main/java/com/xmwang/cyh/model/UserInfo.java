package com.xmwang.cyh.model;

import java.util.List;

/**
 * Created by xmwang on 2017/12/29.
 */

public class UserInfo {
    /**
     * code : 200
     * message : 用户信息
     * data : [{"user_name":"18937935591","user_id":69,"password":"e10adc3949ba59abbe56e057f20f883e","mobile_phone":"18937935591","admin_id":1,"headimg":"/data/headimg/20170906/1fb4424bc1dc1779b541164b381149bb.jpeg","driver_id":9,"rank_points":0,"user_money":"0.00","sex":1,"real_name":"我们自己的","collection":0}]
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
         * user_name : 18937935591
         * user_id : 69
         * password : e10adc3949ba59abbe56e057f20f883e
         * mobile_phone : 18937935591
         * admin_id : 1
         * headimg : /data/headimg/20170906/1fb4424bc1dc1779b541164b381149bb.jpeg
         * driver_id : 9
         * rank_points : 0
         * user_money : 0.00
         * sex : 1
         * real_name : 我们自己的
         * collection : 0
         */

        private String user_name;
        private int user_id;
        private String password;
        private String mobile_phone;
        private int admin_id;
        private String headimg;
        private int driver_id;
        private int rank_points;
        private String user_money;
        private int sex;
        private String real_name;
        private int collection;

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getMobile_phone() {
            return mobile_phone;
        }

        public void setMobile_phone(String mobile_phone) {
            this.mobile_phone = mobile_phone;
        }

        public int getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(int admin_id) {
            this.admin_id = admin_id;
        }

        public String getHeadimg() {
            return headimg;
        }

        public void setHeadimg(String headimg) {
            this.headimg = headimg;
        }

        public int getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(int driver_id) {
            this.driver_id = driver_id;
        }

        public int getRank_points() {
            return rank_points;
        }

        public void setRank_points(int rank_points) {
            this.rank_points = rank_points;
        }

        public String getUser_money() {
            return user_money;
        }

        public void setUser_money(String user_money) {
            this.user_money = user_money;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getReal_name() {
            return real_name;
        }

        public void setReal_name(String real_name) {
            this.real_name = real_name;
        }

        public int getCollection() {
            return collection;
        }

        public void setCollection(int collection) {
            this.collection = collection;
        }
    }
}
