package com.riagon.babydiary;


import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Firebase.RecordFirebase;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.RecordFirebaseObject;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.LogAdapter;
import com.riagon.babydiary.Utility.RecyclerTouchListener;
import com.riagon.babydiary.Utility.SettingHelper;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class LogFragment extends Fragment {
    private RecyclerView recyclerView;
    // private Context mContext;
    private LogAdapter adapterLog;
    private List<LogActivity> activitiesLogList;
    private List<Record> recordsList;
    private DatabaseHelper db;
    private LinearLayout noData_layout;
    public static final String IS_UPDATE = "IS_UPDATE";
    public static final String IS_DELETE = "IS_DELETE";
    public static final String RECORD = "RECORD";
    public static final String POSITION = "POSITION";
    public static final String EXTRA_DATA = "EXTRA_DATA";
    private static final int REQUEST_CODE_UPDATE_RECORD = 1405;
    private Boolean isDelete;
    private Boolean isUpdate;
    public Button left, right, icon_date;
    public TextView text_selected;
    public TextView tx_cancle;
    public TextView text1;
    public TextView text2;
    public TextView text3;
    public TextView text4;
    public TextView text5;
    public TextView text6;
    public TextView totalActivities;

    public String dateStart = "";
    public String dateEnd = "";
    public String firstDateString;
    public String secondDateString;
    public String firstDateShow;
    public String secondDateShow;

    public FormHelper formHelper;
    public SettingHelper settingHelper;
    public LocalDataHelper localDataHelper;
    public DateFormatUtility dateFormatUtility;
    public User currentUser;
    public int days;
    public int selectedActivityID = 0;
    public CheckBox cb_exit, cb_feed, cb_formula, cb_pump, cb_pumpbottle, cb_food, cb_diaper, cb_bath, cb_sleep, cb_tummy,
            cb_sunbathe, cb_play, cb_massage, cb_drink, cb_crying, cb_vaccination, cb_temperature, cb_med, cb_doctorvisit, cb_symptom, cb_potty;
    private List<Integer> activityIDList = new ArrayList<>();
    private Boolean isFirstTime = true;

    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseUser googleUser;
    // private RecordFirebase recordFirebase;

    public LogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_log, container, false);
        db = new DatabaseHelper(getContext());
        dateFormatUtility = new DateFormatUtility();
        formHelper = new FormHelper(getContext());
        settingHelper = new SettingHelper(getContext());
        localDataHelper = new LocalDataHelper(getContext());

        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        googleUser = mAuth.getCurrentUser();
        currentUser = db.getUser(localDataHelper.getActiveUserId());


        //  activitiesLogList = new ArrayList<>();
        recordsList = new ArrayList<>();
//        recordsList.addAll(db.getAllRecords(db.getActiveUser().getUserId()));


        //set up the activity ID list
        setInitActivityIdList();
//        for (int i = 0; i <= 20; i++) {
//            activityIDList.add(i);
//        }


        secondDateString = formHelper.getDateNow2();
        firstDateString = currentUser.getUserBirthday();

        recordsList.addAll(db.getAllRecordsByDayRangeMulti(localDataHelper.getActiveUserId(), activityIDList, firstDateString, secondDateString));

        noData_layout = (LinearLayout) view.findViewById(R.id.growth_list_no_dada);


        //   prepareLogActivity();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_log);
        text_selected = (TextView) view.findViewById(R.id.text_selected);
        icon_date = (Button) view.findViewById(R.id.icon_date);
        left = (Button) view.findViewById(R.id.left);
        right = (Button) view.findViewById(R.id.right);
        totalActivities = view.findViewById(R.id.total_activities_tx);
        intentView(view);

        // totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordCount(db.getActiveUser().getUserId()));
        totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(localDataHelper.getActiveUserId(), activityIDList, firstDateString, secondDateString));
        left.setEnabled(false);
        right.setEnabled(false);

        //dateRangeCalendarView = (DateRangeCalendarView) view.findViewById(R.id.calendar);

        checkNoDataView();

        adapterLog = new LogAdapter(getContext(), recordsList, currentUser, getActivity());

        pullRecordByUserRealtime(currentUser);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapterLog);


        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                launchActivity(recordsList.get(position).getActivitiesId(), position);

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        // checke box
        setChangeCheckBox();


