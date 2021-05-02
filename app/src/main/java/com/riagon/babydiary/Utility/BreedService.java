package com.riagon.babydiary.Utility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.preference.PreferenceManager;

import com.riagon.babydiary.MainActivity;
import com.riagon.babydiary.R;

import java.util.Date;
import java.util.Locale;

import static com.riagon.babydiary.Notification.NotificationScheduler.TAG;

public class BreedService extends Service {

    private NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;
    private long totalSeconds = 10000;
    private long totalLeftSeconds;
    private long totalPassedSeconds;
    private CountDownTimer timer;

    private String durationString;
    private DateFormatUtility dateFormatUtility;
    private FormHelper formHelper;

    private Date dateStart;
    private Date timeStart;
    private Date duration;
    private Date dateEnd;
    private Date timeEnd;
    private int notifyID = 1;
    private String breedRightRunningStatus = "";
    private String breedLeftRunningStatus = "";
    public long breedEndTime;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        totalLeftSeconds = intent.getLongExtra("TimeValue", 0L);
        startCount();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        breedRightRunningStatus = prefs.getString("breedRightRunning", "");
        breedLeftRunningStatus=prefs.getString("breedLeftRunning", "");

        if (!breedRightRunningStatus.equals("Pause")&&!breedLeftRunningStatus.equals("Pause")) {
            editor.putLong("breedEndTime", breedEndTime);
            editor.apply();
        }


        //return super.onStartCommand(intent, flags, startId);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        stopCount();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
       // SharedPreferences.Editor editor = prefs.edit();
        breedRightRunningStatus = prefs.getString("breedRightRunning", "");
        breedLeftRunningStatus=prefs.getString("breedLeftRunning", "");

        if (!breedRightRunningStatus.equals("Pause")&&!breedLeftRunningStatus.equals("Pause")) {
            mNotificationManager.cancel(notifyID);
         //   editor.putLong("breedEndTime", breedEndTime);
         //   editor.apply();
        }

        super.onDestroy();

    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.e("ClearFromRecentService", "END");
        //Code here
        stopSelf();
    }

    public void startCount() {
        dateFormatUtility = new DateFormatUtility(this);
        dateFormatUtility = new DateFormatUtility(this);
        formHelper = new FormHelper(this);

        breedEndTime = System.currentTimeMillis() + totalLeftSeconds * 1000;

        timer = new CountDownTimer(totalLeftSeconds * 1000, 1000) {
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onTick(long millisUntilFinished) {

                Intent intent1local = new Intent();
                intent1local.setAction("Breed Counter");
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

                //here important
                // textView.setText(durationString);
                refreshNotifications(durationString);


                if (diffHours > 0) {
                    duration = dateFormatUtility.getTimeFormat2(durationString);
                } else {
                    duration = dateFormatUtility.getTimeFormat3(durationString);
                }

                intent1local.putExtra("BreedDuration", durationString);
                intent1local.putExtra("BreedSecondLeft", totalLeftSeconds);
                intent1local.putExtra("dateDuration", duration.getTime());

                sendBroadcast(intent1local);

            }

            public void onFinish() {
                Log.d(TAG, "Time's up!");
            }
        };


        timer.start();

    }


    public void stopCount() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void refreshNotifications(String message) {

        builder = new NotificationCompat.Builder(this, "YOUR_CHANNEL_ID");
        mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                    "YOUR_CHANNEL_NAME",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
            mNotificationManager.createNotificationChannel(channel);
        }

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        PendingIntent intent = PendingIntent.getActivity(this, notifyID,
                notificationIntent, 0);

        builder.setContentTitle(message)
                .setAutoCancel(false)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setContentIntent(intent)
                .setSmallIcon(R.drawable.ic_cvfeedwhite).build();

        mNotificationManager.notify(
                notifyID,
                builder.build());
    }


}
