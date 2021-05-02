package com.riagon.babydiary.Data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.riagon.babydiary.Model.Activities;
import com.riagon.babydiary.Model.Growth;
import com.riagon.babydiary.Model.Photo;
import com.riagon.babydiary.Model.Record;
import com.riagon.babydiary.Model.Stats;
import com.riagon.babydiary.Model.Timeline;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.R;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.FormHelper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_TAG = "DATABASE_TAG";
    private static final String DATABASE_NAME = "daily_baby";
    private static final int DATABASE_VERSION = 3;
    private Context mContext;
    private Boolean importStatus = false;

    //In common
    private static final String CREATED_DATETIME = "created_datetime";

    //Table User
    private static final String TABLE_USER_NAME = "User";
    private static final String USER_ID = "user_id";
    private static final String USER_IMAGE = "user_image";
    private static final String USER_NAME = "user_name";
    private static final String USER_BIRTHDAY = "user_birthday";
    private static final String USER_DUEDATE = "user_duedate";
    private static final String USER_SEX = "user_sex";
    private static final String USER_THEME = "user_theme";
    private static final String USER_SYN_STATUS = "user_syn_status";
    private static final String USER_REQUEST_STATUS = "user_request_status";
    private static final String USER_RECORD_STATUS = "user_record_status";
    private static final String USER_CREATE_BY = "user_create_by";
    private static final String USER_CREATE_DATE = "user_create_date";

    //Table Growth
    private static final String TABLE_GROWTH_NAME = "Growth";
    private static final String GROWTH_ID = "growth_id";
    private static final String GROWTH_UNIT = "growth_unit";
    private static final String GROWTH_WEIGHT = "growth_weight";
    private static final String GROWTH_HEAD = "growth_head";
    private static final String GROWTH_LENGTH = "growth_length";
    private static final String GROWTH_NOTE = "growth_note";
    private static final String GROWTH_SYN_STATUS = "growth_syn_status";
    private static final String GROWTH_RECORD_STATUS = "growth_record_status";
    private static final String GROWTH_CREATE_BY = "growth_create_by";
    private static final String GROWTH_DATE = "growth_date";
    private static final String GROWTH_TIME = "growth_time";

    //Table Activities

    private static final String TABLE_ACTIVITIES_NAME = "Activities";
    private static final String ACTIVITIES_ID = "activities_id";
    private static final String ACTIVITIES_FIXED_ID = "activities_fixed_id";
    private static final String ACTIVITIES_IS_SHOW = "activities_is_show";
    private static final String ACTIVITIES_RANK = "activities_rank";
    private static final String ACTIVITIES_IS_NOTIFY = "activities_is_notify";
    private static final String ACTIVITIES_NOTIFY_OPTION = "activities_notify_option";
    private static final String ACTIVITIES_NOTIFY_DATE = "activities_notify_date";
    private static final String ACTIVITIES_NOTIFY_TIME = "activities_notify_time";
    private static final String ACTIVITIES_NOTIFY_REPEAT_IN_DAY = "activities_notify_repeat_in_day";
    private static final String ACTIVITIES_NOTIFY_REPEAT_IN_TIME = "activities_notify_repeat_in_time";


    //Table Record

    private static final String TABLE_RECORD_NAME = "Record";
    private static final String RECORD_ID = "Record_id";
    private static final String RECORD_OPTION = "Record_option";
    private static final String RECORD_AMOUNT = "Record_amount";
    private static final String RECORD_AMOUNT_UNIT = "Record__amount_unit";
    private static final String RECORD_DATE_START = "Record_date_start";
    private static final String RECORD_TIME_START = "Record_time_start";
    private static final String RECORD_DATE_END = "Record_date_end";
    private static final String RECORD_TIME_END = "Record_time_end";
    private static final String RECORD_DURATION = "Record_duration";
    private static final String RECORD_NOTE = "Record_note";
    private static final String RECORD_SYN_STATUS = "Record_syn_status";
    private static final String RECORD_RECORD_STATUS = "Record_record_status";
    private static final String RECORD_CREATE_BY = "Record_create_by";
    private static final String RECORD_CREATE_DATE = "Record_create_date";


    //Table Record

    private static final String TABLE_PHOTO_NAME = "Photo";
    private static final String PHOTO_ID = "Photo_id";
    private static final String PHOTO_IMAGE = "Photo_image";
    private static final String PHOTO_SYN_STATUS = "photo_syn_status";
    private static final String PHOTO_RECORD_STATUS = "photo_record_status";
    private static final String PHOTO_CREATE_BY = "photo_create_by";
    private static final String PHOTO_CREATE_DATE = "photo_create_date";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        Log.d("DB Version", "" + DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlQuery = "CREATE TABLE " + TABLE_USER_NAME + "("
                + USER_ID + " TEXT PRIMARY KEY,"
                + USER_IMAGE + " BLOB," +
                USER_NAME + " TEXT," +
                USER_BIRTHDAY + " TEXT," +
                USER_DUEDATE + " TEXT," +
                USER_SEX + " TEXT," +
                USER_THEME + " TEXT," +
                USER_SYN_STATUS + " TEXT," +
                USER_REQUEST_STATUS + " TEXT," +
                USER_RECORD_STATUS + " TEXT," +
                USER_CREATE_BY + " TEXT," +
                USER_CREATE_DATE + " TEXT)";

        String sqlQueryGrowth = "CREATE TABLE " + TABLE_GROWTH_NAME + "("
                + GROWTH_ID + " TEXT PRIMARY KEY,"
                + GROWTH_UNIT + " INTEGER,"
                + GROWTH_WEIGHT + " INTEGER," +
                GROWTH_HEAD + " INTEGER," +
                GROWTH_LENGTH + " INTEGER," +
                GROWTH_NOTE + " TEXT," +
                GROWTH_SYN_STATUS + " TEXT," +
                GROWTH_RECORD_STATUS + " TEXT," +
                GROWTH_CREATE_BY + " TEXT," +
                USER_ID + " INTEGER," +
                GROWTH_DATE + " TEXT," +
                GROWTH_TIME + " TEXT)";


        String sqlQueryActivities = "CREATE TABLE " + TABLE_ACTIVITIES_NAME + "("
                + ACTIVITIES_ID + " INTEGER PRIMARY KEY,"
                + ACTIVITIES_FIXED_ID + " INTEGER," +
                ACTIVITIES_IS_SHOW + " TEXT," +
                ACTIVITIES_RANK + " INTEGER," +
                ACTIVITIES_IS_NOTIFY + " TEXT," +
                ACTIVITIES_NOTIFY_OPTION + " TEXT," +
                ACTIVITIES_NOTIFY_DATE + " TEXT," +
                ACTIVITIES_NOTIFY_TIME + " TEXT," +
                ACTIVITIES_NOTIFY_REPEAT_IN_DAY + " TEXT," +
                ACTIVITIES_NOTIFY_REPEAT_IN_TIME + " TEXT," +
                CREATED_DATETIME + " TEXT," +
                USER_ID + " INTEGER)";

        String sqlQueryRecord = "CREATE TABLE " + TABLE_RECORD_NAME + "("
                + RECORD_ID + " TEXT PRIMARY KEY,"
                + RECORD_OPTION + " TEXT," +
                RECORD_AMOUNT + " INTEGER," +
                RECORD_AMOUNT_UNIT + " TEXT," +
                RECORD_DATE_START + " TEXT," +
                RECORD_TIME_START + " TEXT," +
                RECORD_DATE_END + " TEXT," +
                RECORD_TIME_END + " TEXT," +
                RECORD_DURATION + " TEXT," +
                RECORD_NOTE + " TEXT," +
                ACTIVITIES_ID + " INTEGER," +
                USER_ID + " INTEGER," +
                RECORD_SYN_STATUS + " TEXT," +
                RECORD_RECORD_STATUS + " TEXT," +
                RECORD_CREATE_BY + " TEXT," +
                RECORD_CREATE_DATE + " TEXT)";

        String sqlQueryPhoto = "CREATE TABLE " + TABLE_PHOTO_NAME + "("
                + PHOTO_ID + " TEXT PRIMARY KEY," +
                PHOTO_IMAGE + " BLOB," +
                PHOTO_SYN_STATUS + " TEXT," +
                PHOTO_RECORD_STATUS + " TEXT," +
                PHOTO_CREATE_BY + " TEXT," +
                GROWTH_ID + " TEXT," +
                USER_ID + " TEXT," +
                PHOTO_CREATE_DATE + " TEXT)";


        // db.execSQL(sqlQueryBreastfeed);
        db.execSQL(sqlQuery);
        db.execSQL(sqlQueryGrowth);
        db.execSQL(sqlQueryActivities);
        db.execSQL(sqlQueryRecord);
        db.execSQL(sqlQueryPhoto);
        // Log.d("DBManager", "2: ");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_GROWTH_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ACTIVITIES_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTO_NAME);
        // db.execSQL("DROP TABLE IF EXISTS " + TABLE_BREASTPUMP_NAME);

        onCreate(db);
        // Log.d("DBManager", "3: ");
    }


    public void backup(String outFileName) {

        //database path
        final String inFileName = mContext.getDatabasePath(DATABASE_NAME).toString();
        String backupSucessful = mContext.getResources().getString(R.string.bk_mes_sucess);
        String backupErr = mContext.getResources().getString(R.string.bk_mes_er);
        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();

            // Toast.makeText(mContext, backupSucessful, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            //  Toast.makeText(mContext, backupErr, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public Boolean getImportStatus() {
        return importStatus;
    }

    public Boolean importDB(String inFileName) {

        final String outFileName = mContext.getDatabasePath(DATABASE_NAME).toString();

        try {

            File dbFile = new File(inFileName);
            FileInputStream fis = new FileInputStream(dbFile);
            // localData = new LocalData(mContext);
            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFileName);

            // Transfer bytes from the input file to the output file
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();
            importStatus = true;


            // Toast.makeText(mContext, mContext.getResources().getString(R.string.bk_import_sucess), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.bk_import_er), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            return importStatus;
        }
    }


    public void prepareCategoryList(String userID) {


        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        FormHelper formHelper = new FormHelper();
        //  DateFormatUtility dateFormatUtility = new DateFormatUtility();
        Activities a1 = new Activities(1, true, 0, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a1);

        Activities a2 = new Activities(2, true, 1, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a2);

        Activities a3 = new Activities(3, true, 2, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);

        addActivities(a3);

        Activities a4 = new Activities(4, true, 3, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a4);

        Activities a5 = new Activities(5, true, 4, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a5);

        Activities a6 = new Activities(6, true, 5, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);

        addActivities(a6);

        Activities a7 = new Activities(7, true, 6, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a7);

        Activities a8 = new Activities(8, true, 7, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a8);

        Activities a9 = new Activities(9, true, 8, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a9);

        Activities a10 = new Activities(10, false, 9, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a10);

        Activities a11 = new Activities(11, false, 10, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a11);

        Activities a12 = new Activities(12, false, 11, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a12);

        Activities a13 = new Activities(13, false, 12, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a13);

        Activities a14 = new Activities(14, false, 13, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a14);

        Activities a15 = new Activities(15, false, 14, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a15);

        Activities a16 = new Activities(16, false, 15, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a16);

        Activities a17 = new Activities(17, false, 16, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a17);

        Activities a18 = new Activities(18, false, 17, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a18);

        Activities a19 = new Activities(19, false, 18, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a19);

        Activities a20 = new Activities(20, false, 19, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a20);

        Activities a21 = new Activities(21, true, 20, false, "Default", null, null, null, null, dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()), userID);
        addActivities(a21);


        //  adapterActivitiesConfig.notifyDataSetChanged();
    }

    //
    public long addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_IMAGE, user.getUserImage());
        values.put(USER_NAME, user.getUserName());
        values.put(USER_BIRTHDAY, user.getUserBirthday());
        values.put(USER_DUEDATE, user.getUserDuedate());
        values.put(USER_SEX, user.getUserSex());
        values.put(USER_THEME, user.getUserTheme());
        // values.put(USER_ACTIVE, user.getActive());
        values.put(USER_SYN_STATUS, user.getSyn());
        values.put(USER_REQUEST_STATUS, user.getRequestStatus());
        values.put(USER_RECORD_STATUS, user.getRecordStatus());
        values.put(USER_CREATE_BY, user.getCreatedBy());
        values.put(USER_CREATE_DATE, user.getUserCreatedDatetime());
        long id = db.insert(TABLE_USER_NAME, null, values);
        db.close();
        return id;
    }

    //
    public long addUserWithID(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_ID, user.getUserId());
        values.put(USER_IMAGE, user.getUserImage());
        values.put(USER_NAME, user.getUserName());
        values.put(USER_BIRTHDAY, user.getUserBirthday());
        values.put(USER_DUEDATE, user.getUserDuedate());
        values.put(USER_SEX, user.getUserSex());
        values.put(USER_THEME, user.getUserTheme());
        values.put(USER_SYN_STATUS, user.getSyn());
        values.put(USER_REQUEST_STATUS, user.getRequestStatus());
        values.put(USER_RECORD_STATUS, user.getRecordStatus());
        values.put(USER_CREATE_BY, user.getCreatedBy());
        values.put(USER_CREATE_DATE, user.getUserCreatedDatetime());
        long id = db.insert(TABLE_USER_NAME, null, values);
        db.close();
        return id;
    }

    public int updateUser(User user) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_IMAGE, user.getUserImage());
        values.put(USER_NAME, user.getUserName());
        values.put(USER_BIRTHDAY, user.getUserBirthday());
        values.put(USER_DUEDATE, user.getUserDuedate());
        values.put(USER_SEX, user.getUserSex());
        values.put(USER_THEME, user.getUserTheme());
        values.put(USER_SYN_STATUS, user.getSyn());
        values.put(USER_REQUEST_STATUS, user.getRequestStatus());
        values.put(USER_RECORD_STATUS, user.getRecordStatus());
        values.put(USER_CREATE_BY, user.getCreatedBy());
        values.put(USER_CREATE_DATE, user.getUserCreatedDatetime());

        //db.close();
        // updating row
        return db.update(TABLE_USER_NAME, values, USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});


    }

    public int updateUserActive(User user, Boolean isActive) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        //values.put(USER_ACTIVE, isActive);

        //   db.close();
        // updating row
        return db.update(TABLE_USER_NAME, values, USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
    }

    public int updateUserSyn(User user, Boolean isSyn, String userCreatedBy) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_SYN_STATUS, isSyn);
        values.put(USER_CREATE_BY, userCreatedBy);

        //   db.close();
        // updating row
        return db.update(TABLE_USER_NAME, values, USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
    }


    public int updateUserRecordStatus(User user, String recordStatus) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_RECORD_STATUS, recordStatus);

        //   db.close();
        // updating row
        return db.update(TABLE_USER_NAME, values, USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
    }


    public User getUser(String id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_USER_NAME,
                new String[]{USER_ID, USER_IMAGE, USER_NAME, USER_BIRTHDAY, USER_DUEDATE, USER_SEX, USER_THEME, USER_SYN_STATUS, USER_REQUEST_STATUS, USER_RECORD_STATUS, USER_CREATE_BY, USER_CREATE_DATE},
                USER_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        //rc.setRecordWeightUnit(c.getString(c.getColumnIndex(RECORD_WEIGHT_UNIT)));
        if (cursor != null)
            cursor.moveToFirst();

        User user = new User();
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        Boolean isSyn = (cursor.getInt((cursor.getColumnIndex(USER_SYN_STATUS))) == 1) ? true : false;
        user.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
        user.setUserImage(cursor.getBlob(cursor.getColumnIndex(USER_IMAGE)));
        user.setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
        user.setUserBirthday(cursor.getString(cursor.getColumnIndex(USER_BIRTHDAY)));
        user.setUserDuedate(cursor.getString(cursor.getColumnIndex(USER_DUEDATE)));
        user.setUserSex(cursor.getString(cursor.getColumnIndex(USER_SEX)));
        user.setUserTheme(cursor.getString(cursor.getColumnIndex(USER_THEME)));
        user.setSyn(isSyn);
        user.setRequestStatus(cursor.getString(cursor.getColumnIndex(USER_REQUEST_STATUS)));
        user.setRecordStatus(cursor.getString(cursor.getColumnIndex(USER_RECORD_STATUS)));
        user.setCreatedBy(cursor.getString(cursor.getColumnIndex(USER_CREATE_BY)));
        user.setUserCreatedDatetime(cursor.getString(cursor.getColumnIndex(USER_CREATE_DATE)));
        // close the db connection
        db.close();
        cursor.close();

        return user;
    }


    public int getUsersCount() {
        // Log.i(DATABASE_TAG, "MyDatabaseHelper.getUsersCount ... ");

        String countQuery = "SELECT  * FROM " + TABLE_USER_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        // return count
        return count;
    }

    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_NAME, USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
    }

    //
    public void deleteUserId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_USER_NAME, USER_ID + " = ?",
                new String[]{id});
        Log.i("CARE", "Sucessfully delete" + id);
        db.close();
    }


    //
    public void deleteAllUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = " DELETE FROM " + TABLE_USER_NAME;
        db.execSQL(sql);
        db.close();
    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_NAME +
                " ORDER BY " + USER_ID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //  DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();

                Boolean isSyn = (cursor.getInt((cursor.getColumnIndex(USER_SYN_STATUS))) == 1) ? true : false;
                user.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                user.setUserImage(cursor.getBlob(cursor.getColumnIndex(USER_IMAGE)));
                user.setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                user.setUserBirthday(cursor.getString(cursor.getColumnIndex(USER_BIRTHDAY)));
                user.setUserDuedate(cursor.getString(cursor.getColumnIndex(USER_DUEDATE)));
                user.setUserSex(cursor.getString(cursor.getColumnIndex(USER_SEX)));
                user.setUserTheme(cursor.getString(cursor.getColumnIndex(USER_THEME)));
                user.setSyn(isSyn);
                user.setRequestStatus(cursor.getString(cursor.getColumnIndex(USER_REQUEST_STATUS)));
                user.setRecordStatus(cursor.getString(cursor.getColumnIndex(USER_RECORD_STATUS)));
                user.setCreatedBy(cursor.getString(cursor.getColumnIndex(USER_CREATE_BY)));
                user.setUserCreatedDatetime(cursor.getString(cursor.getColumnIndex(USER_CREATE_DATE)));

                users.add(user);
                // adding to todo list

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }
    //-------------------Tuan added----------------------------
    public List<String> getAllUserIds() {
        List<String> userIds = new ArrayList<>();
        String selectQuery = "SELECT "+USER_ID+" FROM " + TABLE_USER_NAME ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //  DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                userIds.add(cursor.getString(cursor.getColumnIndex(USER_ID)));
                // adding to todo list
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userIds;
    }

    public String getUserName(String userId) {
        String userName="";
        String selectQuery = "SELECT "+USER_NAME+" FROM " + TABLE_USER_NAME +" WHERE "+USER_ID+" = ?";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{userId});
        //  DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                userName=(cursor.getString(cursor.getColumnIndex(USER_NAME)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return userName;
    }
