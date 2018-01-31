package com.xmwang.cyh.model;

import java.util.List;

/**
 * Created by xmWang on 2018/1/15.
 */

public class MyCouponsModel {


        /**
         * add_time : 2017-05-27
         * coupon_id : 6
         * user_coupon_id : 2
         * is_default : 0
         * coupon_name : 消费满30元优惠2元
         * coupon_type : 0
         * start_time : 2017-04-26
         * end_time : 2017-05-30
         * coupon_pic : /data/favourable_action_pic/coupon9/original9_6_580x260.jpg
         * min_amount : 30.00
         * max_amount : 0.00
         * discount : 2.00
         * supplier_id : 9
         */

        private String add_time;
        private int coupon_id;
        private int user_coupon_id;
        private int is_default;
        private String coupon_name;
        private int coupon_type;
        private String start_time;
        private String end_time;
        private String coupon_pic;
        private String min_amount;
        private String max_amount;
        private String discount;
        private int supplier_id;

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public int getCoupon_id() {
            return coupon_id;
        }

        public void setCoupon_id(int coupon_id) {
            this.coupon_id = coupon_id;
        }

        public int getUser_coupon_id() {
            return user_coupon_id;
        }

        public void setUser_coupon_id(int user_coupon_id) {
            this.user_coupon_id = user_coupon_id;
        }

        public int getIs_default() {
            return is_default;
        }

        public void setIs_default(int is_default) {
            this.is_default = is_default;
        }

        public String getCoupon_name() {
            return coupon_name;
        }

        public void setCoupon_name(String coupon_name) {
            this.coupon_name = coupon_name;
        }

        public int getCoupon_type() {
            return coupon_type;
        }

        public void setCoupon_type(int coupon_type) {
            this.coupon_type = coupon_type;
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

        public int getSupplier_id() {
            return supplier_id;
        }

        public void setSupplier_id(int supplier_id) {
            this.supplier_id = supplier_id;
        }

}
