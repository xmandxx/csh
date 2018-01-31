package com.xmwang.cyh.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xmwang on 2018/1/17.
 */

public class AlipayModel {




        private String orderstr;
        private String order_no;

        public String getOrderstr() {
            return orderstr;
        }
        public void setOrderstr(String orderstr) {
            this.orderstr = orderstr;
        }
        public String getOrder_no() {
            return order_no;
        }
        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

}
