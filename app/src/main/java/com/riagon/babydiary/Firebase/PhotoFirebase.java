package com.riagon.babydiary.Firebase;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
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
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Growth;
import com.riagon.babydiary.Model.Photo;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.RecordFirebaseObject;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.DbBitmapUtility;
import com.riagon.babydiary.Utility.LocalDataHelper;

import java.util.ArrayList;
import java.util.List;

public class PhotoFirebase {
    private Context context;
    private FirebaseUser currentUser;
    private DatabaseHelper db;
    private DatabaseReference growthRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private StorageReference photoStorageRef;
    private DatabaseReference photoRef;
    private LocalDataHelper localDataHelper;
    private List<Photo> photoListFirebase;
    //private User activeUser;

    public PhotoFirebase(Context context, FirebaseUser currentUser) {
        this.currentUser = currentUser;
        this.context = context;
        db = new DatabaseHelper(context);
        storageRef = storage.getReference();
        localDataHelper = new LocalDataHelper(context);
        photoListFirebase = new ArrayList<>();

    }

    public PhotoFirebase() {
    }


    public void initPhotoFirebaseValue() {
        User activeUser = db.getUser(localDataHelper.getActiveUserId());

        if (currentUser != null) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            if (!activeUser.getRequestStatus().equals("Accept")) {
                photoStorageRef = storageRef.child(currentUser.getUid()).child(activeUser.getUserId());

                DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");
                //photoRef = userRef.child(currentUser.getUserId()).child("Growth").child(photo.getGrowthId());
                photoRef = userRef.child(activeUser.getUserId());
            } else {
                photoStorageRef = storageRef.child(activeUser.getCreatedBy()).child(activeUser.getUserId());

                DatabaseReference userRef = database.getReference(activeUser.getCreatedBy() + "/User");
                // photoRef = userRef.child(currentUser.getUserIDByOwner()).child("Growth").child(photo.getGrowthId());
                photoRef = userRef.child(activeUser.getUserId());
            }

        }


    }

    public void initPhotoFirebaseValueByPhoto(Photo photo) {
        if (currentUser != null) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            if (!db.getUser(photo.getUserId()).getRequestStatus().equals("Accept")) {
                photoStorageRef = storageRef.child(currentUser.getUid()).child(photo.getUserId());

                DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");
                //photoRef = userRef.child(currentUser.getUserId()).child("Growth").child(photo.getGrowthId());
                photoRef = userRef.child(photo.getUserId());
            } else {
                photoStorageRef = storageRef.child(photo.getCreatedBy()).child(photo.getUserId());

                DatabaseReference userRef = database.getReference(photo.getCreatedBy() + "/User");
                // photoRef = userRef.child(currentUser.getUserIDByOwner()).child("Growth").child(photo.getGrowthId());
                photoRef = userRef.child(photo.getUserId());
            }


        }

    }


    public void pushPhotoToFirebase(Photo photo) {

        if (currentUser != null) {

            initPhotoFirebaseValueByPhoto(photo);
// Create a reference to "mountains.jpg" check if have image then upload it
            byte[] data = photo.getPhotoImage();

            if (data != null) {

                UploadTask uploadTask = photoStorageRef.child(String.valueOf(photo.getPhotoId())).putBytes(data);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                    }
                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                        photoStorageRef.child(String.valueOf(photo.getPhotoId())).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String url = uri.toString();

                                Photo firebasePhoto = new Photo();
                                firebasePhoto.setPhotoId(photo.getPhotoId());
                                firebasePhoto.setGrowthId(photo.getGrowthId());
                                firebasePhoto.setPhotoImageUrl(url);
                                firebasePhoto.setCreatedBy(photo.getCreatedBy());
                                firebasePhoto.setCreatedDatetime(photo.getCreatedDatetime());
                                photoRef.child("Photo").child(firebasePhoto.getPhotoId()).setValue(firebasePhoto);

                                db.updatePhotoSyn(photo, true, photo.getCreatedBy());

                                //  Log.i("FIREBASE", url + "/n");

                                //Do what you need to do with url
                            }
                        });


                    }
                });


            }


        }


    }


    public void deletePhotoFirebase(List<Photo> photoListFirebase) {
        initPhotoFirebaseValue();

        if (photoListFirebase.size() != 0) {

            for (Photo photo : photoListFirebase) {
                //StorageReference photoStorageRef = storage.getReferenceFromUrl(photo.getPhotoImageUrl());

                Log.i("DeletePhoto", photo.getPhotoId());
                photoRef.child("Photo").child(photo.getPhotoId()).removeValue();


            }

        }


    }


    public void getCurrentGrowthFirebasePhotoList(String growthId) {
        User activeUser = db.getUser(localDataHelper.getActiveUserId());

        if (currentUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference photoRef;

            if (!activeUser.getRequestStatus().equals("Accept")) {
                // photoStorageRef = storageRef.child(currentLoginUser.getUid() + "/GrowthPhoto");

                DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");
                //  photoRef = userRef.child(currentUser.getUserId()).child("Growth").child(growthId);
                photoRef = userRef.child(activeUser.getUserId());

            } else {
                //    photoStorageRef = storageRef.child(activeUser.getCreatedBy() + "/GrowthPhoto");

                DatabaseReference userRef = database.getReference(activeUser.getCreatedBy() + "/User");
                //  photoRef = userRef.child(currentUser.getUserIDByOwner()).child("Growth").child(growthId);
                photoRef = userRef.child(activeUser.getUserId());

            }


            photoRef.child("Photo").orderByChild("growthId").equalTo(growthId).addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    Photo photoFirebase = snapshot.getValue(Photo.class);
                    Log.i("PHOTOLIST", "PhotoID " + photoFirebase.getPhotoId() + " URL " + photoFirebase.getPhotoImageUrl());
                    photoListFirebase.add(photoFirebase);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

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


    public void deleteMultiPhotoStorage(List<Photo> photoListFirebase) {
        if (photoListFirebase.size() != 0) {

            for (Photo photo : photoListFirebase) {
                StorageReference photoStorageRef = storage.getReferenceFromUrl(photo.getPhotoImageUrl());
                photoStorageRef.delete();

            }

        }

    }

    public void deletePhotoFirebaseAfterSecond() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                deleteMultiPhotoStorage(photoListFirebase);
                deletePhotoFirebase(photoListFirebase);

                // Actions to do after 5 seconds
            }
        }, 5000);
    }


    public void deleteAllPhotoBySingleUserFirebase(User activeUser) {
        Log.i("LISTALL", "START LIST ALL");
        if (currentUser != null) {
            //FirebaseDatabase database = FirebaseDatabase.getInstance();

            if (!activeUser.getRequestStatus().equals("Accept")) {

                StorageReference photoStorageRef = storageRef.child(currentUser.getUid()).child(activeUser.getUserId());
                photoStorageRef.listAll()
                        .addOnSuccessListener(new OnSuccessListener<ListResult>() {
                            @Override
                            public void onSuccess(ListResult listResult) {
                                for (StorageReference prefix : listResult.getPrefixes()) {

                                    //
                                    // All the prefixes under listRef.
                                    // You may call listAll() recursively on them.
                                    Log.i("LISTALL", prefix.getDownloadUrl().toString());
                                }

                                for (StorageReference item : listResult.getItems()) {

                                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri downloadUrl) {

                                            Log.i("LISTALL", downloadUrl.toString());
                                            StorageReference photoStorageRef = storage.getReferenceFromUrl(downloadUrl.toString());
                                            photoStorageRef.delete();
                                            //do something with downloadurl
                                        }
                                    });

                                    //Log.i("LISTALL",item.getActiveDownloadTasks().toString());
                                    // All the items under listRef.
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Uh-oh, an error occurred!
                                Log.i("LISTALL", "FALSE");
                            }
                        });


                // DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");

                // userRef.child(activeUser.getUserId()).removeValue();
            }

        } else {
            Log.i("LISTALL", "NO login");
        }

    }


    /// Start syn fuction

    public void addPhoroToSQLite(Photo photo) {

        if (!db.checkIsPhotoIsExist(photo.getPhotoId())) {

            db.addPhoto(photo);

        } else {

            if (db.getPhoto(photo.getPhotoId()).getSyn()) {
                db.updatePhoto(photo);

            }
            //userList.add(updateUser);

        }

    }


    public void pullPhotosByUserToFirebase(User user) {

        final long[] photoDowloadedCounter = {0};

        DatabaseReference photoRef;
        DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();

        if (currentUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            // DatabaseReference growthRef;
            DatabaseReference userRef;
            if (user.getRequestStatus().equals("Accept")) {
                userRef = database.getReference(user.getCreatedBy() + "/User");
            } else {
                userRef = database.getReference(currentUser.getUid() + "/User");
            }
            photoRef = userRef.child(user.getUserId() + "/Photo");


            //Pull all growth first and add it in SQLite. When it finish will push the remain havent syn growth. When finish push will startSynchronizedGrowthPhoto

            photoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshots) {

                    long maxID = snapshots.getChildrenCount();
                    if (maxID == 0) {
                        // pushGrowthByUserToFirebase(user);
                        pushRecordByUserToFirebase(user);
                    } else {

                        for (DataSnapshot snapshot : snapshots.getChildren()) {


                            Photo firebasePhoto = snapshot.getValue(Photo.class);
                            Photo updatePhoto = new Photo();

                            String url = firebasePhoto.getPhotoImageUrl();

                            if (url != null) {
                                Glide.with(context)
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

                                                photoDowloadedCounter[0]++;
                                                addPhoroToSQLite(updatePhoto);

                                                Log.i("CARE", "Pulling Photo data: " + updatePhoto.getPhotoId());

                                                if (photoDowloadedCounter[0] == maxID) {
                                                    Log.i("CARE", "Pulling Photo data completed ");
                                                    // pushUserToFirebase(user);
                                                    pushRecordByUserToFirebase(user);

                                                }


                                                //    adapterAccount.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }
                                        });
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

        Log.d("CARE", "Start push Photo Of " + user.getUserName());
        List<Photo> photosList = db.getAllPhotoByUserNotSyn(user.getUserId());
        //  if (uploadGrowthDataByList(growthList)) {
        uploadRecordDataByList(photosList);


    }


    public Boolean uploadRecordDataByList(List<Photo> photosList) {

        int recordCounter = 0;
        Boolean isComplete = false;

        for (Photo photo : photosList) {
            //addGrowthFirebase(growth);

            pushPhotoToFirebase(photo);
            recordCounter++;
            if (recordCounter == photosList.size()) {
                Log.d("CARE", "COMPLETE PUSH ALL PHOTO FOR USER ");
            }


        }
        return isComplete;


    }


    public Boolean startSynchronizedPhotoByUser() {
        //int userCounter = 0;
        Boolean isComplete = false;
        List<User> userList = new ArrayList<>();
        userList.addAll(db.getAllUsers());

        for (User user : userList) {

            pullPhotosByUserToFirebase(user);

        }

        return isComplete;

    }


}
