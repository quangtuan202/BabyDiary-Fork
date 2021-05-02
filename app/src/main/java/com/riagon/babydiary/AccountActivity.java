package com.riagon.babydiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Firebase.GrowthFirebase;
import com.riagon.babydiary.Firebase.PhotoFirebase;
import com.riagon.babydiary.Firebase.RecordFirebase;
import com.riagon.babydiary.Firebase.UserFirebase;
import com.riagon.babydiary.Model.Care;
import com.riagon.babydiary.Model.CareRequest;
import com.riagon.babydiary.Model.Growth;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Notification.AlarmHelper;
import com.riagon.babydiary.Utility.AccountAdapter;
import com.riagon.babydiary.Utility.CareAdapter;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.DbBitmapUtility;
import com.riagon.babydiary.Utility.FirebaseHelper;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class AccountActivity extends AppCompatActivity {
    private RecyclerView recyclerView, careRecyclerView;
    private AccountAdapter adapterAccount;
    private CareAdapter adapterCare;
    private List<User> userList = new ArrayList<>();
    private List<Care> careList = new ArrayList<>();
    private User activedUser;
    private DatabaseHelper db;
    private FormHelper formHelper;
    private LocalDataHelper localDataHelper;
    private DateFormatUtility dateFormatUtility;
    private DbBitmapUtility dbBitmapUtility;
    private Button addBabyLayout;
    private CircularImageView care_profile_image;
    private SettingHelper settingHelper;
    private ActionBar toolbar;
    private Button googleSignin, googleLogout, synRefresh;
    private LinearLayout beforeSign;
    private LinearLayout afterSign;
    private RelativeLayout caregiver;
    private TextView care_name, care_email, babyName, babySex, babyBirthday, babyAge;
    private TextView premium_plan, premium_detail;
    private Button editUser, addCareButton, buttonSend, deleteUser;
    private LinearLayout babyDetailLayout;
    public Animation animation;
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    //   private StorageReference storageRef;
    private static long maxID = 0;
    private FirebaseUser currentUser;
    //    private int addUserToListCounter;
//    private DatabaseReference growthRef;
    private GrowthFirebase growthFirebase;
    private PhotoFirebase photoFirebase;
    private UserFirebase userFirebase;
    private RecordFirebase recordFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingHelper = new SettingHelper(this);
        db = new DatabaseHelper(this);
        formHelper = new FormHelper(this);
        localDataHelper = new LocalDataHelper(this);
        dateFormatUtility = new DateFormatUtility(this);
        dbBitmapUtility = new DbBitmapUtility();


        if (!localDataHelper.getActiveUserId().equals("")) {
            activedUser = db.getUser(localDataHelper.getActiveUserId());
            settingHelper.setThemes(activedUser.getUserTheme());
        }


        // userList.clear();
        userList.addAll(db.getAllUsers());
        //storageRef = storage.getReference();

        setContentView(R.layout.activity_account);
        initView();

        //if  there are a purchase

        setPurchaseView();


        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        currentUser = mAuth.getCurrentUser();


        getRealtimeUser(currentUser);
        growthFirebase = new GrowthFirebase(this, currentUser);
        recordFirebase = new RecordFirebase(this, currentUser);
        photoFirebase = new PhotoFirebase(this, currentUser);
        userFirebase = new UserFirebase(this, currentUser);
        //this is a test 2

        setTitle(getResources().getString(R.string.title_account));

        toolbar = getSupportActionBar();
        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeButtonEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_account);
        //userList = new ArrayList<>();
        // userList.addAll(db.getAllUsers());

        adapterAccount = new AccountAdapter(this, userList, db);
        LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, true);
        recyclerView.setLayoutManager(horizontalLayoutManagaer);
        recyclerView.setAdapter(adapterAccount);


        if (!localDataHelper.getActiveUserId().equals("")) {

            babyDetailLayout.setVisibility(View.VISIBLE);

            setBabyInfor();
            setEditButtonView();
            setAddCareButtonView();
            setDeleteUserButtonView();

        } else {
            babyDetailLayout.setVisibility(View.GONE);
        }


        googleSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });


        googleLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        addBabyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                intent.putExtra("signUpState", "Add");
//                if (currentUser != null) {
//                    intent.putExtra("currentAccountLogin", currentUser.getUid());
//                }
                startActivity(intent);
            }
        });


