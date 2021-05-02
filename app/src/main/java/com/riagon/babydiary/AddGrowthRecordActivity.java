package com.riagon.babydiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Firebase.GrowthFirebase;
import com.riagon.babydiary.Firebase.PhotoFirebase;
import com.riagon.babydiary.Model.Growth;
import com.riagon.babydiary.Model.Photo;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.Calculator;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.DbBitmapUtility;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.GridSpacingItemDecoration;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.PhotoAdapter;
import com.riagon.babydiary.Utility.SettingHelper;

import org.threeten.bp.Instant;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static com.riagon.babydiary.Utility.PhotoAdapter.REQUEST_CODE_UPDATE_PHOTO;
import static java.security.AccessController.getContext;

public class AddGrowthRecordActivity extends AppCompatActivity {
    public User currentUser;
    public DatabaseHelper db;
    public SettingHelper settingHelper;
    private Button button_minus_weight;
    private Button button_add_weight;
    private EditText edt_weight;
    private Button button_minus_height;
    private Button button_add_height;
    private EditText edt_height;
    private Button button_minus_head;
    private Button button_add_head;
    private EditText edt_head;
    private TextView dueDateTextTime;
    private TextView timepickerTime;
    private TextView noteEdittext;
    private TextView weightUnit, heightUnit, headUnit;
    private Button button_Add_Growth;
    private RecyclerView recyclerView;
    private PhotoAdapter adapterPhoto;
    //This is full detail photo list use to add to database
    private List<Photo> photoListTemp;
    //This is photo list without detail information when take picture or add from gallery
    private List<Photo> photoList;
    private List<Photo> photoListFirebase;
    private ImageView galleryIcon, cameraIcon;
    public byte[] userImage;
    private FormHelper formHelper;
    private DateFormatUtility dateFormatUtility;
    private LocalDataHelper localDataHelper;
    private Calculator calculator;
    public String growthUnit;
    public double growthWeight = 0;
    public double growthHead = 0;
    public double growthLength = 0;
    public String growthNote;
    public byte[] growthImage;
    public String userId;
    public Date growthDate, growthTime;
    private Boolean isUpdate = false;
    private Boolean isSyn = false;
    private String recordStatus;
    private String createBy;
    private Growth currentGrowth;
    private Growth lastGrowth;
    private int positionJustAddRecord = -1;
    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String IS_UPDATE = "IS_UPDATE";
    public static final String GROWTH_ID = "GROWTH_ID";
    public static final String POSITION = "POSITION";
    public static final String IS_DELETE = "IS_DELETE";
    //    public static final String IS_DUPLICATE = "IS_DUPLICATE";
    public static final int REQUEST_IMAGE_CAPTURE = 03061;
    public static final int REQUEST_GALLERY_IMAGE = 03062;
    public int position;
    public int photoPosition;
    public String photoCreatedDatetime;
    public Boolean isPhotoDelete;
    public ArrayList<Growth> tempGrowthList;
    public ArrayList<Growth> temp2GrowthList;
    String uniqueGrowthID;
    public String cameraFilePath;
    public Uri uriCam;
    private ActionBar toolbar;
    RelativeLayout rl_weight, rl_head, rl_height;
    Button add_weight, add_height, add_head, close_weight, close_head, close_height;
    private StorageReference storageRef;
    private FirebaseUser currentLoginUser;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private DatabaseReference growthRef;
    private StorageReference photoStorageRef;
    private DatabaseReference photoRef;
    private  PhotoFirebase photoFirebase;
    private GrowthFirebase growthFirebase;
    int mYear;
    int mMonth;
    int mDay;
    public long brithDay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        localDataHelper = new LocalDataHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        settingHelper.setThemes(currentUser.getUserTheme());
        setContentView(R.layout.activity_add_growth_record);
        calculator = new Calculator();
        initView();

        toolbar = getSupportActionBar();
        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeButtonEnabled(true);

        settingHelper.setBackgroundButtonAdd(this, button_Add_Growth, currentUser.getUserTheme());
        setDefaultTime();
        setInitValue();
        initValueForUpdate();
        setTitle();

        // initFirebaseValue();

        Locale locale = getResources().getConfiguration().locale;
        Locale.setDefault(locale);

        mAuth = FirebaseAuth.getInstance();
        currentLoginUser = mAuth.getCurrentUser();
        storageRef = storage.getReference();

