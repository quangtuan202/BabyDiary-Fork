package com.riagon.babydiary.Utility;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riagon.babydiary.ActivitiesConfigActivity;
import com.riagon.babydiary.BathActivity;
import com.riagon.babydiary.BottlePumpedActivity;
import com.riagon.babydiary.BreastfeedActivity;
import com.riagon.babydiary.CryingActivity;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.DiaperActivity;
import com.riagon.babydiary.DoctorVisitActivity;
import com.riagon.babydiary.DrinkActivity;
import com.riagon.babydiary.Firebase.RecordFirebase;
import com.riagon.babydiary.FoodActivity;
import com.riagon.babydiary.FormulaActivity;
import com.riagon.babydiary.HomeFragment;
import com.riagon.babydiary.MassageActivity;
import com.riagon.babydiary.MedicineActivity;
import com.riagon.babydiary.Model.Activities;
import com.riagon.babydiary.Model.Record;
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
import com.riagon.babydiary.Notification.UserObject;
import com.riagon.babydiary.PlayActivity;
import com.riagon.babydiary.PottyActivity;
import com.riagon.babydiary.PumpActivity;
import com.riagon.babydiary.SleepActivity;
import com.riagon.babydiary.SunbathTimeActivity;
import com.riagon.babydiary.SymptomActivity;
import com.riagon.babydiary.TemperatureActivity;
import com.riagon.babydiary.TummyTimeActivity;
import com.riagon.babydiary.VaccinationActivity;
import com.riagon.babydiary.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static com.riagon.babydiary.Notification.NotificationConstant.HOME_FRAGMENT_BUTTON_ICON_UPDATE;
import static com.riagon.babydiary.Notification.NotificationConstant.TIME_COUNTER_ACTION;
import static com.riagon.babydiary.Notification.NotificationTimeConverter.convertToTimeWithTwoFormat;
import static com.riagon.babydiary.Notification.NotificationTimer.notificationTypeMapForCheckingLen;

public class ActivitiesAdapter extends RecyclerView.Adapter<ActivitiesAdapter.MyViewHolder> {
    private List<Activities> activitiesList;
    public Context mContext;
    public DatabaseHelper db;
    public DateFormatUtility dateFormatUtility;
    public FormHelper formHelper;
    public SettingHelper settingHelper;
    public LocalDataHelper localDataHelper;
    // public Boolean isDarkMode;
    public String option;
    public double amount;
    public String amountUnit;
    public Date dateStart;
    public Date timeStart;
    public Date dateEnd;
    public Date timeEnd;
    public String recordCreatedDatetime;
    public Date durationBreed;
    public Date durationPump;
    public Date durationSleep;
    public Date durationTummy;
    public Date durationSunbath;
    public String note;
    public byte[] image;
    public int activitiesId;
    public String userID;
    public String chart;
    private String volumeUnit;
    private String temperatureUnit;
    private String whhUnit;
    public HomeFragment homeFragment;
    private ActivityHelper activityHelper;
    private String sleepRunningStatus;
    private String tummyRunningStatus;
    private String sunbathRunningStatus;
    private String breedLeftRunningStatus;
    private String breedRightRunningStatus;
    private String pumpLeftRunningStatus;
    private String pumpRightRunningStatus;
    private String pumpLeftRightRunningStatus;
    private FirebaseUser googleUser;
    private FirebaseAuth mAuth;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView activitiesName;
        public ImageView activitiesIcon, recordDetailIcon;
        public ImageView cvAdd;
        public View lineView;
        public TextView activitiesLastRecord, cvDuration, recordDetailTx;
        public Button cvFeedL, cvFeedR, cvPumpL, cvPumpR, cvPumpLR, cvSleepStart;
        public Button peeBt, pooBt, peePooBt, bathShowerBt, washHairBt, washBothBt, formular30Bt, formular60Bt, formular90Bt;
        public CardView cardView;
        public LinearLayout recordDetailLayout;
        public LinearLayout other_activity_layout;


