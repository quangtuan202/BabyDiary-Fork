package com.riagon.babydiary.Notification;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.CountDownTimer;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.auth.FirebaseUser;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Firebase.RecordFirebase;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.R;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.FormHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.riagon.babydiary.Notification.NotificationConstant.HOME_FRAGMENT_BUTTON_ICON_UPDATE;
import static com.riagon.babydiary.Notification.NotificationConstant.TIME_COUNTER_ACTION;
import static com.riagon.babydiary.Notification.NotificationTimeConverter.convertToTime;
import static com.riagon.babydiary.Notification.NotificationTimeConverter.convertToTimeWithTwoFormat;


public class NotificationTimer {
    public String userId;
    public String userName;
    public NotificationType notificationType;
    public Context context;
    public int notificationIcon;
    public int startButtonIcon;
    public int pauseButtonIcon;
    public int stopButtonIcon;
    public boolean pauseStatus;
    public boolean isVolumeUnit;
    public int id;
    public SharedPreferences sharedPreference;
    public CountDownTimer countDownTimer;
    public long timePass;
    public long timePassOnStopOnNotification;
    public NotificationCompat.Builder notificationBuilder;
    public Intent broadcastIntent;
    public Intent intentPause;
    public PendingIntent pendingIntentPause;
    public Intent intentResume;
    public PendingIntent pendingIntentResume;
    public Intent intentStop;
    public PendingIntent pendingIntentStop;
   // public NotificationManager notificationManager;
    public NotificationManagerCompat notificationManager;
    //public IntentFilter intentFilterResume;
    //public IntentFilter intentFilterPause;
    //public IntentFilter intentFilterStop;
    //public BroadcastReceiver intentFilterReceiver;
    public static int uniqueId = 0;
    public static FormHelper formHelper;
    public String sharedPrefKeyForStartDate;
    public String sharedPrefKeyForStartTime;
    public String notificationStatus;
    public BroadcastReceiver broadcastReceiver;
    public static List<BroadcastReceiver> broadcastReceiverList=new ArrayList<>();
    public static HashMap<String, NotificationTimer> sleepTimerMap = new HashMap<String, NotificationTimer>();
    public static List<String> sleepNameListForCheckingExistence = new ArrayList();
    public static HashMap<String, NotificationTimer> tummyTimerMap = new HashMap<String, NotificationTimer>();
    public static List<String> tummyNameListForCheckingExistence = new ArrayList();
    public static HashMap<String, NotificationTimer> sunBathTimerMap = new HashMap<String, NotificationTimer>();
    public static List<String> sunBathNameListForCheckingExistence = new ArrayList();
    public static HashMap<String, NotificationTimer> breastPumpLeftTimerMap = new HashMap<String, NotificationTimer>();
    public static List<String> breastPumpLeftNameListForCheckingExistence = new ArrayList();
    public static HashMap<String, NotificationTimer> breastPumpRightTimerMap = new HashMap<String, NotificationTimer>();
    public static List<String> breastPumpRightNameListForCheckingExistence = new ArrayList();
    public static HashMap<String, NotificationTimer> breastPumpAllTimerMap = new HashMap<String, NotificationTimer>();
    public static List<String> breastPumpAllNameListForCheckingExistence = new ArrayList();
    public static HashMap<String, NotificationTimer> breastFeedLeftTimerMap = new HashMap<String, NotificationTimer>();
    public static List<String> breastFeedLeftNameListForCheckingExistence = new ArrayList();
    public static HashMap<String, NotificationTimer> breastFeedRightTimerMap = new HashMap<String, NotificationTimer>();
    public static List<String> breastFeedRightNameListForCheckingExistence = new ArrayList();
    public static List<NotificationType> notificationTypeList = Arrays.asList(NotificationType.values());
    public static HashMap<String, Integer> notificationTypeMapForCheckingLen=new HashMap<String, Integer>();
    public static List<Integer>broadcastListSize =new ArrayList();
    public static int broadcastListSizeBefore;
    public static HashMap<String, List<BroadcastReceiver>> broadcastReceiverMap=new HashMap<String, List<BroadcastReceiver>>();
    public List<BroadcastReceiver> broadcastReceiverListOfUser=new ArrayList<>();

