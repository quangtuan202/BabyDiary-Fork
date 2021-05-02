package com.riagon.babydiary;


import android.app.AlertDialog;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.Timeline;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TimelineFragment extends Fragment {
    private CandleStickChart candleStickChart;
    private DatabaseHelper db;
    private FormHelper fh;
    private DateFormatUtility df;
    private LocalDataHelper localDataHelper;
    // private String startDate = "2020-05-31";
    private String startDate = "";
    //  private String endDate = "2020-06-17";
    private String endDate = "";
    private int periodDays = 0;
    public CheckBox cb_exit, cb_feed, cb_formula, cb_pump, cb_pumpbottle, cb_food, cb_diaper, cb_bath, cb_sleep, cb_tummy,
            cb_sunbathe, cb_play, cb_massage, cb_drink, cb_crying, cb_vaccination, cb_temperature, cb_med, cb_doctorvisit, cb_symptom, cb_potty;
    public Button icon_date;
    int mYear;
    int mMonth;
    int mDay;
    public TextView tx_cancle;
    public TextView text1, text2, text3, text4, text5, text6, text7, text8, text9, text10, text11, text12;
    public SettingHelper settingHelper;
    public FormHelper formHelper;
    public User currentUser;
    public String today;
    private List<Record> feedRecordList;
    private List<Integer> activityIDList = new ArrayList<>();
    private Boolean isFirstTime = true;
    private Boolean isFreshChart = false;
    private Boolean isDarkMode = false;

    public TimelineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        intentView(view);
        setChangeCheckBox();
        fh = new FormHelper(getContext());
        df = new DateFormatUtility(getContext());
        db = new DatabaseHelper(getContext());
        settingHelper = new SettingHelper(getContext());
        localDataHelper = new LocalDataHelper(getContext());
        formHelper = new FormHelper();
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        isDarkMode = localDataHelper.getDarkMode();
        //set up the activity ID list
        setInitActivityIdList();

        //set up init value for startdate and enddate

        endDate = getLastDayOfMonth(0);
        startDate = getFirstDayOfMonth(0);


        candleStickChart = view.findViewById(R.id.candleStickChart);
        //  candleStickChart.setBackgroundColor(getResources().getColor(R.color.lightGreyChart));
        candleStickChart.setBackgroundColor(getResources().getColor(R.color.opacityWhite90));
        candleStickChart.setBorderColor(getResources().getColor(R.color.opacityWhite90));
        candleStickChart.setGridBackgroundColor(getResources().getColor(R.color.opacityWhite90));

//        if (!isDarkMode) {
//       //     candleStickChart.setBackgroundColor(getResources().getColor(R.color.white));
//            candleStickChart.setGridBackgroundColor(getResources().getColor(R.color.white));
//            //candleStickChart.setBorderColor(getResources().getColor(R.color.lightGrey));
//        } else {
//          //  candleStickChart.setBackgroundColor(getResources().getColor(R.color.black));
//            candleStickChart.setGridBackgroundColor(getResources().getColor(R.color.black));
//          //  candleStickChart.setBorderColor(getResources().getColor(R.color.darkGrey));
//
//        }

        candleStickChart.setDrawGridBackground(true);
        candleStickChart.setDrawBorders(true);

        candleStickChart.setDragEnabled(true);
        candleStickChart.getDescription().setEnabled(false);
//candleStickChart.moveViewToX(18);

        CustomMarkerViewTimeline mvTimeline = new CustomMarkerViewTimeline(getContext(), R.layout.custom_marker_view_timeline_layout);
        candleStickChart.setHighlightPerTapEnabled(true);
        candleStickChart.setTouchEnabled(true);
        candleStickChart.setDrawMarkers(true);
        candleStickChart.setMarker(mvTimeline);
        candleStickChart.setMaxHighlightDistance(20);


//get current day to set view to
        String currentDate = fh.getDateNow2();
        //  String currentTime = fh.getTimeNow2();
        List<String> dates = formHelper.getArrayOfDate(startDate, endDate);
        double index = formHelper.getIndexOfDate(dates, currentDate);

        candleStickChart.moveViewToX((float) index);
        candleStickChart.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onGlobalLayout() {

                        candleStickChart.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        int offset = (candleStickChart.getHeight() - candleStickChart.getWidth()) / 2;

                        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) candleStickChart.getLayoutParams();
                        layoutParams.width = candleStickChart.getHeight();
                        layoutParams.height = candleStickChart.getWidth();
                        candleStickChart.setLayoutParams(layoutParams);

                        candleStickChart.setTranslationX(-offset);
                        candleStickChart.setTranslationY(offset);

                        initCandleStickChart();
                    }
                });


        icon_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationPickerDialog();
            }
        });

        return view;
    }


    private void DurationPickerDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.list_date, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        tx_cancle = (TextView) alertLayout.findViewById(R.id.tx_cancle);
        text1 = (TextView) alertLayout.findViewById(R.id.text1);
        text2 = (TextView) alertLayout.findViewById(R.id.text2);
        text3 = (TextView) alertLayout.findViewById(R.id.text3);
        text4 = (TextView) alertLayout.findViewById(R.id.text4);
        text5 = (TextView) alertLayout.findViewById(R.id.text5);
        text6 = (TextView) alertLayout.findViewById(R.id.text6);
        text7 = (TextView) alertLayout.findViewById(R.id.text7);
        text8 = (TextView) alertLayout.findViewById(R.id.text8);
        text9 = (TextView) alertLayout.findViewById(R.id.text9);
        text10 = (TextView) alertLayout.findViewById(R.id.text10);
        text11 = (TextView) alertLayout.findViewById(R.id.text11);
        text12 = (TextView) alertLayout.findViewById(R.id.text12);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        final AlertDialog dialog = alert.create();

        settingHelper.setTextColorDialog(getContext(), tx_cancle, currentUser.getUserTheme());


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM-yyyy", getContext().getResources().getConfiguration().locale);
        text1.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        text2.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        text3.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        text4.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        text5.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        text6.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        text7.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        text8.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        text9.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        text10.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        text11.setText(dateFormat.format(calendar.getTime()));
        calendar.add(Calendar.MONTH, -1);
        text12.setText(dateFormat.format(calendar.getTime()));


        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDate = getFirstDayOfMonth(0);
                endDate = getLastDayOfMonth(0);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);

                dialog.dismiss();

            }
        });


        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDate = getFirstDayOfMonth(-1);
                endDate = getLastDayOfMonth(-1);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);

                dialog.dismiss();

            }
        });

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDate = getFirstDayOfMonth(-2);
                endDate = getLastDayOfMonth(-2);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);

                dialog.dismiss();

            }
        });

        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDate = getFirstDayOfMonth(-3);
                endDate = getLastDayOfMonth(-3);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);

                dialog.dismiss();

            }
        });

        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDate = getFirstDayOfMonth(-4);
                endDate = getLastDayOfMonth(-4);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);

                dialog.dismiss();

            }
        });

        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDate = getFirstDayOfMonth(-5);
                endDate = getLastDayOfMonth(-5);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);

                dialog.dismiss();
            }
        });
        text7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDate = getFirstDayOfMonth(-6);
                endDate = getLastDayOfMonth(-6);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);


                dialog.dismiss();
            }
        });
        text8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDate = getFirstDayOfMonth(-7);
                endDate = getLastDayOfMonth(-7);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);


                dialog.dismiss();
            }
        });
        text9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDate = getFirstDayOfMonth(-8);
                endDate = getLastDayOfMonth(-8);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);

                dialog.dismiss();
            }
        });
        text10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startDate = getFirstDayOfMonth(-9);
                endDate = getLastDayOfMonth(-9);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);

                dialog.dismiss();
            }
        });
        text11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startDate = getFirstDayOfMonth(-10);
                endDate = getLastDayOfMonth(-10);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);

                dialog.dismiss();
            }
        });
        text12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate = getFirstDayOfMonth(-11);
                endDate = getLastDayOfMonth(-11);
                initCandleStickChart();

                Log.i("TIMELINE", startDate);
                Log.i("TIMELINE", endDate);

                dialog.dismiss();
            }
        });

        tx_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }


    public String getLastDayOfMonth(int month) {
        String lastDayOfMonth = "";

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        //  cal.set(Calendar.DATE, 1);
        cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

        lastDayOfMonth = formatter.format(cal.getTime());


        return lastDayOfMonth;
    }

    public String getFirstDayOfMonth(int month) {
        String firstDayOfMonth = "";


        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, month);
        cal.set(Calendar.DATE, 1);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        firstDayOfMonth = formatter.format(cal.getTime());


        return firstDayOfMonth;
    }


    public ArrayList<CandleEntry> getyValsCandleStick(int activityID) {

        ArrayList<CandleEntry> yValsCandleStick = new ArrayList<>();

        Drawable icon = null;

        switch (activityID) {
            case 1:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvfeedrotate);
                break;
            case 2:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvpumprotate);
                break;
            case 8:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvsleeprotate);
                break;
            case 9:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvtummyrotate);
                break;
            case 10:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvsunbatherotate);
                break;
            case 11:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvplayrotate);
                break;
            case 12:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvmassagerotate);
                break;
            case 14:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvcryingrotate);
                break;
        }


        periodDays = fh.getDaysDiff(startDate, endDate);
        String startDateAddOne = fh.minusDate(startDate, 1);
        // Log.i("TIMELINE", " Start Date " + startDateAddOne);

        ArrayList<Timeline> timelineList = new ArrayList<>();
        timelineList.addAll(db.getAllTimeline(localDataHelper.getActiveUserId(), activityID, startDateAddOne, endDate));
        if (timelineList.size() == 0) {
            // yValsCandleStick.add(new CandleEntry((float) 0, 0, 0, 0, 0));
        } else {
            for (int i = 0; i < timelineList.size(); i++) {
                Log.i("TIMELINE", "Activity ID " + activityID + " " + " Index: " + timelineList.get(i).getIndex() + " Start Date: " + df.getStringDateFormat2(timelineList.get(i).getStartDate()) + "End Date: " + df.getStringDateFormat2(timelineList.get(i).getEndDate()));

                if (timelineList.get(i).getStartDate().equals(timelineList.get(i).getEndDate())) {
                    float startDate = (float) fh.getHoursDouble(timelineList.get(i).getStartTime());
                    float endDate = (float) fh.getHoursDouble(timelineList.get(i).getEndTime());

                    if (startDate - endDate < 0.1) {
                        yValsCandleStick.add(new CandleEntry((float) (timelineList.get(i).getIndex() + 0.2), startDate, endDate + (float) 0.1, startDate, endDate + (float) 0.1, icon, timelineList.get(i).getRecordId()));
                    } else {
                        yValsCandleStick.add(new CandleEntry((float) (timelineList.get(i).getIndex() + 0.2), startDate, endDate, startDate, endDate, icon, timelineList.get(i).getRecordId()));
                    }
                } else {
                    //today timeline
                    float startDate = (float) 0;
                    float endDate = (float) fh.getHoursDouble(timelineList.get(i).getEndTime());

//Yesterday timeline
                    float startDate2 = (float) fh.getHoursDouble(timelineList.get(i).getStartTime());
                    float endDate2 = (float) 23.9;

                    yValsCandleStick.add(new CandleEntry((float) (timelineList.get(i).getIndex() - 1 + 0.2), startDate, endDate, startDate, endDate, icon, timelineList.get(i).getRecordId()));
                    yValsCandleStick.add(new CandleEntry((float) (timelineList.get(i).getIndex() + 0.2), startDate2, endDate2, startDate2, endDate2, icon, timelineList.get(i).getRecordId()));

                }

            }
        }


        return yValsCandleStick;

    }


    public ArrayList<CandleEntry> getyValsCandleStickWithIcon(int activityID) {

        ArrayList<CandleEntry> yValsCandleStickIcon = new ArrayList<>();
        Drawable icon = null;

        switch (activityID) {
            case 3:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvformularotate);
                break;
            case 4:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvpumpbottlerotate);
                break;
            case 5:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvfoodrotate);
                break;
            case 6:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvdiaperrotate);
                break;
            case 7:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvbathrotate);
                break;
            case 13:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvdrinkrotate);
                break;
            case 15:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvvaccinationrotate);
                break;
            case 16:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvtemperaturerotete);
                break;
            case 17:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_medrotate);
                break;
            case 18:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvdoctorvisitrotate);
                break;
            case 19:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvsymptomrotate);
                break;
            case 20:
                icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_cvpottyrotate);
                break;
        }

        //  periodDays = fh.getDaysDiff(startDate, endDate);

        ArrayList<Timeline> timelineList = new ArrayList<>();
        timelineList.addAll(db.getAllTimelineIcon(localDataHelper.getActiveUserId(), activityID, startDate, endDate));

        if (timelineList.size() == 0) {
            //  yValsCandleStickIcon.add(new CandleEntry((float) 0, 0, 0, 0, 0));
        } else {
            for (int i = 0; i < timelineList.size(); i++) {
                Log.i("TIMELINE", "Activity ID " + activityID + " " + " Index: " + timelineList.get(i).getIndex() + " Date: " + df.getStringDateFormat(timelineList.get(i).getEndDate()));
                float endDate = (float) fh.getHoursDouble(timelineList.get(i).getEndTime());
                yValsCandleStickIcon.add(new CandleEntry((float) (timelineList.get(i).getIndex() + 0.7), endDate, endDate, endDate, endDate, icon, timelineList.get(i).getRecordId()));

            }
        }


        return yValsCandleStickIcon;

    }


    public ArrayList<CandleEntry> getyValsCandleStickCurrentTime() {
        FormHelper formHelper = new FormHelper(getContext());
        ArrayList<CandleEntry> yValsCandleStick = new ArrayList<>();

        String currentDate = fh.getDateNow2();
        String currentTime = fh.getTimeNow2();
        List<String> dates = formHelper.getArrayOfDate(startDate, endDate);
        double index = formHelper.getIndexOfDate(dates, currentDate) + 0.5;

        float startDate = (float) fh.getHoursDouble(df.getTimeFormat2(currentTime));
        float endDate = (float) (fh.getHoursDouble(df.getTimeFormat2(currentTime)) + 0.1);

        // Log.i("TIMELINE", "currentTime " + currentTime + " StartDate " + startDate + " End Date " + endDate);

        yValsCandleStick.add(new CandleEntry((float) index, startDate, endDate, startDate, endDate));

        return yValsCandleStick;

    }


    private void initCandleStickChart() {

        candleStickChart.highlightValue(null);
        //set up the label axis x
        List<String> mDates = new ArrayList<>();

        //Show xAxis Label
        periodDays = fh.getDaysDiff(startDate, endDate);

        for (int i = 0; i < periodDays + 2; i++) {
            Calendar cal = Calendar.getInstance();
            Date endStartDate = df.getDateFormat2(endDate);
            cal.setTime(endStartDate);
            cal.add(Calendar.DATE, -i);
            // SimpleDateFormat dateShowFormat = new SimpleDateFormat("d/M");
            SimpleDateFormat dateShowFormat = new SimpleDateFormat("dd");
            String dateShow = dateShowFormat.format(cal.getTime());
            mDates.add(dateShow);
        }


        //String[] xAxisLables = new String[]{"1/5", "2/5", "3/5", "4/5", "5/5", "6/5", "7/5", "8/5", "9/5", "10/5", "11/5", "12/5", "13/5", "14/5", "15/5", "16/5", "17/5", "18/5", "19/5", "20/5", "21/5", "22/5", "23/5", "24/5"};

        candleStickChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(mDates));

        XAxis xAxis = candleStickChart.getXAxis();
        xAxis.setCenterAxisLabels(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(-90);
        xAxis.setLabelCount(periodDays + 1, false);
        xAxis.setDrawGridLines(true);// disable x axis grid lines
        xAxis.setGridLineWidth(1f);

        if (!isDarkMode) {
            xAxis.setGridColor(getResources().getColor(R.color.lightGreyChart));
            xAxis.setTextColor(getResources().getColor(R.color.darkerGrey));
        } else {
            xAxis.setGridColor(getResources().getColor(R.color.darkGrey));
            xAxis.setTextColor(getResources().getColor(R.color.grey));
        }

        xAxis.setGranularityEnabled(true);
        xAxis.setGranularity(1f); //set difference between label
        xAxis.setTextSize(9f);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(periodDays + 1);
        xAxis.setYOffset(5);

        Legend l = candleStickChart.getLegend();
        l.setEnabled(false);

        final YAxis yAxis = candleStickChart.getAxisLeft();
        yAxis.setTextSize(8f);
        yAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yAxis.setDrawGridLines(true);
        if (!isDarkMode) {
            yAxis.setGridColor(getResources().getColor(R.color.lightGreyChart));
        } else {
            yAxis.setGridColor(getResources().getColor(R.color.darkGrey));
        }

        yAxis.setGridLineWidth(0.5f);
        yAxis.setDrawLabels(false);
        yAxis.setLabelCount(26, true);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(24f);

        YAxis yAxisRight = candleStickChart.getAxisRight();
        yAxisRight.setDrawLabels(false);
        yAxisRight.setDrawGridLines(false);


        Log.i("TIMELINE", "Start chart");

        candleStickChart.setPinchZoom(false);
        candleStickChart.setDoubleTapToZoomEnabled(false);
        candleStickChart.setVisibleXRangeMaximum(10); // allow 10 values to be displayed
        candleStickChart.setVerticalScrollBarEnabled(true);

        CandleData data = new CandleData();


        ArrayList<CandleEntry> yValsCandleStickCurrentTime = getyValsCandleStickCurrentTime();
        //  yValsCandleStickCurrentTime.add(new CandleEntry(1.5f, 14.1f, 14.2f, 14.1f, 14.2f));

        CandleDataSet setCurrentTime = new CandleDataSet(yValsCandleStickCurrentTime, "Current Time");
        setCurrentTime.setIncreasingColor(getResources().getColor(R.color.grey));
        setCurrentTime.setIncreasingPaintStyle(Paint.Style.FILL);
        setCurrentTime.setDrawValues(false);
        setCurrentTime.setShowCandleBar(true);
        setCurrentTime.setHighlightEnabled(false);
        setCurrentTime.setShadowWidth(0f);
        setCurrentTime.setBarSpace(0.2f); //size of the candlestick
        data.addDataSet(setCurrentTime);


        //  if (db.getTimelineCount(db.getActiveUser().getUserId(), 1, startDate, endDate) > 0) {

        ArrayList<CandleEntry> yValsCandleStickFeed = getyValsCandleStick(1);

//        yValsCandleStickFeed.add(new CandleEntry(1.2f, 23, 24, 23, 24));

        if (activityIDList.contains(1)) {
            if (yValsCandleStickFeed.size() > 0) {
                CandleDataSet setFeed = new CandleDataSet(yValsCandleStickFeed, "DataSet Feed");
                setFeed.setIncreasingColor(getResources().getColor(R.color.cvPink));
                setFeed.setIncreasingPaintStyle(Paint.Style.FILL);
                setFeed.setDrawValues(false);
                setFeed.setShowCandleBar(true);
                setFeed.setHighlightEnabled(true);
                setFeed.setShadowWidth(0f);
                setFeed.setDrawIcons(false);
                setFeed.setBarSpace(0.4f);
                setFeed.setDrawHighlightIndicators(false);
                setFeed.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setFeed);
            }
        }


        //     }


        //if (db.getTimelineCount(db.getActiveUser().getUserId(), 2, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickPump = getyValsCandleStick(2);

