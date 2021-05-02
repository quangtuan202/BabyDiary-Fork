package com.riagon.babydiary.Firebase;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Care;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.CareAdapter;

import java.util.ArrayList;
import java.util.List;

public class UserFirebase {

    private Context context;
    private FirebaseUser currentUser;
    private DatabaseHelper db;
    private DatabaseReference userRef;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private List<User> notSynUserList;
    private List<User> userAddFirebaseList;
    private GrowthFirebase growthFirebase;
    private RecordFirebase recordFirebase;
    private PhotoFirebase photoFirebase;

    public UserFirebase(Context context, FirebaseUser currentUser) {
        this.currentUser = currentUser;
        this.context = context;
        db = new DatabaseHelper(context);
        storageRef = storage.getReference();
        notSynUserList = new ArrayList<>();
        userAddFirebaseList = new ArrayList<>();
        growthFirebase = new GrowthFirebase(context, currentUser);
        recordFirebase = new RecordFirebase(context, currentUser);
        photoFirebase = new PhotoFirebase(context, currentUser);
    }


    public void pushSingleUserToFirebase(User user, Boolean isUpdate) {

        //Only the owner of baby can Update the baby

        DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");
        StorageReference photoRef = storageRef.child(currentUser.getUid()).child(user.getUserId());

// Create a reference to "mountains.jpg" check if have image then upload it
        byte[] data = user.getUserImage();

        if (data != null) {

            UploadTask uploadTask = photoRef.child(String.valueOf(user.getUserId())).putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                    photoRef.child(String.valueOf(user.getUserId())).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            userRef.child(String.valueOf(user.getUserId())).child("userImageUrl").setValue(url);
                            Log.i("FIREBASE", url + "/n");

                            //Do what you need to do with url
                        }
                    });


                }
            });


        } else {

        }

        userRef.child(String.valueOf(user.getUserId())).child("userId").setValue(user.getUserId());
        userRef.child(String.valueOf(user.getUserId())).child("userName").setValue(user.getUserName());
        userRef.child(String.valueOf(user.getUserId())).child("userSex").setValue(user.getUserSex());
        userRef.child(String.valueOf(user.getUserId())).child("userTheme").setValue(user.getUserTheme());
        userRef.child(String.valueOf(user.getUserId())).child("userBirthday").setValue(user.getUserBirthday());
        userRef.child(String.valueOf(user.getUserId())).child("userDuedate").setValue(user.getUserDuedate());
        userRef.child(String.valueOf(user.getUserId())).child("userCreatedDatetime").setValue(user.getUserCreatedDatetime());
        userRef.child(String.valueOf(user.getUserId())).child("createdBy").setValue(currentUser.getUid());
        userRef.child(String.valueOf(user.getUserId())).child("userCreatedDatetime").setValue(user.getUserCreatedDatetime());


        if (!isUpdate) {
            Care care = new Care();
            care.setCareEmail(currentUser.getEmail());
            care.setCareId(currentUser.getUid());
            care.setCareName(currentUser.getDisplayName());
            care.setCareURLPhoto(currentUser.getPhotoUrl().toString());
            care.setOwner(true);

            userRef.child(user.getUserId() + "/caregiver").push().setValue(care);
        }


    }


    public void deleteSingleUserFirebase(User activeUser) {

        if (currentUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            if (!activeUser.getRequestStatus().equals("Accept")) {
                DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");
                userRef.child(activeUser.getUserId()).removeValue();
            }

        }

    }


    public void deleteOthersUserFirebase(Care care, User activeUser) {

        if (currentUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference userRef = database.getReference(care.getCareId() + "/User");
            userRef.child(activeUser.getUserId()).removeValue();

        }

    }



    public void deleteUserInCaregiverRoot(User activedUser) {


       // List<Care> careList = new ArrayList<>();

        if (currentUser != null) {

                Log.i("CARE", "Start to take care infor");

                DatabaseReference userRequestRef = database.getReference(activedUser.getCreatedBy() + "/User");

                // Attach a listener to read the data at our posts reference
                userRequestRef.child(activedUser.getUserId() + "/caregiver").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                      //  careList.clear();
                       for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            // String careID = ds.getKey();

                            Care care = ds.getValue(Care.class);

                            if (!care.getOwner())
                            {
                                deleteOthersUserFirebase(care,activedUser);

                            }
                           // careList.add(care);

                      }

                       deleteSingleUserFirebase(activedUser);


                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //   System.out.println("The read failed: " + databaseError.getCode());
                    }
                });





        }


    }




    public void pushAllUserToFirebase() {
        //add data
//        // Write a message to the database
//Syn the user havent syn

        // final Boolean[] isCompleted = {false};
        DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");
        StorageReference photoRef = storageRef.child(currentUser.getUid());

        final int[] addUserToListCounter = {0};
        notSynUserList.clear();
        notSynUserList.addAll(db.getAllUsersNotSyn());
        int i = 0;

        if (notSynUserList.size() == 0) {
            growthFirebase.startSynchronizedGrowthByUser();
            recordFirebase.startSynchronizedRecordByUser();
            photoFirebase.startSynchronizedPhotoByUser();
            //growthFirebase.start();
        } else {


            for (User u : notSynUserList) {
                i++;
                Log.i("CARE", "User Havent push " + u.getUserName() + " i " + i);

// Create a reference to "mountains.jpg" check if have image then upload it
                byte[] data = u.getUserImage();

                if (data != null) {

                    UploadTask uploadTask = photoRef.child(u.getUserId()).putBytes(data);

                    //int finalI = i;
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.

                            photoRef.child(String.valueOf(u.getUserId())).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String url = uri.toString();
                                    // userRef.child(String.valueOf(u.getUserId())).child("userImageUrl").setValue(url);
                                    User userFirebase = new User();
                                    userFirebase.setUserImageUrl(url);
                                    //userFirebase.setActive(u.getActive());
                                    userFirebase.setUserBirthday(u.getUserBirthday());
                                    userFirebase.setUserDuedate(u.getUserDuedate());
                                    userFirebase.setUserCreatedDatetime(u.getUserCreatedDatetime());
                                    userFirebase.setUserId(u.getUserId());
                                    userFirebase.setUserName(u.getUserName());
                                    userFirebase.setUserSex(u.getUserSex());
                                    userFirebase.setUserTheme(u.getUserTheme());
                                    userFirebase.setCreatedBy(currentUser.getUid());
                                    //Log.i("FIREBASE", url + "/n");

                                    userAddFirebaseList.add(userFirebase);
                                    addUserToListCounter[0]++;

                                    Log.i("FIREBASE", "Got URL counter " + addUserToListCounter[0]);


                                    if (addUserToListCounter[0] == notSynUserList.size()) {

                                        Log.i("FIREBASE", "Start Upload " + addUserToListCounter[0]);

                                        pushAllUserNotSyn(userRef);


                                    }

                                    //Do what you need to do with url
                                }
                            });


                        }
                    });

                } else {

                    User userFirebase = new User();
                    //userFirebase.setActive(u.getActive());
                    userFirebase.setUserBirthday(u.getUserBirthday());
                    userFirebase.setUserDuedate(u.getUserDuedate());
                    userFirebase.setUserCreatedDatetime(u.getUserCreatedDatetime());
                    userFirebase.setUserId(u.getUserId());
                    userFirebase.setUserName(u.getUserName());
                    userFirebase.setUserSex(u.getUserSex());
                    userFirebase.setUserTheme(u.getUserTheme());
                    userFirebase.setCreatedBy(currentUser.getUid());
                    userAddFirebaseList.add(userFirebase);
                    addUserToListCounter[0]++;
                    Log.i("FIREBASE", "No URL counter " + addUserToListCounter[0]);

                    if (addUserToListCounter[0] == notSynUserList.size()) {

                        Log.i("FIREBASE", "Start Upload " + addUserToListCounter[0]);
                        pushAllUserNotSyn(userRef);

                    }


                }

            }


        }


        //return isCompleted[0];


    }


    //Start only when the photo finish Upload
    public void pushAllUserNotSyn(DatabaseReference userRef) {
        final int[] counter = {0};
        //final Boolean[] isComplete = {false};
        for (User uk : userAddFirebaseList) {
            userRef.child(String.valueOf(uk.getUserId())).setValue(uk).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Log.i("FIREBASE", "Pushing user:  " + uk.getUserName());

                    counter[0]++;
                    db.updateUserSyn(uk, true, uk.getCreatedBy());
                    if (counter[0] == userAddFirebaseList.size()) {
                        //  db.deleteAllUser();
                        ///   downloadData(user);
                        //   adapterAccount.notifyDataSetChanged();
                        Log.i("CARE", "ALL USER  COMPLETE PUSHED TO FIREBASE");
                        //isComplete[0] =true;

                        growthFirebase.startSynchronizedGrowthByUser();
                        recordFirebase.startSynchronizedRecordByUser();
                        photoFirebase.startSynchronizedPhotoByUser();
                    }

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

            Care care1 = new Care(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail(), true, currentUser.getPhotoUrl().toString());
            userRef.child(uk.getUserId() + "/caregiver").push().setValue(care1);

        }


        //  return isComplete[0];
    }


}
