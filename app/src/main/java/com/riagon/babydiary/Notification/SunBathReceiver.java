package com.riagon.babydiary.Notification;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class SunBathReceiver extends BroadcastReceiver {

    String userId;
    String action;
    @Override
    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
        Log.d("Action:",intent.getAction());
        userId = intent.getStringExtra(NotificationConstant.EXTRA_USER_NAME);
        if (action.equals(NotificationConstant.ACTION_PAUSE+NotificationType.SUN_BATH)) {
            timerPause();

            Log.d("Username:", userId);
        }
        else if(action.equals(NotificationConstant.ACTION_RESUME+NotificationType.SUN_BATH)) {
            timerResume();
            Log.d("Action:", action);
        }
        else if(action.equals(NotificationConstant.ACTION_STOP+NotificationType.SUN_BATH)) {
            timerStop();
            Log.d("Action:", action);
        }
        else{
            ;
        }

    }
    @SuppressLint("RestrictedApi")
    public void timerResume(){
        NotificationTimer.sunBathTimerMap.get(userId).ResumeOnNotification();
        Log.d("Test resume:", userId);

    }

    @SuppressLint("RestrictedApi")
    public void timerPause(){
        NotificationTimer.sunBathTimerMap.get(userId).pauseOnNotification();
        Log.d("Test Pause:", userId);
    }

    @SuppressLint("RestrictedApi")
    public void timerStop(){
        NotificationTimer.sunBathTimerMap.get(userId).stopOnNotification();

    }
}