//            yValsCandleStickPump.add(new CandleEntry(19.2f, 14.1f, 16f, 14.1f, 16f));
        if (activityIDList.contains(2)) {
            if (yValsCandleStickPump.size() > 0) {
                CandleDataSet setPump = new CandleDataSet(yValsCandleStickPump, "DataSet Pump");
                setPump.setIncreasingColor(getResources().getColor(R.color.cvLtPurple));
                setPump.setIncreasingPaintStyle(Paint.Style.FILL);
                setPump.setDrawValues(false);
                setPump.setShowCandleBar(true);
                setPump.setHighlightEnabled(true);
                setPump.setShadowWidth(0f);
                setPump.setDrawIcons(false);
                setPump.setBarSpace(0.4f);
                setPump.setDrawHighlightIndicators(false);
                setPump.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setPump);
            }
        }

        //  }


        //   if (db.getTimelineCount(db.getActiveUser().getUserId(), 3, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickFormula = getyValsCandleStickWithIcon(3);
//        yValsCandleStickFormula.add(new CandleEntry(3.5f, 10, 10, 10, 10, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvformularotate)));

        if (activityIDList.contains(3)) {
            if (yValsCandleStickFormula.size() > 0) {

                CandleDataSet setFormula = new CandleDataSet(yValsCandleStickFormula, "DataSet Formula");
                setFormula.setDrawValues(false);
                setFormula.setShowCandleBar(true);
                setFormula.setHighlightEnabled(true);
                setFormula.setShadowWidth(0f);
                setFormula.setDrawIcons(true);
                setFormula.setDrawHighlightIndicators(false);
                setFormula.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setFormula);

            }
        }


        //    }

        // setFormula.setHighLightColor(Color.BLACK);// set the color when click highlighted need to set each

