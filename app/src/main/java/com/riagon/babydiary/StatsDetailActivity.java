package com.riagon.babydiary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DbBitmapUtility;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

public class StatsDetailActivity extends AppCompatActivity {

    private ActionBar toolbar;
    public User currentUser;
    public DatabaseHelper db;
    private Bitmap userProfileImage;
    private TextView profileName;
    private SettingHelper settingHelper;
    private LocalDataHelper localDataHelper;
    private LinearLayout profileLayout;
    private String userName;
    public RadioButton rd_feed, rd_pump, rd_formula, rd_pumpbottle, rd_food, rd_diaper, rd_bath, rd_sleep,
            rd_tummy, rd_sunbathe, rd_play, rd_massage, rd_drink, rd_crying, rd_vaccination, rd_temperature,rd_med, rd_doctorvisit, rd_symptom, rd_potty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        localDataHelper= new LocalDataHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        settingHelper.setThemes(currentUser.getUserTheme());
        setContentView(R.layout.activity_stats_detail);

        initView();


        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        // getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        profileName = view.findViewById(R.id.name);
        Button profileAvatar = view.findViewById(R.id.profile_avatar);
        profileLayout = (LinearLayout) view.findViewById(R.id.profile_image_layout);

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        toolbar = getSupportActionBar();
        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeButtonEnabled(true);


        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
        if (currentUser.getUserImage() == null) {
            settingHelper.setDefaultProfileImage(this, profileAvatar, settingHelper.getFirstChar(currentUser.getUserName()), currentUser.getUserTheme());

        } else {
            userProfileImage = dbBitmapUtility.getImage(currentUser.getUserImage());
            Drawable drawableIcon = new BitmapDrawable(getResources(), dbBitmapUtility.createCircleBitmap(userProfileImage));
            profileAvatar.setBackground(drawableIcon);
        }


        userName = currentUser.getUserName();
        profileName.setText(userName);

        showFragment();

