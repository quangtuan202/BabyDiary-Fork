package com.riagon.babydiary.Utility;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.riagon.babydiary.MainActivity;
import com.riagon.babydiary.R;

import java.util.Date;
import java.util.Locale;

public class TimeCountingHelper {
    String TAG = "TAG";
    public Context context;
    public long totalSeconds;
    public long intervalSeconds;
    public long totalLeftSeconds;
    public CountDownTimer timer;
    public Date duration;
    public String durationString;

    public Date dateStart;
    public Date timeStart;
    public Date dateEnd;
    public Date timeEnd;

    public long mTimeLeftInMillis;
    public long mEndTime;

    public long totalPassedSeconds;

    private DateFormatUtility dateFormatUtility;
    private FormHelper formHelper;
    NotificationCompat.Builder builder;
    NotificationManager mNotificationManager;


    public TimeCountingHelper(Context context, long totalSeconds, long intervalSeconds) {
        this.context = context;
        this.totalSeconds = totalSeconds;
        this.intervalSeconds = intervalSeconds;
        dateFormatUtility = new DateFormatUtility(context);
        formHelper = new FormHelper(context);

        builder = new NotificationCompat.Builder(context, "YOUR_CHANNEL_ID");
        mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void startCount(TextView textView) {

        dateStart = dateFormatUtility.getDateFormat(formHelper.getDateNow());
        timeStart = dateFormatUtility.getTimeFormat(formHelper.getTimeNow());

        mEndTime = System.currentTimeMillis() + totalLeftSeconds * 1000;

        timer = new CountDownTimer(totalLeftSeconds * 1000, intervalSeconds * 1000) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onTick(long millisUntilFinished) {
                // long totalPassedSeconds;
                ///Log.d(TAG, "Elapsed: " + (totalSeconds * 1000 - millisUntilFinished) / 1000);

                // mTimeLeftInMillis=millisUntilFinished;
                totalLeftSeconds = millisUntilFinished / 1000;
                totalPassedSeconds = totalSeconds * 1000 - millisUntilFinished;
                //   Log.d(TAG, "Elapsed: " + totalPassedSeconds);

                int diffSeconds = (int) totalPassedSeconds / (1000) % 60;
                int diffMinutes = (int) totalPassedSeconds / (60 * 1000) % 60;
                int diffHours = (int) totalPassedSeconds / (60 * 60 * 1000) % 24;
                int diffDays = (int) totalPassedSeconds / (24 * 60 * 60 * 1000);

                if (diffHours > 0) {
                    durationString = String.format(Locale.getDefault(), "%02d:%02d:%02d", diffHours, diffMinutes, diffSeconds);
                } else {
                    durationString = String.format(Locale.getDefault(), "%02d:%02d", diffMinutes, diffSeconds);
                }

                Log.d(TAG, "Elapsed: " + durationString);

                textView.setText(durationString);

                refreshNotifications(durationString);


                if (diffHours > 0) {
                    duration = dateFormatUtility.getTimeFormat2(durationString);
                } else {
                    duration = dateFormatUtility.getTimeFormat3(durationString);
                }

                String endDate = formHelper.getEndDate(formHelper.getDateWithTimeFormat(dateFormatUtility.getStringDateFormat(dateStart), dateFormatUtility.getStringTimeFormat(timeStart)), dateFormatUtility.getStringTimeFormat(duration));
                dateEnd = dateFormatUtility.getDateFormat(endDate);
                timeEnd = dateFormatUtility.getDateFormatWithHours(endDate);

            }

            public void onFinish() {
                Log.d(TAG, "Time's up!");
            }
        };


        timer.start();

    }

    public void stopCount() {

        timer.cancel();


    }

    public void stopNotification() {
        mNotificationManager.cancel(1);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshNotifications(String message) {
        int notifyID = 1;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }


        Intent notificationIntent = new Intent(context, MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(context, 1,
                notificationIntent, 0);


        Notification notification = builder.setContentTitle(message)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.ic_launcher_foreground).build();
        // int numMessages = 0;
        // Start of a loop that processes data and then notifies the user
        //  builder.setContentText(message);
        // Because the ID remains unchanged, the existing notification is
        // updated.

        mNotificationManager.notify(
                notifyID,
                builder.build());
    }


    public Date getDuration() {
        return duration;
    }

    public String getDurationString() {
        return durationString;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public long getTotalLeftSeconds() {
        return totalLeftSeconds;
    }

    public CountDownTimer getTimer() {
        return timer;
    }

    public long getmTimeLeftInMillis() {
        return mTimeLeftInMillis;
    }

    public long getmEndTime() {
        return mEndTime;
    }


}



