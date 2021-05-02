package com.riagon.babydiary;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Data.LocalBackupRestore;
import com.riagon.babydiary.Data.Permissions;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import java.util.List;

public class SettingFragment extends PreferenceFragmentCompat {

    public SettingHelper settingHelper;
    private LocalDataHelper localDataHelper;
    public User currentUser;
    private DatabaseHelper db;
    private LocalBackupRestore localBackupRestore;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String s) {
        db = new DatabaseHelper(getActivity());
        settingHelper = new SettingHelper(getActivity());
        localDataHelper = new LocalDataHelper(getActivity());
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        addPreferencesFromResource(R.xml.preferences);
        // implement your settings here
        Preference privacyPolicy = findPreference("privacy_settings_key");
        Preference backupPref = findPreference("backup_settings_key");
        Preference darkMode = findPreference("DARK_MODE_KEY");
        Preference shareFriend = findPreference("share_settings_key");
        Preference checkOtherApp = findPreference("other_apps_settings_key");
        Preference terms = findPreference("terms_settings_key");
        Preference remove_ads_settings_key = findPreference("remove_ads_settings_key");
        Preference changeLanguage = findPreference("LANGUAGE_KEY");
        Preference exportToExcel = findPreference("export_excel_settings_key");


        darkMode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean isOn = (boolean) newValue;


                if (isOn) {
                    // getListView().setBackgroundColor(Color.BLACK);
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }

               // Toast.makeText(getContext(), "Status " + isOn, Toast.LENGTH_SHORT).show();
               // getActivity().finish();
              //  startActivity(getActivity().getIntent());
                return true;
            }
        });


        exportToExcel.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getContext(), "Export to PDF", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        changeLanguage.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
              // String language = newValue.toString();

                getActivity().finish();
                startActivity(getActivity().getIntent());

//                getPreferenceScreen().removeAll();
//                addPreferencesFromResource(R.xml.preferences);
                return true;
            }
        });


        shareFriend.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                shareToFriend();
                return true;
            }
        });

        checkOtherApp.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                checkOtherApp();
                return true;
            }
        });


        localBackupRestore = new LocalBackupRestore(this.getActivity());
        backupPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                if (Permissions.verifyStoragePermissions(getActivity())) {

                    Intent intent = new Intent(getContext(), BackupRestoreActivity.class);
                    startActivity(intent);
                    // localBackupRestore.performRestore(db);

                } else {

                }
                return true;
            }
        });

        privacyPolicy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                DialogLoginPrivacyPolicy();
                return true;
            }
        });


        terms.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            public boolean onPreferenceClick(Preference preference) {
                DialogLoginTerms();
                return true;
            }
        });

        remove_ads_settings_key.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), UpgradeProActivity.class);

// Start boardgame
                startActivity(intent);


                return true;
            }
        });


    }

    private void DialogLoginPrivacyPolicy() {
        TextView tx_titleTerm1;
        TextView tx_titleTerm2;
        TextView tx_titleTerm3;
        TextView tx_titleTerm4;
        TextView tx_titleTerm5;
        TextView tx_titleTerm6;
        TextView tx_titleTerm7;
        TextView tx_titleTerm8;
        TextView tx_titleTerm9;
        TextView tx_titleTerm10;
        TextView tx_titleTerm11;
        TextView tx_titleTerm12;
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.privacy_policy_dialog, null);
        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        tx_titleTerm1 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm1);
        tx_titleTerm2 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm2);
        tx_titleTerm3 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm3);
        tx_titleTerm4 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm4);
        tx_titleTerm5 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm5);
        tx_titleTerm6 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm6);
        tx_titleTerm7 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm7);
        tx_titleTerm8 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm8);
        tx_titleTerm9 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm9);
        tx_titleTerm10 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm10);
        tx_titleTerm11 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm11);
        tx_titleTerm12 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm12);
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm1, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm2, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm3, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm4, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm5, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm6, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm7, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm8, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm9, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm10, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm11, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm12, currentUser.getUserTheme());

        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code for matching password
                // Toast.makeText(getContext(), "Login clicked", Toast.LENGTH_SHORT).show();

            }
        });
        androidx.appcompat.app.AlertDialog dialog = alert.create();
        dialog.show();
        //    dialog.getWindow().setLayout(1300, 2000);

    }


    private void DialogLoginTerms() {
        TextView tx_titleTerm1;
        TextView tx_titleTerm2;
        TextView tx_titleTerm3;
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.terms_conditions_dialog, null);
        androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
        tx_titleTerm1 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm1);
        tx_titleTerm2 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm2);
        tx_titleTerm3 = (TextView) alertLayout.findViewById(R.id.tx_titleTerm3);
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm1, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm2, currentUser.getUserTheme());
        settingHelper.setTextColorDialog(getActivity(), tx_titleTerm3, currentUser.getUserTheme());

        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // code for matching password
                // Toast.makeText(getActivity(), "Login clicked", Toast.LENGTH_SHORT).show();

            }
        });
        androidx.appcompat.app.AlertDialog dialog = alert.create();
        dialog.show();
        //    dialog.getWindow().setLayout(1300, 2000);

    }

    public void shareToFriend() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "https://play.google.com/store/apps/details?id=com.perfect.bmi";
        String shareSub = "Track your weight";
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share using Perfect BMI"));

    }


    public void checkOtherApp() {

        Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getActivity().getPackageName())));
        }
    }


}