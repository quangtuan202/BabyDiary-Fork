package com.riagon.babydiary.Utility;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.os.ConfigurationCompat;

import com.riagon.babydiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatUtility {

    private Context context;

    public DateFormatUtility() {
    }

    public DateFormatUtility(Context context) {
        this.context = context;
    }

    // convert from String to Date
    public Date getDateFormat(String string) {
        Date date = null;
        //SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        try {
            date = formatter.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // convert from String to Date
    public Date getDateFormatWithHours(String string) {
        Date date = null;
        //SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        try {
            date = formatter.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public Date getDateFullFormat(String string) {
        Date date = null;
        //SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            date = formatter.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



    // convert from String to Date
    public Date getDateFormat2(String string) {
        Date date = null;
        //SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = formatter.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    // convert from String to Time
    public Date getTimeFormat(String string) {
        Date time = null;
        //SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("H:m");
        try {
            time = formatter.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    // convert from String to Time
    public Date getTimeFormat2(String string) {
        Date time = null;
        //SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        try {
            time = formatter.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }



    // convert from String to Time
    public Date getTimeFormat3(String string) {
        Date time = null;
        //SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("mm:ss");
        try {
            time = formatter.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    // convert from String to Time
    public Date getTimeRepeatInFormat(String string) {
        Date time = null;
        //SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat formatter = new SimpleDateFormat("dd:HH:mm");
        try {
            time = formatter.parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return time;
    }

    // convert from Time to String
    public String getStringTimeFormat(Date date) {
        String dateString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        dateString = formatter.format(date);
        return dateString;
    }

    // convert from Time to String
    public String getStringTimeFormat2(Date date) {
        String dateString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        dateString = formatter.format(date);
        return dateString;
    }


    // convert from Time to String
    public String getStringTimeRepeatInFormat(Date date) {
        String dateString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("dd:HH:mm");
        dateString = formatter.format(date);
        return dateString;
    }


    // convert from Time to String
    public String getStringHoursFormat(Date date) {
        String dateString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("HH");
        dateString = formatter.format(date);

        return dateString;
    }

    // convert from Time to String
    public String getStringMinutesFormat(Date date) {
        String dateString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("mm");
        dateString = formatter.format(date);

        return dateString;
    }


    // convert from Date to String
    public String getStringDateFormat(Date date) {
        String dateString = "";
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        dateString = formatter.format(date);

        return dateString;
    }

    // convert from Date to String With Hours and Minute
    public String getStringDateHourMinuteFormat(Date date) {
        String dateString = "";
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        dateString = formatter.format(date);

        return dateString;
    }

    // convert from Date to String With Hours and Minute
    public String getStringDateFullFormat(Date date) {
        String dateString = "";
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        dateString = formatter.format(date);

        return dateString;
    }

    // convert from Date to String With Hours and Minute
    public String getStringDateFullFormat2(Date date) {
        String dateString = "";
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateString = formatter.format(date);

        return dateString;
    }



    public String getStringDateFormatHuman(Context context, Date date) {
        String dateString = "";
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE , dd MMM yyyy", context.getResources().getConfiguration().locale);
        dateString = formatter.format(date);
        return dateString;
    }

    // convert from Date to String
    public String getStringDateFormat2(Date date) {
        String dateString = "";
        // SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        dateString = formatter.format(date);
        return dateString;
    }


    // convert from Date to Show String
    public String getShowStringDateFormat(Date date) {
        String dateString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("d-M-yyyy");
        dateString = formatter.format(date);

        return dateString;
    }

    // convert from time String to String
    public StringBuilder getDatabaseTimeFormat(String time) {
        StringBuilder timeFormat = new StringBuilder("");
        String h_name = context.getResources().getString(R.string.h_name);
        String m_name = context.getResources().getString(R.string.m_name);
        String twenty_four_name = 24 + context.getResources().getString(R.string.h_name);
        String zero_minute_name = 0 + context.getResources().getString(R.string.h_name);

        if (time.contains(h_name)) {
            if (time.equals(twenty_four_name)) {
                timeFormat.append("24:00");
            } else if (time.contains(m_name)) {

                for (int i = 0; i <= time.length() - 1; i++) {

                    if (time.charAt(i) == h_name.charAt(0)) {
                        //increasing the counter value at each occurrence of 'B'
                        timeFormat.append(":");
                    } else if (time.charAt(i) == m_name.charAt(0)) {
                        timeFormat.append("");
                    } else if (time.charAt(i) == ' ') {
                        timeFormat.append("");
                    } else {
                        timeFormat.append(time.charAt(i));
                    }

                }

            } else {
                for (int i = 0; i <= time.length() - 1; i++) {

                    if (time.charAt(i) == h_name.charAt(0)) {
                        //increasing the counter value at each occurrence of 'B'
                        timeFormat.append(":");
                    } else if (time.charAt(i) == ' ') {
                        timeFormat.append("");
                    } else {
                        timeFormat.append(time.charAt(i));
                    }
                }
                timeFormat.append("00");
            }

        } else {
            if (time.equals(zero_minute_name)) {
                timeFormat.append("00:00");

            } else {
                timeFormat.append("00:");

                for (int i = 0; i <= time.length() - 1; i++) {

                    if (time.charAt(i) == m_name.charAt(0)) {
                        //increasing the counter value at each occurrence of 'B'
                        timeFormat.append("");
                    } else {
                        timeFormat.append(time.charAt(i));
                    }
                }
            }

        }


        return timeFormat;
    }


}
