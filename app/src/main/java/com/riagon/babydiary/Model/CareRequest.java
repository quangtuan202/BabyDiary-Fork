package com.riagon.babydiary.Model;

public class CareRequest {
    private String careId;
    private String careEmail;
    private String babyOwnerId;
    private String babyId;
    private Boolean isAccept;

    public CareRequest() {
    }

//    public CareRequest(String babyOwnerId, String babyId, Boolean isAccept) {
//        this.babyOwnerId = babyOwnerId;
//        this.babyId = babyId;
//        this.isAccept = isAccept;
//    }

//    public CareRequest(String careEmail, String babyOwnerId, String babyId, Boolean isAccept) {
//        this.careEmail = careEmail;
//        this.babyOwnerId = babyOwnerId;
//        this.babyId = babyId;
//        this.isAccept = isAccept;
//    }


    public CareRequest(String careId, String careEmail, String babyOwnerId, String babyId, Boolean isAccept) {
        this.careId = careId;
        this.careEmail = careEmail;
        this.babyOwnerId = babyOwnerId;
        this.babyId = babyId;
        this.isAccept = isAccept;
    }

    public String getBabyOwnerId() {
        return babyOwnerId;
    }

    public void setBabyOwnerId(String babyOwnerId) {
        this.babyOwnerId = babyOwnerId;
    }

    public String getBabyId() {
        return babyId;
    }

    public void setBabyId(String babyId) {
        this.babyId = babyId;
    }

    public Boolean getAccept() {
        return isAccept;
    }

    public void setAccept(Boolean accept) {
        isAccept = accept;
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
}
