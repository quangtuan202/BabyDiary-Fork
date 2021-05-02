package com.riagon.babydiary.Utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.preference.PreferenceManager;

import java.util.Locale;

public class LocalDataHelper {
    private SharedPreferences prefs;
    private SharedPreferences.Editor prefsEditor;
    private Context context;

    private String language;
    private String temperatureUnit;
    private String volumeUnit;
    private String whhUnit;
    private Boolean isDarkMode;
    private String activeUserId;
    private String purchaseState;
    private Long purchaseTime;

    private static final String LANGUAGE_KEY = "LANGUAGE_KEY";
    private static final String TEMPERATURE_KEY = "TEMPERATURE_KEY";
    private static final String VOLUME_KEY = "VOLUME_KEY";
    private static final String WHH_KEY = "WHH_KEY";
    private static final String DARK_MODE_KEY = "DARK_MODE_KEY";
    private static final String ACTIVE_USER_KEY = "ACTIVE_USER_KEY";
    private static final String PURCHASE_TIME_KEY = "PURCHASE_TIME_KEY";
    private static final String PURCHASE_STATE_KEY = "PURCHASE_STATE_KEY";

    public LocalDataHelper(Context context) {
        this.context = context;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.prefsEditor = prefs.edit();
        this.context = context;
        String languageDef = Locale.getDefault().getLanguage();
        language = prefs.getString(LANGUAGE_KEY, languageDef);
        temperatureUnit = prefs.getString(TEMPERATURE_KEY, "c");
        volumeUnit = prefs.getString(VOLUME_KEY, "ml");
        whhUnit = prefs.getString(WHH_KEY, "cm-kg");
        isDarkMode = prefs.getBoolean(DARK_MODE_KEY, false);
        activeUserId = prefs.getString(ACTIVE_USER_KEY, "");
        purchaseState = prefs.getString(PURCHASE_STATE_KEY, "");
        purchaseTime = prefs.getLong(PURCHASE_TIME_KEY, 0);
        // time = prefs.getString("Time", "20:00");
    }


    public String getTemperatureUnit() {
        return temperatureUnit;
    }

    public void setTemperatureUnit(String temperatureUnit) {
        prefsEditor.putString(TEMPERATURE_KEY, temperatureUnit);
        prefsEditor.commit();
        // this.temperatureUnit = temperatureUnit;
    }

    public String getVolumeUnit() {
        return volumeUnit;
    }

    public void setVolumeUnit(String volumeUnit) {
        //  this.volumeUnit = volumeUnit;
        prefsEditor.putString(VOLUME_KEY, volumeUnit);
        prefsEditor.commit();
    }

    public String getWhhUnit() {
        return whhUnit;
    }

    public void setWhhUnit(String whhUnit) {
        // this.whhUnit = whhUnit;
        prefsEditor.putString(WHH_KEY, whhUnit);
        prefsEditor.commit();
    }

    public Boolean getDarkMode() {
        return isDarkMode;
    }

    public void setDarkMode(Boolean darkMode) {

        //isDarkMode = darkMode;
        prefsEditor.putBoolean(DARK_MODE_KEY, darkMode);
        prefsEditor.commit();

    }

    public String getActiveUserId() {
        return activeUserId;
    }

    public void setActiveUserId(String activeUserId) {
        //this.activeUserId = activeUserId;
        prefsEditor.putString(ACTIVE_USER_KEY, activeUserId);
        prefsEditor.commit();
    }


    public String getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(String purchaseState) {
        prefsEditor.putString(PURCHASE_STATE_KEY, purchaseState);
        prefsEditor.commit();

    }

    public Long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(Long purchaseTime) {
        prefsEditor.putLong(PURCHASE_TIME_KEY, purchaseTime);
        prefsEditor.commit();
    }

    public void setLanguage() {
        if (language.contains("Vietnamese")) {
            setLocale("vi", "VN");
        } else if (language.contains("Chinese")) {
            setLocale("zh", "TW");
        } else {
            setLocale("en", "EN");
        }
    }


    public void setLocale(String lang, String lang2) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();

        if (Build.VERSION.SDK_INT >= 17) {

            configuration.setLocale(new Locale(lang, lang2));

        } else {
            configuration.locale = new Locale(lang, lang2);
        }

        resources.updateConfiguration(configuration, dm);

    }

}
