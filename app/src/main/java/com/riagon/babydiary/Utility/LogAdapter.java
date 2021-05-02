package com.riagon.babydiary.Utility;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.TodayLogActivity;
import com.riagon.babydiary.R;

import java.util.List;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.MyViewHolder> {

    //private List<LogActivity> activitiesLogList;
    private List<Record> recordsList;
    private User currentUser;
    private Context mContext;
    private DateFormatUtility dateFormatUtility;
    private FormHelper formHelper;
    private LocalDataHelper localDataHelper;
    private ActivityHelper activityHelper;
    private Boolean isDarkmode;
    private Activity activity;

    public static final int DEFAULT_VIEW_TYPE = 0;
    public static final int FEED_VIEW_TYPE = 1;
    public static final int PUMP_VIEW_TYPE = 2;
    public static final int BOTTLE_VIEW_TYPE = 3;
    public static final int FOOD_VIEW_TYPE = 4;
    public static final int DIAPER_VIEW_TYPE = 5;
    public static final int BATH_VIEW_TYPE = 6;
    public static final int DOCTOR_VIEW_TYPE = 7;

    private String volumeUnit;
    private String temperatureUnit;
    private String whhUnit;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView recordDate, recordBabyAge;
        public ImageView activitiesIcon, dots_separator, icon_diaper, icon_volume,icon_bath;
        public TextView recordTime, recordDuration, txt_option;
        private RelativeLayout dateLayout;
        private LinearLayout log_item_layout;
        private TextView recordNote, recordVolume;


        public MyViewHolder(View view, int viewType) {
            super(view);
            recordDate = (TextView) view.findViewById(R.id.record_date);
            recordBabyAge = (TextView) view.findViewById(R.id.record_baby_age);
            activitiesIcon = (ImageView) view.findViewById(R.id.activitiesIcon);
            dots_separator = (ImageView) view.findViewById(R.id.dots_separator);
            recordTime = (TextView) view.findViewById(R.id.record_time);
            recordDuration = (TextView) view.findViewById(R.id.record_duration);
            dateLayout = (RelativeLayout) view.findViewById(R.id.date_layout);
            log_item_layout = (LinearLayout) view.findViewById(R.id.log_item_layout);
            recordNote = (TextView) view.findViewById(R.id.record_note);
            icon_diaper = (ImageView) view.findViewById(R.id.icon_diaper);
            icon_bath= (ImageView) view.findViewById(R.id.icon_bath);
            icon_volume = (ImageView) view.findViewById(R.id.icon_volume);
            txt_option = (TextView) view.findViewById(R.id.txt_option);

            //  if (viewType == PUMP_VIEW_TYPE) {
            recordVolume = (TextView) view.findViewById(R.id.record_volume);
            //  }

        }
    }


    public LogAdapter(Context mContext, List<Record> recordsList, User currentUser, Activity activity) {
        this.mContext = mContext;
        this.recordsList = recordsList;
        dateFormatUtility = new DateFormatUtility(mContext);
        formHelper = new FormHelper(mContext);
        localDataHelper = new LocalDataHelper(mContext);
        this.currentUser = currentUser;
        activityHelper = new ActivityHelper(mContext);
        this.activity = activity;
        setInitValue();
    }

    @Override
    public LogAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;
        switch (viewType) {
            case FEED_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.log_feed_item, parent, false);
                break;
            case PUMP_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.log_pump_item, parent, false);
                break;
            case BOTTLE_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.log_bottle_item, parent, false);
                break;
            case FOOD_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.log_food_item, parent, false);
                break;
            case DIAPER_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.log_diaper_item, parent, false);
                break;
            case BATH_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.log_bath_item, parent, false);
                break;
            case DOCTOR_VIEW_TYPE:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.log_doctor_item, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.log_list_item, parent, false);
        }


        return new LogAdapter.MyViewHolder(itemView, viewType);
    }

    @Override
    public void onBindViewHolder(final LogAdapter.MyViewHolder holder, final int position) {
        final Record record = recordsList.get(position);
        // holder.recordDate.setText(dateFormatUtility.getStringDateFormat(record.getDateEnd()));
        // holder.recordBabyAge.setText(formHelper.getAgeByMonths(dateFormatUtility.getStringDateFormat(currentUser.getUserBirthday())));


        if (activity instanceof TodayLogActivity) {
            holder.dateLayout.setVisibility(View.GONE);
        } else {
            setDateView(holder, position);
        }

        holder.activitiesIcon.setImageResource(activityHelper.getIcon(record.getActivitiesId()));
        // holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeStart()) + "-" + dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));

        if (record.getActivitiesId() == 1) {

            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeStart()) + "-" + dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(activityHelper.getOptionLocale(record.getOption()) + ": " + formHelper.getDurationFormat(record.getDuration()));

        } else if (record.getActivitiesId() == 2) {
            String amount;
            if (volumeUnit.equals("ml")) {
                amount = String.valueOf((int) formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
            } else {
                amount = String.valueOf(formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
            }
            holder.recordVolume.setText(amount + volumeUnit);
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeStart()) + "-" + dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(activityHelper.getOptionLocale(record.getOption()) + ": " + formHelper.getDurationFormat(record.getDuration()));
        } else if (record.getActivitiesId() == 3) {
            String amount;
            if (volumeUnit.equals("ml")) {
                amount = String.valueOf((int) formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
            } else {
                amount = String.valueOf(formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
            }
            holder.recordVolume.setText(amount + volumeUnit);
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
        } else if (record.getActivitiesId() == 4) {
            String amount;
            if (volumeUnit.equals("ml")) {
                amount = String.valueOf((int) formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
            } else {
                amount = String.valueOf(formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
            }
            holder.recordVolume.setText(amount + volumeUnit);
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));

            // else if (record.getActivitiesId() == 4) {
        } else if (record.getActivitiesId() == 5) {
            String amount;
            if (volumeUnit.equals("ml")) {
                amount = String.valueOf((int) formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
            } else {
                amount = String.valueOf(formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
            }

            holder.recordVolume.setText(amount + volumeUnit);
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            if (record.getOption().equals("")) {
                holder.recordDuration.setVisibility(View.GONE);
            } else {
                holder.recordDuration.setVisibility(View.VISIBLE);
                holder.recordDuration.setText(activityHelper.getOptionLocale(record.getOption())+ ":");
            }


        } else if (record.getActivitiesId() == 6) {
            // settingHelper.setBackgroundCheckboxGroupSelected(getApplicationContext(), fish, currentUser.getUserTheme());
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));

            //  settingHelper.setIconDiaper());
            holder.txt_option.setText(activityHelper.getOptionLocale(record.getOption()));

            if (record.getOption().equals("Wet")) {
                holder.icon_diaper.setBackgroundResource(R.drawable.ic_log_wet);

            } else if (record.getOption().equals("Dirty")) {
                holder.icon_diaper.setBackgroundResource(R.drawable.ic_log_dirty);

            } else if (record.getOption().equals("Mixed")) {
                holder.icon_diaper.setBackgroundResource(R.drawable.ic_log_mixed);

            }


        } else if (record.getActivitiesId() == 7) {
            // settingHelper.setBackgroundCheckboxGroupSelected(getApplicationContext(), fish, currentUser.getUserTheme());
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            //  settingHelper.setIconDiaper());
            holder.txt_option.setText(activityHelper.getOptionLocale(record.getOption()));

            if (record.getOption().equals("Bath")) {
                holder.icon_bath.setBackgroundResource(R.drawable.ic_log_bath);

            } else if (record.getOption().equals("Hair Wash")) {
                holder.icon_bath.setBackgroundResource(R.drawable.ic_log_hairwash);

            } else if (record.getOption().equals("Both")) {
                holder.icon_bath.setBackgroundResource(R.drawable.ic_log_both);

            }
        } else if (record.getActivitiesId() == 8) {
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeStart()) + "-" + dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(formHelper.getDurationFormat(record.getDuration()));

        } else if (record.getActivitiesId() == 9) {
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeStart()) + "-" + dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(formHelper.getDurationFormat(record.getDuration()));

        } else if (record.getActivitiesId() == 10) {
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeStart()) + "-" + dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(formHelper.getDurationFormat(record.getDuration()));

        } else if (record.getActivitiesId() == 11) {
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeStart()) + "-" + dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(formHelper.getDurationFormat(record.getDuration()));

        } else if (record.getActivitiesId() == 12) {
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeStart()) + "-" + dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(formHelper.getDurationFormat(record.getDuration()));

        } else if (record.getActivitiesId() == 13) {
            String amount;
            if (volumeUnit.equals("ml")) {
                amount = String.valueOf((int) formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
            } else {
                amount = String.valueOf(formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
            }
            holder.recordVolume.setText(amount + volumeUnit);
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            //holder.recordDuration.setText(activityHelper.getOptionLocale(record.getOption()) + ": ");
            if (record.getOption().equals("")) {
                holder.recordDuration.setVisibility(View.GONE);
            } else {
                holder.recordDuration.setVisibility(View.VISIBLE);
                holder.recordDuration.setText(activityHelper.getOptionLocale(record.getOption())+ ":");
            }

        } else if (record.getActivitiesId() == 14) {
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeStart()) + "-" + dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(formHelper.getDurationFormat(record.getDuration()));

        } else if (record.getActivitiesId() == 15) {

            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(activityHelper.getOptionLocale(record.getOption()));


        } else if (record.getActivitiesId() == 16) {

            String amount;
            amount = String.valueOf(formHelper.getTemperatureByInit(record.getAmount(), record.getAmountUnit(), temperatureUnit));
            holder.recordVolume.setText(amount + temperatureUnit);
            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setVisibility(View.GONE);

        } else if (record.getActivitiesId() == 17) {

            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(activityHelper.getOptionLocale(record.getOption()));


        } else if (record.getActivitiesId() == 18) {

            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));


        } else if (record.getActivitiesId() == 19) {

            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(activityHelper.getOptionLocale(record.getOption()));

        } else if (record.getActivitiesId() == 20) {

            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            //  settingHelper.setIconDiaper());
            holder.txt_option.setText(activityHelper.getOptionLocale(record.getOption()));

            if (record.getOption().equals("Wet")) {
                holder.icon_diaper.setBackgroundResource(R.drawable.ic_log_wet);

            } else if (record.getOption().equals("Dirty")) {
                holder.icon_diaper.setBackgroundResource(R.drawable.ic_log_dirty);

            } else if (record.getOption().equals("Mixed")) {
                holder.icon_diaper.setBackgroundResource(R.drawable.ic_log_mixed);

            }
        } else if (record.getActivitiesId() == 21) {

            holder.recordTime.setText(dateFormatUtility.getStringTimeFormat(record.getTimeEnd()));
            holder.recordDuration.setText(activityHelper.getOptionLocale(record.getOption()));
            holder.recordVolume.setVisibility(View.GONE);
            holder.icon_volume.setVisibility(View.GONE);

        } else {
            holder.recordDuration.setText(activityHelper.getOptionLocale(record.getOption()) + ": " + formHelper.getDurationFormat(record.getDuration()));
        }

        setNoteView(holder, position);
        setSeparatorDotView(holder, position);
    }

    public void setInitValue() {

        volumeUnit = localDataHelper.getVolumeUnit();
        temperatureUnit = localDataHelper.getTemperatureUnit();
        whhUnit = localDataHelper.getWhhUnit();
        isDarkmode = localDataHelper.getDarkMode();
    }


    private void setNoteView(MyViewHolder holder, int position) {
        Record record = recordsList.get(position);
//        if (record.getNote().equals(null)) {
//            holder.recordNote.setText("");
//
//        } else {
        holder.recordNote.setText(record.getNote());
        //  }

    }


    private void setDateView(MyViewHolder holder, int position) {
        final Record record1 = recordsList.get(position);
        //Growth growthLast=activitiesGrowthList.get(0);
        String dateSet = "";
        String ageSet = "";
        String date1 = "";
        String date2 = "";
        //  String dateLast="";
        date1 = dateFormatUtility.getStringDateFormat(record1.getDateEnd());
        //  dateLast=dateFormatUtility.getStringDateFormat(growthLast.getGrowthDate());

        if (position > 0) {
            dateSet = date1;
            Record record2 = recordsList.get(position - 1);
            date2 = dateFormatUtility.getStringDateFormat(record2.getDateEnd());

            if (date2.equals(date1)) {
                dateSet = "";
                ageSet = "";
                holder.dateLayout.setVisibility(View.GONE);

            } else {
                dateSet = dateFormatUtility.getStringDateFormatHuman(mContext, dateFormatUtility.getDateFormat(date1));
                ageSet = formHelper.getMonthDiffShort(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(currentUser.getUserBirthday())), dateFormatUtility.getStringDateFormat(record1.getDateEnd()));
                holder.dateLayout.setVisibility(View.VISIBLE);
            }
        } else {
            dateSet = dateFormatUtility.getStringDateFormatHuman(mContext, dateFormatUtility.getDateFormat(date1));
            ageSet = formHelper.getMonthDiffShort(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(currentUser.getUserBirthday())), dateFormatUtility.getStringDateFormat(record1.getDateEnd()));
            holder.dateLayout.setVisibility(View.VISIBLE);
            //  holder.dots_separator.setVisibility(View.GONE);
        }

        holder.recordDate.setText(dateSet);
        holder.recordBabyAge.setText("(" + ageSet + ")");

    }

    private void setSeparatorDotView(MyViewHolder holder, int position) {
        final Record record1 = recordsList.get(position);
        //Growth growthLast=activitiesGrowthList.get(0);

        String date1 = "";
        String date2 = "";

        date1 = dateFormatUtility.getStringDateFormat(record1.getDateEnd());


        if (position < recordsList.size() - 1) {
            Record record2 = recordsList.get(position + 1);
            date2 = dateFormatUtility.getStringDateFormat(record2.getDateEnd());

            if (date2.equals(date1)) {
                holder.dots_separator.setVisibility(View.VISIBLE);

            } else {
                holder.dots_separator.setVisibility(View.GONE);
            }
        } else {
            holder.dots_separator.setVisibility(View.VISIBLE);
            //  holder.dots_separator.setVisibility(View.GONE);
        }


        if (position == recordsList.size() - 1) {
            holder.dots_separator.setVisibility(View.GONE);
        }


    }


    @Override
    public int getItemViewType(int position) {
        int viewType = 0; //Default is 1
//        if (activitiesList.get(position).getActivitiesId() == 1)
//            viewType = 1; //if zero, it will be a header view
        switch (recordsList.get(position).getActivitiesId()) {
            case 1:
                viewType = FEED_VIEW_TYPE;
                break;
            case 2:
            case 5:
            case 13:
            case 16:
                viewType = PUMP_VIEW_TYPE;
                break;
            case 3:
            case 4:
                viewType = BOTTLE_VIEW_TYPE;
                break;
//        case 5:
//        viewType = FOOD_VIEW_TYPE;
//        break;
            case 6:
//        viewType = DIAPER_VIEW_TYPE;
//        break;

            case 20:
                viewType = DIAPER_VIEW_TYPE;
                break;
                case 7:
                viewType = BATH_VIEW_TYPE;
                    break;
//            case 7:
//                viewType = BATH_VIEW_TYPE;
//                break;
            case 18:
                viewType = DOCTOR_VIEW_TYPE;
                break;

            default:
                viewType = DEFAULT_VIEW_TYPE;
                break;
        }

        return viewType;
    }


    @Override
    public int getItemCount() {
        return recordsList.size();
    }


}

