package com.sunlin.playcat.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by sunlin on 2017/7/26.
 */

public class Time {
    /**
     * 计算相差的小时
     *
     * @param starTime
     * @param endTime
     * @return
     */
    public static String getTimeDifferenceHour(String starTime, String endTime) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date parse = dateFormat.parse(starTime);
            Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();
            String string = Long.toString(diff);

            float parseFloat = Float.parseFloat(string);

            float hour1 = parseFloat / (60 * 60 * 1000);
            timeString = Float.toString(hour1);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return timeString;

    }
    public static String getTimeDifference(Date parse, Date parse1) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            //Date parse = dateFormat.parse(starTime);
            //Date parse1 = dateFormat.parse(endTime);

            long diff = parse1.getTime() - parse.getTime();

            long day = diff / (24 * 60 * 60 * 1000);

        if(day>3){
            return dateFormat.format(parse);
        }

            long hour = (diff / (60 * 60 * 1000) - day * 24);
            long min = ((diff / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (diff / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
            long ms = (diff - day * 24 * 60 * 60 * 1000 - hour * 60 * 60 * 1000
                    - min * 60 * 1000 - s * 1000);
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +
            // "秒");
            long hour1 = diff / (60 * 60 * 1000);
            String hourString = hour1 + "";
            long min1 = ((diff / (60 * 1000)) - hour1 * 60);
            if(hour1>1){
                timeString = hour1 + "小时" ;
            }else{
                timeString =min1 + "分";
            }
            // System.out.println(day + "天" + hour + "小时" + min + "分" + s +
            // "秒");
        return timeString+"前";

    }

}
