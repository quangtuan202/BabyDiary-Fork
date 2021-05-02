package com.riagon.babydiary.Utility;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Activities;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Notification.AlarmReceiver;
import com.riagon.babydiary.Notification.NotificationScheduler;
import com.riagon.babydiary.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.widget.Toast.makeText;

public class SetNotificationAdapter extends RecyclerView.Adapter<SetNotificationAdapter.MyViewHolder> {
    private DatabaseHelper db;
    private List<Activities> activitiesNotificationList;
    private Context mContext;
    private FormHelper formHelper;
    private LocalDataHelper localDataHelper;
    private DateFormatUtility dateFormatUtility;
    public SettingHelper settingHelper;
    public ActivityHelper activityHelper;
    public User currentUser;
    public TextView text_dialog;
    private ScrollableNumberPicker snpd;
    private ScrollableNumberPicker snph;
    private ScrollableNumberPicker snpm;
    public Switch switch_repaet;
    public TextView timepicker_notification;
    public TextView datepicker_notification;
    public ImageView activities_icon;
    public TextView activities_name;
    public RelativeLayout relativelayout_repeatin;
    public RelativeLayout relativelayout_time;
    public RelativeLayout relativelayout_date;
    private String notiOption = "No Repeat";
    private Date notiDate;
    private Date notiTime;
    private String notiRepeatInDay;
    private String notiRepeatInTime;
    int mYear;
    int mMonth;
    int mDay;
    int hour;
    int minute;
    NotificationScheduler notificationScheduler = new NotificationScheduler();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView activitiesIcon;
        public TextView activitiesName;
        public Switch isNotify;
        public RadioGroup radioGroup_notification;
        public RadioButton radioButton_default;
        public RadioButton radioButton_custom;


