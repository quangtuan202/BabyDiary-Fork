package com.riagon.babydiary;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Data.LocalBackupRestore;

public class WelcomeActivity extends AppCompatActivity {
    private LocalBackupRestore localBackupRestore;
    private DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        db = new DatabaseHelper(this);
        localBackupRestore = new LocalBackupRestore(this);
    }

    public void goSignUpActivity(View view) {
        Intent intent = new Intent(this, SignUpActivity.class);
        this.startActivity(intent);
        finish();
    }

    public void goStoreData(View view) {

        localBackupRestore.performRestore(db);

    }

    public void goSignIn(View view) {
        Intent intent = new Intent(this, AccountActivity.class);
        this.startActivity(intent);
        finish();

    }
}