//-----------------------------------------------------

    public List<User> getAllUsersNotSyn() {
        List<User> users = new ArrayList<User>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER_NAME + " WHERE " + USER_SYN_STATUS + "=0" + " AND " + USER_REQUEST_STATUS + " = " + "'No'" +
                " ORDER BY " + USER_ID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        //  DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();

                Boolean isSyn = (cursor.getInt((cursor.getColumnIndex(USER_SYN_STATUS))) == 1) ? true : false;
                user.setUserId(cursor.getString(cursor.getColumnIndex(USER_ID)));
                user.setUserImage(cursor.getBlob(cursor.getColumnIndex(USER_IMAGE)));
                user.setUserName(cursor.getString(cursor.getColumnIndex(USER_NAME)));
                user.setUserBirthday(cursor.getString(cursor.getColumnIndex(USER_BIRTHDAY)));
                user.setUserDuedate(cursor.getString(cursor.getColumnIndex(USER_DUEDATE)));
                user.setUserSex(cursor.getString(cursor.getColumnIndex(USER_SEX)));
                user.setUserTheme(cursor.getString(cursor.getColumnIndex(USER_THEME)));
                user.setSyn(isSyn);
                user.setRequestStatus(cursor.getString(cursor.getColumnIndex(USER_REQUEST_STATUS)));
                user.setRecordStatus(cursor.getString(cursor.getColumnIndex(USER_RECORD_STATUS)));
                user.setCreatedBy(cursor.getString(cursor.getColumnIndex(USER_CREATE_BY)));
                user.setUserCreatedDatetime(cursor.getString(cursor.getColumnIndex(USER_CREATE_DATE)));

                users.add(user);
                // adding to todo list

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return users;
    }


    public boolean checkIsUserIsExist(String userID) {

        String selectQuery = "SELECT * FROM " + TABLE_USER_NAME + " WHERE " + USER_ID + "='" + userID + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    //CRUD Growth Table

    public String addGrowth(Growth growth) {
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        ContentValues values = new ContentValues();
        values.put(GROWTH_ID, growth.getGrowthId());
        values.put(GROWTH_UNIT, growth.getGrowthUnit());
        values.put(GROWTH_WEIGHT, growth.getGrowthWeight());
        values.put(GROWTH_LENGTH, growth.getGrowthLength());
        values.put(GROWTH_HEAD, growth.getGrowthHead());
        values.put(GROWTH_NOTE, growth.getGrowthNote());
        values.put(GROWTH_SYN_STATUS, growth.getSyn());
        values.put(GROWTH_RECORD_STATUS, growth.getRecordStatus());
        values.put(GROWTH_CREATE_BY, growth.getCreatedBy());
        values.put(USER_ID, growth.getUserId());
        values.put(GROWTH_DATE, growth.getGrowthDate());
        values.put(GROWTH_TIME, growth.getGrowthTime());

//        values.put(GROWTH_DATE, dateFormat.getStringDateFormat2(growth.getGrowthDate()));
//        values.put(GROWTH_TIME, dateFormat.getStringTimeFormat(growth.getGrowthTime()));

        String id = String.valueOf(db.insert(TABLE_GROWTH_NAME, null, values));
        db.close();
        return id;

    }


    public Growth getGrowth(String growthID) {

        String selectQuery = "SELECT * " + " FROM " + TABLE_GROWTH_NAME + " WHERE " + GROWTH_ID + " = " + "'" + growthID + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        Boolean isSyn = (cursor.getInt((cursor.getColumnIndex(GROWTH_SYN_STATUS))) == 1) ? true : false;
        Growth growth = new Growth(cursor.getString(cursor.getColumnIndex(GROWTH_ID)),
                cursor.getString(cursor.getColumnIndex(GROWTH_UNIT)),
                cursor.getDouble(cursor.getColumnIndex(GROWTH_WEIGHT)),
                cursor.getDouble(cursor.getColumnIndex(GROWTH_LENGTH)),
                cursor.getDouble(cursor.getColumnIndex(GROWTH_HEAD)),
                cursor.getString(cursor.getColumnIndex(GROWTH_NOTE)),
                isSyn,
                cursor.getString(cursor.getColumnIndex(GROWTH_RECORD_STATUS)),
                cursor.getString(cursor.getColumnIndex(GROWTH_CREATE_BY)),
                cursor.getString(cursor.getColumnIndex(USER_ID)),
                cursor.getString(cursor.getColumnIndex(GROWTH_DATE)),
                cursor.getString(cursor.getColumnIndex(GROWTH_TIME)));

        cursor.close();
        db.close();
        return growth;

    }


    public int updateGrowth(Growth growth) {
        SQLiteDatabase db = this.getWritableDatabase();
        //  DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        ContentValues values = new ContentValues();
        values.put(GROWTH_UNIT, growth.getGrowthUnit());
        values.put(GROWTH_WEIGHT, growth.getGrowthWeight());
        values.put(GROWTH_LENGTH, growth.getGrowthLength());
        values.put(GROWTH_HEAD, growth.getGrowthHead());
        values.put(GROWTH_NOTE, growth.getGrowthNote());
        values.put(GROWTH_SYN_STATUS, growth.getSyn());
        values.put(GROWTH_RECORD_STATUS, growth.getRecordStatus());
        values.put(GROWTH_CREATE_BY, growth.getCreatedBy());
        values.put(USER_ID, growth.getUserId());
        values.put(GROWTH_DATE, growth.getGrowthDate());
        values.put(GROWTH_TIME, growth.getGrowthTime());
        // updating row
        return db.update(TABLE_GROWTH_NAME, values, GROWTH_ID + " = ?",
                new String[]{String.valueOf(growth.getGrowthId())});
    }

    public int updateGrowthSyn(Growth growth, Boolean isSyn, String createdBy) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(GROWTH_SYN_STATUS, isSyn);
        values.put(GROWTH_CREATE_BY, createdBy);
        //   db.close();
        // updating row
        return db.update(TABLE_GROWTH_NAME, values, GROWTH_ID + " = ?",
                new String[]{String.valueOf(growth.getGrowthId())});
    }


    public int getGrowthsCount(User user) {
        String countQuery = "SELECT  * FROM " + TABLE_GROWTH_NAME + " WHERE " + USER_ID + " = " + "'" + user.getUserId() + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        // Log.i(DATABASE_TAG, "DatabaseHelper.getGrowthsCount ..." + count);
        // return count
        return count;
    }


    public void deleteGrowth(Growth growth) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROWTH_NAME, GROWTH_ID + " = ?",
                new String[]{String.valueOf(growth.getGrowthId())});
        db.close();
    }

    public void deleteAllGrowthByUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROWTH_NAME, USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
    }

    public Boolean deleteAllGrowthByUserAlreadySyned(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROWTH_NAME, USER_ID + " = ?" + " AND " + GROWTH_SYN_STATUS + "=1",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
        return true;
    }


    //
    public void deleteGrowthId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_GROWTH_NAME, GROWTH_ID + " = ?",
                new String[]{id});
        db.close();
    }


