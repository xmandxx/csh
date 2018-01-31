package com.xmwang.cyh.model;

import java.util.List;

/**
 * Created by xmwang on 2018/1/5.
 */

public class NewGoodsList {

        /**
         * goods_name : 强暴鸡米花
         * goods_id : 632
         * supplier_id : 9
         * market_price : 9.60
         * shop_price : 8.00
         * goods_img : themesmobile/68ecshopcom_mobile/images/pic14-1.jpg
         * sale_num : 10
         */

        private String goods_name;
        private int goods_id;
        private int supplier_id;
        private String market_price;
        private String shop_price;
        private String goods_img;
        private int sale_num;

        public String getGoods_name() {
            return goods_name;
        }

        public void setGoods_name(String goods_name) {
            this.goods_name = goods_name;
        }

        public int getGoods_id() {
            return goods_id;
        }

        public void setGoods_id(int goods_id) {
            this.goods_id = goods_id;
        }

        public int getSupplier_id() {
            return supplier_id;
        }

        public void setSupplier_id(int supplier_id) {
            this.supplier_id = supplier_id;
        }

        public String getMarket_price() {
            return market_price;
        }

        public void setMarket_price(String market_price) {
            this.market_price = market_price;
        }

        public String getShop_price() {
            return shop_price;
        }

        public void setShop_price(String shop_price) {
            this.shop_price = shop_price;
        }

        public String getGoods_img() {
            return goods_img;
        }

        public void setGoods_img(String goods_img) {
            this.goods_img = goods_img;
        }

        public int getSale_num() {
            return sale_num;
        }

        public void setSale_num(int sale_num) {
            this.sale_num = sale_num;
        }
    }

