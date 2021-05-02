package com.riagon.babydiary.Utility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.Stats;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.TodayLogActivity;
import com.riagon.babydiary.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class StatisticsAdapter extends RecyclerView.Adapter<StatisticsAdapter.MyViewHolder> {

    //private List<LogActivity> activitiesLogList;
    private List<Record> recordsList;
    private User currentUser;
    private Context mContext;
    private DateFormatUtility dateFormatUtility;
    private TodayLogActivity today;
    private FormHelper fh;
    private LocalDataHelper localDataHelper;
    private ActivityHelper activityHelper;
    private DatabaseHelper db;
    private Boolean isDarkmode;
    private String volumeUnit;
    private String temperatureUnit;
    private String whhUnit;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView activitiesCountTx, amountTx, statisticsDetailTx;
        public ImageView activitiesIcon, amountIcon;


        public MyViewHolder(View view) {
            super(view);
            activitiesCountTx = (TextView) view.findViewById(R.id.activities_count);
            amountTx = (TextView) view.findViewById(R.id.amount);
            statisticsDetailTx = (TextView) view.findViewById(R.id.statistic_detail);
            activitiesIcon = (ImageView) view.findViewById(R.id.activities_icon);
            amountIcon = (ImageView) view.findViewById(R.id.amount_icon);

        }
    }


    public StatisticsAdapter(Context mContext, List<Record> recordsList, User currentUser, TodayLogActivity today) {
        this.mContext = mContext;
        this.recordsList = recordsList;
        db = new DatabaseHelper(mContext);
        dateFormatUtility = new DateFormatUtility(mContext);
        fh = new FormHelper(mContext);
        localDataHelper = new LocalDataHelper(mContext);
        this.currentUser = currentUser;
        activityHelper = new ActivityHelper(mContext);
        this.today = today;
        setInitValue();

    }

    @Override
    public StatisticsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_list_item, parent, false);

        return new StatisticsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final StatisticsAdapter.MyViewHolder holder, final int position) {

        Record record = recordsList.get(position);
        holder.activitiesIcon.setImageResource(activityHelper.getIcon(record.getActivitiesId()));
        holder.activitiesCountTx.setText(String.valueOf(record.getTotalRecord()));
        if (record.getActivitiesId() == 1) {
            holder.statisticsDetailTx.setText(getBreedDurationStatistics());
            holder.amountIcon.setImageResource(R.drawable.ic_start);
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 2) {
            holder.statisticsDetailTx.setText(getPumpVolumeStatistics());
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 3) {
            holder.statisticsDetailTx.setText(getVolumeStatistics(3));
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 4) {
            holder.statisticsDetailTx.setText(getVolumeStatistics(4));
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 5) {
            holder.statisticsDetailTx.setText(getVolumeStatistics(5));
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 6) {
            holder.statisticsDetailTx.setText(getTimeDiaperStatistics(6));
            holder.amountIcon.setVisibility(View.GONE);
        } else if (record.getActivitiesId() == 7) {
            holder.statisticsDetailTx.setText(getTimeBathStatistics());
            holder.amountIcon.setVisibility(View.GONE);
        } else if (record.getActivitiesId() == 8) {
            holder.statisticsDetailTx.setText(getDurationStatistics(8));
            holder.amountIcon.setImageResource(R.drawable.ic_start);
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 9) {
            holder.statisticsDetailTx.setText(getDurationStatistics(9));
            holder.amountIcon.setImageResource(R.drawable.ic_start);
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 10) {
            holder.statisticsDetailTx.setText(getDurationStatistics(10));
            holder.amountIcon.setImageResource(R.drawable.ic_start);
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 11) {
            holder.statisticsDetailTx.setText(getDurationStatistics(1));
            holder.amountIcon.setImageResource(R.drawable.ic_start);
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 12) {
            holder.statisticsDetailTx.setText(getDurationStatistics(12));
            holder.amountIcon.setImageResource(R.drawable.ic_start);
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 13) {
            holder.statisticsDetailTx.setText(getVolumeStatistics(13));
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 14) {
            holder.statisticsDetailTx.setText(getDurationStatistics(14));
            holder.amountIcon.setImageResource(R.drawable.ic_start);
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 16) {
            holder.statisticsDetailTx.setText(getTemperatureStatistics());
            holder.amountIcon.setImageResource(R.drawable.ic_temperature);
            holder.amountIcon.setVisibility(View.VISIBLE);
        } else if (record.getActivitiesId() == 20) {
            holder.statisticsDetailTx.setText(getTimeDiaperStatistics(20));
            holder.amountIcon.setVisibility(View.GONE);
        } else {
            holder.statisticsDetailTx.setText("");
            holder.amountIcon.setVisibility(View.GONE);
        }

    }

    public void setInitValue() {

        volumeUnit = localDataHelper.getVolumeUnit();
        temperatureUnit = localDataHelper.getTemperatureUnit();
        whhUnit = localDataHelper.getWhhUnit();
        isDarkmode = localDataHelper.getDarkMode();
    }

    private String getBreedDurationStatistics() {
        String breedStatistic = "";
        long totalMillisecond = 0;

        List<Record> listRecord = new ArrayList<>();
        listRecord.addAll(db.getAllRecordsByOption(currentUser.getUserId(), 1, today.firstDateString, today.secondDateString, "Left"));

        List<Date> listDate = new ArrayList<>();
        long totalMillisecondLeft = 0;

        List<Record> listRecordRight = new ArrayList<>();
        listRecordRight.addAll(db.getAllRecordsByOption(currentUser.getUserId(), 1, today.firstDateString, today.secondDateString, "Right"));

        List<Date> listDateRight = new ArrayList<>();
        int totalMillisecondRight = 0;


        //this is list of duration by left side
        for (int i = 0; i < listRecord.size(); i++) {
            listDate.add(listRecord.get(i).getDuration());
        }
        //this is list of duration by right side
        for (int i = 0; i < listRecordRight.size(); i++) {
            listDateRight.add(listRecordRight.get(i).getDuration());
        }

        totalMillisecondLeft = fh.getSumDuration(listDate);
        totalMillisecondRight = fh.getSumDuration(listDateRight);
        totalMillisecond = totalMillisecondLeft + totalMillisecondRight;


        String totalDurationLeft = fh.convertMillisecondToMinFullFormat(totalMillisecondLeft);
        String totalDurationRight = fh.convertMillisecondToMinFullFormat(totalMillisecondRight);
        String totalDuration = fh.convertMillisecondToMinFullFormat(totalMillisecond);

        breedStatistic = totalDuration + " " + mContext.getResources().getString(R.string.left) + ": " + totalDurationLeft + " " + mContext.getResources().getString(R.string.right) + ": " + totalDurationRight;

        return breedStatistic;
    }

    private String getDurationStatistics(int activityID) {
        String breedStatistic = "";
        //long totalMillisecond = 0;

        List<Record> listRecord = new ArrayList<>();
        listRecord.addAll(db.getAllRecordsByOption(currentUser.getUserId(), activityID, today.firstDateString, today.secondDateString, ""));

        List<Date> listDate = new ArrayList<>();
        long totalMillisecond = 0;


        //this is list of duration by left side
        for (int i = 0; i < listRecord.size(); i++) {
            listDate.add(listRecord.get(i).getDuration());
        }


        totalMillisecond = fh.getSumDuration(listDate);
        String totalDuration = fh.convertMillisecondToMinFullFormat(totalMillisecond);

        breedStatistic = totalDuration;

        return breedStatistic;
    }


    private String getPumpVolumeStatistics() {

        String pumpStatistic = "";

        Calculator cc = new Calculator();

        List<Record> listRecord = new ArrayList<>();
        listRecord.addAll(db.getAllRecordsByOption(currentUser.getUserId(), 2, today.firstDateString, today.secondDateString, "Left"));
        List<Double> listVolumeLeft = new ArrayList<>();
        double totalVolume = 0;

        double totalVolumeLeft = 0;

        for (int i = 0; i < listRecord.size(); i++) {

            if (listRecord.get(i).getAmountUnit().equals(volumeUnit)) {
                listVolumeLeft.add(listRecord.get(i).getAmount());
            } else {
                if (volumeUnit.equals("ml")) {
                    listVolumeLeft.add(cc.convertOzMl(listRecord.get(i).getAmount()));
                } else {
                    listVolumeLeft.add(cc.convertMlOz(listRecord.get(i).getAmount()));
                }
            }

        }

        totalVolumeLeft = fh.getSumVolume(listVolumeLeft);
        BigDecimal bd = new BigDecimal(totalVolumeLeft).setScale(2, RoundingMode.HALF_UP);
        totalVolumeLeft = bd.doubleValue();

//start right record
        List<Record> listRecordRight = new ArrayList<>();
        listRecordRight.addAll(db.getAllRecordsByOption(currentUser.getUserId(), 2, today.firstDateString, today.secondDateString, "Right"));
        List<Double> listVolumeRight = new ArrayList<>();
        double totalVolumeRight = 0;

        for (int i = 0; i < listRecordRight.size(); i++) {

            if (listRecordRight.get(i).getAmountUnit().equals(volumeUnit)) {
                listVolumeRight.add(listRecordRight.get(i).getAmount());
            } else {
                if (volumeUnit.equals("ml")) {
                    listVolumeRight.add(cc.convertOzMl(listRecordRight.get(i).getAmount()));
                } else {
                    listVolumeRight.add(cc.convertMlOz(listRecordRight.get(i).getAmount()));
                }
            }

        }

        totalVolumeRight = fh.getSumVolume(listVolumeRight);

        BigDecimal bd2 = new BigDecimal(totalVolumeRight).setScale(2, RoundingMode.HALF_UP);
        totalVolumeRight = bd2.doubleValue();

        totalVolume = totalVolumeLeft + totalVolumeRight;


        pumpStatistic = totalVolume + volumeUnit + " " + mContext.getResources().getString(R.string.left) + ": " + totalVolumeLeft + volumeUnit + " " + mContext.getResources().getString(R.string.right) + ": " + totalVolumeRight + volumeUnit;
//        entries3.add(new PieEntry((float) totalVolumeLeft, totalVolumeLeft + volumeUnit));
//        entries3.add(new PieEntry((float) totalVolumeRight, totalVolumeRight + volumeUnit));


        return pumpStatistic;

    }

    private String getVolumeStatistics(int activityID) {

        String pumpStatistic = "";

        Calculator cc = new Calculator();

        List<Record> listRecord = new ArrayList<>();
        listRecord.addAll(db.getAllRecordsByOption(currentUser.getUserId(), activityID, today.firstDateString, today.secondDateString, ""));
        List<Double> listVolume = new ArrayList<>();
        double totalVolume = 0;

        // double totalVolumeLeft = 0;

        for (int i = 0; i < listRecord.size(); i++) {

            if (listRecord.get(i).getAmountUnit().equals(volumeUnit)) {
                listVolume.add(listRecord.get(i).getAmount());
            } else {
                if (volumeUnit.equals("ml")) {
                    listVolume.add(cc.convertOzMl(listRecord.get(i).getAmount()));
                } else {
                    listVolume.add(cc.convertMlOz(listRecord.get(i).getAmount()));
                }
            }

        }

        totalVolume = fh.getSumVolume(listVolume);
        BigDecimal bd = new BigDecimal(totalVolume).setScale(2, RoundingMode.HALF_UP);
        totalVolume = bd.doubleValue();

        pumpStatistic = totalVolume + volumeUnit;
//        entries3.add(new PieEntry((float) totalVolumeLeft, totalVolumeLeft + volumeUnit));
//        entries3.add(new PieEntry((float) totalVolumeRight, totalVolumeRight + volumeUnit));

        return pumpStatistic;

    }

    private String getTemperatureStatistics() {
        String temperatureStatics = "";

        Calculator cc = new Calculator();

        List<Record> listRecord = new ArrayList<>();
        listRecord.addAll(db.getAllRecordsByOption(currentUser.getUserId(), 16, today.firstDateString, today.secondDateString, ""));
        List<Double> listVolume = new ArrayList<>();
        double maxAmount = 0;

        for (int i = 0; i < listRecord.size(); i++) {

            if (listRecord.get(i).getAmountUnit().equals(temperatureUnit)) {
                listVolume.add(listRecord.get(i).getAmount());
            } else {
                if (temperatureUnit.equals("c")) {
                    listVolume.add(cc.convertFtoC(listRecord.get(i).getAmount()));
                } else {
                    listVolume.add(cc.convertCtoF(listRecord.get(i).getAmount()));
                }
            }

        }


        if (listVolume.size() > 0) {
            maxAmount = Collections.max(listVolume);
        }
        temperatureStatics = mContext.getResources().getString(R.string.max_tem) + ": " + maxAmount + temperatureUnit;


        return temperatureStatics;

    }


    private String getTimeDiaperStatistics(int activityID) {
        String timeStatistics = "";

        List<Stats> statsList = new ArrayList<>();
        statsList.addAll(db.getAllStartsByTimes(currentUser.getUserId(), activityID, today.firstDateString, today.secondDateString));


        int totalTimes = 0;
        int wetTimes = 0;
        int dirtyTimes = 0;
        int mixedTimes = 0;

        for (int i = 0; i < statsList.size(); i++) {
            if (statsList.get(i).getStatsOption().equals("Wet")) {
                wetTimes = statsList.get(i).getStatsTime();
            } else if (statsList.get(i).getStatsOption().equals("Dirty")) {
                dirtyTimes = statsList.get(i).getStatsTime();
            } else if (statsList.get(i).getStatsOption().equals("Mixed")) {
                mixedTimes = statsList.get(i).getStatsTime();
            }
        }
        totalTimes = wetTimes + dirtyTimes + mixedTimes;

        timeStatistics = mContext.getResources().getString(R.string.wet) + ": " + wetTimes + " " + mContext.getResources().getString(R.string.dirty) + ": " + dirtyTimes + " " + mContext.getResources().getString(R.string.mixed) + ": " + mixedTimes;

        return timeStatistics;

    }

    private String getTimeBathStatistics() {
        String timeStatistics = "";

        List<Stats> statsList = new ArrayList<>();
        statsList.addAll(db.getAllStartsByTimes(currentUser.getUserId(), 7, today.firstDateString, today.secondDateString));


        int totalTimes = 0;
        int wetTimes = 0;
        int dirtyTimes = 0;
        int mixedTimes = 0;

        for (int i = 0; i < statsList.size(); i++) {
            if (statsList.get(i).getStatsOption().equals("Bath")) {
                wetTimes = statsList.get(i).getStatsTime();
            } else if (statsList.get(i).getStatsOption().equals("Hair Wash")) {
                dirtyTimes = statsList.get(i).getStatsTime();
            } else if (statsList.get(i).getStatsOption().equals("Both")) {
                mixedTimes = statsList.get(i).getStatsTime();
            }
        }
        totalTimes = wetTimes + dirtyTimes + mixedTimes;

        timeStatistics = mContext.getResources().getString(R.string.bath_name) + ": " + wetTimes + " " + mContext.getResources().getString(R.string.hair_wash) + ": " + dirtyTimes + " " + mContext.getResources().getString(R.string.both) + ": " + mixedTimes;

        return timeStatistics;

    }


    @Override
    public int getItemCount() {
        return recordsList.size();
    }


}

