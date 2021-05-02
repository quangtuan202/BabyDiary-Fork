package com.riagon.babydiary;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Data.FilePicker;
import com.riagon.babydiary.Data.LocalBackupRestore;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.FileAdapter;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class BackupRestoreActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private LocalBackupRestore localBackupRestore;
    private LocalDataHelper localDataHelper;
    private SettingHelper settingHelper;
    private User activeUser;
    private Switch automaticBackup;
    private Button backupNow;
    private ArrayList<FilePicker> listFile2;
    private FileAdapter fileListViewAdapter;
    private ListView listViewFile;
    private LinearLayout noData_layout;
    private ActionBar toolbar;
    private String fromActivity = "Backup";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(this);
        settingHelper = new SettingHelper(this);
        localBackupRestore = new LocalBackupRestore(this);
        localDataHelper = new LocalDataHelper(this);
        activeUser = db.getUser(localDataHelper.getActiveUserId());
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            fromActivity = getIntent().getStringExtra("FROM");
        }

        if (fromActivity.equals("Welcome")) {
            settingHelper.setThemes("pink");
        } else {
            settingHelper.setThemes(activeUser.getUserTheme());
        }

        setTitle(getResources().getString(R.string.backup_restore_title));
        setContentView(R.layout.activity_backup_restore);
        noData_layout = (LinearLayout) findViewById(R.id.growth_list_no_dada);

        toolbar = getSupportActionBar();
        toolbar.setDisplayShowHomeEnabled(true);
        toolbar.setDisplayHomeAsUpEnabled(true);
        toolbar.setHomeButtonEnabled(true);


        listFile2 = new ArrayList<>();

        //listFile =  (ArrayList<FilePicker>)getIntent().getSerializableExtra("FILES_TO_SEND");

        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + getResources().getString(R.string.app_name));
        String errCreateFile = getResources().getString(R.string.bk_er_file);
        String errFindForder = getResources().getString(R.string.bk_er_find_forder);

        if (folder.exists()) {
            final File[] files = folder.listFiles();

            //sort file

            Arrays.sort(files, new Comparator() {
                public int compare(Object o1, Object o2) {
                    // return new Long(((File)o1).lastModified()).compareTo(new Long(((File) o2).lastModified()));
                    return new Long(((File) o2).lastModified()).compareTo(new Long(((File) o1).lastModified()));
                }

            });


            ArrayList<FilePicker> listFile;
            listFile = new ArrayList<>();

            for (File file : files) {
                Date lastModDate = new Date(file.lastModified());
                DateFormat dateShowFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", this.getResources().getConfiguration().locale);
                dateShowFormat.format(lastModDate);
                //Log.i("File last modified @ : "+ lastModDate.toString());
                listFile.add(new FilePicker(file.getName(), dateShowFormat.format(lastModDate).toString(), String.valueOf(file.length() / 1024), String.valueOf(file.getAbsolutePath())));

            }
            if (listFile.size() > 0) {
                listFile2 = listFile;


                // noData_layout.setVisibility(View.GONE);
            } else {
                //noData_layout.setVisibility(View.VISIBLE);
                Toast.makeText(this, errCreateFile, Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, errFindForder, Toast.LENGTH_SHORT).show();
        }

        fileListViewAdapter = new FileAdapter(this, listFile2, db);
        listViewFile = findViewById(R.id.list_file);
        listViewFile.setAdapter(fileListViewAdapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_backup, menu);

        MenuItem item = menu.findItem(R.id.backup_now);

        if (fromActivity.equals("Welcome")) {
            item.setVisible(false);   //hide it
        } else {
            item.setVisible(true);   //hide it
        }


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.backup_now:

                String outFileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + getResources().getString(R.string.app_name) + File.separator;
                localBackupRestore.performBackup(db, outFileName);

                //listFile =  (ArrayList<FilePicker>)getIntent().getSerializableExtra("FILES_TO_SEND");
                File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + getResources().getString(R.string.app_name));
                String errCreateFile = getResources().getString(R.string.bk_er_file);
                String errFindForder = getResources().getString(R.string.bk_er_find_forder);

                if (folder.exists()) {
                    final File[] files = folder.listFiles();

                    //sort file
                    Arrays.sort(files, new Comparator() {
                        public int compare(Object o1, Object o2) {
                            // return new Long(((File)o1).lastModified()).compareTo(new Long(((File) o2).lastModified()));
                            return new Long(((File) o2).lastModified()).compareTo(new Long(((File) o1).lastModified()));
                        }

                    });

                    ArrayList<FilePicker> listFile;
                    listFile = new ArrayList<>();

                    for (File file : files) {
                        Date lastModDate = new Date(file.lastModified());
                        DateFormat dateShowFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", this.getResources().getConfiguration().locale);
                        dateShowFormat.format(lastModDate);
                        //Log.i("File last modified @ : "+ lastModDate.toString());
                        listFile.add(new FilePicker(file.getName(), dateShowFormat.format(lastModDate).toString(), String.valueOf(file.length() / 1024), String.valueOf(file.getAbsolutePath())));

                    }
                    if (listFile.size() > 0) {
                        listFile2.clear();
                        listFile2.addAll(listFile);
                        fileListViewAdapter.notifyDataSetChanged();
                        // noData_layout.setVisibility(View.GONE);
                    } else {
                        //noData_layout.setVisibility(View.VISIBLE);
                        //   Toast.makeText(this, errCreateFile, Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // Toast.makeText(this, errFindForder, Toast.LENGTH_SHORT).show();
                }


        }

        return super.onOptionsItemSelected(item);
    }


    private void checkNoDataView() {
        if (db.getGrowthsCount(activeUser) == 0) {
            noData_layout.setVisibility(View.VISIBLE);
            //recyclerView.setVisibility(View.GONE);
        } else {

            noData_layout.setVisibility(View.GONE);
            //   recyclerView.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
