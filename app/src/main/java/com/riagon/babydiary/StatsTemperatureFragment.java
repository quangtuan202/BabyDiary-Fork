package com.riagon.babydiary;

import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.Stats;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.Calculator;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class StatsTemperatureFragment extends Fragment {

    private PieChart pieChartTimes;
    private PieChart pieChartMaxTem;
    private PieChart pieChartVolume;
    private BarChart barChart;
    private LinearLayout piechartLayout1,piechartLayout2,piechartLayout3;
    private CombinedChart mChart;
    private TextView stats_period_total, stats_day_average, stats_period, stats_day, stats_no_of_time, stats_duration, stats_volume;
    private DatabaseHelper db;
    private DateFormatUtility df;
    private FormHelper fh;
    private LocalDataHelper ld;
    private String volumeUnit;
    private int periodDays = 7;
    private int activityId = 16;
    private int timesCount;

    private static final String BY_PERIOD = "by_period";
    private static final String BY_DAY = "by_day";

    private static final String BY_TIME = "by_time";
    private static final String BY_DURATION = "by_duration";
    private static final String BY_VOLUME = "by_volume";

    // private static final String RANGE_BY_PICKER = "Picker";
    private static final String RANGE_7_DAYS = "7Days";
    private static final String RANGE_14_DAYS = "14Days";
    private static final String RANGE_30_DAYS = "30Days";

    private String startDate;
    private String endDate;
    public String startDateShow;
    public String endDateShow;
    private String range_option;
    private String period_option;
    private String type_option;
    public Button left, right,icon_date;
    public TextView text_selected;
    public TextView tx_cancle;
    public TextView text1;
    public TextView text2;
    public TextView text3;
    public TextView text4;
    public TextView text5;
    public TextView text6;
    public TextView totalActivities;

    public FormHelper formHelper;
    public SettingHelper settingHelper;
    public User currentUser;
    public int days;

    public StatsTemperatureFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_stats_temperature, container, false);

        db = new DatabaseHelper(getContext());
        df = new DateFormatUtility(getContext());
        fh = new FormHelper(getContext());
        ld = new LocalDataHelper(getContext());
        volumeUnit = ld.getTemperatureUnit();
        initview(view);
        initValue();

        //Create chart by default
        refreshChart();
        icon_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationPickerDialog();
            }
        });

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        MaterialDatePicker.Builder<Pair<Long, Long>> builderRange = MaterialDatePicker.Builder.dateRangePicker();
        builderRange.setCalendarConstraints(limitRange().build());
        builderRange.setTitleText(getResources().getString(R.string.select_range_date));
        final MaterialDatePicker<Pair<Long, Long>> pickerRange = builderRange.build();

        text_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  DurationPickerDialog1();
                pickerRange.show(getFragmentManager(), pickerRange.toString());

            }
        });

        pickerRange.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                androidx.core.util.Pair date = (androidx.core.util.Pair) selection;


                String longFirst = date.first.toString();
                long millisecondFirst = Long.parseLong(longFirst);
                String longSecond = date.second.toString();
                long millisecondSecond = Long.parseLong(longSecond);

                startDateShow = new SimpleDateFormat("dd-MM-yyyy").format(new Date(millisecondFirst));
                endDateShow = new SimpleDateFormat("dd-MM-yyyy").format(new Date(millisecondSecond));


                startDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(millisecondFirst));
                endDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(millisecondSecond));

                text_selected.setText(startDateShow + " - " + endDateShow);

                //Refresh Charts
                refreshChart();

                days = formHelper.getDaysDiff(startDate, endDate);

                //   Log.i("TAGLOG", "Day: " + days);
            }
        });


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DateFormatUtility df = new DateFormatUtility();

                FormHelper fh = new FormHelper();

                startDate = fh.minusDate(startDate, days);

                endDate = fh.minusDate(endDate, days);
//set humand read format
                startDateShow = df.getStringDateFormat(df.getDateFormat2(startDate));
                endDateShow = df.getStringDateFormat(df.getDateFormat2(endDate));
                text_selected.setText(startDateShow + " - " + endDateShow);

                //Refresh Charts
                refreshChart();


            }
        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();

                DateFormatUtility df = new DateFormatUtility();

                FormHelper fh = new FormHelper();

                startDate = fh.addDate(startDate, days);

                endDate = fh.addDate(endDate, days);