//      yValsCandleStickPumpBottle.add(new CandleEntry(3.5f, 14, 14, 14, 14, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvpumpbottlerotate)));
        //  if (db.getTimelineCount(db.getActiveUser().getUserId(), 4, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickPumpBottle = getyValsCandleStickWithIcon(4);

        if (activityIDList.contains(4)) {
            if (yValsCandleStickPumpBottle.size() > 0) {
                CandleDataSet setPumpBottle = new CandleDataSet(yValsCandleStickPumpBottle, "DataSet Pump Bottle");
                setPumpBottle.setDrawValues(false);
                setPumpBottle.setShowCandleBar(true);
                setPumpBottle.setHighlightEnabled(true);
                setPumpBottle.setShadowWidth(0f);
                setPumpBottle.setDrawIcons(true);
                setPumpBottle.setDrawHighlightIndicators(false);
                setPumpBottle.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setPumpBottle);
            }
        }


        //   }

        //   if (db.getTimelineCount(db.getActiveUser().getUserId(), 5, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickFood = getyValsCandleStickWithIcon(5);
        //yValsCandleStickFood.add(new CandleEntry(3.5f, 18, 18, 18, 18, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvfoodrotate)));

        if (activityIDList.contains(5)) {
            if (yValsCandleStickFood.size() > 0) {
                CandleDataSet setFood = new CandleDataSet(yValsCandleStickFood, "DataSet Food");
                setFood.setDrawValues(false);
                setFood.setShowCandleBar(true);
                setFood.setHighlightEnabled(true);
                setFood.setShadowWidth(0f);
                setFood.setDrawIcons(true);
                setFood.setDrawHighlightIndicators(false);
                setFood.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setFood);
            }

        }


        //   }


        //   if (db.getTimelineCount(db.getActiveUser().getUserId(), 6, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickDiaper = getyValsCandleStickWithIcon(6);
