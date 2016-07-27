package com.mxk.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class TimeUtil
{
    public static final String STR_FORMAT_MMM_D_YYYY = "MMM d, yyyy";
    public static final String STR_FORMAT_MMM_D = "MMM d";
    public static final String STR_FORMAT_HH_MM = "HH:mm";
    public static final String STR_FORMAT_YYYYMMDDHHMM = "yyyyMMDDHHmm";

    public static String getDateFormat(long milliseconds)
    {
        Date date = new Date(milliseconds);
        Date curDate = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf;
        if (date.getYear() != curDate.getYear())
        {
            sdf = new SimpleDateFormat(STR_FORMAT_MMM_D_YYYY, Locale.CHINA);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        }
        else if (date.getMonth() != curDate.getMonth()
            || date.getDate() != curDate.getDate())
        {
            sdf = new SimpleDateFormat(STR_FORMAT_MMM_D, Locale.CHINA);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        }
        else
        {
            sdf = new SimpleDateFormat(STR_FORMAT_HH_MM, Locale.CHINA);
            sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        }
        return sdf.format(date);

    }

    public static String getDrawerPaintFormat()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf;

        sdf = new SimpleDateFormat(STR_FORMAT_YYYYMMDDHHMM, Locale.CHINA);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));

        return sdf.format(date);

    }
}