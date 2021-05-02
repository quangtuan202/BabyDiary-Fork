package com.riagon.babydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Data.LocalBackupRestore;
import com.riagon.babydiary.Model.Activities;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Notification.AlarmReceiver;
import com.riagon.babydiary.Notification.BreastFeedLeftReceiver;
import com.riagon.babydiary.Notification.BreastFeedRightReceiver;
import com.riagon.babydiary.Notification.BreastPumpAllReceiver;
import com.riagon.babydiary.Notification.BreastPumpLeftReceiver;
import com.riagon.babydiary.Notification.BreastPumpRightReceiver;
import com.riagon.babydiary.Notification.NotificationConstant;
import com.riagon.babydiary.Notification.NotificationScheduler;
import com.riagon.babydiary.Notification.NotificationTimer;
import com.riagon.babydiary.Notification.NotificationType;
import com.riagon.babydiary.Notification.SleepReceiver;
import com.riagon.babydiary.Notification.SunBathReceiver;
import com.riagon.babydiary.Notification.TummyReceiver;
import com.riagon.babydiary.Notification.UserObject;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.DbBitmapUtility;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserDetailActivity extends AppCompatActivity {

    // private ActionBar toolbar;
    private String userName;
    private String currentUserID;
    private User currentUser;
    private DatabaseHelper db;
    private LocalDataHelper localDataHelper;
    private Bitmap userProfileImage;
    private TextView profileName;
    private SettingHelper settingHelper;
    private LinearLayout profileLayout;
    private LocalBackupRestore localBackupRestore;
    private Boolean isDarkMode;
    private FrameLayout container;

    private FormHelper formHelper;
    private DateFormatUtility dateFormatUtility;
    private NotificationScheduler notificationScheduler;
    private AdView mAdView;
    private Boolean isAd = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("Tuan User activity:", "onCreate");

        //  userName = getIntent().getStringExtra("UserName");
        // userID =  getIntent().getIntExtra("UserID",1);
        db = new DatabaseHelper(this);
        localDataHelper = new LocalDataHelper(this);
        formHelper = new FormHelper();
        dateFormatUtility = new DateFormatUtility();

        currentUserID = localDataHelper.getActiveUserId();
        // currentUser = currentUser;
        currentUser = db.getUser(currentUserID);
        isDarkMode = localDataHelper.getDarkMode();
        localDataHelper.setLanguage();

        settingHelper = new SettingHelper(this);

        settingHelper.setThemes(currentUser.getUserTheme());
        // setTheme(R.style.GreenAppTheme);
        if (localDataHelper.getPurchaseState().equals("")) {
            setContentView(R.layout.activity_user_detail_ad);
            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            mAdView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);


        } else {
            setContentView(R.layout.activity_user_detail);
        }

        localBackupRestore = new LocalBackupRestore(this);
        localDataHelper.setLanguage();

        //     toolbar = getSupportActionBar();

        container = findViewById(R.id.frame_container);


        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        // getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        profileName = view.findViewById(R.id.name);
        Button profileAvatar = view.findViewById(R.id.profile_avatar);
        profileLayout = (LinearLayout) view.findViewById(R.id.profile_image_layout);

        //localDataHelper.setLanguage();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
        if (currentUser.getUserImage() == null) {
            settingHelper.setDefaultProfileImage(this, profileAvatar, settingHelper.getFirstChar(currentUser.getUserName()), currentUser.getUserTheme());
        } else {
            userProfileImage = dbBitmapUtility.getImage(currentUser.getUserImage());
            Drawable drawableIcon = new BitmapDrawable(getResources(), dbBitmapUtility.createCircleBitmap(userProfileImage));
            profileAvatar.setBackground(drawableIcon);
        }
        profileAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });

        profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
                startActivity(intent);
            }
        });

        userName = currentUser.getUserName();
        profileName.setText(userName);

        loadFragment(new HomeFragment());

    //    setNotification();


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("User Activity:","onResume");
        Log.d("User Activity:",currentUserID);
        // Clear notifications & Unregister Broadcast receivers-----------------------------------------------------------------------------------
        List<BroadcastReceiver> broadcastReceiverListForUnregistering= UserObject.UserObjectMap.get(currentUserID).broadcastReceiverListOfUser;
        if(broadcastReceiverListForUnregistering!=null) {
            Log.d("User Activity:",broadcastReceiverListForUnregistering.toString());
            for (BroadcastReceiver broadcastReceiver : broadcastReceiverListForUnregistering) {
                try {
                    getApplicationContext().unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                    ;
                }
            }
        }
        else{
            ;
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //   toolbar.setTitle(userName);
                    profileName.setText(userName);
                    fragment = new HomeFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_log:
                    //  toolbar.setTitle("Log");
                    profileName.setText(getResources().getString(R.string.title_log));
                    fragment = new LogFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_starts:
                    // toolbar.setTitle("Starts");
                    profileName.setText(getResources().getString(R.string.title_starts));
                    fragment = new StartsFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_timeline:
                    //toolbar.setTitle("Timeline");
                    profileName.setText(getResources().getString(R.string.title_timeline));
                    fragment = new TimelineFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("Tuan user activity:", "onDestroy");


        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.breastFeedLeftTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.breastFeedRightTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.breastPumpLeftTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.breastPumpRightTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.breastPumpAllTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.sleepTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification

        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.tummyTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification

        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.sunBathTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.top_setting:

                //  Toast.makeText(ProfileDetail.this, "Setting Selected",
                //    Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, SettingActivity.class);
                startActivity(i);
                return true;

            case R.id.top_set_notification:
                Intent intentSetNotification = new Intent(this, SetNotificationActivity.class);
                startActivity(intentSetNotification);
                return true;

            case R.id.top_growth_detail:
                Intent intentGrowthDetail = new Intent(this, GrowthDetail.class);
                startActivity(intentGrowthDetail);
                return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStop() {
        super.onStop();
        autoBackUp();
        Log.d("Tuan User activity:", "onStop");
        NotificationTimer.broadcastListSizeBefore = NotificationTimer.broadcastReceiverList.size();
        Log.d("broadcastSize After:", String.valueOf(NotificationTimer.broadcastListSizeBefore));
        // Save un-saved time passed in shared preference for start-up data----------------------------------------
        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.breastFeedLeftTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
            if (timer.getValue().timePass != 0) {
                //SharedPreferences sharedPreference = getSharedPreferences(timer.getValue().userId, MODE_PRIVATE);
                SharedPreferences.Editor myEditor = timer.getValue().sharedPreference.edit();
                myEditor.putLong(timer.getValue().userId + timer.getValue().notificationType, timer.getValue().timePass);
                myEditor.apply();
                Log.d("Time  pass:", String.valueOf(timer.getValue().timePass));

            }
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.breastFeedRightTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
            if (timer.getValue().timePass != 0) {
                //SharedPreferences sharedPreference = getSharedPreferences(timer.getValue().userId, MODE_PRIVATE);
                SharedPreferences.Editor myEditor = timer.getValue().sharedPreference.edit();
                myEditor.putLong(timer.getValue().userId + timer.getValue().notificationType, timer.getValue().timePass);
                myEditor.apply();
                Log.d("Time  pass:", String.valueOf(timer.getValue().timePass));
            }
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.breastPumpLeftTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
            if (timer.getValue().timePass != 0) {
                //SharedPreferences sharedPreference = getSharedPreferences(timer.getValue().userId, MODE_PRIVATE);
                SharedPreferences.Editor myEditor = timer.getValue().sharedPreference.edit();
                myEditor.putLong(timer.getValue().userId + timer.getValue().notificationType, timer.getValue().timePass);
                myEditor.apply();
                Log.d("Time  pass:", String.valueOf(timer.getValue().timePass));
            }
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.breastPumpRightTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
            if (timer.getValue().timePass != 0) {
                //SharedPreferences sharedPreference = getSharedPreferences(timer.getValue().userId, MODE_PRIVATE);
                SharedPreferences.Editor myEditor = timer.getValue().sharedPreference.edit();
                myEditor.putLong(timer.getValue().userId + timer.getValue().notificationType, timer.getValue().timePass);
                myEditor.apply();
                Log.d("Time  pass:", String.valueOf(timer.getValue().timePass));
            }
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.breastPumpAllTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
            if (timer.getValue().timePass != 0) {
                //SharedPreferences sharedPreference = getSharedPreferences(timer.getValue().userId, MODE_PRIVATE);
                SharedPreferences.Editor myEditor = timer.getValue().sharedPreference.edit();
                myEditor.putLong(timer.getValue().userId + timer.getValue().notificationType, timer.getValue().timePass);
                myEditor.apply();
                Log.d("Time  pass:", String.valueOf(timer.getValue().timePass));
            }
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.sleepTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
            if (timer.getValue().timePass != 0) {
                //SharedPreferences sharedPreference = getSharedPreferences(timer.getValue().userId, MODE_PRIVATE);
                SharedPreferences.Editor myEditor = timer.getValue().sharedPreference.edit();
                myEditor.putLong(timer.getValue().userId + timer.getValue().notificationType, timer.getValue().timePass);
                myEditor.apply();
                Log.d("Time  pass:", String.valueOf(timer.getValue().timePass));
            }
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.tummyTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
            if (timer.getValue().timePass != 0) {
                //SharedPreferences sharedPreference = getSharedPreferences(timer.getValue().userId, MODE_PRIVATE);
                SharedPreferences.Editor myEditor = timer.getValue().sharedPreference.edit();
                myEditor.putLong(timer.getValue().userId + timer.getValue().notificationType, timer.getValue().timePass);
                myEditor.apply();
                Log.d("Time  pass:", String.valueOf(timer.getValue().timePass));
            }
        }

        for (Map.Entry<String, NotificationTimer> timer : NotificationTimer.sunBathTimerMap.entrySet()) {
            timer.getValue().notificationManager.cancel(timer.getValue().id); // cancel notification
            if (timer.getValue().timePass != 0) {
                //SharedPreferences sharedPreference = getSharedPreferences(timer.getValue().userId, MODE_PRIVATE);
                SharedPreferences.Editor myEditor = timer.getValue().sharedPreference.edit();
                myEditor.putLong(timer.getValue().userId + timer.getValue().notificationType, timer.getValue().timePass);
                myEditor.apply();
                Log.d("Time  pass:", String.valueOf(timer.getValue().timePass));
            }
        }


//-------
    }

    @Override
    public void onBackPressed() {
        Log.d("Tuan User activity:", "onBackPressed");
        finish();
    }


    public void autoBackUp() {
        String outFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + getResources().getString(R.string.app_name) + File.separator;
        localBackupRestore.performAutoBackup(db, outFileName);
    }

    //Start set notification

