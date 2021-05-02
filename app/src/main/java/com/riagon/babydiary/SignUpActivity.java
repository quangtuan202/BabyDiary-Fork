package com.riagon.babydiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Firebase.UserFirebase;
import com.riagon.babydiary.Model.Care;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.DbBitmapUtility;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {

    public Button btSave;
    public CircularImageView circularImageView;
    public RelativeLayout profile_img_layout, dueDateLayout;
    public TextInputLayout layout_user_birthday, layout_user_name;
    public TextInputEditText input_user_birthday, input_user_name;
    public TextView dueDateTx;
    public RadioButton radioButtonGreen, radioButtonBlue, radioButtonPurple, radioButtonRed, radioButtonPink, radioMale, radioFemale;
    public RadioGroup radioGroupSex, radioGroupTheme;
    public SwitchCompat switchPremature;
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int REQUEST_IMAGE = 100;
    private TextView welcomeTextview;
    private LinearLayout createBabyLayout;
    private LinearLayout termLayout;
    public DatabaseHelper db;
    public DateFormatUtility dateFormatUtility;
    public DbBitmapUtility dbBitmapUtility;
    public FormHelper formHelper;
    public LocalDataHelper dataHelper;
    public byte[] userImage;
    public String userName;
    public String userBirthday;
    public String userSex = "male", userTheme = "pink";
    public Boolean isActive = true;
    public Boolean isSyn = false;
    public String requestStatus;
    public String createBy;
    public String userIdByOwner;
    public String userDueDate, userCreatedDatetime;
    public String signUpState;
    public String currentUserId;
    int mYear;
    int mMonth;
    int mDay;
    public TextView terms, termTitle;
    public TextView privacyPolicy;
    public TextView tx_titleTerm1;
    public TextView tx_titleTerm2;
    public TextView tx_titleTerm3;
    public TextView tx_titleTerm4;
    public TextView tx_titleTerm5;
    public TextView tx_titleTerm6;
    public TextView tx_titleTerm7;
    public TextView tx_titleTerm8;
    public TextView tx_titleTerm9;
    public TextView tx_titleTerm10;
    public TextView tx_titleTerm11;
    public TextView tx_titleTerm12;
    public TextView link_ggPlay;
    public TextView adMob;
    public SettingHelper settingHelper;
    public User currentUser;
    // public String currentAccountLogin;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private FirebaseUser currentLoginUser;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        ///  currentUser = db.getActiveUser();
        settingHelper = new SettingHelper(this);
        formHelper = new FormHelper(this);
        storageRef = storage.getReference();
        dataHelper = new LocalDataHelper(this);

        setContentView(R.layout.activity_sign_up);
        ImagePickerActivity.clearCache(this);
        init();
        initValueForUpdate();
        //Toast.makeText(SignUpActivity.this, "Status: " + isAddNewUser + "User ID" + currentUserId + db.getUser(currentUserId).getUserName(), Toast.LENGTH_SHORT).show();

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        mAuth = FirebaseAuth.getInstance();
        currentLoginUser = mAuth.getCurrentUser();

        dueDateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    c.setTime(sdf.parse(dueDateTx.getText().toString()));
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {


                                dueDateTx.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
        });

        input_user_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                if (signUpState.equals("Update")) {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    try {
                        c.setTime(sdf.parse(input_user_birthday.getText().toString()));
                        mYear = c.get(Calendar.YEAR);
                        mMonth = c.get(Calendar.MONTH);
                        mDay = c.get(Calendar.DAY_OF_MONTH);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                } else {
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(SignUpActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                input_user_birthday.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }


        });


        switchPremature.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton,
                                                 boolean b) {

                        if (switchPremature.isChecked()) {
                            dueDateLayout.setVisibility(View.VISIBLE);
                            DateFormatUtility dateFormatUtility = new DateFormatUtility(getApplicationContext());
                            if (!input_user_birthday.getText().toString().equals("")) {
                                userDueDate = dateFormatUtility.getStringDateFormat2(dateFormatUtility.getDateFormat(input_user_birthday.getText().toString()));
                                dueDateTx.setText(userDueDate);
                            } else {
                                Date date = new Date();
                                dueDateTx.setText(dateFormatUtility.getStringDateFormat(date));
                            }


//                            Date date =dateFormatUtility.getDateFormat(input_user_birthday.getText().toString());
                            // userBirthday = dateFormatUtility.getDateFormat(input_user_birthday.getText().toString());

                        } else {
                            dueDateLayout.setVisibility(View.GONE);
                        }
                        //  textView.setText("Switch is " +
                        //  (switchPremature.isChecked() ? "On" : "Off"));
                    }
                });


        profile_img_layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                requestCameraPermission();

            }
        });


        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId) {
                    case R.id.radioButton_male:
                        userSex = "male";
                        break;
                    case R.id.radioButton_female:
                        userSex = "female";
                        break;
                }
            }
        });

        radioGroupTheme.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_green:
                        btSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_add_green));
                        //  btSave.setBackgroundColor(getResources().getColor(R.color.green));
                        circularImageView.setBorderColor(getResources().getColor(R.color.green));
                        userTheme = "green";
                        setTheme(R.style.GreenNoActionBar);

