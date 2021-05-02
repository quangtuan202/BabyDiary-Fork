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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hopenlib.flextools.FlexRadioGroup;
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

public class VaccinationActivity extends AppCompatActivity {
    public User currentUser;
    public DatabaseHelper db;
    public SettingHelper settingHelper;
    public LocalDataHelper localDataHelper;
    public TextView dueDateTextTime;
    public TextView timepickerTime;
    public FormHelper formHelper;
    int mYear;
    int mMonth;
    int mDay;
    int hour;
    int minute;
    public Button button_Add_Vaccination;
    private EditText amountEdittext;
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
    private String volumeUnit;
    private Record currentRecord;
    private Boolean isUpdate;

    public DateFormatUtility dateFormatUtility;
    public RadioButton bcg, dtap, influenza, hepA, hepB, hib,
            mmr, pcv, polio, rv, varicella, other_vaccination;
    public FlexRadioGroup rg;

    private String uniqueRecordID;
    private String recordCreatedDatetime;
    private FirebaseUser googleUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        localDataHelper=new LocalDataHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        //settingHelper.setThemes(currentUser.getUserTheme());
        setContentView(R.layout.activity_vaccination);


        initView();
        //  settingHelper.setBackgroundButtonAdd(this, button_Add_Vaccination, currentUser.getUserTheme());
        setInitValue();
        setInitView();
        initValueForUpdate();
        // setTitle();
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
                final DatePickerDialog datePickerDialog = new DatePickerDialog(VaccinationActivity.this,
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
                mTimePicker = new TimePickerDialog(VaccinationActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timepickerTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));

                    }
                }, hour, minute, true);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        // Group radio button
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            if (bcg.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), bcg, currentUser.getUserTheme());
                option = "BCG";
//            } else {
//                bcg.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (dtap.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), dtap, currentUser.getUserTheme());
                option = "DTap";
//            } else {
//                dtap.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (influenza.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), influenza, currentUser.getUserTheme());
                option = "Influenza";
//            } else {
//                influenza.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (hepA.isChecked()) {
                //  settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), hepA, currentUser.getUserTheme());
                option = "HepA";
//            } else {
//                hepA.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (hepB.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), hepB, currentUser.getUserTheme());
                option = "HepB";
//            } else {
//                hepB.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (hib.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), hib, currentUser.getUserTheme());
                option = "Hib";
//            } else {
//                hib.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (mmr.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), mmr, currentUser.getUserTheme());
                option = "MMR";
//            } else {
//                mmr.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (pcv.isChecked()) {
                //settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), pcv, currentUser.getUserTheme());
                option = "PCV";
//            } else {
//                pcv.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (polio.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), polio, currentUser.getUserTheme());
                option = "Polio";
//            } else {
//                polio.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (rv.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), rv, currentUser.getUserTheme());
                option = "RV";
//            } else {
//                rv.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (varicella.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), varicella, currentUser.getUserTheme());
                option = "Varicella";
//            } else {
//                varicella.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (other_vaccination.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), other_vaccination, currentUser.getUserTheme());
                option = "Others";
