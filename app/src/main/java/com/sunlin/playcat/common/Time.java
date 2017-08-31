package com.sunlin.playcat.common;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
    public static String getTimeTalk(Date parse,Date parse1){
        Calendar calendar=new GregorianCalendar();
        calendar.setTime(parse);

        Calendar calendar1=new GregorianCalendar();
        calendar1.setTime(parse1);


        int year=calendar.get(Calendar.YEAR);
        int month=calendar.get(Calendar.MONTH)+1;
        int day=calendar.get(Calendar.DAY_OF_MONTH);
        int hour=calendar.get(Calendar.HOUR_OF_DAY);
        int minute=calendar.get(Calendar.MINUTE);

        int year1=calendar1.get(Calendar.YEAR);
        int month1=calendar1.get(Calendar.MONTH)+1;
        int day1=calendar1.get(Calendar.DAY_OF_MONTH);
        int hour1=calendar1.get(Calendar.HOUR_OF_DAY);
        int minute1=calendar1.get(Calendar.MINUTE);

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String hourStr=dateFormat.format(parse);
        if(year==year1&&month==month1){
            int temp=day1-day;
            if(temp==0){
                return hourStr;
            }else if(temp==1){
                return "昨天 "+hourStr;
            }
            else if(temp==2){
                return "前天 "+hourStr;
            }
            return  month+"月"+day+"日 "+hourStr;
        }else if(year==year1){
            return  month+"月"+day+"日 "+hourStr;
        }else{
            return  year+"年"+month+"月"+day+"日 "+hourStr;
        }

    }
    public static String getTimeDifference(Date parse, Date parse1) {
        String timeString = "";
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
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