//        yValsCandleStickDiaper.add(new CandleEntry(3.5f, 15, 15, 15, 15, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvdiaperrotate)));
//        yValsCandleStickDiaper.add(new CandleEntry(3.5f, 11, 11, 11, 11, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvdiaperrotate)));

        if (activityIDList.contains(6)) {
            if (yValsCandleStickDiaper.size() > 0) {
                CandleDataSet setDiaper = new CandleDataSet(yValsCandleStickDiaper, "DataSet Diaper");
                setDiaper.setDrawValues(false);
                setDiaper.setShowCandleBar(true);
                setDiaper.setHighlightEnabled(true);
                setDiaper.setShadowWidth(0f);
                setDiaper.setDrawIcons(true);
                setDiaper.setDrawHighlightIndicators(false);
                setDiaper.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setDiaper);
            }
        }


        //    }


        //  if (db.getTimelineCount(db.getActiveUser().getUserId(), 7, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickBath = getyValsCandleStickWithIcon(7);
//        yValsCandleStickBath.add(new CandleEntry(4.5f, 20, 20, 20, 20, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvbathrotate)));

        if (activityIDList.contains(7)) {

            if (yValsCandleStickBath.size() > 0) {
                CandleDataSet setBath = new CandleDataSet(yValsCandleStickBath, "DataSet Bath");
                setBath.setDrawValues(false);
                setBath.setShowCandleBar(true);
                setBath.setHighlightEnabled(true);
                setBath.setShadowWidth(0f);
                setBath.setDrawIcons(true);
                setBath.setDrawHighlightIndicators(false);
                setBath.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setBath);
            }

        }


        //      }


        //   if (db.getTimelineCount(db.getActiveUser().getUserId(), 8, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickSleep = getyValsCandleStick(8);

