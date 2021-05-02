package com.riagon.babydiary;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.michaelmuenzer.android.scrollablennumberpicker.ScrollableNumberPicker;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Firebase.RecordFirebase;
import com.riagon.babydiary.Firebase.UserFirebase;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class BreastfeedActivity extends AppCompatActivity {

    public TextView dueDateTextStart;
    public TextView dueDateTextEnded;
    public TextView timepickerStart;
    public TextView timepickerEnded;
    public Button button_Add_Breastfeed;
    public TextView error;
    public TextView time_result;
    public EditText noteEdittext;
    public User currentUser;
    public DatabaseHelper db;
    public DateFormatUtility dateFormatUtility;
    public SettingHelper settingHelper;
    public ScrollableNumberPicker snp1;
    public ScrollableNumberPicker snp2;
    public TextView text_dialogpicker;
    public FormHelper formHelper;
    public LocalDataHelper localDataHelper;
    public RadioButton radioButtonLeft, radioButtonRight, radioButtonLeftRight;
    public RadioGroup radioGroupSide;
    public String option = "Left";
    public double amount;
    public String amountUnit;
    public Date dateStart;
    public Date timeStart;
    public Date dateEnd;
    public Date timeEnd;
    public Date duration;
    public String note;
    public byte[] image;
    public int activitiesId;
    public String userId;
    public String uniqueRecordID;
    private String recordCreatedDatetime;
    private FirebaseUser googleUser;
    private FirebaseAuth mAuth;

    int mYear;
    int mMonth;
    int mDay;
    int hour;
    int minute;


    private int position;
    private Record currentRecord;
    private Boolean isUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        localDataHelper = new LocalDataHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        //  settingHelper.setThemes(currentUser.getUserTheme());
        setContentView(R.layout.activity_breastfeed);
        //setTitle("Breastfeed");

        initView();
        //settingHelper.setBackgroundButtonAdd(this, button_Add_Breastfeed, currentUser.getUserTheme());
        setDefaultTime();
        setInitValue();
        initValueForUpdate();
        // setTitle();
        setToolBar();


        radioGroupSide.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_left:
                        option = "Left";
                        break;
                    case R.id.radioButton_leftRight:
                        option = "Both";
                        break;
                    case R.id.radioButton_right:
                        option = "Right";
                        break;
                }
            }
        });

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);
        //Datepickerz
        dueDateTextStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {

                    c.setTime(sdf.parse(dueDateTextStart.getText().toString()));
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final DatePickerDialog datePickerDialog = new DatePickerDialog(BreastfeedActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dueDateTextStart.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));
                                formHelper.timeDiffCount(dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result);
                                ;
//                                dateStart = dateFormatUtility.getDateFormat(dueDateTextStart.getText().toString());
//                                duration = dateFormatUtility.getTimeFormat(dateFormatUtility.getDatabaseTimeFormat(time_result.getText().toString()).toString());
                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        dueDateTextEnded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {

                    c.setTime(sdf.parse(dueDateTextEnded.getText().toString()));
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(BreastfeedActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dueDateTextEnded.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));
                                formHelper.timeDiffCount(dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result);

//                                dateEnd = dateFormatUtility.getDateFormat(dueDateTextEnded.getText().toString());
//                                duration = dateFormatUtility.getTimeFormat(dateFormatUtility.getDatabaseTimeFormat(time_result.getText().toString()).toString());

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

// time_picker

        timepickerStart.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    mcurrentTime.setTime(sdf.parse(timepickerStart.getText().toString()));// all done
                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    minute = mcurrentTime.get(Calendar.MINUTE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BreastfeedActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timepickerStart.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                        formHelper.timeDiffCount(dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result);

//                        timeStart = dateFormatUtility.getTimeFormat(timepickerStart.getText().toString());
//                        duration = dateFormatUtility.getTimeFormat(dateFormatUtility.getDatabaseTimeFormat(time_result.getText().toString()).toString());
                    }
                }, hour, minute, true);//Yes 24 hour time
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        timepickerEnded.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    mcurrentTime.setTime(sdf.parse(timepickerEnded.getText().toString()));// all done
                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    minute = mcurrentTime.get(Calendar.MINUTE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(BreastfeedActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timepickerEnded.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                        formHelper.timeDiffCount(dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result);

//                        timeEnd = dateFormatUtility.getTimeFormat(timepickerEnded.getText().toString());
//                        duration = dateFormatUtility.getTimeFormat(dateFormatUtility.getDatabaseTimeFormat(time_result.getText().toString()).toString());
                    }
                }, hour, minute, true);//Yes 24 hour time
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        //      button_add

        button_Add_Breastfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitForm();

            }
        });

        time_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationPickerDialog();

            }
        });


    }

    public void setToolBar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle(getResources().getString(R.string.breastfeed_name));

        toolBar.setTitleTextColor(getResources().getColor(R.color.cvPink));
        setSupportActionBar(toolBar);
        //set logo
        toolBar.setLogo(R.drawable.icon_breatfeed);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