        public MyViewHolder(View view) {
            super(view);
            activitiesName = (TextView) view.findViewById(R.id.cvName);
            activitiesIcon = (ImageView) view.findViewById(R.id.cvIcon);
            isNotify = (Switch) view.findViewById(R.id.isNotifySwitch);
            radioGroup_notification = (RadioGroup) view.findViewById(R.id.radioGroup_notification);
            radioButton_default = (RadioButton) view.findViewById(R.id.radioButton_default);
            radioButton_custom = (RadioButton) view.findViewById(R.id.radioButton_custom);

        }
    }

    public SetNotificationAdapter(Context mContext, List<Activities> activitiesNotificationList, DatabaseHelper databaseHelper) {
        this.mContext = mContext;
        this.activitiesNotificationList = activitiesNotificationList;
        this.db = databaseHelper;
        formHelper = new FormHelper(mContext);
        settingHelper = new SettingHelper(mContext);
        localDataHelper = new LocalDataHelper(mContext);
        dateFormatUtility = new DateFormatUtility(mContext);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        activityHelper = new ActivityHelper(mContext);
    }

    @Override
    public SetNotificationAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_item, parent, false);

        return new SetNotificationAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SetNotificationAdapter.MyViewHolder holder, final int position) {
        final Activities activities = activitiesNotificationList.get(position);
        holder.activitiesName.setText(activityHelper.getActivityName(activities.getFixedID()));
        // holder.activitiesName.setTextColor(mContext.getResources().getColor(setTint(activities.getActivitiesName())));
        holder.isNotify.setChecked(activities.getNotify());

        setButtonChecked(holder, activities.getNotifyOption());
        setButtonEnable(holder, activities.getNotify());
        holder.activitiesIcon.setImageResource(activityHelper.getIcon(activities.getFixedID()));

//        if (activities.getNotify()) {
//
//            if (activities.getNotifyOption().equals("No Repeat")) {
//                int hours = parseHour(dateFormatUtility.getStringTimeFormat(activities.getNotifyTime()));
//                int minute = parseMinute(dateFormatUtility.getStringTimeFormat(activities.getNotifyTime()));
//                notificationScheduler.setCustomReminder(mContext, AlarmReceiver.class, activities.getNotifyDate(), hours, minute, activities.getFixedID());
//                Log.i("NOTI", "Set NotificationScheduler" + activities.getActivitiesId() + " " + dateFormatUtility.getStringDateFormat(activities.getNotifyDate()) + " " + activities.getFixedID() + " " + hours + " " + minute);
//            } else {
//                if (db.getRecordByCatCount(activities.getFixedID(), currentUser.getUserId()) > 0) {
//                    int minute = Integer.parseInt(dateFormatUtility.getStringMinutesFormat(db.getLastRecord(activities.getFixedID(), currentUser.getUserId()).getTimeEnd()));
//                    notificationScheduler.setReminder(mContext, AlarmReceiver.class, formHelper.getHoursNow(), formHelper.addThreeMinutes(minute), activities.getFixedID());
//                    Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + activities.getFixedID() + " " + formHelper.getHoursNow() + " " + formHelper.addThreeMinutes(minute));
//                }
//            }
//
//
//        } else {
//            //  cancelReminder(mContext, AlarmReceiver.class,activities.getActivitiesId());
//            // Log.i("NOTI", "Cancel NotificationScheduler" + activities.getFixedID());
//        }


        holder.isNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonEnable(holder, holder.isNotify.isChecked());
                db.updateIsNotifyActivities(activities, holder.isNotify.isChecked(), dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));
                if (holder.isNotify.isChecked()) {

                    //NotificationScheduler.setReminder(mContext, AlarmReceiver.class, 14, 35);
                    if (holder.radioButton_custom.isChecked()) {

                        int hours = parseHour(dateFormatUtility.getStringTimeFormat(activities.getNotifyTime()));
                        int minute = parseMinute(dateFormatUtility.getStringTimeFormat(activities.getNotifyTime()));
                        long millisecond = Integer.parseInt(activities.getNotifyRepeatInDay()) * 24 * 60 * 60 * 1000 + Integer.parseInt(dateFormatUtility.getStringHoursFormat(dateFormatUtility.getTimeFormat(activities.getNotifyRepeatInTime()))) * 60 * 60 * 1000 + Integer.parseInt(dateFormatUtility.getStringMinutesFormat(dateFormatUtility.getTimeFormat(activities.getNotifyRepeatInTime()))) * 60 * 1000;

                        if (notiOption.equals("No Repeat")) {
                            // notificationScheduler.setCustomReminder(mContext, AlarmReceiver.class, notiDate, hours, minute, activities.getActivitiesId());
                            notificationScheduler.setCustomReminder(mContext, AlarmReceiver.class, activities.getNotifyDate(), hours, minute, activities.getActivitiesId());
                            Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + dateFormatUtility.getStringDateFormat(activities.getNotifyDate()) + " " + activities.getFixedID() + " " + hours + " " + minute);
                        } else {
                            notificationScheduler.setCustomRepeatReminder(mContext, AlarmReceiver.class, activities.getNotifyDate(), hours, minute, millisecond, activities.getActivitiesId());
                            Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + dateFormatUtility.getStringDateFormat(activities.getNotifyDate()) + " " + activities.getFixedID() + " " + hours + " " + minute + " Repeat in " + notiRepeatInDay + notiRepeatInTime);
                        }

                        //  Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + dateFormatUtility.getStringDateFormat(activities.getNotifyDate()) + " " + activities.getFixedID() + " " + hours + " " + minute);
                    } else {
                        if (db.getRecordByCatCount(activities.getFixedID(), currentUser.getUserId()) > 0) {
                            int minute = Integer.parseInt(dateFormatUtility.getStringMinutesFormat(db.getLastRecord(activities.getFixedID(), currentUser.getUserId()).getTimeEnd()));
                            int hours = Integer.parseInt(dateFormatUtility.getStringHoursFormat(db.getLastRecord(activities.getFixedID(), currentUser.getUserId()).getTimeEnd()));
                            notificationScheduler.setReminder(mContext, AlarmReceiver.class, formHelper.addThreeHours(hours), minute, activities.getActivitiesId());
                            Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + activities.getFixedID() + " " + formHelper.addThreeHours(hours) + " " + minute);
                        }
                    }


                } else {

                    notificationScheduler.cancelReminder(mContext, AlarmReceiver.class, activities.getFixedID());
                    Log.i("NOTI", "Cancel NotificationScheduler" + activities.getFixedID());

                }


            }
        });