//        yValsCandleStickSleep.add(new CandleEntry(2.2f, 14.1f, 16f, 14.1f, 16f));
        if (activityIDList.contains(8)) {

            if (yValsCandleStickSleep.size() > 0) {
                CandleDataSet setSleep = new CandleDataSet(yValsCandleStickSleep, "DataSet Sleep");
                setSleep.setIncreasingColor(getResources().getColor(R.color.cvDkPurple));
                setSleep.setIncreasingPaintStyle(Paint.Style.FILL);
                setSleep.setDrawValues(false);
                setSleep.setShowCandleBar(true);
                setSleep.setHighlightEnabled(true);
                setSleep.setShadowWidth(0f);
                setSleep.setDrawIcons(false);
                setSleep.setBarSpace(0.4f);
                setSleep.setDrawHighlightIndicators(false);
                setSleep.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setSleep);
            }

        }


        //  }

        //    if (db.getTimelineCount(db.getActiveUser().getUserId(), 9, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickTummyTime = getyValsCandleStick(9);
        //yValsCandleStickTummyTime.add(new CandleEntry(3.2f, 8f, 8.1f, 8f, 8.1f));

        if (activityIDList.contains(9)) {

            if (yValsCandleStickTummyTime.size() > 0) {
                CandleDataSet setTummyTime = new CandleDataSet(yValsCandleStickTummyTime, "DataSet Tummy Time");
                setTummyTime.setIncreasingColor(getResources().getColor(R.color.cvYellow));
                setTummyTime.setIncreasingPaintStyle(Paint.Style.FILL);
                setTummyTime.setDrawValues(false);
                setTummyTime.setShowCandleBar(true);
                setTummyTime.setHighlightEnabled(true);
                setTummyTime.setShadowWidth(0f);
                setTummyTime.setDrawIcons(false);
                setTummyTime.setBarSpace(0.4f);
                setTummyTime.setDrawHighlightIndicators(false);
                setTummyTime.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setTummyTime);
            }

        }


        //    }


        //   if (db.getTimelineCount(db.getActiveUser().getUserId(), 10, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickSunbathe = getyValsCandleStick(10);
        // yValsCandleStickSunbathe.add(new CandleEntry(3.2f, 7f, 7.1f, 7f, 7.1f));


        if (activityIDList.contains(10)) {
            if (yValsCandleStickSunbathe.size() > 0) {
                CandleDataSet setSunbathe = new CandleDataSet(yValsCandleStickSunbathe, "DataSet Sunbathe");
                setSunbathe.setIncreasingColor(getResources().getColor(R.color.cvSunYellow));
                setSunbathe.setIncreasingPaintStyle(Paint.Style.FILL);
                setSunbathe.setDrawValues(false);
                setSunbathe.setShowCandleBar(true);
                setSunbathe.setHighlightEnabled(true);
                setSunbathe.setDrawIcons(false);
                setSunbathe.setShadowWidth(0f);
                setSunbathe.setBarSpace(0.4f);
                setSunbathe.setDrawHighlightIndicators(false);
                setSunbathe.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setSunbathe);
            }
        }


        //      }

        //  if (db.getTimelineCount(db.getActiveUser().getUserId(), 11, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickPlay = getyValsCandleStick(11);
        //  yValsCandleStickPlay.add(new CandleEntry(3.2f, 17f, 17.1f, 17f, 17.1f));

        if (activityIDList.contains(11)) {

            if (yValsCandleStickPlay.size() > 0) {
                CandleDataSet setPlay = new CandleDataSet(yValsCandleStickPlay, "DataSet Play");
                setPlay.setIncreasingColor(getResources().getColor(R.color.cvPlayOrange));
                setPlay.setIncreasingPaintStyle(Paint.Style.FILL);
                setPlay.setDrawValues(false);
                setPlay.setShowCandleBar(true);
                setPlay.setHighlightEnabled(true);
                setPlay.setShadowWidth(0f);
                setPlay.setDrawIcons(false);
                setPlay.setBarSpace(0.4f);
                setPlay.setDrawHighlightIndicators(false);
                setPlay.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setPlay);
            }

        }


        //      }


        //  if (db.getTimelineCount(db.getActiveUser().getUserId(), 12, startDate, endDate) > 0) {

        ArrayList<CandleEntry> yValsCandleStickMassage = getyValsCandleStick(12);
        // yValsCandleStickMassage.add(new CandleEntry(6.2f, 17f, 17.1f, 17f, 17.1f));

        if (activityIDList.contains(12)) {

            if (yValsCandleStickMassage.size() > 0) {
                CandleDataSet setMassage = new CandleDataSet(yValsCandleStickMassage, "DataSet Massage");
                setMassage.setIncreasingColor(getResources().getColor(R.color.cvMassagePurple));
                setMassage.setIncreasingPaintStyle(Paint.Style.FILL);
                setMassage.setDrawValues(false);
                setMassage.setShowCandleBar(true);
                setMassage.setHighlightEnabled(true);
                setMassage.setDrawIcons(false);
                setMassage.setShadowWidth(0f);
                setMassage.setBarSpace(0.4f);
                setMassage.setDrawHighlightIndicators(false);
                setMassage.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setMassage);
            }

        }


        //   }


        //   if (db.getTimelineCount(db.getActiveUser().getUserId(), 13, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickDrink = getyValsCandleStickWithIcon(13);
        //yValsCandleStickDrink.add(new CandleEntry(2.5f, 20.5f, 20.5f, 20.5f, 20.5f, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvdrinkrotate)));

        if (activityIDList.contains(13)) {

            if (yValsCandleStickDrink.size() > 0) {
                CandleDataSet setDrink = new CandleDataSet(yValsCandleStickDrink, "DataSet Drink");
                setDrink.setDrawValues(false);
                setDrink.setShowCandleBar(true);
                setDrink.setHighlightEnabled(true);
                setDrink.setShadowWidth(0f);
                setDrink.setDrawIcons(true);
                setDrink.setDrawHighlightIndicators(false);
                setDrink.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setDrink);
            }

        }


        //    }


        //   if (db.getTimelineCount(db.getActiveUser().getUserId(), 14, startDate, endDate) > 0) {

        ArrayList<CandleEntry> yValsCandleStickCrying = getyValsCandleStick(14);
        // yValsCandleStickCrying.add(new CandleEntry(6.2f, 17.5f, 17.6f, 17.5f, 17.6f));

        if (activityIDList.contains(14)) {
            if (yValsCandleStickCrying.size() > 0) {
                CandleDataSet setCrying = new CandleDataSet(yValsCandleStickCrying, "DataSet Crying");
                setCrying.setIncreasingColor(getResources().getColor(R.color.cvCryingRed));
                setCrying.setIncreasingPaintStyle(Paint.Style.FILL);
                setCrying.setDrawValues(false);
                setCrying.setShowCandleBar(true);
                setCrying.setHighlightEnabled(true);
                setCrying.setShadowWidth(0f);
                setCrying.setDrawIcons(false);
                setCrying.setBarSpace(0.4f);
                setCrying.setDrawHighlightIndicators(false);
                setCrying.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setCrying);
            }
        }

        //    }


        //yValsCandleStickVaccination.add(new CandleEntry(6.5f, 14.5f, 14.5f, 14.5f, 14.5f, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvvaccinationrotate)));
        //    if (db.getTimelineCount(db.getActiveUser().getUserId(), 15, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickVaccination = getyValsCandleStickWithIcon(15);

        if (activityIDList.contains(15)) {
            if (yValsCandleStickVaccination.size() > 0) {
                CandleDataSet setVaccination = new CandleDataSet(yValsCandleStickVaccination, "DataSet Vaccination");
                setVaccination.setDrawValues(false);
                setVaccination.setShowCandleBar(true);
                setVaccination.setHighlightEnabled(true);
                setVaccination.setShadowWidth(0f);
                setVaccination.setDrawIcons(true);
                setVaccination.setDrawHighlightIndicators(false);
                setVaccination.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setVaccination);
            }
        }


        //   }


        // yValsCandleStickTemperature.add(new CandleEntry(6.5f, 16f, 16f, 16f, 16f, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvtemperaturerotete)));
        //   if (db.getTimelineCount(db.getActiveUser().getUserId(), 16, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickTemperature = getyValsCandleStickWithIcon(16);

        if (activityIDList.contains(16)) {

            if (yValsCandleStickTemperature.size() > 0) {
                CandleDataSet setTemperature = new CandleDataSet(yValsCandleStickTemperature, "DataSet Temperature");
                setTemperature.setDrawValues(false);
                setTemperature.setShowCandleBar(true);
                setTemperature.setHighlightEnabled(true);
                setTemperature.setShadowWidth(0f);
                setTemperature.setDrawIcons(true);
                setTemperature.setDrawHighlightIndicators(false);
                setTemperature.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setTemperature);

            }
        }


        //  }


        //  if (db.getTimelineCount(db.getActiveUser().getUserId(), 17, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickMed = getyValsCandleStickWithIcon(17);
        //  yValsCandleStickMed.add(new CandleEntry(6.5f, 13f, 13f, 13f, 13f, ContextCompat.getDrawable(getContext(), R.drawable.ic_medrotate)));


        if (activityIDList.contains(17)) {
            if (yValsCandleStickMed.size() > 0) {
                CandleDataSet setMed = new CandleDataSet(yValsCandleStickMed, "DataSet Med");
                setMed.setDrawValues(false);
                setMed.setShowCandleBar(true);
                setMed.setHighlightEnabled(true);
                setMed.setShadowWidth(0f);
                setMed.setDrawIcons(true);
                setMed.setDrawHighlightIndicators(false);
                setMed.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setMed);
            }
        }


        //     }


        //  if (db.getTimelineCount(db.getActiveUser().getUserId(), 18, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickDoctorVisit = getyValsCandleStickWithIcon(18);
        //yValsCandleStickDoctorVisit.add(new CandleEntry(6.5f, 13f, 13f, 13f, 13f, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvdoctorvisitrotate)));

        if (activityIDList.contains(18)) {
            if (yValsCandleStickDoctorVisit.size() > 0) {
                CandleDataSet setDoctorVisit = new CandleDataSet(yValsCandleStickDoctorVisit, "DataSet Doctor Visit");
                setDoctorVisit.setDrawValues(false);
                setDoctorVisit.setShowCandleBar(true);
                setDoctorVisit.setHighlightEnabled(true);
                setDoctorVisit.setShadowWidth(0f);
                setDoctorVisit.setDrawIcons(true);
                setDoctorVisit.setDrawHighlightIndicators(false);
                setDoctorVisit.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setDoctorVisit);
            }

        }


        //    }


        //  if (db.getTimelineCount(db.getActiveUser().getUserId(), 19, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickSymptom = getyValsCandleStickWithIcon(19);
        //yValsCandleStickSymptom.add(new CandleEntry(6.5f, 18f, 18f, 18f, 18f, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvsymptomrotate)));

        if (activityIDList.contains(19)) {
            if (yValsCandleStickSymptom.size() > 0) {
                CandleDataSet setSymptom = new CandleDataSet(yValsCandleStickSymptom, "DataSet Symptom");
                setSymptom.setDrawValues(false);
                setSymptom.setShowCandleBar(true);
                setSymptom.setHighlightEnabled(true);
                setSymptom.setShadowWidth(0f);
                setSymptom.setDrawIcons(true);
                setSymptom.setDrawHighlightIndicators(false);
                setSymptom.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setSymptom);
            }
        }


        //   }


        //  if (db.getTimelineCount(db.getActiveUser().getUserId(), 20, startDate, endDate) > 0) {
        ArrayList<CandleEntry> yValsCandleStickPotty = getyValsCandleStickWithIcon(20);
