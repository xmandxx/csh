package com.xmwang.cyh.model;

import java.util.List;

/**
 * Created by xmWang on 2018/1/15.
 */

public class QiangCouponsModel {


        /**
         * coupon_id : 10
         * coupon_name : 10元抵用券
         * type : 1
         * start_time : 2017-12-24
         * end_time : 2017-12-29
         * coupon_num : 20
         * coupon_pic : /data/favourable_action_pic/coupon0/original0_10_580x260.jpg
         * min_amount : 0.00
         * max_amount : 0.00
         * discount : 10.00
         * user_coupon_num : 0
         * supplier_id : 0
         * admin_id : 1
         */

        private int coupon_id;
        private String coupon_name;
        private int type;
        private String start_time;
        private String end_time;
        private int coupon_num;
        private String coupon_pic;
        private String min_amount;
        private String max_amount;
        private String discount;
        private int user_coupon_num;
        private int supplier_id;
        private int admin_id;
        private String bai;
        public String getBai() {
            return bai;
        }

        public void setBai(String bai) {
            this.bai = bai;
        }

        public int getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(int coupon_id) {
            this.coupon_id = coupon_id;
        }

        public String getCoupon_name() {
            return coupon_name;
        }

        public void setCoupon_name(String coupon_name) {
            this.coupon_name = coupon_name;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public int getCoupon_num() {
            return coupon_num;
        }

        public void setCoupon_num(int coupon_num) {
            this.coupon_num = coupon_num;
        }

        public String getCoupon_pic() {
            return coupon_pic;
        }

        public void setCoupon_pic(String coupon_pic) {
            this.coupon_pic = coupon_pic;
        }

        public String getMin_amount() {
            return min_amount;
        }

        public void setMin_amount(String min_amount) {
            this.min_amount = min_amount;
        }

        public String getMax_amount() {
            return max_amount;
        }

        public void setMax_amount(String max_amount) {
            this.max_amount = max_amount;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public int getUser_coupon_num() {
            return user_coupon_num;
        }

        public void setUser_coupon_num(int user_coupon_num) {
            this.user_coupon_num = user_coupon_num;
        }

        public int getSupplier_id() {
            return supplier_id;
        }

        public void setSupplier_id(int supplier_id) {
            this.supplier_id = supplier_id;
        }

        public int getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(int admin_id) {
            this.admin_id = admin_id;
        }
    }