// set mau mui ten
        toolBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.cvPink), PorterDuff.Mode.SRC_ATOP);

        if (isUpdate) {

            button_Add_Breastfeed.setText(getResources().getString(R.string.dialog_save));
        } else {

            button_Add_Breastfeed.setText(getResources().getString(R.string.add));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu_growth, menu);

        if (isUpdate) {
            MenuItem item = menu.findItem(R.id.top_delete);
            Drawable icon = getResources().getDrawable(R.drawable.ic_delete_white_24dp);
            icon.setColorFilter(getResources().getColor(R.color.cvPink), PorterDuff.Mode.SRC_IN);
            item.setIcon(icon);
            item.setVisible(true);   //hide it

        } else {
            MenuItem item = menu.findItem(R.id.top_delete);
            item.setVisible(false);   //hide it
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.top_delete:
                if (googleUser != null) {
                    //deleteGrowthFirebase(currentGrowth);
                    RecordFirebase recordFirebase = new RecordFirebase(this, googleUser);
                    recordFirebase.deleteRecordFirebase(currentRecord);
                }
                db.deleteRecord(currentRecord);
                final Intent data = new Intent();
                data.putExtra(LogFragment.POSITION, position);
                data.putExtra(LogFragment.IS_DELETE, true);
                setResult(Activity.RESULT_OK, data);
                finish();
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    public void initView() {

        dueDateTextStart = (TextView) findViewById(R.id.dueDateTextStart);
        dueDateTextEnded = (TextView) findViewById(R.id.dueDateTextEnded);
        timepickerStart = (TextView) findViewById(R.id.timepickerStart);
        timepickerEnded = (TextView) findViewById(R.id.timepickerEnded);
        button_Add_Breastfeed = (Button) findViewById(R.id.button_Add_Breastfeed);
        time_result = (TextView) findViewById(R.id.time_result);
        error = (TextView) findViewById(R.id.error);
        noteEdittext = (EditText) findViewById(R.id.note);
        radioButtonLeft = (RadioButton) findViewById(R.id.radioButton_left);
        radioButtonLeftRight = (RadioButton) findViewById(R.id.radioButton_leftRight);
        radioButtonRight = (RadioButton) findViewById(R.id.radioButton_right);
        radioGroupSide = (RadioGroup) findViewById(R.id.radioGroup_side);
        formHelper = new FormHelper(this);
        dateFormatUtility = new DateFormatUtility(this);
    }


    public void setInitValue() {
        dateStart = dateFormatUtility.getDateFormat(formHelper.getDateNow());
        timeStart = dateFormatUtility.getTimeFormat2(formHelper.getTimeNow2());
        dateEnd = dateFormatUtility.getDateFormat(formHelper.getDateNow());
        timeEnd = dateFormatUtility.getTimeFormat2(formHelper.getTimeNow2());
        duration = dateFormatUtility.getTimeFormat2("00:00:00");
        uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
        mAuth = FirebaseAuth.getInstance();
        googleUser = mAuth.getCurrentUser();
        image = null;
        activitiesId = 1;
        userId = currentUser.getUserId();
        formHelper.timeDiffCount(dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result);
    }


    private void initValueForUpdate() {

        isUpdate = getIntent().getBooleanExtra(LogFragment.IS_UPDATE, false);
        position = getIntent().getIntExtra(LogFragment.POSITION, 0);
        currentRecord = (Record) getIntent().getSerializableExtra(LogFragment.RECORD);

        if (isUpdate) {
            dueDateTextStart.setText(dateFormatUtility.getStringDateFormat(currentRecord.getDateStart()));
            timepickerStart.setText(dateFormatUtility.getStringTimeFormat(currentRecord.getTimeStart()));
            dueDateTextEnded.setText(dateFormatUtility.getStringDateFormat(currentRecord.getDateEnd()));
            timepickerEnded.setText(dateFormatUtility.getStringTimeFormat(currentRecord.getTimeEnd()));
            time_result.setText(dateFormatUtility.getStringTimeFormat(currentRecord.getDuration()));
            noteEdittext.setText(currentRecord.getNote());
            option = currentRecord.getOption();

            if (currentRecord.getOption().isEmpty()) {

            } else {
                if (currentRecord.getOption().equals("Left")) {
                    radioButtonLeft.setChecked(true);
                } else if (currentRecord.getOption().equals("Right")) {
                    radioButtonRight.setChecked(true);
                } else if (currentRecord.getOption().equals("Both")) {
                    radioButtonLeftRight.setChecked(true);
                }
            }


        }


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


    public void setDefaultTime() {

        dueDateTextStart.setText(formHelper.getDateNow());
        dueDateTextEnded.setText(formHelper.getDateNow());
        timepickerStart.setText(formHelper.getTimeNow());
        timepickerEnded.setText(formHelper.getTimeNow());

    }

    private void DurationPickerDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.numberpicker, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        snp1 = (ScrollableNumberPicker) alertLayout.findViewById(R.id.snp1);
        snp2 = (ScrollableNumberPicker) alertLayout.findViewById(R.id.snp2);
        text_dialogpicker = (TextView) alertLayout.findViewById(R.id.text_dialogpicker);
        settingHelper.setBackgroundDialog(text_dialogpicker, currentUser.getUserTheme());
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton(getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                //Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();


            }
        });

        alert.setPositiveButton(getResources().getString(R.string.dialog_save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code for matching password

                //String result = String.format("%d t %d p", snp1.getValue(), snp2.getValue());

                String result = snp1.getValue() + getResources().getString(R.string.h_name) + snp2.getValue() + getResources().getString(R.string.m_name);

                dateEnd = dateFormatUtility.getDateFormat(dueDateTextEnded.getText().toString());
                timeEnd = dateFormatUtility.getTimeFormat(timepickerEnded.getText().toString());

                //  Toast.makeText(getBaseContext(), "Login clicked"+dateFormatUtility.getDatabaseTimeFormat(result), Toast.LENGTH_SHORT).show();
                time_result.setText(result);

                String startDate = formHelper.getStartDate(formHelper.getDateWithTimeFormat(dateFormatUtility.getStringDateFormat(dateEnd), dateFormatUtility.getStringTimeFormat(timeEnd)), dateFormatUtility.getDatabaseTimeFormat(result).toString());

                dueDateTextStart.setText(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat(startDate)));
                timepickerStart.setText(dateFormatUtility.getStringTimeFormat(dateFormatUtility.getDateFormatWithHours(startDate)));
                formHelper.timeDiffCount(dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result);

                //  Toast.makeText(getBaseContext(), "Login clicked"+dateFormatUtility.getStringTimeFormat(dateFormatUtility.getDateFormatWithHours(startDate)), Toast.LENGTH_SHORT).show();


            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();

    }

    public void updateRecord(Record record, int position) {
        int i = db.updateRecord(record);
        final Intent data = new Intent();
        data.putExtra(LogFragment.EXTRA_DATA, (Serializable) record);
        data.putExtra(LogFragment.POSITION, position);
        setResult(Activity.RESULT_OK, data);
        finish();
    }


    public void submitForm() {
        if (formHelper.timeDiffCount(dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result)) {
            if (!noteEdittext.getText().toString().trim().isEmpty()) {
                note = noteEdittext.getText().toString();
            }
            dateStart = dateFormatUtility.getDateFormat(dueDateTextStart.getText().toString());
            dateEnd = dateFormatUtility.getDateFormat(dueDateTextEnded.getText().toString());
            timeStart = dateFormatUtility.getTimeFormat2(timepickerStart.getText().toString() + ":" + formHelper.getTimeSecondNow());
            timeEnd = dateFormatUtility.getTimeFormat2(timepickerEnded.getText().toString() + ":" + formHelper.getTimeSecondNow());
            duration = dateFormatUtility.getTimeFormat2(dateFormatUtility.getDatabaseTimeFormat(time_result.getText().toString()).toString() + ": 00");
            RecordFirebase recordFirebase = new RecordFirebase(this, googleUser);
            recordCreatedDatetime = dateFormatUtility.getStringDateFullFormat2(dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));
            // Toast.makeText(getApplicationContext(), "Note: "+note+"   Date end: "+dateEnd+"   Time end: "+timeEnd, Toast.LENGTH_SHORT).show();

            if (!isUpdate) {
                if (googleUser != null) {
                    //    User user = new User(userID, userImage, userName, userBirthday, userDueDate, userSex, userTheme, true, "No", "Add", currentLoginUser.getUid(), userCreatedDatetime);
                    Record record = new Record(uniqueRecordID, option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userId, false, "Add", googleUser.getUid(), recordCreatedDatetime);
                    db.addRecord(record);
                    recordFirebase.pushSingleRecordToFirebase(record);
                } else {
                    Record record = new Record(uniqueRecordID, option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userId, false, "Add", null, recordCreatedDatetime);
                    db.addRecord(record);
                }

            } else {


                if (googleUser != null) {
                    Record record = new Record(currentRecord.getRecordId(), option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userId, false, "Update", googleUser.getUid(), recordCreatedDatetime);
                    updateRecord(record, position);
                    recordFirebase.pushSingleRecordToFirebase(record);
                } else {
                    Record record = new Record(currentRecord.getRecordId(), option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userId, false, "Update", null, recordCreatedDatetime);
                    updateRecord(record, position);

                }
            }

            finish();

        } else {
            Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
        }

    }


}