//    public Growth getGrowthById(String id) {
//        // get readable database as we are not inserting anything
//        SQLiteDatabase db = this.getReadableDatabase();
//        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
//        Cursor cursor = db.query(TABLE_GROWTH_NAME,
//                new String[]{GROWTH_ID, GROWTH_UNIT, GROWTH_WEIGHT, GROWTH_LENGTH, GROWTH_HEAD, GROWTH_NOTE, USER_ID, GROWTH_DATE, GROWTH_TIME},
//                GROWTH_ID + "=?",
//                new String[]{String.valueOf(id)}, null, null, null, null);
//        //rc.setRecordWeightUnit(c.getString(c.getColumnIndex(RECORD_WEIGHT_UNIT)));
//        if (cursor != null)
//            cursor.moveToFirst();
//        Boolean isSyn = (cursor.getInt((cursor.getColumnIndex(GROWTH_SYN_STATUS))) == 1) ? true : false;
//        Growth growth = new Growth(cursor.getString(cursor.getColumnIndex(GROWTH_ID)),
//                cursor.getString(cursor.getColumnIndex(GROWTH_UNIT)),
//                cursor.getDouble(cursor.getColumnIndex(GROWTH_WEIGHT)),
//                cursor.getDouble(cursor.getColumnIndex(GROWTH_LENGTH)),
//                cursor.getDouble(cursor.getColumnIndex(GROWTH_HEAD)),
//                cursor.getString(cursor.getColumnIndex(GROWTH_NOTE)),
//                isSyn,
//                cursor.getString(cursor.getColumnIndex(GROWTH_RECORD_STATUS)),
//                cursor.getString(cursor.getColumnIndex(USER_ID)),
//                cursor.getString(cursor.getColumnIndex(GROWTH_DATE)),
//                cursor.getString(cursor.getColumnIndex(GROWTH_TIME)));
//
//        // close the db connection
//        db.close();
//        cursor.close();
//
//        return growth;
//    }


    public Growth getLastGrowth(String userID) {

        String selectQuery = "SELECT * " + " FROM " + TABLE_GROWTH_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" +
                " ORDER BY " + GROWTH_DATE + " DESC " + " , " + GROWTH_TIME + " DESC " + " LIMIT 1";
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        SQLiteDatabase db = this.getReadableDatabase();
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor != null)
            cursor.moveToFirst();
        Boolean isSyn = (cursor.getInt((cursor.getColumnIndex(GROWTH_SYN_STATUS))) == 1) ? true : false;
        Growth growth = new Growth(cursor.getString(cursor.getColumnIndex(GROWTH_ID)),
                cursor.getString(cursor.getColumnIndex(GROWTH_UNIT)),
                cursor.getDouble(cursor.getColumnIndex(GROWTH_WEIGHT)),
                cursor.getDouble(cursor.getColumnIndex(GROWTH_LENGTH)),
                cursor.getDouble(cursor.getColumnIndex(GROWTH_HEAD)),
                cursor.getString(cursor.getColumnIndex(GROWTH_NOTE)),
                isSyn,
                cursor.getString(cursor.getColumnIndex(GROWTH_RECORD_STATUS)),
                cursor.getString(cursor.getColumnIndex(GROWTH_CREATE_BY)),
                cursor.getString(cursor.getColumnIndex(USER_ID)),
                cursor.getString(cursor.getColumnIndex(GROWTH_DATE)),
                cursor.getString(cursor.getColumnIndex(GROWTH_TIME)));

        cursor.close();
        db.close();
        return growth;

    }


    public List<Growth> getAllGrowths(User user) {
        List<Growth> growths = new ArrayList<Growth>();
        String selectQuery = "SELECT  * FROM " + TABLE_GROWTH_NAME + " WHERE " + USER_ID + " = " + "'" + user.getUserId() + "'" +
                " ORDER BY " + GROWTH_DATE + " DESC" + "," + GROWTH_TIME + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Boolean isSyn = (c.getInt((c.getColumnIndex(GROWTH_SYN_STATUS))) == 1) ? true : false;
                Growth growth = new Growth();
                growth.setGrowthId(c.getString(c.getColumnIndex(GROWTH_ID)));
                growth.setGrowthUnit(c.getString(c.getColumnIndex(GROWTH_UNIT)));
                growth.setGrowthWeight(c.getDouble(c.getColumnIndex(GROWTH_WEIGHT)));
                growth.setGrowthLength(c.getDouble(c.getColumnIndex(GROWTH_LENGTH)));
                growth.setGrowthHead(c.getDouble(c.getColumnIndex(GROWTH_HEAD)));
                growth.setGrowthNote(c.getString(c.getColumnIndex(GROWTH_NOTE)));
                growth.setSyn(isSyn);
                growth.setRecordStatus(c.getString(c.getColumnIndex(GROWTH_RECORD_STATUS)));
                growth.setCreatedBy(c.getString(c.getColumnIndex(GROWTH_CREATE_BY)));
                growth.setUserId(c.getString(c.getColumnIndex(USER_ID)));
                growth.setGrowthDate(c.getString(c.getColumnIndex(GROWTH_DATE)));
                growth.setGrowthTime(c.getString(c.getColumnIndex(GROWTH_TIME)));
                growths.add(growth);
                // adding to todo list

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return growths;
    }

    public List<Growth> getAllGrowthNotSynByUser(String userID) {
        List<Growth> growths = new ArrayList<Growth>();
        String selectQuery = "SELECT  * FROM " + TABLE_GROWTH_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + GROWTH_SYN_STATUS + "=0" +
                " ORDER BY " + GROWTH_ID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        //  DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Boolean isSyn = (c.getInt((c.getColumnIndex(GROWTH_SYN_STATUS))) == 1) ? true : false;
                Growth growth = new Growth();
                growth.setGrowthId(c.getString(c.getColumnIndex(GROWTH_ID)));
                growth.setGrowthUnit(c.getString(c.getColumnIndex(GROWTH_UNIT)));
                growth.setGrowthWeight(c.getDouble(c.getColumnIndex(GROWTH_WEIGHT)));
                growth.setGrowthLength(c.getDouble(c.getColumnIndex(GROWTH_LENGTH)));
                growth.setGrowthHead(c.getDouble(c.getColumnIndex(GROWTH_HEAD)));
                growth.setGrowthNote(c.getString(c.getColumnIndex(GROWTH_NOTE)));
                growth.setSyn(isSyn);
                growth.setRecordStatus(c.getString(c.getColumnIndex(GROWTH_RECORD_STATUS)));
                growth.setCreatedBy(c.getString(c.getColumnIndex(GROWTH_CREATE_BY)));
                growth.setUserId(c.getString(c.getColumnIndex(USER_ID)));
                growth.setGrowthDate(c.getString(c.getColumnIndex(GROWTH_DATE)));
                growth.setGrowthTime(c.getString(c.getColumnIndex(GROWTH_TIME)));
                growths.add(growth);

                growths.add(growth);
                // adding to todo list

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return growths;
    }


    public List<Growth> getAllGrowthsOneYear(User user) {
        List<Growth> growths = new ArrayList<Growth>();
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
//        String selectQuery = "SELECT  * FROM " + TABLE_GROWTH_NAME + " WHERE " + USER_ID + " = " + user.getUserId() +
//                " ORDER BY " + GROWTH_DATE + " DESC" + "," + GROWTH_TIME + " DESC";
        String selectQuery = "SELECT * FROM " + TABLE_GROWTH_NAME + " WHERE " + GROWTH_TIME + "=(" + "SELECT" + " Max(" + GROWTH_TIME + ") FROM " + TABLE_GROWTH_NAME + " as f "
                + " WHERE " + " f. " + GROWTH_DATE + " = " + TABLE_GROWTH_NAME + " . " + GROWTH_DATE + " AND " + GROWTH_DATE + " >= '" + user.getUserBirthday() + "'" + " AND " + GROWTH_DATE + " <= '" + getBirthdayNextYear(dateFormatUtility.getDateFormat2(user.getUserBirthday())) + "')" + " AND " + USER_ID + " = " + "'" + user.getUserId() + "'" + " ORDER BY " + GROWTH_DATE + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Boolean isSyn = (c.getInt((c.getColumnIndex(GROWTH_SYN_STATUS))) == 1) ? true : false;
                Growth growth = new Growth();
                growth.setGrowthId(c.getString(c.getColumnIndex(GROWTH_ID)));
                growth.setGrowthUnit(c.getString(c.getColumnIndex(GROWTH_UNIT)));
                growth.setGrowthWeight(c.getDouble(c.getColumnIndex(GROWTH_WEIGHT)));
                growth.setGrowthLength(c.getDouble(c.getColumnIndex(GROWTH_LENGTH)));
                growth.setGrowthHead(c.getDouble(c.getColumnIndex(GROWTH_HEAD)));
                growth.setGrowthNote(c.getString(c.getColumnIndex(GROWTH_NOTE)));
                growth.setSyn(isSyn);
                growth.setRecordStatus(c.getString(c.getColumnIndex(GROWTH_RECORD_STATUS)));
                growth.setCreatedBy(c.getString(c.getColumnIndex(GROWTH_CREATE_BY)));
                growth.setUserId(c.getString(c.getColumnIndex(USER_ID)));
                growth.setGrowthDate(c.getString(c.getColumnIndex(GROWTH_DATE)));
                growth.setGrowthTime(c.getString(c.getColumnIndex(GROWTH_TIME)));
                growths.add(growth);
                // adding to todo list

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return growths;
    }


    public List<Growth> getAllGrowthsTwoYear(User user) {
        List<Growth> growths = new ArrayList<Growth>();
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
//        String selectQuery = "SELECT  * FROM " + TABLE_GROWTH_NAME + " WHERE " + USER_ID + " = " + user.getUserId() +
//                " ORDER BY " + GROWTH_DATE + " DESC" + "," + GROWTH_TIME + " DESC";
        String selectQuery = "SELECT * FROM " + TABLE_GROWTH_NAME + " WHERE " + GROWTH_TIME + "=(" + "SELECT" + " Max(" + GROWTH_TIME + ") FROM " + TABLE_GROWTH_NAME + " as f "
                + " WHERE " + " f. " + GROWTH_DATE + " = " + TABLE_GROWTH_NAME + " . " + GROWTH_DATE + " AND " + GROWTH_DATE + " >= '" + user.getUserBirthday() + "'" + " AND " + GROWTH_DATE + " <= '" + getBirthdayAddTwoYear(dateFormatUtility.getDateFormat2(user.getUserBirthday())) + "')" + " AND " + USER_ID + " = " + "'" + user.getUserId() + "'" + " ORDER BY " + GROWTH_DATE + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Boolean isSyn = (c.getInt((c.getColumnIndex(GROWTH_SYN_STATUS))) == 1) ? true : false;
                Growth growth = new Growth();
                growth.setGrowthId(c.getString(c.getColumnIndex(GROWTH_ID)));
                growth.setGrowthUnit(c.getString(c.getColumnIndex(GROWTH_UNIT)));
                growth.setGrowthWeight(c.getDouble(c.getColumnIndex(GROWTH_WEIGHT)));
                growth.setGrowthLength(c.getDouble(c.getColumnIndex(GROWTH_LENGTH)));
                growth.setGrowthHead(c.getDouble(c.getColumnIndex(GROWTH_HEAD)));
                growth.setGrowthNote(c.getString(c.getColumnIndex(GROWTH_NOTE)));
                growth.setSyn(isSyn);
                growth.setRecordStatus(c.getString(c.getColumnIndex(GROWTH_RECORD_STATUS)));
                growth.setCreatedBy(c.getString(c.getColumnIndex(GROWTH_CREATE_BY)));
                growth.setUserId(c.getString(c.getColumnIndex(USER_ID)));
                growth.setGrowthDate(c.getString(c.getColumnIndex(GROWTH_DATE)));
                growth.setGrowthTime(c.getString(c.getColumnIndex(GROWTH_TIME)));
                growths.add(growth);
                // adding to todo list

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return growths;
    }


    public List<Growth> getAllGrowthsThreeYear(User user) {
        List<Growth> growths = new ArrayList<Growth>();
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
//        String selectQuery = "SELECT  * FROM " + TABLE_GROWTH_NAME + " WHERE " + USER_ID + " = " + user.getUserId() +
//                " ORDER BY " + GROWTH_DATE + " DESC" + "," + GROWTH_TIME + " DESC";
        String selectQuery = "SELECT * FROM " + TABLE_GROWTH_NAME + " WHERE " + GROWTH_TIME + "=(" + "SELECT" + " Max(" + GROWTH_TIME + ") FROM " + TABLE_GROWTH_NAME + " as f "
                + " WHERE " + " f. " + GROWTH_DATE + " = " + TABLE_GROWTH_NAME + " . " + GROWTH_DATE + " AND " + GROWTH_DATE + " >= '" + user.getUserBirthday() + "'" + " AND " + GROWTH_DATE + " <= '" + getBirthdayAddThreeYear(dateFormatUtility.getDateFormat2(user.getUserBirthday())) + "')" + " AND " + USER_ID + " = " + "'" + user.getUserId() + "'" + " ORDER BY " + GROWTH_DATE + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Boolean isSyn = (c.getInt((c.getColumnIndex(GROWTH_SYN_STATUS))) == 1) ? true : false;
                Growth growth = new Growth();
                growth.setGrowthId(c.getString(c.getColumnIndex(GROWTH_ID)));
                growth.setGrowthUnit(c.getString(c.getColumnIndex(GROWTH_UNIT)));
                growth.setGrowthWeight(c.getDouble(c.getColumnIndex(GROWTH_WEIGHT)));
                growth.setGrowthLength(c.getDouble(c.getColumnIndex(GROWTH_LENGTH)));
                growth.setGrowthHead(c.getDouble(c.getColumnIndex(GROWTH_HEAD)));
                growth.setGrowthNote(c.getString(c.getColumnIndex(GROWTH_NOTE)));
                growth.setSyn(isSyn);
                growth.setRecordStatus(c.getString(c.getColumnIndex(GROWTH_RECORD_STATUS)));
                growth.setCreatedBy(c.getString(c.getColumnIndex(GROWTH_CREATE_BY)));
                growth.setUserId(c.getString(c.getColumnIndex(USER_ID)));
                growth.setGrowthDate(c.getString(c.getColumnIndex(GROWTH_DATE)));
                growth.setGrowthTime(c.getString(c.getColumnIndex(GROWTH_TIME)));
                growths.add(growth);
                // adding to todo list

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return growths;
    }

    public List<Growth> getAllGrowthsFourYear(User user) {
        List<Growth> growths = new ArrayList<Growth>();
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
//        String selectQuery = "SELECT  * FROM " + TABLE_GROWTH_NAME + " WHERE " + USER_ID + " = " + user.getUserId() +
//                " ORDER BY " + GROWTH_DATE + " DESC" + "," + GROWTH_TIME + " DESC";
        String selectQuery = "SELECT * FROM " + TABLE_GROWTH_NAME + " WHERE " + GROWTH_TIME + "=(" + "SELECT" + " Max(" + GROWTH_TIME + ") FROM " + TABLE_GROWTH_NAME + " as f "
                + " WHERE " + " f. " + GROWTH_DATE + " = " + TABLE_GROWTH_NAME + " . " + GROWTH_DATE + " AND " + GROWTH_DATE + " >= '" + user.getUserBirthday() + "'" + " AND " + GROWTH_DATE + " <= '" + getBirthdayAddFourYear(dateFormatUtility.getDateFormat2(user.getUserBirthday())) + "')" + " AND " + USER_ID + " = " + "'" + user.getUserId() + "'" + " ORDER BY " + GROWTH_DATE + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Boolean isSyn = (c.getInt((c.getColumnIndex(GROWTH_SYN_STATUS))) == 1) ? true : false;
                Growth growth = new Growth();
                growth.setGrowthId(c.getString(c.getColumnIndex(GROWTH_ID)));
                growth.setGrowthUnit(c.getString(c.getColumnIndex(GROWTH_UNIT)));
                growth.setGrowthWeight(c.getDouble(c.getColumnIndex(GROWTH_WEIGHT)));
                growth.setGrowthLength(c.getDouble(c.getColumnIndex(GROWTH_LENGTH)));
                growth.setGrowthHead(c.getDouble(c.getColumnIndex(GROWTH_HEAD)));
                growth.setGrowthNote(c.getString(c.getColumnIndex(GROWTH_NOTE)));
                growth.setSyn(isSyn);
                growth.setRecordStatus(c.getString(c.getColumnIndex(GROWTH_RECORD_STATUS)));
                growth.setCreatedBy(c.getString(c.getColumnIndex(GROWTH_CREATE_BY)));
                growth.setUserId(c.getString(c.getColumnIndex(USER_ID)));
                growth.setGrowthDate(c.getString(c.getColumnIndex(GROWTH_DATE)));
                growth.setGrowthTime(c.getString(c.getColumnIndex(GROWTH_TIME)));
                growths.add(growth);
                // adding to todo list

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return growths;
    }


    public List<Growth> getAllGrowthsFiveYear(User user) {
        List<Growth> growths = new ArrayList<Growth>();
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
//        String selectQuery = "SELECT  * FROM " + TABLE_GROWTH_NAME + " WHERE " + USER_ID + " = " + user.getUserId() +
//                " ORDER BY " + GROWTH_DATE + " DESC" + "," + GROWTH_TIME + " DESC";
        String selectQuery = "SELECT * FROM " + TABLE_GROWTH_NAME + " WHERE " + GROWTH_TIME + "=(" + "SELECT" + " Max(" + GROWTH_TIME + ") FROM " + TABLE_GROWTH_NAME + " as f "
                + " WHERE " + " f. " + GROWTH_DATE + " = " + TABLE_GROWTH_NAME + " . " + GROWTH_DATE + " AND " + GROWTH_DATE + " >= '" + user.getUserBirthday() + "'" + " AND " + GROWTH_DATE + " <= '" + getBirthdayAddFiveYear(dateFormatUtility.getDateFormat2(user.getUserBirthday())) + "')" + " AND " + USER_ID + " = " + "'" + user.getUserId() + "'" + " ORDER BY " + GROWTH_DATE + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Boolean isSyn = (c.getInt((c.getColumnIndex(GROWTH_SYN_STATUS))) == 1) ? true : false;
                Growth growth = new Growth();
                growth.setGrowthId(c.getString(c.getColumnIndex(GROWTH_ID)));
                growth.setGrowthUnit(c.getString(c.getColumnIndex(GROWTH_UNIT)));
                growth.setGrowthWeight(c.getDouble(c.getColumnIndex(GROWTH_WEIGHT)));
                growth.setGrowthLength(c.getDouble(c.getColumnIndex(GROWTH_LENGTH)));
                growth.setGrowthHead(c.getDouble(c.getColumnIndex(GROWTH_HEAD)));
                growth.setGrowthNote(c.getString(c.getColumnIndex(GROWTH_NOTE)));
                growth.setSyn(isSyn);
                growth.setRecordStatus(c.getString(c.getColumnIndex(GROWTH_RECORD_STATUS)));
                growth.setCreatedBy(c.getString(c.getColumnIndex(GROWTH_CREATE_BY)));
                growth.setUserId(c.getString(c.getColumnIndex(USER_ID)));
                growth.setGrowthDate(c.getString(c.getColumnIndex(GROWTH_DATE)));
                growth.setGrowthTime(c.getString(c.getColumnIndex(GROWTH_TIME)));
                growths.add(growth);
                // adding to todo list

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return growths;

    }


    public String getBirthdayNextYear(Date birthday) {
        String currentTimeTemp = "";
        Date birthdayNextYear;
        Calendar c = Calendar.getInstance();
        c.setTime(birthday);
        c.add(Calendar.YEAR, 1);
        birthdayNextYear = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentTimeTemp = dateFormat.format(birthdayNextYear);
        return currentTimeTemp;

    }

    public String getBirthdayAddTwoYear(Date birthday) {
        String currentTimeTemp = "";
        Date birthdayNextYear;
        Calendar c = Calendar.getInstance();
        c.setTime(birthday);
        c.add(Calendar.YEAR, 2);
        birthdayNextYear = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentTimeTemp = dateFormat.format(birthdayNextYear);
        return currentTimeTemp;
    }

    public String getBirthdayAddThreeYear(Date birthday) {
        String currentTimeTemp = "";
        Date birthdayNextYear;
        Calendar c = Calendar.getInstance();
        c.setTime(birthday);
        c.add(Calendar.YEAR, 3);
        birthdayNextYear = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentTimeTemp = dateFormat.format(birthdayNextYear);
        return currentTimeTemp;
    }

    public String getBirthdayAddFourYear(Date birthday) {
        String currentTimeTemp = "";
        Date birthdayNextYear;
        Calendar c = Calendar.getInstance();
        c.setTime(birthday);
        c.add(Calendar.YEAR, 4);
        birthdayNextYear = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentTimeTemp = dateFormat.format(birthdayNextYear);
        return currentTimeTemp;
    }

    public String getBirthdayAddFiveYear(Date birthday) {
        String currentTimeTemp = "";
        Date birthdayNextYear;
        Calendar c = Calendar.getInstance();
        c.setTime(birthday);
        c.add(Calendar.YEAR, 5);
        birthdayNextYear = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        currentTimeTemp = dateFormat.format(birthdayNextYear);
        return currentTimeTemp;
    }

    public boolean checkIsGrowthIsExist(String growthID) {

        String selectQuery = "SELECT * FROM " + TABLE_GROWTH_NAME + " WHERE " + GROWTH_ID + "='" + growthID + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    //Photo Feature

    public long addPhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PHOTO_ID, photo.getPhotoId());
        values.put(PHOTO_IMAGE, photo.getPhotoImage());
        values.put(PHOTO_SYN_STATUS, photo.getSyn());
        values.put(PHOTO_RECORD_STATUS, photo.getRecordStatus());
        values.put(PHOTO_CREATE_BY, photo.getCreatedBy());
        values.put(GROWTH_ID, photo.getGrowthId());
        values.put(USER_ID, photo.getUserId());
        values.put(PHOTO_CREATE_DATE, photo.getCreatedDatetime());

        long id = db.insert(TABLE_PHOTO_NAME, null, values);
        db.close();
        return id;
    }

    public int updatePhoto(Photo photo) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PHOTO_ID, photo.getPhotoId());
        values.put(PHOTO_IMAGE, photo.getPhotoImage());
        values.put(PHOTO_SYN_STATUS, photo.getSyn());
        values.put(PHOTO_RECORD_STATUS, photo.getRecordStatus());
        values.put(PHOTO_CREATE_BY, photo.getCreatedBy());
        values.put(GROWTH_ID, photo.getGrowthId());
        values.put(USER_ID, photo.getUserId());
        values.put(PHOTO_CREATE_DATE, photo.getCreatedDatetime());
        //db.close();
        // updating row
        return db.update(TABLE_PHOTO_NAME, values, PHOTO_ID + " = ?",
                new String[]{String.valueOf(photo.getPhotoId())});


    }

    public int updatePhotoSyn(Photo photo, Boolean isSyn, String createdBy) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PHOTO_SYN_STATUS, isSyn);
        values.put(PHOTO_CREATE_BY, createdBy);
        //   db.close();
        // updating row
        return db.update(TABLE_PHOTO_NAME, values, PHOTO_ID + " = ?",
                new String[]{String.valueOf(photo.getPhotoId())});
    }


    public List<Photo> getAllPhotoByGrowthId(String growthID) {

        List<Photo> photos = new ArrayList<Photo>();
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTO_NAME + " WHERE " + GROWTH_ID + " = " + "'" + growthID + "'" +
                " ORDER BY " + PHOTO_ID + " ASC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Boolean isSyn = (c.getInt((c.getColumnIndex(PHOTO_SYN_STATUS))) == 1) ? true : false;
                Photo photo = new Photo(c.getString(c.getColumnIndex(PHOTO_ID)),
                        c.getBlob(c.getColumnIndex(PHOTO_IMAGE)),
                        isSyn,
                        c.getString(c.getColumnIndex(PHOTO_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(PHOTO_CREATE_BY)),
                        c.getString(c.getColumnIndex(GROWTH_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        c.getString(c.getColumnIndex(PHOTO_CREATE_DATE)));
                photos.add(photo);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return photos;
    }

    public Photo getPhoto(String photoID) {

        String selectQuery = "SELECT * " + " FROM " + TABLE_PHOTO_NAME + " WHERE " + PHOTO_ID + " = " + "'" + photoID + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Boolean isSyn = (c.getInt((c.getColumnIndex(PHOTO_SYN_STATUS))) == 1) ? true : false;
        Photo photo = new Photo(c.getString(c.getColumnIndex(PHOTO_ID)),
                c.getBlob(c.getColumnIndex(PHOTO_IMAGE)),
                isSyn,
                c.getString(c.getColumnIndex(PHOTO_RECORD_STATUS)),
                c.getString(c.getColumnIndex(PHOTO_CREATE_BY)),
                c.getString(c.getColumnIndex(GROWTH_ID)),
                c.getString(c.getColumnIndex(USER_ID)),
                c.getString(c.getColumnIndex(PHOTO_CREATE_DATE)));

        c.close();
        db.close();
        return photo;

    }


    public List<Photo> getAllPhotoByUserNotSyn(String userID) {

        List<Photo> photos = new ArrayList<Photo>();
        String selectQuery = "SELECT  * FROM " + TABLE_PHOTO_NAME + " WHERE " + GROWTH_ID + " = " + "'" + userID + "'" + " AND " + PHOTO_SYN_STATUS + "=0" +
                " ORDER BY " + PHOTO_ID + " ASC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Boolean isSyn = (c.getInt((c.getColumnIndex(PHOTO_SYN_STATUS))) == 1) ? true : false;
                Photo photo = new Photo(c.getString(c.getColumnIndex(PHOTO_ID)),
                        c.getBlob(c.getColumnIndex(PHOTO_IMAGE)),
                        isSyn,
                        c.getString(c.getColumnIndex(PHOTO_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(PHOTO_CREATE_BY)),
                        c.getString(c.getColumnIndex(GROWTH_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        c.getString(c.getColumnIndex(PHOTO_CREATE_DATE)));
                photos.add(photo);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return photos;
    }


    public void deletePhoto(Growth growth) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTO_NAME, GROWTH_ID + " = ?",
                new String[]{String.valueOf(growth.getGrowthId())});
        db.close();
    }


    public void deleteAllPhotoByUser(User User) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTO_NAME, USER_ID + " = ?",
                new String[]{String.valueOf(User.getUserId())});
        db.close();
    }


    public void deletePhotoID(Photo Photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_PHOTO_NAME, PHOTO_ID + " = ?",
                new String[]{String.valueOf(Photo.getPhotoId())});
        db.close();
    }


    public int getPhotoByGrowthIDCount(String growthID) {
        // Log.i(DATABASE_TAG, "MyDatabaseHelper.getUsersCount ... ");

        String countQuery = "SELECT  * FROM " + TABLE_PHOTO_NAME + " WHERE " + GROWTH_ID + "=" + "'" + growthID + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        // return count
        return count;
    }

    public boolean checkIsPhotoIsExist(String photoID) {

        String selectQuery = "SELECT * FROM " + TABLE_PHOTO_NAME + " WHERE " + PHOTO_ID + "='" + photoID + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    //CRUD Activities Table
    public void addActivities(Activities activities) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ACTIVITIES_FIXED_ID, activities.getFixedID());
        values.put(ACTIVITIES_IS_SHOW, activities.getShow());
        values.put(ACTIVITIES_RANK, activities.getRank());
        values.put(ACTIVITIES_IS_NOTIFY, activities.getNotify());
        values.put(ACTIVITIES_NOTIFY_OPTION, activities.getNotifyOption());
        values.put(ACTIVITIES_NOTIFY_DATE, (activities.getNotifyDate() == null) ? null : dateFormatUtility.getStringDateFormat2(activities.getNotifyDate()));
        values.put(ACTIVITIES_NOTIFY_TIME, (activities.getNotifyTime() == null) ? null : dateFormatUtility.getStringTimeFormat(activities.getNotifyTime()));
        values.put(ACTIVITIES_NOTIFY_REPEAT_IN_DAY, activities.getNotifyRepeatInDay());
        values.put(ACTIVITIES_NOTIFY_REPEAT_IN_TIME, activities.getNotifyRepeatInTime());
        values.put(CREATED_DATETIME, (activities.getCreatedDatetime() == null) ? null : dateFormatUtility.getStringDateFullFormat(activities.getCreatedDatetime()));
        values.put(USER_ID, activities.getUserId());
        db.insert(TABLE_ACTIVITIES_NAME, null, values);
        db.close();
    }


    public int updateActivities(Activities activities) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACTIVITIES_FIXED_ID, activities.getFixedID());
        values.put(ACTIVITIES_IS_SHOW, activities.getShow());
        values.put(ACTIVITIES_RANK, activities.getRank());
        values.put(ACTIVITIES_IS_NOTIFY, activities.getNotify());
        values.put(ACTIVITIES_NOTIFY_OPTION, activities.getNotifyOption());
        values.put(ACTIVITIES_NOTIFY_DATE, (activities.getNotifyDate() == null) ? null : dateFormatUtility.getStringDateFormat2(activities.getNotifyDate()));
        values.put(ACTIVITIES_NOTIFY_TIME, (activities.getNotifyTime() == null) ? null : dateFormatUtility.getStringTimeFormat(activities.getNotifyTime()));
        values.put(ACTIVITIES_NOTIFY_REPEAT_IN_DAY, activities.getNotifyRepeatInDay());
        values.put(ACTIVITIES_NOTIFY_REPEAT_IN_TIME, activities.getNotifyRepeatInTime());
        values.put(CREATED_DATETIME, (activities.getCreatedDatetime() == null) ? null : dateFormatUtility.getStringDateFullFormat(activities.getCreatedDatetime()));
        values.put(USER_ID, activities.getUserId());
        db.insert(TABLE_ACTIVITIES_NAME, null, values);