//
        icon_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DurationPickerDialog();
                //  DialogAddVolume();

            }
        });

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        MaterialDatePicker.Builder<Pair<Long, Long>> builderRange = MaterialDatePicker.Builder.dateRangePicker();
        builderRange.setCalendarConstraints(limitRange().build());
        builderRange.setTitleText(getResources().getString(R.string.select_range_date));
        final MaterialDatePicker<Pair<Long, Long>> pickerRange = builderRange.build();


        text_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  DurationPickerDialog1();
                pickerRange.show(getFragmentManager(), pickerRange.toString());

            }
        });

        pickerRange.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                androidx.core.util.Pair date = (androidx.core.util.Pair) selection;

//                Log.i("TAG1", "save" + date.first.toString());
//                Log.i("TAG2", "save" + date.second.toString());

                String longFirst = date.first.toString();
                long millisecondFirst = Long.parseLong(longFirst);
                String longSecond = date.second.toString();
                long millisecondSecond = Long.parseLong(longSecond);

                firstDateShow = new SimpleDateFormat("dd-MM-yyyy").format(new Date(millisecondFirst));
                secondDateShow = new SimpleDateFormat("dd-MM-yyyy").format(new Date(millisecondSecond));


                firstDateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date(millisecondFirst));
                secondDateString = new SimpleDateFormat("yyyy-MM-dd").format(new Date(millisecondSecond));

//                Log.i("TAGLOG", "Start Date: " + firstDateString);
//                Log.i("TAGLOG", "End Date: " + secondDateString);


                recordsList.clear();
                recordsList.addAll(db.getAllRecordsByDayRangeMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                adapterLog.notifyDataSetChanged();

                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));

                text_selected.setText(firstDateShow + " - " + secondDateShow);

                days = formHelper.getDaysDiff(firstDateString, secondDateString) + 1;

                left.setEnabled(true);
                right.setEnabled(true);

                Log.i("TAGLOG", "Day: " + days);
            }
        });


        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DateFormatUtility df = new DateFormatUtility();

                FormHelper fh = new FormHelper();

                firstDateString = fh.minusDate(firstDateString, days);

                secondDateString = fh.minusDate(secondDateString, days);


//set humand read format
                firstDateShow = df.getStringDateFormat(df.getDateFormat2(firstDateString));
                secondDateShow = df.getStringDateFormat(df.getDateFormat2(secondDateString));
                text_selected.setText(firstDateShow + " - " + secondDateShow);

                //Refresh the list
                recordsList.clear();
                recordsList.addAll(db.getAllRecordsByDayRangeMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                adapterLog.notifyDataSetChanged();


                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));


                // Toast.makeText(getContext(), "left", Toast.LENGTH_SHORT).show();
                // Log.i("TAG", "Diff:" + days);
                // Log.i("TAG", "Start minus :" + minusdDateFirst);
                //  Log.i("TAG", "Start minus :" + secondDateString);

            }
        });


        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(), "right", Toast.LENGTH_SHORT).show();

                DateFormatUtility df = new DateFormatUtility();

                FormHelper fh = new FormHelper();

                firstDateString = fh.addDate(firstDateString, days);

                secondDateString = fh.addDate(secondDateString, days);

