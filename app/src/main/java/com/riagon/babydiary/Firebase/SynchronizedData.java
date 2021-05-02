package com.riagon.babydiary.Firebase;

import android.content.Context;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.riagon.babydiary.Data.DatabaseHelper;

public class SynchronizedData {
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private FirebaseUser currentUser;
    private Context context;
    private DatabaseHelper db;
    private FirebaseDatabase database;
    private DatabaseReference userRef;
    private StorageReference photoRef;

    public SynchronizedData(Context context,FirebaseUser currentUser) {
        this.context=context;
        this.currentUser = currentUser;
        this.storageRef = storageRef;
        storage = FirebaseStorage.getInstance();
        db= new DatabaseHelper(context);
        database = FirebaseDatabase.getInstance();
        photoRef = storageRef.child(currentUser.getUid());
        userRef = database.getReference(currentUser.getUid() + "/User");
    }








}
