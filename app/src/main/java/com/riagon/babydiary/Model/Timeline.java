package com.riagon.babydiary.Model;

import java.util.Date;

public class Timeline {
    int index;
    private String recordId;
    private String option;
    private Double amount;
    private String amountUnit;
    private Date startDate;
    private Date startTime;
    private Date endDate;
    private Date endTime;
    private Date duration;
    private String note;
    private int activitiesId;
    private int userId;

//    public Timeline(int index, Date startDate, Date startTime, Date endDate, Date endTime) {
//        this.index = index;
//        this.startDate = startDate;
//        this.startTime = startTime;
//        this.endDate = endDate;
//        this.endTime = endTime;
//    }

    public Timeline(int index, String recordId, String option, Double amount, String amountUnit, Date startDate, Date startTime, Date endDate, Date endTime, Date duration, String note, int activitiesId, int userId) {
        this.index = index;
        this.recordId = recordId;
        this.option = option;
        this.amount = amount;
        this.amountUnit = amountUnit;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.duration = duration;
        this.note = note;
        this.activitiesId = activitiesId;
        this.userId = userId;
    }


    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getAmountUnit() {
        return amountUnit;
    }

    public void setAmountUnit(String amountUnit) {
        this.amountUnit = amountUnit;
    }

    public Date getDuration() {
        return duration;
    }

    public void setDuration(Date duration) {
        this.duration = duration;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getActivitiesId() {
        return activitiesId;
    }

    public void setActivitiesId(int activitiesId) {
        this.activitiesId = activitiesId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