//    public void setNotification() {
//
//        notificationScheduler = new NotificationScheduler();
//        List<Activities> activitiesNotificationList = new ArrayList<>();
//        activitiesNotificationList.addAll(db.getAllActivitiesToShow(currentUser.getUserId()));
//        activitiesNotificationList.remove(activitiesNotificationList.size() - 1);
//
//        for (int i = 0; i < activitiesNotificationList.size(); i++) {
//            Activities activities = activitiesNotificationList.get(i);
//
//            if (!activities.getNotify()) {
//
//                notificationScheduler.cancelReminder(this, AlarmReceiver.class, activities.getFixedID());
//                Log.i("NOTI", "Cancel NotificationScheduler" + activities.getFixedID());
//
//            } else {
//
//                //NotificationScheduler.setReminder(mContext, AlarmReceiver.class, 14, 35);
//                if (activities.getNotifyOption().equals("Default")) {
//
//                    if (db.getRecordByCatCount(activities.getFixedID(), currentUser.getUserId()) > 0) {
//                        int minute = Integer.parseInt(dateFormatUtility.getStringMinutesFormat(db.getLastRecord(activities.getFixedID(), currentUser.getUserId()).getTimeEnd()));
//                        int hours = Integer.parseInt(dateFormatUtility.getStringHoursFormat(db.getLastRecord(activities.getFixedID(), currentUser.getUserId()).getTimeEnd()));
////                        notificationScheduler.setReminder(this, AlarmReceiver.class, formHelper.addThreeHours(hours), minute, activities.getActivitiesId());
////                        Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + activities.getFixedID() + " " + formHelper.addThreeHours(hours) + " " + minute);
//
//                        notificationScheduler.setReminder(this, AlarmReceiver.class, formHelper.getHoursNow(), formHelper.getMinutesNow(), activities.getActivitiesId());
//                        Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + activities.getFixedID() + " " + formHelper.getHoursNow() + " " + formHelper.getMinutesNow());
//                    }
//
//
//                } else {
//
//                    int hours = formHelper.parseHour(dateFormatUtility.getStringTimeFormat(activities.getNotifyTime()));
//                    int minute = formHelper.parseMinute(dateFormatUtility.getStringTimeFormat(activities.getNotifyTime()));
//                    long millisecond = Integer.parseInt(activities.getNotifyRepeatInDay()) * 24 * 60 * 60 * 1000 + Integer.parseInt(dateFormatUtility.getStringHoursFormat(dateFormatUtility.getTimeFormat(activities.getNotifyRepeatInTime()))) * 60 * 60 * 1000 + Integer.parseInt(dateFormatUtility.getStringMinutesFormat(dateFormatUtility.getTimeFormat(activities.getNotifyRepeatInTime()))) * 60 * 1000;
//
//                    if (activities.getNotifyOption().equals("No Repeat")) {
//                        // notificationScheduler.setCustomReminder(mContext, AlarmReceiver.class, notiDate, hours, minute, activities.getActivitiesId());
//                        notificationScheduler.setCustomReminder(this, AlarmReceiver.class, activities.getNotifyDate(), hours, minute, activities.getActivitiesId());
//                        Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + dateFormatUtility.getStringDateFormat(activities.getNotifyDate()) + " " + activities.getFixedID() + " " + hours + " " + minute);
//                    } else {
//                        notificationScheduler.setCustomRepeatReminder(this, AlarmReceiver.class, activities.getNotifyDate(), hours, minute, millisecond, activities.getActivitiesId());
//                        Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + dateFormatUtility.getStringDateFormat(activities.getNotifyDate()) + " " + activities.getFixedID() + " " + hours + " " + minute + " Repeat in " + activities.getNotifyRepeatInDay() + activities.getNotifyRepeatInTime());
//                    }
//
//
//
//
//                }
//
//
//
//
//
//            }
//
//
//        }
//
//
//    }


}