//set humand read format
                firstDateShow = df.getStringDateFormat(df.getDateFormat2(firstDateString));
                secondDateShow = df.getStringDateFormat(df.getDateFormat2(secondDateString));
                text_selected.setText(firstDateShow + " - " + secondDateShow);

                //Refresh the list
                recordsList.clear();
                recordsList.addAll(db.getAllRecordsByDayRangeMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                adapterLog.notifyDataSetChanged();


                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));


            }
        });


        return view;

    }

    public void intentView(View view) {

        //  cb_exit = (CheckBox) view.findViewById(R.id.cb_exit);
        cb_feed = (CheckBox) view.findViewById(R.id.cb_feed);
        cb_formula = (CheckBox) view.findViewById(R.id.cb_formula);
        cb_pump = (CheckBox) view.findViewById(R.id.cb_pump);
        cb_pumpbottle = (CheckBox) view.findViewById(R.id.cb_pumpbottle);
        cb_food = (CheckBox) view.findViewById(R.id.cb_food);
        cb_diaper = (CheckBox) view.findViewById(R.id.cb_diaper);
        cb_bath = (CheckBox) view.findViewById(R.id.cb_bath);
        cb_sleep = (CheckBox) view.findViewById(R.id.cb_sleep);
        cb_tummy = (CheckBox) view.findViewById(R.id.cb_tummy);
        cb_sunbathe = (CheckBox) view.findViewById(R.id.cb_sunbathe);
        cb_play = (CheckBox) view.findViewById(R.id.cb_play);
        cb_massage = (CheckBox) view.findViewById(R.id.cb_massage);
        cb_drink = (CheckBox) view.findViewById(R.id.cb_drink);
        cb_crying = (CheckBox) view.findViewById(R.id.cb_crying);
        cb_vaccination = (CheckBox) view.findViewById(R.id.cb_vaccination);
        cb_temperature = (CheckBox) view.findViewById(R.id.cb_temperature);
        cb_med = (CheckBox) view.findViewById(R.id.cb_med);
        cb_doctorvisit = (CheckBox) view.findViewById(R.id.cb_doctorvisit);
        cb_symptom = (CheckBox) view.findViewById(R.id.cb_symptom);
        cb_potty = (CheckBox) view.findViewById(R.id.cb_potty);

    }


    public void setInitActivityIdList() {
        activityIDList.clear();
        for (int i = 0; i <= 20; i++) {
            activityIDList.add(i);
        }
    }

    public void resetActivityList() {
        if (activityIDList.size() == 0) {
            setInitActivityIdList();
            isFirstTime = true;
            setAllButtonTrue();
        }

    }

    public void refreshView() {
        recordsList.clear();
        recordsList.addAll(db.getAllRecordsByDayRangeMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
        adapterLog.notifyDataSetChanged();
        totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
    }

    public void setChangeCheckBox() {

        cb_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_feed.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(1);
                    // isFreshChart = true;

                } else {

                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(1);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(1));
                    }
                    resetActivityList();

                }
                refreshView();
            }
        });


        cb_pump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_pump.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(2);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(2);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(2));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_formula.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_formula.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(3);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(3);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(3));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_pumpbottle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_pumpbottle.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(4);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(4);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(4));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_food.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(5);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(5);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(5));

                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_diaper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_diaper.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(6);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(6);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(6));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_bath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_bath.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(7);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(7);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(7));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_sleep.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(8);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(8);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(8));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_tummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_tummy.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(9);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(9);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(9));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_sunbathe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_sunbathe.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(10);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(10);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(10));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_play.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(11);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(11);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(11));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_massage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_massage.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(12);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(12);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(12));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_drink.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(13);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(13);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(13));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_crying.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_crying.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(14);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(14);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(14));

                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_vaccination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_vaccination.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(15);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(15);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(15));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_temperature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_temperature.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(16);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(16);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(16));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_med.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(17);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(17);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(17));
                    }

                    resetActivityList();
                }
                refreshView();
            }
        });


        cb_doctorvisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_doctorvisit.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(18);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(18);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(18));
                    }

                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_symptom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_symptom.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(19);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(19);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(19));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });

        cb_potty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isFirstTime) {
                    setAllButtonFalse();
                    cb_potty.setChecked(true);
                    isFirstTime = false;
                    activityIDList.clear();
                    activityIDList.add(20);

                } else {
                    if (((CheckBox) v).isChecked()) {
                        activityIDList.add(20);
                    } else {
                        activityIDList.remove(activityIDList.indexOf(20));
                    }
                    resetActivityList();
                }
                refreshView();
            }
        });


    }

    public void setAllButtonFalse() {

        cb_feed.setChecked(false);
        cb_formula.setChecked(false);
        cb_pump.setChecked(false);
        cb_pumpbottle.setChecked(false);
        cb_food.setChecked(false);
        cb_diaper.setChecked(false);
        cb_bath.setChecked(false);
        cb_sleep.setChecked(false);
        cb_tummy.setChecked(false);
        cb_sunbathe.setChecked(false);
        cb_play.setChecked(false);
        cb_massage.setChecked(false);
        cb_drink.setChecked(false);
        cb_crying.setChecked(false);
        cb_vaccination.setChecked(false);
        cb_temperature.setChecked(false);
        cb_med.setChecked(false);
        cb_doctorvisit.setChecked(false);
        cb_symptom.setChecked(false);
        cb_potty.setChecked(false);

    }

    public void setAllButtonTrue() {

        cb_feed.setChecked(true);
        cb_formula.setChecked(true);
        cb_pump.setChecked(true);
        cb_pumpbottle.setChecked(true);
        cb_food.setChecked(true);
        cb_diaper.setChecked(true);
        cb_bath.setChecked(true);
        cb_sleep.setChecked(true);
        cb_tummy.setChecked(true);
        cb_sunbathe.setChecked(true);
        cb_play.setChecked(true);
        cb_massage.setChecked(true);
        cb_drink.setChecked(true);
        cb_crying.setChecked(true);
        cb_vaccination.setChecked(true);
        cb_temperature.setChecked(true);
        cb_med.setChecked(true);
        cb_doctorvisit.setChecked(true);
        cb_symptom.setChecked(true);
        cb_potty.setChecked(true);

    }


    public void setCheckButton() {

        if (activityIDList.contains(1)) {
            cb_feed.setChecked(true);
        }
        if (activityIDList.contains(1)) {
            cb_formula.setChecked(true);
        }


    }


    //    private void DialogAddVolume() {
