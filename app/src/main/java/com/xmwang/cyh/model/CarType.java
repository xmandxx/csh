package com.xmwang.cyh.model;

/**
 * Created by wangxiaoming on 2018/2/5.
 */



public class CarType{
        /**
         * cartype_id : 6
         * logo : images/201612/1481434156589082614.png
         * name : AC Schnitzer 7系
         */

        private int cartype_id;
        private String logo;
        private String name;

        public int getCartype_id() {
            return cartype_id;
        }

        public void setCartype_id(int cartype_id) {
            this.cartype_id = cartype_id;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
}
