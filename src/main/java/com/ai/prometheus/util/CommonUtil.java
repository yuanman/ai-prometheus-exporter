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
}
