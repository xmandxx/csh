package com.xmwang.cyh.model;

import java.util.List;

/**
 * Created by xmwang on 2017/12/29.
 */

public class TempInfo {


        /**
         * charging_id : 19
         * driver_id : 0
         * charging : D模板(一线城市)
         * wait_time : 10
         * wait_money : 0.00
         * ave_time : 1
         * ave_min_time : 1
         * ave_minute_money : 1.00
         * add_time : 2017-12-28 12:40:06
         * admin_id : 1
         * is_default : 0
         * times : [{"time_id":17,"start_amount":"98.00","start_kilometre":10,"average_kilometre":10,"average_amount":"20.00","start_time":0,"end_time":359,"msg":null},{"time_id":14,"start_amount":"38.00","start_kilometre":10,"average_kilometre":10,"average_amount":"20.00","start_time":360,"end_time":1319,"msg":null},{"time_id":15,"start_amount":"58.00","start_kilometre":10,"average_kilometre":10,"average_amount":"20.00","start_time":1320,"end_time":1379,"msg":null},{"time_id":16,"start_amount":"78.00","start_kilometre":10,"average_kilometre":10,"average_amount":"20.00","start_time":1380,"end_time":1439,"msg":null}]
         */

        private int charging_id;
        private int driver_id;
        private String charging;
        private int wait_time;
        private String wait_money;
        private int ave_time;
        private int ave_min_time;
        private String ave_minute_money;
        private String add_time;
        private int admin_id;
        private int is_default;
        private List<TimesBean> times;

        public int getCharging_id() {
            return charging_id;
        }

        public void setCharging_id(int charging_id) {
            this.charging_id = charging_id;
        }

        public int getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(int driver_id) {
            this.driver_id = driver_id;
        }

        public String getCharging() {
            return charging;
        }

        public void setCharging(String charging) {
            this.charging = charging;
        }

        /**
         * 免费等待时间
         * @return
         */
        public int getWait_time() {
            return wait_time;
        }

        public void setWait_time(int wait_time) {
            this.wait_time = wait_time;
        }

        public String getWait_money() {
            return wait_money;
        }

        public void setWait_money(String wait_money) {
            this.wait_money = wait_money;
        }

        /**
         * 超出多少时间开始计费
         * @return
         */
        public int getAve_time() {
            return ave_time;
        }

        public void setAve_time(int ave_time) {
            this.ave_time = ave_time;
        }

        public int getAve_min_time() {
            return ave_min_time;
        }

        public void setAve_min_time(int ave_min_time) {
            this.ave_min_time = ave_min_time;
        }

        /**
         * 超出时间后收费金额
         * @return
         */
        public String getAve_minute_money() {
            return ave_minute_money;
        }

        public void setAve_minute_money(String ave_minute_money) {
            this.ave_minute_money = ave_minute_money;
        }

        public String getAdd_time() {
            return add_time;
        }

        public void setAdd_time(String add_time) {
            this.add_time = add_time;
        }

        public int getAdmin_id() {
            return admin_id;
        }

        public void setAdmin_id(int admin_id) {
            this.admin_id = admin_id;
        }

        public int getIs_default() {
            return is_default;
        }

        public void setIs_default(int is_default) {
            this.is_default = is_default;
        }

        public List<TimesBean> getTimes() {
            return times;
        }

        public void setTimes(List<TimesBean> times) {
            this.times = times;
        }

        public static class TimesBean {
            /**
             * time_id : 17
             * start_amount : 98.00
             * start_kilometre : 10
             * average_kilometre : 10
             * average_amount : 20.00
             * start_time : 0
             * end_time : 359
             * msg : null
             */

            private int time_id;
            private String start_amount;
            /**
             * 起步公里数
             * @return
             */
            private int start_kilometre;
            private int average_kilometre;
            private String average_amount;
            private int start_time;
            private int end_time;
            private Object msg;

            public int getTime_id() {
                return time_id;
            }

            public void setTime_id(int time_id) {
                this.time_id = time_id;
            }

            /**
             * 起步价
             * @return
             */
            public String getStart_amount() {
                return start_amount;
            }

            public void setStart_amount(String start_amount) {
                this.start_amount = start_amount;
            }

            /**
             * 起步公里数
             * @return
             */
            public int getStart_kilometre() {
                return start_kilometre;
            }

            public void setStart_kilometre(int start_kilometre) {
                this.start_kilometre = start_kilometre;
            }

            /**
             * 超出后多少公里开始加价
             * @return
             */
            public int getAverage_kilometre() {
                return average_kilometre;
            }

            public void setAverage_kilometre(int average_kilometre) {
                this.average_kilometre = average_kilometre;
            }

            /**
             * 超出后每公里加价金额
             * @return
             */
            public String getAverage_amount() {
                return average_amount;
            }

            public void setAverage_amount(String average_amount) {
                this.average_amount = average_amount;
            }

            public int getStart_time() {
                return start_time;
            }

            public void setStart_time(int start_time) {
                this.start_time = start_time;
            }

            public int getEnd_time() {
                return end_time;
            }

            public void setEnd_time(int end_time) {
                this.end_time = end_time;
            }

            public Object getMsg() {
                return msg;
            }

            public void setMsg(Object msg) {
                this.msg = msg;
            }
        }

}