//db.close();
        // updating row
        return db.update(TABLE_ACTIVITIES_NAME, values, ACTIVITIES_ID + " = ?",
                new String[]{String.valueOf(activities.getActivitiesId())});
    }


    public int updateNotificationActivities(Activities activities, String notifyOption, Date notifyDate, Date notifyTime, String repeatInDay, String repeatInTime, Date createdDateTime) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACTIVITIES_NOTIFY_OPTION, notifyOption);
        values.put(ACTIVITIES_NOTIFY_DATE, dateFormatUtility.getStringDateFormat2(notifyDate));
        values.put(ACTIVITIES_NOTIFY_TIME, dateFormatUtility.getStringTimeFormat(notifyTime));
        values.put(ACTIVITIES_NOTIFY_REPEAT_IN_DAY, repeatInDay);
        values.put(ACTIVITIES_NOTIFY_REPEAT_IN_TIME, repeatInTime);
        values.put(CREATED_DATETIME, dateFormatUtility.getStringDateFullFormat(createdDateTime));
//db.close();
        // updating row
        return db.update(TABLE_ACTIVITIES_NAME, values, ACTIVITIES_ID + " = ?",
                new String[]{String.valueOf(activities.getActivitiesId())});
    }

    public int updateNotificationOptionActivities(Activities activities, String notifyOption, Date createdDateTime) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACTIVITIES_NOTIFY_OPTION, notifyOption);
        values.put(CREATED_DATETIME, dateFormatUtility.getStringDateFullFormat(createdDateTime));
        // db.close();
        // updating row
        return db.update(TABLE_ACTIVITIES_NAME, values, ACTIVITIES_ID + " = ?",
                new String[]{String.valueOf(activities.getActivitiesId())});
    }

    public int updateIsShowActivities(Activities activities, Boolean isShow, Date createdDateTime) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACTIVITIES_IS_SHOW, isShow);
        values.put(CREATED_DATETIME, dateFormatUtility.getStringDateFullFormat(createdDateTime));
        //  db.close();
        // updating row
        return db.update(TABLE_ACTIVITIES_NAME, values, ACTIVITIES_ID + " = ?",
                new String[]{String.valueOf(activities.getActivitiesId())});
    }

    public int updateIsNotifyActivities(Activities activities, Boolean isNotify, Date createdDateTime) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACTIVITIES_IS_NOTIFY, isNotify);
        values.put(CREATED_DATETIME, dateFormatUtility.getStringDateFullFormat(createdDateTime));
        // db.close();
        // updating row
        return db.update(TABLE_ACTIVITIES_NAME, values, ACTIVITIES_ID + " = ?",
                new String[]{String.valueOf(activities.getActivitiesId())});
    }


    public int updateRankActivities(Activities activities, int rank, Date createdDateTime) {

        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(ACTIVITIES_RANK, rank);
        values.put(CREATED_DATETIME, dateFormatUtility.getStringDateFullFormat(createdDateTime));

        // db.close();
        // updating row
        return db.update(TABLE_ACTIVITIES_NAME, values, ACTIVITIES_ID + " = ?",
                new String[]{String.valueOf(activities.getActivitiesId())});
    }

    public int getActivitiesCount(String userID) {

        String countQuery = "SELECT  * FROM " + TABLE_ACTIVITIES_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();
        // Log.i(DATABASE_TAG, "DatabaseHelper.getActivitiesCount for CurrentUSer ... " + userID + " count" + count);
        // return count
        return count;
    }

    public List<Activities> getAllActivitiesCount(String userID) {
        List<Activities> activities = new ArrayList<Activities>();

        DateFormatUtility dateFormat = new DateFormatUtility(mContext);

        String selectQuery = " SELECT *," + "COUNT(" + ACTIVITIES_ID + ")" + " AS Total " + " FROM " + TABLE_RECORD_NAME
                + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " GROUP BY " + ACTIVITIES_ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Activities activity = new Activities();
                activity.setFixedID(c.getInt(c.getColumnIndex(ACTIVITIES_ID)));
                activity.setUserId(c.getString(c.getColumnIndex(USER_ID)));
                activity.setTotalActivities(c.getInt(c.getColumnIndex("Total")));
                activities.add(activity);
                // adding to todo list

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return activities;
    }


    public void deleteActivities(Activities activities) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACTIVITIES_NAME, ACTIVITIES_ID + " = ?",
                new String[]{String.valueOf(activities.getActivitiesId())});
        db.close();
    }


    //
    public void deleteActivitiesId(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACTIVITIES_NAME, ACTIVITIES_ID + " = ?",
                new String[]{id});
        db.close();
    }

    public void deleteAllActivitiesByUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ACTIVITIES_NAME, USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
    }


    public List<Activities> getAllActivities(String userID) {
        List<Activities> activities = new ArrayList<Activities>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACTIVITIES_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" +
                " ORDER BY " + ACTIVITIES_RANK + " ASC";
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Activities activity = new Activities();
                activity.setActivitiesId(c.getInt(c.getColumnIndex(ACTIVITIES_ID)));
                activity.setFixedID(c.getInt(c.getColumnIndex(ACTIVITIES_FIXED_ID)));
                Boolean isShow = (c.getInt((c.getColumnIndex(ACTIVITIES_IS_SHOW))) == 1) ? true : false;
                Boolean isNotify = (c.getInt((c.getColumnIndex(ACTIVITIES_IS_NOTIFY))) == 1) ? true : false;
                activity.setShow(isShow);
                activity.setRank(c.getInt(c.getColumnIndex(ACTIVITIES_RANK)));
                activity.setNotify(isNotify);
                activity.setNotifyOption(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_OPTION)));
                activity.setNotifyDate((c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_DATE)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_DATE))));
                activity.setNotifyTime((c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_TIME)) == null) ? null : dateFormat.getTimeFormat(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_TIME))));
                activity.setNotifyRepeatInDay(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_REPEAT_IN_DAY)));
                activity.setNotifyRepeatInTime(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_REPEAT_IN_TIME)));
                activity.setCreatedDatetime((c.getString(c.getColumnIndex(CREATED_DATETIME)) == null) ? null : dateFormat.getDateFullFormat(c.getString(c.getColumnIndex(CREATED_DATETIME))));
                activities.add(activity);
                // adding to todo list

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return activities;
    }

    public List<Activities> getAllActivitiesToShow(String userID) {
        List<Activities> activities = new ArrayList<Activities>();
        String selectQuery = "SELECT  * FROM " + TABLE_ACTIVITIES_NAME + " WHERE " + ACTIVITIES_IS_SHOW + "=1" + " AND " + USER_ID + " = " + "'" + userID + "'" +
                " ORDER BY " + ACTIVITIES_RANK + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Activities activity = new Activities();
                activity.setActivitiesId(c.getInt(c.getColumnIndex(ACTIVITIES_ID)));
                activity.setFixedID(c.getInt(c.getColumnIndex(ACTIVITIES_FIXED_ID)));
                Boolean isShow = (c.getInt((c.getColumnIndex(ACTIVITIES_IS_SHOW))) == 1) ? true : false;
                Boolean isNotify = (c.getInt((c.getColumnIndex(ACTIVITIES_IS_NOTIFY))) == 1) ? true : false;
                activity.setShow(isShow);
                activity.setRank(c.getInt(c.getColumnIndex(ACTIVITIES_RANK)));
                activity.setNotify(isNotify);
                activity.setNotifyOption(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_OPTION)));
                activity.setNotifyDate((c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_DATE)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_DATE))));
                activity.setNotifyTime((c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_TIME)) == null) ? null : dateFormat.getTimeFormat(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_TIME))));
                activity.setNotifyRepeatInDay(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_REPEAT_IN_DAY)));
                activity.setNotifyRepeatInTime(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_REPEAT_IN_TIME)));
                activity.setCreatedDatetime((c.getString(c.getColumnIndex(CREATED_DATETIME)) == null) ? null : dateFormat.getDateFullFormat(c.getString(c.getColumnIndex(CREATED_DATETIME))));
                activities.add(activity);
                // adding to todo list

            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return activities;
    }


    public Activities getActivities(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        Cursor c = db.query(TABLE_ACTIVITIES_NAME,
                new String[]{ACTIVITIES_ID, ACTIVITIES_FIXED_ID, ACTIVITIES_IS_SHOW, ACTIVITIES_IS_NOTIFY, ACTIVITIES_RANK, ACTIVITIES_NOTIFY_OPTION, ACTIVITIES_NOTIFY_DATE, ACTIVITIES_NOTIFY_TIME, ACTIVITIES_NOTIFY_REPEAT_IN_DAY, ACTIVITIES_NOTIFY_REPEAT_IN_TIME, CREATED_DATETIME},
                ACTIVITIES_FIXED_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        //rc.setRecordWeightUnit(c.getString(c.getColumnIndex(RECORD_WEIGHT_UNIT)));
        if (c != null)
            c.moveToFirst();
        // Boolean isStart = (cursor.getInt((cursor.getColumnIndex(RECORD_IS_START))) == 1) ? true : false;
        // prepare note object

        Activities activity = new Activities();
        activity.setActivitiesId(c.getInt(c.getColumnIndex(ACTIVITIES_ID)));
        activity.setFixedID(c.getInt(c.getColumnIndex(ACTIVITIES_FIXED_ID)));
        Boolean isShow = (c.getInt((c.getColumnIndex(ACTIVITIES_IS_SHOW))) == 1) ? true : false;
        Boolean isNotify = (c.getInt((c.getColumnIndex(ACTIVITIES_IS_NOTIFY))) == 1) ? true : false;
        activity.setShow(isShow);
        activity.setRank(c.getInt(c.getColumnIndex(ACTIVITIES_RANK)));
        activity.setNotify(isNotify);
        activity.setNotifyOption(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_OPTION)));
        activity.setNotifyDate((c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_DATE)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_DATE))));
        activity.setNotifyTime((c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_TIME)) == null) ? null : dateFormat.getTimeFormat(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_TIME))));
        activity.setNotifyRepeatInDay(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_REPEAT_IN_DAY)));
        activity.setNotifyRepeatInTime(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_REPEAT_IN_TIME)));
        activity.setCreatedDatetime((c.getString(c.getColumnIndex(CREATED_DATETIME)) == null) ? null : dateFormat.getDateFullFormat(c.getString(c.getColumnIndex(CREATED_DATETIME))));
        // close the db connection
        c.close();
        db.close();

        return activity;
    }

    public Activities getActivities(long id, int userID) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);


        String selectQuery = "SELECT  * FROM " + TABLE_ACTIVITIES_NAME + " WHERE " + ACTIVITIES_FIXED_ID + "=" + id + " AND " + USER_ID + " = " + "'" + userID + "'";

        Cursor c = db.rawQuery(selectQuery, null);

        //rc.setRecordWeightUnit(c.getString(c.getColumnIndex(RECORD_WEIGHT_UNIT)));
        if (c != null)
            c.moveToFirst();
        // Boolean isStart = (cursor.getInt((cursor.getColumnIndex(RECORD_IS_START))) == 1) ? true : false;
        // prepare note object

        Activities activity = new Activities();
        activity.setActivitiesId(c.getInt(c.getColumnIndex(ACTIVITIES_ID)));
        activity.setFixedID(c.getInt(c.getColumnIndex(ACTIVITIES_FIXED_ID)));
        Boolean isShow = (c.getInt((c.getColumnIndex(ACTIVITIES_IS_SHOW))) == 1) ? true : false;
        Boolean isNotify = (c.getInt((c.getColumnIndex(ACTIVITIES_IS_NOTIFY))) == 1) ? true : false;
        activity.setShow(isShow);
        activity.setRank(c.getInt(c.getColumnIndex(ACTIVITIES_RANK)));
        activity.setNotify(isNotify);
        activity.setNotifyOption(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_OPTION)));
        activity.setNotifyDate((c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_DATE)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_DATE))));
        activity.setNotifyTime((c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_TIME)) == null) ? null : dateFormat.getTimeFormat(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_TIME))));
        activity.setNotifyRepeatInDay(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_REPEAT_IN_DAY)));
        activity.setNotifyRepeatInTime(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_REPEAT_IN_TIME)));
        activity.setCreatedDatetime((c.getString(c.getColumnIndex(CREATED_DATETIME)) == null) ? null : dateFormat.getDateFullFormat(c.getString(c.getColumnIndex(CREATED_DATETIME))));
        // close the db connection
        c.close();
        db.close();

        return activity;
    }


