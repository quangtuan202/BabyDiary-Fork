package com.riagon.babydiary.Model;

public class GrowthStandard {
    public int month;
    public double threePercent;
    public double fifteenPercent;
    public double fiftyPercent;
    public double eightyFivePercent;
    public double ninetySeventhPercent;


    public GrowthStandard(int month, double threePercent, double fifteenPercent, double fiftyPercent, double eightyFivePercent, double ninetySeventhPercent) {
        this.month = month;
        this.threePercent = threePercent;
        this.fifteenPercent = fifteenPercent;
        this.fiftyPercent = fiftyPercent;
        this.eightyFivePercent = eightyFivePercent;
        this.ninetySeventhPercent = ninetySeventhPercent;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public double getThreePercent() {
        return threePercent;
    }

    public void setThreePercent(double threePercent) {
        this.threePercent = threePercent;
    }

    public double getFifteenPercent() {
        return fifteenPercent;
    }

    public void setFifteenPercent(double fifteenPercent) {
        this.fifteenPercent = fifteenPercent;
    }

    public double getFiftyPercent() {
        return fiftyPercent;
    }

    public void setFiftyPercent(double fiftyPercent) {
        this.fiftyPercent = fiftyPercent;
    }

    public double getEightyFivePercent() {
        return eightyFivePercent;
    }

    public void setEightyFivePercent(double eightyFivePercent) {
        this.eightyFivePercent = eightyFivePercent;
    }

    public double getNinetySeventhPercent() {
        return ninetySeventhPercent;
    }

    public void setNinetySeventhPercent(double ninetySeventhPercent) {
        this.ninetySeventhPercent = ninetySeventhPercent;
    }
}
