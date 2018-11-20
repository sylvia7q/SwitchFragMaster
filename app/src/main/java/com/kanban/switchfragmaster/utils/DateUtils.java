package com.kanban.switchfragmaster.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    //获取日期
    public static String getDate() {
        //日期
        SimpleDateFormat simpleDat = new SimpleDateFormat("yyyy-MM-dd");
        String date = simpleDat.format(new Date(new Date().getYear(),new Date().getMonth(),new Date().getDate()-1));
        return date;
    }
    //获取日期
    public static String getTime() {
        //时间
        SimpleDateFormat simpleTime = new SimpleDateFormat("HH:mm");
        String time = simpleTime.format(new Date());
        return time;
    }
    public static String getDateTime(){
        return getDate() + " " + getTime();
    }
}
