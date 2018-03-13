package com.ai.prometheus.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CommonUtil {
    public static String getBeforeDay() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1); // 得到前一天的日期字符串
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    public static String getCurrentDay() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 0); // 得到当天的日期字符串
        Date date = calendar.getTime();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * 给第字符串第一个字母大写
     * 
     * @param str
     * @return
     */
    public static String capitalize(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return str;
        }
        return new StringBuilder(strLen).append(Character.toTitleCase(str.charAt(0))).append(str.substring(1))
            .toString();
    }

    /**
     * 使用StringBuilder拼接字符串
     * 
     * @param objects
     * @return
     */
    public static String appendSbl(Object... objects) {
        StringBuilder sbl = new StringBuilder();
        for (Object obj : objects) {
            sbl.append(obj);
        }
        return sbl.toString();
    }

    /**
     * 使用StringBuffer拼接字符串
     * 
     * @param objects
     * @return
     */
    public static String appendSbf(Object... objects) {
        StringBuffer sbl = new StringBuffer();
        for (Object obj : objects) {
            sbl.append(obj);
        }
        return sbl.toString();
    }

    /**
     * 根据字符串，获取后缀
     * 
     * @param str 若获取不到，返回 null
     */
    public static String getSuffix(String str) {
        if (null != str && str.lastIndexOf(".") > 0) {
            str = str.substring(str.lastIndexOf("."), str.length());
        } else {
            str = null;
        }
        return str;
    }
}