//        yValsCandleStickPotty.add(new CandleEntry(7.5f, 19, 19, 19, 19, ContextCompat.getDrawable(getContext(), R.drawable.ic_cvpottyrotate)));

        if (activityIDList.contains(20)) {
            if (yValsCandleStickPotty.size() > 0) {
                CandleDataSet setPotty = new CandleDataSet(yValsCandleStickPotty, "DataSet Potty");
                setPotty.setDrawValues(false);
                setPotty.setShowCandleBar(true);
                setPotty.setHighlightEnabled(true);
                setPotty.setShadowWidth(0f);
                setPotty.setDrawIcons(true);
                setPotty.setDrawHighlightIndicators(false);
                setPotty.setDrawHorizontalHighlightIndicator(false);
                data.addDataSet(setPotty);
            }
        }


        //    }


// create a data object with the datasets
        //  CandleData data = new CandleData();
        //    data.addDataSet(setFeed);
        //  data.addDataSet(setPump);
        // data.addDataSet(setFormula);
        // data.addDataSet(setPumpBottle);
        //  data.addDataSet(setFood);
        //  data.addDataSet(setDiaper);
        // data.addDataSet(setBath);
        //   data.addDataSet(setSleep);
        //   data.addDataSet(setTummyTime);
        //    data.addDataSet(setSunbathe);
        // data.addDataSet(setPlay);
        // data.addDataSet(setMassage);
        // data.addDataSet(setCrying);
        //   data.addDataSet(setVaccination);
        //  data.addDataSet(setTemperature);
        //  data.addDataSet(setMed);
        //  data.addDataSet(setDoctorVisit);
        // data.addDataSet(setSymptom);
        // data.addDataSet(setPotty);

