package com.riagon.babydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DbBitmapUtility;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

public class GrowthDetail extends AppCompatActivity {
    private ActionBar toolbar;
    public User currentUser;
    public DatabaseHelper db;
    private Bitmap userProfileImage;
    private TextView profileName;
    private SettingHelper settingHelper;
    private LocalDataHelper localDataHelper;
    private LinearLayout profileLayout;
    private String userName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        localDataHelper = new LocalDataHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        settingHelper.setThemes(currentUser.getUserTheme());
        setContentView(R.layout.activity_growth_detail);

//        toolbar = getSupportActionBar();
//        toolbar.setTitle("Growth");
//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
//        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        // getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        profileName = view.findViewById(R.id.name);
        Button profileAvatar = view.findViewById(R.id.profile_avatar);
        profileLayout=(LinearLayout) view.findViewById(R.id.profile_image_layout);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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


//        profileAvatar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
//                startActivity(intent);
//            }
//        });

//        profileLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), AccountActivity.class);
//                startActivity(intent);
//            }
//        });


//name.setText(userName);
//        name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(UserDetailActivity.this, "You have clicked tittle", Toast.LENGTH_LONG).show();
//            }
//        });

        userName = currentUser.getUserName();
        profileName.setText(userName);


        loadFragment(new GrowthListFragment());
    }

    @Override
    protected void onResume() {
        super.onResume();
        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        settingHelper.setThemes(currentUser.getUserTheme());
        setContentView(R.layout.activity_growth_detail);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        // getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        profileName = view.findViewById(R.id.name);
        Button profileAvatar = view.findViewById(R.id.profile_avatar);
        profileLayout=(LinearLayout) view.findViewById(R.id.profile_image_layout);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

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

        loadFragment(new GrowthListFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_growthList:
                   // toolbar.setTitle("Growth");
                    profileName.setText(userName);
                    fragment = new GrowthListFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_weight:
                   // toolbar.setTitle("Weight");
                    profileName.setText(userName);
                    fragment = new WeightFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_length:
                    //toolbar.setTitle("Length");
                    profileName.setText(userName);
                    fragment = new LengthFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_head:
                   // toolbar.setTitle("Head");
                    profileName.setText(userName);
                    fragment = new HeadFragment();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };


    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
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
