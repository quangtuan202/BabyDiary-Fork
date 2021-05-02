package com.riagon.babydiary;


import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Activities;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.RecordFirebaseObject;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Notification.BreastFeedLeftReceiver;
import com.riagon.babydiary.Notification.BreastFeedRightReceiver;
import com.riagon.babydiary.Notification.NotificationTimer;
import com.riagon.babydiary.Notification.NotificationType;
import com.riagon.babydiary.Utility.ActivitiesAdapter;
import com.riagon.babydiary.Utility.BreedService;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.LocalDataHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ActivitiesAdapter adapterActivities;
    private List<Activities> activitiesList;
    public DatabaseHelper db;
    private LocalDataHelper localDataHelper;
    private User currentUser;
   // private Boolean isDarkMode;
    private LinearLayout homeLayout;
    private FloatingActionButton floatingActionButton;
    public BroadcastReceiver sleepReceiver;
    public BroadcastReceiver tummyReceiver;
    public BroadcastReceiver sunbathReceiver;
    public BroadcastReceiver breedReceiver;
    public BroadcastReceiver pumpReceiver;
    public Intent breedService;

    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseUser googleUser;

    //  public String sleepRunningStatus = "Stop";

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        db = new DatabaseHelper(getContext());
        localDataHelper = new LocalDataHelper(getContext());
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        if (db.getActivitiesCount(currentUser.getUserId()) == 0) {
            db.prepareCategoryList(currentUser.getUserId());
        }

        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        googleUser = mAuth.getCurrentUser();


        breedService = new Intent(getContext(), BreedService.class);

        homeLayout = (LinearLayout) view.findViewById(R.id.home_layout);


        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(getActivity().getApplicationContext(), "You click the floating action button.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), TodayLogActivity.class);
                startActivity(intent);

            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        activitiesList = new ArrayList<>();
        activitiesList.clear();
        activitiesList.addAll(db.getAllActivitiesToShow(currentUser.getUserId()));

        pullRecordByUserRealtime(currentUser);
        adapterActivities = new ActivitiesAdapter(getContext(), db, this);


        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterActivities);


        return view;



    }

    @Override
    public void onResume() {
        Log.d("Tuan home fragment:" ,"onResume");
        super.onResume();
        activitiesList.clear();
        activitiesList.addAll(db.getAllActivitiesToShow(currentUser.getUserId()));
        adapterActivities = new ActivitiesAdapter(getContext(), db, this);
        recyclerView.setAdapter(adapterActivities);


    }


    @Override
    public void onStart() {
        super.onStart();
        Log.i("LIFE", "onStart");
        Log.d("Tuan home fragment:" ,"onStart");
    }


    @Override
    public void onStop() {
        super.onStop();
        Log.d("Tuan home fragment:" ,"onStop");

      /*  //   getContext().stopService(breedService);
        Log.i("LIFE", "Onstop");
        if (sleepReceiver != null) {
            getContext().unregisterReceiver(sleepReceiver);
        }
        if (tummyReceiver != null) {
            getContext().unregisterReceiver(tummyReceiver);
        }
        if (sunbathReceiver != null) {
            getContext().unregisterReceiver(sunbathReceiver);
        }
        if (breedReceiver != null && activitiesList.get(0).getShow()) {
            getContext().unregisterReceiver(breedReceiver);
        }
        if (pumpReceiver != null) {
            getContext().unregisterReceiver(pumpReceiver);
        }*/





    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("LIFE", "onPause");
        Log.d("Tuan home fragment:" ,"onPause");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("LIFE", "onDestroy");
        Log.d("Tuan home fragment:" ,"onDestroy");


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

                        activitiesList.clear();
                        activitiesList.addAll(db.getAllActivitiesToShow(currentUser.getUserId()));
                        adapterActivities.notifyDataSetChanged();

                    }


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    RecordFirebaseObject recordFirebaseObject = snapshot.getValue(RecordFirebaseObject.class);

                   if (!googleUser.getUid().equals(recordFirebaseObject.getCreatedBy())) {

                        Record updateRecord = convertToRecord(recordFirebaseObject);

                        db.updateRecord(updateRecord);

                        activitiesList.clear();
                        activitiesList.addAll(db.getAllActivitiesToShow(currentUser.getUserId()));
                        adapterActivities.notifyDataSetChanged();
                }


                    Log.i("CARE", "Pulling Update Realtime Record data: " + recordFirebaseObject.getRecordId());

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    RecordFirebaseObject recordFirebaseObject = snapshot.getValue(RecordFirebaseObject.class);
                    // Record updateRecord = convertToRecord(recordFirebaseObject);
                    Log.i("CARE", "Pulling Remove Realtime Record data: " + recordFirebaseObject.getRecordId());

                    db.deleteRecordByID(recordFirebaseObject.getRecordId());

                    activitiesList.clear();
                    activitiesList.addAll(db.getAllActivitiesToShow(currentUser.getUserId()));
                    adapterActivities.notifyDataSetChanged();


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



    public void addRecordToSQLite(Record record) {

        //db.deleteAllRecordByUser(currentUser);

        if (!db.checkIsRecordIsExist(record.getRecordId())) {

            db.addRecord(record);

        } else {

            if (db.getRecord(record.getRecordId()).getSyn()) {
                db.updateRecord(record);
            }

            //userList.add(updateUser);

        }


        activitiesList.clear();
        activitiesList.addAll(db.getAllActivitiesToShow(currentUser.getUserId()));
        adapterActivities.notifyDataSetChanged();

    }




}