//    public List<Activities> getAllActivitiesWithDetailToShow() {
//        List<Activities> activities = new ArrayList<Activities>();
//
//        String selectQuery = "SELECT  * FROM " + TABLE_ACTIVITIES_NAME + " LEFT JOIN(SELECT *, MAX(" + RECORD_DATE_END + " ),MAX( " + RECORD_TIME_END + " )FROM " + TABLE_RECORD_NAME + " GROUP BY " + ACTIVITIES_ID + ")" + TABLE_RECORD_NAME + " ON " + TABLE_ACTIVITIES_NAME + "." + ACTIVITIES_ID + "=" + TABLE_RECORD_NAME + "."
//                + ACTIVITIES_ID + " WHERE " + TABLE_ACTIVITIES_NAME + "." + ACTIVITIES_IS_SHOW + "=1" + " ORDER BY " + ACTIVITIES_RANK + " ASC";
//
//
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor c = db.rawQuery(selectQuery, null);
//        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
//        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
//        // looping through all rows and adding to list
//        if (c.moveToFirst()) {
//            do {
//                Activities activity = new Activities();
//                activity.setActivitiesId(c.getInt(c.getColumnIndex(ACTIVITIES_ID)));
//                Boolean isShow = (c.getInt((c.getColumnIndex(ACTIVITIES_IS_SHOW))) == 1) ? true : false;
//                Boolean isNotify = (c.getInt((c.getColumnIndex(ACTIVITIES_IS_NOTIFY))) == 1) ? true : false;
//                String duration;
//                activity.setActivitiesName(c.getString(c.getColumnIndex(ACTIVITIES_NAME)));
//                activity.setActivitiesIcon(c.getInt(c.getColumnIndex(ACTIVITIES_ICON)));
//                activity.setShow(isShow);
//                activity.setRank(c.getInt(c.getColumnIndex(ACTIVITIES_RANK)));
//                activity.setNotify(isNotify);
//                activity.setNotifyTime(c.getString(c.getColumnIndex(ACTIVITIES_NOTIFY_TIME)));
//                if (!c.getString(c.getColumnIndex(RECORD_DURATION)).equals("")) {
//                    activity.setDuration(c.getString(c.getColumnIndex(RECORD_DURATION)));
//                } else {
//                    activity.setDuration("");
//                }
//
//                if (!c.getString(c.getColumnIndex(RECORD_TIME_END)).equals("")) {
//                    activity.setLastRecordTime(c.getString(c.getColumnIndex(RECORD_TIME_END)));
//                } else {
//                    activity.setLastRecordTime("");
//                }
//                activities.add(activity);
//                // adding to todo list
//                Log.d(DATABASE_TAG, "Duration: " + c.getString(c.getColumnIndex(RECORD_DURATION)));
//            } while (c.moveToNext());
//        }
//
//        return activities;
//    }


    //CRUD Record Table

    public long addRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        ContentValues values = new ContentValues();
        values.put(RECORD_ID, record.getRecordId());
        values.put(RECORD_OPTION, record.getOption());
        values.put(RECORD_AMOUNT, record.getAmount());
        values.put(RECORD_AMOUNT_UNIT, record.getAmountUnit());
        String dateStart = (record.getDateStart() == null) ? null : dateFormatUtility.getStringDateFormat2(record.getDateStart());
        String timeStart = (record.getTimeStart() == null) ? null : dateFormatUtility.getStringTimeFormat2(record.getTimeStart());
        String duration = (record.getDuration() == null) ? null : dateFormatUtility.getStringTimeFormat2(record.getDuration()); //Orriginal
        values.put(RECORD_DATE_START, dateStart);
        values.put(RECORD_TIME_START, timeStart);
        values.put(RECORD_DATE_END, dateFormatUtility.getStringDateFormat2(record.getDateEnd()));
        values.put(RECORD_TIME_END, dateFormatUtility.getStringTimeFormat2(record.getTimeEnd()));
        values.put(RECORD_DURATION, duration);
        values.put(RECORD_NOTE, record.getNote());
        values.put(ACTIVITIES_ID, record.getActivitiesId());
        values.put(USER_ID, record.getUserId());
        values.put(RECORD_SYN_STATUS, record.getSyn());
        values.put(RECORD_RECORD_STATUS, record.getRecordStatus());
        values.put(RECORD_CREATE_BY, record.getCreatedBy());
        values.put(RECORD_CREATE_DATE, record.getRecordCreatedDatetime());
        long id = db.insert(TABLE_RECORD_NAME, null, values);
        db.close();
        return id;
    }



    public int updateRecord(Record record) {
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RECORD_OPTION, record.getOption());
        values.put(RECORD_AMOUNT, record.getAmount());
        values.put(RECORD_AMOUNT_UNIT, record.getAmountUnit());
        String dateStart = (record.getDateStart() == null) ? null : dateFormatUtility.getStringDateFormat2(record.getDateStart());
        String timeStart = (record.getTimeStart() == null) ? null : dateFormatUtility.getStringTimeFormat2(record.getTimeStart());
        String duration = (record.getDuration() == null) ? null : dateFormatUtility.getStringTimeFormat2(record.getDuration());
        values.put(RECORD_DATE_START, dateStart);
        values.put(RECORD_TIME_START, timeStart);
        values.put(RECORD_DATE_END, dateFormatUtility.getStringDateFormat2(record.getDateEnd()));
        values.put(RECORD_TIME_END, dateFormatUtility.getStringTimeFormat2(record.getTimeEnd()));
        values.put(RECORD_DURATION, duration);
        values.put(RECORD_NOTE, record.getNote());
        values.put(ACTIVITIES_ID, record.getActivitiesId());
        values.put(USER_ID, record.getUserId());
        values.put(RECORD_SYN_STATUS, record.getSyn());
        values.put(RECORD_RECORD_STATUS, record.getRecordStatus());
        values.put(RECORD_CREATE_BY, record.getCreatedBy());
        values.put(RECORD_CREATE_DATE, record.getRecordCreatedDatetime());

        // db.close();
        // updating row
        return db.update(TABLE_RECORD_NAME, values, RECORD_ID + " = ?",
                new String[]{String.valueOf(record.getRecordId())});
    }


    public List<Record> getAllRecords(int userID) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" +
                " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
                Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        isSyn,
                        c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_DATE))
                );
                records.add(record);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return records;
    }

    public List<Record> getAllRecordsNotSyn(String userID) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + RECORD_SYN_STATUS + "=0" +
                " ORDER BY " + RECORD_ID + " ASC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
                Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        isSyn,
                        c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_DATE))
                );
                records.add(record);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return records;
    }


    public Record getLastRecord(int activityID, String userID) {

        //String selectQuery = "SELECT *, MAX(" + RECORD_DATE_END + "),MAX(" + RECORD_TIME_END + ")" + ",MAX(" + RECORD_ID + ")" + " FROM " + TABLE_RECORD_NAME + " WHERE " + ACTIVITIES_ID + " = " + activityID + " AND " + USER_ID + " = " + "'" + userID + "'" +
               // " ORDER BY " + ACTIVITIES_ID;

        String selectQuery = "SELECT *, MAX(" + RECORD_DATE_END + "),MAX(" + RECORD_TIME_END + "),MAX(" +RECORD_CREATE_DATE+ ")" + " FROM " + TABLE_RECORD_NAME + " WHERE " + ACTIVITIES_ID + " = " + activityID + " AND " + USER_ID + " = " + "'" + userID + "'" +
                " ORDER BY " + ACTIVITIES_ID; // Tuan changed
        SQLiteDatabase db = this.getReadableDatabase();
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormatUtility.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
        Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormatUtility.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
        Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormatUtility.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
        Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
        Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                c.getString(c.getColumnIndex(RECORD_OPTION)),
                c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                dateStart,
                timeStart,
                dateFormatUtility.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                dateFormatUtility.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                duration,
                c.getString(c.getColumnIndex(RECORD_NOTE)),
                c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                c.getString(c.getColumnIndex(USER_ID)),
                isSyn,
                c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                c.getString(c.getColumnIndex(RECORD_CREATE_DATE))
        );

        c.close();
        db.close();
        return record;

    }

    public Record getRecord(String id) {

        String selectQuery = "SELECT *, MAX(" + RECORD_DATE_END + "),MAX(" + RECORD_TIME_END + ")" + ",MAX(" + RECORD_ID + ")" + " FROM " + TABLE_RECORD_NAME + " WHERE " + RECORD_ID + " = " + "'" + id + "'" +
                " ORDER BY " + ACTIVITIES_ID;
        SQLiteDatabase db = this.getReadableDatabase();
        DateFormatUtility dateFormatUtility = new DateFormatUtility(mContext);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null)
            c.moveToFirst();
        Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormatUtility.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
        Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormatUtility.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
        Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormatUtility.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
        Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
        Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                c.getString(c.getColumnIndex(RECORD_OPTION)),
                c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                dateStart,
                timeStart,
                dateFormatUtility.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                dateFormatUtility.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                duration,
                c.getString(c.getColumnIndex(RECORD_NOTE)),
                c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                c.getString(c.getColumnIndex(USER_ID)),
                isSyn,
                c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                c.getString(c.getColumnIndex(RECORD_CREATE_DATE))
        );

        c.close();
        db.close();
        return record;

    }


    public int getRecordCount(String userID) {

        String countQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();
        // Log.i(DATABASE_TAG, "DatabaseHelper.getActivitiesCount for CurrentUSer ... " + userID + " count" + count);
        // return count
        return count;
    }

    public int getRecordByCatCount(int activityID, String userID) {
        //  Log.i(DATABASE_TAG, "DatabaseHelper.getActivitiesCount ... ");

        String countQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + ACTIVITIES_ID + " = " + activityID + " AND " + USER_ID + " = " + "'" + userID + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();
        //  Log.i(DATABASE_TAG, "DatabaseHelper.getActivitiesCount ... " + count);
        // return count
        return count;
    }

    public int getRecordByDateRangeCount(String userID, int activityID, String endDate1, String endDate2) {
        //  Log.i(DATABASE_TAG, "DatabaseHelper.getActivitiesCount ... ");

        String selectQuery = "";
        if (activityID == 0) {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        }

        // String countQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + ACTIVITIES_ID + " = " + activityID + " AND " + USER_ID + " = " + userID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();
        //  Log.i(DATABASE_TAG, "DatabaseHelper.getActivitiesCount ... " + count);
        // return count
        return count;
    }

    public int getRecordByDateRangeCountMulti(String userID, List<Integer> activityIDList, String endDate1, String endDate2) {
// Log.i(DATABASE_TAG, "DatabaseHelper.getActivitiesCount ... ");

        String selectQuery = "";

        String activityID = "";
        for (int i = 0; i < activityIDList.size(); i++) {
            if (i == activityIDList.size() - 1) {
                activityID = activityID + "'" + activityIDList.get(i) + "'";
            } else {
                activityID = activityID + "'" + activityIDList.get(i) + "'" + ",";
            }

        }

        if (activityID.equals("")) {
            selectQuery = "SELECT * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        } else {
            selectQuery = "SELECT * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + " IN (" + activityID + ")" +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        }

// String countQuery = "SELECT * FROM " + TABLE_RECORD_NAME + " WHERE " + ACTIVITIES_ID + " = " + activityID + " AND " + USER_ID + " = " + userID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();
// Log.i(DATABASE_TAG, "DatabaseHelper.getActivitiesCount ... " + count);
// return count
        return count;
    }


    public List<Record> getTotalRecordByDateRange(String userID, String endDate1, String endDate2) {
        //  Log.i(DATABASE_TAG, "DatabaseHelper.getActivitiesCount ... ");
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "";

        selectQuery = "SELECT  *, COUNT(*) AS TOTAL FROM  " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" +
                " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                " GROUP BY " + ACTIVITIES_ID;

        SQLiteDatabase db = this.getReadableDatabase();


        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Record record = new Record(c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        c.getInt(c.getColumnIndex("TOTAL")));
                records.add(record);
                // adding to todo list

            } while (c.moveToNext());
        }

        //  Log.i(DATABASE_TAG, "DatabaseHelper.getActivitiesCount ... " + count);
        // return count
        return records;
    }


    public void deleteRecord(Record record) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORD_NAME, RECORD_ID + " = ?",
                new String[]{String.valueOf(record.getRecordId())});
        db.close();
    }

    public void deleteRecordByID(String recordID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORD_NAME, RECORD_ID + " = ?",
                new String[]{recordID});
        db.close();
    }

    public void deleteAllRecordByUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORD_NAME, USER_ID + " = ?",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
    }

    public Boolean deleteAllRecordByUserAlreadySyned(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_RECORD_NAME, USER_ID + " = ?" + " AND " + RECORD_SYN_STATUS + "=1",
                new String[]{String.valueOf(user.getUserId())});
        db.close();
        return true;
    }

