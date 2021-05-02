package com.riagon.babydiary.Firebase;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Growth;
import com.riagon.babydiary.Model.User;

import java.util.ArrayList;
import java.util.List;

public class GrowthFirebase {
    private Context context;
    private FirebaseUser currentUser;
    private DatabaseHelper db;
    private DatabaseReference growthRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    public GrowthFirebase(Context context, FirebaseUser currentUser) {
        this.currentUser = currentUser;
        this.context = context;
        db = new DatabaseHelper(context);
        storageRef = storage.getReference();
    }


    public GrowthFirebase() {
    }

    public void initGrowthFirebaseValue(Growth growth) {
        if (currentUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            // DatabaseReference growthRef;

            if (!db.getUser(growth.getUserId()).getRequestStatus().equals("Accept")) {
                DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");
                growthRef = userRef.child(growth.getUserId());
            } else {

                DatabaseReference userRef = database.getReference(db.getUser(growth.getUserId()).getCreatedBy() + "/User");
                growthRef = userRef.child(growth.getUserId());

            }
        }

    }


    public Boolean pushGrowthToFirebase(Growth growth) {

        final Boolean[] isSuccess = {false};


        if (currentUser != null) {
            initGrowthFirebaseValue(growth);
            Growth firebaseGrowth = new Growth();
            firebaseGrowth.setGrowthId(growth.getGrowthId());
            firebaseGrowth.setGrowthUnit(growth.getGrowthUnit());
            firebaseGrowth.setGrowthHead(growth.getGrowthHead());
            firebaseGrowth.setGrowthWeight(growth.getGrowthWeight());
            firebaseGrowth.setGrowthLength(growth.getGrowthLength());
            firebaseGrowth.setGrowthDate(growth.getGrowthDate());
            firebaseGrowth.setGrowthTime(growth.getGrowthTime());
            firebaseGrowth.setUserId(growth.getUserId());
            firebaseGrowth.setGrowthNote(growth.getGrowthNote());
            firebaseGrowth.setCreatedBy(currentUser.getUid());

            growthRef.child("Growth").child(firebaseGrowth.getGrowthId()).setValue(firebaseGrowth).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    db.updateGrowthSyn(growth, true, currentUser.getUid());
                    isSuccess[0] = true;
                    // Write was successful!
                    // ...
                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Write failed
                            // ...
                        }
                    });


        }
        return isSuccess[0];


    }


    public void deleteGrowthFirebase(Growth growth) {
        initGrowthFirebaseValue(growth);
        growthRef.child("Growth").child(growth.getGrowthId()).removeValue();

    }


    public Boolean startSynchronizedGrowthByUser() {
        //int userCounter = 0;
        Boolean isComplete = false;
        List<User> userList = new ArrayList<>();
        userList.addAll(db.getAllUsers());

        for (User user : userList) {

            pullGrowthByUserToFirebase(user);
//            Log.d("CARE", "Start push Growth Of " + user.getUserName());
//            List<Growth> growthList = db.getAllGrowthNotSynByUser(user.getUserId());
//            if (uploadGrowthDataByList(growthList)) {
//                userCounter++;
//                if (userCounter == userList.size()) {
//                    Log.d("CARE", "COMPLETE PUSH ALL GROWTH FOR EACH USER");
//                    isComplete = true;
//                }
//            }

        }

        return isComplete;

    }


    public void pullGrowthByUserToFirebase(User user) {

        final long[] growthDowloadedCounter = {0};

        DatabaseReference growthRef;

        if (currentUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            // DatabaseReference growthRef;

            if (user.getRequestStatus().equals("Accept")) {
                DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");
                growthRef = userRef.child(user.getUserId() + "/Growth");
            } else {

                DatabaseReference userRef = database.getReference(user.getCreatedBy() + "/User");
                growthRef = userRef.child(user.getUserId() + "/Growth");

            }

            //Pull all growth first and add it in SQLite. When it finish will push the remain havent syn growth. When finish push will startSynchronizedGrowthPhoto

            growthRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshots) {

                    long maxID = snapshots.getChildrenCount();
                    if (maxID == 0) {
                        pushGrowthByUserToFirebase(user);
                    } else {

                        for (DataSnapshot snapshot : snapshots.getChildren()) {

                            Growth growth = snapshot.getValue(Growth.class);
                            Growth updateGrowth = new Growth();

                            updateGrowth.setGrowthId(growth.getGrowthId());
                            updateGrowth.setGrowthUnit(growth.getGrowthUnit());
                            updateGrowth.setGrowthWeight(growth.getGrowthWeight());
                            updateGrowth.setGrowthLength(growth.getGrowthLength());
                            updateGrowth.setGrowthHead(growth.getGrowthHead());
                            updateGrowth.setGrowthNote(growth.getGrowthNote());
                            updateGrowth.setSyn(true);
                            updateGrowth.setRecordStatus(null);
                            updateGrowth.setCreatedBy(growth.getCreatedBy());
                            updateGrowth.setUserId(growth.getUserId());
                            updateGrowth.setGrowthDate(growth.getGrowthDate());
                            updateGrowth.setGrowthTime(growth.getGrowthTime());


                            growthDowloadedCounter[0]++;
                            addGrowthToSQLite(updateGrowth);

                            Log.i("CARE", "Pulling Growth data: " + updateGrowth.getGrowthId());

                            if (growthDowloadedCounter[0] == maxID) {
                                Log.i("CARE", "Pulling Growth data completed ");
                                // pushUserToFirebase(user);
                                pushGrowthByUserToFirebase(user);

                            }


                        }


                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        }


    }


    public void addGrowthToSQLite(Growth growth) {

        if (!db.checkIsGrowthIsExist(growth.getGrowthId())) {

            db.addGrowth(growth);

        } else {

            if (db.getGrowth(growth.getGrowthId()).getSyn()) {
                db.updateGrowth(growth);
            }
            //userList.add(updateUser);

        }

    }


    public void pushGrowthByUserToFirebase(User user) {
        //    int userCounter = 0;
        //  Boolean isComplete = false;

//        List<User> userList = new ArrayList<>();
//        userList.clear();
//        userList.addAll(db.getAllUsers());

        //  for (User user : userList) {
        Log.d("CARE", "Start push Growth Of " + user.getUserName());
        List<Growth> growthList = db.getAllGrowthNotSynByUser(user.getUserId());
        //  if (uploadGrowthDataByList(growthList)) {

        uploadGrowthDataByList(growthList);
//            userCounter++;
//            if (userCounter == growthList.size()) {
//                Log.d("CARE", "COMPLETE PUSH ALL GROWTH FOR USER " + user.getUserName());
//              //  isComplete = true;
//            }
        ///  }

    }

    // return isComplete;

    // }


    public Boolean uploadGrowthDataByList(List<Growth> growthList) {

        int growthCounter = 0;
        Boolean isComplete = false;

        for (Growth growth : growthList) {
            //addGrowthFirebase(growth);

            if (pushGrowthToFirebase(growth)) {
                growthCounter++;
                if (growthCounter == growthList.size()) {
                    Log.d("CARE", "COMPLETE PUSH ALL GROWTH FOR USER ");
                }
            }


        }
        return isComplete;


    }


}
