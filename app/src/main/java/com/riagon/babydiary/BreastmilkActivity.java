package com.riagon.babydiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import java.util.Calendar;
import java.util.Locale;

public class BreastmilkActivity extends AppCompatActivity {
    private TextView dueDateTextStart;
    private TextView dueDateTextEnded;
    private TextView timepickerStart;
    private TextView timepickerEnded;
    private Button button_Add_Breastmilk;
    private Button button_add;
    private Button button_minus;
    private EditText edt;
    private TextView error;
    private TextView time_result;
    private User currentUser;
    private DatabaseHelper db;
    private SettingHelper settingHelper;
    private LocalDataHelper localDataHelper;
    public ScrollableNumberPicker snp1;
    public ScrollableNumberPicker snp2;
    FormHelper formHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        localDataHelper= new LocalDataHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        settingHelper.setThemes(currentUser.getUserTheme());

        setContentView(R.layout.activity_breastmilk);

        setTitle(getResources().getString(R.string.breastfeed_name));

        button_Add_Breastmilk = (Button) findViewById(R.id.button_Add_Breastmilk);
        settingHelper.setBackgroundButtonAdd(this, button_Add_Breastmilk, currentUser.getUserTheme());
        dueDateTextStart = (TextView) findViewById(R.id.dueDateTextStart);
        dueDateTextEnded = (TextView) findViewById(R.id.dueDateTextEnded);
        timepickerStart = (TextView) findViewById(R.id.timepickerStart);
        timepickerEnded = (TextView) findViewById(R.id.timepickerEnded);
        button_add = (Button) findViewById(R.id.button_add);
        button_minus = (Button) findViewById(R.id.button_minus);
        edt = (EditText) findViewById(R.id.edt);
        time_result = (TextView) findViewById(R.id.time_result);
        error = (TextView) findViewById(R.id.error);
        formHelper = new FormHelper(this);

//DATE TIME NOW

        timeDateNow();

//add_minus

        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curentNumber = Integer.parseInt(edt.getText().toString());
                edt.setText(String.valueOf(formHelper.addFive(curentNumber)));
            }
        });


        button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curentNumber = Integer.parseInt(edt.getText().toString());
                edt.setText(String.valueOf(formHelper.minusFive(curentNumber)));
            }
        });

//Datepicker

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        dueDateTextStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(BreastmilkActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                dueDateTextStart.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));
                                formHelper.timeDiffCount(dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result);;
                            }

                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), Calendar.DAY_OF_MONTH);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        dueDateTextEnded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                DatePickerDialog datePickerDialog = new DatePickerDialog(BreastmilkActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                dueDateTextEnded.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));
                                formHelper.timeDiffCount(dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result);;
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), Calendar.DAY_OF_MONTH);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

// time_picker

        timepickerStart.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BreastmilkActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timepickerStart.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                        formHelper.timeDiffCount(dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result);;
                    }
                }, hour, minute, true);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        timepickerEnded.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BreastmilkActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timepickerEnded.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                        formHelper.timeDiffCount(dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result);;
                    }
                }, hour, minute, true);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

//    button_add


        button_Add_Breastmilk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getApplicationContext(), "Breastmilk Feed Activity", Toast.LENGTH_SHORT).show();
            }
        });

        time_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogLogin();

            }
        });


    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void timeDateNow() {
        dueDateTextStart.setText(formHelper.getDateNow());
        dueDateTextEnded.setText(formHelper.getDateNow());
        timepickerStart.setText(formHelper.getTimeNow());
        timepickerEnded.setText(formHelper.getTimeNow());
    }

    private void DialogLogin() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.numberpicker, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        snp1 = (ScrollableNumberPicker) alertLayout.findViewById(R.id.snp1);
        snp2 = (ScrollableNumberPicker) alertLayout.findViewById(R.id.snp2);

        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code for matching password
                Toast.makeText(getBaseContext(), "Login clicked", Toast.LENGTH_SHORT).show();
                time_result.setText(String.format("%d h %d m", snp1.getValue(), snp2.getValue()));
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();

    }

}
