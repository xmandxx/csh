package com.xmwang.cyh.model;

import java.util.List;

/**
 * Created by xmWang on 2018/1/20.
 */

public class OrderModel {


        /**
         * order_id : 110
         * goods_name : 强暴鸡米花
         * goods_price : 8.50
         * goods_number : 67
         * goods_amount : 569.50
         * original_img : images/201704/source_img/632_G_1493164159319.jpg
         * supplier_name : shop
         * order_type : 1
         */

        private int order_id;
        private String goods_name;
        private String goods_price;
        private int goods_number;
        private String goods_amount;
        private String original_img;
        private String supplier_name;
        private int order_type;

        public int getOrder_id() {
            return order_id;
        }

        public void setOrder_id(int order_id) {
            this.order_id = order_id;
        }

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public String getGoods_price() {
            return goods_price;
        }

        public void setGoods_price(String goods_price) {
            this.goods_price = goods_price;
        }

        public int getGoods_number() {
            return goods_number;
        }

        public void setGoods_number(int goods_number) {
            this.goods_number = goods_number;
        }

        public String getGoods_amount() {
            return goods_amount;
        }

        public void setGoods_amount(String goods_amount) {
            this.goods_amount = goods_amount;
        }

        public String getOriginal_img() {
            return original_img;
        }

        public void setOriginal_img(String original_img) {
            this.original_img = original_img;
        }

        public String getSupplier_name() {
            return supplier_name;
        }

        public void setSupplier_name(String supplier_name) {
            this.supplier_name = supplier_name;
        }

        public int getOrder_type() {
            return order_type;
        }

        public void setOrder_type(int order_type) {
            this.order_type = order_type;
        }

}
