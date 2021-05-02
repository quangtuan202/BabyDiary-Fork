package com.riagon.babydiary.Model;

import java.io.Serializable;

public class Photo implements Serializable {
    private String photoId;
    private byte[] photoImage;
    private String photoImageUrl;
    private Boolean isSyn;
    private String recordStatus;
    private String createdBy;
    private String growthId;
    private String userId;
    private String createdDatetime;


    public Photo() {
    }

    //Contructor for local data

    public Photo(String photoId, byte[] photoImage, Boolean isSyn, String recordStatus, String createdBy, String growthId, String userId, String createdDatetime) {
        this.photoId = photoId;
        this.photoImage = photoImage;
        this.isSyn = isSyn;
        this.recordStatus = recordStatus;
        this.createdBy = createdBy;
        this.growthId = growthId;
        this.userId = userId;
        this.createdDatetime = createdDatetime;
    }


//    public Photo(String photoId, byte[] photoImage, Boolean isSyn, String recordStatus, String growthId,String userId) {
//        this.photoId = photoId;
//        this.photoImage = photoImage;
//        this.isSyn = isSyn;
//        this.recordStatus = recordStatus;
//        this.growthId = growthId;
//        this.userId=userId;
//    }
//
//    public Photo(String photoId, byte[] photoImage, Boolean isSyn, String recordStatus, String createdBy, String growthId, String userId) {
//        this.photoId = photoId;
//        this.photoImage = photoImage;
//        this.isSyn = isSyn;
//        this.recordStatus = recordStatus;
//        this.createdBy = createdBy;
//        this.growthId = growthId;
//        this.userId = userId;
//    }

//    public Photo(byte[] photoImage, Boolean isSyn, String recordStatus, String growthId,String userId) {
//        this.photoImage = photoImage;
//        this.isSyn = isSyn;
//        this.recordStatus = recordStatus;
//        this.growthId = growthId;
//        this.userId=userId;
//    }

    //Constructor for Firebase data

//    public Photo(String photoId, String photoImageUrl, String growthId) {
//        this.photoId = photoId;
//        this.photoImageUrl = photoImageUrl;
//        this.growthId = growthId;
//    }

    public Photo(String photoId, String photoImageUrl, String createdBy, String growthId) {
        this.photoId = photoId;
        this.photoImageUrl = photoImageUrl;
        this.createdBy = createdBy;
        this.growthId = growthId;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public byte[] getPhotoImage() {
        return photoImage;
    }

    public void setPhotoImage(byte[] photoImage) {
        this.photoImage = photoImage;
    }

    public String getPhotoImageUrl() {
        return photoImageUrl;
    }

    public void setPhotoImageUrl(String photoImageUrl) {
        this.photoImageUrl = photoImageUrl;
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

    public String getGrowthId() {
        return growthId;
    }

    public void setGrowthId(String growthId) {
        this.growthId = growthId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }
}

