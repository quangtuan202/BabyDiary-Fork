package com.riagon.babydiary.Firebase;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Growth;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.RecordFirebaseObject;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.LogAdapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecordFirebase {

    private Context context;
    private FirebaseUser currentUser;
    private DatabaseHelper db;
    private DatabaseReference recordRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;

    public RecordFirebase(Context context, FirebaseUser currentUser) {
        this.currentUser = currentUser;
        this.context = context;
        db = new DatabaseHelper(context);
        storageRef = storage.getReference();
    }


    public RecordFirebase() {
    }


    public void initRecordFirebaseValue(Record record) {
        if (currentUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            // DatabaseReference growthRef;

            if (!db.getUser(record.getUserId()).getRequestStatus().equals("Accept")) {
                DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");
                recordRef = userRef.child(record.getUserId());
            } else {

                DatabaseReference userRef = database.getReference(db.getUser(record.getUserId()).getCreatedBy() + "/User");
                recordRef = userRef.child(record.getUserId());

            }
        }

    }


    public Boolean pushSingleRecordToFirebase(Record record) {

        final Boolean[] isSuccess = {false};
        DateFormatUtility dateFormatUtility = new DateFormatUtility();


        if (currentUser != null) {
            initRecordFirebaseValue(record);
            RecordFirebaseObject firebaseRecord = new RecordFirebaseObject();

            String dateStart = (record.getDateStart() == null) ? null : dateFormatUtility.getStringDateFormat2(record.getDateStart());
            String timeStart = (record.getTimeStart() == null) ? null : dateFormatUtility.getStringTimeFormat2(record.getTimeStart());
            String duration = (record.getDuration() == null) ? null : dateFormatUtility.getStringTimeFormat2(record.getDuration());

            firebaseRecord.setRecordId(record.getRecordId());
            firebaseRecord.setOption(record.getOption());
            firebaseRecord.setAmount(record.getAmount());
            firebaseRecord.setAmountUnit(record.getAmountUnit());
            firebaseRecord.setDateStart(dateStart);
            firebaseRecord.setTimeStart(timeStart);
            firebaseRecord.setDateEnd(dateFormatUtility.getStringDateFormat2(record.getDateEnd()));
            firebaseRecord.setTimeEnd(dateFormatUtility.getStringTimeFormat2(record.getTimeEnd()));
            firebaseRecord.setDuration(duration);
            firebaseRecord.setNote(record.getNote());
            firebaseRecord.setUserId(record.getUserId());
            firebaseRecord.setActivitiesId(record.getActivitiesId());
            firebaseRecord.setCreatedBy(currentUser.getUid());
            firebaseRecord.setRecordCreatedDatetime(record.getRecordCreatedDatetime());


            recordRef.child("Record").child(firebaseRecord.getRecordId()).setValue(firebaseRecord).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    db.updateRecordSyn(record, true, currentUser.getUid());
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


    public void deleteRecordFirebase(Record record) {
        initRecordFirebaseValue(record);
        recordRef.child("Record").child(record.getRecordId()).removeValue();

    }


    public void addRecordToSQLite(Record record) {

        if (!db.checkIsRecordIsExist(record.getRecordId())) {

            db.addRecord(record);

        } else {

            if (db.getRecord(record.getRecordId()).getSyn()) {
                db.updateRecord(record);
            }
            //userList.add(updateUser);

        }

    }


    public void pullRecordByUserToFirebase(User user) {

        final long[] recordDowloadedCounter = {0};

        DatabaseReference recordRef;
        DateFormatUtility dateFormatUtility = new DateFormatUtility();

        if (currentUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            // DatabaseReference growthRef;

            if (user.getRequestStatus().equals("Accept")) {
                DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");
                recordRef = userRef.child(user.getUserId() + "/Record");
            } else {

                DatabaseReference userRef = database.getReference(user.getCreatedBy() + "/User");
                recordRef = userRef.child(user.getUserId() + "/Record");

            }

            //Pull all growth first and add it in SQLite. When it finish will push the remain havent syn growth. When finish push will startSynchronizedGrowthPhoto

            recordRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshots) {

                    long maxID = snapshots.getChildrenCount();
                    if (maxID == 0) {
                        // pushGrowthByUserToFirebase(user);
                        db.deleteAllRecordByUserAlreadySyned(user);
                        pushRecordByUserToFirebase(user);
                    } else {

                        db.deleteAllRecordByUserAlreadySyned(user);

                        for (DataSnapshot snapshot : snapshots.getChildren()) {


                            RecordFirebaseObject recordFirebaseObject = snapshot.getValue(RecordFirebaseObject.class);
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

                            recordDowloadedCounter[0]++;
                            addRecordToSQLite(updateRecord);

                            Log.i("CARE", "Pulling Record data: " + updateRecord.getRecordId());

                            if (recordDowloadedCounter[0] == maxID) {
                                Log.i("CARE", "Pulling Record data completed ");
                                // pushUserToFirebase(user);
                                pushRecordByUserToFirebase(user);

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


    public void pushRecordByUserToFirebase(User user) {

        Log.d("CARE", "Start push Record Of " + user.getUserName());
        List<Record> recordsList = db.getAllRecordsNotSyn(user.getUserId());
        //  if (uploadGrowthDataByList(growthList)) {
        uploadRecordDataByList(recordsList);


    }


    public Boolean uploadRecordDataByList(List<Record> recordsList) {

        int recordCounter = 0;
        Boolean isComplete = false;

        for (Record record : recordsList) {
            //addGrowthFirebase(growth);

            if (pushSingleRecordToFirebase(record)) {
                recordCounter++;
                if (recordCounter == recordsList.size()) {
                    Log.d("CARE", "COMPLETE PUSH ALL RECORD FOR USER ");
                }
            }


        }
        return isComplete;


    }


    public Boolean startSynchronizedRecordByUser() {
        //int userCounter = 0;
        Boolean isComplete = false;
        List<User> userList = new ArrayList<>();
        userList.addAll(db.getAllUsers());

        for (User user : userList) {

            pullRecordByUserToFirebase(user);

        }

        return isComplete;

    }


}