//            } else {
//                other_vaccination.setBackgroundResource(R.drawable.bg_group_normal);
            }

        });

        button_Add_Vaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitForm();
            }
        });

    }


    public void initView() {
        button_Add_Vaccination = (Button) findViewById(R.id.button_Add_Vaccination);
        dueDateTextTime = (TextView) findViewById(R.id.dueDateTextTime);
        timepickerTime = (TextView) findViewById(R.id.timepickerTime);
        noteEdittext = (EditText) findViewById(R.id.note);
        dateFormatUtility = new DateFormatUtility(this);
        formHelper = new FormHelper(this);
        rg = (FlexRadioGroup) findViewById(R.id.rg);
        bcg = (RadioButton) findViewById(R.id.bcg);
        dtap = (RadioButton) findViewById(R.id.dtap);
        influenza = (RadioButton) findViewById(R.id.influenza);
        hepA = (RadioButton) findViewById(R.id.hepA);
        hepB = (RadioButton) findViewById(R.id.hepB);
        hib = (RadioButton) findViewById(R.id.hib);
        mmr = (RadioButton) findViewById(R.id.mmr);
        pcv = (RadioButton) findViewById(R.id.pcv);
        polio = (RadioButton) findViewById(R.id.polio);
        rv = (RadioButton) findViewById(R.id.rv);
        varicella = (RadioButton) findViewById(R.id.varicella);
        other_vaccination = (RadioButton) findViewById(R.id.other_vaccination);

    }

    public void setInitValue() {

        dateEnd = dateFormatUtility.getDateFormat(formHelper.getDateNow());
        timeEnd = dateFormatUtility.getTimeFormat(formHelper.getTimeNow2());
        image = null;
        dateStart = null;
        timeStart = null;
        duration = null;
        option = "";

        uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
        mAuth = FirebaseAuth.getInstance();
        googleUser = mAuth.getCurrentUser();

        activitiesId = 15;
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

            option = currentRecord.getOption();
            if (currentRecord.getOption().isEmpty()) {

            } else {

                if (currentRecord.getOption().contains("BCG")) {
                    bcg.setChecked(true);
                    //  settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), bcg, currentUser.getUserTheme());
                    //  bcg.setClickable(false);

                }
                if (currentRecord.getOption().contains("DTap")) {
                    dtap.setChecked(true);
                    // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), dtap, currentUser.getUserTheme());
                    //  dtap.setClickable(false);
                }
                if (currentRecord.getOption().contains("Influenza")) {
                    influenza.setChecked(true);
                    //  settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), influenza, currentUser.getUserTheme());
                    // influenza.setClickable(false);
                }
                if (currentRecord.getOption().contains("HepA")) {
                    hepA.setChecked(true);
                    // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), hepA, currentUser.getUserTheme());
                    //  hepA.setClickable(false);
                }
                if (currentRecord.getOption().contains("HepB")) {
                    hepB.setChecked(true);
                    //settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), hepB, currentUser.getUserTheme());
                    //  hepB.setClickable(false);
                }
                if (currentRecord.getOption().contains("Hib")) {
                    hib.setChecked(true);
                    //settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), hib, currentUser.getUserTheme());
                    //   hib.setClickable(false);
                }
                if (currentRecord.getOption().contains("MMR")) {
                    mmr.setChecked(true);
                    // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), mmr, currentUser.getUserTheme());
                    //  mmr.setClickable(false);
                }
                if (currentRecord.getOption().contains("PCV")) {
                    pcv.setChecked(true);
                    //settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), pcv, currentUser.getUserTheme());
                    //  pcv.setClickable(false);
                }
                if (currentRecord.getOption().contains("Polio")) {
                    polio.setChecked(true);
                    //settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), polio, currentUser.getUserTheme());
                    //polio.setClickable(false);
                }
                if (currentRecord.getOption().contains("RV")) {
                    rv.setChecked(true);
                    // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), rv, currentUser.getUserTheme());
                    // rv.setClickable(false);
                }
                if (currentRecord.getOption().contains("Varicella")) {
                    varicella.setChecked(true);
                    // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), varicella, currentUser.getUserTheme());
                    //  varicella.setClickable(false);
                }
                if (currentRecord.getOption().contains("Others")) {
                    other_vaccination.setChecked(true);
                    //   settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), other_vaccination, currentUser.getUserTheme());
                    // other_vaccination.setClickable(false);
                }
            }


        }

    }

//    public void setTitle() {
//        setTitle(getResources().getString(R.string.vaccination_name));
//        if (isUpdate) {
//            button_Add_Vaccination.setText(getResources().getString(R.string.dialog_save));
//        } else {
//            button_Add_Vaccination.setText(getResources().getString(R.string.add));
//        }
//    }


    public void setToolBar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle(getResources().getString(R.string.vaccination_name));

        toolBar.setTitleTextColor(getResources().getColor(R.color.cvVaccineRed));
        setSupportActionBar(toolBar);
        //set logo
        toolBar.setLogo(R.drawable.icon_vaccination);
// mui ten
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
// set mau mui ten
        toolBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.cvVaccineRed), PorterDuff.Mode.SRC_ATOP);

        if (isUpdate) {
            button_Add_Vaccination.setText(getResources().getString(R.string.dialog_save));
        } else {
            button_Add_Vaccination.setText(getResources().getString(R.string.add));
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu_growth, menu);

        if (isUpdate) {
            MenuItem item = menu.findItem(R.id.top_delete);
            Drawable icon = getResources().getDrawable(R.drawable.ic_delete_white_24dp);
            icon.setColorFilter(getResources().getColor(R.color.cvVaccineRed), PorterDuff.Mode.SRC_IN);
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


    public void setInitView() {
        dueDateTextTime.setText(formHelper.getDateNow());
        timepickerTime.setText(formHelper.getTimeNow());

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
                Record record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userId, false, "Add", googleUser.getUid(), recordCreatedDatetime);
                db.addRecord(record);
                recordFirebase.pushSingleRecordToFirebase(record);
            } else {
                Record record = new Record(uniqueRecordID, option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userId, false, "Add", null, recordCreatedDatetime);
                db.addRecord(record);
            }

        } else {


            if (googleUser != null) {
                Record record = new Record(currentRecord.getRecordId(), option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userId, false, "Update", googleUser.getUid(), recordCreatedDatetime);
                updateRecord(record, position);
                recordFirebase.pushSingleRecordToFirebase(record);
            } else {
                Record record = new Record(currentRecord.getRecordId(), option, amount, volumeUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, activitiesId, userId, false, "Update", null, recordCreatedDatetime);
                updateRecord(record, position);

            }
        }

//            Record record = new Record(option, amount, amountUnit, dateStart, timeStart, dateEnd, timeEnd, duration, note, image, activitiesId, userId);
//            int id = (int) db.addRecord(record);


        finish();

    }


}
