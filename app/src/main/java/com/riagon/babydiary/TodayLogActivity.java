package com.riagon.babydiary;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.DbBitmapUtility;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.LogAdapter;
import com.riagon.babydiary.Utility.RecyclerTouchListener;
import com.riagon.babydiary.Utility.SettingHelper;
import com.riagon.babydiary.Utility.StatisticsAdapter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class TodayLogActivity extends AppCompatActivity {
    private ActionBar toolbar;
    public User currentUser;
    public DatabaseHelper db;
    public FormHelper formHelper;
    private LocalDataHelper localDataHelper;
    private Bitmap userProfileImage;
    private TextView profileName;
    private SettingHelper settingHelper;
    private LinearLayout profileLayout;
    private MaterialCardView today_top_layout;
    private String userName;

    private RecyclerView statisticsRecyclerView;
    private RecyclerView recyclerView;
    private StatisticsAdapter statisticsAdapter;
    // private Context mContext;
    private LogAdapter adapterLog;
    private List<Record> recordsList;
    private List<Record> statisticsList;
    private LinearLayout noData_layout;
    public Button left, right,icon_date;
    public TextView text_selected;
    public TextView totalActivities;

    public static final String IS_UPDATE = "IS_UPDATE";
    public static final String IS_DELETE = "IS_DELETE";
    public static final String RECORD = "RECORD";
    public static final String POSITION = "POSITION";
    public static final String EXTRA_DATA = "EXTRA_DATA";
    private static final int REQUEST_CODE_UPDATE_RECORD = 1405;
    private Boolean isDelete;
    private Boolean isUpdate;

    int mYear;
    int mMonth;
    int mDay;

    public String dateStart;
    public String dateEnd;
    public String firstDateString;
    public String secondDateString;
    public String firstDateShow;
    public String secondDateShow;
    public int days;
    DateFormat dateFormat2;
    DateFormat dateFormat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        localDataHelper= new LocalDataHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        settingHelper.setThemes(currentUser.getUserTheme());
        setContentView(R.layout.activity_today_log);

        dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy", getResources().getConfiguration().locale);
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");


        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        // getSupportActionBar().setElevation(0);
        View view = getSupportActionBar().getCustomView();
        profileName = view.findViewById(R.id.name);
        Button profileAvatar = view.findViewById(R.id.profile_avatar);
        profileLayout = (LinearLayout) view.findViewById(R.id.profile_image_layout);
        today_top_layout= findViewById(R.id.today_top_layout);


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


        //   prepareLogActivity();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_log);
        statisticsRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_statistics);

        text_selected = (TextView) findViewById(R.id.text_selected);
        icon_date = (Button) findViewById(R.id.icon_date);
        left = (Button) findViewById(R.id.left);
        right = (Button) findViewById(R.id.right);
        totalActivities = findViewById(R.id.total_activities_tx);


        recordsList = new ArrayList<>();
        statisticsList= new ArrayList<>();
        //   recordsList.addAll(db.getAllRecords(db.getActiveUser().getUserId()));


        //get today date

        Calendar date = Calendar.getInstance();
        //DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // DateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy", this.getResources().getConfiguration().locale);

        secondDateString = dateFormat.format(date.getTime());
        secondDateShow = dateFormat2.format(date.getTime());

        firstDateString = dateFormat.format(date.getTime());
        firstDateShow = dateFormat2.format(date.getTime());
        days = 1;


        recordsList.addAll(db.getAllRecordsByDayRange(localDataHelper.getActiveUserId(), 0, secondDateString, secondDateString));
        statisticsList.addAll(db.getTotalRecordByDateRange(localDataHelper.getActiveUserId(),secondDateString, secondDateString));

        text_selected.setText(secondDateShow);


        noData_layout = (LinearLayout) findViewById(R.id.growth_list_no_dada);
        totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCount(localDataHelper.getActiveUserId(), 0, secondDateString, secondDateString));


        //dateRangeCalendarView = (DateRangeCalendarView) view.findViewById(R.id.calendar);
        formHelper = new FormHelper(this);
        settingHelper = new SettingHelper(this);
        checkTopView();
      //  checkNoDataView();

        //set data for Statistic Recyleview
        statisticsAdapter = new StatisticsAdapter(this, statisticsList, currentUser,this);

        statisticsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        statisticsRecyclerView.setAdapter(statisticsAdapter);

