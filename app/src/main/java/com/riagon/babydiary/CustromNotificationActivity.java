package com.riagon.babydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CustromNotificationActivity extends AppCompatActivity {
//    public Switch switch_repaet;
//    public TextView timepickerTime_ed;
//    public TextView datepickerDate;
//    public FormHelper formHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custrom_notification);

//
//        switch_repaet = (Switch) findViewById(R.id.switch_repaet);
//        timepickerTime_ed = (TextView) findViewById(R.id.timepickerTime_ed);
//        datepickerDate = (TextView) findViewById(R.id.datepickerDate);
//        formHelper = new FormHelper();
//
//
//        datepickerDate.setText(formHelper.getDateNow());
//        timepickerTime_ed.setText(formHelper.getTimeNow());
//
//
//        timepickerTime_ed.setOnClickListener(new View.OnClickListener() {
//
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                Calendar mcurrentTime = Calendar.getInstance();
//                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
//                int minute = mcurrentTime.get(Calendar.MINUTE);
//                TimePickerDialog mTimePicker;
//                mTimePicker = new TimePickerDialog(CustromNotificationActivity.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
//                        timepickerTime_ed.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
//
//                    }
//                }, hour, minute, true);//Yes 24 hour time
//                mTimePicker.setTitle("Select Time");
//                mTimePicker.show();
//
//            }
//        });
//
//        datepickerDate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                final Calendar c = Calendar.getInstance();
//
//                DatePickerDialog datePickerDialog = new DatePickerDialog(CustromNotificationActivity.this,
//                        new DatePickerDialog.OnDateSetListener() {
//
//                            @Override
//                            public void onDateSet(DatePicker view, int year,
//                                                  int monthOfYear, int dayOfMonth) {
//                                datepickerDate.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));
//
//
//                            }
//                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), Calendar.DAY_OF_MONTH);
//                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
//                datePickerDialog.show();
//            }
//        });
//
//
//
//


    }


}
