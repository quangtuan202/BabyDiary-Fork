package com.riagon.babydiary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Activities;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SetNotificationAdapter;
import com.riagon.babydiary.Utility.SettingHelper;

import java.util.ArrayList;
import java.util.List;

public class SetNotificationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    // private Context mContext;
    private SetNotificationAdapter adapterSetNotification;
    private List<Activities> activitiesNotificationList;
    private DatabaseHelper db;
    private SettingHelper settingHelper;
    private LocalDataHelper localDataHelper;
    private ActionBar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        settingHelper= new SettingHelper(this);
        localDataHelper= new LocalDataHelper(this);
        settingHelper.setThemes(db.getUser(localDataHelper.getActiveUserId()).getUserTheme());
        setTitle(getResources().getString(R.string.set_notification));
        setContentView(R.layout.activity_set_notification);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_notif);
        activitiesNotificationList = new ArrayList<>();

        toolbar = getSupportActionBar();
        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeButtonEnabled(true);

        activitiesNotificationList.addAll(db.getAllActivitiesToShow(db.getUser(localDataHelper.getActiveUserId()).getUserId()));
        activitiesNotificationList.remove(activitiesNotificationList.size()-1);

        //prepareCategoryList();
        adapterSetNotification = new SetNotificationAdapter(this, activitiesNotificationList,db);
        recyclerView.setAdapter(adapterSetNotification);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

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