//        LayoutInflater inflater = getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.add_volume, null);
//        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
//
//        alert.setView(alertLayout);
//        alert.setCancelable(false);
//
//        final AlertDialog dialog = alert.create();
//
//
//
//        dialog.show();
//
//    }
    // dateRangePicker not selected date future
    private CalendarConstraints.Builder limitRange() {

        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        Calendar calendarStart = Calendar.getInstance();
        Calendar calendarEnd = Calendar.getInstance();

        int year = 2019;
        int startMonth = 6;
        int startDate = 5;

        calendarStart.set(year, startMonth - 1, startDate - 1);

        // calendarStart.add(Calendar.DAY_OF_YEAR,1);

        long minDate = calendarStart.getTimeInMillis();
        long maxDate = calendarEnd.getTimeInMillis();


        constraintsBuilderRange.setStart(minDate);
        constraintsBuilderRange.setEnd(maxDate);
        constraintsBuilderRange.setValidator(new RangeValidator(minDate, maxDate));

        return constraintsBuilderRange;
    }


    static class RangeValidator implements CalendarConstraints.DateValidator {

        long minDate, maxDate;

        RangeValidator(long minDate, long maxDate) {
            this.minDate = minDate;
            this.maxDate = maxDate;
        }

        RangeValidator(Parcel parcel) {
            minDate = parcel.readLong();
            maxDate = parcel.readLong();
        }

        @Override
        public boolean isValid(long date) {
            return !(minDate > date || maxDate < date);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(minDate);
            dest.writeLong(maxDate);
        }

        public static final Parcelable.Creator<RangeValidator> CREATOR = new Parcelable.Creator<RangeValidator>() {

            @Override
            public RangeValidator createFromParcel(Parcel parcel) {
                return new RangeValidator(parcel);
            }

            @Override
            public RangeValidator[] newArray(int size) {
                return new RangeValidator[size];
            }
        };


    }

    private void DurationPickerDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.drop_down_list, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        tx_cancle = (TextView) alertLayout.findViewById(R.id.tx_cancle);
        text1 = (TextView) alertLayout.findViewById(R.id.text1);
        text2 = (TextView) alertLayout.findViewById(R.id.text2);
        text3 = (TextView) alertLayout.findViewById(R.id.text3);
        text4 = (TextView) alertLayout.findViewById(R.id.text4);
        text5 = (TextView) alertLayout.findViewById(R.id.text5);
        text6 = (TextView) alertLayout.findViewById(R.id.text6);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        final AlertDialog dialog = alert.create();

        settingHelper.setTextColorDialog(getContext(), tx_cancle, currentUser.getUserTheme());

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //get record within days

                Calendar date = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

                secondDateString = dateFormat.format(date.getTime());
                secondDateShow = dateFormat2.format(date.getTime());
                date.add(Calendar.DAY_OF_MONTH, -6);

                firstDateString = dateFormat.format(date.getTime());
                firstDateShow = dateFormat2.format(date.getTime());
                days = 7;

                text_selected.setText(firstDateShow + " - " + secondDateShow);

                //Refresh the list
                recordsList.clear();
                recordsList.addAll(db.getAllRecordsByDayRangeMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                adapterLog.notifyDataSetChanged();

                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                left.setEnabled(true);
                right.setEnabled(true);
                //text_selected.setText(dateStart + " - " + dateEnd);
                dialog.dismiss();

            }
        });


        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar date = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
                secondDateString = dateFormat.format(date.getTime());
                secondDateShow = dateFormat2.format(date.getTime());
                date.add(Calendar.DAY_OF_MONTH, -13);

                firstDateString = dateFormat.format(date.getTime());
                firstDateShow = dateFormat2.format(date.getTime());

                days = 14;
                text_selected.setText(firstDateShow + " - " + secondDateShow);

                //Refresh the list
                recordsList.clear();
                recordsList.addAll(db.getAllRecordsByDayRangeMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                adapterLog.notifyDataSetChanged();
                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                left.setEnabled(true);
                right.setEnabled(true);

                dialog.dismiss();

            }
        });

        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//get record within 30 days

                Calendar date = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

                secondDateString = dateFormat.format(date.getTime());
                secondDateShow = dateFormat2.format(date.getTime());
                date.add(Calendar.DAY_OF_MONTH, -29);

                firstDateString = dateFormat.format(date.getTime());
                firstDateShow = dateFormat2.format(date.getTime());
                days = 30;

                text_selected.setText(firstDateShow + " - " + secondDateShow);

                //Refresh the list
                recordsList.clear();
                recordsList.addAll(db.getAllRecordsByDayRangeMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                adapterLog.notifyDataSetChanged();

                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));

                left.setEnabled(true);
                right.setEnabled(true);

                dialog.dismiss();

            }
        });

        text4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "this month", Toast.LENGTH_SHORT).show();
