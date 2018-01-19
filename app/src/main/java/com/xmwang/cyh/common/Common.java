package com.xmwang.cyh.common;

import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.Map;

/**
 * Created by xmwang on 2017/12/19.
 */

public class Common {
    public static String baseUrl = "http://api.itopv.com/base/api/";
    public static String adminId = "1";
    public static String amapKey = "fdd0670eb8a61e9920b280b6fe1f21b8";
    public static boolean isEmpty(String str){
        if (str == null){
            return true;
        }
        if (str.isEmpty()){
            return true;
        }
        if (str.equals("")){
            return true;
        }
        return false;
    }
    public static boolean isEmptyTrim(String str) {
        if (str == null){
            return true;
        }
        return Common.isEmpty(str.trim());
    }
    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、152、157(TD)、158、159、178(新)、182、184、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、170、173、177、180、181、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        String num = "[1][345789]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (Common.isEmptyTrim(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.trim().matches(num);
        }
    }
    public static android.view.Display getWindow(android.content.Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
    }

    public static int getWindowWidth(android.content.Context context) {
        return getWindow(context).getWidth();
    }

    public static int getWindowHeight(android.content.Context context) {
        return getWindow(context).getHeight();
    }

    /**
     * 判断字符串是否为null或空
     *
     * @param string
     * @return true:为 空或null;false:不为 空或null
     */
    public static boolean isNullOrEmpty(String string) {
        boolean flag = false;
        if (null == string || string.trim().length() == 0) {
            flag = true;
        }
        return flag;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dpVal
     * @return
     */
    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, context.getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param context
     * @param spVal
     * @return
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px转dp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2dp(Context context, float pxVal) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxVal / scale);
    }

    /**
     * px转sp
     *
     * @param context
     * @param pxVal
     * @return
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    public static Calendar getCalendar(){
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        return c;
//        int year = c.get(Calendar.YEAR);
//        int month = c.get(Calendar.MONTH);
//        int date = c.get(Calendar.DATE);
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//        int minute = c.get(Calendar.MINUTE);
//        int second = c.get(Calendar.SECOND);
    }
    /**
     * 获取系统SDK版本
     * @return
     */
    public static int getSDKVersionNumber() {
        int sdkVersion;
        try {
            sdkVersion = Integer.valueOf(android.os.Build.VERSION.SDK_INT);
        } catch (NumberFormatException e) {
            sdkVersion = 0;
        }
        return sdkVersion;
    }

    /**
     * 将Map转化为Json
     *
     * @param map
     * @return String
     */
    public static String mapToJson(Map<String, String> map) {
        String str = "{";
        int i = 0;
        for (String key : map.keySet()) {
            if (i > 0){
                str += ",";
            }
            str += "\"";
            str += key;
            str += "\":\"";
            str += map.get(key);
            str += "\"";
            i++;
        }
        str += "}";
        return str;
//        Gson gson = new Gson();
//        String jsonStr = gson.toJson(map);
//        return jsonStr;
    }
}