        public MyViewHolder(View view, int viewType) {
            super(view);
            activitiesName = (TextView) view.findViewById(R.id.cvName);
            activitiesIcon = (ImageView) view.findViewById(R.id.cvIcon);
            cvAdd = (ImageView) view.findViewById(R.id.cvAdd);
            lineView = (View) view.findViewById(R.id.lineView);
            cvDuration = (TextView) view.findViewById(R.id.cvDuration);
            activitiesLastRecord = (TextView) view.findViewById(R.id.cvLastRecord);
            cardView = (CardView) view.findViewById(R.id.card_view);
            other_activity_layout = view.findViewById(R.id.other_activity_layout);
            if (viewType == 1) {
                cvFeedL = view.findViewById(R.id.cvFeedL);
                cvFeedR = view.findViewById(R.id.cvFeedR);
            } else if (viewType == 2) {
                cvPumpL = view.findViewById(R.id.cvPumpL);
                cvPumpR = view.findViewById(R.id.cvPumpR);
                cvPumpLR = view.findViewById(R.id.cvPumpLR);

            } else if (viewType == 6) {
                peeBt = view.findViewById(R.id.cvPee);
                pooBt = view.findViewById(R.id.cvPoo);
                peePooBt = view.findViewById(R.id.cvPeePoo);
            } else if (viewType == 7) {
                bathShowerBt = view.findViewById(R.id.cvBathShower);
                washHairBt = view.findViewById(R.id.cvWashHair);
                washBothBt = view.findViewById(R.id.cvWashBoth);

            }

            formular30Bt = view.findViewById(R.id.cvFormula30);
            formular60Bt = view.findViewById(R.id.cvFormula60);
            formular90Bt = view.findViewById(R.id.cvFormula90);
            cvSleepStart = view.findViewById(R.id.cvSleepStart);

        }
    }

    public ActivitiesAdapter(Context mContext, DatabaseHelper db, HomeFragment homeFragment) {
        this.mContext = mContext;
        this.db = db;
        //this.isDarkMode = isDarkMode;
        this.homeFragment = homeFragment;
        formHelper = new FormHelper(mContext);
        localDataHelper = new LocalDataHelper(mContext);
        userID = localDataHelper.getActiveUserId();
        dateFormatUtility = new DateFormatUtility(mContext);
        activityHelper = new ActivityHelper(mContext);

        activitiesList = new ArrayList<>();
        activitiesList.addAll(db.getAllActivitiesToShow(userID));
        mAuth = FirebaseAuth.getInstance();
        googleUser = mAuth.getCurrentUser();


        setInitValue();

    }

    public ActivitiesAdapter(Context mContext, List<Activities> activitiesList) {
        this.mContext = mContext;
        this.activitiesList = activitiesList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        switch (viewType) {
            case 1:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activities_card_feed, parent, false);
                break;
            case 2:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activities_card_pump, parent, false);
                break;
            case 3:
            case 4:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activities_card_formula, parent, false);
                break;
            case 6:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activities_card_diaper, parent, false);
                break;
            case 7:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activities_card_bath, parent, false);
                break;
            case 8:
            case 9:
            case 10:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activities_card_sleep, parent, false);
                break;
            case 21:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activities_card_more, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.activities_card, parent, false);
        }

        return new MyViewHolder(itemView, viewType);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Activities activities = activitiesList.get(position);
        RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
        //  FormHelper formHelper = new FormHelper();

        // Create a map of user object to store broadcast receiver of each user
        if(!UserObject.UserObjectMap.containsKey(localDataHelper.getActiveUserId())) {
            UserObject userObject = new UserObject(localDataHelper.getActiveUserId());
            UserObject.UserObjectMap.put(localDataHelper.getActiveUserId(),userObject);
        }

        if (activities.getFixedID() == 1) {
            setViewByCat(holder, 1);
            // Left
            updateCardView(mContext.getApplicationContext()
                    ,false
                    ,recordFirebase
                    ,1
                    ,"Left"
                    ,holder
                    ,NotificationTimer.breastFeedLeftTimerMap
                    ,NotificationTimer.breastFeedLeftNameListForCheckingExistence
                    ,NotificationType.BREAST_FEED_LEFT
                    , R.drawable.icon_breatfeed
                    , R.drawable.ic_cvl
                    , R.drawable.ic_pause
                    , R.drawable.ic_stop
                    , BreastFeedLeftReceiver.class
                    , holder.cvDuration
                    , holder.cvFeedL);
            // Right
            updateCardView(mContext.getApplicationContext()
                    ,false
                    ,recordFirebase
                    ,1
                    ,"Right"
                    ,holder
                    ,NotificationTimer.breastFeedRightTimerMap
                    ,NotificationTimer.breastFeedRightNameListForCheckingExistence
                    ,NotificationType.BREAST_FEED_RIGHT
                    , R.drawable.icon_breatfeed
                    , R.drawable.ic_cvr
                    , R.drawable.ic_pause
                    , R.drawable.ic_stop
                    , BreastFeedRightReceiver.class
                    , holder.cvDuration
                    , holder.cvFeedR);

            holder.cvFeedL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("broadcastSize " , String.valueOf(NotificationTimer.broadcastListSize.size()));
                    Log.d("hieu so :" , String.valueOf(NotificationTimer.broadcastListSize.size()-NotificationTimer.broadcastListSizeBefore));
                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.breastFeedLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0 || NotificationTimer.breastFeedLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseStatus==true ) {
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_stop);
                        holder.cvFeedL.setBackground(iconPlay);
                        NotificationTimer.breastFeedLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).startOrResumeOnFragment();
                    }
                    else {
                        NotificationTimer.breastFeedLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,1,"Left",amount,amountUnit,note,db);
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_cvl);
                        holder.cvFeedL.setBackground(iconPlay);
                        setViewByCat(holder, 1);
                        //Record lastRecordByCat = db.getLastRecord(1, userID);
                    }
                    // Stop right button
                    if(NotificationTimer.breastFeedRightNameListForCheckingExistence.contains(localDataHelper.getActiveUserId())){
                        if(NotificationTimer.breastFeedRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass!=0){
                            NotificationTimer.breastFeedRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,1,"Right",amount,amountUnit,note,db);
                            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvr);
                            holder.cvFeedR.setBackground(iconPlay);
                        }
                        else{
                            ;
                        }
                    }
                    else{
                        ;
                    }

                }
            });

            // Handle button long click------------------------------------------------------------------------------------------------------------------
            holder.cvFeedL.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.breastFeedLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0) {

                    } else {
                        Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
                        holder.cvFeedL.setBackground(iconPause);
                        NotificationTimer.breastFeedLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseOnFragment();
                    }

                    return true;
                }
            });

            holder.cvFeedR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("broadcastSize " , String.valueOf(NotificationTimer.broadcastListSize.size()));
                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.breastFeedRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0 || NotificationTimer.breastFeedRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseStatus==true ) {
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_stop);
                        holder.cvFeedR.setBackground(iconPlay);
                        NotificationTimer.breastFeedRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).startOrResumeOnFragment();
                    }
                    else {
                        NotificationTimer.breastFeedRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,1,"Right",amount,amountUnit,note,db);
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_cvr);
                        holder.cvFeedR.setBackground(iconPlay);
                        setViewByCat(holder, 1);
                       // Record lastRecordByCat = db.getLastRecord(1, userID);
                    }
                    // Stop left button
                    if(NotificationTimer.breastFeedLeftNameListForCheckingExistence.contains(localDataHelper.getActiveUserId())){
                        if(NotificationTimer.breastFeedLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass!=0){
                            NotificationTimer.breastFeedLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,1,"Left",amount,amountUnit,note,db);
                            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvl);
                            holder.cvFeedL.setBackground(iconPlay);
                        }
                        else{
                            ;
                        }
                    }
                    else{
                        ;
                    }

                }
            });

            // Handle button long click------------------------------------------------------------------------------------------------------------------
            holder.cvFeedR.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.breastFeedRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0) {

                    } else {
                        Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
                        holder.cvFeedR.setBackground(iconPause);
                        NotificationTimer.breastFeedRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseOnFragment();
                    }

                    return true;
                }
            });


        } else if (activities.getFixedID() == 2) {
            setViewByCat(holder, 2);
            // Left
            updateCardView(mContext.getApplicationContext()
                    ,true
                    ,recordFirebase
                    ,2
                    ,"Left"
                    ,holder
                    ,NotificationTimer.breastPumpLeftTimerMap
                    ,NotificationTimer.breastPumpLeftNameListForCheckingExistence
                    ,NotificationType.BREAST_PUMP_LEFT
                    , R.drawable.icon_breatpump
                    , R.drawable.ic_cvl
                    , R.drawable.ic_pause
                    , R.drawable.ic_stop
                    , BreastPumpLeftReceiver.class
                    , holder.cvDuration
                    , holder.cvPumpL);
            // Right
            updateCardView(mContext.getApplicationContext()
                    ,true
                    ,recordFirebase
                    ,2
                    ,"Right"
                    ,holder
                    ,NotificationTimer.breastPumpRightTimerMap
                    ,NotificationTimer.breastPumpRightNameListForCheckingExistence
                    ,NotificationType.BREAST_PUMP_RIGHT
                    , R.drawable.icon_breatpump
                    , R.drawable.ic_cvr
                    , R.drawable.ic_pause
                    , R.drawable.ic_stop
                    , BreastPumpRightReceiver.class
                    , holder.cvDuration
                    , holder.cvPumpR);
            // Both
            updateCardView(mContext.getApplicationContext()
                    ,true
                    ,recordFirebase
                    ,2
                    ,"LeftRight"
                    ,holder
                    ,NotificationTimer.breastPumpAllTimerMap
                    ,NotificationTimer.breastPumpAllNameListForCheckingExistence
                    ,NotificationType.BREAST_PUMP_ALL
                    , R.drawable.icon_breatpump
                    , R.drawable.ic_cvlr
                    , R.drawable.ic_pause
                    , R.drawable.ic_stop
                    , BreastPumpAllReceiver.class
                    , holder.cvDuration
                    , holder.cvPumpLR);

            holder.cvPumpL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.breastPumpLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0 || NotificationTimer.breastPumpLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseStatus==true ) {
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_stop);
                        holder.cvPumpL.setBackground(iconPlay);
                        NotificationTimer.breastPumpLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).startOrResumeOnFragment();
                    }
                    else {
                        NotificationTimer.breastPumpLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,2,"Left",amount,volumeUnit,note,db);
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_cvl);
                        holder.cvPumpL.setBackground(iconPlay);
                        setViewByCat(holder, 2);
                        //Record lastRecordByCat = db.getLastRecord(2, userID);
                    }
                    // Stop right button
                    if(NotificationTimer.breastPumpRightNameListForCheckingExistence.contains(localDataHelper.getActiveUserId())){
                        if(NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass!=0){
                            NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,2,"Right",amount,volumeUnit,note,db);
                            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvr);
                            holder.cvPumpR.setBackground(iconPlay);
                        }
                        else{
                            ;
                        }
                    }
                    else{
                        ;
                    }
                    // Stop L&R button
                    if(NotificationTimer.breastPumpAllNameListForCheckingExistence.contains(localDataHelper.getActiveUserId())){
                        if(NotificationTimer.breastPumpAllTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass!=0){
                            NotificationTimer.breastPumpAllTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,2,"LeftRight",amount,volumeUnit,note,db);
                            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvr);
                            holder.cvPumpLR.setBackground(iconPlay);
                        }
                        else{
                            ;
                        }
                    }
                    else{
                        ;
                    }

                }
            });

            // Handle button long click------------------------------------------------------------------------------------------------------------------
            holder.cvPumpL.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.breastPumpLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0) {

                    } else {
                        Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
                        holder.cvPumpL.setBackground(iconPause);
                        NotificationTimer.breastPumpLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseOnFragment();
                    }

                    return true;
                }
            });

            holder.cvPumpR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0 || NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseStatus==true ) {
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_stop);
                        holder.cvPumpR.setBackground(iconPlay);
                        NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).startOrResumeOnFragment();
                    }
                    else {
                        NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,2,"Right",amount,volumeUnit,note,db);
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_cvr);
                        holder.cvPumpR.setBackground(iconPlay);
                        setViewByCat(holder, 2);
                        //Record lastRecordByCat = db.getLastRecord(2, userID);
                    }
                    // Stop left button
                    if(NotificationTimer.breastPumpLeftNameListForCheckingExistence.contains(localDataHelper.getActiveUserId())){
                        if(NotificationTimer.breastPumpLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass!=0){
                            NotificationTimer.breastPumpLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,2,"Left",amount,volumeUnit,note,db);
                            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvl);
                            holder.cvPumpL.setBackground(iconPlay);
                        }
                        else{
                            ;
                        }
                    }
                    else{
                        ;
                    }
                    // Stop L&R button
                    if(NotificationTimer.breastPumpAllNameListForCheckingExistence.contains(localDataHelper.getActiveUserId())){
                        if(NotificationTimer.breastPumpAllTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass!=0){
                            NotificationTimer.breastPumpAllTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,2,"LeftRight",amount,volumeUnit,note,db);
                            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvr);
                            holder.cvPumpLR.setBackground(iconPlay);
                        }
                        else{
                            ;
                        }
                    }
                    else{
                        ;
                    }

                }
            });

            // Handle button long click------------------------------------------------------------------------------------------------------------------
            holder.cvPumpR.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0) {

                    } else {
                        Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
                        holder.cvPumpR.setBackground(iconPause);
                        NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseOnFragment();
                    }

                    return true;
                }
            });