//        statisticsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
//                statisticsRecyclerView, new RecyclerTouchListener.ClickListener() {
//            @Override
//            public void onClick(View view, final int position) {
//
//                launchActivity(recordsList.get(position).getActivitiesId(), position);
//
//            }
//
//            @Override
//            public void onLongClick(View view, int position) {
//
//            }
//        }));



        //set data for Recyleview
        adapterLog = new LogAdapter(this, recordsList, currentUser,this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapterLog);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                launchActivity(recordsList.get(position).getActivitiesId(), position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));




        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        icon_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                try {
                    c.setTime(dateFormat2.parse(text_selected.getText().toString()));
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(TodayLogActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String result = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;

                                DateFormatUtility df = new DateFormatUtility();


                                firstDateString = dateFormat.format(df.getDateFormat(result).getTime());
                                secondDateString = dateFormat.format(df.getDateFormat(result).getTime());


                                firstDateShow = dateFormat2.format(df.getDateFormat(result).getTime());
                                secondDateShow = dateFormat2.format(df.getDateFormat(result).getTime());
                                text_selected.setText(secondDateShow);

                                //Refresh the list
                                recordsList.clear();
                                recordsList.addAll(db.getAllRecordsByDayRange(currentUser.getUserId(), 0, firstDateString, secondDateString));
                                adapterLog.notifyDataSetChanged();

                                //Refresh the statistic list
                                statisticsList.clear();
                                statisticsList.addAll(db.getTotalRecordByDateRange(currentUser.getUserId(), firstDateString, secondDateString));
                                statisticsAdapter.notifyDataSetChanged();

                                checkTopView();
                                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCount(currentUser.getUserId(), 0, firstDateString, secondDateString));

                               // text_selected.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();

            }
        });


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DateFormatUtility df = new DateFormatUtility();

                FormHelper fh = new FormHelper();

                firstDateString = fh.minusDate(firstDateString, days);

                secondDateString = fh.minusDate(secondDateString, days);


                // DateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yyyy", getResources().getConfiguration().locale);

                //     secondDateString = dateFormat.format(date.getTime());

//set humand read format
                firstDateShow = dateFormat2.format(df.getDateFormat2(firstDateString).getTime());
                secondDateShow = dateFormat2.format(df.getDateFormat2(secondDateString).getTime());
                text_selected.setText(secondDateShow);

                //Refresh the list
                recordsList.clear();
                recordsList.addAll(db.getAllRecordsByDayRange(currentUser.getUserId(), 0, firstDateString, secondDateString));
                adapterLog.notifyDataSetChanged();

                //Refresh the statistic list
                statisticsList.clear();
                statisticsList.addAll(db.getTotalRecordByDateRange(currentUser.getUserId(), firstDateString, secondDateString));
                statisticsAdapter.notifyDataSetChanged();


                checkTopView();
                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCount(currentUser.getUserId(), 0, firstDateString, secondDateString));


            }
        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(this, "right", Toast.LENGTH_SHORT).show();

                DateFormatUtility df = new DateFormatUtility();

                FormHelper fh = new FormHelper();

                firstDateString = fh.addDate(firstDateString, days);

                secondDateString = fh.addDate(secondDateString, days);