//    public Boolean deleteAllRecordByUserAlreadySynedNotCreateByCurrentSignin(User user, String googleID) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE_RECORD_NAME, USER_ID + " = ?" + " AND " + RECORD_SYN_STATUS + "=1" + " AND " + RECORD_CREATE_BY + "!='" + googleID+"'",
//                new String[]{String.valueOf(user.getUserId())});
//        db.close();
//        return true;
//    }


    public int updateRecordSyn(Record record, Boolean isSyn, String createdBy) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(RECORD_SYN_STATUS, isSyn);
        values.put(RECORD_CREATE_BY, createdBy);
        //   db.close();
        // updating row
        return db.update(TABLE_RECORD_NAME, values, RECORD_ID + " = ?",
                new String[]{String.valueOf(record.getRecordId())});
    }


    // This is begin of stats page


    public List<Stats> getAllStartsByTimes(String userID, int activityID, String endDate1, String endDate2) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Stats> stats = new ArrayList<Stats>();
//
//        SELECT Record_option, COUNT(*) from Record where activities_id=1 AND user_id=1 AND Record_date_end >= '2020-02-20 ' AND Record_date_end <= '2020-12-29'
//        GROUP BY Record_option
        String selectQuery = " SELECT " + RECORD_OPTION + ", COUNT(*) AS TIMES" + ", SUM(" + RECORD_AMOUNT + ") AS AMOUNT" + " FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                " GROUP BY " + RECORD_OPTION + " ORDER BY " + RECORD_OPTION + " DESC ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                Stats stat = new Stats(c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getInt(c.getColumnIndex("TIMES")),
                        c.getDouble(c.getColumnIndex("AMOUNT")));
                stats.add(stat);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return stats;
    }

    public List<Stats> getAllStartsByTimesMultiOp(String userID, int activityID, String endDate1, String endDate2, List<String> optionList) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Stats> stats = new ArrayList<Stats>();

        String op = "";
        for (int i = 0; i < optionList.size(); i++) {
            if (i == optionList.size() - 1) {
                op = op + "'" + optionList.get(i) + "'";
            } else {
                op = op + "'" + optionList.get(i) + "'" + ",";
            }

        }

        String selectQuery = "";

        if (op.equals("")) {
            selectQuery = " SELECT *, COUNT(*) AS TIMES" + ", SUM(" + RECORD_AMOUNT + ") AS AMOUNT" + " FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'";
        } else {
            selectQuery = " SELECT *, COUNT(*) AS TIMES" + ", SUM(" + RECORD_AMOUNT + ") AS AMOUNT" + " FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" + " AND " + RECORD_OPTION + " IN (" + op + ")";
        }


