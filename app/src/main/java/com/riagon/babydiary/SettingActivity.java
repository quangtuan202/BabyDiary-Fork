package com.riagon.babydiary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

public class SettingActivity extends AppCompatActivity {
    private SettingHelper settingHelper;
    private DatabaseHelper db;
    private LocalDataHelper localDataHelper;
    private ActionBar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingHelper = new SettingHelper(this);
        db = new DatabaseHelper(this);
        localDataHelper = new LocalDataHelper(this);
        settingHelper.setThemes(db.getUser(localDataHelper.getActiveUserId()).getUserTheme());
        localDataHelper.setLanguage();
        setTitle(getResources().getString(R.string.setting_title));

        toolbar = getSupportActionBar();
        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeButtonEnabled(true);

        setContentView(R.layout.activity_setting);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new SettingFragment()).commit();

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

//    @Override
//    public void onBackPressed() {
//        finish();
//    }

    public void onBackPressed() {

        //  finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