        photoFirebase = new PhotoFirebase(this, currentLoginUser);
        growthFirebase= new GrowthFirebase(this,currentLoginUser);
        if (currentGrowth != null) {
           //getCurrentGrowthFirebasePhotoList(currentGrowth.getGrowthId());
            photoFirebase.getCurrentGrowthFirebasePhotoList(currentGrowth.getGrowthId());
        } else {
           // getCurrentGrowthFirebasePhotoList(uniqueGrowthID);
            photoFirebase.getCurrentGrowthFirebasePhotoList(uniqueGrowthID);
        }



        dueDateTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                try {

                    c.setTime(sdf.parse(dueDateTextTime.getText().toString()));
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddGrowthRecordActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dueDateTextTime.setText(String.format("%02d-%02d-%d", dayOfMonth, (monthOfYear + 1), year));

                            }

                        }, mYear, mMonth, mDay);
                // set max date picker dialog
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                // set min date picker dialog
                DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date date = formatter.parse(currentUser.getUserBirthday());
                    brithDay = date.getTime();

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                long longBrithDay = brithDay;

                datePickerDialog.getDatePicker().setMinDate(longBrithDay);

                datePickerDialog.show();
            }
        });


        timepickerTime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddGrowthRecordActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timepickerTime.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                //mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });


        button_add_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // button_add_weight.setBackgroundResource(R.drawable.button_addminus_selected);

                double curentNumber = 0;
                if (!edt_weight.getText().toString().isEmpty()) {
                    curentNumber = Double.parseDouble(edt_weight.getText().toString());
                }

                curentNumber = formHelper.addGrowth(curentNumber);
                BigDecimal bd = new BigDecimal(curentNumber).setScale(1, RoundingMode.HALF_UP);
                curentNumber = bd.doubleValue();
                edt_weight.setText(String.valueOf(curentNumber));
            }
        });

        button_minus_weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double curentNumber = 0;
                if (!edt_weight.getText().toString().isEmpty()) {
                    curentNumber = Double.parseDouble(edt_weight.getText().toString());
                }

                curentNumber = formHelper.minusGrowth(curentNumber);
                BigDecimal bd = new BigDecimal(curentNumber).setScale(1, RoundingMode.HALF_UP);
                curentNumber = bd.doubleValue();
                edt_weight.setText(String.valueOf(curentNumber));

            }
        });


        button_add_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double curentNumber = 0;
                if (!edt_height.getText().toString().isEmpty()) {
                    curentNumber = Double.parseDouble(edt_height.getText().toString());
                }

                curentNumber = formHelper.addGrowth(curentNumber);
                BigDecimal bd = new BigDecimal(curentNumber).setScale(1, RoundingMode.HALF_UP);
                curentNumber = bd.doubleValue();
                edt_height.setText(String.valueOf(curentNumber));
            }
        });
        button_minus_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double curentNumber = 0;
                if (!edt_height.getText().toString().isEmpty()) {
                    curentNumber = Double.parseDouble(edt_height.getText().toString());
                }
                curentNumber = formHelper.minusGrowth(curentNumber);
                BigDecimal bd = new BigDecimal(curentNumber).setScale(1, RoundingMode.HALF_UP);
                curentNumber = bd.doubleValue();
                edt_height.setText(String.valueOf(curentNumber));


            }
        });


        button_add_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                double curentNumber = 0;
                if (!edt_head.getText().toString().isEmpty()) {
                    curentNumber = Double.parseDouble(edt_head.getText().toString());
                }

                curentNumber = formHelper.addGrowth(curentNumber);
                BigDecimal bd = new BigDecimal(curentNumber).setScale(1, RoundingMode.HALF_UP);
                curentNumber = bd.doubleValue();
                edt_head.setText(String.valueOf(curentNumber));
            }
        });

        button_minus_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double curentNumber = 0;
                if (!edt_head.getText().toString().isEmpty()) {
                    curentNumber = Double.parseDouble(edt_head.getText().toString());
                }
                curentNumber = formHelper.minusGrowth(curentNumber);
                BigDecimal bd = new BigDecimal(curentNumber).setScale(1, RoundingMode.HALF_UP);
                curentNumber = bd.doubleValue();
                edt_head.setText(String.valueOf(curentNumber));

            }
        });


        galleryIcon.setOnClickListener(v -> {
            //  Toast.makeText(getApplicationContext(), "Gallery Icon", Toast.LENGTH_SHORT).show();
            pickFromGallery();
        });
        cameraIcon.setOnClickListener(v -> {
            // Toast.makeText(getApplicationContext(), "Camera Icon", Toast.LENGTH_SHORT).show();
            takeCameraImage();
        });

        button_Add_Growth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(), "Add Growth Data", Toast.LENGTH_SHORT).show();
                submitForm(true);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu_growth, menu);

        if (isUpdate) {
            MenuItem item = menu.findItem(R.id.top_delete);
            item.setVisible(true);   //hide it
        } else {
            MenuItem item = menu.findItem(R.id.top_delete);
            item.setVisible(false);   //hide it
        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.top_delete:
                db.deleteGrowth(currentGrowth);
                db.deletePhoto(currentGrowth);
                if (currentLoginUser != null) {
                    //deleteGrowthFirebase(currentGrowth);
                    growthFirebase.deleteGrowthFirebase(currentGrowth);
                    photoFirebase.deletePhotoFirebaseAfterSecond();
                }

                final Intent data = new Intent();
                data.putExtra(POSITION, position);
                data.putExtra(IS_DELETE, true);
                // Đặt resultCode là Activity.RESULT_OK to
                // thể hiện đã thành công và có chứa kết quả trả về
                setResult(Activity.RESULT_OK, data);
                Toast.makeText(AddGrowthRecordActivity.this, "Deleted",
                        Toast.LENGTH_SHORT).show();
                finish();

        }

        return super.onOptionsItemSelected(item);
    }


    public void setTitle() {
        if (isUpdate) {
            setTitle(getResources().getString(R.string.title_edit_growth));
            button_Add_Growth.setText(getResources().getString(R.string.dialog_save));
        } else {
            setTitle(getResources().getString(R.string.title_add_growth));
            button_Add_Growth.setText(getResources().getString(R.string.add));
        }
    }

    public void setDefaultTime() {
        dueDateTextTime.setText(formHelper.getDateNow());
        timepickerTime.setText(formHelper.getTimeNow());

    }

    public void initView() {
        button_minus_weight = (Button) findViewById(R.id.button_minus_weight);
        button_add_weight = (Button) findViewById(R.id.button_add_weight);
        edt_weight = (EditText) findViewById(R.id.edt_weight);
        button_minus_height = (Button) findViewById(R.id.button_minus_height);
        button_add_height = (Button) findViewById(R.id.button_add_height);
        edt_height = (EditText) findViewById(R.id.edt_height);
        button_minus_head = (Button) findViewById(R.id.button_minus_head);
        button_add_head = (Button) findViewById(R.id.button_add_head);
        edt_head = (EditText) findViewById(R.id.edt_head);
        headUnit = (TextView) findViewById(R.id.head_unit);
        heightUnit = (TextView) findViewById(R.id.height_unit);
        weightUnit = (TextView) findViewById(R.id.weight_unit);
        dueDateTextTime = (TextView) findViewById(R.id.dueDateTextTime);
        timepickerTime = (TextView) findViewById(R.id.timepickerTime);
        button_Add_Growth = (Button) findViewById(R.id.button_Add_Growth);
        noteEdittext = (EditText) findViewById(R.id.note);
        recyclerView = findViewById(R.id.recycler_view_photo);
        galleryIcon = findViewById(R.id.icon_gallery);
        cameraIcon = findViewById(R.id.icon_camera);
        rl_weight = findViewById(R.id.rl_weight);
        rl_head = findViewById(R.id.rl_head);
        rl_height = findViewById(R.id.rl_height);
        add_weight = findViewById(R.id.add_weight);
        add_height = findViewById(R.id.add_height);
        add_head = findViewById(R.id.add_head);
        close_weight = findViewById(R.id.close_weight);
        close_head = findViewById(R.id.close_head);
        close_height = findViewById(R.id.close_height);
        formHelper = new FormHelper(this);
        dateFormatUtility = new DateFormatUtility(this);
        tempGrowthList = new ArrayList<>();
        temp2GrowthList = new ArrayList<>();
    }

    public void setInitValue() {
        growthUnit = localDataHelper.getWhhUnit();
        growthNote = null;
        growthImage = null;
        uniqueGrowthID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
        userId = currentUser.getUserId();
        weightUnit.setText(settingHelper.getUnitFormat(growthUnit, "weight"));
        heightUnit.setText(settingHelper.getUnitFormat(growthUnit, "height"));
        headUnit.setText(settingHelper.getUnitFormat(growthUnit, "head"));
        photoList = new ArrayList<>();
        photoListFirebase = new ArrayList<>();
        adapterPhoto = new PhotoAdapter(this, photoList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(5), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterPhoto);


    }


    private void pickFromGallery() {
        //Create an Intent with action as ACTION_PICK

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            // Sets the type as image/*. This ensures only components of type image are selected
                            intent.setType("image/*");
                            //We pass an extra array with the accepted mime types. This will ensure only components with these MIME types as targeted.
                            String[] mimeTypes = {"image/jpeg", "image/png"};
                            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                            // Launching the Intent
                            startActivityForResult(intent, REQUEST_GALLERY_IMAGE);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();


    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");

        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        // cameraFilePath = "file://" + image.getAbsolutePath();
        cameraFilePath = image.getAbsolutePath();
        return image;

    }


    public static void clearCache(Context context) {
        File path = new File(context.getExternalCacheDir(), "camera");
        if (path.exists() && path.isDirectory()) {
            for (File child : path.listFiles()) {
                child.delete();
            }
        }
    }


    private void takeCameraImage() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            try {
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", createImageFile()));
                                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result code is RESULT_OK only if the user selects an Image
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK)
            switch (requestCode) {
                case REQUEST_GALLERY_IMAGE:

                    Uri selectedImage = data.getData();

                    String realPath = getRealPathFromURI(selectedImage);

                    Log.i("PHOTO", "Gallery " + realPath);

                    Bitmap loadedBitmap = BitmapFactory.decodeFile(realPath);

                    ExifInterface exif = null;
                    try {
                        File pictureFile = new File(realPath);
                        exif = new ExifInterface(pictureFile.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int orientation = ExifInterface.ORIENTATION_NORMAL;

                    if (exif != null)
                        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    switch (orientation) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            loadedBitmap = rotateBitmap(loadedBitmap, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            loadedBitmap = rotateBitmap(loadedBitmap, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            loadedBitmap = rotateBitmap(loadedBitmap, 270);
                            break;
                    }


                    DbBitmapUtility dbBitmapUtility = new DbBitmapUtility();
                    // userImage = dbBitmapUtility.getBytes(getScaledBitmap(loadedBitmap));
                    userImage = dbBitmapUtility.getHighBytes(getScaledBitmap(loadedBitmap));

                    //  photoListTemp= new ArrayList<>();
                    // Photo photo = new Photo(userImage);
                    addPhoto(userImage);
//                    photoList.add(photo);
//                    adapterPhoto.notifyDataSetChanged();

                    break;

                case REQUEST_IMAGE_CAPTURE:

                    Log.i("PHOTO", "Cam " + cameraFilePath);

                    ExifInterface exif2 = null;
                    try {
                        // File pictureFile = new File(realPath);
                        exif2 = new ExifInterface(cameraFilePath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    int orientation2 = ExifInterface.ORIENTATION_NORMAL;

                    if (exif2 != null)
                        orientation2 = exif2.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

                    Bitmap loadedBitmap2 = BitmapFactory.decodeFile(cameraFilePath);

                    switch (orientation2) {
                        case ExifInterface.ORIENTATION_ROTATE_90:
                            loadedBitmap2 = rotateBitmap(loadedBitmap2, 90);
                            break;
                        case ExifInterface.ORIENTATION_ROTATE_180:
                            loadedBitmap2 = rotateBitmap(loadedBitmap2, 180);
                            break;

                        case ExifInterface.ORIENTATION_ROTATE_270:
                            loadedBitmap2 = rotateBitmap(loadedBitmap2, 270);
                            break;
                    }

                    DbBitmapUtility dbBitmapUtility2 = new DbBitmapUtility();
                    // userImage = dbBitmapUtility.getBytes(getScaledBitmap(loadedBitmap));
                    userImage = dbBitmapUtility2.getHighBytes(getScaledBitmap(loadedBitmap2));

                    //  photoListTemp= new ArrayList<>();
                    //Photo photo2 = new Photo(userImage);
                    addPhoto(userImage);
//                    photoList.add(photo2);
//                    adapterPhoto.notifyDataSetChanged();
                    //Delete temprary catche file
                    File fdelete = new File(cameraFilePath);
                    fdelete.delete();

                    break;
                case REQUEST_CODE_UPDATE_PHOTO:

                    isPhotoDelete = data.getExtras().getBoolean(PhotoDetailActivity.IS_PHOTO_DELETE, false);
                    if (isPhotoDelete) {
                        photoPosition = data.getExtras().getInt(PhotoDetailActivity.PHOTO_POSITION);
                        photoList.remove(photoPosition);
                        adapterPhoto.notifyDataSetChanged();

                    }
                    break;

            }
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        String path = "";
        if (getContentResolver() != null) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                path = cursor.getString(idx);
                cursor.close();
            }
        }
        return path;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public Bitmap getScaledBitmap(Bitmap bitmap) {

        //final int maxSize = 1080;
        final int maxSize = 640;
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if (inWidth > inHeight) {
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, false);

        return resizedBitmap;


    }


    public double getWeightByInit(Double weight, String weightUnit, String settingUnit) {
        double weightConverted = 0;
        if (weightUnit.equals(settingUnit)) {
            weightConverted = weight;
        } else {

            if (settingUnit.equals("cm-kg")) {
                weightConverted = calculator.convertLbKg(weight);
            } else {
                weightConverted = calculator.convertKgLb(weight);
            }

        }
        return weightConverted;
    }

    public double getLengthByInit(Double length, String lengthUnit, String settingUnit) {
        double lengthConverted = 0;
        if (lengthUnit.equals(settingUnit)) {
            lengthConverted = length;
        } else {

            if (settingUnit.equals("cm-kg")) {
                lengthConverted = calculator.convertInCm(length);
            } else {
                lengthConverted = calculator.convertCmIn(length);
            }

        }
        return lengthConverted;
    }


    private void initValueForUpdate() {

        isUpdate = getIntent().getBooleanExtra(IS_UPDATE, false);
        position = getIntent().getIntExtra(POSITION, 0);
        currentGrowth = (Growth) getIntent().getSerializableExtra(GROWTH_ID);

        if (isUpdate) {
            dueDateTextTime.setText(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(currentGrowth.getGrowthDate())));
            timepickerTime.setText(dateFormatUtility.getStringTimeFormat(dateFormatUtility.getTimeFormat(currentGrowth.getGrowthTime())));

            double currentHead = getLengthByInit(currentGrowth.getGrowthHead(), currentGrowth.getGrowthUnit(), growthUnit);
            double currentWeight = getWeightByInit(currentGrowth.getGrowthWeight(), currentGrowth.getGrowthUnit(), growthUnit);
            double currentHeight = getLengthByInit(currentGrowth.getGrowthLength(), currentGrowth.getGrowthUnit(), growthUnit);

            if (currentHead > 0) {
                showInput(rl_head, add_head, close_head, edt_head, currentHead);

            } else {
                // edt_head.setText(String.valueOf(0.0));
                hiddenInput(rl_head, add_head, close_head);
            }

            if (currentWeight > 0) {
                showInput(rl_weight, add_weight, close_weight, edt_weight, currentWeight);
            } else {
                hiddenInput(rl_weight, add_weight, close_weight);
            }


            if (currentHeight > 0) {
                showInput(rl_height, add_height, close_height, edt_height, currentHeight);
            } else {
                hiddenInput(rl_height, add_height, close_height);
            }


            noteEdittext.setText(currentGrowth.getGrowthNote());
            isSyn = currentGrowth.getSyn();
            recordStatus = currentGrowth.getRecordStatus();
            createBy = currentGrowth.getCreatedBy();

            // List<Photo> photoList2= new ArrayList<>();

            //show all photo for this growth
            photoList.addAll(db.getAllPhotoByGrowthId(currentGrowth.getGrowthId()));
            adapterPhoto = new PhotoAdapter(this, photoList);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 3);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(1), true));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapterPhoto);
        } else {

            //if add new growth
            //if there are last record
            if (db.getGrowthsCount(currentUser) > 0) {
                lastGrowth = db.getLastGrowth(currentUser.getUserId());
                if (lastGrowth.getGrowthWeight() > 0) {
                    Double lastWeight = getWeightByInit(lastGrowth.getGrowthWeight(), lastGrowth.getGrowthUnit(), growthUnit);
                    showInput(rl_weight, add_weight, close_weight, edt_weight, lastWeight);
                } else {
                    edt_weight.setText(String.valueOf(0));
                    hiddenInput(rl_weight, add_weight, close_weight);
                }

                if (lastGrowth.getGrowthLength() > 0) {
                    Double lastHeight = getLengthByInit(lastGrowth.getGrowthLength(), lastGrowth.getGrowthUnit(), growthUnit);
                    // edt_height.setText(String.valueOf(getLengthByInit(lastGrowth.getGrowthLength(), lastGrowth.getGrowthUnit(), growthUnit)));
                    showInput(rl_height, add_height, close_height, edt_height, lastHeight);

                } else {
                    edt_height.setText(String.valueOf(0));
                    hiddenInput(rl_height, add_height, close_height);
                    // edt_height.setText(String.valueOf(getLengthByInit(60.0,lastGrowth.getGrowthUnit(),growthUnit)));
                }


                if (lastGrowth.getGrowthHead() > 0) {

                    Double lastHead = getLengthByInit(lastGrowth.getGrowthHead(), lastGrowth.getGrowthUnit(), growthUnit);
                    showInput(rl_head, add_head, close_head, edt_head, lastHead);

                } else {
                    //  edt_head.setText(String.valueOf(getLengthByInit(45.0,lastGrowth.getGrowthUnit(),growthUnit)));
                    edt_head.setText(String.valueOf(0));
                    hiddenInput(rl_head, add_head, close_head);
                }

                //if there are no last record
            } else {

                edt_weight.setText(String.valueOf(0));
                hiddenInput(rl_weight, add_weight, close_weight);

                edt_height.setText(String.valueOf(0));
                hiddenInput(rl_height, add_height, close_height);

                edt_head.setText(String.valueOf(0));
                hiddenInput(rl_head, add_head, close_head);

            }


        }


    }


    public void hiddenInput(RelativeLayout rl, Button add, Button close) {

        rl.setVisibility(View.GONE);
        add.setVisibility(View.VISIBLE);
        rl.animate().translationX(1400).setDuration(100).setStartDelay(100);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add.animate().translationX(140).alpha(1).setDuration(100).setStartDelay(100);
                rl.animate().translationX(0).setDuration(100).setStartDelay(100);
                rl.setVisibility(View.VISIBLE);

            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add.animate().translationX(0).setDuration(100).setStartDelay(100);
                rl.animate().translationX(1400).alpha(1).setDuration(100).setStartDelay(100);
            }
        });

    }


    public void showInput(RelativeLayout rl, Button add, Button close, EditText edt, Double currentAmount) {

        rl.setVisibility(View.VISIBLE);
        add.setVisibility(View.GONE);
        edt.setText(String.valueOf(currentAmount));
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                add.animate().translationX(140).alpha(1).setDuration(100).setStartDelay(100);
                rl.animate().translationX(0).setDuration(100).setStartDelay(100);
                rl.setVisibility(View.VISIBLE);


            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.setVisibility(View.VISIBLE);
                add.animate().translationX(0).setDuration(100).setStartDelay(100);
                rl.animate().translationX(1400).alpha(1).setDuration(100).setStartDelay(100);
                edt.setText(String.valueOf(0));
            }
        });
    }


    //public void setTitle()


    //
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    public void submitForm(Boolean isCloseView) {


        if (!noteEdittext.getText().toString().trim().isEmpty()) {
            growthNote = noteEdittext.getText().toString();
        }

        growthDate = dateFormatUtility.getDateFormat(dueDateTextTime.getText().toString());
        growthTime = dateFormatUtility.getTimeFormat2(timepickerTime.getText().toString() + ":" + formHelper.getTimeSecondNow());

        Log.i("ADD", timepickerTime.getText().toString() + ":" + formHelper.getTimeSecondNow());

        if (!edt_weight.getText().toString().isEmpty()) {
            growthWeight = Double.parseDouble(edt_weight.getText().toString());
        }


        if (!edt_head.getText().toString().isEmpty()) {
            growthHead = Double.parseDouble(edt_head.getText().toString());
        }

        if (!edt_height.getText().toString().isEmpty()) {
            growthLength = Double.parseDouble(edt_height.getText().toString());
        }


        if (!isUpdate) {
            if (currentLoginUser != null) {
                createBy = currentLoginUser.getUid();
            }
            //String uniqueGrowthID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
            Growth growth = new Growth(uniqueGrowthID, growthUnit, growthWeight, growthLength, growthHead, growthNote, false, "Add", createBy, userId, dateFormatUtility.getStringDateFormat2(growthDate), dateFormatUtility.getStringTimeFormat2(growthTime));
            if (!db.checkIsGrowthIsExist(uniqueGrowthID)) {
                addGrowth(growth, isCloseView);
                // addPhotoFirebase(photo,currentUser);
            } else {
                updateGrowth(growth, positionJustAddRecord, isCloseView);
            }


        } else {


            //This is where update the growth
            Growth growthUpdate = new Growth(currentGrowth.getGrowthId(), growthUnit, growthWeight, growthLength, growthHead, growthNote, false, "Update", createBy, userId, dateFormatUtility.getStringDateFormat2(growthDate), dateFormatUtility.getStringTimeFormat2(growthTime));
            updateGrowth(growthUpdate, position, isCloseView);
            //Toast.makeText(getApplicationContext(), "Sucessful"+i, Toast.LENGTH_SHORT).show();

        }


    }


    public void updateGrowth(Growth growth, int position, Boolean isCloseView) {
        db.updateGrowth(growth);
       // addGrowthFirebase(growth);
        growthFirebase.pushGrowthToFirebase(growth);

        final Intent data = new Intent();
        data.putExtra(EXTRA_DATA, (Serializable) growth);
        data.putExtra(POSITION, position);
        setResult(Activity.RESULT_OK, data);
        if (isCloseView) {
            finish();
        }

    }


    public void addGrowth(Growth growth, Boolean isCloseView) {

        //String uniquePhotoID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
        db.addGrowth(growth);
        //addGrowthFirebase(growth);
        growthFirebase.pushGrowthToFirebase(growth);
        if (tempGrowthList.size() == 0) {
            tempGrowthList.addAll(db.getAllGrowths(db.getUser(localDataHelper.getActiveUserId())));
        }

        // int positionJustAddRecord = -1;
        for (int i = 0; i < tempGrowthList.size(); i++) {
            if (tempGrowthList.get(i).getGrowthId().equals(growth.getGrowthId())) {
                positionJustAddRecord = i;
                // break;  // uncomment to get the first instance
            }
        }

        final Intent data = new Intent();
        // Truyền data vào intent
        data.putExtra(EXTRA_DATA, (Serializable) growth);
        data.putExtra(POSITION, positionJustAddRecord);
        // Đặt resultCode là Activity.RESULT_OK to
        // thể hiện đã thành công và có chứa kết quả trả về
        setResult(Activity.RESULT_OK, data);
        // Toast.makeText(getApplicationContext(), "Sucessful", Toast.LENGTH_SHORT).show();
        //  }
        if (isCloseView) {
            finish();
        }


    }


    public void addPhoto(byte[] userImage) {
        PhotoFirebase photoFirebase = new PhotoFirebase(this, currentLoginUser);
        photoCreatedDatetime = dateFormatUtility.getStringDateFullFormat2(dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));

        if (!isUpdate) {

            if (currentLoginUser != null) {
                createBy = currentLoginUser.getUid();
            }

            if (!db.checkIsGrowthIsExist(uniqueGrowthID)) {
                submitForm(false);
                currentGrowth = (Growth) getIntent().getSerializableExtra(GROWTH_ID);
                // addPhotoFirebase(photo,currentUser);
            }

            String uniquePhotoID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
            Photo photo = new Photo(uniquePhotoID, userImage, false, "Add", createBy, uniqueGrowthID, currentUser.getUserId(), photoCreatedDatetime);
            db.addPhoto(photo);
            photoList.add(photo);
            adapterPhoto.notifyDataSetChanged();
            //addPhotoFirebase(photo);
            photoFirebase.pushPhotoToFirebase(photo);
        } else {
            String uniquePhotoID = String.valueOf(formHelper.convertUID(String.valueOf(UUID.randomUUID())));
            Photo photo = new Photo(uniquePhotoID, userImage, false, "Add", createBy, currentGrowth.getGrowthId(), currentUser.getUserId(), photoCreatedDatetime);
            db.addPhoto(photo);
            photoList.add(photo);
            adapterPhoto.notifyDataSetChanged();
            //addPhotoFirebase(photo);
            photoFirebase.pushPhotoToFirebase(photo);

        }


    }



    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public void onBackPressed() {
        finish();
    }

}