//set humand read format
                startDateShow = df.getStringDateFormat(df.getDateFormat2(startDate));
                endDateShow = df.getStringDateFormat(df.getDateFormat2(endDate));
                text_selected.setText(startDateShow + " - " + endDateShow);

                //Refresh Charts
                refreshChart();

            }
        });




        // Inflate the layout for this fragment
        return view;
    }



    private void refreshChart() {
        showTimePieChart(period_option);
        //showDurationPieChart(period_option);
        showVolumePieChart(period_option);
        showMaxAmountPieChart(period_option);
        showBarChart();
        showCombinedChart(type_option);
        showSevenDays();
        showChartByOption();
    }


    // dateRangePicker not selected date future
    private CalendarConstraints.Builder limitRange() {

        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();

        int year = 2019;
        int startMonth = 6;
        int startDate = 5;

        calendarStart.set(year, startMonth - 1, startDate - 1);

        // calendarStart.add(Calendar.DAY_OF_YEAR,1);

        long minDate = calendarStart.getTimeInMillis();
        long maxDate = calendarEnd.getTimeInMillis();


        constraintsBuilderRange.setStart(minDate);
        constraintsBuilderRange.setEnd(maxDate);
        constraintsBuilderRange.setValidator(new LogFragment.RangeValidator(minDate, maxDate));

        return constraintsBuilderRange;
    }


    static class RangeValidator implements CalendarConstraints.DateValidator {

        long minDate, maxDate;

        RangeValidator(long minDate, long maxDate) {
            this.minDate = minDate;
            this.maxDate = maxDate;
        }

        RangeValidator(Parcel parcel) {
            minDate = parcel.readLong();
            maxDate = parcel.readLong();
        }

        @Override
        public boolean isValid(long date) {
            return !(minDate > date || maxDate < date);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(minDate);
            dest.writeLong(maxDate);
        }

        public static final Parcelable.Creator<LogFragment.RangeValidator> CREATOR = new Parcelable.Creator<LogFragment.RangeValidator>() {

            @Override
            public LogFragment.RangeValidator createFromParcel(Parcel parcel) {
                return new LogFragment.RangeValidator(parcel);
            }

            @Override
            public LogFragment.RangeValidator[] newArray(int size) {
                return new LogFragment.RangeValidator[size];
            }
        };


    }

    private void DurationPickerDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.drop_down_list, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        tx_cancle = (TextView) alertLayout.findViewById(R.id.tx_cancle);
        text1 = (TextView) alertLayout.findViewById(R.id.text1);
        text2 = (TextView) alertLayout.findViewById(R.id.text2);
        text3 = (TextView) alertLayout.findViewById(R.id.text3);
        text4 = (TextView) alertLayout.findViewById(R.id.text4);
        text5 = (TextView) alertLayout.findViewById(R.id.text5);
        text6 = (TextView) alertLayout.findViewById(R.id.text6);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();

        settingHelper.setTextColorDialog(getContext(), tx_cancle, currentUser.getUserTheme());

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //get record within days

                Calendar date = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

                endDate = dateFormat.format(date.getTime());
                endDateShow = dateFormat2.format(date.getTime());
                date.add(Calendar.DAY_OF_MONTH, -6);

                startDate = dateFormat.format(date.getTime());
                startDateShow = dateFormat2.format(date.getTime());
                days = 7;

                text_selected.setText(startDateShow + " - " + endDateShow);

                //Refresh Charts
                refreshChart();
                //text_selected.setText(dateStart + " - " + dateEnd);
                dialog.dismiss();

            }
        });


        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar date = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
                endDate = dateFormat.format(date.getTime());
                endDateShow = dateFormat2.format(date.getTime());
                date.add(Calendar.DAY_OF_MONTH, -13);

                startDate = dateFormat.format(date.getTime());
                startDateShow = dateFormat2.format(date.getTime());
                days = 14;

                text_selected.setText(startDateShow + " - " + endDateShow);

                refreshChart();


                dialog.dismiss();

            }
        });

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//get record within 30 days

                Calendar date = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

                endDate = dateFormat.format(date.getTime());
                endDateShow = dateFormat2.format(date.getTime());
                date.add(Calendar.DAY_OF_MONTH, -29);

                startDate = dateFormat.format(date.getTime());
                startDateShow = dateFormat2.format(date.getTime());
                days = 30;

                text_selected.setText(startDateShow + " - " + endDateShow);

                refreshChart();

                dialog.dismiss();

            }
        });

        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "this month", Toast.LENGTH_SHORT).show();
