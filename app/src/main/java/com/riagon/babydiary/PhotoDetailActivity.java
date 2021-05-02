package com.riagon.babydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Photo;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.DbBitmapUtility;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import static com.riagon.babydiary.AddGrowthRecordActivity.GROWTH_ID;
import static com.riagon.babydiary.Utility.PhotoAdapter.PHOTO_COUNTER;
import static com.riagon.babydiary.Utility.PhotoAdapter.PHOTO_ID;

public class PhotoDetailActivity extends AppCompatActivity {

    private PhotoView photoDetail;
    public User currentUser;
    public DatabaseHelper db;
    private LocalDataHelper localDataHelper;
    private SettingHelper settingHelper;
    private ActionBar toolbar;
    private Photo currentPhoto;
    private Photo currentFirebasePhoto;
    // private int growthID;
    private int position;
    private DbBitmapUtility bm;
    private int photoNumber;
    public static final String IS_PHOTO_DELETE = "IS_PHOTO_DELETE";
    public static final String PHOTO_POSITION = "PHOTO_POSITION";
    private StorageReference storageRef;
    private FirebaseUser currentLoginUser;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        localDataHelper= new LocalDataHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        settingHelper.setThemes(currentUser.getUserTheme());

        bm = new DbBitmapUtility();

        setContentView(R.layout.activity_photo_detail);
        photoDetail = findViewById(R.id.photo_detail);

        mAuth = FirebaseAuth.getInstance();
        currentLoginUser = mAuth.getCurrentUser();
        storageRef = storage.getReference();


        toolbar = getSupportActionBar();
        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeButtonEnabled(true);

        setInitValue();
        getCurrentFirebasePhoto(currentPhoto, currentUser);

        photoDetail.setImageBitmap(bm.getImage(currentPhoto.getPhotoImage()));

        //photoNumber=db.getPhotoByGrowthIDCount(growthID);
        setTitle((position + 1) + "/" + photoNumber);


    }

    public void setInitValue() {

        // growthID = getIntent().getIntExtra(GROWTH_ID, -1);
        position = getIntent().getIntExtra(PHOTO_POSITION, -1);
        currentPhoto = (Photo) getIntent().getSerializableExtra(PHOTO_ID);
        photoNumber = getIntent().getIntExtra(PHOTO_COUNTER, -1);

        Log.i("DETAIL", "Photo ID " + currentPhoto.getPhotoId());
        Log.i("DETAIL", "Photo Position" + position);


    }


    public void deleteSinglePhotoFirebase(Photo photo, User activeUser) {

        if (currentLoginUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //DatabaseReference growthRef;
            //StorageReference photoStorageRef;
            DatabaseReference photoRef;

            if (!activeUser.getRequestStatus().equals("Accept")) {
                // photoStorageRef = storageRef.child(currentLoginUser.getUid() + "/GrowthPhoto");

                DatabaseReference userRef = database.getReference(currentLoginUser.getUid() + "/User");
                //photoRef = userRef.child(activeUser.getUserId()).child("Growth").child(photo.getGrowthId());
                photoRef = userRef.child(activeUser.getUserId());

            } else {
                //   photoStorageRef = storageRef.child(activeUser.getCreatedBy() + "/GrowthPhoto");

                DatabaseReference userRef = database.getReference(activeUser.getCreatedBy() + "/User");
               // photoRef = userRef.child(activeUser.getUserIDByOwner()).child("Growth").child(photo.getGrowthId());
                photoRef = userRef.child(activeUser.getUserId());

            }

            photoRef.child("Photo").child(photo.getPhotoId()).removeValue();
            //  Log.i("FIREBASE", url + "/n");

        }


    }


    public void deleteSinglePhotoStorage(Photo currentFirebasePhoto) {
        if (currentFirebasePhoto != null) {

            StorageReference photoStorageRef = storage.getReferenceFromUrl(currentFirebasePhoto.getPhotoImageUrl());
            photoStorageRef.delete();

        }

    }

    public void getCurrentFirebasePhoto(Photo photo, User activeUser) {

        if (currentLoginUser != null) {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //DatabaseReference growthRef;
            //StorageReference photoStorageRef;
            DatabaseReference photoRef;

            if (!activeUser.getRequestStatus().equals("Accept")) {
                // photoStorageRef = storageRef.child(currentLoginUser.getUid() + "/GrowthPhoto");

                DatabaseReference userRef = database.getReference(currentLoginUser.getUid() + "/User");
              //  photoRef = userRef.child(activeUser.getUserId()).child("Growth").child(photo.getGrowthId());
                photoRef = userRef.child(activeUser.getUserId());

            } else {
                //    photoStorageRef = storageRef.child(activeUser.getCreatedBy() + "/GrowthPhoto");

                DatabaseReference userRef = database.getReference(activeUser.getCreatedBy() + "/User");
               // photoRef = userRef.child(activeUser.getUserIDByOwner()).child("Growth").child(photo.getGrowthId());
                photoRef = userRef.child(activeUser.getUserId());
            }


            photoRef.child("Photo").child(photo.getPhotoId()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    currentFirebasePhoto = dataSnapshot.getValue(Photo.class);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //photoRef.child("Photo").child(photo.getPhotoId()).removeValue();
            //  Log.i("FIREBASE", url + "/n");


        }

    }

    public void deletePhotoFirebaseAfterSecond()

    {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {

                deleteSinglePhotoFirebase(currentPhoto, currentUser);
                deleteSinglePhotoStorage(currentFirebasePhoto);


                // Actions to do after 10 seconds
            }
        }, 5000);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu_growth, menu);

        MenuItem item = menu.findItem(R.id.top_delete);
        item.setVisible(true);   //hide it

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.top_delete:
                db.deletePhotoID(currentPhoto);
                deletePhotoFirebaseAfterSecond();
//                deleteSinglePhotoFirebase(currentPhoto, currentUser);
//                deleteSinglePhotoStorage(currentFirebasePhoto);
                //    deleteSinglePhotoStorage(currentPhoto,currentUser);
                final Intent data = new Intent();
                data.putExtra(PHOTO_POSITION, position);
                data.putExtra(IS_PHOTO_DELETE, true);
                setResult(Activity.RESULT_OK, data);
                Toast.makeText(this, "Deleted" + position,
                        Toast.LENGTH_SHORT).show();
                finish();

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}