package com.riagon.babydiary;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Firebase.RecordFirebase;
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

public class DoctorVisitActivity extends AppCompatActivity {
    public User currentUser;
    public DatabaseHelper db;
    public SettingHelper settingHelper;
    public LocalDataHelper localDataHelper;
    public Button button_Add_DoctorVisit;
    public TextView dueDateTextTime;
    public TextView timepickerTime;
    public FormHelper formHelper;
    int mYear;
    int mMonth;
    int mDay;
    int hour;
    int minute;
    public EditText noteEdittext;
    public String option;
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
    private int position;
    private Record currentRecord;
    private Boolean isUpdate;
    public DateFormatUtility dateFormatUtility;

    private String uniqueRecordID;
    private String recordCreatedDatetime;
    private FirebaseUser googleUser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        localDataHelper= new LocalDataHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        // settingHelper.setThemes(currentUser.getUserTheme());
        setContentView(R.layout.activity_doctor_visit);


        initView();
      //  settingHelper.setBackgroundButtonAdd(this, button_Add_DoctorVisit, currentUser.getUserTheme());
        setInitValue();
//date now
        dueDateTextTime.setText(formHelper.getDateNow());
        timepickerTime.setText(formHelper.getTimeNow());

        initValueForUpdate();
        // setTitle()
        setToolBar();
        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

//date picker
        dueDateTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {

                    c.setTime(sdf.parse(dueDateTextTime.getText().toString()));
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final DatePickerDialog datePickerDialog = new DatePickerDialog(DoctorVisitActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dueDateTextTime.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));

                            }

                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });


        //time picker
        timepickerTime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                try {
                    mcurrentTime.setTime(sdf.parse(timepickerTime.getText().toString()));// all done
                    hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    minute = mcurrentTime.get(Calendar.MINUTE);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(DoctorVisitActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timepickerTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

                    }
                }, hour, minute, true);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        button_Add_DoctorVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                submitForm();
            }
        });


    }

    public void initView() {
        button_Add_DoctorVisit = (Button) findViewById(R.id.button_Add_DoctorVisit);
        dueDateTextTime = (TextView) findViewById(R.id.dueDateTextTime);
        timepickerTime = (TextView) findViewById(R.id.timepickerTime);
        formHelper = new FormHelper(this);
        dateFormatUtility = new DateFormatUtility(this);
        noteEdittext = (EditText) findViewById(R.id.note);
    }


//    public void setTitle() {
//        setTitle(getResources().getString(R.string.doctor_visit_name));
//        if (isUpdate) {
//            button_Add_DoctorVisit.setText(getResources().getString(R.string.dialog_save));
//        } else {
//            button_Add_DoctorVisit.setText(getResources().getString(R.string.add));
//        }
//    }

    public void setToolBar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle(getResources().getString(R.string.doctor_visit_name));

        toolBar.setTitleTextColor(getResources().getColor(R.color.cvDoctorRed));
        setSupportActionBar(toolBar);
        //set logo
        toolBar.setLogo(R.drawable.icon_doctor);
// mui ten
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
// set mau mui ten
        toolBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.cvDoctorRed), PorterDuff.Mode.SRC_ATOP);

        if (isUpdate) {
            button_Add_DoctorVisit.setText(getResources().getString(R.string.dialog_save));
        } else {
            button_Add_DoctorVisit.setText(getResources().getString(R.string.add));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu_growth, menu);

        if (isUpdate) {
            MenuItem item = menu.findItem(R.id.top_delete);
            Drawable icon = getResources().getDrawable(R.drawable.ic_delete_white_24dp);
            icon.setColorFilter(getResources().getColor(R.color.cvDoctorRed), PorterDuff.Mode.SRC_IN);
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
                    RecordFirebase recordFirebase = new RecordFirebase(this,googleUser);
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


    public void setInitValue() {
        dateEnd = dateFormatUtility.getDateFormat(formHelper.getDateNow());
        timeEnd = dateFormatUtility.getTimeFormat(formHelper.getTimeNow2());
        activitiesId = 18;
        dateStart = null;
        timeStart = null;
        amount = 0;
       // amountUnit = "";
      //  option = "";
       // duration = null;

        uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
        mAuth = FirebaseAuth.getInstance();
        googleUser = mAuth.getCurrentUser();
        image = null;
        userId = currentUser.getUserId();

    }

    private void initValueForUpdate() {

        isUpdate = getIntent().getBooleanExtra(LogFragment.IS_UPDATE, false);
        position = getIntent().getIntExtra(LogFragment.POSITION, 0);
        currentRecord = (Record) getIntent().getSerializableExtra(LogFragment.RECORD);

        if (isUpdate) {
            dueDateTextTime.setText(dateFormatUtility.getStringDateFormat(currentRecord.getDateEnd()));
            timepickerTime.setText(dateFormatUtility.getStringTimeFormat(currentRecord.getTimeEnd()));
            noteEdittext.setText(currentRecord.getNote());


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


    public void updateRecord(Record record, int position) {
        int i = db.updateRecord(record);
        final Intent data = new Intent();
        data.putExtra(LogFragment.EXTRA_DATA, (Serializable) record);
        data.putExtra(LogFragment.POSITION, position);
        setResult(Activity.RESULT_OK, data);
        finish();
    }


    public void submitForm() {
        // if (formHelper.timeDiffCount(getApplicationContext(), dueDateTextStart, timepickerStart, dueDateTextEnded, timepickerEnded, error, time_result)) {

        if (!noteEdittext.getText().toString().trim().isEmpty()) {
            note = noteEdittext.getText().toString();
        }

        dateEnd = dateFormatUtility.getDateFormat(dueDateTextTime.getText().toString());
        timeEnd = dateFormatUtility.getTimeFormat2(timepickerTime.getText().toString() + ":" + formHelper.getTimeSecondNow());


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

//        } else {
//            Toast.makeText(getApplicationContext(), "NO", Toast.LENGTH_SHORT).show();
//        }

    }


}