//get record for this month
                Calendar date = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

                endDate = dateFormat.format(date.getTime());
                endDateShow = dateFormat2.format(date.getTime());
                date.setTime(date.getTime());
                date.set(Calendar.DATE, 1);
                startDate = dateFormat.format(date.getTime());
                startDateShow = dateFormat2.format(date.getTime());
                days = formHelper.getDaysDiff(startDate, endDate);

                text_selected.setText(startDateShow + " - " + endDateShow);

                refreshChart();

                // text_selected.setText(dateStart + " - " + dateEnd);
                dialog.dismiss();

            }
        });

        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get all record last month
                //  Toast.makeText(getContext(), "last month", Toast.LENGTH_SHORT).show();

                Calendar date = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

                date.setTime(date.getTime());
                date.set(Calendar.DATE, 1);
                date.add(Calendar.MONTH, -1);
                startDate = dateFormat.format(date.getTime());
                startDateShow = dateFormat2.format(date.getTime());

                Calendar cal = Calendar.getInstance();
                cal.setTime(cal.getTime());
                cal.add(Calendar.MONTH, -1);
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

                endDate = dateFormat.format(cal.getTime());
                endDateShow = dateFormat2.format(cal.getTime());

                days = formHelper.getDaysDiff(startDate, endDate);
                text_selected.setText(startDateShow + " - " + endDateShow);

                refreshChart();

                dialog.dismiss();

            }
        });

        text6.setVisibility(View.GONE);


        tx_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public void initview(View view) {
        pieChartTimes = (PieChart) view.findViewById(R.id.piechart_times);
        pieChartMaxTem = (PieChart) view.findViewById(R.id.piechart_max_tem);
        pieChartVolume = (PieChart) view.findViewById(R.id.piechart_volume);
        barChart = (BarChart) view.findViewById(R.id.barChart);
        mChart = (CombinedChart) view.findViewById(R.id.combineChart);
        stats_period_total = (TextView) view.findViewById(R.id.stats_period_total);
        stats_day_average = (TextView) view.findViewById(R.id.stats_day_average);
//        stats_period = (TextView) view.findViewById(R.id.stats_period);
//        stats_day = (TextView) view.findViewById(R.id.stats_day);
        stats_no_of_time = (TextView) view.findViewById(R.id.stats_no_of_time);
        stats_duration = (TextView) view.findViewById(R.id.stats_duration);
        stats_volume = (TextView) view.findViewById(R.id.stats_volume);
        text_selected = (TextView) view.findViewById(R.id.text_selected);
        icon_date = (Button) view.findViewById(R.id.icon_date);
        left = (Button) view.findViewById(R.id.left);
        right = (Button) view.findViewById(R.id.right);
        totalActivities = view.findViewById(R.id.total_activities_tx);
        piechartLayout1=view.findViewById(R.id.piechart1_layout);
        piechartLayout2=view.findViewById(R.id.piechart2_layout);
        piechartLayout3=view.findViewById(R.id.piechart3_layout);

        //dateRangeCalendarView = (DateRangeCalendarView) view.findViewById(R.id.calendar);
        formHelper = new FormHelper(getContext());
        settingHelper = new SettingHelper(getContext());
        currentUser = db.getUser(ld.getActiveUserId());

        CustomMarkerView mv = new CustomMarkerView(getContext(), R.layout.custom_marker_view_layout);
        mChart.setHighlightFullBarEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDrawMarkers(true);
        mChart.setMarker(mv);

    }

    public void initValue() {
        period_option = BY_PERIOD;
        range_option = RANGE_7_DAYS;
        type_option = BY_TIME;

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        endDate = dateFormat.format(calendar.getTime());

        if (range_option.equals(RANGE_7_DAYS)) {

            Calendar date = Calendar.getInstance();
            //  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
            endDate = dateFormat.format(date.getTime());
            endDateShow = dateFormat2.format(date.getTime());
            date.add(Calendar.DAY_OF_MONTH, -6);

            startDate = dateFormat.format(date.getTime());
            startDateShow = dateFormat2.format(date.getTime());

            days = 7;
            text_selected.setText(startDateShow + " - " + endDateShow);

        } else if (range_option.equals(RANGE_14_DAYS)) {


            Calendar date = Calendar.getInstance();
            //  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
            endDate = dateFormat.format(date.getTime());
            endDateShow = dateFormat2.format(date.getTime());
            date.add(Calendar.DAY_OF_MONTH, -13);

            startDate = dateFormat.format(date.getTime());
            startDateShow = dateFormat2.format(date.getTime());

            days = 14;
            text_selected.setText(startDateShow + " - " + endDateShow);


        } else if (range_option.equals(RANGE_30_DAYS)) {
            Calendar date = Calendar.getInstance();
            //  DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
            endDate = dateFormat.format(date.getTime());
            endDateShow = dateFormat2.format(date.getTime());
            date.add(Calendar.DAY_OF_MONTH, -29);

            startDate = dateFormat.format(date.getTime());
            startDateShow = dateFormat2.format(date.getTime());

            days = 30;
            text_selected.setText(startDateShow + " - " + endDateShow);
        }


    }


    public void showTimePieChart(String period_option) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries = getPieEnteriesTime(entries, period_option);

        PieDataSet dataSet = new PieDataSet(entries, "Times");
        setPieDataSetSetting(dataSet);

        PieData pieData = new PieData(dataSet);
        pieData.setValueTextColor(Color.WHITE);
        pieData.setValueFormatter(new PercentFormatter(pieChartTimes));

        setPieChartSetting(pieChartTimes, pieData);

    }


    public void showVolumePieChart(String period_option) {

        ArrayList<PieEntry> entries3 = new ArrayList<PieEntry>();
        entries3 = getPieEnteriesTemperature(entries3, period_option);

        PieDataSet dataSet3 = new PieDataSet(entries3, "Temperature");
        setPieDataSetSetting(dataSet3);

        PieData pieData3 = new PieData(dataSet3);
        pieData3.setValueTextSize(8f);
        pieData3.setValueTextColor(Color.WHITE);
        pieData3.setValueFormatter(new PercentFormatter(pieChartVolume));

        setPieChartSetting(pieChartVolume, pieData3);

    }


    public void showMaxAmountPieChart(String period_option) {

        ArrayList<PieEntry> entries4 = new ArrayList<PieEntry>();
        entries4 = getPieEnteriesMaxTemperature(entries4, period_option);

        PieDataSet dataSet4 = new PieDataSet(entries4, "Max Temperature");
        setPieDataSetSetting(dataSet4);

        PieData pieData4 = new PieData(dataSet4);
        pieData4.setValueTextSize(8f);
        pieData4.setValueTextColor(Color.WHITE);
        pieData4.setValueFormatter(new PercentFormatter(pieChartMaxTem));

        setPieChartSetting(pieChartMaxTem, pieData4);

    }



    public void setPieDataSetSetting(PieDataSet dataSet) {
        dataSet.setXValuePosition(PieDataSet.ValuePosition.INSIDE_SLICE);
        dataSet.setValueLinePart1OffsetPercentage(80.f);
        dataSet.setValueLinePart1Length(0.2f);
        dataSet.setValueLinePart2Length(0.4f);
        dataSet.setSelectionShift(3f);
        dataSet.setColors(getResources().getColor(R.color.cvTemperatureRed));
        dataSet.setValueTextSize(8f);
    }

    public void setPieChartSetting(PieChart pieChart, PieData pieData) {
        pieChart.setUsePercentValues(true);
        pieChart.setData(pieData);
        pieChart.setDrawEntryLabels(false);
        pieChart.setRotationEnabled(false);
        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0);
        pieChart.setDrawEntryLabels(true);
        pieChart.setEntryLabelTextSize(8f);
        pieChart.getLegend().setEnabled(false); // Hide the legend
        pieChart.getDescription().setEnabled(false); // Hide the description
        pieChart.notifyDataSetChanged();
        pieChart.invalidate();

    }

    private ArrayList<PieEntry> getPieEnteriesTime(ArrayList<PieEntry> entries, String period_option) {

//        entries.add(new PieEntry(2f, "2 times"));
//        entries.add(new PieEntry(3f, "3 times"));
        //  entries.add(new PieEntry(50f, "5 times"));
        List<Stats> statsList = new ArrayList<>();
        statsList.addAll(db.getAllStartsByTimes(currentUser.getUserId(), activityId, startDate, endDate));

        if (period_option.equals(BY_PERIOD)) {
            periodDays = 1;
        } else {
            periodDays = fh.getDaysDiff(startDate, endDate) + 1;
        }
        if (statsList.size() > 0) {
            timesCount =  statsList.get(0).getStatsTime();
            double time = (double) statsList.get(0).getStatsTime() / periodDays;
            BigDecimal bd2 = new BigDecimal(time).setScale(2, RoundingMode.HALF_UP);
            time = bd2.doubleValue();
            entries.add(new PieEntry((float) time,  time + " "+getResources().getString(R.string.times)));
        } else {
            double time = 0;
            entries.add(new PieEntry((float) time,  time + " "+getResources().getString(R.string.times)));
        }

        return entries;
    }


    private ArrayList<PieEntry> getPieEnteriesTemperature(ArrayList<PieEntry> entries3, String period_option) {
        Calculator cc = new Calculator();

//        if (period_option.equals(BY_PERIOD)) {
//            periodDays = 1;
//        } else {
//            periodDays = fh.getDaysDiff(startDate, endDate) + 1;
//        }

        List<Record> listRecord = new ArrayList<>();
        listRecord.addAll(db.getAllRecordsByOption(currentUser.getUserId(), activityId, startDate, endDate, ""));
        List<Double> listVolume = new ArrayList<>();
        double totalVolumeLeft = 0;

        for (int i = 0; i < listRecord.size(); i++) {

            if (listRecord.get(i).getAmountUnit().equals(volumeUnit)) {
                listVolume.add(listRecord.get(i).getAmount());
            } else {
                if (volumeUnit.equals("c")) {
                    listVolume.add(cc.convertFtoC(listRecord.get(i).getAmount()));
                } else {
                    listVolume.add(cc.convertCtoF(listRecord.get(i).getAmount()));
                }
            }

        }

        totalVolumeLeft = fh.getSumVolume(listVolume) / timesCount;
        
        if (totalVolumeLeft>0)
        {
            BigDecimal bd = new BigDecimal(totalVolumeLeft).setScale(2, RoundingMode.HALF_UP);
            totalVolumeLeft = bd.doubleValue();
        }


        entries3.add(new PieEntry((float) totalVolumeLeft, totalVolumeLeft + volumeUnit));

//        entries3.add(new PieEntry(30f, "500 ml"));
//        entries3.add(new PieEntry(30f, "700 ml"));
        //    entries3.add(new PieEntry(40f, "4 times"));

        return entries3;
    }

    private ArrayList<PieEntry> getPieEnteriesMaxTemperature(ArrayList<PieEntry> entries4, String period_option) {
        Calculator cc = new Calculator();
        List<Record> listRecord = new ArrayList<>();
        listRecord.addAll(db.getAllRecordsByOption(currentUser.getUserId(), activityId, startDate, endDate, ""));
        List<Double> listVolume = new ArrayList<>();
        double totalVolumeLeft = 0;

        for (int i = 0; i < listRecord.size(); i++) {

            if (listRecord.get(i).getAmountUnit().equals(volumeUnit)) {
                listVolume.add(listRecord.get(i).getAmount());
            } else {
                if (volumeUnit.equals("c")) {
                    listVolume.add(cc.convertFtoC(listRecord.get(i).getAmount()));
                } else {
                    listVolume.add(cc.convertCtoF(listRecord.get(i).getAmount()));
                }
            }

        }


        if (listVolume.size()>0)
        {
            totalVolumeLeft=Collections.max(listVolume);
        }

        entries4.add(new PieEntry((float) totalVolumeLeft, totalVolumeLeft + volumeUnit));

//        entries3.add(new PieEntry(30f, "500 ml"));
//        entries3.add(new PieEntry(30f, "700 ml"));
        //    entries3.add(new PieEntry(40f, "4 times"));

        return entries4;
    }




    public void showBarChart() {

        BarDataSet barDataSet = new BarDataSet(getBarEntries(), getResources().getString(R.string.average_time_between_activities_title));
        barDataSet.setColors(ContextCompat.getColor(getContext(), R.color.cvTemperatureRed));
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.3f);
        barData.setDrawValues(false);
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(7);

        Legend l = barChart.getLegend();
        if (ld.getDarkMode()) {
            xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            l.setTextColor(Color.WHITE);
        }

        //final String[] months = new String[]{"1/5", "2/5", "3/5", "4/5", "5/5", "6/5", "7/5"};

        List<String> mDates = new ArrayList<>();
        periodDays = fh.getDaysDiff(startDate, endDate);

        mDates.add("");
        for (int i = 0; i < periodDays + 1; i++) {
            Calendar cal = Calendar.getInstance();
            Date endStartDate = df.getDateFormat2(startDate);
            cal.setTime(endStartDate);
            cal.add(Calendar.DATE, i);
            // SimpleDateFormat dateShowFormat = new SimpleDateFormat("d/M");
            SimpleDateFormat dateShowFormat = new SimpleDateFormat("d");
            String dateShow = dateShowFormat.format(cal.getTime());
            mDates.add(dateShow);
        }


        IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(mDates);
        xAxis.setGranularity(1f);
        xAxis.setValueFormatter(formatter);
        xAxis.setLabelCount(periodDays, false);
        YAxis rightAxis = barChart.getAxisRight();