//        holder.radioGroup_notification.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                switch (checkedId) {
//                    case R.id.radioButton_default:
//                        //  setButtonChecked(holder, activities.getNotifyOption());
//                        db.updateNotificationOptionActivities(activities, "Default");
//                        break;
//                    case R.id.radioButton_custom:
//                        DurationPickerDialog(holder, activities.getFixedID());
//                        //  setButtonChecked(holder, activities.getNotifyOption());
//                        break;
//
//                }
//            }
//        });

        holder.radioButton_default.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.updateNotificationOptionActivities(activities, "Default", dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));


                if (holder.isNotify.isChecked()) {

                    if (db.getRecordByCatCount(activities.getFixedID(), currentUser.getUserId()) > 0) {
                        int minute = Integer.parseInt(dateFormatUtility.getStringMinutesFormat(db.getLastRecord(activities.getFixedID(), currentUser.getUserId()).getTimeEnd()));
                        int hours = Integer.parseInt(dateFormatUtility.getStringHoursFormat(db.getLastRecord(activities.getFixedID(), currentUser.getUserId()).getTimeEnd()));
                        notificationScheduler.setReminder(mContext, AlarmReceiver.class, formHelper.addThreeHours(hours), minute, activities.getActivitiesId());
                        Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + activities.getFixedID() + " " + formHelper.addThreeHours(hours) + " " + minute);
                    }
                }


            }
        });

        holder.radioButton_custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationPickerDialog(holder, activities.getFixedID());
            }
        });


    }


    private void DurationPickerDialog(MyViewHolder holder, int activitiesID) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.activity_custrom_notification, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        Activities activities = db.getActivities(activitiesID);
        activities_icon = (ImageView) alertLayout.findViewById(R.id.icon_activities);
        activities_name = (TextView) alertLayout.findViewById(R.id.text_activities_name);
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);

        //notiDate = dateFormatUtility.getDateFormat("05-05-2020");
        //notiTime = dateFormatUtility.getTimeFormat("05:05");

        activities_icon.setImageResource(activityHelper.getIcon(activities.getFixedID()));
        activities_name.setText(activityHelper.getActivityName(activities.getFixedID()));
        text_dialog = (TextView) alertLayout.findViewById(R.id.text_dialog);
        settingHelper.setBackgroundDialog(text_dialog, currentUser.getUserTheme());
        snpd = (ScrollableNumberPicker) alertLayout.findViewById(R.id.snpd);
        snph = (ScrollableNumberPicker) alertLayout.findViewById(R.id.snph);
        snpm = (ScrollableNumberPicker) alertLayout.findViewById(R.id.snpm);
        switch_repaet = (Switch) alertLayout.findViewById(R.id.switch_repaet);
        timepicker_notification = (TextView) alertLayout.findViewById(R.id.timepicker_notification);
        datepicker_notification = (TextView) alertLayout.findViewById(R.id.datepicker_notification);
        relativelayout_repeatin = (RelativeLayout) alertLayout.findViewById(R.id.relativelayout_repeatin);
        relativelayout_time = (RelativeLayout) alertLayout.findViewById(R.id.relativelayout_time);
        relativelayout_date = (RelativeLayout) alertLayout.findViewById(R.id.relativelayout_date);
        alert.setView(alertLayout);
        alert.setCancelable(false);

        datepicker_notification.setText(formHelper.getDateNow());
        timepicker_notification.setText(formHelper.getTimeNow());


        switch_repaet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton,
                                         boolean b) {

                if (switch_repaet.isChecked()) {
                    relativelayout_repeatin.setVisibility(View.VISIBLE);
                    relativelayout_time.setVisibility(View.GONE);
                    relativelayout_date.setVisibility(View.GONE);
                    //Toast.makeText(mContext, "true", Toast.LENGTH_SHORT).show();
                    notiOption = "Repeat";

                } else {
                    relativelayout_repeatin.setVisibility(View.GONE);
                    relativelayout_time.setVisibility(View.VISIBLE);
                    relativelayout_date.setVisibility(View.VISIBLE);
                    // Toast.makeText(mContext, "false", Toast.LENGTH_SHORT).show();
                    notiOption = "No Repeat";
                }
                //  textView.setText("Switch is " +
                //  (switchPremature.isChecked() ? "On" : "Off"));
            }
        });


        timepicker_notification.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    mcurrentTime.setTime(sdf.parse(timepicker_notification.getText().toString()));// all done
                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    minute = mcurrentTime.get(Calendar.MINUTE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TimePickerDialog mTimePicker;

                mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timepicker_notification.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

                    }
                }, hour, minute, true);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        datepicker_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {

                    c.setTime(sdf.parse(datepicker_notification.getText().toString()));
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                datepicker_notification.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        alert.setNegativeButton(mContext.getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //makeText(mContext, "Cancel clicked", Toast.LENGTH_SHORT).show();
                if (activities.getNotifyOption().equals("Default")) {
                    setButtonChecked(holder, "Default");
                }

                // setButtonChecked(holder,notiOption);
            }
        });

        alert.setPositiveButton(mContext.getResources().getString(R.string.dialog_save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code for matching password
                setButtonChecked(holder, notiOption);
                notiDate = dateFormatUtility.getDateFormat(datepicker_notification.getText().toString());
                notiTime = dateFormatUtility.getTimeFormat(timepicker_notification.getText().toString());

                // Date dateNoti=dateFormatUtility.getDateFormatWithHours(datepicker_notification.getText().toString()+" "+timepicker_notification.getText().toString());

                // notiRepeatInDay = String.valueOf(snpd.getValue()) + ":" + String.valueOf(snph.getValue()) + ":" + String.valueOf(snpm.getValue());
                notiRepeatInDay = String.valueOf(snpd.getValue());
                notiRepeatInTime = snph.getValue() + ":" + snpm.getValue();


                // makeText(mContext, "Login clicked", Toast.LENGTH_SHORT).show();
                db.updateNotificationActivities(activities, notiOption, notiDate, notiTime, notiRepeatInDay, notiRepeatInTime, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));


                int hours = parseHour(timepicker_notification.getText().toString());

                int minute = parseMinute(timepicker_notification.getText().toString());

                long millisecond = snpd.getValue() * 24 * 60 * 60 * 1000 + snph.getValue() * 60 * 60 * 1000 + snpm.getValue() * 60 * 1000;

                if (notiOption.equals("No Repeat")) {
                    notificationScheduler.setCustomReminder(mContext, AlarmReceiver.class, notiDate, hours, minute, activities.getActivitiesId());
                    Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + dateFormatUtility.getStringDateFormat(notiDate) + " " + activities.getFixedID() + " " + hours + " " + minute);
                } else {
                    notificationScheduler.setCustomRepeatReminder(mContext, AlarmReceiver.class, notiDate, hours, minute, millisecond, activities.getActivitiesId());

                    Log.i("NOTI", "Set NotificationScheduler" + activities.getFixedID() + " " + dateFormatUtility.getStringDateFormat(notiDate) + " " + activities.getFixedID() + " " + hours + " " + minute + " Repeat in " + notiRepeatInDay + notiRepeatInTime);
                }


                // time_result.setText(String.format("%d h %d m", snp1.getValue(), snp2.getValue()));
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();


    }


    public void setButtonChecked(MyViewHolder holder, String notifyOption) {
        if (notifyOption.equals("Default")) {
            holder.radioButton_default.setChecked(true);

        } else {
            holder.radioButton_custom.setChecked(true);
        }
    }

    public void setButtonEnable(MyViewHolder holder, Boolean isNotify) {
        if (isNotify) {
            // holder.radioGroup_notification.setEnabled(true);

            for (int i = 0; i < holder.radioGroup_notification.getChildCount(); i++) {
                holder.radioGroup_notification.getChildAt(i).setEnabled(true);
            }
        } else {
            // holder.radioGroup_notification.setEnabled(false);
            for (int i = 0; i < holder.radioGroup_notification.getChildCount(); i++) {
                holder.radioGroup_notification.getChildAt(i).setEnabled(false);
            }
        }
    }


    @Override
    public int getItemCount() {
        return activitiesNotificationList.size();
    }

    public static int parseHour(String value) {
        try {
            String[] time = value.split(":");
            return (Integer.parseInt(time[0]));
        } catch (Exception e) {
            return 0;
        }
    }

    public static int parseMinute(String value) {
        try {
            String[] time = value.split(":");
            return (Integer.parseInt(time[1]));
        } catch (Exception e) {
            return 0;
        }
    }


}



