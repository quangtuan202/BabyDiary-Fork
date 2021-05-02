package com.riagon.babydiary.Model;

public class User {
    private String userId;
    private byte[] userImage;
    private String userImageUrl;
    private String userName;
    private String userBirthday;
    private String userDuedate;
    private String userSex, userTheme;
    private Boolean isSyn;
    private String requestStatus;
    private String requestorId;
    private String recordStatus;
    private String createdBy;
    private String userCreatedDatetime;

    public User() {
    }

    public User(String userId, String userImageUrl, String userName, String userBirthday, String userSex, String userTheme, String userDuedate, String userCreatedDatetime) {
        this.userId = userId;
        this.userImageUrl = userImageUrl;
        this.userName = userName;
        this.userBirthday = userBirthday;
        this.userSex = userSex;
        this.userTheme = userTheme;
        this.userDuedate = userDuedate;
        this.userCreatedDatetime = userCreatedDatetime;
    }

    public User(String userId, byte[] userImage, String userName, String userBirthday, String userSex, String userTheme, String userDuedate, String userCreatedDatetime) {
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
        this.userBirthday = userBirthday;
        this.userSex = userSex;
        this.userTheme = userTheme;
        this.userDuedate = userDuedate;
        this.userCreatedDatetime = userCreatedDatetime;
    }

    public User(byte[] userImage, String userName, String userBirthday, String userSex, String userTheme, String userDuedate, String userCreatedDatetime) {
        this.userImage = userImage;
        this.userName = userName;
        this.userBirthday = userBirthday;
        this.userSex = userSex;
        this.userTheme = userTheme;
        this.userDuedate = userDuedate;
        this.userCreatedDatetime = userCreatedDatetime;
    }

    public User(byte[] userImage, String userName, String userBirthday, String userSex, String userTheme, String userCreatedDatetime) {
        this.userImage = userImage;
        this.userName = userName;
        this.userBirthday = userBirthday;
        this.userSex = userSex;
        this.userTheme = userTheme;
        this.userCreatedDatetime = userCreatedDatetime;
    }

    public User(byte[] userImage, String userName, String userBirthday, String userSex, String userTheme, Boolean isSyn, String userDuedate, String userCreatedDatetime) {
        this.userImage = userImage;
        this.userName = userName;
        this.userBirthday = userBirthday;
        this.userSex = userSex;
        this.userTheme = userTheme;
        this.isSyn = isSyn;
        this.userDuedate = userDuedate;
        this.userCreatedDatetime = userCreatedDatetime;
    }

    public User(String userId, byte[] userImage, String userName, String userBirthday, String userSex, String userTheme, Boolean isActive, Boolean isSyn, String userDuedate, String userCreatedDatetime) {
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
        this.userBirthday = userBirthday;
        this.userSex = userSex;
        this.userTheme = userTheme;
        this.isSyn = isSyn;
        this.userDuedate = userDuedate;
        this.userCreatedDatetime = userCreatedDatetime;
    }

    public User(String userId, String userImageUrl, String userName, String userBirthday, String userSex, String userTheme, String userDuedate, String userCreatedDatetime, String createdBy) {
        this.userId = userId;
        this.userImageUrl = userImageUrl;
        this.userName = userName;
        this.userBirthday = userBirthday;
        this.userSex = userSex;
        this.userTheme = userTheme;
        this.userDuedate = userDuedate;
        this.userCreatedDatetime = userCreatedDatetime;
        this.createdBy = createdBy;
    }


    public User(String userId, byte[] userImage, String userName, String userBirthday, String userDuedate, String userSex, String userTheme, Boolean isSyn, String requestStatus, String recordStatus, String createdBy, String userCreatedDatetime) {
        this.userId = userId;
        this.userImage = userImage;
        this.userName = userName;
        this.userBirthday = userBirthday;
        this.userDuedate = userDuedate;
        this.userSex = userSex;
        this.userTheme = userTheme;
        this.isSyn = isSyn;
        this.requestStatus = requestStatus;
        this.recordStatus = recordStatus;
        this.createdBy = createdBy;
        this.userCreatedDatetime = userCreatedDatetime;
    }

    public User(byte[] userImage, String userName, String userBirthday, String userDuedate, String userSex, String userTheme, Boolean isSyn, String requestStatus, String recordStatus, String createdBy, String userCreatedDatetime) {
        this.userImage = userImage;
        this.userName = userName;
        this.userBirthday = userBirthday;
        this.userDuedate = userDuedate;
        this.userSex = userSex;
        this.userTheme = userTheme;
        this.isSyn = isSyn;
        this.requestStatus = requestStatus;
        this.recordStatus = recordStatus;
        this.createdBy = createdBy;
        this.userCreatedDatetime = userCreatedDatetime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public byte[] getUserImage() {
        return userImage;
    }

    public void setUserImage(byte[] userImage) {
        this.userImage = userImage;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserDuedate() {
        return userDuedate;
    }

    public void setUserDuedate(String userDuedate) {
        this.userDuedate = userDuedate;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex;
    }

    public String getUserTheme() {
        return userTheme;
    }

    public void setUserTheme(String userTheme) {
        this.userTheme = userTheme;
    }

    public Boolean getSyn() {
        return isSyn;
    }

    public void setSyn(Boolean syn) {
        isSyn = syn;
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRequestorId() {
        return requestorId;
    }

    public void setRequestorId(String requestorId) {
        this.requestorId = requestorId;
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

    public String getUserCreatedDatetime() {
        return userCreatedDatetime;
    }

    public void setUserCreatedDatetime(String userCreatedDatetime) {
        this.userCreatedDatetime = userCreatedDatetime;
    }
}
