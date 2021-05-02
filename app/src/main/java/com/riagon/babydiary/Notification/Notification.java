package com.riagon.babydiary.Notification;


import android.content.Context;

import com.riagon.babydiary.Data.DatabaseHelper;

public class Notification {

    private static final String reminderStatus = "reminderStatus";
    private static final String repeatStatus = "repeatStatus";
    private static final String day = "day";
    private static final String hour = "hour";
    private static final String min = "min";
    private String time;
    private String date;
    private DatabaseHelper db;


    public Notification(Context context) {

        db= new DatabaseHelper(context);

    }


    public int get_hour() {
        return parseHour(time);
    }

    public int get_min() {
        return parseMinute(time);
    }


    public static int parseHour(String value)
    {
        try
        {
            String[] time = value.split(":");
            return (Integer.parseInt(time[0]));
        }
        catch (Exception e)
        {
            return 0;
        }
    }

    public static int parseMinute(String value)
    {
        try
        {
            String[] time = value.split(":");
            return (Integer.parseInt(time[1]));
        }
        catch (Exception e)
        {
            return 0;
        }
    }

}
