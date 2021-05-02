package com.riagon.babydiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Growth;
import com.riagon.babydiary.Model.OnIntentReceived;
import com.riagon.babydiary.Model.Photo;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.RecordFirebaseObject;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.DbBitmapUtility;
import com.riagon.babydiary.Utility.GrowthListAdapter;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.RecyclerTouchListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class GrowthListFragment extends Fragment {
    private RecyclerView recyclerView;
    // private Context mContext;
    private GrowthListAdapter adapterGrowthList;
    private List<Growth> growthList;
    private FloatingActionButton floatingActionButton;
    private LinearLayout noData_layout;
    private Boolean isEmpty = false;
    private DatabaseHelper db;
    private LocalDataHelper localDataHelper;
    private static final int REQUEST_CODE_ADD_GROWTH = 02051;
    private static final int REQUEST_CODE_UPDATE_GROWTH = 02052;
    private Boolean isDelete;
    private Boolean isDuplicate;
    private User activeUser;

    private FirebaseAuth mAuth;
    // [END declare_auth]
    private FirebaseUser googleUser;

    private OnIntentReceived mIntentListener;


    public GrowthListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_growth_list, container, false);
        db = new DatabaseHelper(getContext());
        localDataHelper = new LocalDataHelper(getContext());
        noData_layout = (LinearLayout) view.findViewById(R.id.growth_list_no_dada);
        activeUser = db.getUser(localDataHelper.getActiveUserId());

        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
        googleUser = mAuth.getCurrentUser();

        AddGrowthRecordActivity.clearCache(getContext());

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity().getApplicationContext(),"You click the floating action button.", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), AddGrowthRecordActivity.class);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_ADD_GROWTH);

            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_growth_list);
        growthList = new ArrayList<>();
        // prepareGrowthList();

        checkNoDataView();

        growthList.addAll(db.getAllGrowths(activeUser));


        adapterGrowthList = new GrowthListAdapter(getContext(), growthList, activeUser, db);

        pullGrowthByUserRealtime(activeUser);
        // pullPhotosByUserRealtime(activeUser);


        recyclerView.setAdapter(adapterGrowthList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
//        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(),
                recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, final int position) {

                //  Toast.makeText(getActivity().getApplicationContext(),String.valueOf(recordList.get(position).getRecordId()), Toast.LENGTH_SHORT).show();
                // showCustomDialog(true, unitSetting, position);
                Intent intent = new Intent(getContext(), AddGrowthRecordActivity.class);
                intent.putExtra(AddGrowthRecordActivity.IS_UPDATE, true);
                intent.putExtra(AddGrowthRecordActivity.GROWTH_ID, growthList.get(position));
                // Toast.makeText(getActivity().getApplicationContext(),String.valueOf(growthList.get(position).getGrowthId()), Toast.LENGTH_SHORT).show();
                intent.putExtra(AddGrowthRecordActivity.POSITION, position);
                //startActivity(intent);
                startActivityForResult(intent, REQUEST_CODE_UPDATE_GROWTH);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_ADD_GROWTH) {

            if (resultCode == Activity.RESULT_OK) {

                Growth result = (Growth) data.getExtras().getSerializable(AddGrowthRecordActivity.EXTRA_DATA);
                int position = data.getExtras().getInt(AddGrowthRecordActivity.POSITION);
                checkNoDataView();
                growthList.add(position, result);
                adapterGrowthList.notifyDataSetChanged();
                Log.i("TAG", "Add at Index ... " + position);


            } else {
// DetailActivity không thành công, không có data trả về.
            }
        } else if (requestCode == REQUEST_CODE_UPDATE_GROWTH) {
            if (resultCode == Activity.RESULT_OK) {
                isDelete = data.getExtras().getBoolean(AddGrowthRecordActivity.IS_DELETE, false);
                if (isDelete) {
                    int position = data.getExtras().getInt(AddGrowthRecordActivity.POSITION);
                    checkNoDataView();
                    growthList.remove(position);
                    adapterGrowthList.notifyDataSetChanged();
                    Log.i("TAG", "Delete at Index ... " + position);
                } else {
                    Growth result = (Growth) data.getExtras().getSerializable(AddGrowthRecordActivity.EXTRA_DATA);
                    int position = data.getExtras().getInt(AddGrowthRecordActivity.POSITION);
                    checkNoDataView();
                    growthList.set(position, result);
                    adapterGrowthList.notifyDataSetChanged();
                    Log.i("TAG", "Update at Index ... " + position);
                }


            } else {
                // DetailActivity không thành công, không có data trả về.
            }
        }


    }

    private void checkNoDataView() {
        if (db.getGrowthsCount(activeUser) == 0) {
            noData_layout.setVisibility(View.VISIBLE);
            //recyclerView.setVisibility(View.GONE);
        } else {

            noData_layout.setVisibility(View.GONE);
            //   recyclerView.setVisibility(View.VISIBLE);

        }


    }


    @Override
    public void onResume() {
        super.onResume();
        //adapterGrowthList.notifyDataSetChanged();

    }


    //Start realtime growth update

    public void pullGrowthByUserRealtime(User user) {


        DatabaseReference growthRef;

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

            growthRef = userRef.child(user.getUserId() + "/Growth");
            //Pull all growth first and add it in SQLite. When it finish will push the remain havent syn growth. When finish push will startSynchronizedGrowthPhoto


            growthRef.addChildEventListener(new ChildEventListener() {


                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                    Growth growth = snapshot.getValue(Growth.class);

                    if (!googleUser.getUid().equals(growth.getCreatedBy())) {

                        Log.i("CARE", "Pulling Add Realtime Growth data From other user: " + growth.getGrowthId());
                        addGrowthToSQLite(growth);
                    }


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Growth growth = snapshot.getValue(Growth.class);

                    if (!googleUser.getUid().equals(growth.getCreatedBy())) {

                        for (int i = 0; i < growthList.size(); i++) {
                            if (growthList.get(i).getGrowthId().equals(growth.getGrowthId())) {
                                growthList.set(i, growth);
                                // adapterGrowthList.notifyItemRemoved(i);
                                adapterGrowthList.notifyDataSetChanged();
                                break;
                            }
                        }
                        // adapterGrowthList.notifyDataSetChanged();
                        db.updateGrowth(growth);
                    }


                    Log.i("CARE", "Pulling Update Realtime Growth data: " + growth.getGrowthId());

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    Growth growth = snapshot.getValue(Growth.class);
                    // Record updateRecord = convertToRecord(recordFirebaseObject);
                    Log.i("CARE", "Pulling Remove Realtime Growth data: " + growth.getGrowthId());

                    for (int i = 0; i < growthList.size(); i++) {
                        if (growthList.get(i).getGrowthId().equals(growth.getGrowthId())) {
                            growthList.remove(i);
                            // adapterLog.notifyItemRemoved(i);
                            adapterGrowthList.notifyDataSetChanged();
                            break;
                        }
                    }
                    db.deleteGrowthId(growth.getGrowthId());


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


    public int getIndexOfGrowth(Growth growth) {

        int indexOfGrowth = 0;
        if (growthList.size() > 0) {
            for (int i = 0; i < growthList.size(); i++) {
                if (growthList.get(i).getGrowthId().equals(growth.getGrowthId())) {
                    indexOfGrowth = i;

                }

            }
        }

        return indexOfGrowth;


    }


    public void addGrowthToSQLite(Growth growth) {

        //db.deleteAllRecordByUser(currentUser);
        growthList.clear();
        growthList.addAll(db.getAllGrowths(activeUser));

        if (!db.checkIsGrowthIsExist(growth.getGrowthId())) {
            db.addGrowth(growth);
            growthList.add(growth);
        } else {

            if (db.getGrowth(growth.getGrowthId()).getSyn()) {
                db.updateGrowth(growth);
                growthList.set(getIndexOfGrowth(growth), growth);
            }

            adapterGrowthList.notifyDataSetChanged();
            //userList.add(updateUser);

        }

    }


    //Start photo realtime


    public void pullPhotosByUserRealtime(User user) {

        DatabaseReference photoRef;
        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();

        if (googleUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            // DatabaseReference growthRef;
            DatabaseReference userRef;
            if (user.getRequestStatus().equals("Accept")) {

                userRef = database.getReference(user.getCreatedBy() + "/User");


            } else {

                userRef = database.getReference(googleUser.getUid() + "/User");

            }

            photoRef = userRef.child(user.getUserId() + "/Photo");


            //Pull all growth first and add it in SQLite. When it finish will push the remain havent syn growth. When finish push will startSynchronizedGrowthPhoto

            photoRef.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {


                    Photo firebasePhoto = snapshot.getValue(Photo.class);

                    if (!googleUser.getUid().equals(firebasePhoto.getCreatedBy())) {
                        Log.i("CARE", "Pulling Update Realtime Photo data: " + firebasePhoto.getPhotoId());

                        Photo updatePhoto = new Photo();
                        String url = firebasePhoto.getPhotoImageUrl();

                        if (url != null) {
                            Glide.with(getActivity())
                                    .asBitmap()
                                    .load(url)
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            // count=1;
                                            // Log.i("CARE", "Convert Photo URL to bitmap " + url);
                                            updatePhoto.setPhotoImage(dbBitmapUtility.getBytes(resource));
                                            updatePhoto.setPhotoId(firebasePhoto.getPhotoId());
                                            updatePhoto.setSyn(true);
                                            updatePhoto.setRecordStatus(null);
                                            updatePhoto.setCreatedBy(firebasePhoto.getCreatedBy());
                                            updatePhoto.setGrowthId(firebasePhoto.getGrowthId());
                                            updatePhoto.setUserId(firebasePhoto.getUserId());
                                            updatePhoto.setCreatedDatetime(firebasePhoto.getCreatedDatetime());


                                            addPhotoToSQLite(updatePhoto);


                                            //    adapterAccount.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });
                        }

                    }


                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    Photo photo = snapshot.getValue(Photo.class);

                    if (!googleUser.getUid().equals(photo.getCreatedBy())) {

//                        for (int i = 0; i < photoList.size(); i++) {
//                            if (photoList.get(i).getPhotoId().equals(photo.getPhotoId())) {
//                                photoList.set(i, photo);
//                                // adapterLog.notifyItemRemoved(i);
//                                adapterPhoto.notifyDataSetChanged();
//                                break;
//                            }
//                        }
                        db.updatePhoto(photo);
                        adapterGrowthList.notifyItemChanged(getIndexOfGrowthByPhoto(photo.getGrowthId()));
                    }


                    Log.i("CARE", "Pulling Update Realtime Photo data: " + photo.getGrowthId());


                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    Photo photo = snapshot.getValue(Photo.class);
                    // Record updateRecord = convertToRecord(recordFirebaseObject);
                    Log.i("CARE", "Pulling Remove Realtime Photo data: " + photo.getPhotoId());

//                    for (int i = 0; i < photoList.size(); i++) {
//                        if (photoList.get(i).getPhotoId().equals(photo.getPhotoId())) {
//                            photoList.remove(i);
//                            // adapterLog.notifyItemRemoved(i);
//                            adapterPhoto.notifyDataSetChanged();
//                            break;
//                        }
//                    }
                    db.deletePhotoID(photo);
                    adapterGrowthList.notifyItemChanged(getIndexOfGrowthByPhoto(photo.getGrowthId()));


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


    public void addPhotoToSQLite(Photo photo) {

        if (!db.checkIsPhotoIsExist(photo.getPhotoId())) {

            db.addPhoto(photo);
            adapterGrowthList.notifyItemChanged(getIndexOfGrowthByPhoto(photo.getGrowthId()));

        } else {

            if (db.getPhoto(photo.getPhotoId()).getSyn()) {
                db.updatePhoto(photo);

            }
            //userList.add(updateUser);

        }

    }


    public int getIndexOfGrowthByPhoto(String growthID) {

        int indexOfGrowth = 0;
        if (growthList.size() > 0) {
            for (int i = 0; i < growthList.size(); i++) {
                if (growthList.get(i).getGrowthId().equals(growthID)) {
                    indexOfGrowth = i;

                }

            }
        }

        return indexOfGrowth;


    }


}