// set data
        candleStickChart.setData(data);
        candleStickChart.notifyDataSetChanged();
        candleStickChart.invalidate();
    }

    public void intentView(View view) {

        // cb_exit = (CheckBox) view.findViewById(R.id.cb_exit);
        cb_feed = (CheckBox) view.findViewById(R.id.cb_feed);
        cb_formula = (CheckBox) view.findViewById(R.id.cb_formula);
        cb_pump = (CheckBox) view.findViewById(R.id.cb_pump);
        cb_pumpbottle = (CheckBox) view.findViewById(R.id.cb_pumpbottle);
        cb_food = (CheckBox) view.findViewById(R.id.cb_food);
        cb_diaper = (CheckBox) view.findViewById(R.id.cb_diaper);
        cb_bath = (CheckBox) view.findViewById(R.id.cb_bath);
        cb_sleep = (CheckBox) view.findViewById(R.id.cb_sleep);
        cb_tummy = (CheckBox) view.findViewById(R.id.cb_tummy);
        cb_sunbathe = (CheckBox) view.findViewById(R.id.cb_sunbathe);
        cb_play = (CheckBox) view.findViewById(R.id.cb_play);
        cb_massage = (CheckBox) view.findViewById(R.id.cb_massage);
        cb_drink = (CheckBox) view.findViewById(R.id.cb_drink);
        cb_crying = (CheckBox) view.findViewById(R.id.cb_crying);
        cb_vaccination = (CheckBox) view.findViewById(R.id.cb_vaccination);
        cb_temperature = (CheckBox) view.findViewById(R.id.cb_temperature);
        cb_med = (CheckBox) view.findViewById(R.id.cb_med);
        cb_doctorvisit = (CheckBox) view.findViewById(R.id.cb_doctorvisit);
        cb_symptom = (CheckBox) view.findViewById(R.id.cb_symptom);
        cb_potty = (CheckBox) view.findViewById(R.id.cb_potty);
        icon_date = (Button) view.findViewById(R.id.icon_date);

    }

    public void setChangeCheckBox() {

//        cb_feed.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//
//
//                if (isFirstTime) {
//                    setAllButton();
//                    cb_feed.setChecked(true);
//                    isFirstTime = false;
//                    activityIDList.clear();
//                    activityIDList.add(1);
//                    isFreshChart = true;
//
//                } else {
//                    if (isChecked) {
//                        activityIDList.add(1);
//                    } else {
//                        activityIDList.remove(activityIDList.indexOf(1));
//                    }
//
//                }
//
//                if (isFreshChart) {
//                    initCandleStickChart();
//                }
//
//
//                for (int i = 0; i < activityIDList.size(); i++) {
//                    Log.i("TAG5", "ID: " + activityIDList.get(i));
//                }
//
//
//            }
//        });

        cb_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_feed.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(1);
                    // isFreshChart = true;

                } else {

                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(1);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(1));
                    }
                    resetActivityList();
                }
                initCandleStickChart();

