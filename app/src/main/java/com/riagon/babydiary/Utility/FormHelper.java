package com.riagon.babydiary.Utility;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.riagon.babydiary.R;

import org.threeten.bp.LocalDate;
import org.threeten.bp.Period;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.temporal.ChronoUnit;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FormHelper {

    private Context context;
    private String y_name;
    private String mo_name;
    private String d_name;
    private String h_name;
    private String m_name;
    private String s_name;


    public FormHelper(Context context) {
        this.context = context;
    }

    public FormHelper() {
    }

    public int minusFive(int curentNumber) {
        int newNumber = 0;
        if (curentNumber >= 5) {
            newNumber = curentNumber - 5;
        } else {
            newNumber = 0;
        }
        return newNumber;

    }

    public double minusDotFive(double curentNumber) {
        double newNumber = 0;
        BigDecimal bd = new BigDecimal(curentNumber).setScale(1, RoundingMode.HALF_UP);
        curentNumber = bd.doubleValue();

        if (curentNumber >= 0.5) {
            newNumber = curentNumber - 0.5;
        } else {
            newNumber = 0;
        }
        return newNumber;

    }

    public int addFive(int curentNumber) {
        int newNumber = 0;
        if (curentNumber >= 0) {
            newNumber = curentNumber + 5;
        } else {
            newNumber = 0;

        }
        return newNumber;

    }

    public double addDotFive(double curentNumber) {
        double newNumber = 0;
        BigDecimal bd = new BigDecimal(curentNumber).setScale(1, RoundingMode.HALF_UP);
        curentNumber = bd.doubleValue();
        if (curentNumber >= 0) {
            newNumber = curentNumber + 0.5;
        } else {
            newNumber = 0;

        }
        return newNumber;

    }

    public double minusGrowth(double curentNumber) {
        double newNumber = 0;
        if (curentNumber >= 0.1) {
            newNumber = (curentNumber - 0.1);
        } else {
            newNumber = 0;

        }
        return newNumber;

    }

    public double addGrowth(double curentNumber) {
        double newNumber = 0;
        if (curentNumber >= 0) {
            newNumber = (curentNumber + 0.1);
        } else {
            newNumber = 0;

        }
        return newNumber;

    }

    public String minusDate(String date, int days) {
        String newDate = "";
//        if (curentNumber >= 0) {
//            newNumber = (curentNumber + 0.1);
//        } else {
//            newNumber = 0;
//
//        }
        DateFormatUtility df = new DateFormatUtility();
        Date minusFirst = df.getDateFormat2(date);
        Calendar c = Calendar.getInstance();
        c.setTime(minusFirst);
        c.add(Calendar.DATE, -days);
        minusFirst = c.getTime();
        // c.add(Calendar.DAY_OF_MONTH, 30);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        newDate = dateFormat.format(minusFirst);


        return newDate;

    }

    public String addDate(String date, int days) {
        String newDate = "";

        DateFormatUtility df = new DateFormatUtility();
        Date minusFirst = df.getDateFormat2(date);
        Calendar c = Calendar.getInstance();
        c.setTime(minusFirst);
        c.add(Calendar.DATE, days);
        minusFirst = c.getTime();
        // c.add(Calendar.DAY_OF_MONTH, 30);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        newDate = dateFormat.format(minusFirst);


        return newDate;

    }


    public String getStartDate(String endTime, String duration) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility(context);


        String startDate = "";
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        //SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

        int hours = Integer.parseInt(dateFormatUtility.getStringHoursFormat(dateFormatUtility.getTimeFormat(duration)));
        int minute = Integer.parseInt(dateFormatUtility.getStringMinutesFormat(dateFormatUtility.getTimeFormat(duration)));

        Date d1 = null;
        //  Date d2 = null;
        long hmMinisecond = hours * 60 * 60 * 1000 + minute * 60 * 1000;

        try {
            d1 = formatDate.parse(endTime);

            // d2 = formatTime.parse(duration);

            //in milliseconds
            long diff = d1.getTime() - hmMinisecond;

            Date currentDate = new Date(diff);
            startDate = dateFormatUtility.getStringDateHourMinuteFormat(currentDate);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return startDate;
    }

    public String getEndDate(String endTime, String duration) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility(context);


        String startDate = "";
        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        //SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");

        int hours = Integer.parseInt(dateFormatUtility.getStringHoursFormat(dateFormatUtility.getTimeFormat(duration)));
        int minute = Integer.parseInt(dateFormatUtility.getStringMinutesFormat(dateFormatUtility.getTimeFormat(duration)));

        Date d1 = null;
        //  Date d2 = null;
        long hmMinisecond = hours * 60 * 60 * 1000 + minute * 60 * 1000;

        try {
            d1 = formatDate.parse(endTime);

            // d2 = formatTime.parse(duration);

            //in milliseconds
            long diff = d1.getTime() + hmMinisecond;

            Date currentDate = new Date(diff);
            startDate = dateFormatUtility.getStringDateHourMinuteFormat(currentDate);


        } catch (Exception e) {
            e.printStackTrace();
        }


        return startDate;
    }

    public Boolean timeDiffCount(TextView textViewDateStart, TextView textViewTimeStart, TextView textViewDateEnd, TextView textViewTimeEnd, TextView error, TextView result) {
        String Date1 = textViewDateStart.getText().toString();
        String Time1 = textViewTimeStart.getText().toString();
        String Date2 = textViewDateEnd.getText().toString();
        String Time2 = textViewTimeEnd.getText().toString();

        String dateStart = Date1 + " " + Time1;
        String dateStop = Date2 + " " + Time2;

//HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();


            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);


            if (diff >= 0) {
                if (diffDays >= 1) {
                    //  time_result_breastpump.setVisibility(View.GONE);
                    error.setVisibility(View.VISIBLE);
                    error.setText(context.getResources().getString(R.string.time_diff_eror));
                    result.setText("24" + context.getResources().getString(R.string.h_name));
                    return false;

                } else {
                    error.setVisibility(View.GONE);
                    result.setVisibility(View.VISIBLE);
                    // result.setText(diffHours + " " + "h" + " " + diffMinutes + " " + "m");
                    result.setText(timeResultFormat(diff, diffDays, diffHours, diffMinutes));
                    return true;
                }


            } else {

                error.setVisibility(View.VISIBLE);
                error.setText(context.getResources().getString(R.string.time_diff_eror_2));
                result.setText(timeResultFormat(diff, diffDays, diffHours, diffMinutes));
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
    }

    public String getDateWithTimeFormat(String date, String time) {
        return date + " " + time;
    }


    public String timeDiffNowCount(String lastRecordTime) {
        String dateStart = lastRecordTime;
        String dateStop = getDateWithTimeFormat(getDateNow(), getTimeNow());
        String dateDiffNow = "";
//HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diff >= 0) {
                if (diffDays >= 1) {
                    dateDiffNow = context.getResources().getString(R.string.tweenty_fouth_ago);

                } else {
                    dateDiffNow = timeResultFormat(diff, diffDays, diffHours, diffMinutes) + " " + context.getResources().getString(R.string.ago);
                }


            } else {
                dateDiffNow = timeResultFormat(diff, diffDays, diffHours, diffMinutes) + " " + context.getResources().getString(R.string.ago);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateDiffNow;
    }


    public String timeDiffCount(String startTime, String lastRecordTime) {
        String dateStart = lastRecordTime;
        String dateStop = startTime;
        String dateDiffNow = "";
        d_name = context.getResources().getString(R.string.d_name);
        h_name = context.getResources().getString(R.string.h_name);
        m_name = context.getResources().getString(R.string.m_name);
        s_name = context.getResources().getString(R.string.s_name);
//HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);

            if (diff >= 0) {
                if (diffDays >= 1) {
                    // dateDiffNow = ">24 h ago";
                    dateDiffNow = diffDays + d_name + diffHours + h_name + diffMinutes + m_name;

                } else {
                    dateDiffNow = timeResultFormat(diff, diffDays, diffHours, diffMinutes);
                }


            } else {
                dateDiffNow = timeResultFormat(diff, diffDays, diffHours, diffMinutes);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateDiffNow;
    }


    public long timeDiffMilliseconds(String startTime, String lastRecordTime) {
        String dateStart = lastRecordTime;
        String dateStop = startTime;
        long millisecond = 0;
        // String dateDiffNow = "";
//HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            millisecond = d2.getTime() - d1.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return millisecond;
    }


    public String getWeekDiff(String startTime, String lastRecordTime) {
        String dateStart = startTime;
        String dateStop = lastRecordTime;
        String dateDiffNow = "";
//HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            // long diffMinutes = diff / (60 * 1000) % 60;
            //  long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000) % 7;
            long diffWeeks = diff / (7 * 24 * 60 * 60 * 1000);

            // dateDiffNow = diffWeeks + " weeks " + diffDays + " days";
            dateDiffNow = context.getResources().getString(R.string.week_name) + " " + (int) (diffWeeks + 1);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dateDiffNow;
    }

    public String getAgeByWeeks(String birthDay) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility(context);
        String ageByMonth = "";
        Date date = new Date();
        ageByMonth = getWeekDiff(birthDay, dateFormatUtility.getStringDateFormat(date));

        return ageByMonth;
    }


    public String timeResultFormat(long diff, long diffDays, long diffHours, long diffMinutes) {

        String resultFormat = "";
        d_name = context.getResources().getString(R.string.d_name);
        h_name = context.getResources().getString(R.string.h_name);
        m_name = context.getResources().getString(R.string.m_name);
        s_name = context.getResources().getString(R.string.s_name);

        if (diff >= 0) {
            if (diffHours == 0) {
                resultFormat = diffMinutes + m_name;
            } else if (diffMinutes == 0) {

                resultFormat = diffHours + h_name;
            } else {
                resultFormat = diffHours + h_name + diffMinutes + m_name;
            }
        } else {
            if (diffDays == 0) {
                if (diffHours == 0) {
                    resultFormat = diffMinutes + m_name;
                } else if (diffMinutes == 0) {
                    resultFormat = diffHours + h_name;
                } else {
                    resultFormat = diffHours + h_name + Math.abs(diffMinutes) + m_name;
                }
            } else if (diffHours == 0) {
                if (diffDays == 0) {
                    resultFormat = diffMinutes + m_name;
                } else if (diffMinutes == 0) {
                    resultFormat = diffDays + d_name;
                } else {
                    resultFormat = diffDays + d_name + Math.abs(diffMinutes) + m_name;
                }
            } else if (diffMinutes == 0) {
                if (diffDays == 0) {
                    resultFormat = diffHours + h_name;
                } else if (diffHours == 0) {
                    resultFormat = diffDays + d_name;
                } else {
                    resultFormat = diffDays + d_name + Math.abs(diffHours) + h_name;
                }
            } else {
                resultFormat = diffDays + d_name + Math.abs(diffHours) + h_name + Math.abs(diffMinutes) + m_name;
            }

        }

        return resultFormat;

    }


    public String getDurationFormat(Date time) {
        String durationFormat = "";
        int d;
        int h;
        int m;
        int s;
        d_name = context.getResources().getString(R.string.d_name);
        h_name = context.getResources().getString(R.string.h_name);
        m_name = context.getResources().getString(R.string.m_name);
        s_name = context.getResources().getString(R.string.s_name);

        SimpleDateFormat hFormatter = new SimpleDateFormat("HH");
        h = Integer.parseInt(hFormatter.format(time));

        SimpleDateFormat mFormatter = new SimpleDateFormat("mm");
        m = Integer.parseInt(mFormatter.format(time));

        SimpleDateFormat sFormatter = new SimpleDateFormat("ss");
        s = Integer.parseInt(sFormatter.format(time));

        if (h > 0) {
            if (m > 0) {
                if (s > 0) {
                    durationFormat = h + h_name + " " + m + m_name + " " + s + s_name;
                } else {
                    durationFormat = h + h_name + " " + m + m_name;
                }

            } else {
                if (s > 0) {
                    durationFormat = h + h_name + " " + s + s_name;
                } else {
                    durationFormat = h + h_name;
                }

            }

        } else {
            if (m > 0) {
                if (s > 0) {
                    durationFormat = m + m_name + " " + s + s_name;
                } else {
                    durationFormat = m + m_name;
                }

            } else {
                durationFormat = s + s_name;
            }
        }

        return durationFormat;

    }


    public String getTimeNow() {
        String time;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        time = timeFormat.format(calendar.getTime());
        return time;
    }


    public String getTimeNow2() {
        String time;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        time = timeFormat.format(calendar.getTime());
        return time;
    }

    public String getTimeSecondNow() {
        String time;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("ss");
        time = timeFormat.format(calendar.getTime());
        return time;
    }


    public int getHoursNow() {
        int time;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH");
        time = Integer.parseInt(timeFormat.format(calendar.getTime()));
        return time;
    }

    public int getMinutesNow() {
        int time;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat timeFormat = new SimpleDateFormat("mm");
        time = Integer.parseInt(timeFormat.format(calendar.getTime())) + 3;
        return time;
    }

    public int addThreeHours(int hours) {
        int addedHours = 0;

        addedHours = hours + 3;
        if (addedHours > 24) {
            addedHours = hours - 24;
        }

        return addedHours;

    }

    public int addThreeMinutes(int hours) {
        int addedMinutes = 0;

        addedMinutes = hours + 3;
        if (addedMinutes > 60) {
            addedMinutes = hours - 60;
        }

        return addedMinutes;

    }

    public String getDatetimeNow() {
        String date;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        date = dateFormat.format(calendar.getTime());

        return date;
    }


    public String getDateNow() {
        String date;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        date = dateFormat.format(calendar.getTime());

        return date;
    }

    public String getDateNow2() {
        String date;

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        date = dateFormat.format(calendar.getTime());

        return date;
    }

    public String getMonthDiff(String startDate, String endDate) {

        String monthDiff = "";

        String year_name = context.getResources().getString(R.string.year_name);
        String month_name = context.getResources().getString(R.string.month_name);
        String day_name = context.getResources().getString(R.string.day_name);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-u", Locale.ROOT);
        LocalDate dueDate = LocalDate.parse(endDate, dateFormatter);
        LocalDate startDateLocal = LocalDate.parse(startDate, dateFormatter);
        Period diff = Period.between(startDateLocal, dueDate);
        if (diff.getYears() > 0) {
            if (diff.getMonths() > 0) {
                monthDiff = diff.getYears() + " " + year_name + " " + diff.getMonths() + " " + month_name + " " + diff.getDays() + " " + day_name;
            } else {
                monthDiff = diff.getYears() + " " + year_name + " " + diff.getDays() + " " + day_name;
            }
//            monthDiff = diff.getYears()+ " "  + year_name + " " + diff.getMonths() + " " + month_name + " " + diff.getDays() + " " + day_name;
        } else {
            if (diff.getMonths() > 0) {
                monthDiff = diff.getMonths() + " " + month_name + " " + diff.getDays() + " " + day_name;
            } else {
                monthDiff = diff.getDays() + " " + day_name;
            }
        }
        //  monthDiff = months + " months " + days + " days ";

        return monthDiff;
    }


    public String getMonthDiffShort(String startDate, String endDate) {

        String monthDiff = "";
        y_name = context.getResources().getString(R.string.y_name);
        mo_name = context.getResources().getString(R.string.mo_name);
        d_name = context.getResources().getString(R.string.d_name);

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-u", Locale.ROOT);
        //  ZoneId zone = ZoneId.systemDefault();

        LocalDate dueDate = LocalDate.parse(endDate, dateFormatter);
        LocalDate startDateLocal = LocalDate.parse(startDate, dateFormatter);
        Period diff = Period.between(startDateLocal, dueDate);

        if (diff.getYears() > 0) {
            monthDiff = diff.getYears() + y_name + diff.getMonths() + mo_name + diff.getDays() + d_name;
        } else {
            if (diff.getMonths() > 0) {
                monthDiff = diff.getMonths() + mo_name + diff.getDays() + d_name;
            } else {
                monthDiff = diff.getDays() + d_name;
            }
        }
        //  monthDiff = months + " months " + days + " days ";

        return monthDiff;
    }

    public String getAgeByMonths(String birthDay) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility(context);
        String ageByMonth = "";
        Date date = new Date();
        ageByMonth = getMonthDiff(birthDay, dateFormatUtility.getStringDateFormat(date));

        return ageByMonth;
    }


    public double getMonthDiffDouble(String startDate, String endDate) {

        double monthDiff = 0;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-u", Locale.ROOT);
        LocalDate dueDate = LocalDate.parse(endDate, dateFormatter);
        LocalDate startDateLocal = LocalDate.parse(startDate, dateFormatter);
        Period diff = Period.between(startDateLocal, dueDate);

        if (diff.getYears() > 0) {
            monthDiff = (double) diff.getYears() * 12 + diff.getMonths() + (double) diff.getDays() / 30;
        } else {
            if (diff.getMonths() > 0) {
                monthDiff = diff.getMonths() + (double) diff.getDays() / 30;
            } else {
                monthDiff = (double) diff.getDays() / 30;
            }
        }

        BigDecimal bd = new BigDecimal(monthDiff).setScale(3, RoundingMode.HALF_UP);
        monthDiff = bd.doubleValue();

        return monthDiff;
    }

    public String getMonthDiffDoubleToString(float month) {

        String monthDiff = "";
        y_name = context.getResources().getString(R.string.y_name);
        mo_name = context.getResources().getString(R.string.mo_name);
        d_name = context.getResources().getString(R.string.d_name);

        int year = (int) (month / 12);
        if (year > 0) {
            int months = (int) (month - year * 12);
            double days = (month - year * 12 - months) * 30;
            if (months > 0) {
                monthDiff = year + y_name + months + mo_name + Math.round(days) + d_name;
            } else {
                monthDiff = year + y_name + Math.round(days) + d_name;
            }

        } else {
            int months = (int) (month - year * 12);
            if (months > 0) {
                double days = (month - year * 12 - months) * 30;
                //int day2=Integer.parseInt(String.valueOf(days));
                monthDiff = months + mo_name + Math.round(days) + d_name;

            } else {
                double days = (month - year * 12 - months) * 30;
                monthDiff = Math.round(days) + d_name;

            }
        }

        return monthDiff;
    }

    public double geVolumeByInit(Double volume, String volumeUnit, String settingUnit) {
        Calculator calculator = new Calculator();
        double weightConverted = 0;
        if (volumeUnit.equals(settingUnit)) {
            weightConverted = volume;
        } else {
            if (settingUnit.equals("ml")) {
                weightConverted = calculator.convertOzMl(volume);
            } else {
                weightConverted = calculator.convertMlOz(volume);
            }

        }
        return weightConverted;
    }

    public double getTemperatureByInit(Double temperature, String temperatureUnit, String settingUnit) {
        Calculator calculator = new Calculator();
        double temperatureConverted = 0;
        if (temperatureUnit.equals(settingUnit)) {
            temperatureConverted = temperature;
        } else {
            if (settingUnit.equals("c")) {
                temperatureConverted = calculator.convertFtoC(temperature);
            } else {
                temperatureConverted = calculator.convertCtoF(temperature);
            }

        }
        return temperatureConverted;
    }
    //this for stats chart section


    public int getSumDuration(List<Date> durationList) {
        int totalMillisecond = 0;

        for (int i = 0; i < durationList.size(); i++) {
            totalMillisecond = totalMillisecond + convertToMillisecond(durationList.get(i));
        }

        return totalMillisecond;

    }


    public int convertToMillisecond(Date duration) {
        int millisecond = 0;
        int h;
        int m;
        int s;
        SimpleDateFormat hFormatter = new SimpleDateFormat("HH");
        h = Integer.parseInt(hFormatter.format(duration));

        SimpleDateFormat mFormatter = new SimpleDateFormat("mm");
        m = Integer.parseInt(mFormatter.format(duration));

        SimpleDateFormat sFormatter = new SimpleDateFormat("ss");
        s = Integer.parseInt(sFormatter.format(duration));

        millisecond = h * 60 * 60 * 1000 + m * 60 * 1000 + s * 1000;

        return millisecond;
    }

