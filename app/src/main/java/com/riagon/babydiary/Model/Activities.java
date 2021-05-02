package com.riagon.babydiary.Model;

import java.util.Date;

public class Activities {

    private int activitiesId;
    private int fixedID;
    private Boolean isShow;
    private int rank;
    private Boolean isNotify;
    private String notifyOption;
    private Date notifyDate;
    private Date notifyTime;
    private String notifyRepeatInDay;
    private String notifyRepeatInTime;
    private Date createdDatetime;
    private String UserId;
    private int totalActivities;



    public Activities() {
    }

    public Activities(int fixedID, String userId, int totalActivities) {
        this.fixedID = fixedID;
        UserId = userId;
        this.totalActivities = totalActivities;
    }

    public Activities(int fixedID, Boolean isShow, int rank, Boolean isNotify, String notifyOption, Date notifyDate, Date notifyTime, String notifyRepeatInDay, String notifyRepeatInTime, Date createdDatetime, String userId) {
        this.fixedID = fixedID;
        this.isShow = isShow;
        this.rank = rank;
        this.isNotify = isNotify;
        this.notifyOption = notifyOption;
        this.notifyDate = notifyDate;
        this.notifyTime = notifyTime;
        this.notifyRepeatInDay = notifyRepeatInDay;
        this.notifyRepeatInTime = notifyRepeatInTime;
        this.createdDatetime = createdDatetime;
        UserId = userId;
    }


    public Activities(int activitiesId, int fixedID, Boolean isShow, int rank, Boolean isNotify, String notifyOption, Date notifyDate, Date notifyTime, String notifyRepeatInDay, String notifyRepeatInTime, Date createdDatetime, String userId) {
        this.activitiesId = activitiesId;
        this.fixedID = fixedID;
        this.isShow = isShow;
        this.rank = rank;
        this.isNotify = isNotify;
        this.notifyOption = notifyOption;
        this.notifyDate = notifyDate;
        this.notifyTime = notifyTime;
        this.notifyRepeatInDay = notifyRepeatInDay;
        this.notifyRepeatInTime = notifyRepeatInTime;
        this.createdDatetime = createdDatetime;
        UserId = userId;
    }

    public int getActivitiesId() {
        return activitiesId;
    }

    public void setActivitiesId(int activitiesId) {
        this.activitiesId = activitiesId;
    }

    public int getFixedID() {
        return fixedID;
    }

    public void setFixedID(int fixedID) {
        this.fixedID = fixedID;
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Boolean getNotify() {
        return isNotify;
    }

    public void setNotify(Boolean notify) {
        isNotify = notify;
    }

    public String getNotifyOption() {
        return notifyOption;
    }

    public void setNotifyOption(String notifyOption) {
        this.notifyOption = notifyOption;
    }

    public Date getNotifyDate() {
        return notifyDate;
    }

    public void setNotifyDate(Date notifyDate) {
        this.notifyDate = notifyDate;
    }

    public Date getNotifyTime() {
        return notifyTime;
    }

    public void setNotifyTime(Date notifyTime) {
        this.notifyTime = notifyTime;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public int getTotalActivities() {
        return totalActivities;
    }

    public void setTotalActivities(int totalActivities) {
        this.totalActivities = totalActivities;
    }

    public String getNotifyRepeatInDay() {
        return notifyRepeatInDay;
    }

    public void setNotifyRepeatInDay(String notifyRepeatInDay) {
        this.notifyRepeatInDay = notifyRepeatInDay;
    }

    public String getNotifyRepeatInTime() {
        return notifyRepeatInTime;
    }

    public void setNotifyRepeatInTime(String notifyRepeatInTime) {
        this.notifyRepeatInTime = notifyRepeatInTime;
    }

    public Date getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(Date createdDatetime) {
        this.createdDatetime = createdDatetime;
    }
}

