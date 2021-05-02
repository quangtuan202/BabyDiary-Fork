package com.riagon.babydiary.Model;

public class Stats {
    String statsOption;
    int statsTime;
    double statsVolume;

    public Stats(String statsOption, int statsTime,double statsVolume) {
        this.statsOption = statsOption;
        this.statsTime = statsTime;
        this.statsVolume=statsVolume;
    }

    public String getStatsOption() {
        return statsOption;
    }

    public void setStatsOption(String statsOption) {
        this.statsOption = statsOption;
    }

    public int getStatsTime() {
        return statsTime;
    }

    public void setStatsTime(int statsTime) {
        this.statsTime = statsTime;
    }

    public double getStatsVolume() {
        return statsVolume;
    }

    public void setStatsVolume(double statsVolume) {
        this.statsVolume = statsVolume;
    }
}