//                        Toast.makeText(SignUpActivity.this,
//                                "Green", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.radioButton_blue:
                        btSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_add_blue));
                        //   btSave.setBackgroundColor(getResources().getColor(R.color.blue));
                        circularImageView.setBorderColor(getResources().getColor(R.color.blue));
                        userTheme = "blue";
                        setTheme(R.style.BlueNoActionBar);
                        break;
                    case R.id.radioButton_purple:
                        btSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_add_purple));
                        // btSave.setBackgroundColor(getResources().getColor(R.color.purple));
                        circularImageView.setBorderColor(getResources().getColor(R.color.purple));
                        userTheme = "purple";
                        setTheme(R.style.NoActionBar);
                        break;
                    case R.id.radioButton_red:
                        btSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_add_red));
                        //btSave.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                        circularImageView.setBorderColor(getResources().getColor(R.color.colorPrimary));
                        userTheme = "red";
                        setTheme(R.style.RedNoActionBar);
                        break;
                    case R.id.radioButton_pink:
                        btSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_add_pink));
                        //btSave.setBackgroundColor(getResources().getColor(R.color.pink));
                        circularImageView.setBorderColor(getResources().getColor(R.color.pink));
                        userTheme = "pink";
                        setTheme(R.style.PinkNoActionBar);
                        break;
                }
            }
        });

        terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogLoginTerms();

            }
        });
        terms.setTextColor(getResources().getColor(R.color.grey));
        privacyPolicy.setTextColor(getResources().getColor(R.color.grey));

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogLoginPrivacyPolicy();
            }
        });

        //termTitle.setTextColor(getResources().getColor(R.color.grey));
        // settingHelper.setTextColorDialog(this, termTitle, userTheme);

    }


    private void DialogLoginTerms() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.terms_conditions_dialog, null);
        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
        tx_titleTerm1 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm1);
        tx_titleTerm2 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm2);
        tx_titleTerm3 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm3);
        settingHelper.setTextColorDialog(this, tx_titleTerm1, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm2, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm3, userTheme);

        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code for matching password
                //Toast.makeText(getBaseContext(), "Login clicked", Toast.LENGTH_SHORT).show();

            }
        });
        androidx.appcompat.app.AlertDialog dialog = alert.create();
        dialog.show();
        //    dialog.getWindow().setLayout(1300, 2000);

    }

    private void DialogLoginPrivacyPolicy() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.privacy_policy_dialog, null);
        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(this);
        tx_titleTerm1 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm1);
        tx_titleTerm2 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm2);
        tx_titleTerm3 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm3);
        tx_titleTerm4 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm4);
        tx_titleTerm5 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm5);
        tx_titleTerm6 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm6);
        tx_titleTerm7 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm7);
        tx_titleTerm8 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm8);
        tx_titleTerm9 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm9);
        tx_titleTerm10 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm10);
        tx_titleTerm11 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm11);
        tx_titleTerm12 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm12);
        link_ggPlay = (TextView) alertLayout.findViewById(R.id.link_ggPlay);
        adMob = (TextView) alertLayout.findViewById(R.id.adMob);
        settingHelper.setTextColorDialog(this, tx_titleTerm1, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm2, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm3, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm4, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm5, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm6, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm7, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm8, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm9, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm10, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm11, userTheme);
        settingHelper.setTextColorDialog(this, tx_titleTerm12, userTheme);


        link_ggPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToGooglePlayServices();
            }
        });

        adMob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToAdMob();
            }
        });


        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code for matching password
                // Toast.makeText(getBaseContext(), "Login clicked", Toast.LENGTH_SHORT).show();

            }
        });
        androidx.appcompat.app.AlertDialog dialog = alert.create();
        dialog.show();
        //    dialog.getWindow().setLayout(1300, 2000);

    }


    public void goToGooglePlayServices() {
        String url = "https://policies.google.com/privacy";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);


    }

    public void goToAdMob() {
        String url = "https://support.google.com/admob/answer/6128543?hl=en";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }


    private void init() {
        db = new DatabaseHelper(this);
        dateFormatUtility = new DateFormatUtility(this);
        dbBitmapUtility = new DbBitmapUtility();
        btSave = (Button) findViewById(R.id.button_save);

        circularImageView = (CircularImageView) findViewById(R.id.profile_image);
        profile_img_layout = (RelativeLayout) findViewById(R.id.profile_layout);

        radioGroupTheme = (RadioGroup) findViewById(R.id.radioGroup_theme);
        radioButtonGreen = (RadioButton) findViewById(R.id.radioButton_green);
        radioButtonBlue = (RadioButton) findViewById(R.id.radioButton_blue);
        radioButtonPurple = (RadioButton) findViewById(R.id.radioButton_purple);
        radioButtonRed = (RadioButton) findViewById(R.id.radioButton_red);
        radioButtonPink = (RadioButton) findViewById(R.id.radioButton_pink);

        radioGroupSex = (RadioGroup) findViewById(R.id.radioGroup_sex);
        radioMale = (RadioButton) findViewById(R.id.radioButton_male);
        radioFemale = (RadioButton) findViewById(R.id.radioButton_female);


        layout_user_name = (TextInputLayout) findViewById(R.id.layout_user_name);
        input_user_name = (TextInputEditText) findViewById(R.id.user_name);
        input_user_name.addTextChangedListener(new MyTextWatcher(input_user_name));

        layout_user_birthday = (TextInputLayout) findViewById(R.id.layout_user_birthday);
        input_user_birthday = (TextInputEditText) findViewById(R.id.user_birthday);
        switchPremature = (SwitchCompat) findViewById(R.id.premature_switch);

        dueDateLayout = (RelativeLayout) findViewById(R.id.dueDate_layout);
        dueDateTx = (TextView) findViewById(R.id.set_due_date);
        welcomeTextview = (TextView) findViewById(R.id.welcome_textview);
        createBabyLayout = (LinearLayout) findViewById(R.id.create_baby_layout);
        terms = (TextView) findViewById(R.id.terms);
        privacyPolicy = (TextView) findViewById(R.id.privacyPolicy);
        termLayout = (LinearLayout) findViewById(R.id.term_layout);
        termTitle = (TextView) findViewById(R.id.term_title);
    }

    private void loadProfileDefault() {
        if (dataHelper.getDarkMode()) {
            Glide.with(this).load(R.drawable.ic_ellips_black)
                    .into(circularImageView);
        } else {
            Glide.with(this).load(R.drawable.ic_ellips)
                    .into(circularImageView);
        }


        circularImageView.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void initValueForUpdate() {

        if (getIntent().getStringExtra("signUpState") == null) {
            signUpState = "SignUp";
        } else {
            signUpState = getIntent().getStringExtra("signUpState");
        }

        currentUserId = getIntent().getStringExtra("userId");

//        currentAccountLogin = getIntent().getStringExtra("currentAccountLogin");
//        if (currentAccountLogin != null) {
//            Log.i("CARE", "Login with ID" + currentAccountLogin);
//        } else {
//            Log.i("CARE", "Not login");
//        }


        if (signUpState.equals("Update")) {
            welcomeTextview.setText(getResources().getString(R.string.edit_baby));
            createBabyLayout.setVisibility(View.GONE);
            termLayout.setVisibility(View.GONE);
            userImage = db.getUser(currentUserId).getUserImage();
            User currentUser = db.getUser(currentUserId);
            if (currentUser.getUserImage() == null) {
                loadProfileDefault();
            } else {
                Glide.with(this).load(dbBitmapUtility.getImage(currentUser.getUserImage()))
                        .into(circularImageView);
                circularImageView.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
                profile_img_layout.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                profile_img_layout.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            }

            //loadProfile(currentUser.getUserImage());
            input_user_name.setText(currentUser.getUserName());
            input_user_birthday.setText(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(currentUser.getUserBirthday())));
            setDefaultSex(currentUser);
            setDefautTheme(currentUser);
            isSyn = currentUser.getSyn();
            requestStatus = currentUser.getRequestStatus();
            createBy = currentUser.getCreatedBy();
            //userIdByOwner = currentUser.getUserId();


        } else if (signUpState.equals("Add")) {
            welcomeTextview.setText(getResources().getString(R.string.add_baby));
            createBabyLayout.setVisibility(View.GONE);
            termLayout.setVisibility(View.GONE);
        } else {

        }


    }

    public void setDefaultSex(User currentUser) {
        if (currentUser.getUserSex().equals("male")) {
            userSex = "male";
            radioMale.setChecked(true);
        } else {
            userSex = "female";
            radioFemale.setChecked(true);

        }

    }


    public void setDefautTheme(User currentUser) {
        if (currentUser.getUserTheme().equals("green")) {
            userTheme = "green";
            radioButtonGreen.setChecked(true);
            btSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_add_green));
            //  btSave.setBackgroundColor(getResources().getColor(R.color.green));
            circularImageView.setBorderColor(getResources().getColor(R.color.green));
            setTheme(R.style.GreenNoActionBar);
            // termTitle.setTextColor(getResources().getColor(R.color.green));
            //  settingHelper.setTextColorDialog(this, termTitle, "green");

        } else if (currentUser.getUserTheme().equals("blue")) {
            userTheme = "blue";
            radioButtonBlue.setChecked(true);
            btSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_add_blue));
            //   btSave.setBackgroundColor(getResources().getColor(R.color.blue));
            circularImageView.setBorderColor(getResources().getColor(R.color.blue));
            setTheme(R.style.BlueNoActionBar);
            //termTitle.setTextColor(getResources().getColor(R.color.blue));
            //settingHelper.setTextColorDialog(this, termTitle, "blue");

        } else if (currentUser.getUserTheme().equals("purple")) {
            userTheme = "purple";
            radioButtonPurple.setChecked(true);
            btSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_add_purple));
            // btSave.setBackgroundColor(getResources().getColor(R.color.purple));
            circularImageView.setBorderColor(getResources().getColor(R.color.purple));
            setTheme(R.style.NoActionBar);
            // termTitle.setTextColor(getResources().getColor(R.color.purple));
            // settingHelper.setTextColorDialog(this, termTitle, "purple");
        } else if (currentUser.getUserTheme().equals("pink")) {
            userTheme = "pink";
            radioButtonPink.setChecked(true);
            btSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_add_pink));
            //   btSave.setBackgroundColor(getResources().getColor(R.color.pink));
            circularImageView.setBorderColor(getResources().getColor(R.color.pink));
            setTheme(R.style.PinkNoActionBar);
            // termTitle.setTextColor(getResources().getColor(R.color.pink));
            // settingHelper.setTextColorDialog(this, termTitle, "pink");
        } else {
            userTheme = "red";
            radioButtonRed.setChecked(true);
            btSave.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.button_add_red));
            //  btSave.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            circularImageView.setBorderColor(getResources().getColor(R.color.colorPrimary));
            setTheme(R.style.RedNoActionBar);
            // termTitle.setTextColor(getResources().getColor(R.color.colorPrimary));

            // settingHelper.setTextColorDialog(this, termTitle, "red");
        }
    }

    //    private void loadProfileDefault() {