//        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
        rightAxis.setValueFormatter(new MyAxisValueFormatter(BY_DURATION));
//
        YAxis leftAxis = barChart.getAxisLeft();
//        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
        leftAxis.setValueFormatter(new MyAxisValueFormatter(BY_DURATION));

        if (ld.getDarkMode()) {
            leftAxis.setTextColor(Color.WHITE);
            rightAxis.setTextColor(Color.WHITE);
            xAxis.setTextColor(Color.WHITE);
        }


        CustomMarkerView mv = new CustomMarkerView(getContext(), R.layout.custom_marker_view_layout);
        barChart.setHighlightFullBarEnabled(true);
        barChart.setTouchEnabled(true);
        barChart.setDrawMarkers(true);
        barChart.setMarker(mv);

        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getDescription().setEnabled(false);
        barChart.setData(barData);
        // barChart.animateY(5000);
        barChart.invalidate();
    }

    private ArrayList<BarEntry> getBarEntries() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        periodDays = fh.getDaysDiff(startDate, endDate);

        for (int i = 0; i < periodDays + 1; i++) {
            Calendar cal = Calendar.getInstance();
            Date endStartDate = df.getDateFormat2(startDate);
            cal.setTime(endStartDate);
            cal.add(Calendar.DATE, i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(cal.getTime());
            // entries.add(new BarEntry(i + 1, sumTimesByDay(date, "Right")));
            entries.add(new BarEntry(i + 1, (float) sumDurationsBetweenByDay(date)));

        }
//        entries.add(new BarEntry(0f, 2.3f));
//        entries.add(new BarEntry(1f, 3f));
//        entries.add(new BarEntry(2f, 2.8f));
//        entries.add(new BarEntry(3f, 3.5f));
//        entries.add(new BarEntry(4f, 4f));
//        entries.add(new BarEntry(5f, 3.5f));
//        entries.add(new BarEntry(6f, 1.5f));
        return entries;
    }


    public void showSevenDays() {

        for (int i = 0; i < 7; i++) {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(cal.getTime());

            SimpleDateFormat dateShowFormat = new SimpleDateFormat("d/M");
            String dateShow = dateShowFormat.format(cal.getTime());

            //int dayofWeek = cal.get (Calendar.DATE);
            // Log.i("TAG2","day "+i);

            Log.i("TAG2", "day " + date);
//            Log.i("TAG2", "Date title " + dateShow);
//            Log.i("TAG2", "Left Times: " + sumTimesByDay(date, "Left"));
//            Log.i("TAG2", "Right Times: " + sumTimesByDay(date, "Right"));
//            Log.i("TAG2", "Left Times: " + sumDurationByDay(date, "Left"));
//            Log.i("TAG2", "Right Times: " + sumDurationByDay(date, "Right"));
            //     Log.i("TAG2", "Left Times: " + sumVolumeByDay(date, "Left"));
            //  Log.i("TAG2", "Right Times: " + sumVolumeByDay(date, "Right"));

            Log.i("TAG2", "Time Between " + sumDurationsBetweenByDay(date));

        }
        //  Log.i("TAG2", "Day Diff: " + fh.getDaysDiff("2020-05-01", "2020-05-08"));
        //Log.i("TAG2", "Start date: " + startDate+" End date: "+endDate+" Days: "+periodDays);
    }

    public int sumTimesByDay(String date, String option) {
        int sumTimeByDay = 0;

        List<Record> recordsList = new ArrayList<>();
        recordsList.addAll(db.getAllRecordsByDay(currentUser.getUserId(), activityId, date, option));

        sumTimeByDay = recordsList.size();

        return sumTimeByDay;

    }


    public double sumVolumeByDay(String date, String option) {
        double sumVolumeByDay = 0;
        Calculator cc = new Calculator();
        List<Record> recordsList = new ArrayList<>();
        recordsList.addAll(db.getAllRecordsByDay(currentUser.getUserId(), activityId, date, option));

        List<Double> listVolume = new ArrayList<>();

        for (int i = 0; i < recordsList.size(); i++) {

            if (recordsList.get(i).getAmountUnit().equals(volumeUnit)) {
                listVolume.add(recordsList.get(i).getAmount());
            } else {
                if (volumeUnit.equals("c")) {
                    listVolume.add(cc.convertFtoC(recordsList.get(i).getAmount()));
                } else {
                    listVolume.add(cc.convertCtoF(recordsList.get(i).getAmount()));
                }
            }

        }


//        sumVolumeByDay = fh.getSumVolume(listVolume);

        if (listVolume.size()>0)
        {
            sumVolumeByDay= Collections.max(listVolume);
        }

       // BigDecimal bd = new BigDecimal(sumVolumeByDay).setScale(2, RoundingMode.HALF_UP);
     //   sumVolumeByDay = bd.doubleValue();

        return sumVolumeByDay;

    }




    public double sumDurationsBetweenByDay(String date) {
        double sumDurationBetweenByDay = 0;

        List<Record> recordsList = new ArrayList<>();
        recordsList.addAll(db.getAllRecordsByDay(currentUser.getUserId(), activityId, date));

        List<Long> listDate = new ArrayList<>();
        long totalMillisecond = 0;

        //this is list of duration by left side
        for (int i = 0; i < recordsList.size(); i++) {
            long millisecondsChange;
            //   int durationBetweenByHours=0;
            if (i < recordsList.size() - 1) {

                String timePos1 = fh.getDateWithTimeFormat(df.getStringDateFormat(recordsList.get(i).getDateEnd()), df.getStringTimeFormat(recordsList.get(i).getTimeEnd()));
                String timePos2 = fh.getDateWithTimeFormat(df.getStringDateFormat(recordsList.get(i + 1).getDateEnd()), df.getStringTimeFormat(recordsList.get(i + 1).getTimeEnd()));
                millisecondsChange = fh.timeDiffMilliseconds(timePos1, timePos2);

                // durationBetweenByHours=fh.convertToMillisecond(recordsList.get(i).getDuration())-fh.convertToMillisecond(recordsList.get(i+1).getDuration());
            } else {
                millisecondsChange = 0;
            }


            listDate.add(millisecondsChange);
            // Log.i("TAG", recordsList.get(i).getDuration() + " h");
        }

        for (int x = 0; x < listDate.size(); x++) {
            totalMillisecond = totalMillisecond + listDate.get(x);
        }

        if (listDate.size() > 1) {
            totalMillisecond = totalMillisecond / (listDate.size() - 1);
        }

        //  totalMillisecond = fh.sum(listDate);

        sumDurationBetweenByDay = fh.convertMillisecondToHours(totalMillisecond);

//        BigDecimal bd = new BigDecimal(sumDurationBetweenByDay).setScale(2, RoundingMode.HALF_UP);
//        sumDurationBetweenByDay = bd.doubleValue();

        return sumDurationBetweenByDay;

    }


    public void showCombinedChart(String type_option) {
        mChart.getDescription().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.setDrawBarShadow(false);


        CustomMarkerView mv = new CustomMarkerView(getContext(), R.layout.custom_marker_view_layout);
        mChart.setHighlightFullBarEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDrawMarkers(true);
        mChart.setMarker(mv);

        // draw bars behind lines
        mChart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE
        });
        Legend l = mChart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        if (ld.getDarkMode()) {
            l.setTextColor(Color.WHITE);
        }

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        rightAxis.setValueFormatter(new MyAxisValueFormatter(type_option));

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        leftAxis.setValueFormatter(new MyAxisValueFormatter(type_option));


        //This place bind data for date

        List<String> mDates = new ArrayList<>();
