package com.riagon.babydiary.Notification;

import android.content.Context;
import android.util.Log;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Activities;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.FormHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmHelper {

    private Context context;
    private NotificationScheduler notificationScheduler;
    private DatabaseHelper db;
    private DateFormatUtility dateFormatUtility;
    private FormHelper formHelper;

    public AlarmHelper(Context context,DatabaseHelper db) {
        this.context = context;
        this.db=db;
        notificationScheduler = new NotificationScheduler();
        dateFormatUtility= new DateFormatUtility(context);
        formHelper= new FormHelper(context);
    }


    public void setNotification(User currentUser) {


        List<Activities> activitiesNotificationList = new ArrayList<>();
        activitiesNotificationList.addAll(db.getAllActivitiesToShow(currentUser.getUserId()));
        activitiesNotificationList.remove(activitiesNotificationList.size() - 1);

        for (int i = 0; i < activitiesNotificationList.size(); i++) {
            Activities activities = activitiesNotificationList.get(i);

            if (!activities.getNotify()) {

                notificationScheduler.cancelReminder(context, AlarmReceiver.class, activities.getFixedID());
                Log.i("NOTI", "Cancel NotificationScheduler" + activities.getFixedID());

            } else {

                //NotificationScheduler.setReminder(mContext, AlarmReceiver.class, 14, 35);
                if (activities.getNotifyOption().equals("Default")) {

                    if (db.getRecordByCatCount(activities.getFixedID(), currentUser.getUserId()) > 0) {
                        int minute = Integer.parseInt(dateFormatUtility.getStringMinutesFormat(db.getLastRecord(activities.getFixedID(), currentUser.getUserId()).getTimeEnd()));
                        int hours = Integer.parseInt(dateFormatUtility.getStringHoursFormat(db.getLastRecord(activities.getFixedID(), currentUser.getUserId()).getTimeEnd()));
//                        notificationScheduler.setReminder(this, AlarmReceiver.class, formHelper.addThreeHours(hours), minute, activities.getActivitiesId());
//                        Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + activities.getFixedID() + " " + formHelper.addThreeHours(hours) + " " + minute);

                        notificationScheduler.setReminder(context, AlarmReceiver.class, formHelper.getHoursNow(), formHelper.getMinutesNow(), activities.getActivitiesId());
                        Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + activities.getFixedID() + " " + formHelper.getHoursNow() + " " + formHelper.getMinutesNow());
                    }


                } else {

                    int hours = formHelper.parseHour(dateFormatUtility.getStringTimeFormat(activities.getNotifyTime()));
                    int minute = formHelper.parseMinute(dateFormatUtility.getStringTimeFormat(activities.getNotifyTime()));
                    long millisecond = Integer.parseInt(activities.getNotifyRepeatInDay()) * 24 * 60 * 60 * 1000 + Integer.parseInt(dateFormatUtility.getStringHoursFormat(dateFormatUtility.getTimeFormat(activities.getNotifyRepeatInTime()))) * 60 * 60 * 1000 + Integer.parseInt(dateFormatUtility.getStringMinutesFormat(dateFormatUtility.getTimeFormat(activities.getNotifyRepeatInTime()))) * 60 * 1000;

                    if (activities.getNotifyOption().equals("No Repeat")) {
                        // notificationScheduler.setCustomReminder(mContext, AlarmReceiver.class, notiDate, hours, minute, activities.getActivitiesId());
                        notificationScheduler.setCustomReminder(context, AlarmReceiver.class, activities.getNotifyDate(), hours, minute, activities.getActivitiesId());
                        Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + dateFormatUtility.getStringDateFormat(activities.getNotifyDate()) + " " + activities.getFixedID() + " " + hours + " " + minute);
                    } else {
                        notificationScheduler.setCustomRepeatReminder(context, AlarmReceiver.class, activities.getNotifyDate(), hours, minute, millisecond, activities.getActivitiesId());
                        Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + dateFormatUtility.getStringDateFormat(activities.getNotifyDate()) + " " + activities.getFixedID() + " " + hours + " " + minute + " Repeat in " + activities.getNotifyRepeatInDay() + activities.getNotifyRepeatInTime());
                    }



                }




            }


        }


    }
}