//---------------------------------------------------------------------Both
            holder.cvPumpLR.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.breastPumpAllTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0 || NotificationTimer.breastPumpAllTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseStatus==true ) {
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_stop);
                        holder.cvPumpLR.setBackground(iconPlay);
                        NotificationTimer.breastPumpAllTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).startOrResumeOnFragment();
                    }
                    else {
                        NotificationTimer.breastPumpAllTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,2,"LeftRight",amount,volumeUnit,note,db);
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_cvlr);
                        holder.cvPumpLR.setBackground(iconPlay);
                        setViewByCat(holder, 2);
                        Record lastRecordByCat = db.getLastRecord(2, userID);
                    }
                    // Stop left button
                    if(NotificationTimer.breastPumpLeftNameListForCheckingExistence.contains(localDataHelper.getActiveUserId())){
                        if(NotificationTimer.breastPumpLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass!=0){
                            NotificationTimer.breastPumpLeftTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,2,"Left",amount,volumeUnit,note,db);
                            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvl);
                            holder.cvPumpL.setBackground(iconPlay);
                        }
                        else{
                            ;
                        }
                    }
                    else{
                        ;
                    }
                    // Stop right button
                    if(NotificationTimer.breastPumpRightNameListForCheckingExistence.contains(localDataHelper.getActiveUserId())){
                        if(NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass!=0){
                            NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,2,"Right",amount,volumeUnit,note,db);
                            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvr);
                            holder.cvPumpR.setBackground(iconPlay);
                        }
                        else{
                            ;
                        }
                    }
                    else{
                        ;
                    }

                }
            });

            // Handle button long click------------------------------------------------------------------------------------------------------------------
            holder.cvPumpLR.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0) {

                    } else {
                        Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
                        holder.cvPumpR.setBackground(iconPause);
                        NotificationTimer.breastPumpRightTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseOnFragment();
                    }

                    return true;
                }
            });




        } else if (activities.getFixedID() == 3) {
            setViewByCat(holder, 3);
            setFormularButton(holder);

        } else if (activities.getFixedID() == 4) {
            setViewByCat(holder, 4);
            setPumpBottleButton(holder);

        } else if (activities.getFixedID() == 5) {
            setViewByCat(holder, 5);
        } else if (activities.getFixedID() == 6) {

            setViewByCat(holder, 6);

            Date dateEnd = dateFormatUtility.getDateFormat(formHelper.getDateNow());
            Date timeEnd = dateFormatUtility.getTimeFormat2(formHelper.getTimeNow2());
            Date dateStart = null;
            Date timeStart = null;
            Date duration = null;
            int activitiesId = 6;
            recordCreatedDatetime = dateFormatUtility.getStringDateFullFormat2(dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));

            holder.peeBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    option = "Wet";
                    //Record record = new Record(option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userID);
                    //int id = (int) db.addRecord(record);
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 6);
                }
            });

            holder.pooBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    option = "Dirty";
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 6);

                }
            });


            holder.peePooBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    option = "Mixed";
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 6);

                }
            });


        } else if (activities.getFixedID() == 7) {
            setViewByCat(holder, 7);

            Date dateEnd = dateFormatUtility.getDateFormat(formHelper.getDateNow());
            Date timeEnd = dateFormatUtility.getTimeFormat2(formHelper.getTimeNow2());
            Date dateStart = null;
            Date timeStart = null;
            Date duration = null;
            int activitiesId = 7;
            recordCreatedDatetime = dateFormatUtility.getStringDateFullFormat2(dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));

            holder.bathShowerBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    option = "Bath";
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 7);
                }
            });

            holder.washHairBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    option = "Hair Wash";
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 7);

                }
            });


            holder.washBothBt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    option = "Both";
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 7);

                }
            });


        } else if (activities.getFixedID() == 8) {

            setViewByCat(holder, 8);
            updateCardView(mContext.getApplicationContext()
                    ,false
                    ,recordFirebase
                    ,8
                    ,""
                    ,holder
                    ,NotificationTimer.sleepTimerMap
                    ,NotificationTimer.sleepNameListForCheckingExistence
                    ,NotificationType.SLEEP
                    , R.drawable.icon_sleep
                    , R.drawable.ic_cvstart
                    , R.drawable.ic_pause
                    , R.drawable.ic_stop
                    , SleepReceiver.class
                    , holder.cvDuration
                    , holder.cvSleepStart);

            holder.cvSleepStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.sleepTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0 || NotificationTimer.sleepTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseStatus==true ) {
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_stop);
                        holder.cvSleepStart.setBackground(iconPlay);
                        NotificationTimer.sleepTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).startOrResumeOnFragment();
                    }
                    else {
                        NotificationTimer.sleepTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,8,"",amount,amountUnit,note,db);
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_cvstart);
                        holder.cvSleepStart.setBackground(iconPlay);
                        setViewByCat(holder, 8);
                        Record lastRecordByCat = db.getLastRecord(8, userID);
                        Log.d("Last record",formHelper.getDurationFormat(lastRecordByCat.getDuration()));


                    }
                }
            });

            // Handle button long click------------------------------------------------------------------------------------------------------------------
            holder.cvSleepStart.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.sleepTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0) {

                    } else {
                        Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
                        holder.cvSleepStart.setBackground(iconPause);
                        NotificationTimer.sleepTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseOnFragment();
                    }

                    return true;
                }
            });

        } else if (activities.getFixedID() == 9) {
            //  setViewByCat(holder, 9);

            setViewByCat(holder, 9);
            updateCardView(mContext.getApplicationContext()
                    ,false
                    ,recordFirebase
                    ,9
                    ,""
                    ,holder
                    ,NotificationTimer.tummyTimerMap
                    ,NotificationTimer.tummyNameListForCheckingExistence
                    ,NotificationType.TUMMY
                    , R.drawable.icon_tummy
                    , R.drawable.ic_cvstart
                    , R.drawable.ic_pause
                    , R.drawable.ic_stop
                    , TummyReceiver.class
                    , holder.cvDuration
                    , holder.cvSleepStart);

            holder.cvSleepStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.tummyTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0 || NotificationTimer.tummyTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseStatus==true ) {
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_stop);
                        holder.cvSleepStart.setBackground(iconPlay);
                        NotificationTimer.tummyTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).startOrResumeOnFragment();
                    }
                    else {
                        NotificationTimer.tummyTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,9,"",amount,amountUnit,note,db);
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_cvstart);
                        holder.cvSleepStart.setBackground(iconPlay);
                        setViewByCat(holder, 9);
                        Record lastRecordByCat = db.getLastRecord(9, userID);
                        Log.d("Last record",formHelper.getDurationFormat(lastRecordByCat.getDuration()));

                    }
                }
            });

            // Handle button long click------------------------------------------------------------------------------------------------------------------
            holder.cvSleepStart.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.tummyTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0) {

                    } else {
                        Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
                        holder.cvSleepStart.setBackground(iconPause);
                        NotificationTimer.tummyTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseOnFragment();
                    }

                    return true;
                }
            });



        } else if (activities.getFixedID() == 10) {

            setViewByCat(holder, 10);
            updateCardView(mContext.getApplicationContext()
                    ,false
                    ,recordFirebase
                    ,10
                    ,""
                    ,holder
                    ,NotificationTimer.sunBathTimerMap
                    ,NotificationTimer.sunBathNameListForCheckingExistence
                    ,NotificationType.SUN_BATH
                    , R.drawable.icon_sunbathe
                    , R.drawable.ic_cvstart
                    , R.drawable.ic_pause
                    , R.drawable.ic_stop
                    , SunBathReceiver.class
                    , holder.cvDuration
                    , holder.cvSleepStart);

            holder.cvSleepStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.sunBathTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0 || NotificationTimer.sunBathTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseStatus==true ) {
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_stop);
                        holder.cvSleepStart.setBackground(iconPlay);
                        NotificationTimer.sunBathTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).startOrResumeOnFragment();
                    }
                    else {
                        NotificationTimer.sunBathTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser,recordFirebase,10,"",amount,amountUnit,note,db);
                        Drawable iconPlay = res.getDrawable(R.drawable.ic_cvstart);
                        holder.cvSleepStart.setBackground(iconPlay);
                        setViewByCat(holder, 10);
                        Record lastRecordByCat = db.getLastRecord(10, userID);
                        Log.d("Last record",formHelper.getDurationFormat(lastRecordByCat.getDuration()));

                    }
                }
            });

            // Handle button long click------------------------------------------------------------------------------------------------------------------
            holder.cvSleepStart.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    Resources res = holder.itemView.getContext().getResources();
                    if (NotificationTimer.sunBathTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass==0) {

                    } else {
                        Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
                        holder.cvSleepStart.setBackground(iconPause);
                        NotificationTimer.sunBathTimerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).pauseOnFragment();
                    }

                    return true;
                }
            });


        } else if (activities.getFixedID() == 11) {
            setViewByCat(holder, 11);
        } else if (activities.getFixedID() == 12) {
            setViewByCat(holder, 12);
        } else if (activities.getFixedID() == 13) {
            setViewByCat(holder, 13);
        } else if (activities.getFixedID() == 14) {
            setViewByCat(holder, 14);
        } else if (activities.getFixedID() == 15) {
            setViewByCat(holder, 15);
        } else if (activities.getFixedID() == 16) {
            setViewByCat(holder, 16);
        } else if (activities.getFixedID() == 17) {
            setViewByCat(holder, 17);
        } else if (activities.getFixedID() == 18) {
            setViewByCat(holder, 18);
        } else if (activities.getFixedID() == 19) {
            setViewByCat(holder, 19);
        } else if (activities.getFixedID() == 20) {
            setViewByCat(holder, 20);
        } else if (activities.getFixedID() == 21) {

        }

        //this place is top that only activity 20 is different
        if (activities.getFixedID() == 21) {
            holder.other_activity_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ActivitiesConfigActivity.class);
                    mContext.startActivity(intent);
                }
            });
        } else {

            holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, activityHelper.getTint(activities.getFixedID())));
            holder.activitiesName.setText(activityHelper.getActivityName(activities.getFixedID()));
            holder.activitiesIcon.setImageResource(activityHelper.getIcon(activities.getFixedID()));
        }


        //this is place every category is same
        holder.cvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // launchActivity(activities.getActivitiesId());
                launchActivity(activities.getFixedID());
            }
        });