//    public String convertMillisecondToMin(long millisecond) {
//        String time = "";
//        //  int d;
//        long d;
//        long h;
//        long m;
//        long s;
//
//        s = millisecond / (1000) % 60;
//        m = millisecond / (60 * 1000) % 60;
//        h = millisecond / (60 * 60 * 1000) % 24;
//        d = millisecond / (24 * 60 * 60 * 1000);
//
//        time = d + ":" + h + ":" + m + ":" + s;
//
//        return time;
//    }

    public String convertMillisecondToMinFullFormat(long millisecond) {
        String time = "";
        d_name = context.getResources().getString(R.string.d_name);
        h_name = context.getResources().getString(R.string.h_name);
        m_name = context.getResources().getString(R.string.m_name);
        s_name = context.getResources().getString(R.string.s_name);
        //  int d;
        long d;
        long h;
        long m;
        long s;

        s = millisecond / (1000) % 60;
        m = millisecond / (60 * 1000) % 60;
        h = millisecond / (60 * 60 * 1000) % 24;
        d = millisecond / (24 * 60 * 60 * 1000);

        //  time = d + ":" + h + ":" + m + ":" + s;
        if (d > 0) {
            if (h > 0) {
                if (m > 0) {
                    if (s > 0) {
                        time = d + d_name + " " + h + h_name + " " + m + m_name + " " + s + s_name;
                    } else {
                        time = d + d_name + " " + h + h_name + " " + m + m_name;
                    }

                } else {
                    if (s > 0) {
                        time = d + d_name + " " + h + h_name + " " + s + s_name;
                    } else {
                        time = d + d_name + " " + h + h_name;
                    }

                }

            } else {
                if (m > 0) {
                    if (s > 0) {
                        time = d + d_name + " " + m + m_name + " " + s + s_name;
                    } else {
                        time = d + d_name + " " + m + m_name;
                    }

                } else {
                    time = d + d_name + " " + s + s_name;
                }
            }
        } else {
            if (h > 0) {
                if (m > 0) {
                    if (s > 0) {
                        time = h + h_name + " " + m + m_name + " " + s + s_name;
                    } else {
                        time = h + h_name + " " + m + m_name;
                    }

                } else {
                    if (s > 0) {
                        time = h + h_name + " " + s + s_name;
                    } else {
                        time = h + h_name;
                    }

                }

            } else {
                if (m > 0) {
                    if (s > 0) {
                        time = m + m_name + " " + s + s_name;
                    } else {
                        time = m + m_name;
                    }

                } else {
                    time = s + s_name;
                }
            }
        }


        return time;
    }


    public double convertMillisecondToHours(long millisecond) {
        double time = 0;
        long d;
        long h;
        long m;
        long s;

        s = millisecond / (1000) % 60;
        m = millisecond / (60 * 1000) % 60;
        h = millisecond / (60 * 60 * 1000) % 24;
        d = millisecond / (24 * 60 * 60 * 1000);
        time = d * 24 + h + (double) m / 60 + (double) s / 3600;

        BigDecimal bd = new BigDecimal(time).setScale(2, RoundingMode.HALF_UP);
        time = bd.doubleValue();

        return time;
    }


    public double getSumVolume(List<Double> volumeList) {
        double totalVolume = 0;

        for (int i = 0; i < volumeList.size(); i++) {
            totalVolume = totalVolume + volumeList.get(i);
        }

        return totalVolume;

    }


    public int getDaysDiff(String startDate, String endDate) {

        int daysDiff = 0;

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.ROOT);
        LocalDate dueDate = LocalDate.parse(endDate, dateFormatter);
        LocalDate startDateLocal = LocalDate.parse(startDate, dateFormatter);

        daysDiff = (int) ChronoUnit.DAYS.between(startDateLocal, dueDate);


        return daysDiff;
    }

    //Timeline page

    public double getHoursDouble(Date time) {

        double hours = 0;

        int h;
        int m;
        int s;
        SimpleDateFormat hFormatter = new SimpleDateFormat("HH");
        h = Integer.parseInt(hFormatter.format(time));

        SimpleDateFormat mFormatter = new SimpleDateFormat("mm");
        m = Integer.parseInt(mFormatter.format(time));

        SimpleDateFormat sFormatter = new SimpleDateFormat("ss");
        s = Integer.parseInt(sFormatter.format(time));

        hours = h + (double) m / 60 + (double) s / (60 * 60);

        BigDecimal bd = new BigDecimal(hours).setScale(2, RoundingMode.HALF_UP);
        hours = bd.doubleValue();

        return hours;
    }

    public ArrayList<String> getArrayOfDate(String startDate, String endDate) {

        ArrayList<String> mDates = new ArrayList<>();
        FormHelper fh = new FormHelper(context);
        DateFormatUtility df = new DateFormatUtility(context);

        int periodDays = fh.getDaysDiff(startDate, endDate);


        for (int i = 0; i < periodDays + 2; i++) {
            Calendar cal = Calendar.getInstance();
            Date endStartDate = df.getDateFormat2(endDate);
            cal.setTime(endStartDate);
            cal.add(Calendar.DATE, -i);
            // SimpleDateFormat dateShowFormat = new SimpleDateFormat("d/M");
            SimpleDateFormat dateShowFormat = new SimpleDateFormat("yyyy-MM-dd");
            String dateShow = dateShowFormat.format(cal.getTime());
            mDates.add(dateShow);
        }

        return mDates;

    }


    public int getIndexOfDate(List<String> date, String current) {
        return date.indexOf(current);
    }

    public StringBuilder convertUID(String id) {
        StringBuilder timeFormat = new StringBuilder("");

        for (int i = 0; i <= id.length() - 1; i++) {

            if (id.charAt(i) == '-') {
                //increasing the counter value at each occurrence of 'B'
                timeFormat.append("");
            } else {
                timeFormat.append(id.charAt(i));
            }

        }

        return timeFormat;

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