//set humand read format
                firstDateShow = dateFormat2.format(df.getDateFormat2(firstDateString).getTime());
                secondDateShow = dateFormat2.format(df.getDateFormat2(secondDateString).getTime());

                text_selected.setText(secondDateShow);

                //Refresh the list
                recordsList.clear();
                recordsList.addAll(db.getAllRecordsByDayRange(currentUser.getUserId(), 0, firstDateString, secondDateString));
                adapterLog.notifyDataSetChanged();


                //Refresh the statistic list
                statisticsList.clear();
                statisticsList.addAll(db.getTotalRecordByDateRange(currentUser.getUserId(), firstDateString, secondDateString));
                statisticsAdapter.notifyDataSetChanged();

                checkTopView();
                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCount(currentUser.getUserId(), 0, firstDateString, secondDateString));


            }
        });


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_UPDATE_RECORD) {
            if (resultCode == Activity.RESULT_OK) {
                isDelete = data.getExtras().getBoolean(IS_DELETE, false);
                //Refresh the statistic list
                statisticsList.clear();
                statisticsList.addAll(db.getTotalRecordByDateRange(currentUser.getUserId(), firstDateString, secondDateString));
                statisticsAdapter.notifyDataSetChanged();

                if (isDelete) {
                    int position = data.getExtras().getInt(POSITION);
                   // checkNoDataView();
                    checkTopView();
                    totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCount(currentUser.getUserId(), 0, firstDateString, secondDateString));
                    recordsList.remove(position);
                    adapterLog.notifyDataSetChanged();
                    Log.i("TAG", "Delete at Index ... " + position);
                } else {
                    Record result = (Record) data.getExtras().getSerializable(EXTRA_DATA);
                    int position = data.getExtras().getInt(POSITION);
                    //checkNoDataView();
                    checkTopView();
                    totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCount(currentUser.getUserId(), 0, firstDateString, secondDateString));
                    recordsList.set(position, result);
                    adapterLog.notifyDataSetChanged();

                    Log.i("TAG", "Update at Index ... " + position);
                }

            } else {
                // DetailActivity không thành công, không có data trả về.
            }
        }


    }

    private void checkNoDataView() {
        if (db.getRecordCount(currentUser.getUserId()) == 0) {
            noData_layout.setVisibility(View.VISIBLE);
            //recyclerView.setVisibility(View.GONE);
        } else {

            noData_layout.setVisibility(View.GONE);
            //   recyclerView.setVisibility(View.VISIBLE);

        }


    }
private void checkTopView()
{
    if (db.getRecordByDateRangeCount(currentUser.getUserId(), 0, secondDateString, secondDateString)==0)
    {
        today_top_layout.setVisibility(View.GONE);
    }
    else
    {
        today_top_layout.setVisibility(View.VISIBLE);
    }

}

    public void launchActivity(int activityID, int position) {

        if (activityID == 1) {
            Intent intent = new Intent(this, BreastfeedActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 2) {

            Intent intent = new Intent(this, PumpActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);
        } else if (activityID == 3) {

            Intent intent = new Intent(this, FormulaActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);
        } else if (activityID == 4) {
            Intent intent = new Intent(this, BottlePumpedActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);
            // startActivity(intent);
        } else if (activityID == 5) {
            Intent intent = new Intent(this, FoodActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);
            // startActivity(intent);
        } else if (activityID == 6) {
            Intent intent = new Intent(this, DiaperActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 7) {
            Intent intent = new Intent(this, BathActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 8) {
            Intent intent = new Intent(this, SleepActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);
        } else if (activityID == 9) {
            Intent intent = new Intent(this, TummyTimeActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 10) {
            Intent intent = new Intent(this, SunbathTimeActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 11) {

            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 12) {

            Intent intent = new Intent(this, MassageActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 13) {

            Intent intent = new Intent(this, DrinkActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 14) {

            Intent intent = new Intent(this, CryingActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 15) {

            Intent intent = new Intent(this, VaccinationActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 16) {

            Intent intent = new Intent(this, TemperatureActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 17) {

            Intent intent = new Intent(this, MedicineActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 18) {

            Intent intent = new Intent(this, DoctorVisitActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 19) {

            Intent intent = new Intent(this, SymptomActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 20) {

            Intent intent = new Intent(this, PottyActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        }

    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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