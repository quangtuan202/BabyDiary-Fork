package com.riagon.babydiary.Data;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import com.riagon.babydiary.BackupRestoreActivity;
import com.riagon.babydiary.R;
import com.riagon.babydiary.WelcomeActivity;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LocalBackupRestore {


    private Activity activity;

    public LocalBackupRestore(Activity activity) {
        this.activity = activity;
    }


    //ask to the user a name for the backup and perform it. The backup will be saved to a custom folder.
    public void performBackup(final DatabaseHelper db, final String outFileName) {

        if (Permissions.verifyStoragePermissions(activity)) {

            //File folder = new File(activity.getExternalFilesDir(null).getAbsolutePath() + File.separator + activity.getResources().getString(R.string.app_name));
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + activity.getResources().getString(R.string.app_name));
            // String intStorageDirectory = Environment.getDataDirectory().getAbsolutePath().toString()+ "/YourDirectoryName";
            //File folder = new File(intStorageDirectory);
            boolean success = true;
            String errCreateForder = activity.getResources().getString(R.string.bk_er_create_forder);
            if (!folder.exists())
                success = folder.mkdirs();
            if (success) {
                String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                String out = outFileName + "BabyDiary_" + timeStamp + ".db";
                db.backup(out);

            } else
                Toast.makeText(activity, errCreateForder, Toast.LENGTH_SHORT).show();

        } else {
            return;
        }
    }

    //ask to the user a name for the backup and perform it. The backup will be saved to a custom folder.
    public void performAutoBackup(final DatabaseHelper db, final String outFileName) {

        if (Permissions.verifyStoragePermissions(activity)) {

            //File folder = new File(activity.getExternalFilesDir(null).getAbsolutePath() + File.separator + activity.getResources().getString(R.string.app_name));
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + activity.getResources().getString(R.string.app_name));
            // String intStorageDirectory = Environment.getDataDirectory().getAbsolutePath().toString()+ "/YourDirectoryName";
            //File folder = new File(intStorageDirectory);
            boolean success = true;
            String errCreateForder = activity.getResources().getString(R.string.bk_er_create_forder);
            if (!folder.exists())
                success = folder.mkdirs();
            if (success) {
                String timeStamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
                String out = outFileName + "BabyDiary_" + timeStamp + "_auto" + ".db";
                db.backup(out);

            } else {

            }
            //Toast.makeText(activity, errCreateForder, Toast.LENGTH_SHORT).show();

        } else {
            return;
        }
    }


    //ask to the user what backup to restore
    public void performRestore(final DatabaseHelper db) {
        if (Permissions.verifyStoragePermissions(activity)) {
            //File folder = new File(activity.getExternalFilesDir(null).getAbsolutePath() + File.separator + activity.getResources().getString(R.string.app_name));
            File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + File.separator + activity.getResources().getString(R.string.app_name));
            String errCreateFile = activity.getResources().getString(R.string.bk_er_file);
            String errFindForder = activity.getResources().getString(R.string.bk_er_find_forder);
            if (folder.exists()) {
                final File[] files = folder.listFiles();
                ArrayList<FilePicker> listFile;
                listFile = new ArrayList<>();

                for (File file : files) {
                    Date lastModDate = new Date(file.lastModified());
                    DateFormat dateShowFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
                    dateShowFormat.format(lastModDate);
                    //Log.i("File last modified @ : "+ lastModDate.toString());
                    listFile.add(new FilePicker(file.getName(), dateShowFormat.format(lastModDate).toString(), String.valueOf(file.length() / 1024), String.valueOf(file.getAbsolutePath())));

                }
                if (listFile.size() > 0) {
                    Intent i = new Intent(activity, BackupRestoreActivity.class);

                    if (this.activity instanceof WelcomeActivity) {
                        i.putExtra("FROM", "Welcome");
                    } else {
                        i.putExtra("FROM", "Backup");
                    }

                    i.putExtra("FILES_TO_SEND", listFile);
                    activity.startActivity(i);
                    activity.finish();
                } else {
                    Toast.makeText(activity, errCreateFile, Toast.LENGTH_SHORT).show();
                }


            } else
                Toast.makeText(activity, errFindForder, Toast.LENGTH_SHORT).show();
            // return successful;
        } else {
            return;
        }


    }
}