        rd_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "feed", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsFeedFragment();
                loadFragment(fragment);
            }
        });


        rd_pump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "pump", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsPumpFragment();
                loadFragment(fragment);
            }
        });


        rd_formula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "formula", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsFormulaFragment();
                loadFragment(fragment);
            }
        });


        rd_pumpbottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "pumpbottle", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsBottleFragment();
                loadFragment(fragment);
            }
        });


        rd_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(), "food", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsFoodFragment();
                loadFragment(fragment);
            }
        });


        rd_diaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(), "diaper", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsDiaperFragment();
                loadFragment(fragment);
            }
        });


        rd_bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "bath", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsBathFragment();
                loadFragment(fragment);
            }
        });


        rd_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(), "sleep", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsSleepFragment();
                loadFragment(fragment);
            }
        });


        rd_tummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "tummy", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsTummyFragment();
                loadFragment(fragment);
            }
        });

        rd_sunbathe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "sunbathe", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsSunBathFragment();
                loadFragment(fragment);

            }
        });

        rd_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "play", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsPlayFragment();
                loadFragment(fragment);
            }
        });

        rd_massage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "massage", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsMassageFragment();
                loadFragment(fragment);
            }
        });

        rd_drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(), "drink", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsDrinkFragment();
                loadFragment(fragment);
            }
        });

        rd_crying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(), "crying", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsCryingFragment();
                loadFragment(fragment);
            }
        });
        rd_vaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "vaccination", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsVaccinationFragment();
                loadFragment(fragment);
            }
        });
        rd_temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "temperature", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsTemperatureFragment();
                loadFragment(fragment);
            }
        });
        rd_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getApplicationContext(), "temperature", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsMedicineFragment();
                loadFragment(fragment);
            }
        });

        rd_doctorvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "doctorvisit", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsDoctorFragment();
                loadFragment(fragment);
            }
        });
        rd_symptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(), "symptom", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsSymptomFragment();
                loadFragment(fragment);
            }
        });

        rd_potty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(), "potty", Toast.LENGTH_SHORT).show();
                Fragment fragment = new StatsPottyFragment();
                loadFragment(fragment);
            }
        });


    }

    public void initView() {
        rd_feed = (RadioButton) findViewById(R.id.rd_feed);
        rd_pump = (RadioButton) findViewById(R.id.rd_pump);
        rd_formula = (RadioButton) findViewById(R.id.rd_formula);
        rd_pumpbottle = (RadioButton) findViewById(R.id.rd_pumpbottle);
        rd_food = (RadioButton) findViewById(R.id.rd_food);
        rd_diaper = (RadioButton) findViewById(R.id.rd_diaper);
        rd_bath = (RadioButton) findViewById(R.id.rd_bath);
        rd_sleep = (RadioButton) findViewById(R.id.rd_sleep);
        rd_tummy = (RadioButton) findViewById(R.id.rd_tummy);
        rd_sunbathe = (RadioButton) findViewById(R.id.rd_sunbathe);
        rd_play = (RadioButton) findViewById(R.id.rd_play);
        rd_massage = (RadioButton) findViewById(R.id.rd_massage);
        rd_drink = (RadioButton) findViewById(R.id.rd_drink);
        rd_crying = (RadioButton) findViewById(R.id.rd_crying);
        rd_vaccination = (RadioButton) findViewById(R.id.rd_vaccination);
        rd_temperature = (RadioButton) findViewById(R.id.rd_temperature);
        rd_med=(RadioButton) findViewById(R.id.rd_med);
        rd_doctorvisit = (RadioButton) findViewById(R.id.rd_doctorvisit);
        rd_symptom = (RadioButton) findViewById(R.id.rd_symptom);
        rd_potty = (RadioButton) findViewById(R.id.rd_potty);

    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void showFragment() {
        Intent intent = getIntent();
        int activityId = intent.getIntExtra("Activity", 0);

        switch (activityId) {
            case 1:
                loadFragment(new StatsFeedFragment());
                rd_feed.setChecked(true);

                break;
            case 2:
                loadFragment(new StatsPumpFragment());
                rd_pump.setChecked(true);
                break;
            case 3:
                loadFragment(new StatsFormulaFragment());
                rd_formula.setChecked(true);
                break;
            case 4:
                loadFragment(new StatsBottleFragment());
                rd_pumpbottle.setChecked(true);
                break;
            case 5:
                loadFragment(new StatsFoodFragment());
                rd_food.setChecked(true);
                break;
            case 6:
                loadFragment(new StatsDiaperFragment());
                rd_diaper.setChecked(true);
                break;
            case 7:
                loadFragment(new StatsBathFragment());
                rd_bath.setChecked(true);
                break;
            case 8:
                loadFragment(new StatsSleepFragment());
                rd_sleep.setChecked(true);
                break;
            case 9:
                loadFragment(new StatsTummyFragment());
                rd_tummy.setChecked(true);
                break;
            case 10:
                loadFragment(new StatsSunBathFragment());
                rd_sunbathe.setChecked(true);
                rd_sunbathe.setFocusable(true);
                rd_sunbathe.setFocusableInTouchMode(true);
                rd_sunbathe.requestFocus();
                break;
            case 11:
                loadFragment(new StatsPlayFragment());
                rd_play.setChecked(true);
                rd_play.setFocusable(true);
                rd_play.setFocusableInTouchMode(true);
                rd_play.requestFocus();
                break;
            case 12:
                loadFragment(new StatsMassageFragment());
                rd_massage.setChecked(true);
                rd_massage.setFocusable(true);
                rd_massage.setFocusableInTouchMode(true);
                rd_massage.requestFocus();
                break;
            case 13:
                loadFragment(new StatsDrinkFragment());
                rd_drink.setChecked(true);
                rd_drink.setFocusable(true);
                rd_drink.setFocusableInTouchMode(true);
                rd_drink.requestFocus();
                break;
            case 14:
                loadFragment(new StatsCryingFragment());
                rd_crying.setChecked(true);
                rd_crying.setFocusable(true);
                rd_crying.setFocusableInTouchMode(true);
                rd_crying.requestFocus();
                break;
            case 15:
                loadFragment(new StatsVaccinationFragment());
                rd_vaccination.setChecked(true);
                rd_vaccination.setFocusable(true);
                rd_vaccination.setFocusableInTouchMode(true);
                rd_vaccination.requestFocus();
                break;
            case 16:
                loadFragment(new StatsTemperatureFragment());
                rd_temperature.setChecked(true);
                rd_temperature.setFocusable(true);
                rd_temperature.setFocusableInTouchMode(true);
                rd_temperature.requestFocus();
                break;
            case 17:
                loadFragment(new StatsMedicineFragment());
                rd_med.setChecked(true);
                rd_med.setFocusable(true);
                rd_med.setFocusableInTouchMode(true);
                rd_med.requestFocus();
                break;
            case 18:
                loadFragment(new StatsDoctorFragment());
                rd_doctorvisit.setChecked(true);
                rd_doctorvisit.setFocusable(true);
                rd_doctorvisit.setFocusableInTouchMode(true);
                rd_doctorvisit.requestFocus();
                break;
            case 19:
                loadFragment(new StatsSymptomFragment());
                rd_symptom.setChecked(true);
                rd_symptom.setFocusable(true);
                rd_symptom.setFocusableInTouchMode(true);
                rd_symptom.requestFocus();
                break;
            case 20:
                loadFragment(new StatsPottyFragment());
                rd_potty.setChecked(true);
                rd_potty.setFocusable(true);
                rd_potty.setFocusableInTouchMode(true);
                rd_potty.requestFocus();
                break;


        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
