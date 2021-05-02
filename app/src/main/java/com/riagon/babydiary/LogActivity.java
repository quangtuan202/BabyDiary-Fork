package com.riagon.babydiary;

import android.widget.ImageView;
import android.widget.TextView;

public class LogActivity {
    private String logDate, logBabyAge;
    private int activitiesIcon;
    private String logTime, logDuration;


    public LogActivity(){

    }

    public LogActivity(String logDate, String logBabyAge, int activitiesIcon, String logTime, String logDuration) {
        this.logDate = logDate;
        this.logBabyAge = logBabyAge;
        this.activitiesIcon = activitiesIcon;
        this.logTime = logTime;
        this.logDuration = logDuration;
    }

    public String getLogDate() {
        return logDate;
    }

    public void setLogDate(String logDate) {
        this.logDate = logDate;
    }

    public String getLogBabyAge() {
        return logBabyAge;
    }

    public void setLogBabyAge(String logBabyAge) {
        this.logBabyAge = logBabyAge;
    }

    public int getActivitiesIcon() {
        return activitiesIcon;
    }

    public void setActivitiesIcon(int activitiesIcon) {
        this.activitiesIcon = activitiesIcon;
    }

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getLogDuration() {
        return logDuration;
    }

    public void setLogDuration(String logDuration) {
        this.logDuration = logDuration;
    }


}