//-------------------------------------------------------------------------------------------------------------------------------------
        /*NotificationTimer.broadcastListSizeAfter=NotificationTimer.broadcastReceiverList.size();
        Log.d("broadcastSize before:" , String.valueOf(NotificationTimer.broadcastListSizeAfter));
        NotificationTimer.broadcastListSize.add(NotificationTimer.broadcastListSizeAfter);
        for(Integer i: NotificationTimer.broadcastListSize){
            Log.d("broadcastSize i" , String.valueOf(i));
        }
        Log.d("broadcastSize " , String.valueOf(NotificationTimer.broadcastListSize.size()));*/

    }

   /* private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }*/


    private void setFormularButton(MyViewHolder holder) {
        Resources res = holder.itemView.getContext().getResources();
        Date dateEnd = dateFormatUtility.getDateFormat(formHelper.getDateNow());
        Date timeEnd = dateFormatUtility.getTimeFormat2(formHelper.getTimeNow2());
        String option = "";
        Date dateStart = null;
        Date timeStart = null;
        Date duration = null;
        int activitiesId = 3;
        recordCreatedDatetime = dateFormatUtility.getStringDateFullFormat2(dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));
        if (volumeUnit.equals("ml")) {

            Drawable icon30 = res.getDrawable(R.drawable.ic_cvformula30_selector);
            Drawable icon60 = res.getDrawable(R.drawable.ic_cvformula60_selector);
            Drawable icon90 = res.getDrawable(R.drawable.ic_cvformula90_selector);

            holder.formular30Bt.setBackground(icon30);
            holder.formular60Bt.setBackground(icon60);
            holder.formular90Bt.setBackground(icon90);
            holder.formular30Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 30;
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 3);
                }
            });

            holder.formular60Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 60;
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 3);
                }
            });


            holder.formular90Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 90;
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 3);

                }
            });


        } else {

            Drawable icon30 = res.getDrawable(R.drawable.ic_cvformula1_selector);
            Drawable icon60 = res.getDrawable(R.drawable.ic_cvformula2_selector);
            Drawable icon90 = res.getDrawable(R.drawable.ic_cvformula3_selector);

            holder.formular30Bt.setBackground(icon30);
            holder.formular60Bt.setBackground(icon60);
            holder.formular90Bt.setBackground(icon90);
            holder.formular30Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 1;
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 3);
                }
            });

            holder.formular60Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 2;
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 3);

                }
            });


            holder.formular90Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 3;
                    /*Record record = new Record(option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userID);
                    int id = (int) db.addRecord(record);
                    setViewByCat(holder, 3);*/
                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 3);

                }
            });


        }
    }

    private void setPumpBottleButton(MyViewHolder holder) {
        Resources res = holder.itemView.getContext().getResources();
        Date dateEnd = dateFormatUtility.getDateFormat(formHelper.getDateNow());
        Date timeEnd = dateFormatUtility.getTimeFormat2(formHelper.getTimeNow2());
        String option = "";
        Date dateStart = null;
        Date timeStart = null;
        Date duration = null;
        int activitiesId = 4;
        recordCreatedDatetime = dateFormatUtility.getStringDateFullFormat2(dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));
        if (volumeUnit.equals("ml")) {

            Drawable icon30 = res.getDrawable(R.drawable.ic_cvformula30_selector);
            Drawable icon60 = res.getDrawable(R.drawable.ic_cvformula60_selector);
            Drawable icon90 = res.getDrawable(R.drawable.ic_cvformula90_selector);

            holder.formular30Bt.setBackground(icon30);
            holder.formular60Bt.setBackground(icon60);
            holder.formular90Bt.setBackground(icon90);
            holder.formular30Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 30;
                    /*Record record = new Record(option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userID);
                    int id = (int) db.addRecord(record);*/

                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 4);
                }
            });

            holder.formular60Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 60;
                   /* Record record = new Record(option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userID);
                    int id = (int) db.addRecord(record);*/

                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 4);

                }
            });


            holder.formular90Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 90;
                    /*Record record = new Record(option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userID);
                    int id = (int) db.addRecord(record);*/

                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 4);

                }
            });


        } else {

            Drawable icon30 = res.getDrawable(R.drawable.ic_cvformula1_selector);
            Drawable icon60 = res.getDrawable(R.drawable.ic_cvformula2_selector);
            Drawable icon90 = res.getDrawable(R.drawable.ic_cvformula3_selector);

            holder.formular30Bt.setBackground(icon30);
            holder.formular60Bt.setBackground(icon60);
            holder.formular90Bt.setBackground(icon90);
            holder.formular30Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 1;

                    /*Record record = new Record(option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userID);
                    int id = (int) db.addRecord(record);*/

                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 4);
                }
            });

            holder.formular60Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 2;
                    /*Record record = new Record(option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userID);
                    int id = (int) db.addRecord(record);*/

                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 4);

                }
            });


            holder.formular90Bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    amount = 3;
                    /*Record record = new Record(option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userID);
                    int id = (int) db.addRecord(record);*/

                    String uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
                    RecordFirebase recordFirebase = new RecordFirebase(mContext, googleUser);
                    Record record;
                    if(googleUser!=null){
                        record = new Record(uniqueRecordID,option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(),false,"Add",googleUser.getUid(),recordCreatedDatetime);
                        recordFirebase.pushSingleRecordToFirebase(record);
                    }
                    else {
                        record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, durationSleep, note, activitiesId, localDataHelper.getActiveUserId(), false, "Add", null, recordCreatedDatetime);
                    }
                    db.addRecord(record);
                    setViewByCat(holder, 4);

                }
            });


        }
    }




   /* public void updateButtonLeft(MyViewHolder holder) {

        Resources res = holder.itemView.getContext().getResources();

        if (breedLeftRunningStatus.equals("Stop")) {

            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvl);
            holder.cvFeedL.setBackground(iconPlay);

        } else if (breedLeftRunningStatus.equals("Pause")) {
            Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
            holder.cvFeedL.setBackground(iconPause);

        } else {
            // Toast.makeText(getApplication(),"Before Click: "+leftisrunning,Toast.LENGTH_SHORT).show();
            Drawable iconPause = res.getDrawable(R.drawable.ic_stop);
            holder.cvFeedL.setBackground(iconPause);

        }

    }

    public void updateButtonRight(MyViewHolder holder) {

        Resources res = holder.itemView.getContext().getResources();

        if (breedRightRunningStatus.equals("Stop")) {

            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvr);
            holder.cvFeedR.setBackground(iconPlay);

        } else if (breedRightRunningStatus.equals("Pause")) {
            Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
            holder.cvFeedR.setBackground(iconPause);

        } else {
            // Toast.makeText(getApplication(),"Before Click: "+leftisrunning,Toast.LENGTH_SHORT).show();
            Drawable iconPause = res.getDrawable(R.drawable.ic_stop);
            holder.cvFeedR.setBackground(iconPause);

        }

    }

    public void updateButtonPumpLeft(MyViewHolder holder) {

        Resources res = holder.itemView.getContext().getResources();

        if (pumpLeftRunningStatus.equals("Stop")) {

            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvl);
            holder.cvPumpL.setBackground(iconPlay);

        } else if (pumpLeftRunningStatus.equals("Pause")) {
            Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
            holder.cvPumpL.setBackground(iconPause);

        } else {
            // Toast.makeText(getApplication(),"Before Click: "+leftisrunning,Toast.LENGTH_SHORT).show();
            Drawable iconPause = res.getDrawable(R.drawable.ic_stop);
            holder.cvPumpL.setBackground(iconPause);

        }

    }


    public void updateButtonPumpRight(MyViewHolder holder) {

        Resources res = holder.itemView.getContext().getResources();

        if (pumpRightRunningStatus.equals("Stop")) {

            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvr);
            holder.cvPumpR.setBackground(iconPlay);

        } else if (pumpRightRunningStatus.equals("Pause")) {
            Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
            holder.cvPumpR.setBackground(iconPause);

        } else {
            // Toast.makeText(getApplication(),"Before Click: "+leftisrunning,Toast.LENGTH_SHORT).show();
            Drawable iconPause = res.getDrawable(R.drawable.ic_stop);
            holder.cvPumpR.setBackground(iconPause);

        }

    }


    public void updateButtonPumpLeftRight(MyViewHolder holder) {

        Resources res = holder.itemView.getContext().getResources();

        if (pumpLeftRightRunningStatus.equals("Stop")) {

            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvlr);
            holder.cvPumpLR.setBackground(iconPlay);

        } else if (pumpLeftRightRunningStatus.equals("Pause")) {
            Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
            holder.cvPumpLR.setBackground(iconPause);

        } else {
            // Toast.makeText(getApplication(),"Before Click: "+leftisrunning,Toast.LENGTH_SHORT).show();
            Drawable iconPause = res.getDrawable(R.drawable.ic_stop);
            holder.cvPumpLR.setBackground(iconPause);

        }

    }


    public void updateSleepButton(MyViewHolder holder) {

        Resources res = holder.itemView.getContext().getResources();

        if (sleepRunningStatus.equals("Stop")) {

            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvstart);
            holder.cvSleepStart.setBackground(iconPlay);

        } else if (sleepRunningStatus.equals("Pause")) {
            Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
            holder.cvSleepStart.setBackground(iconPause);

        } else {
            // Toast.makeText(getApplication(),"Before Click: "+leftisrunning,Toast.LENGTH_SHORT).show();
            Drawable iconPause = res.getDrawable(R.drawable.ic_stop);
            holder.cvSleepStart.setBackground(iconPause);

        }

    }

    public void updateTummyButton(MyViewHolder holder) {

        Resources res = holder.itemView.getContext().getResources();

        if (tummyRunningStatus.equals("Stop")) {

            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvstart);
            holder.cvSleepStart.setBackground(iconPlay);

        } else if (tummyRunningStatus.equals("Pause")) {
            Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
            holder.cvSleepStart.setBackground(iconPause);

        } else {
            // Toast.makeText(getApplication(),"Before Click: "+leftisrunning,Toast.LENGTH_SHORT).show();
            Drawable iconPause = res.getDrawable(R.drawable.ic_stop);
            holder.cvSleepStart.setBackground(iconPause);

        }

    }

    public void updateSunbathButton(MyViewHolder holder) {

        Resources res = holder.itemView.getContext().getResources();

        if (sunbathRunningStatus.equals("Stop")) {

            Drawable iconPlay = res.getDrawable(R.drawable.ic_cvstart);
            holder.cvSleepStart.setBackground(iconPlay);

        } else if (sunbathRunningStatus.equals("Pause")) {
            Drawable iconPause = res.getDrawable(R.drawable.ic_pause);
            holder.cvSleepStart.setBackground(iconPause);

        } else {
            // Toast.makeText(getApplication(),"Before Click: "+leftisrunning,Toast.LENGTH_SHORT).show();
            Drawable iconPause = res.getDrawable(R.drawable.ic_stop);
            holder.cvSleepStart.setBackground(iconPause);

        }

    }*/

    public void setViewByCat(MyViewHolder holder, int cat) {
        Record lastRecordByCat = null;
        final DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        FormHelper formHelper = new FormHelper(mContext);
        SettingHelper settingHelper = new SettingHelper(mContext);

        if (db.getRecordByCatCount(cat, userID) > 0) {
            holder.activitiesLastRecord.setVisibility(View.VISIBLE);

            if (cat == 1) {
                lastRecordByCat = db.getLastRecord(cat, userID);

                // settingHelper.getFirstChar(lastRecordByCat.getOption());

                holder.cvDuration.setText(settingHelper.getFirstChar(lastRecordByCat.getOption()) + "  " + formHelper.getDurationFormat(lastRecordByCat.getDuration()));
//                holder.recordDetailLayout.setVisibility(View.GONE);
                //String datetime = formHelper.getDateWithTimeFormat(dateFormatUtility.getStringDateFormat(lastRecordByCat.getDateEnd()), dateFormatUtility.getStringTimeFormat(lastRecordByCat.getTimeEnd()));
                //holder.activitiesLastRecord.setText(formHelper.timeDiffNowCount(datetime));
            } else if (cat == 2) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                //  holder.cvDuration.setVisibility(View.GONE);


                String amount;
                if (volumeUnit.equals("ml")) {
                    Log.d("VolumeUnit:",volumeUnit);

                    amount = String.valueOf((int) formHelper.geVolumeByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), volumeUnit));
                } else {
                    amount = String.valueOf(formHelper.geVolumeByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), volumeUnit));
                }
                holder.cvDuration.setText(settingHelper.getFirstChar(lastRecordByCat.getOption()) + "  " + amount + volumeUnit);
                //holder.recordDetailTx.setText(amount + volumeUnit);
            } else if (cat == 3) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                // holder.cvDuration.setVisibility(View.GONE);
                // holder.cvDuration.setText(lastRecordByCat.getOption() + ":");
                String amount;
                if (volumeUnit.equals("ml")) {
                    amount = String.valueOf((int) formHelper.geVolumeByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), volumeUnit));
                } else {
                    amount = String.valueOf(formHelper.geVolumeByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), volumeUnit));
                }
                holder.cvDuration.setText(amount + volumeUnit);
            } else if (cat == 4) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                // holder.cvDuration.setVisibility(View.GONE);
                // holder.cvDuration.setText(lastRecordByCat.getOption() + ":");
                String amount;
                if (volumeUnit.equals("ml")) {
                    amount = String.valueOf((int) formHelper.geVolumeByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), volumeUnit));
                } else {
                    amount = String.valueOf(formHelper.geVolumeByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), volumeUnit));
                }
                holder.cvDuration.setText(amount + volumeUnit);
            } else if (cat == 5) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                // holder.cvDuration.setVisibility(View.GONE);

                String amount;
                if (volumeUnit.equals("ml")) {
                    amount = String.valueOf((int) formHelper.geVolumeByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), volumeUnit));
                } else {
                    amount = String.valueOf(formHelper.geVolumeByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), volumeUnit));
                }
                // holder.recordDetailTx.setText(amount + volumeUnit);
                String optionString = activityHelper.getOptionLocale(lastRecordByCat.getOption());

                if (optionString.length() > 5) {

                    optionString = optionString.substring(0, 5) + "...";

                    holder.cvDuration.setText(optionString + amount + volumeUnit);
                } else {
                    holder.cvDuration.setText(lastRecordByCat.getOption() + "  " + amount + volumeUnit);
                }

            } else if (cat == 6) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                // holder.recordDetailTx.setVisibility(View.GONE);
                //  holder.recordDetailLayout.setVisibility(View.GONE);
                holder.cvDuration.setText(activityHelper.getOptionLocale(lastRecordByCat.getOption()));

            } else if (cat == 7) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                //  holder.recordDetailTx.setVisibility(View.GONE);
                // holder.recordDetailLayout.setVisibility(View.GONE);
                holder.cvDuration.setText(activityHelper.getOptionLocale(lastRecordByCat.getOption()));

            } else if (cat == 8) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                holder.cvDuration.setText(formHelper.getDurationFormat(lastRecordByCat.getDuration()));
                //  holder.recordDetailLayout.setVisibility(View.GONE);
            } else if (cat == 9) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                holder.cvDuration.setText(formHelper.getDurationFormat(lastRecordByCat.getDuration()));
                //  holder.recordDetailLayout.setVisibility(View.GONE);
            } else if (cat == 10) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                holder.cvDuration.setText(formHelper.getDurationFormat(lastRecordByCat.getDuration()));
                //  holder.recordDetailLayout.setVisibility(View.GONE);
            } else if (cat == 11) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                holder.cvDuration.setText(formHelper.getDurationFormat(lastRecordByCat.getDuration()));
                // holder.recordDetailLayout.setVisibility(View.GONE);
            } else if (cat == 12) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                holder.cvDuration.setText(formHelper.getDurationFormat(lastRecordByCat.getDuration()));
                //  holder.recordDetailLayout.setVisibility(View.GONE);
            } else if (cat == 13) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                // holder.cvDuration.setVisibility(View.GONE);

                String amount;
                if (volumeUnit.equals("ml")) {
                    amount = String.valueOf((int) formHelper.geVolumeByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), volumeUnit));
                } else {
                    amount = String.valueOf(formHelper.geVolumeByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), volumeUnit));
                }

                String optionString = activityHelper.getOptionLocale(lastRecordByCat.getOption());

                if (optionString.length() > 5) {

                    optionString = optionString.substring(0, 5) + "...";

                    holder.cvDuration.setText(optionString + amount + volumeUnit);
                } else {
                    holder.cvDuration.setText(optionString + "  " + amount + volumeUnit);
                }
                // holder.cvDuration.setText(lastRecordByCat.getOption() + "  "+amount + volumeUnit);
                // holder.recordDetailTx.setText(amount + volumeUnit);
            } else if (cat == 14) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                holder.cvDuration.setText(formHelper.getDurationFormat(lastRecordByCat.getDuration()));
                //  holder.recordDetailLayout.setVisibility(View.GONE);
            } else if (cat == 15) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                // holder.cvDuration.setVisibility(View.GONE);
                //   holder.cvDuration.setText(lastRecordByCat.getOption() + ":");
                String optionString = activityHelper.getOptionLocale(lastRecordByCat.getOption());

                if (optionString.length() > 12) {

                    optionString = optionString.substring(0, 11) + "...";

                    holder.cvDuration.setText(optionString);
                } else {
                    holder.cvDuration.setText(optionString);
                }
