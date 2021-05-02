package com.riagon.babydiary.Utility;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Data.FilePicker;
import com.riagon.babydiary.UserDetailActivity;
import com.riagon.babydiary.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileAdapter extends BaseAdapter {
    //Dữ liệu liên kết bởi Adapter là một mảng các sản phẩm
    ArrayList<FilePicker> listFile;
    private Activity context;
    private DatabaseHelper db;

    public FileAdapter(Context context, ArrayList<FilePicker> listFile, DatabaseHelper db) {
        this.listFile = listFile;
        this.context = (Activity) context;
        this.db = db;
    }

    @Override
    public int getCount() {
        return listFile.size();
    }

    @Override
    public Object getItem(int position) {
        return listFile.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewFile;
        if (convertView == null) {
            viewFile = View.inflate(parent.getContext(), R.layout.file_list_row, null);
        } else viewFile = convertView;


        //Bind sữ liệu phần tử vào View
        final FilePicker file = (FilePicker) getItem(position);

        ((TextView) viewFile.findViewById(R.id.file_name)).setText(String.format(file.getName()));
        ((TextView) viewFile.findViewById(R.id.file_date_time)).setText(String.format(file.getDatetime()));
        ((TextView) viewFile.findViewById(R.id.file_size)).setText(String.format(file.getSize()) + " kb");

        RelativeLayout fileContainer = viewFile.findViewById(R.id.file_picker_container);
        LinearLayout fileDetailLayout = viewFile.findViewById(R.id.file_detail_layout);
        Button shareBackup = viewFile.findViewById(R.id.share_backup);
        Button deleteBackup = viewFile.findViewById(R.id.delete_backup);

        shareBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "Share it", Toast.LENGTH_SHORT).show();

                Log.i("FILE", "File: " + file.getPath());
                shareFile(file.getPath());

            }
        });
        deleteBackup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String dir = file.getPath();
                File f0 = new File(dir);
                f0.delete();

                listFile.remove(file);
                notifyDataSetChanged();

                //   Toast.makeText(context, "Delete it at" + f0, Toast.LENGTH_SHORT).show();

            }
        });

        fileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //  Toast.makeText(context, file.getName()+"Path: "+file.getPath(), Toast.LENGTH_SHORT).show();
                String askRestore = context.getResources().getString(R.string.bk_ask_restore);
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)

                        // set message, title, and icon
                        .setTitle(context.getResources().getString(R.string.import_title))
                        .setMessage(askRestore)

                        .setPositiveButton(context.getResources().getString(R.string.import_title), new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //clearApplicationData();
                                try {
                                    db.importDB(file.getPath());
                                    Intent i = new Intent(context, UserDetailActivity.class);
                                    context.startActivity(i);
                                    context.finish();
                                } catch (Exception e) {
                                    Toast.makeText(context, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                                }
                                //your deleting code
                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton(context.getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();

                myQuittingDialogBox.show();

            }
        });


        fileDetailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(context, file.getName()+"Path: "+file.getPath(), Toast.LENGTH_SHORT).show();
                String askRestore = context.getResources().getString(R.string.bk_ask_restore);
                AlertDialog myQuittingDialogBox = new AlertDialog.Builder(context)

                        // set message, title, and icon
                        .setTitle(context.getResources().getString(R.string.import_title))
                        .setMessage(askRestore)

                        .setPositiveButton(context.getResources().getString(R.string.import_title), new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //clearApplicationData();
                                try {
                                    db.importDB(file.getPath());
                                    Intent i = new Intent(context, UserDetailActivity.class);
                                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                            | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    context.startActivity(i);
                                    context.finish();
                                } catch (Exception e) {
                                    Toast.makeText(context, "Unable to restore. Retry", Toast.LENGTH_SHORT).show();
                                }
                                //your deleting code
                                dialog.dismiss();
                            }

                        })
                        .setNegativeButton(context.getResources().getString(R.string.dialog_cancel), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create();

                myQuittingDialogBox.show();


            }
        });


        return viewFile;
    }


    public void shareFile(String filePath) {

        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
        File fileWithinMyDir = new File(filePath);
        Uri fileUri;

        // Uri fileUri =Uri.parse( "content://"+filePath);

        if (fileWithinMyDir.exists()) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intentShareFile.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                fileUri = FileProvider.getUriForFile(context, "com.riagon.babydiary.provider", fileWithinMyDir);

            } else{

                fileUri =Uri.fromFile(new File(filePath));
            }

            intentShareFile.setType("*/*");
            intentShareFile.putExtra(Intent.EXTRA_STREAM, fileUri);
         //   Log.i("FILE", "File URI: " + Uri.parse("content://" + filePath));
            intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                    "Sharing File...");
            intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

         //   context.startActivity(Intent.createChooser(intentShareFile, "Share File"));

            Intent chooser = Intent.createChooser(intentShareFile, "Share File");

            List<ResolveInfo> resInfoList = context.getPackageManager().queryIntentActivities(chooser, PackageManager.MATCH_DEFAULT_ONLY);

            for (ResolveInfo resolveInfo : resInfoList) {
                String packageName = resolveInfo.activityInfo.packageName;
                context.grantUriPermission(packageName, fileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }

            context.startActivity(chooser);

        }

    }


}