//        SELECT Record_option, COUNT(*) from Record where activities_id=1 AND user_id=1 AND Record_date_end >= '2020-02-20 ' AND Record_date_end <= '2020-12-29'
//        GROUP BY Record_option

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {

                Stats stat = new Stats(c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getInt(c.getColumnIndex("TIMES")),
                        c.getDouble(c.getColumnIndex("AMOUNT")));
                stats.add(stat);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return stats;
    }


    public List<Record> getAllRecordsByDayRange(String userID, int activityID, String endDate1, String endDate2) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "";
        if (activityID == 0) {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
                Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        isSyn,
                        c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_DATE))

                );
                records.add(record);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return records;
    }


    public List<Record> getAllRecordsByDayRangeMulti(String userID, List<Integer> activityIDList, String endDate1, String endDate2) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "";

        String activityID = "";
        for (int i = 0; i < activityIDList.size(); i++) {
            if (i == activityIDList.size() - 1) {
                activityID = activityID + "'" + activityIDList.get(i) + "'";
            } else {
                activityID = activityID + "'" + activityIDList.get(i) + "'" + ",";
            }

        }


        if (activityID.equals("")) {
            selectQuery = "SELECT * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        } else {
            selectQuery = "SELECT * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + " IN (" + activityID + ")" +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
// DateFormatUtility dateFormat = new DateFormatUtility(mContext);
// looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
                Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        isSyn,
                        c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_DATE))

                );
                records.add(record);
// adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return records;
    }


    public List<Record> getAllRecordsByOption(String userID, int activityID, String endDate1, String endDate2, String option) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "";

        if (option.equals("")) {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" + " AND " + RECORD_OPTION + "=\'" + option + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
                Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        isSyn,
                        c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_DATE))

                );
                records.add(record);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return records;
    }


    public List<Record> getAllRecordsByMultiOption(String userID, int activityID, String endDate1, String endDate2, List<String> optionList) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Record> records = new ArrayList<Record>();
        String op = "";
        for (int i = 0; i < optionList.size(); i++) {
            if (i == optionList.size() - 1) {
                op = op + "'" + optionList.get(i) + "'";
            } else {
                op = op + "'" + optionList.get(i) + "'" + ",";
            }

        }

        String selectQuery = "";

        if (op.equals("")) {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" + " AND " + RECORD_OPTION + " IN (" + op + ")" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
                Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        isSyn,
                        c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_DATE))

                );
                records.add(record);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return records;
    }


    public List<Record> getAllRecordsByDay(String userID, int activityID, String endDate, String option) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "";
        if (option.equals("")) {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + "='" + endDate + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + "='" + endDate + "'" + " AND " + RECORD_OPTION + "=\'" + option + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
                Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        isSyn,
                        c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_DATE))

                );
                records.add(record);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return records;
    }


    public List<Record> getAllRecordsByDayMultiOp(String userID, int activityID, String endDate, List<String> optionList) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Record> records = new ArrayList<Record>();
        String op = "";
        for (int i = 0; i < optionList.size(); i++) {
            if (i == optionList.size() - 1) {
                op = op + "'" + optionList.get(i) + "'";
            } else {
                op = op + "'" + optionList.get(i) + "'" + ",";
            }

        }

        String selectQuery = "";


        if (op.equals("")) {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + "='" + endDate + "'" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        } else {
            selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                    " AND " + RECORD_DATE_END + "='" + endDate + "'" + " AND " + RECORD_OPTION + " IN (" + op + ")" +
                    " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
                Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        isSyn,
                        c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_DATE))

                );
                records.add(record);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return records;
    }


    public List<Record> getAllRecordsByDay(String userID, int activityID, String endDate) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                " AND " + RECORD_DATE_END + "='" + endDate + "'" +
                " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
                Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        isSyn,
                        c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_DATE))

                );
                records.add(record);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return records;
    }

    public boolean checkIsRecordIsExist(String recordID) {

        String selectQuery = "SELECT * FROM " + TABLE_RECORD_NAME + " WHERE " + RECORD_ID + "='" + recordID + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }


    //New Algorithm


    public List<Timeline> getAllTimeline(String userID, int activityID, String endDate1, String endDate2) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        FormHelper formHelper = new FormHelper(mContext);
        List<Timeline> timelines = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                " AND " + RECORD_DATE_START + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_START + "<='" + endDate2 + "'" +
                " ORDER BY " + RECORD_DATE_START + " DESC," + RECORD_TIME_START + " DESC," + RECORD_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        List<String> dates = formHelper.getArrayOfDate(endDate1, endDate2);

        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Timeline timeline = new Timeline(formHelper.getIndexOfDate(dates, c.getString(c.getColumnIndex(RECORD_DATE_START))),
                        c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getInt(c.getColumnIndex(USER_ID)));
                timelines.add(timeline);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return timelines;
    }


    public List<Timeline> getAllTimelineIcon(String userID, int activityID, String endDate1, String endDate2) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        FormHelper formHelper = new FormHelper(mContext);
        List<Timeline> timelines = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        List<String> dates = formHelper.getArrayOfDate(endDate1, endDate2);


        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Timeline timeline = new Timeline(formHelper.getIndexOfDate(dates, c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getInt(c.getColumnIndex(USER_ID)));
                timelines.add(timeline);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return timelines;
    }


    public int getTimelineCount(String userID, int activityID, String endDate1, String endDate2) {
        // Log.i(DATABASE_TAG, "MyDatabaseHelper.getUsersCount ... ");
        String countQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                " AND " + RECORD_DATE_END + ">='" + endDate1 + "'" + " AND " + RECORD_DATE_END + "<='" + endDate2 + "'" +
                " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();

        cursor.close();
        db.close();

        // return count
        return count;
    }


    //by start day
    public List<Record> getAllRecordsByDayToDraw(String userID, int activityID, String startDay) {
        DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        List<Record> records = new ArrayList<Record>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECORD_NAME + " WHERE " + USER_ID + " = " + "'" + userID + "'" + " AND " + ACTIVITIES_ID + "=" + activityID +
                " AND " + RECORD_DATE_START + "='" + startDay + "'" +
                " ORDER BY " + RECORD_DATE_END + " DESC," + RECORD_TIME_END + " DESC," + RECORD_ID + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // DateFormatUtility dateFormat = new DateFormatUtility(mContext);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Date dateStart = (c.getString(c.getColumnIndex(RECORD_DATE_START)) == null) ? null : dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_START)));
                Date timeStart = (c.getString(c.getColumnIndex(RECORD_TIME_START)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_START)));
                Date duration = (c.getString(c.getColumnIndex(RECORD_DURATION)) == null) ? null : dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_DURATION)));
                Boolean isSyn = (c.getInt((c.getColumnIndex(RECORD_SYN_STATUS))) == 1) ? true : false;
                Record record = new Record(c.getString(c.getColumnIndex(RECORD_ID)),
                        c.getString(c.getColumnIndex(RECORD_OPTION)),
                        c.getDouble(c.getColumnIndex(RECORD_AMOUNT)),
                        c.getString(c.getColumnIndex(RECORD_AMOUNT_UNIT)),
                        dateStart,
                        timeStart,
                        dateFormat.getDateFormat2(c.getString(c.getColumnIndex(RECORD_DATE_END))),
                        dateFormat.getTimeFormat2(c.getString(c.getColumnIndex(RECORD_TIME_END))),
                        duration,
                        c.getString(c.getColumnIndex(RECORD_NOTE)),
                        c.getInt(c.getColumnIndex(ACTIVITIES_ID)),
                        c.getString(c.getColumnIndex(USER_ID)),
                        isSyn,
                        c.getString(c.getColumnIndex(RECORD_RECORD_STATUS)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_BY)),
                        c.getString(c.getColumnIndex(RECORD_CREATE_DATE))

                );
                records.add(record);
                // adding to todo list

            } while (c.moveToNext());
        }

        c.close();
        db.close();
        return records;
    }


}

