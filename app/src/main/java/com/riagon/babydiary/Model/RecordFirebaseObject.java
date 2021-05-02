package com.riagon.babydiary.Model;

import java.util.Date;

public class RecordFirebaseObject {
    private String recordId;
    private String option;
    private Double amount;
    private String amountUnit;
    private String dateStart;
    private String timeStart;
    private String dateEnd;
    private String timeEnd;
    private String duration;
    private String note;
    private int activitiesId;
    private String userId;
    private String createdBy;
    private String recordCreatedDatetime;

    public RecordFirebaseObject() {
    }

    public RecordFirebaseObject(String recordId, String option, Double amount, String amountUnit, String dateStart, String timeStart, String dateEnd, String timeEnd, String duration, String note, int activitiesId, String userId, String createdBy, String recordCreatedDatetime) {
        this.recordId = recordId;
        this.option = option;
        this.amount = amount;
        this.amountUnit = amountUnit;
        this.dateStart = dateStart;
        this.timeStart = timeStart;
        this.dateEnd = dateEnd;
        this.timeEnd = timeEnd;
        this.duration = duration;
        this.note = note;
        this.activitiesId = activitiesId;
        this.userId = userId;
        this.createdBy = createdBy;
        this.recordCreatedDatetime = recordCreatedDatetime;
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

    public String getDateStart() {
        return dateStart;
    }

    public void setDateStart(String dateStart) {
        this.dateStart = dateStart;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(String dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getRecordCreatedDatetime() {
        return recordCreatedDatetime;
    }

    public void setRecordCreatedDatetime(String recordCreatedDatetime) {
        this.recordCreatedDatetime = recordCreatedDatetime;
    }
}
