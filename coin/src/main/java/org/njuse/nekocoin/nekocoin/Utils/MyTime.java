package org.njuse.nekocoin.nekocoin.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyTime {
    public static String getSystemTime(){
        Date date = new Date();
        //设置要获取到什么样的时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        //获取String类型的时间
        String createdate = sdf.format(date);
        return createdate;
    }
}
