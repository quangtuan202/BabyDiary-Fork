package com.riagon.babydiary.Utility;


import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculator {

    public double weightKg;
    public double weightLb;
    public double heightCm;
    public int heightFt;
    public double heightIn;
    public double volumeOz;
    public double volumeMl;
    public double temperatureC;
    public double temperatureF;


    public Calculator() {

    }

    public double convertKgLb(double kg) {
        weightLb = kg * 2.20462;
        //DecimalFormat df = new DecimalFormat("#.#");
        //  weightLb = Double.valueOf(df.format(weightLb));
        BigDecimal bd = new BigDecimal(weightLb).setScale(1, RoundingMode.HALF_UP);
        weightLb = bd.doubleValue();
        return weightLb;
    }


    public double convertLbKg(double lb) {
        weightKg = lb * 0.453592;
        // DecimalFormat df = new DecimalFormat("#.#");
        //   weightKg = Double.valueOf(df.format(weightKg));
        BigDecimal bd = new BigDecimal(weightKg).setScale(1, RoundingMode.HALF_UP);
        weightKg = bd.doubleValue();
        return weightKg;
    }


    public double convertCmFt(double cm) {

        BigDecimal bigDecimal = new BigDecimal(String.valueOf(cm * 0.0328084));
        heightFt = bigDecimal.intValue();
        return heightFt;

    }

    public double convertCmIn(double cm) {

        BigDecimal bigDecimal = new BigDecimal(String.valueOf(cm * 0.0328084));
        int intValue = bigDecimal.intValue();
        //   heightFt = intValue;
        double heightFtDecimal = Double.parseDouble(bigDecimal.subtract(new BigDecimal(intValue)).toString());

        heightIn = heightFtDecimal * 12;

        //  DecimalFormat df = new DecimalFormat("#.#");
        //heightIn = Double.valueOf(df.format(heightIn));
        BigDecimal bd = new BigDecimal(heightIn).setScale(1, RoundingMode.HALF_UP);
        heightIn = bd.doubleValue();

        return heightIn;

    }

    public double convertSimpleCmIn(double cm) {

        heightIn = cm * 0.393700787;

        //  DecimalFormat df = new DecimalFormat("#.#");
        //heightIn = Double.valueOf(df.format(heightIn));
        BigDecimal bd = new BigDecimal(heightIn).setScale(1, RoundingMode.HALF_UP);
        heightIn = bd.doubleValue();


        return heightIn;

    }


    public double convertFtInCm(int ft, double in) {

        heightCm = ft * 30.48 + in * 2.54;
        //DecimalFormat df = new DecimalFormat("#.#");
        //heightCm = Double.valueOf(df.format(heightCm));
        BigDecimal bd = new BigDecimal(heightCm).setScale(1, RoundingMode.HALF_UP);
        heightCm = bd.doubleValue();
        return heightCm;

    }

    public double convertInCm(double in) {
        heightCm = in * 2.54;
        //DecimalFormat df = new DecimalFormat("#.#");
        //heightCm = Double.valueOf(df.format(heightCm));
        BigDecimal bd = new BigDecimal(heightCm).setScale(1, RoundingMode.HALF_UP);
        heightCm = bd.doubleValue();
        return heightCm;

    }

    public double convertMlOz(double ml) {
        volumeOz = ml * 0.03381;
        //DecimalFormat df = new DecimalFormat("#.#");
        //  weightLb = Double.valueOf(df.format(weightLb));
        BigDecimal bd = new BigDecimal(volumeOz).setScale(2, RoundingMode.HALF_UP);
        volumeOz = bd.doubleValue();
        return volumeOz;
    }


    public double convertOzMl(double oz) {
        volumeMl = (int) (oz * 29.5735296);
        return volumeMl;
    }

    public double convertCtoF(double c) {
        temperatureF = c * 1.8 + 32;
        BigDecimal bd = new BigDecimal(temperatureF).setScale(1, RoundingMode.HALF_UP);
        temperatureF = bd.doubleValue();
        return temperatureF;
    }

    public double convertFtoC(double f) {
        temperatureC = (f - 32) / 1.8;
        BigDecimal bd = new BigDecimal(temperatureC).setScale(1, RoundingMode.HALF_UP);
        temperatureC = bd.doubleValue();
        return temperatureC;
    }


    public double getWeightKg() {
        return weightKg;
    }

    public double getWeightLb() {
        return weightLb;
    }

    public double getHeightCm() {
        return heightCm;
    }

    public int getHeightFt() {
        return heightFt;
    }

    public double getHeightIn() {
        return heightIn;
    }

    public double getVolumeOz() {
        return volumeOz;
    }

    public double getVolumeMl() {
        return volumeMl;
    }

    public double getTemperatureC() {
        return temperatureC;
    }

    public double getTemperatureF() {
        return temperatureF;
    }
}