    public NotificationTimer(Boolean isVolumeUnit,String userId, String userName, NotificationType notificationType, Context context, int notificationIcon, int startButtonIcon, int pauseButtonIcon, int stopButtonIcon, Class notificationReceiverClass) {

        this.id = uniqueId; // Java always passes primitive variables by value
        uniqueId++; // increase uniqueId for the next notification
        this.context = context;
        //this.runningStatus=NotificationType.RUNNING;
        this.userId = userId;
        this.userName=userName;
        this.notificationType = notificationType;
        this.isVolumeUnit=isVolumeUnit;
        //Icon
        this.notificationIcon=notificationIcon;
        this.startButtonIcon = startButtonIcon;
        this.pauseButtonIcon = pauseButtonIcon;
        this.stopButtonIcon = stopButtonIcon;
        //Shared preferences
        this.sharedPreference = context.getSharedPreferences(userId, MODE_PRIVATE);
        this.sharedPrefKeyForStartDate = this.userId + this.notificationType + NotificationConstant.START_DATE;
        this.sharedPrefKeyForStartTime = this.userId + this.notificationType + NotificationConstant.START_TIME;
        this.timePass = 0;
        this.notificationStatus = NotificationConstant.STOP;

        //Intent Pause : request code must be unique if use the same receiver
        this.intentPause = new Intent(context, notificationReceiverClass);
        this.intentPause.setAction(NotificationConstant.ACTION_PAUSE+this.notificationType);
        //Log.d("Put Extra:", NotificationConstant.ACTION_PAUSE+this.notificationType);
        this.intentPause.putExtra(NotificationConstant.EXTRA_USER_NAME, userId);
        this.pendingIntentPause = PendingIntent.getBroadcast(this.context, this.id, this.intentPause, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent Resume : request code must be unique if use the same receiver
        this.intentResume = new Intent(context, notificationReceiverClass);
        this.intentResume.setAction(NotificationConstant.ACTION_RESUME+this.notificationType);
        //Log.d("Put Extra:", NotificationConstant.ACTION_RESUME+this.notificationType);
        this.intentResume.putExtra(NotificationConstant.EXTRA_USER_NAME, userId);
        this.pendingIntentResume = PendingIntent.getBroadcast(this.context, this.id + 10000, this.intentResume, PendingIntent.FLAG_UPDATE_CURRENT);

        //Intent Stop : request code must be unique if use the same receiver
        this.intentStop = new Intent(context, notificationReceiverClass);
        this.intentStop.setAction(NotificationConstant.ACTION_STOP+this.notificationType);
        Log.d("Put Extra:", NotificationConstant.ACTION_STOP+this.notificationType);
        this.intentStop.putExtra(NotificationConstant.EXTRA_USER_NAME, userId);
        this.pendingIntentStop = PendingIntent.getBroadcast(this.context, this.id + 20000, this.intentStop, PendingIntent.FLAG_UPDATE_CURRENT);

        // Create notification manager
        this.notificationManager =  NotificationManagerCompat.from(this.context);
        //Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    //this.userId+this.id,
                    //this.userId,
                    this.userId+String.valueOf(this.id),
                    "Channel" + this.userId,
                    NotificationManager.IMPORTANCE_LOW
            ); //this.userName is channelId
            notificationChannel.setDescription("This is Channel");
            //this.notificationManager = getSystemService(this.context, NotificationManager.class);
            this.notificationManager.createNotificationChannel(notificationChannel);
        }

