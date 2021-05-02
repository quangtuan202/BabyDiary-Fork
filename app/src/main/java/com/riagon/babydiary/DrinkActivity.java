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
import android.util.Log;
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
import com.riagon.babydiary.Utility.Calculator;
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

public class DrinkActivity extends AppCompatActivity {
    public User currentUser;
    public DatabaseHelper db;
    public SettingHelper settingHelper;
    public LocalDataHelper localDataHelper;
    private Button button_add;
    private Button button_minus;
    public TextView dueDateTextTime;
    public TextView timepickerTime;
    public FormHelper formHelper;
    int mYear;
    int mMonth;
    int mDay;
    int hour;
    int minute;
    private TextView volumeUnitTx;
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
    private Record currentRecord;
    private Boolean isUpdate;
    private String volumeUnit;
    public DateFormatUtility dateFormatUtility;
    private Calculator calculator;

    public Button button_Add_Drink;
    public RadioButton water;
    public RadioButton milk;
    public RadioButton juice;
    public RadioButton fermentedmilk;
    public RadioButton other_drink;
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
        localDataHelper= new LocalDataHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        //settingHelper.setThemes(currentUser.getUserTheme());
        setContentView(R.layout.activity_drink);


        initView();
       // settingHelper.setBackgroundButtonAdd(this, button_Add_Drink, currentUser.getUserTheme());
        setInitValue();
        setInitView();
        initValueForUpdate();
        // setTitle();
        setToolBar();

//add_minus
        button_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (volumeUnit.equals("ml")) {
                    int curentNumber = Integer.parseInt(amountEdittext.getText().toString());
                    amountEdittext.setText(String.valueOf(formHelper.addFive(curentNumber)));
                } else {
                    double curentNumber = Double.parseDouble(amountEdittext.getText().toString());
                    amountEdittext.setText(String.valueOf(formHelper.addDotFive(curentNumber)));
                }

            }
        });


        button_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int curentNumber = Integer.parseInt(amountEdittext.getText().toString());
//                amountEdittext.setText(String.valueOf(formHelper.minusFive(curentNumber)));
                if (volumeUnit.equals("ml")) {
                    int curentNumber = Integer.parseInt(amountEdittext.getText().toString());
                    amountEdittext.setText(String.valueOf(formHelper.minusFive(curentNumber)));
                } else {
                    double curentNumber = Double.parseDouble(amountEdittext.getText().toString());
                    amountEdittext.setText(String.valueOf(formHelper.minusDotFive(curentNumber)));
                }
            }
        });


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
                final DatePickerDialog datePickerDialog = new DatePickerDialog(DrinkActivity.this,
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
                mTimePicker = new TimePickerDialog(DrinkActivity.this, new TimePickerDialog.OnTimeSetListener() {
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
            if (water.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), water, currentUser.getUserTheme());
                option = "Water";
//            } else {
//                water.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (juice.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), juice, currentUser.getUserTheme());
                option = "Juice";

//            } else {
//                juice.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (milk.isChecked()) {
                //  settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), milk, currentUser.getUserTheme());
                option = "Milk";
//            } else {
//                milk.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (fermentedmilk.isChecked()) {
                // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), fermentedmilk, currentUser.getUserTheme());
                option = "Fermentedmilk";
//            } else {
//                fermentedmilk.setBackgroundResource(R.drawable.bg_group_normal);
            }
            if (other_drink.isChecked()) {
                //  settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), other_drink, currentUser.getUserTheme());
                option = "Others";
//            } else {
//                other_drink.setBackgroundResource(R.drawable.bg_group_normal);
            }

        });


        button_Add_Drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Drink Activity", Toast.LENGTH_SHORT).show();
                submitForm();
            }
        });


    }

    public void initView() {
        button_add = (Button) findViewById(R.id.button_add);
        button_minus = (Button) findViewById(R.id.button_minus);
        dueDateTextTime = (TextView) findViewById(R.id.dueDateTextTime);
        timepickerTime = (TextView) findViewById(R.id.timepickerTime);
        amountEdittext = (EditText) findViewById(R.id.edt);
        noteEdittext = (EditText) findViewById(R.id.note);
        volumeUnitTx = (TextView) findViewById(R.id.volume_unit);
        rg = (FlexRadioGroup) findViewById(R.id.rg);
        water = (RadioButton) findViewById(R.id.water);
        milk = (RadioButton) findViewById(R.id.milk);
        juice = (RadioButton) findViewById(R.id.juice);
        fermentedmilk = (RadioButton) findViewById(R.id.fermentedmilk);
        other_drink = (RadioButton) findViewById(R.id.other_drink);
        button_Add_Drink = (Button) findViewById(R.id.button_Add_Drink);
        calculator = new Calculator();
        localDataHelper = new LocalDataHelper(this);
        formHelper = new FormHelper(this);
        dateFormatUtility = new DateFormatUtility(this);
    }

    public void setInitValue() {

        dateEnd = dateFormatUtility.getDateFormat(formHelper.getDateNow());
        timeEnd = dateFormatUtility.getTimeFormat(formHelper.getTimeNow2());
        image = null;
        dateStart = null;
        timeStart = null;
        duration = null;

        uniqueRecordID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
        mAuth = FirebaseAuth.getInstance();
        googleUser = mAuth.getCurrentUser();

        activitiesId = 13;
        option = "";
        userId = currentUser.getUserId();
        volumeUnit = localDataHelper.getVolumeUnit();
    }

    private void initValueForUpdate() {

        isUpdate = getIntent().getBooleanExtra(LogFragment.IS_UPDATE, false);
        position = getIntent().getIntExtra(LogFragment.POSITION, 0);
        currentRecord = (Record) getIntent().getSerializableExtra(LogFragment.RECORD);

        if (isUpdate) {

            dueDateTextTime.setText(dateFormatUtility.getStringDateFormat(currentRecord.getDateEnd()));
            timepickerTime.setText(dateFormatUtility.getStringTimeFormat(currentRecord.getTimeEnd()));

            noteEdittext.setText(currentRecord.getNote());

            if (volumeUnit.equals("ml")) {
                amountEdittext.setText(String.valueOf((int) geVolumeByInit(currentRecord.getAmount(), currentRecord.getAmountUnit(), volumeUnit)));
            } else {
                amountEdittext.setText(String.valueOf(geVolumeByInit(currentRecord.getAmount(), currentRecord.getAmountUnit(), volumeUnit)));
            }

            option = currentRecord.getOption();
            if (currentRecord.getOption().isEmpty()) {

            } else {
                Log.i("TAG1", "Time  " + (option));
                if (currentRecord.getOption().contains("Water")) {
                    water.setChecked(true);
                    //settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), water, currentUser.getUserTheme());

                }
                if (currentRecord.getOption().contains("Milk")) {
                    milk.setChecked(true);
                    //settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), milk, currentUser.getUserTheme());

                }
                if (currentRecord.getOption().contains("Juice")) {
                    juice.setChecked(true);
                    //settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), juice, currentUser.getUserTheme());

                }
                if (currentRecord.getOption().contains("Fermentedmilk")) {
                    fermentedmilk.setChecked(true);
                    // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), fermentedmilk, currentUser.getUserTheme());

                }
                if (currentRecord.getOption().contains("Others")) {
                    other_drink.setChecked(true);
                    // settingHelper.setBackgroundRadioButtonSelected(getApplicationContext(), other_drink, currentUser.getUserTheme());

                }
            }


        }

    }

