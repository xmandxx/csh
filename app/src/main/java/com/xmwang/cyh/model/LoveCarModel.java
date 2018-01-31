package com.xmwang.cyh.model;

import java.util.List;

/**
 * Created by xmwang on 2018/1/20.
 */

public class LoveCarModel {


        /**
         * car_id : 11
         * car_name : Jeep汽车
         * car_year : 1478620800
         * car_card : 豫A88888
         * car_kilometre : 200
         * car_number : 123456
         * user_id : 69
         * is_default : 0
         * cars_imgs :
         * admin_id : 1
         */

        private int car_id;
        private String car_name;
        private int car_year;
        private String car_card;
        private int car_kilometre;
        private String car_number;
        private int user_id;
        private int is_default;
        private String cars_imgs;
        private int admin_id;

        public int getCar_id() {
            return car_id;
        }

        public void setCar_id(int car_id) {
            this.car_id = car_id;
        }

        public String getCar_name() {
            return car_name;
        }

        public void setCar_name(String car_name) {
            this.car_name = car_name;
        }

        public int getCar_year() {
            return car_year;
        }

        public void setCar_year(int car_year) {
            this.car_year = car_year;
        }

        public String getCar_card() {
            return car_card;
        }

        public void setCar_card(String car_card) {
            this.car_card = car_card;
        }

        public int getCar_kilometre() {
            return car_kilometre;
        }

        public void setCar_kilometre(int car_kilometre) {
            this.car_kilometre = car_kilometre;
        }

        public String getCar_number() {
            return car_number;
        }

        public void setCar_number(String car_number) {
            this.car_number = car_number;
        }

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public int getIs_default() {
            return is_default;
        }

        public void setIs_default(int is_default) {
            this.is_default = is_default;
        }

        public String getCars_imgs() {
            return cars_imgs;
        }

        public void setCars_imgs(String cars_imgs) {
            this.cars_imgs = cars_imgs;
        }

        public int getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(int admin_id) {
            this.admin_id = admin_id;
        }

}
