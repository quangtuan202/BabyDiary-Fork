package com.riagon.babydiary;

import android.content.Context;
import android.graphics.Canvas;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.Utils;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Utility.ActivityHelper;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;

public class CustomMarkerViewTimeline extends MarkerView {
    private ImageView tvContentIcon;
    private TextView tvContent;
    private TextView tvContentTime;
    private TextView tvContentDetail;
    private CandleStickChart candleStickChart;
    private DatabaseHelper db;
    private DateFormatUtility df;
    private FormHelper formHelper;
    private LocalDataHelper localDataHelper;
    private ActivityHelper activityHelper;
    private String volumeUnit;
    private String temperatureUnit;

    public CustomMarkerViewTimeline(Context context, int layoutResource) {
        super(context, layoutResource);
        // this markerview only displays a textview
        tvContentIcon = findViewById(R.id.tvContentIcon);
        tvContent = findViewById(R.id.tvContent);
        tvContentTime = findViewById(R.id.tvContentTime);
        tvContentDetail = findViewById(R.id.tvContentDetail);
        candleStickChart = findViewById(R.id.candleStickChart);
        db = new DatabaseHelper(context);
        df = new DateFormatUtility(context);
        formHelper = new FormHelper(context);
        localDataHelper = new LocalDataHelper(context);
        activityHelper = new ActivityHelper(context);
        volumeUnit = localDataHelper.getVolumeUnit();
        temperatureUnit = localDataHelper.getTemperatureUnit();

    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
            if (ce.getData() != null) {

                Record record = db.getRecord(ce.getData().toString());
                tvContent.setText(activityHelper.getActivityName(record.getActivitiesId()));

                if (record.getActivitiesId() == 1) {
                    // holder.recordTime.setText(df.getStringTimeFormat(record.getTimeStart()) + "-" + df.getStringTimeFormat(record.getTimeEnd()));
                    tvContentDetail.setText(activityHelper.getOptionLocale(record.getOption()) + ": " + formHelper.getDurationFormat(record.getDuration()));

                } else if (record.getActivitiesId() == 2) {
                    String amount;
                    if (volumeUnit.equals("ml")) {
                        amount = String.valueOf((int) formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
                    } else {
                        amount = String.valueOf(formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
                    }
                    tvContentDetail.setText(activityHelper.getOptionLocale(record.getOption()) + ": " + formHelper.getDurationFormat(record.getDuration()) + "  "+ amount + volumeUnit);
                    // holder.recordTime.setText(df.getStringTimeFormat(record.getTimeStart()) + "-" + df.getStringTimeFormat(record.getTimeEnd()));
                    // tvContentDetail.setText(activityHelper.getOptionLocale(record.getOption()) + ": " + formHelper.getDurationFormat(record.getDuration()));
                } else if (record.getActivitiesId() == 3) {
                    String amount;
                    if (volumeUnit.equals("ml")) {
                        amount = String.valueOf((int) formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
                    } else {
                        amount = String.valueOf(formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
                    }
                    tvContentDetail.setText(amount + volumeUnit);
                    // holder.recordTime.setText(df.getStringTimeFormat(record.getTimeEnd()));
                } else if (record.getActivitiesId() == 4) {
                    String amount;
                    if (volumeUnit.equals("ml")) {
                        amount = String.valueOf((int) formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
                    } else {
                        amount = String.valueOf(formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
                    }
                    tvContentDetail.setText(amount + volumeUnit);
                } else if (record.getActivitiesId() == 5) {
                    String amount;
                    if (volumeUnit.equals("ml")) {
                        amount = String.valueOf((int) formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
                    } else {
                        amount = String.valueOf(formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
                    }

                    tvContentDetail.setText(amount + volumeUnit + " " + activityHelper.getOptionLocale(record.getOption()));

                } else if (record.getActivitiesId() == 6) {
                    tvContentDetail.setText(activityHelper.getOptionLocale(record.getOption()));

                } else if (record.getActivitiesId() == 7) {
                    tvContentDetail.setText(activityHelper.getOptionLocale(record.getOption()));

                } else if (record.getActivitiesId() == 8) {
                    //     holder.recordTime.setText(df.getStringTimeFormat(record.getTimeStart()) + "-" + df.getStringTimeFormat(record.getTimeEnd()));
                    tvContentDetail.setText(formHelper.getDurationFormat(record.getDuration()));
//
                } else if (record.getActivitiesId() == 9) {
                    //     holder.recordTime.setText(df.getStringTimeFormat(record.getTimeStart()) + "-" + df.getStringTimeFormat(record.getTimeEnd()));
                    tvContentDetail.setText(formHelper.getDurationFormat(record.getDuration()));

                } else if (record.getActivitiesId() == 10) {
                    //  holder.recordTime.setText(df.getStringTimeFormat(record.getTimeStart()) + "-" + df.getStringTimeFormat(record.getTimeEnd()));
                    tvContentDetail.setText(formHelper.getDurationFormat(record.getDuration()));

                } else if (record.getActivitiesId() == 11) {
                    //   holder.recordTime.setText(df.getStringTimeFormat(record.getTimeStart()) + "-" + df.getStringTimeFormat(record.getTimeEnd()));
                    tvContentDetail.setText(formHelper.getDurationFormat(record.getDuration()));

                } else if (record.getActivitiesId() == 12) {
                    // holder.recordTime.setText(df.getStringTimeFormat(record.getTimeStart()) + "-" + df.getStringTimeFormat(record.getTimeEnd()));
                    tvContentDetail.setText(formHelper.getDurationFormat(record.getDuration()));

                } else if (record.getActivitiesId() == 13) {
                    String amount;
                    if (volumeUnit.equals("ml")) {
                        amount = String.valueOf((int) formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
                    } else {
                        amount = String.valueOf(formHelper.geVolumeByInit(record.getAmount(), record.getAmountUnit(), volumeUnit));
                    }
                    tvContentDetail.setText(amount + volumeUnit + " " + activityHelper.getOptionLocale(record.getOption()));
                } else if (record.getActivitiesId() == 14) {
                    //   holder.recordTime.setText(df.getStringTimeFormat(record.getTimeStart()) + "-" + df.getStringTimeFormat(record.getTimeEnd()));
                    tvContentDetail.setText(formHelper.getDurationFormat(record.getDuration()));

                } else if (record.getActivitiesId() == 15) {

                    tvContentDetail.setText(activityHelper.getOptionLocale(record.getOption()));


                } else if (record.getActivitiesId() == 16) {

                    String amount;
                    amount = String.valueOf(formHelper.getTemperatureByInit(record.getAmount(), record.getAmountUnit(), temperatureUnit));
                    tvContentDetail.setText(amount + temperatureUnit);


                } else if (record.getActivitiesId() == 17) {

                    tvContentDetail.setText(activityHelper.getOptionLocale(record.getOption()));


                } else if (record.getActivitiesId() == 18) {


                } else if (record.getActivitiesId() == 19) {

                    tvContentDetail.setText(activityHelper.getOptionLocale(record.getOption()));

                } else if (record.getActivitiesId() == 20) {
                    tvContentDetail.setText(activityHelper.getOptionLocale(record.getOption()));
                } else {
                    tvContentDetail.setText("");
                }


                if (ce.getHigh() == ce.getLow()) {
                    tvContentIcon.setImageDrawable(e.getIcon());
                    tvContentTime.setText("" + df.getStringTimeFormat(record.getTimeEnd()));


                } else {
                    tvContentIcon.setImageDrawable(e.getIcon());
                    tvContentTime.setText("" + df.getStringTimeFormat(record.getTimeStart()) + "-" + df.getStringTimeFormat(record.getTimeEnd()));

                }


            }

            else
            {
                tvContentIcon.setImageDrawable(null);
                tvContentTime.setText("");
                tvContent.setText("");
                tvContentDetail.setText("");
            }


        }

        super.refreshContent(e, highlight);
        // this will perform necessary layouting


    }


    //    private MPPointF mOffset;
////
////    @Override
////    public MPPointF getOffset() {
////
////        if (mOffset == null) {
////            // center the marker horizontally and vertically
////            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
////        }
////
////        return mOffset;
////    }
    @Override
    public void draw(Canvas canvas, float posX, float posY) {

        //  MPPointF offset = getOffsetForDrawingAtPoint(posX, posY);

       // posX = Utils.convertDpToPixel((float)(getHeight()/2.8));
        posX = Utils.convertDpToPixel(20);
        posY = Utils.convertDpToPixel((float)getWidth()/4);

//        posX = getBottom();
//        posY = Utils.convertDpToPixel(getWidth()/2);

        //   posX = Utils.convertDpToPixel(getHeight()/2);
        //  posY = Utils.convertDpToPixel(getWidth() / 2);

//        posX = 0;
//        posY = Utils.convertDpToPixel(getWidth() / 2); // fix marker view on top

        int saveId = canvas.save();
        // translate to the correct position and draw
        //  canvas.translate(posX + offset.x, posY + offset.y);
        canvas.translate(posX, posY);
        draw(canvas);
        canvas.restoreToCount(saveId);
        canvas.translate(-posX, -posY);

    }

}