//        mDates.add("");
////        mDates.add("1/5");
////        mDates.add("2/5");
////        mDates.add("3/5");
////        mDates.add("4/5");
////        mDates.add("5/5");
////        mDates.add("6/5");
////        mDates.add("7/5");

        periodDays = fh.getDaysDiff(startDate, endDate);

        mDates.add("");
        for (int i = 0; i < periodDays + 1; i++) {
            Calendar cal = Calendar.getInstance();
            Date endStartDate = df.getDateFormat2(startDate);
            cal.setTime(endStartDate);
            cal.add(Calendar.DATE, i);
            // SimpleDateFormat dateShowFormat = new SimpleDateFormat("d/M");
            SimpleDateFormat dateShowFormat = new SimpleDateFormat("d");
            String dateShow = dateShowFormat.format(cal.getTime());
            mDates.add(dateShow);
        }

        // IndexAxisValueFormatter formatter = new IndexAxisValueFormatter(mDates);
        //xAxis.setGranularity(1f);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //  xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        xAxis.setTextSize(7);
        // xAxis.setValueFormatter(formatter);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return mDates.get((int) value % mDates.size());
            }
        });

        xAxis.setLabelCount(periodDays, false);

        if (ld.getDarkMode()) {
            leftAxis.setTextColor(Color.WHITE);
            rightAxis.setTextColor(Color.WHITE);
            xAxis.setTextColor(Color.WHITE);
        }


        CombinedData data = new CombinedData();

        data.setData(generateLineData(type_option));
        data.setData(generateCombinedBarData(type_option));
        //data.setHighlightEnabled(false);

        data.setDrawValues(false);

        mChart.setHighlightFullBarEnabled(false);
        xAxis.setAxisMaximum(data.getXMax() + 0.7f);
        xAxis.setAxisMinimum(0f);
        //xAxis.setAxisMinimum(data.getXMin()+1f);
        // xAxis.setCenterAxisLabels(true);
        mChart.getAxisLeft().setDrawGridLines(true);
        mChart.getXAxis().setDrawGridLines(false);
        mChart.setData(data);
        mChart.setData(data);
        mChart.invalidate();
    }


    private ArrayList<Entry> getLineEntriesData(ArrayList<Entry> entries, String type_option) {
        // entries.add(new Entry(0, 15));
//        entries.add(new Entry(1, 10));
//        entries.add(new Entry(2, 5));
//        entries.add(new Entry(3, 7));
//        entries.add(new Entry(4, 8));
//        entries.add(new Entry(5, 6));
//        entries.add(new Entry(6, 14));
//        entries.add(new Entry(7, 16));
        periodDays = fh.getDaysDiff(startDate, endDate);
        for (int i = 0; i < periodDays + 1; i++) {
            Calendar cal = Calendar.getInstance();
            Date endStartDate = df.getDateFormat2(startDate);
            cal.setTime(endStartDate);
            cal.add(Calendar.DATE, i);
            // cal.add(Calendar.DATE, -i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(cal.getTime());

            if (type_option.equals(BY_TIME)) {
                int totalTime = sumTimesByDay(date, "");
                entries.add(new Entry(i + 1, totalTime));
            } else if (type_option.equals(BY_VOLUME)) {
                double totalVolume = sumVolumeByDay(date, "");
                entries.add(new Entry(i + 1, (float) totalVolume));
            }


        }


        return entries;
    }

    private ArrayList<BarEntry> getCombinedBarEnteries(ArrayList<BarEntry> entries, String type_option) {
        periodDays = fh.getDaysDiff(startDate, endDate);

        for (int i = 0; i < periodDays + 1; i++) {
            Calendar cal = Calendar.getInstance();
            Date endStartDate = df.getDateFormat2(startDate);
            cal.setTime(endStartDate);
            cal.add(Calendar.DATE, i);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String date = dateFormat.format(cal.getTime());
            if (type_option.equals(BY_TIME)) {
                entries.add(new BarEntry(i + 1, sumTimesByDay(date, "")));
            } else if (type_option.equals(BY_VOLUME)) {
                entries.add(new BarEntry(i + 1, (float) sumVolumeByDay(date, "")));
            }

        }

//        entries.add(new BarEntry(1, 5));
//        entries.add(new BarEntry(2, 2));
//        entries.add(new BarEntry(3, 4));
//        entries.add(new BarEntry(4, 5));
//        entries.add(new BarEntry(5, 2));
//        entries.add(new BarEntry(6, 5));
//        entries.add(new BarEntry(7, 8));
        return entries;
    }


    private LineData generateLineData(String type_option) {

        LineData d = new LineData();

        ArrayList<Entry> entries = new ArrayList<Entry>();

        entries = getLineEntriesData(entries, type_option);

        LineDataSet set = new LineDataSet(entries, getResources().getString(R.string.total));

        set.setColors(ContextCompat.getColor(getContext(), R.color.darkerGrey));
        set.setLineWidth(2.5f);
        set.setCircleColor(ContextCompat.getColor(getContext(), R.color.darkerGrey));
        set.setDrawCircleHole(false);
        set.setCircleRadius(4f);
        set.setFillColor(ContextCompat.getColor(getContext(), R.color.darkerGrey));

        set.setDrawValues(false);
        set.setValueTextSize(8f);
        set.setValueTextColor(ContextCompat.getColor(getContext(), R.color.darkerGrey));

        set.setDrawValues(false);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        // showChartByOption();

        return d;
    }


    private BarData generateCombinedBarData(String type_option) {

        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        entries = getCombinedBarEnteries(entries, type_option);

        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();
        //  entries2 = getCombinedBarEnteries2(entries2, type_option);

        BarDataSet set1 = new BarDataSet(entries, getResources().getString(R.string.total));
        //   BarDataSet set2 = new BarDataSet(entries2, "Right");

        set1.setColors(ContextCompat.getColor(getContext(), R.color.cvTemperatureRed));
        set1.setValueTextColor(ContextCompat.getColor(getContext(), R.color.cvTemperatureRed));
        set1.setValueTextSize(8f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);


        float barWidth = 0.2f; // x2 dataset

        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        dataSets.add(set1);
        //  dataSets.add(set2);

        BarData d = new BarData(dataSets);
        // d.setValueFormatter(new PercentFormatter(new DecimalFormat("###,###,##0"));

        d.setBarWidth(barWidth);
        d.setDrawValues(false);
        return d;
    }

    public void showChartByOption() {

        stats_period_total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "period total", Toast.LENGTH_SHORT).show();
                piechartLayout2.setVisibility(View.VISIBLE);
                piechartLayout3.setVisibility(View.VISIBLE);
                showTimePieChart(BY_PERIOD);
                //   Log.i("TAG2", "Period: " + BY_PERIOD + " Start date: " + startDate + " End date: " + endDate);
                //  showDurationPieChart(BY_PERIOD);
                showVolumePieChart(BY_PERIOD);
                showMaxAmountPieChart(BY_PERIOD);
            }
        });

        stats_day_average.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePieChart(BY_DAY);
                piechartLayout2.setVisibility(View.GONE);
                piechartLayout3.setVisibility(View.GONE);
                // Log.i("TAG2", "Period: " + BY_PERIOD + " Start date: " + startDate + " End date: " + endDate);
                // showDurationPieChart(BY_DAY);
               // showVolumePieChart(BY_DAY);
                // Toast.makeText(getContext(), "day average", Toast.LENGTH_SHORT).show();
            }
        });

        stats_no_of_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //   Toast.makeText(getContext(), "no.of time", Toast.LENGTH_SHORT).show();
                showCombinedChart(BY_TIME);
            }
        });


        stats_volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "volume", Toast.LENGTH_SHORT).show();
                showCombinedChart(BY_VOLUME);
            }
        });

    }


    public String getAxisTitle(String typeOption) {
        String axisTile = "";
        if (typeOption.equals(BY_TIME)) {
            axisTile = "t";
        } else if (typeOption.equals(BY_DURATION)) {
            axisTile = "h";
        } else if (typeOption.equals(BY_VOLUME)) {
            if (volumeUnit.equals("c")) {
                axisTile = "c";
            } else {
                axisTile = "f";
            }

        }
        return axisTile;
    }


    public class MyAxisValueFormatter extends ValueFormatter {
        String typeOption;

        public MyAxisValueFormatter(String typeOption) {
            this.typeOption = typeOption;
        }

        @Override
        public String getFormattedValue(float value) {

            double tmp = (double) value;
            BigDecimal bd = new BigDecimal(tmp).setScale(2, RoundingMode.HALF_UP);
            tmp = bd.doubleValue();

            return tmp + " " + getAxisTitle(typeOption);
        }

    }


}
