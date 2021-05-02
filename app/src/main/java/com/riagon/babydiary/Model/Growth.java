package com.riagon.babydiary.Model;

import java.io.Serializable;
import java.util.Date;

public class Growth implements Serializable {
    private String growthId;
    private String growthUnit;
    private double growthWeight;
    private double growthLength;
    private double growthHead;
    private String growthNote;
    private Boolean isSyn;
    private String recordStatus;
    private String createdBy;
    private String userId;
    private String growthDate, growthTime;

    public Growth() {

    }

//    public Growth(String growthId, String growthUnit, double growthWeight, double growthLength, double growthHead, String growthNote, Boolean isSyn, String recordStatus, String userId, String growthDate, String growthTime) {
//        this.growthId = growthId;
//        this.growthUnit = growthUnit;
//        this.growthWeight = growthWeight;
//        this.growthLength = growthLength;
//        this.growthHead = growthHead;
//        this.growthNote = growthNote;
//        this.isSyn = isSyn;
//        this.recordStatus = recordStatus;
//        this.userId = userId;
//        this.growthDate = growthDate;
//        this.growthTime = growthTime;
//    }

    public Growth(String growthId, String growthUnit, double growthWeight, double growthLength, double growthHead, String growthNote, Boolean isSyn, String recordStatus, String createdBy, String userId, String growthDate, String growthTime) {
        this.growthId = growthId;
        this.growthUnit = growthUnit;
        this.growthWeight = growthWeight;
        this.growthLength = growthLength;
        this.growthHead = growthHead;
        this.growthNote = growthNote;
        this.isSyn = isSyn;
        this.recordStatus = recordStatus;
        this.createdBy = createdBy;
        this.userId = userId;
        this.growthDate = growthDate;
        this.growthTime = growthTime;
    }

    public Growth(String growthUnit, double growthWeight, double growthLength, double growthHead, String growthNote, Boolean isSyn, String recordStatus, String createdBy, String userId, String growthDate, String growthTime) {
        this.growthUnit = growthUnit;
        this.growthWeight = growthWeight;
        this.growthLength = growthLength;
        this.growthHead = growthHead;
        this.growthNote = growthNote;
        this.isSyn = isSyn;
        this.recordStatus = recordStatus;
        this.createdBy = createdBy;
        this.userId = userId;
        this.growthDate = growthDate;
        this.growthTime = growthTime;
    }
//    public Growth(String growthUnit, double growthWeight, double growthLength, double growthHead, String growthNote, Boolean isSyn, String recordStatus, String userId, String growthDate, String growthTime) {
//        this.growthUnit = growthUnit;
//        this.growthWeight = growthWeight;
//        this.growthLength = growthLength;
//        this.growthHead = growthHead;
//        this.growthNote = growthNote;
//        this.isSyn = isSyn;
//        this.recordStatus = recordStatus;
//        this.userId = userId;
//        this.growthDate = growthDate;
//        this.growthTime = growthTime;
//    }


    //Firebase


    public String getGrowthId() {
        return growthId;
    }

    public void setGrowthId(String growthId) {
        this.growthId = growthId;
    }

    public String getGrowthUnit() {
        return growthUnit;
    }

    public void setGrowthUnit(String growthUnit) {
        this.growthUnit = growthUnit;
    }

    public double getGrowthWeight() {
        return growthWeight;
    }

    public void setGrowthWeight(double growthWeight) {
        this.growthWeight = growthWeight;
    }

    public double getGrowthLength() {
        return growthLength;
    }

    public void setGrowthLength(double growthLength) {
        this.growthLength = growthLength;
    }

    public double getGrowthHead() {
        return growthHead;
    }

    public void setGrowthHead(double growthHead) {
        this.growthHead = growthHead;
    }

    public String getGrowthNote() {
        return growthNote;
    }

    public void setGrowthNote(String growthNote) {
        this.growthNote = growthNote;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGrowthDate() {
        return growthDate;
    }

    public void setGrowthDate(String growthDate) {
        this.growthDate = growthDate;
    }

    public String getGrowthTime() {
        return growthTime;
    }

    public void setGrowthTime(String growthTime) {
        this.growthTime = growthTime;
    }
}
