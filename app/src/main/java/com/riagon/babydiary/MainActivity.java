package com.riagon.babydiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Notification.BreastFeedLeftReceiver;
import com.riagon.babydiary.Notification.BreastFeedRightReceiver;
import com.riagon.babydiary.Notification.BreastPumpAllReceiver;
import com.riagon.babydiary.Notification.BreastPumpLeftReceiver;
import com.riagon.babydiary.Notification.BreastPumpRightReceiver;
import com.riagon.babydiary.Notification.NotificationConstant;
import com.riagon.babydiary.Notification.NotificationTimer;
import com.riagon.babydiary.Notification.NotificationType;
import com.riagon.babydiary.Notification.SleepReceiver;
import com.riagon.babydiary.Notification.SunBathReceiver;
import com.riagon.babydiary.Notification.TummyReceiver;
import com.riagon.babydiary.Utility.LocalDataHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements PurchasesUpdatedListener {


    private DatabaseHelper db;
    private LocalDataHelper localDataHelper;
    private BillingClient billingClient;
    public String skuLifetime = "life_time";
    public String skuMonthly = "monthly";
    public String skuYearly = "yearly";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Tuan Main activity:" ,"OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DatabaseHelper(this);
        localDataHelper = new LocalDataHelper(this);

        setupBillingClient();

        if (localDataHelper.getDarkMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        checkSetting();

//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            public void run() {
//                checkSetting();
//
//                // Actions to do after 10 seconds
//            }
//        }, 1000);

        // Tuan added-----
        List<String> userIds=db.getAllUserIds();
        //Check shared preferences for time passed value
        for(String userId: userIds){
            SharedPreferences sharedPreference = getSharedPreferences(userId, MODE_PRIVATE);
            for(NotificationType notificationType: NotificationTimer.notificationTypeList){
                long timePass=sharedPreference.getLong(userId+notificationType,0);
                Log.d("time passed from SP:", String.valueOf(timePass));
                Log.d("user name:", db.getUserName(userId));

                if(timePass!=0){
                    switch(notificationType){
                        //---------------------------------------------------------------------------------------
                        case BREAST_FEED_LEFT:
                            if(NotificationTimer.breastFeedLeftNameListForCheckingExistence.size()==0) {
                                NotificationTimer notificationTimerBreastFeedLeft = new NotificationTimer(false
                                        , userId
                                        , db.getUserName(userId)
                                        , notificationType
                                        , getApplicationContext()
                                        , R.drawable.icon_breatfeed
                                        , R.drawable.ic_cvl
                                        , R.drawable.ic_pause
                                        , R.drawable.ic_stop
                                        , BreastFeedLeftReceiver.class);
                                notificationTimerBreastFeedLeft.timePass = timePass;
                                notificationTimerBreastFeedLeft.pauseStatus = false;
                                notificationTimerBreastFeedLeft.notificationStatus = NotificationConstant.RESUME;
                                notificationTimerBreastFeedLeft.startOrResumeOnFragment();
                                NotificationTimer.breastFeedLeftTimerMap.put(userId, notificationTimerBreastFeedLeft);
                                NotificationTimer.breastFeedLeftNameListForCheckingExistence.add(userId);
                            }
                            else {
                                ;
                            }
                            break;
                        //---------------------------------------------------------------------------------------
                        case BREAST_FEED_RIGHT:
                            if(NotificationTimer.breastFeedRightNameListForCheckingExistence.size()==0) {
                            NotificationTimer notificationTimerBreastFeedRight = new NotificationTimer(false
                                    ,userId
                                    ,db.getUserName(userId)
                                    , notificationType
                                    , getApplicationContext()
                                    , R.drawable.icon_breatfeed
                                    , R.drawable.ic_cvr
                                    , R.drawable.ic_pause
                                    , R.drawable.ic_stop
                                    , BreastFeedRightReceiver.class);
                            notificationTimerBreastFeedRight.timePass=timePass;
                            notificationTimerBreastFeedRight.pauseStatus=false;
                            notificationTimerBreastFeedRight.notificationStatus= NotificationConstant.RESUME;
                            notificationTimerBreastFeedRight.startOrResumeOnFragment();
                            NotificationTimer.breastFeedRightTimerMap.put(userId,notificationTimerBreastFeedRight);
                            NotificationTimer.breastFeedRightNameListForCheckingExistence.add(userId);
                            }
                            else {
                                ;
                            }
                            break;
                        //---------------------------------------------------------------------------------------
                        case BREAST_PUMP_LEFT:
                            if(NotificationTimer.breastPumpLeftNameListForCheckingExistence.size()==0) {
                            NotificationTimer notificationTimerBreastPumpLeft = new NotificationTimer(true
                                    ,userId
                                    ,db.getUserName(userId)
                                    , notificationType
                                    , getApplicationContext()
                                    , R.drawable.icon_breatpump
                                    , R.drawable.ic_cvl
                                    , R.drawable.ic_pause
                                    , R.drawable.ic_stop
                                    , BreastPumpLeftReceiver.class);
                            notificationTimerBreastPumpLeft.timePass=timePass;
                            notificationTimerBreastPumpLeft.pauseStatus=false;
                            notificationTimerBreastPumpLeft.notificationStatus= NotificationConstant.RESUME;
                            notificationTimerBreastPumpLeft.startOrResumeOnFragment();
                            NotificationTimer.breastPumpLeftTimerMap.put(userId,notificationTimerBreastPumpLeft);
                            NotificationTimer.breastPumpLeftNameListForCheckingExistence.add(userId);
                            }
                            else {
                                ;
                            }
                            break;
                        //---------------------------------------------------------------------------------------
                        case BREAST_PUMP_RIGHT:
                            if(NotificationTimer.breastPumpRightNameListForCheckingExistence.size()==0) {
                            NotificationTimer notificationTimerBreastPumpRight = new NotificationTimer(true
                                    ,userId
                                    , db.getUserName(userId)
                                    , notificationType
                                    , getApplicationContext()
                                    , R.drawable.icon_breatpump
                                    , R.drawable.ic_cvl
                                    , R.drawable.ic_pause
                                    , R.drawable.ic_stop
                                    , BreastPumpRightReceiver.class);
                            notificationTimerBreastPumpRight.timePass=timePass;
                            notificationTimerBreastPumpRight.pauseStatus=false;
                            notificationTimerBreastPumpRight.notificationStatus= NotificationConstant.RESUME;
                            notificationTimerBreastPumpRight.startOrResumeOnFragment();
                            NotificationTimer.breastPumpRightTimerMap.put(userId,notificationTimerBreastPumpRight);
                            NotificationTimer.breastPumpRightNameListForCheckingExistence.add(userId);
                            }
                            else {
                                ;
                            }
                            break;
                        //---------------------------------------------------------------------------------------
                        case BREAST_PUMP_ALL:
                            if(NotificationTimer.breastPumpAllNameListForCheckingExistence.size()==0) {
                            NotificationTimer notificationTimerBreastPumpAll = new NotificationTimer(true
                                    ,userId
                                    , db.getUserName(userId)
                                    , notificationType
                                    , getApplicationContext()
                                    , R.drawable.icon_breatpump
                                    , R.drawable.ic_cvl
                                    , R.drawable.ic_pause
                                    , R.drawable.ic_stop
                                    , BreastPumpAllReceiver.class);
                            notificationTimerBreastPumpAll.timePass=timePass;
                            notificationTimerBreastPumpAll.pauseStatus=false;
                            notificationTimerBreastPumpAll.notificationStatus= NotificationConstant.RESUME;
                            notificationTimerBreastPumpAll.startOrResumeOnFragment();
                            NotificationTimer.breastPumpAllTimerMap.put(userId,notificationTimerBreastPumpAll);
                            NotificationTimer.breastPumpAllNameListForCheckingExistence.add(userId);
                            }
                            else {
                                ;
                            }
                            break;
                        //---------------------------------------------------------------------------------------
                        case TUMMY:
                            if(NotificationTimer.tummyNameListForCheckingExistence.size()==0) {
                            NotificationTimer notificationTimerTummy = new NotificationTimer(false
                                    ,userId
                                    ,db.getUserName(userId)
                                    , notificationType
                                    , getApplicationContext()
                                    , R.drawable.icon_tummy
                                    , R.drawable.ic_cvl
                                    , R.drawable.ic_pause
                                    , R.drawable.ic_stop
                                    , TummyReceiver.class);
                            notificationTimerTummy.timePass=timePass;
                            notificationTimerTummy.pauseStatus=false;
                            notificationTimerTummy.notificationStatus= NotificationConstant.RESUME;
                            notificationTimerTummy.startOrResumeOnFragment();
                            NotificationTimer.tummyTimerMap.put(userId,notificationTimerTummy);
                            NotificationTimer.tummyNameListForCheckingExistence.add(userId);
                            }
                            else {
                                ;
                            }
                            break;
                        //---------------------------------------------------------------------------------------
                        case SUN_BATH:
                            if(NotificationTimer.sunBathNameListForCheckingExistence.size()==0) {
                            NotificationTimer notificationTimerSunBath = new NotificationTimer(false
                                    ,userId
                                    ,db.getUserName(userId)
                                    , notificationType
                                    , getApplicationContext()
                                    , R.drawable.icon_sunbathe
                                    , R.drawable.ic_cvl
                                    , R.drawable.ic_pause
                                    , R.drawable.ic_stop
                                    , SunBathReceiver.class);
                            notificationTimerSunBath.timePass=timePass;
                            notificationTimerSunBath.pauseStatus=false;
                            notificationTimerSunBath.notificationStatus= NotificationConstant.RESUME;
                            notificationTimerSunBath.startOrResumeOnFragment();
                            NotificationTimer.sunBathTimerMap.put(userId,notificationTimerSunBath);
                            NotificationTimer.sunBathNameListForCheckingExistence.add(userId);
                            }
                            else {
                                ;
                            }
                            break;
                        //---------------------------------------------------------------------------------------
                        case SLEEP:
                            if(NotificationTimer.sleepNameListForCheckingExistence.size()==0) {
                            NotificationTimer notificationTimerSleep = new NotificationTimer(false
                                    ,userId
                                    ,db.getUserName(userId)
                                    , NotificationType.SLEEP
                                    , getApplicationContext()
                                    , R.drawable.icon_sleep
                                    , R.drawable.ic_cvl
                                    , R.drawable.ic_pause
                                    , R.drawable.ic_stop
                                    , SleepReceiver.class);
                            notificationTimerSleep.timePass=timePass;
                            notificationTimerSleep.pauseStatus=false;
                            notificationTimerSleep.notificationStatus= NotificationConstant.RESUME;
                            notificationTimerSleep.startOrResumeOnFragment();
                            NotificationTimer.sleepTimerMap.put(userId,notificationTimerSleep);
                            NotificationTimer.sleepNameListForCheckingExistence.add(userId);
                            }
                            else {
                                ;
                            }
                            break;

                    }


                }

            }
        }
        //-----
    }

    public void checkSetting() {
        if (db.getUsersCount() > 0) {
            Intent i = new Intent(this, UserDetailActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(this, WelcomeActivity.class);
            startActivity(i);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("Tuan Main activity:" ,"onResume");
        finish();
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Tuan Main activity:" ,"OnPause");

        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Tuan Main activity:" ,"onDestroy");

    }


//Start check inapp purchase or subcription


    private void setupBillingClient() {
        billingClient = BillingClient.newBuilder(this).enablePendingPurchases().setListener(this).build();
        billingClient.startConnection(new BillingClientStateListener() {

            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    // The BillingClient is setup successfully
                    checkIfPurchase();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }

    private void checkIfPurchase() {
        //  Toast.makeText(ProUpgradeActivity.this, "", Toast.LENGTH_SHORT).show();

        if (billingClient.isReady()) {
            // Toast.makeText(MainActivity.this, "billingclient ready", Toast.LENGTH_SHORT).show();
            List<Purchase> purchases=new ArrayList<>();
            purchases = billingClient.queryPurchases(BillingClient.SkuType.INAPP).getPurchasesList();
            if(purchases==null) {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }

            if (purchases.size()>0)
            {
                handleItemAlreadyPurchase(purchases);
            }

        } else {

        }
        // Toast.makeText(MainActivity.this, "billingclient not ready", Toast.LENGTH_SHORT).show();

    }


    private void handleItemAlreadyPurchase(@NonNull List<Purchase> purchases) {
        try {
            for (Purchase purchase : purchases) {

                if (purchase.getSku().equals(skuLifetime)) {
                    localDataHelper.setPurchaseState(skuLifetime);

                } else if (purchase.getSku().equals(skuMonthly)) {

                    localDataHelper.setPurchaseState(skuMonthly);

                } else if (purchase.getSku().equals(skuYearly)) {

                    localDataHelper.setPurchaseState(skuYearly);
                }


                localDataHelper.setPurchaseTime(purchase.getPurchaseTime());



            }
        } catch (NullPointerException  e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "No item purchased", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {

    }
}