//////                else{
//////
//////                    your_text_view.setText(YourString); //Dont do any change
//              holder.cvDuration.setText(lastRecordByCat.getOption() );


            } else if (cat == 16) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                // holder.recordDetailTx.setVisibility(View.GONE);
                // holder.recordDetailLayout.setVisibility(View.GONE);
                String tempreture;
                if (temperatureUnit.equals("c")) {
                    tempreture = String.valueOf(formHelper.getTemperatureByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), temperatureUnit));
                } else {
                    tempreture = String.valueOf(formHelper.getTemperatureByInit(lastRecordByCat.getAmount(), lastRecordByCat.getAmountUnit(), temperatureUnit));
                }
                holder.cvDuration.setText(tempreture + temperatureUnit);

            } else if (cat == 17) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                // holder.recordDetailTx.setVisibility(View.GONE);
                // holder.recordDetailLayout.setVisibility(View.GONE);
                String optionString = activityHelper.getOptionLocale(lastRecordByCat.getOption());

                if (optionString.length() > 12) {

                    optionString = optionString.substring(0, 11) + "...";

                    holder.cvDuration.setText(optionString);
                } else {
                    holder.cvDuration.setText(optionString);
                }

            } else if (cat == 18) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                // holder.recordDetailTx.setVisibility(View.GONE);
                // holder.recordDetailLayout.setVisibility(View.GONE);

            } else if (cat == 19) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                // holder.cvDuration.setVisibility(View.GONE);
                //   holder.cvDuration.setText(lastRecordByCat.getOption() + ":");
                String optionString = activityHelper.getOptionLocale(lastRecordByCat.getOption());

                if (optionString.length() > 12) {

                    optionString = optionString.substring(0, 11) + "...";

                    holder.cvDuration.setText(optionString);
                } else {
                    holder.cvDuration.setText(optionString);
                }
                //  holder.cvDuration.setText(lastRecordByCat.getOption() );

            } else if (cat == 20) {
                lastRecordByCat = db.getLastRecord(cat, userID);
                // holder.recordDetailTx.setVisibility(View.GONE);
                // holder.recordDetailLayout.setVisibility(View.GONE);
                holder.cvDuration.setText(activityHelper.getOptionLocale(lastRecordByCat.getOption()));
            }

            String datetime = formHelper.getDateWithTimeFormat(dateFormatUtility.getStringDateFormat(lastRecordByCat.getDateEnd()), dateFormatUtility.getStringTimeFormat(lastRecordByCat.getTimeEnd()));
            holder.activitiesLastRecord.setText(formHelper.timeDiffNowCount(datetime));

        } else {
            holder.cvDuration.setText("No data");
            holder.activitiesLastRecord.setVisibility(View.GONE);
//            holder.recordDetailLayout.setVisibility(View.GONE);
        }


    }


    public void setInitValue() {

        volumeUnit = localDataHelper.getVolumeUnit();
        temperatureUnit = localDataHelper.getTemperatureUnit();
        whhUnit = localDataHelper.getWhhUnit();
        image = null;

    }


    public void launchActivity(int activityID) {

        if (activityID == 1) {
            Intent intent = new Intent(mContext, BreastfeedActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 2) {

            Intent intent = new Intent(mContext, PumpActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 3) {

            Intent intent = new Intent(mContext, FormulaActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 4) {

            Intent intent = new Intent(mContext, BottlePumpedActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 5) {

            Intent intent = new Intent(mContext, FoodActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 6) {

            Intent intent = new Intent(mContext, DiaperActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 7) {

            Intent intent = new Intent(mContext, BathActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 8) {

            Intent intent = new Intent(mContext, SleepActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 9) {

            Intent intent = new Intent(mContext, TummyTimeActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 10) {

            Intent intent = new Intent(mContext, SunbathTimeActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 11) {

            Intent intent = new Intent(mContext, PlayActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 12) {

            Intent intent = new Intent(mContext, MassageActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 13) {

            Intent intent = new Intent(mContext, DrinkActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 14) {

            Intent intent = new Intent(mContext, CryingActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 15) {

            Intent intent = new Intent(mContext, VaccinationActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 16) {

            Intent intent = new Intent(mContext, TemperatureActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 17) {

            Intent intent = new Intent(mContext, MedicineActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 18) {

            Intent intent = new Intent(mContext, DoctorVisitActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 19) {

            Intent intent = new Intent(mContext, SymptomActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 20) {

            Intent intent = new Intent(mContext, PottyActivity.class);
            mContext.startActivity(intent);
        } else if (activityID == 21) {
            Intent intent = new Intent(mContext, ActivitiesConfigActivity.class);
            mContext.startActivity(intent);
        }
    }


    @Override
    public int getItemCount() {
        return activitiesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        int viewType = 0; //Default is 1
//        if (activitiesList.get(position).getActivitiesId() == 1)
//            viewType = 1; //if zero, it will be a header view
        switch (activitiesList.get(position).getFixedID()) {
            case 1:
                viewType = 1;
                break;
            case 2:
                viewType = 2;
                break;
            case 3:
                viewType = 3;
                break;
            case 4:
                viewType = 4;
                break;
            case 5:
                viewType = 5;
                break;
            case 6:
                viewType = 6;
                break;
            case 7:
                viewType = 7;
                break;
            case 8:
                viewType = 8;
                break;
            case 9:
                viewType = 9;
                break;
            case 10:
                viewType = 10;
                break;
            case 21:
                viewType = 21;
                break;
        }

        return viewType;
    }

    public void updateCardView(Context context
            , Boolean isVolumeUnit
            , RecordFirebase recordFirebase
            , int activitiesId
            , String option
            , MyViewHolder holder
            , HashMap<String, NotificationTimer> timerMap
            , List<String> nameListForCheckingExistence
            , NotificationType notificationType
            , int notificationIcon
            , int startButtonIcon
            , int pauseButtonIcon
            , int stopButtonIcon
            , Class notificationReceiverClass
            , TextView cvDuration /*holder.cvDuration*/
            , Button cvButton /*holder.cvSleepStart*/){

        IntentFilter intentFilterDuration = new IntentFilter();
        IntentFilter intentFilterSButtonIcon = new IntentFilter();

        long[] duration = new long[1];
        final String[] notificationStatus = new String[1];
       // intentFilterDuration.addAction(TIME_COUNTER_ACTION+ActivitiesAdapter.this.localDataHelper.getActiveUserId()+ timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).notificationType);
       // intentFilterSButtonIcon.addAction(HOME_FRAGMENT_BUTTON_ICON_UPDATE+ timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).notificationType+ActivitiesAdapter.this.localDataHelper.getActiveUserId());

        intentFilterDuration.addAction(TIME_COUNTER_ACTION+ActivitiesAdapter.this.localDataHelper.getActiveUserId()+ notificationType);
        intentFilterSButtonIcon.addAction(HOME_FRAGMENT_BUTTON_ICON_UPDATE+ notificationType+ActivitiesAdapter.this.localDataHelper.getActiveUserId());


        if(nameListForCheckingExistence.contains(this.localDataHelper.getActiveUserId())==false) {
            NotificationTimer notificationTimer = new NotificationTimer(isVolumeUnit,ActivitiesAdapter.this.localDataHelper.getActiveUserId(),
                    ActivitiesAdapter.this.db.getUser(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).getUserName()
                    , notificationType
                    , context
                    , notificationIcon
                    , startButtonIcon
                    , pauseButtonIcon
                    , stopButtonIcon
                    , notificationReceiverClass);

            timerMap.put(this.localDataHelper.getActiveUserId(),notificationTimer);
            nameListForCheckingExistence.add(this.localDataHelper.getActiveUserId());
        }
        Log.d("test update card view",ActivitiesAdapter.this.localDataHelper.getActiveUserId()+timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).notificationType);
        // Register intentFilterSleepDuration


        //if(!notificationTypeMapForCheckingLen.containsKey(notificationType+ActivitiesAdapter.this.localDataHelper.getActiveUserId())) {

       //}
        // Change button icon
        if((NotificationConstant.PAUSE).equals(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).notificationStatus)){
            Resources res = holder.itemView.getContext().getResources();
            Drawable iconPause = res.getDrawable(pauseButtonIcon);
            cvDuration.setText(convertToTimeWithTwoFormat(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass));
            cvButton.setBackground(iconPause);
        }
        else if((NotificationConstant.STOP).equals(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).notificationStatus)){
            Resources res = holder.itemView.getContext().getResources();
            Drawable iconStart = res.getDrawable(startButtonIcon);
            cvButton.setBackground(iconStart);
        }
        else if((NotificationConstant.RESUME).equals(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).notificationStatus)){
            Resources res = holder.itemView.getContext().getResources();
            Drawable iconStop = res.getDrawable(stopButtonIcon);
            cvButton.setBackground(iconStop);
        }
        // Create a broadcast receiver to receive data from Timer and action buttons
        timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                duration[0] = intent.getLongExtra(TIME_COUNTER_ACTION + timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).notificationType + ActivitiesAdapter.this.localDataHelper.getActiveUserId(), timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePassOnStopOnNotification);
                Log.d("Sleep time:", String.valueOf(duration[0]));
                notificationStatus[0] = intent.getStringExtra(HOME_FRAGMENT_BUTTON_ICON_UPDATE + timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).notificationType + ActivitiesAdapter.this.localDataHelper.getActiveUserId());

                cvDuration.setText(convertToTimeWithTwoFormat(duration[0]));
                if ((NotificationConstant.PAUSE).equals(notificationStatus[0])) {
                    Resources res = holder.itemView.getContext().getResources();
                    Drawable iconPause = res.getDrawable(pauseButtonIcon);
                    cvButton.setBackground(iconPause);
                    cvDuration.setText(convertToTimeWithTwoFormat(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePass));
                } else if ((NotificationConstant.STOP).equals(notificationStatus[0])) {
                    if (!timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).isVolumeUnit) {
                        timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser, recordFirebase, activitiesId, option, amount, amountUnit, note, db);
                    } else {
                        timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).stopOnFragment(googleUser, recordFirebase, activitiesId, option, amount, volumeUnit, note, db);
                    }
                    Log.d("this.timePassOnStop:", String.valueOf(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).timePassOnStopOnNotification));
                    Resources res = holder.itemView.getContext().getResources();
                    Drawable iconStart = res.getDrawable(startButtonIcon);
                    cvButton.setBackground(iconStart);
                    setViewByCat(holder, activitiesId);
                } else if ((NotificationConstant.RESUME).equals(notificationStatus[0])) {
                    Resources res = holder.itemView.getContext().getResources();
                    Drawable iconStop = res.getDrawable(stopButtonIcon);
                    cvButton.setBackground(iconStop);
                }
            }
        };
        // Add broadcast receiver to the list

        Log.d("Tuan Broadcast list:", String.valueOf(NotificationTimer.broadcastReceiverList.size()));
        //notificationTypeMapForCheckingLen.put(notificationType+ActivitiesAdapter.this.localDataHelper.getActiveUserId(),0);////
        context.getApplicationContext().registerReceiver(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiver, intentFilterDuration);
        context.getApplicationContext().registerReceiver(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiver, intentFilterSButtonIcon);
        NotificationTimer.broadcastReceiverList.add(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiver);
        //timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiverListOfUser.add(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiver);
        UserObject.UserObjectMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiverListOfUser.add(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiver);
        /*if(!NotificationTimer.broadcastReceiverMap.containsKey(ActivitiesAdapter.this.localDataHelper.getActiveUserId())) {
            NotificationTimer.broadcastReceiverMap.put(ActivitiesAdapter.this.localDataHelper.getActiveUserId(), timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiverListOfUser);
        }
        else{
            ;
        }*/
        Log.d("ReceiverListOfUser",timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiverListOfUser.toString());
        Log.d("ReceiverListOfUserSize", String.valueOf(timerMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiverListOfUser.size()));
        Log.d("ReceiverListOfUserStatic", String.valueOf( UserObject.UserObjectMap.get(ActivitiesAdapter.this.localDataHelper.getActiveUserId()).broadcastReceiverListOfUser.size()));

    }


}