// animation
        animation = AnimationUtils.loadAnimation(this, R.anim.anim);

        synRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                synRefresh.startAnimation(animation);
                refreshSyn(currentUser);
            }
        });


    }


    public void initView() {
        addBabyLayout = findViewById(R.id.add_baby_button);
        care_profile_image = findViewById(R.id.care_profile_image);
        googleSignin = findViewById(R.id.google_signin);
        googleLogout = findViewById(R.id.logout);
        synRefresh = findViewById(R.id.refresh_syn);
        beforeSign = findViewById(R.id.before_sign);
        caregiver = findViewById(R.id.caregiver_layout);
        afterSign = findViewById(R.id.after_sign);
        premium_plan = findViewById(R.id.premium_plan);
        premium_detail = findViewById(R.id.premium_detail);
        care_email = findViewById(R.id.care_email);
        care_name = findViewById(R.id.care_name);
        //  babyName = findViewById(R.id.baby_name);
        editUser = findViewById(R.id.editIcon);
        babySex = findViewById(R.id.baby_sex);
        babyBirthday = findViewById(R.id.baby_birthday);
        babyAge = findViewById(R.id.baby_age);
        addCareButton = findViewById(R.id.add_care_button);
        deleteUser = findViewById(R.id.deleteIcon);
        babyDetailLayout = findViewById(R.id.baby_detail_layout);
    }


    private void setPurchaseView() {
        if (!localDataHelper.getPurchaseState().equals("")) {

            String planStatus = "";

            Date purchaseDate = new Date(localDataHelper.getPurchaseTime());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(purchaseDate);


            switch (localDataHelper.getPurchaseState()) {

                case "life_time":
                    planStatus = "Lifetime";
                    premium_detail.setText("");
                    break;

                case "monthly":
                    planStatus = "Monthly";
                    calendar.add(Calendar.MONTH, 1);
                    premium_detail.setText("Value until: " + calendar.getTime());
                    break;
                case "yearly":
                    planStatus = "Yearly";
                    calendar.add(Calendar.YEAR, 1);
                    premium_detail.setText("Value until: " + calendar.getTime());
                    break;

            }

            premium_plan.setText("Pro plan: " + planStatus);

        }


    }


    private void setDeleteUserButtonView() {
        if (activedUser.getRequestStatus().equals("No")) {
            deleteUser.setVisibility(View.VISIBLE);
        } else {
            if (currentUser != null) {
                deleteUser.setVisibility(View.GONE);
            } else {
                deleteUser.setVisibility(View.VISIBLE);
            }

        }

        deleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteAllUserData(activedUser);

                Toast.makeText(getApplicationContext(), "Delete User", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void setAddCareButtonView() {


        if (currentUser != null) {

            if (activedUser.getCreatedBy().equals(currentUser.getUid())) {
                caregiver.setVisibility(View.VISIBLE);
                // addCareButton.setVisibility(View.VISIBLE);
                addCareButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        caregiveEmailDialog();

                    }
                });

            } else {
                caregiver.setVisibility(View.GONE);
                // addCareButton.setVisibility(View.GONE);
            }
        } else {
            caregiver.setVisibility(View.GONE);
            // addCareButton.setVisibility(View.GONE);
        }


    }

    private void setEditButtonView() {

        if (activedUser.getRequestStatus().equals("No")) {
            editUser.setVisibility(View.VISIBLE);
            editUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                    intent.putExtra("signUpState", "Update");
                    intent.putExtra("userId", activedUser.getUserId());
                    startActivity(intent);
                }
            });

        } else {
            editUser.setVisibility(View.GONE);
        }


    }


    public void deleteAllUserData(User user) {

        setActivedUserAfterDeleteUser();

        userFirebase.deleteUserInCaregiverRoot(activedUser);
        photoFirebase.deleteAllPhotoBySingleUserFirebase(activedUser);
        //  userFirebase.deleteSingleUserFirebase(activedUser);

        db.deleteUser(user);
        db.deleteAllActivitiesByUser(user);
        db.deleteAllGrowthByUser(user);
        db.deleteAllRecordByUser(user);
        db.deleteAllPhotoByUser(user);

//        deleteAllPhotoBySingleUserFirebase(activedUser);
//        deleteSingleUserFirebase(user);


        //  growthFirebase.deleteSingleUserFirebase(user);

        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);

        finish();

        //setActivedUserAfterDeleteUser();

    }


    public void setActivedUserAfterDeleteUser() {

        List<User> notActiveUserList = new ArrayList<>();

        if (userList.size() > 1) {
            for (User user1 : userList) {
                if (!user1.getUserId().equals(activedUser.getUserId())) {
                    notActiveUserList.add(user1);
                }
            }
            //db.updateUserActive(notActiveUserList.get(notActiveUserList.size() - 1), true);
            localDataHelper.setActiveUserId(notActiveUserList.get(notActiveUserList.size() - 1).getUserId());
            AlarmHelper alarmHelper= new AlarmHelper(this,db);
            alarmHelper.setNotification(notActiveUserList.get(notActiveUserList.size() - 1));

        } else {
            localDataHelper.setActiveUserId("");
        }


    }


    //Dialog caregive'email
    private void caregiveEmailDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View alertLayout = inflater.inflate(R.layout.email_caregiver, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        buttonSend = alertLayout.findViewById(R.id.button_send);

        alert.setView(alertLayout);
        alert.setCancelable(false);

        AlertDialog dialog = alert.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(true);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edt_email_caregiver = alertLayout.findViewById(R.id.edt_email_caregive);

                String emailAddress = edt_email_caregiver.getText().toString().trim();

                if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {

                    sendCareInvitation();
                    dialog.dismiss();

                } else {

                }


            }

            private void sendCareInvitation() {
                EditText edt_email_caregive = alertLayout.findViewById(R.id.edt_email_caregive);

                FirebaseDatabase database = FirebaseDatabase.getInstance();

                //Get current user count
                DatabaseReference userRef = database.getReference(currentUser.getUid() + "/User");

                String careRequestId = userRef.child(activedUser.getUserId() + "/caregiver").push().getKey();

                Care care = new Care();
                care.setCareEmail(edt_email_caregive.getText().toString());
                care.setCareId(careRequestId);
                care.setCareName(null);
                care.setCareURLPhoto(null);
                care.setOwner(false);
                care.setAccept(false);
                careList.clear();
                careList.add(care);
                adapterCare.notifyDataSetChanged();

                //String careRequestId = "BXbKOfJ8HiejWPVzrzZLSR9DUqC2";
                // HashMap<String, Boolean> babyId = new HashMap();
                // babyId.put(currentUser.getUid(), false);
                //    userRef.child(activedUser.getUserId() + "/caregiver").setValue(babyId);
                //userRef.child(activedUser.getUserId() + "/caregiver").child(careRequestId).setValue(care);
                userRef.child(activedUser.getUserId() + "/caregiver").child(careRequestId).setValue(care);

                //Send request to others
                //Get current user count

                //DatabaseReference careRequestRef = database.getReference(careRequestId + "/CareRequest");
                DatabaseReference careRequestRef = database.getReference("/CareRequest");
                CareRequest careRequest = new CareRequest(careRequestId, edt_email_caregive.getText().toString(), currentUser.getUid(), activedUser.getUserId(), false);
                careRequestRef.child(careRequestId).setValue(careRequest);


            }
        });


    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //  FirebaseUser currentUser = mAuth.getCurrentUser();
        currentUser = mAuth.getCurrentUser();
        growthFirebase = new GrowthFirebase(this, currentUser);
        recordFirebase = new RecordFirebase(this, currentUser);
        photoFirebase = new PhotoFirebase(this, currentUser);
        userFirebase = new UserFirebase(this, currentUser);
        updateUI(currentUser);
        //  adapterAccount.notifyDataSetChanged();

    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(String idToken) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //FirebaseUser user = mAuth.getCurrentUser();
                            currentUser = mAuth.getCurrentUser();
                            growthFirebase = new GrowthFirebase(getApplicationContext(), currentUser);
                            recordFirebase = new RecordFirebase(getApplicationContext(), currentUser);
                            photoFirebase = new PhotoFirebase(getApplicationContext(), currentUser);
                            userFirebase = new UserFirebase(getApplicationContext(), currentUser);
                            refreshSyn(currentUser);
                            //updateUI(currentUser);
                            //  adapterAccount.notifyDataSetChanged();
                            //  checkCareRequest(currentUser);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            updateUI(null);
                        }

                    }
                });
    }
    // [END auth_with_google]


    public void setBabyInfor() {
        // babyName.setText(activedUser.getUserName());
        babyBirthday.setText(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(activedUser.getUserBirthday())));
        // babySex.setText(activedUser.getUserSex());

        if (activedUser.getUserSex().equals("male")) {
            babySex.setText(getResources().getString(R.string.sex_boy));
        }
        if (activedUser.getUserSex().equals("female")) {
            babySex.setText(getResources().getString(R.string.sex_girl));
        }

        babyAge.setText(formHelper.getAgeByMonths(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(activedUser.getUserBirthday()))));

    }


    public void startSynchronizedData(FirebaseUser user) {

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Get current user count
        DatabaseReference userRef = database.getReference(user.getUid() + "/User");

        synRefresh.startAnimation(animation);


        final long[] userDowloadedCounter = {0};
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshots) {
                maxID = snapshots.getChildrenCount();

                if (maxID == 0) {
                    userFirebase.pushAllUserToFirebase();
                } else {
                    for (DataSnapshot snapshot : snapshots.getChildren()) {
                        User user1 = snapshot.getValue(User.class);

                        User updateUser = new User();

                        //Check if this is this user is from other owner
                        if (user1.getRequestStatus() != null) {
                            //Get current user count
                            DatabaseReference userRequestRef = database.getReference(user1.getCreatedBy() + "/User/" + user1.getUserId());
                            userRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {

                                    User userRequest = snapshot.getValue(User.class);

                                    String url = userRequest.getUserImageUrl();

                                    if (url != null) {
                                        Glide.with(getApplicationContext())
                                                .asBitmap()
                                                .load(url)
                                                .into(new CustomTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                        // count=1;
                                                        // Log.i("CARE", "Convert Photo URL to bitmap " + url);

                                                        updateUser.setUserImage(dbBitmapUtility.getBytes(resource));
                                                        updateUser.setUserBirthday(userRequest.getUserBirthday());
                                                        updateUser.setUserDuedate(userRequest.getUserDuedate());
                                                        updateUser.setSyn(true);
                                                        updateUser.setRequestStatus("Accept");
                                                        updateUser.setUserCreatedDatetime(userRequest.getUserCreatedDatetime());
                                                        updateUser.setUserId(user1.getUserId());
                                                        updateUser.setUserName(userRequest.getUserName());
                                                        updateUser.setUserSex(userRequest.getUserSex());
                                                        updateUser.setUserTheme(userRequest.getUserTheme());
                                                        updateUser.setCreatedBy(userRequest.getCreatedBy());

                                                        addUserToSQLite(updateUser);
                                                        if (localDataHelper.getActiveUserId().equals(""))
                                                        {
                                                            localDataHelper.setActiveUserId(updateUser.getUserId());
                                                        }


                                                        Log.i("CARE", "Pulling user data: " + userRequest.getUserName());
                                                        Log.i("CARE", "User pull counter: " + userDowloadedCounter[0]);
                                                        userDowloadedCounter[0]++;

                                                        if (userDowloadedCounter[0] == maxID) {
                                                            Log.i("CARE", "Pulling user data completed ");

                                                            // Refresh main activity upon close of dialog box
                                                            Intent refresh = new Intent(getApplicationContext(), AccountActivity.class);
                                                            startActivity(refresh);
                                                            finish(); //
                                                            //  adapterAccount.notifyDataSetChanged();

                                                            // pushUserToFirebase(user);
//                                                        if (userFirebase.pushAllUserToFirebase(user)) {
//                                                            growthFirebase.startSynchronizedGrowth();
//                                                        }
                                                            userFirebase.pushAllUserToFirebase();
                                                            synRefresh.clearAnimation();
                                                            // growthFirebase.startSynchronizedGrowthByUser();
                                                        }

                                                        //    adapterAccount.notifyDataSetChanged();
                                                    }

                                                    @Override
                                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                                    }
                                                });
                                    } else {

                                        updateUser.setUserBirthday(userRequest.getUserBirthday());
                                        updateUser.setUserDuedate(userRequest.getUserDuedate());
                                        updateUser.setSyn(true);
                                        updateUser.setRequestStatus("Accept");
                                        updateUser.setUserCreatedDatetime(userRequest.getUserCreatedDatetime());
                                        updateUser.setUserId(user1.getUserId());
                                        updateUser.setUserName(userRequest.getUserName());
                                        updateUser.setUserSex(userRequest.getUserSex());
                                        updateUser.setUserTheme(userRequest.getUserTheme());
                                        updateUser.setCreatedBy(userRequest.getCreatedBy());

                                        addUserToSQLite(updateUser);
                                        if (localDataHelper.getActiveUserId().equals(""))
                                        {
                                            localDataHelper.setActiveUserId(updateUser.getUserId());
                                        }
//                                                //userList.add(updateUser);
//                                            }
                                        Log.i("CARE", "Pulling user data: " + userRequest.getUserName());
                                        Log.i("CARE", "User pull counter: " + userDowloadedCounter[0]);
                                        userDowloadedCounter[0]++;

                                        // userList.add(updateUser);
                                        if (userDowloadedCounter[0] == maxID) {

                                            Intent refresh = new Intent(getApplicationContext(), AccountActivity.class);
                                            startActivity(refresh);
                                            finish(); //
                                            // adapterAccount.notifyDataSetChanged();
                                            Log.i("CARE", "Pulling user data completed ");
                                            // pushUserToFirebase(user);
//                                        if (userFirebase.pushAllUserToFirebase(user)) {
//                                            growthFirebase.startSynchronizedGrowth();
//                                        }
                                            userFirebase.pushAllUserToFirebase();
                                            synRefresh.clearAnimation();

                                            //growthFirebase.startSynchronizedGrowthByUser();
                                        }

                                        //  adapterAccount.notifyDataSetChanged();
                                    }


                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                            //else this user belong to this current account
                        } else {

                            // User updateUser = new User();

                            String url = user1.getUserImageUrl();

                            if (url != null) {
                                Glide.with(getApplicationContext())
                                        .asBitmap()
                                        .load(url)
                                        .into(new CustomTarget<Bitmap>() {
                                            @Override
                                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {

                                                updateUser.setUserImage(dbBitmapUtility.getBytes(resource));
                                                updateUser.setUserId(user1.getUserId());
                                                updateUser.setUserBirthday(user1.getUserBirthday());
                                                updateUser.setUserDuedate(user1.getUserDuedate());
                                                updateUser.setSyn(true);
                                                updateUser.setRequestStatus("No");
                                                updateUser.setUserCreatedDatetime(user1.getUserCreatedDatetime());
                                                updateUser.setUserName(user1.getUserName());
                                                updateUser.setUserSex(user1.getUserSex());
                                                updateUser.setUserTheme(user1.getUserTheme());
                                                updateUser.setCreatedBy(user1.getCreatedBy());

                                                // db.addUserWithID(updateUser);
                                                userDowloadedCounter[0]++;
                                                //  userList.add(updateUser);

                                                addUserToSQLite(updateUser);
                                                if (localDataHelper.getActiveUserId().equals(""))
                                                {
                                                    localDataHelper.setActiveUserId(updateUser.getUserId());
                                                }

                                                Log.i("CARE", "Pulling user data: " + user1.getUserName());
                                                Log.i("CARE", "User pull counter: " + userDowloadedCounter[0]);
                                                if (userDowloadedCounter[0] == maxID) {


                                                    Intent refresh = new Intent(getApplicationContext(), AccountActivity.class);
                                                    startActivity(refresh);
                                                    finish(); //
                                                    //  adapterAccount.notifyDataSetChanged();
                                                    Log.i("CARE", "Pulling user data completed ");
                                                    // pushUserToFirebase(user);
//                                                if (userFirebase.pushAllUserToFirebase(user)) {
//                                                    growthFirebase.startSynchronizedGrowth();
//                                                }
                                                    userFirebase.pushAllUserToFirebase();
                                                    synRefresh.clearAnimation();
                                                    //growthFirebase.startSynchronizedGrowthByUser();
                                                }


                                                //   adapterAccount.notifyDataSetChanged();

                                            }

                                            @Override
                                            public void onLoadCleared(@Nullable Drawable placeholder) {
                                            }
                                        });
                            } else {

                                updateUser.setUserBirthday(user1.getUserBirthday());
                                updateUser.setUserDuedate(user1.getUserDuedate());
                                updateUser.setSyn(true);
                                updateUser.setRequestStatus("No");
                                updateUser.setUserCreatedDatetime(user1.getUserCreatedDatetime());
                                updateUser.setUserId(user1.getUserId());
                                updateUser.setUserName(user1.getUserName());
                                updateUser.setUserSex(user1.getUserSex());
                                updateUser.setUserTheme(user1.getUserTheme());
                                updateUser.setCreatedBy(user1.getCreatedBy());

                                //db.addUserWithID(updateUser);
                                userDowloadedCounter[0]++;

                                addUserToSQLite(updateUser);
                                if (localDataHelper.getActiveUserId().equals(""))
                                {
                                    localDataHelper.setActiveUserId(updateUser.getUserId());
                                }
                                // userList.add(updateUser);

                                Log.i("CARE", "Pulling user data: " + user1.getUserName());
                                Log.i("CARE", "User pull counter: " + userDowloadedCounter[0]);
                                if (userDowloadedCounter[0] == maxID) {


                                    Intent refresh = new Intent(getApplicationContext(), AccountActivity.class);
                                    startActivity(refresh);
                                    finish(); //
                                    // adapterAccount.notifyDataSetChanged();
                                    Log.i("CARE", "Pulling user data completed ");
                                    //  pushUserToFirebase(user);
//                                if (userFirebase.pushAllUserToFirebase(user)) {
//                                    growthFirebase.startSynchronizedGrowth();
//                                }

                                    userFirebase.pushAllUserToFirebase();
                                    synRefresh.clearAnimation();
                                    // growthFirebase.startSynchronizedGrowthByUser();

                                }

                                // adapterAccount.notifyDataSetChanged();
                            }

                        }

                    }


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    public void addUserToSQLite(User user) {

        if (!db.checkIsUserIsExist(user.getUserId())) {

            db.addUserWithID(user);
            userList.add(user);
            //setContentView(R.layout.activity_account);
            //  localDataHelper.setActiveUserId(user.getUserId());
            adapterAccount.notifyDataSetChanged();


        } else {

            if (db.getUser(user.getUserId()).getSyn()) {
                db.updateUser(user);
                userList.set(getIndexOfUser(user), user);
                // setContentView(R.layout.activity_account);
                //localDataHelper.setActiveUserId(user.getUserId());
                adapterAccount.notifyDataSetChanged();

            }
            //userList.add(updateUser);

        }


    }


    public int getIndexOfUser(User user) {

        int indexOfUser = 0;
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).getUserId().equals(user.getUserId())) {
                indexOfUser = i;

            }

        }
        return indexOfUser;


    }


    private void checkCareRequest(FirebaseUser user) {

        userList.clear();
        userList.addAll(db.getAllUsers());

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //Get current user count
        DatabaseReference careRequestRef = database.getReference("/CareRequest");
//        userList.clear();
//        userList.addAll(db.getAllUsers());
        careRequestRef.orderByChild("careEmail").equalTo(user.getEmail()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                CareRequest careRequest = snapshot.getValue(CareRequest.class);
                // String userUniqueID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));

                DatabaseReference babyRef = database.getReference(careRequest.getBabyOwnerId() + "/User");
                babyRef.child(String.valueOf(careRequest.getBabyId())).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        User user1 = snapshot.getValue(User.class);
                        String url = user1.getUserImageUrl();

                        User updateUser = new User();

                        if (url != null) {
                            Glide.with(getApplicationContext())
                                    .asBitmap()
                                    .load(url)
                                    .into(new CustomTarget<Bitmap>() {
                                        @Override
                                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                            // count=1;
                                            Log.i("CARE", "Convert Photo URL to bitmap " + url);

                                            updateUser.setUserImage(dbBitmapUtility.getBytes(resource));
                                            updateUser.setUserId(careRequest.getBabyId());
                                            updateUser.setUserName(user1.getUserName());
                                            updateUser.setUserSex(user1.getUserSex());
                                            updateUser.setUserBirthday(user1.getUserBirthday());
                                            updateUser.setUserDuedate(user1.getUserDuedate());
                                            //updateUser.setActive(false);
                                            updateUser.setSyn(true);
                                            updateUser.setRequestStatus("Yes");
                                            updateUser.setRequestorId(String.valueOf(careRequest.getCareId()));
                                            updateUser.setUserCreatedDatetime(user1.getUserCreatedDatetime());
                                            updateUser.setUserTheme(user1.getUserTheme());
                                            updateUser.setCreatedBy(user1.getCreatedBy());
                                            // db.addUserWithID(updateUser);
                                            Log.i("CARE", "A new User Request: " + user1.getUserName());
//                                            Log.i("CARE", "A new baby ID: " + careRequest.getBabyId());
//                                            Log.i("CARE", "A new care ID: " + careRequest.getCareId());

                                            if (!userList.contains(updateUser)) {
                                                userList.add(updateUser);
                                            }

                                            adapterAccount.notifyDataSetChanged();

                                        }

                                        @Override
                                        public void onLoadCleared(@Nullable Drawable placeholder) {
                                        }
                                    });
                        } else {

                            updateUser.setUserId(careRequest.getBabyId());
                            updateUser.setUserName(user1.getUserName());
                            updateUser.setUserSex(user1.getUserSex());
                            updateUser.setUserTheme(user1.getUserTheme());
                            updateUser.setUserBirthday(user1.getUserBirthday());
                            updateUser.setUserDuedate(user1.getUserDuedate());
                            updateUser.setSyn(true);
                            // updateUser.setActive(false);
                            updateUser.setRequestStatus("Yes");
                            updateUser.setRequestorId(String.valueOf(careRequest.getCareId()));
                            updateUser.setUserCreatedDatetime(user1.getUserCreatedDatetime());
                            updateUser.setCreatedBy(user1.getCreatedBy());
                            //     db.addUserWithID(updateUser);
                            Log.i("CARE", "A new User Request: " + user1.getUserName());
//                            Log.i("CARE", "A new baby ID: " + careRequest.getBabyId());
//                            Log.i("CARE", "A new care ID: " + careRequest.getCareId());
                            if (!userList.contains(updateUser)) {
                                userList.add(updateUser);
                            }
                            adapterAccount.notifyDataSetChanged();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                CareRequest careRequest = snapshot.getValue(CareRequest.class);

//                User userDelete = null;
//                for (User user : userList) {
//                    if (user.getUserId().equals(careRequest.getBabyId())) {
//                        userDelete = user;
//                    }
//                }
//
//                userList.remove(userDelete);
//                adapterAccount.notifyDataSetChanged();

//                for (int i = 0; i < userList.size(); i++) {
//                    if (userList.get(i).getUserId().equals(careRequest.getBabyId())) {
//                        userList.remove(i);
//                        // adapterLog.notifyItemRemoved(i);
//
//                        break;
//                    }
//                }
//                adapterAccount.notifyDataSetChanged();

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void refreshSyn(FirebaseUser user) {

        startSynchronizedData(user);
        updateUI(currentUser);
        // adapterAccount.notifyDataSetChanged();


    }

    // [START signin]
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    private void signOut() {
        // Firebase sign out
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        currentUser = null;
                        growthFirebase = new GrowthFirebase(getApplicationContext(), currentUser);
                        recordFirebase = new RecordFirebase(getApplicationContext(), currentUser);
                        photoFirebase = new PhotoFirebase(getApplicationContext(), currentUser);
                        userFirebase = new UserFirebase(getApplicationContext(), currentUser);
                        updateUI(null);
                    }
                });
    }

    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

        if (user != null) {

            beforeSign.setVisibility(View.GONE);
            afterSign.setVisibility(View.VISIBLE);
            // caregiver.setVisibility(View.VISIBLE);
            care_name.setText(user.getDisplayName());
            care_email.setText(user.getEmail());

            Glide.with(this).load(user.getPhotoUrl())
                    .into(care_profile_image);
            care_profile_image.setColorFilter(ContextCompat.getColor(this, android.R.color.transparent));

            //getRealtimeUser(user);

            if (!localDataHelper.getActiveUserId().equals("")) {

                careRecyclerView = (RecyclerView) findViewById(R.id.recycler_view_caregiver);

                // Get a reference to our posts
                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                //DatabaseReference ref = database.getReference("server/saving-data/fireblog/posts");

                DatabaseReference userRef = database.getReference(user.getUid() + "/User");

                //Start caregiver function

                if (activedUser.getRequestStatus().equals("Accept")) {

                    Log.i("CARE", "Start to take care infor");

                    DatabaseReference userRequestRef = database.getReference(activedUser.getCreatedBy() + "/User");

                    // Attach a listener to read the data at our posts reference
                    userRequestRef.child(activedUser.getUserId() + "/caregiver").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            careList.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                // String careID = ds.getKey();

                                Care care = ds.getValue(Care.class);

                                careList.add(care);
                                adapterCare.notifyDataSetChanged();

                            }


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //   System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });

                    adapterCare = new CareAdapter(getApplicationContext(), careList, activedUser);
                    careRecyclerView.setAdapter(adapterCare);
                    careRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                } else {
                    // Attach a listener to read the data at our posts reference
                    userRef.child(activedUser.getUserId() + "/caregiver").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            careList.clear();
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                // String careID = ds.getKey();

                                Care care = ds.getValue(Care.class);

                                careList.add(care);
                                adapterCare.notifyDataSetChanged();

                            }


                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            //   System.out.println("The read failed: " + databaseError.getCode());
                        }
                    });


                    adapterCare = new CareAdapter(this, careList, activedUser);
                    careRecyclerView.setAdapter(adapterCare);
                    careRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                }

                checkCareRequest(user);

            }


        } else {

            beforeSign.setVisibility(View.VISIBLE);
            afterSign.setVisibility(View.GONE);
            caregiver.setVisibility(View.GONE);

        }
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


        //finish();
        if (!localDataHelper.getActiveUserId().equals("")) {

            Intent i = new Intent(this, UserDetailActivity.class); // Your list's Intent
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);

            if (db.getActivitiesCount(localDataHelper.getActiveUserId()) == 0) {
                db.prepareCategoryList(localDataHelper.getActiveUserId());
            }

        }


    }

    //Start realtime account update

    private void getRealtimeUser(FirebaseUser user) {
        if (user != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //Get current user count
            DatabaseReference userRef = database.getReference(user.getUid() + "/User");

            userRef.addChildEventListener(new ChildEventListener() {

                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    User user1 = snapshot.getValue(User.class);

                    User updateUser = new User();

                    //Check if this is this user is from other owner
                    if (user1.getRequestStatus() != null) {
                        //Get current user count
                        DatabaseReference userRequestRef = database.getReference(user1.getCreatedBy() + "/User/" + user1.getUserId());
                        userRequestRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                User userRequest = snapshot.getValue(User.class);

                                String url = userRequest.getUserImageUrl();

                                if (url != null) {
                                    Glide.with(getApplicationContext())
                                            .asBitmap()
                                            .load(url)
                                            .into(new CustomTarget<Bitmap>() {
                                                @Override
                                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                                    // count=1;
                                                    // Log.i("CARE", "Convert Photo URL to bitmap " + url);

                                                    updateUser.setUserImage(dbBitmapUtility.getBytes(resource));
                                                    updateUser.setUserBirthday(userRequest.getUserBirthday());
                                                    updateUser.setUserDuedate(userRequest.getUserDuedate());
                                                    updateUser.setSyn(true);
                                                    updateUser.setRequestStatus("Accept");
                                                    updateUser.setUserCreatedDatetime(userRequest.getUserCreatedDatetime());
                                                    updateUser.setUserId(user1.getUserId());
                                                    updateUser.setUserName(userRequest.getUserName());
                                                    updateUser.setUserSex(userRequest.getUserSex());
                                                    updateUser.setUserTheme(userRequest.getUserTheme());
                                                    updateUser.setCreatedBy(userRequest.getCreatedBy());

                                                    addUserToSQLite(updateUser);


                                                    Log.i("CARE", "Pulling user data realtime: " + userRequest.getUserName());


                                                    //    adapterAccount.notifyDataSetChanged();
                                                }

                                                @Override
                                                public void onLoadCleared(@Nullable Drawable placeholder) {
                                                }
                                            });
                                } else {

                                    updateUser.setUserBirthday(userRequest.getUserBirthday());
                                    updateUser.setUserDuedate(userRequest.getUserDuedate());
                                    updateUser.setSyn(true);
                                    updateUser.setRequestStatus("Accept");
                                    updateUser.setUserCreatedDatetime(userRequest.getUserCreatedDatetime());
                                    updateUser.setUserId(user1.getUserId());
                                    updateUser.setUserName(userRequest.getUserName());
                                    updateUser.setUserSex(userRequest.getUserSex());
                                    updateUser.setUserTheme(userRequest.getUserTheme());
                                    updateUser.setCreatedBy(userRequest.getCreatedBy());

                                    addUserToSQLite(updateUser);

//                                                //userList.add(updateUser);
//                                            }
                                    Log.i("CARE", "Pulling user data realtime: " + userRequest.getUserName());


                                    //  adapterAccount.notifyDataSetChanged();
                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                    }


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


}