        //Create notification builder
        this.notificationBuilder = new NotificationCompat.Builder(this.context, userId+String.valueOf(this.id));
        this.notificationBuilder.setSmallIcon(notificationIcon)
                .setContentTitle(userName)
                .addAction(pauseButtonIcon, "Pause", this.pendingIntentPause) //default button is pause, after click, change it to play icon and intentResume
                .addAction(stopButtonIcon, "Stop", this.pendingIntentStop)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1))
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);

        //Countdown Timer
        this.broadcastIntent = new Intent();
        this.countDownTimer = new CountDownTimer(NotificationConstant.TOTAL_TIME, NotificationConstant.COUNT_DOWN_INTERVAL) {

            @Override
            public void onTick(long millisUntilFinished) {
                NotificationTimer.this.timePass++;
                //NotificationTimer.this.notificationBuilder.setContentTitle("Sleeping...");
                NotificationTimer.this.notificationBuilder.setContentText(convertToTimeWithTwoFormat(NotificationTimer.this.timePass));
                NotificationTimer.this.notificationManager.notify(NotificationTimer.this.id, NotificationTimer.this.notificationBuilder.build());
                NotificationTimer.this.broadcastIntent.setAction(TIME_COUNTER_ACTION + NotificationTimer.this.userId+ NotificationTimer.this.notificationType );
                NotificationTimer.this.broadcastIntent.putExtra(TIME_COUNTER_ACTION + NotificationTimer.this.notificationType + NotificationTimer.this.userId, NotificationTimer.this.timePass);
                NotificationTimer.this.context.sendBroadcast(NotificationTimer.this.broadcastIntent);
            }

            @Override
            public void onFinish() {
                ;
            }
        };

    }

    /////-------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public void startOrResumeOnFragment() {
        // Write start date and time to shared preference
        formHelper = new FormHelper(this.context);
        if (this.timePass == 0) {
            this.sharedPreference = this.context.getSharedPreferences(this.userId, MODE_PRIVATE);
            SharedPreferences.Editor editor = this.sharedPreference.edit();
            editor.putString(this.sharedPrefKeyForStartDate, formHelper.getDateNow());
            editor.putString(this.sharedPrefKeyForStartTime, formHelper.getTimeNow());
            editor.apply();
        } else {
            ; //Do nothing
        }
        // Start timer and set action buttons
        this.countDownTimer.start();
        // Notification
        this.notificationBuilder.mActions.clear();
        this.notificationBuilder.setSmallIcon(this.notificationIcon)
                //.setOngoing(true)
                //.setContentTitle("Sleeping")
                .addAction(R.drawable.ic_pause, "Pause", this.pendingIntentPause)
                .addAction(R.drawable.ic_stop, "Stop", this.pendingIntentStop)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1)) //(0) only one action button
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setTicker("Ticker Text");
        this.notificationManager.notify(this.id, this.notificationBuilder.build());
        //Set PAUSE state to false
        this.pauseStatus = false;
        this.notificationStatus = NotificationConstant.RESUME;
    }

    /////-------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public void pauseOnFragment() {
        this.countDownTimer.cancel();
        this.notificationBuilder.mActions.clear();
        //Change action button icon
        this.notificationBuilder.setSmallIcon(this.notificationIcon)
                .setOngoing(true)
                //.setContentTitle("Sleeping")
                .addAction(R.drawable.ic_cvstart, "Pause", this.pendingIntentResume)
                .addAction(R.drawable.ic_stop, "Stop", this.pendingIntentStop)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1)) //(0) only one action button
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
        this.notificationManager.notify(this.id, this.notificationBuilder.build());
        //Set PAUSE state to false
        this.pauseStatus = true;

        this.notificationStatus = NotificationConstant.PAUSE;
    }

    /////-------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public void stopOnFragment(FirebaseUser googleUser,RecordFirebase recordFirebase , int activitiesId, String option, double amount, String amountUnit, String note, DatabaseHelper db) {
        // Create an unique ID for the record
        formHelper = new FormHelper(this.context);
        String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));

        // Write start date and time to shared preference
        this.countDownTimer.cancel();
        this.notificationStatus = NotificationConstant.STOP;
        // Store value of timePass in timePassOnStopOnNotification to set text on holder.cvDuration and save before timePass is set to 0
        if(this.timePassOnStopOnNotification==0){
            this.timePassOnStopOnNotification=this.timePass;
        }
        DateFormatUtility dateFormatUtility = new DateFormatUtility(this.context);
        Date durationSleep = new Date();
        durationSleep = dateFormatUtility.getTimeFormat2(convertToTime(this.timePassOnStopOnNotification));

        Date dateStart = dateFormatUtility.getDateFormat(this.sharedPreference.getString(this.sharedPrefKeyForStartDate, "00:00:00"));
        Date timeStart = dateFormatUtility.getTimeFormat(this.sharedPreference.getString(this.sharedPrefKeyForStartTime, "00:00:00"));

        String endDate = formHelper.getEndDate(formHelper.getDateWithTimeFormat(dateFormatUtility.getStringDateFormat(dateStart), dateFormatUtility.getStringTimeFormat(timeStart)), convertToTime(this.timePassOnStopOnNotification));
        Date dateEnd = dateFormatUtility.getDateFormat(endDate);
        Date timeEnd = dateFormatUtility.getDateFormatWithHours(endDate);
        String  recordCreatedDatetime = dateFormatUtility.getStringDateFullFormat2(dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));

        Record record;
        if(googleUser!=null){
            record = new Record(uniqueRecordID,option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, this.userId,false,"Add",googleUser.getUid(),recordCreatedDatetime);
            recordFirebase.pushSingleRecordToFirebase(record);
        }
        else {
            record = new Record(uniqueRecordID, option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, this.userId, false, "Add", null, recordCreatedDatetime);
        }

        db.addRecord(record);
        // Clear notification
        this.notificationManager.cancel(this.id);
        //Reset value counted
        this.timePass = 0;
        this.timePassOnStopOnNotification=0;
        // Set Pause status to False
        this.pauseStatus = false;

       // Set Shared preference value to 0
        SharedPreferences.Editor myEditor = this.sharedPreference.edit();
        myEditor.putLong(this.userId + this.notificationType, 0);
        myEditor.apply();
        Log.d("Tuan Broadcast list:", String.valueOf(NotificationTimer.broadcastReceiverList.size()));

    }


    /////-------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public void ResumeOnNotification() {
        this.countDownTimer.start();
        this.notificationBuilder.mActions.clear();
        //Change action button icon
        this.notificationBuilder.setSmallIcon(this.notificationIcon)
                //.setOngoing(true)
                //.setContentTitle("Sleeping")
                .addAction(R.drawable.ic_pause, "Pause", this.pendingIntentPause)
                .addAction(R.drawable.ic_stop, "Stop", this.pendingIntentStop)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1)) //(0) only one action button
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
        this.notificationManager.notify(this.id, this.notificationBuilder.build());
        //Set PAUSE state to false
        this.pauseStatus = false;
        this.notificationStatus = NotificationConstant.RESUME;
        // Send intent to home fragment to update icon
        Intent intentUpdateHomeFragmentButtonIconResume = new Intent();
        intentUpdateHomeFragmentButtonIconResume.setAction(HOME_FRAGMENT_BUTTON_ICON_UPDATE + NotificationTimer.this.userId);
        intentUpdateHomeFragmentButtonIconResume.putExtra(HOME_FRAGMENT_BUTTON_ICON_UPDATE + this.notificationType + this.userId, NotificationConstant.RESUME);
        this.context.sendBroadcast(intentUpdateHomeFragmentButtonIconResume);


    }

    /////-------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public void pauseOnNotification() {
        this.countDownTimer.cancel();
        this.notificationBuilder.mActions.clear();
        //Change action button icon
        this.notificationBuilder.setSmallIcon(this.notificationIcon)
                .setOngoing(true)
                //.setContentTitle("Sleeping")
                .addAction(R.drawable.ic_cvstart, "Pause", this.pendingIntentResume)
                .addAction(R.drawable.ic_stop, "Stop", this.pendingIntentStop)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setShowActionsInCompactView(0, 1)) //(0) only one action button
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE);
        this.notificationManager.notify(this.id, this.notificationBuilder.build());
        this.timePassOnStopOnNotification=this.timePass;
        //Set PAUSE state to false
        this.pauseStatus = true;
        this.notificationStatus = NotificationConstant.PAUSE;
        // Send intent to home fragment to update icon
        Intent intentUpdateHomeFragmentButtonIconPause = new Intent();
        intentUpdateHomeFragmentButtonIconPause.setAction(HOME_FRAGMENT_BUTTON_ICON_UPDATE + NotificationTimer.this.userId);
        intentUpdateHomeFragmentButtonIconPause.putExtra(HOME_FRAGMENT_BUTTON_ICON_UPDATE + this.notificationType + this.userId, NotificationConstant.PAUSE);
        this.context.sendBroadcast(intentUpdateHomeFragmentButtonIconPause);

    }

    /////-------------------------------------------------------------------------------------------------------------------------
    @SuppressLint("RestrictedApi")
    public void stopOnNotification() {
        this.countDownTimer.cancel();
        Intent intentUpdateHomeFragmentButtonIconStop = new Intent();
        intentUpdateHomeFragmentButtonIconStop.setAction(HOME_FRAGMENT_BUTTON_ICON_UPDATE+ this.notificationType + this.userId);
        intentUpdateHomeFragmentButtonIconStop.putExtra(HOME_FRAGMENT_BUTTON_ICON_UPDATE + this.notificationType + this.userId, NotificationConstant.STOP);
        this.context.sendBroadcast(intentUpdateHomeFragmentButtonIconStop);
        // Clear notification
        this.notificationManager.cancel(this.id);
        // Store value of timePass in timePassOnStopOnNotification to set text on holder.cvDuration
        //this.timePass is set to 0 when sent via intent, therefore, it is needed to be stored on this.timePassOnStopOnNotification
        this.timePassOnStopOnNotification=this.timePass;
        //Reset value counted
        this.timePass = 0;
        // Set Pause status to False
        this.pauseStatus = false;
        this.notificationStatus = NotificationConstant.STOP;
        Log.d("Tuan Broadcast list:", String.valueOf(NotificationTimer.broadcastReceiverList.size()));
    }
}



