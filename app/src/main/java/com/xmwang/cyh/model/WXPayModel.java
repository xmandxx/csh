package com.xmwang.cyh.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by xmwang on 2018/1/17.
 */

public class WXPayModel {


        /**
         * noncestr : 6G7ZLxSVZ6Ee9qZb
         * package : Sign=WXPay
         * partnerid : 1492050762
         * prepayid : wx201801171727386b9ad6a4420347499616
         * timestamp : 1516181258
         * sign : ED9A89DDA00615F9F440CF9530568942
         * order_no : WXCZ1801173894746
         */

        private String noncestr;
        @SerializedName("package")
        private String packageX;
        private String partnerid;
        private String prepayid;
        private int timestamp;
        private String sign;
        private String order_no;

        public String getNoncestr() {
            return noncestr;
        }

        public void setNoncestr(String noncestr) {
            this.noncestr = noncestr;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public int getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(int timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getOrder_no() {
            return order_no;
        }

        public void setOrder_no(String order_no) {
            this.order_no = order_no;
        }

}
