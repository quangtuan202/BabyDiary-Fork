package com.riagon.babydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Activities;
import com.riagon.babydiary.Utility.ActivitiesConfigAdapter;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ActivitiesConfigActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    // private Context mContext;
    private ActivitiesConfigAdapter adapterActivitiesConfig;
    private List<Activities> activitiesConfigList;
    //private List<Activities> newActivitiesConfigList;
    private DatabaseHelper db;
    private DateFormatUtility dateFormatUtility;
    private FormHelper formHelper;
    private SettingHelper settingHelper;
    private LocalDataHelper localDataHelper;
    private ActionBar toolbar;
    public static final String EXTRA_DATA = "EXTRA_DATA";

//    public ActivitiesConfigActivity(){
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        formHelper = new FormHelper(this);
        localDataHelper= new LocalDataHelper(this);
        dateFormatUtility = new DateFormatUtility(this);
        settingHelper = new SettingHelper(this);
        settingHelper.setThemes(db.getUser(localDataHelper.getActiveUserId()).getUserTheme());

        setTitle(getResources().getString(R.string.set_configuration));
        setContentView(R.layout.activity_activities_config);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_config);
        activitiesConfigList = new ArrayList<>();
        // newActivitiesConfigList = new ArrayList<>();

        //   db.prepareCategoryList();

        toolbar = getSupportActionBar();
        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeButtonEnabled(true);

        activitiesConfigList.addAll(db.getAllActivities(localDataHelper.getActiveUserId()));
        activitiesConfigList.remove(activitiesConfigList.size() - 1);

        //prepareCategoryList();
        adapterActivitiesConfig = new ActivitiesConfigAdapter(this, activitiesConfigList, db);
        recyclerView.setAdapter(adapterActivitiesConfig);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(dividerItemDecoration);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();

            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(activitiesConfigList, i, i + 1);
                    swapRank(activitiesConfigList, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(activitiesConfigList, i, i - 1);
                    swapRank(activitiesConfigList, i, i - 1);
                }
            }

            recyclerView.getAdapter().notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };


    private void swapRank(List<Activities> activitiesConfigList, int pos1, int pos2) {
        int temp = pos1;
        //   db.updateRankActivities(activitiesConfigList.get(pos1), temp);
        pos1 = pos2;
        db.updateRankActivities(activitiesConfigList.get(pos1), pos2, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));
        pos2 = temp;
        db.updateRankActivities(activitiesConfigList.get(pos2), temp, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));

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