//        Glide.with(this).load(R.drawable.baseline_account_circle_black_48)
//                .into(circularImageView);
//        circularImageView.setColorFilter(ContextCompat.getColor(this, R.color.colorAccent));
//    }
    private void loadProfile(String url) {
        Log.d(TAG, "Image cache path: " + url);
        Glide.with(this).load(url)
                .into(circularImageView);
        circularImageView.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));
    }

    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            showActionsDialog();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    private void showActionsDialog() {
        CharSequence colors[] = new CharSequence[]{"Choose from Gallery", "Take picture"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Profile Photo");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // showNoteDialog(true, notesList.get(position), position);
                    launchGalleryIntent();
                } else {
                    //deleteNote(position);
                    launchCameraIntent();
                }
            }
        });
        builder.show();
    }


    private void launchCameraIntent() {
        Intent intent = new Intent(SignUpActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_IMAGE_CAPTURE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);

        // setting maximum bitmap width and height
        intent.putExtra(ImagePickerActivity.INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_WIDTH, 1000);
        intent.putExtra(ImagePickerActivity.INTENT_BITMAP_MAX_HEIGHT, 1000);

        startActivityForResult(intent, REQUEST_IMAGE);
    }

    private void launchGalleryIntent() {
        Intent intent = new Intent(SignUpActivity.this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_IMAGE_PICKER_OPTION, ImagePickerActivity.REQUEST_GALLERY_IMAGE);

        // setting aspect ratio
        intent.putExtra(ImagePickerActivity.INTENT_LOCK_ASPECT_RATIO, true);
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_X, 1); // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(ImagePickerActivity.INTENT_ASPECT_RATIO_Y, 1);
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getParcelableExtra("path");
                try {
                    // You can update this bitmap to your server
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                    DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
                    userImage = dbBitmapUtility.getBytes(bitmap);

                    // loading profile image from local cache
                    loadProfile(uri.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle(getString(R.string.dialog_permission_title));
        builder.setMessage(getString(R.string.dialog_permission_message));
        builder.setPositiveButton(getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                SignUpActivity.this.openSettings();
            }
        });
        builder.setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
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


    private boolean validateName() {
        if (input_user_name.getText().toString().trim().isEmpty()) {
            layout_user_name.setError("Empty");
            requestFocus(input_user_name);
            return false;
        } else if (input_user_name.getText().toString().length() < 1) {
            layout_user_name.setError("Name is too short");
            requestFocus(input_user_name);
            return false;
        } else if (input_user_name.getText().toString().length() > 20) {
            layout_user_name.setError("Name is to long");
            requestFocus(input_user_name);
            return false;
        } else {
            layout_user_name.setErrorEnabled(false);

        }
        userName = input_user_name.getText().toString();
        return true;
    }

    private boolean validateBirthday() {
        if (input_user_birthday.getText().toString().trim().isEmpty()) {
            layout_user_birthday.setError("Empty");
            requestFocus(input_user_birthday);
            return false;
        } else {
            layout_user_birthday.setErrorEnabled(false);

        }
        DateFormatUtility dateFormatUtility = new DateFormatUtility(this);
        userBirthday = dateFormatUtility.getStringDateFormat2(dateFormatUtility.getDateFormat(input_user_birthday.getText().toString()));
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


        }

        public void afterTextChanged(Editable editable) {

            switch (view.getId()) {

                case R.id.user_name:
                    validateName();
                    break;
                case R.id.user_birthday:
                    validateBirthday();
                    break;
            }


        }
    }


    public void sign_up(View view) {
        submitForm();
    }


    private void submitForm() {

        if (!validateName()) {
            return;
        } else if (!validateBirthday()) {
            return;
        }

        UserFirebase userFirebase = new UserFirebase(this, currentLoginUser);
        // Date date = new Date();
        userCreatedDatetime = dateFormatUtility.getStringDateFullFormat2(dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));
        DateFormatUtility dateFormatUtility = new DateFormatUtility(this);
        LocalDataHelper localDataHelper = new LocalDataHelper(this);

        if (switchPremature.isChecked()) {
            userDueDate = dateFormatUtility.getStringDateFormat2(dateFormatUtility.getDateFormat(dueDateTx.getText().toString()));
        } else {
            userDueDate = userBirthday;
        }

        if (signUpState.equals("Add") || signUpState.equals("SignUp")) {
            String userID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
            if (currentLoginUser != null) {
                User user = new User(userID, userImage, userName, userBirthday, userDueDate, userSex, userTheme, true, "No", "Add", currentLoginUser.getUid(), userCreatedDatetime);
                db.addUserWithID(user);
                userFirebase.pushSingleUserToFirebase(user, false);

            } else {
                //User user = new User(userID, userImage, userName, userBirthday, userDueDate, userSex, userTheme, isSyn, "No", "Add", createBy, userCreatedDatetime);
                User user = new User(userID, userImage, userName, userBirthday, userDueDate, userSex, userTheme, false, "No", "Add", createBy, userCreatedDatetime);
                db.addUserWithID(user);
            }
            localDataHelper.setActiveUserId(userID);

        } else {
            if (currentLoginUser != null) {
                // User user = new User(currentUserId, userImage, userName, userBirthday, userDueDate, userSex, userTheme, db.getUser(currentUserId).getSyn(), requestStatus, "Update", createBy, userCreatedDatetime);
                User user = new User(currentUserId, userImage, userName, userBirthday, userDueDate, userSex, userTheme, true, requestStatus, "Update", createBy, userCreatedDatetime);
                db.updateUser(user);
                userFirebase.pushSingleUserToFirebase(user, true);

            } else {
                User user = new User(currentUserId, userImage, userName, userBirthday, userDueDate, userSex, userTheme, false, requestStatus, "Update", createBy, userCreatedDatetime);
                db.updateUser(user);

            }

        }

        Intent i = new Intent(getBaseContext(), UserDetailActivity.class);
        startActivity(i);
        finish();


    }


}