//get record for this month
                Calendar date = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

                secondDateString = dateFormat.format(date.getTime());
                secondDateShow = dateFormat2.format(date.getTime());
                date.setTime(date.getTime());
                date.set(Calendar.DATE, 1);
                firstDateString = dateFormat.format(date.getTime());
                firstDateShow = dateFormat2.format(date.getTime());
                days = formHelper.getDaysDiff(firstDateString, secondDateString);

                text_selected.setText(firstDateShow + " - " + secondDateShow);

                //Refresh the list
                recordsList.clear();
                recordsList.addAll(db.getAllRecordsByDayRangeMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                adapterLog.notifyDataSetChanged();

                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));

                left.setEnabled(true);
                right.setEnabled(true);

                // text_selected.setText(dateStart + " - " + dateEnd);
                dialog.dismiss();

            }
        });

        text5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get all record last month
                //  Toast.makeText(getContext(), "last month", Toast.LENGTH_SHORT).show();

                Calendar date = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                DateFormat dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");

                date.setTime(date.getTime());
                date.set(Calendar.DATE, 1);
                date.add(Calendar.MONTH, -1);
                firstDateString = dateFormat.format(date.getTime());
                firstDateShow = dateFormat2.format(date.getTime());

                Calendar cal = Calendar.getInstance();
                cal.setTime(cal.getTime());
                cal.add(Calendar.MONTH, -1);
                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

                secondDateString = dateFormat.format(cal.getTime());
                secondDateShow = dateFormat2.format(cal.getTime());

                days = formHelper.getDaysDiff(firstDateString, secondDateString);
                text_selected.setText(firstDateShow + " - " + secondDateShow);

                //Refresh the list
                recordsList.clear();
                recordsList.addAll(db.getAllRecordsByDayRangeMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                adapterLog.notifyDataSetChanged();

                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));

                left.setEnabled(true);
                right.setEnabled(true);

                dialog.dismiss();

            }
        });

        text6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getContext(), "since birthday", Toast.LENGTH_SHORT).show();
                DateFormatUtility dateFormatUtility = new DateFormatUtility();
                //  text_selected.setText(" since birthday ");

                Calendar date = Calendar.getInstance();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                secondDateString = dateFormat.format(date.getTime());

                firstDateString = currentUser.getUserBirthday();

                days = formHelper.getDaysDiff(firstDateString, secondDateString);
                text_selected.setText(firstDateString + " - " + secondDateString);

                recordsList.clear();
                recordsList.addAll(db.getAllRecordsByDayRangeMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                adapterLog.notifyDataSetChanged();

                totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));

                left.setEnabled(false);
                right.setEnabled(false);


                dialog.dismiss();
            }
        });


        tx_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.show();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_UPDATE_RECORD) {
            if (resultCode == Activity.RESULT_OK) {
                isDelete = data.getExtras().getBoolean(IS_DELETE, false);
                if (isDelete) {

                    if (googleUser == null) {
                        int position = data.getExtras().getInt(POSITION);
                        checkNoDataView();
                        totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
                        recordsList.remove(position);
                        adapterLog.notifyDataSetChanged();
                        Log.i("TAG", "Delete at Index ... " + position);
                    }


                } else {
                    Record result = (Record) data.getExtras().getSerializable(EXTRA_DATA);
                    int position = data.getExtras().getInt(POSITION);
                    checkNoDataView();
                    totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));
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
        if (db.getRecordCount(localDataHelper.getActiveUserId()) == 0) {
            noData_layout.setVisibility(View.VISIBLE);
            //recyclerView.setVisibility(View.GONE);
        } else {

            noData_layout.setVisibility(View.GONE);
            //   recyclerView.setVisibility(View.VISIBLE);

        }


    }


    public void launchActivity(int activityID, int position) {

        if (activityID == 1) {
            Intent intent = new Intent(getContext(), BreastfeedActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 2) {

            Intent intent = new Intent(getContext(), PumpActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);
        } else if (activityID == 3) {

            Intent intent = new Intent(getContext(), FormulaActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            // startActivity(intent);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);
        } else if (activityID == 4) {
            Intent intent = new Intent(getContext(), BottlePumpedActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);
            // startActivity(intent);
        } else if (activityID == 5) {
            Intent intent = new Intent(getContext(), FoodActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);
            // startActivity(intent);
        } else if (activityID == 6) {
            Intent intent = new Intent(getContext(), DiaperActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 7) {
            Intent intent = new Intent(getContext(), BathActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 8) {
            Intent intent = new Intent(getContext(), SleepActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);
        } else if (activityID == 9) {
            Intent intent = new Intent(getContext(), TummyTimeActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 10) {
            Intent intent = new Intent(getContext(), SunbathTimeActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 11) {

            Intent intent = new Intent(getContext(), PlayActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 12) {

            Intent intent = new Intent(getContext(), MassageActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 13) {

            Intent intent = new Intent(getContext(), DrinkActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 14) {

            Intent intent = new Intent(getContext(), CryingActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 15) {

            Intent intent = new Intent(getContext(), VaccinationActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 16) {

            Intent intent = new Intent(getContext(), TemperatureActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 17) {

            Intent intent = new Intent(getContext(), MedicineActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 18) {

            Intent intent = new Intent(getContext(), DoctorVisitActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 19) {

            Intent intent = new Intent(getContext(), SymptomActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        } else if (activityID == 20) {

            Intent intent = new Intent(getContext(), PottyActivity.class);
            intent.putExtra(IS_UPDATE, true);
            intent.putExtra(RECORD, recordsList.get(position));
            // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
            intent.putExtra(POSITION, position);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_RECORD);

        }

    }



    //Start realtime record update

    public void pullRecordByUserRealtime(User user) {


        DatabaseReference recordRef;

        if (googleUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            // DatabaseReference growthRef;
            DatabaseReference userRef;
            if (user.getRequestStatus().equals("Accept")) {

                userRef = database.getReference(user.getCreatedBy() + "/User");
                // recordRef = userRef.child(user.getUserId() + "/Record");

            } else {

                userRef = database.getReference(googleUser.getUid() + "/User");

            }

            recordRef = userRef.child(user.getUserId() + "/Record");
            //Pull all growth first and add it in SQLite. When it finish will push the remain havent syn growth. When finish push will startSynchronizedGrowthPhoto


            recordRef.addChildEventListener(new ChildEventListener() {


                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                    RecordFirebaseObject recordFirebaseObject = snapshot.getValue(RecordFirebaseObject.class);

                    if (!googleUser.getUid().equals(recordFirebaseObject.getCreatedBy())) {
                        Record updateRecord = convertToRecord(recordFirebaseObject);
                        Log.i("CARE", "Pulling Add Realtime Record data From other user: " + recordFirebaseObject.getRecordId());
                        addRecordToSQLite(updateRecord);
                    }


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    RecordFirebaseObject recordFirebaseObject = snapshot.getValue(RecordFirebaseObject.class);

                    if (!googleUser.getUid().equals(recordFirebaseObject.getCreatedBy())) {

                        Record updateRecord = convertToRecord(recordFirebaseObject);

                        for (int i = 0; i < recordsList.size(); i++) {
                            if (recordsList.get(i).getRecordId().equals(updateRecord.getRecordId())) {
                                recordsList.set(i, updateRecord);
                                // adapterLog.notifyItemRemoved(i);
                                adapterLog.notifyDataSetChanged();
                                break;
                            }
                        }
                        db.updateRecord(updateRecord);
                    }


                    Log.i("CARE", "Pulling Update Realtime Record data: " + recordFirebaseObject.getRecordId());

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    RecordFirebaseObject recordFirebaseObject = snapshot.getValue(RecordFirebaseObject.class);
                    // Record updateRecord = convertToRecord(recordFirebaseObject);
                    Log.i("CARE", "Pulling Remove Realtime Record data: " + recordFirebaseObject.getRecordId());

                    for (int i = 0; i < recordsList.size(); i++) {
                        if (recordsList.get(i).getRecordId().equals(recordFirebaseObject.getRecordId())) {
                            recordsList.remove(i);
                            // adapterLog.notifyItemRemoved(i);
                            adapterLog.notifyDataSetChanged();
                            break;
                        }
                    }
                    db.deleteRecordByID(recordFirebaseObject.getRecordId());
                    totalActivities.setText(getResources().getString(R.string.total_activities) + db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString));


                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }


    public Record convertToRecord(RecordFirebaseObject recordFirebaseObject) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility();
        Record updateRecord = new Record();

        Date dateStart = (recordFirebaseObject.getDateStart() == null) ? null : dateFormatUtility.getDateFormat2(recordFirebaseObject.getDateStart());
        Date timeStart = (recordFirebaseObject.getTimeStart() == null) ? null : dateFormatUtility.getTimeFormat2(recordFirebaseObject.getTimeStart());
        Date duration = (recordFirebaseObject.getDuration() == null) ? null : dateFormatUtility.getTimeFormat2(recordFirebaseObject.getDuration());

        updateRecord.setRecordId(recordFirebaseObject.getRecordId());
        updateRecord.setOption(recordFirebaseObject.getOption());
        updateRecord.setAmount(recordFirebaseObject.getAmount());
        updateRecord.setAmountUnit(recordFirebaseObject.getAmountUnit());
        updateRecord.setDateStart(dateStart);
        updateRecord.setTimeStart(timeStart);
        updateRecord.setDateEnd(dateFormatUtility.getDateFormat2(recordFirebaseObject.getDateEnd()));
        updateRecord.setTimeEnd(dateFormatUtility.getTimeFormat2(recordFirebaseObject.getTimeEnd()));
        updateRecord.setDuration(duration);
        updateRecord.setNote(recordFirebaseObject.getNote());
        updateRecord.setActivitiesId(recordFirebaseObject.getActivitiesId());
        updateRecord.setUserId(recordFirebaseObject.getUserId());
        updateRecord.setSyn(true);
        updateRecord.setRecordStatus(null);
        updateRecord.setCreatedBy(recordFirebaseObject.getCreatedBy());
        updateRecord.setRecordCreatedDatetime(recordFirebaseObject.getRecordCreatedDatetime());

        return updateRecord;
    }


    public int getIndexOfRecord(Record record) {

        int indexOfUser = 0;
        if (recordsList.size() > 0) {
            for (int i = 0; i < recordsList.size(); i++) {
                if (recordsList.get(i).getRecordId().equals(record.getRecordId())) {
                    indexOfUser = i;

                }

            }
        }

        return indexOfUser;


    }


    public void addRecordToSQLite(Record record) {

        //db.deleteAllRecordByUser(currentUser);
        recordsList.clear();
        recordsList.addAll(db.getAllRecordsByDayRangeMulti(localDataHelper.getActiveUserId(), activityIDList, firstDateString, secondDateString));

        if (!db.checkIsRecordIsExist(record.getRecordId())) {

            db.addRecord(record);
            if (isWithinRange(record.getDateEnd())) {
                recordsList.add(record);

            }


        } else {

            if (db.getRecord(record.getRecordId()).getSyn()) {
                db.updateRecord(record);
                if (isWithinRange(record.getDateEnd())) {
                    recordsList.set(getIndexOfRecord(record), record);
                }

            }

            //userList.add(updateUser);

        }


        totalActivities.setText(String.format("%s%d", getResources().getString(R.string.total_activities), db.getRecordByDateRangeCountMulti(currentUser.getUserId(), activityIDList, firstDateString, secondDateString)));
        adapterLog.notifyDataSetChanged();

    }


    public boolean isWithinRange(Date testDate) {
        return !(testDate.before(dateFormatUtility.getDateFormat2(firstDateString)) || testDate.after(dateFormatUtility.getDateFormat2(secondDateString)));
    }


}