//                candleStickChart.notifyDataSetChanged();
//                candleStickChart.invalidate();


//
//                for (int i = 0; i < activityIDList.size(); i++) {
//                    Log.i("TAG5", "ID: " + activityIDList.get(i));
//                }

            }
        });


        cb_pump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_pump.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(2);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(2);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(2));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });


        cb_formula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_formula.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(3);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(3);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(3));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });


        cb_pumpbottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_pumpbottle.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(4);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(4);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(4));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });


        cb_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_food.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(5);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(5);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(5));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });


        cb_diaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_diaper.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(6);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(6);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(6));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });


        cb_bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_bath.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(7);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(7);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(7));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
//                candleStickChart.notifyDataSetChanged();
//                candleStickChart.invalidate();
            }
        });


        cb_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_sleep.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(8);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(8);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(8));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

//        cb_exit.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    //Toast.makeText(getContext(), "exit true", Toast.LENGTH_SHORT).show();
//                    activityIDList.add(4);
//                } else {
//                    //Toast.makeText(getContext(), "exit false", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });


        cb_tummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_tummy.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(9);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(9);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(9));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

        cb_sunbathe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_sunbathe.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(10);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(10);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(10));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

        cb_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_play.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(11);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(11);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(11));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

        cb_massage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_massage.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(12);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(12);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(12));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

        cb_drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_drink.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(13);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(13);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(13));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

        cb_crying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_crying.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(14);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(14);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(14));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

        cb_vaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_vaccination.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(15);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(15);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(15));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

        cb_temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_temperature.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(16);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(16);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(16));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

        cb_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_med.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(17);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(17);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(17));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

        cb_doctorvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_doctorvisit.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(18);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(18);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(18));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

        cb_symptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_symptom.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(19);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(19);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(19));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });

        cb_potty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_potty.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(20);
                    isFreshChart = true;
                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(20);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(20));
                    }
                    resetActivityList();
                }
                initCandleStickChart();
            }
        });


    }

    public void setInitActivityIdList() {
        activityIDList.clear();
        for (int i = 0; i <= 20; i++) {
            activityIDList.add(i);
        }
    }

    public void resetActivityList() {
        if (activityIDList.size() == 0) {
            setInitActivityIdList();
            isFirstTime = true;
            setAllButtonTrue();
        }

    }

    public void setAllButtonFalse() {

        cb_feed.setChecked(false);
        cb_formula.setChecked(false);
        cb_pump.setChecked(false);
        cb_pumpbottle.setChecked(false);
        cb_food.setChecked(false);
        cb_diaper.setChecked(false);
        cb_bath.setChecked(false);
        cb_sleep.setChecked(false);
        cb_tummy.setChecked(false);
        cb_sunbathe.setChecked(false);
        cb_play.setChecked(false);
        cb_massage.setChecked(false);
        cb_drink.setChecked(false);
        cb_crying.setChecked(false);
        cb_vaccination.setChecked(false);
        cb_temperature.setChecked(false);
        cb_med.setChecked(false);
        cb_doctorvisit.setChecked(false);
        cb_symptom.setChecked(false);
        cb_potty.setChecked(false);

    }

    public void setAllButtonTrue() {

        cb_feed.setChecked(true);
        cb_formula.setChecked(true);
        cb_pump.setChecked(true);
        cb_pumpbottle.setChecked(true);
        cb_food.setChecked(true);
        cb_diaper.setChecked(true);
        cb_bath.setChecked(true);
        cb_sleep.setChecked(true);
        cb_tummy.setChecked(true);
        cb_sunbathe.setChecked(true);
        cb_play.setChecked(true);
        cb_massage.setChecked(true);
        cb_drink.setChecked(true);
        cb_crying.setChecked(true);
        cb_vaccination.setChecked(true);
        cb_temperature.setChecked(true);
        cb_med.setChecked(true);
        cb_doctorvisit.setChecked(true);
        cb_symptom.setChecked(true);
        cb_potty.setChecked(true);

    }

}
