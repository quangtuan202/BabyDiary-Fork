package com.riagon.babydiary.Model;

public class Care {
    private String careId;
    private String careName;
    private String careEmail;
    private Boolean isOwner;
    private String careURLPhoto;
    private Boolean isAccept;

    public Care() {
    }

//    public Care(String careId, String careName, String careEmail) {
//        this.careId = careId;
//        this.careName = careName;
//        this.careEmail = careEmail;
//    }
//
//    public Care(String careId, String careName, String careEmail, Boolean isOwner) {
//        this.careId = careId;
//        this.careName = careName;
//        this.careEmail = careEmail;
//        this.isOwner = isOwner;
//    }

    public Care(String careId, String careName, String careEmail, Boolean isOwner, String careURLPhoto) {
        this.careId = careId;
        this.careName = careName;
        this.careEmail = careEmail;
        this.isOwner = isOwner;
        this.careURLPhoto = careURLPhoto;
    }

    public Care(String careId, String careName, String careEmail, Boolean isOwner, String careURLPhoto, Boolean isAccept) {
        this.careId = careId;
        this.careName = careName;
        this.careEmail = careEmail;
        this.isOwner = isOwner;
        this.careURLPhoto = careURLPhoto;
        this.isAccept = isAccept;
    }

    public String getCareName() {
        return careName;
    }

    public void setCareName(String careName) {
        this.careName = careName;
    }

    public String getCareEmail() {
        return careEmail;
    }

    public void setCareEmail(String careEmail) {
        this.careEmail = careEmail;
    }

    public String getCareId() {
        return careId;
    }

    public void setCareId(String careId) {
        this.careId = careId;
    }

    public Boolean getOwner() {
        return isOwner;
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }

    public String getCareURLPhoto() {
        return careURLPhoto;
    }

    public void setCareURLPhoto(String careURLPhoto) {
        this.careURLPhoto = careURLPhoto;
    }

    public Boolean getAccept() {
        return isAccept;
    }

    public void setAccept(Boolean accept) {
        isAccept = accept;
    }
}
