package com.riagon.babydiary.Notification;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BreastPumpLeftReceiver extends BroadcastReceiver {
    String userId;
    String action;
    @Override
    public void onReceive(Context context, Intent intent) {
        action = intent.getAction();
        Log.d("Action:",intent.getAction());
        userId = intent.getStringExtra(NotificationConstant.EXTRA_USER_NAME);
        if (action.equals(NotificationConstant.ACTION_PAUSE+NotificationType.BREAST_PUMP_LEFT)) {
            timerPause();

            Log.d("Username:", userId);
        }
        else if(action.equals(NotificationConstant.ACTION_RESUME+NotificationType.BREAST_PUMP_LEFT)) {
            timerResume();
            Log.d("Action:", action);
        }
        else if(action.equals(NotificationConstant.ACTION_STOP+NotificationType.BREAST_PUMP_LEFT)) {
            timerStop();
            Log.d("Action:", action);
        }
        else{
            ;
        }

    }
    @SuppressLint("RestrictedApi")
    public void timerResume(){
        NotificationTimer.breastPumpLeftTimerMap.get(userId).ResumeOnNotification();
        Log.d("Test resume:", userId);

    }

    @SuppressLint("RestrictedApi")
    public void timerPause(){
        NotificationTimer.breastPumpLeftTimerMap.get(userId).pauseOnNotification();
        Log.d("Test Pause:", userId);
    }

    @SuppressLint("RestrictedApi")
    public void timerStop(){
        NotificationTimer.breastPumpLeftTimerMap.get(userId).stopOnNotification();

    }
}
