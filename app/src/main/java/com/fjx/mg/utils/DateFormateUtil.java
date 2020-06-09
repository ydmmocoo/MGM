package com.fjx.mg.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateFormateUtil {

    //2019-06-05 17:03:31
    private static final String FORMAT1 = "yyyy-MM-dd HH:mm:ss";


    public static long getTime(String date) {
        DateFormat format = new SimpleDateFormat(FORMAT1);
        try {
            return format.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0l;
    }

}
