package com.riagon.babydiary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

public class OtherActivity extends AppCompatActivity {
    public User currentUser;
    public DatabaseHelper db;
    public SettingHelper settingHelper;
    private LocalDataHelper localDataHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        localDataHelper= new LocalDataHelper(this);
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        settingHelper.setThemes(currentUser.getUserTheme());
        setContentView(R.layout.activity_other);
        setTitle("Other Activity");
    }
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
}