//    public void setTitle() {
//        setTitle(getResources().getString(R.string.drink_name));
//        if (isUpdate) {
//            button_Add_Drink.setText(getResources().getString(R.string.dialog_save));
//        } else {
//            button_Add_Drink.setText(getResources().getString(R.string.add));
//        }
//    }

    public void setToolBar() {
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        toolBar.setTitle(getResources().getString(R.string.drink_name));

        toolBar.setTitleTextColor(getResources().getColor(R.color.cvDrinkGreen));
        setSupportActionBar(toolBar);
        //set logo
        toolBar.setLogo(R.drawable.icon_drink);
// mui ten
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
// set mau mui ten
        toolBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.cvDrinkGreen), PorterDuff.Mode.SRC_ATOP);

        if (isUpdate) {
            button_Add_Drink.setText(getResources().getString(R.string.dialog_save));
        } else {
            button_Add_Drink.setText(getResources().getString(R.string.add));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu_growth, menu);

        if (isUpdate) {
            MenuItem item = menu.findItem(R.id.top_delete);
            Drawable icon = getResources().getDrawable(R.drawable.ic_delete_white_24dp);
            icon.setColorFilter(getResources().getColor(R.color.cvDrinkGreen), PorterDuff.Mode.SRC_IN);
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


    public double geVolumeByInit(Double volume, String volumeUnit, String settingUnit) {
        double weightConverted = 0;
        if (volumeUnit.equals(settingUnit)) {
            weightConverted = volume;
        } else {
            if (settingUnit.equals("ml")) {
                weightConverted = calculator.convertOzMl(volume);
            } else {
                weightConverted = calculator.convertMlOz(volume);
            }

        }
        return weightConverted;
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
        volumeUnitTx.setText(volumeUnit);

        if (volumeUnit.equals("ml")) {
            amountEdittext.setText("100");
        } else {
            amountEdittext.setText("3");
        }


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

        if (!amountEdittext.getText().toString().trim().isEmpty()) {
            amount = Double.parseDouble(amountEdittext.getText().toString());
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