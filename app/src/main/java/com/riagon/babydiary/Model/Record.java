package com.riagon.babydiary.Model;

import java.io.Serializable;
import java.util.Date;

public class Record implements Serializable {

    private String recordId;
    private String option;
    private Double amount;
    private String amountUnit;
    private Date dateStart;
    private Date timeStart;
    private Date dateEnd;
    private Date timeEnd;
    private Date duration;
    private String note;
    private int activitiesId;
    private String userId;
    private Boolean isSyn;
    private String recordStatus;
    private String createdBy;
    private String recordCreatedDatetime;
    private int totalRecord;

    public Record() {

    }

    public Record(int activitiesId, String userId, int totalRecord) {
        this.activitiesId = activitiesId;
        this.userId = userId;
        this.totalRecord = totalRecord;
    }


    public Record(String recordId, String option, Double amount, String amountUnit, Date dateStart, Date timeStart, Date dateEnd, Date timeEnd, Date duration, String note, int activitiesId, String userId) {
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
    }


    public Record(String option, Double amount, String amountUnit, Date dateStart, Date timeStart, Date dateEnd, Date timeEnd, Date duration, String note, int activitiesId, String userId) {
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
    }

    //Start new record the suitable with Firebase


    public Record(String recordId, String option, Double amount, String amountUnit, Date dateStart, Date timeStart, Date dateEnd, Date timeEnd, Date duration, String note, int activitiesId, String userId, Boolean isSyn, String recordStatus, String createdBy, String recordCreatedDatetime) {
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
        this.isSyn = isSyn;
        this.recordStatus = recordStatus;
        this.createdBy = createdBy;
        this.recordCreatedDatetime = recordCreatedDatetime;
    }


    public Record(String option, Double amount, String amountUnit, Date dateStart, Date timeStart, Date dateEnd, Date timeEnd, Date duration, String note, int activitiesId, String userId, Boolean isSyn, String recordStatus, String createdBy, String recordCreatedDatetime) {
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
        this.isSyn = isSyn;
        this.recordStatus = recordStatus;
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

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(Date timeEnd) {
        this.timeEnd = timeEnd;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public Boolean getSyn() {
        return isSyn;
    }

    public void setSyn(Boolean syn) {
        isSyn = syn;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
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
