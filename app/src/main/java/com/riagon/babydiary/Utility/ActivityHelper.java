package com.riagon.babydiary.Utility;

import android.content.Context;

import com.riagon.babydiary.R;

public class ActivityHelper {
    private Context context;

    public ActivityHelper(Context context) {
        this.context = context;
    }

    public String getActivityName(int activitiesID) {
        String activityName = "";
        if (activitiesID == 1) {
            activityName = context.getResources().getString(R.string.breastfeed_name);
        } else if (activitiesID == 2) {
            activityName = context.getResources().getString(R.string.breastpump_name);
        } else if (activitiesID == 3) {
            activityName = context.getResources().getString(R.string.formula_name);
        } else if (activitiesID == 4) {
            activityName = context.getResources().getString(R.string.pump_bottle_name);
        } else if (activitiesID == 5) {
            activityName = context.getResources().getString(R.string.food_name);
        } else if (activitiesID == 6) {
            activityName = context.getResources().getString(R.string.diaper_name);
        } else if (activitiesID == 7) {
            activityName = context.getResources().getString(R.string.bath_name);
        } else if (activitiesID == 8) {
            activityName = context.getResources().getString(R.string.sleep_name);
        } else if (activitiesID == 9) {
            activityName = context.getResources().getString(R.string.tummy_time_name);
        } else if (activitiesID == 10) {
            activityName = context.getResources().getString(R.string.sunbathe_name);
        } else if (activitiesID == 11) {
            activityName = context.getResources().getString(R.string.play_name);
        } else if (activitiesID == 12) {
            activityName = context.getResources().getString(R.string.massage_name);
        } else if (activitiesID == 13) {
            activityName = context.getResources().getString(R.string.drink_name);
        } else if (activitiesID == 14) {
            activityName = context.getResources().getString(R.string.crying_name);
        } else if (activitiesID == 15) {
            activityName = context.getResources().getString(R.string.vaccination_name);
        } else if (activitiesID == 16) {
            activityName = context.getResources().getString(R.string.temperature_name);
        } else if (activitiesID == 17) {
            activityName = context.getResources().getString(R.string.medicine_name);
        } else if (activitiesID == 18) {
            activityName = context.getResources().getString(R.string.doctor_visit_name);
        } else if (activitiesID == 19) {
            activityName = context.getResources().getString(R.string.symptom_name);
        } else if (activitiesID == 20) {
            activityName = context.getResources().getString(R.string.potty_name);
        }

        return activityName;


    }

    public String getOptionLocale(String option) {
        String optionLocale = "";

            if (option.contains("Left")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.left)+" ";
            } if (option.contains("Right")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.right)+" ";
            } if (option.contains("Left+Right")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.left_right)+" ";
            } if (option.contains("Wet")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.wet)+" ";
            } if (option.contains("Dirty")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.dirty)+" ";
            } if (option.contains("Mixed")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.mixed)+" ";
            } if (option.contains("Bath")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.bath)+" ";
            } if (option.contains("Hair Wash")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.hair_wash)+" ";
            } if (option.contains("Both")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.both)+" ";
            } if (option.contains("Fruits")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.fruits_title)+" ";
            } if (option.contains("Meat")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.meat_title)+" ";
            } if (option.contains("Dairy")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.dairy_title)+" ";
            } if (option.contains("Snack")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.snack_title)+" ";
            } if (option.contains("Vegetables")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.vegetables_title)+" ";
            } if (option.contains("Fish")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.fish_title)+" ";
            } if (option.contains("Grains")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.grains_title)+" ";
            } if (option.contains("Others")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.others_title)+" ";
            } if (option.contains("Water")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.water_title)+" ";
            } if (option.contains("Milk")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.milk_title)+" ";
            } if (option.contains("Juice")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.juice_title)+" ";
            } if (option.contains("Fermented Milk")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.fermentedmilk_title)+" ";
            } if (option.contains("Breathing Problems")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.breathing_problems_title)+" ";
            } if (option.contains("Coughing")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.coughing_title)+" ";
            } if (option.contains("Decreased Appetite")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.decreased_appetite_title)+" ";
            } if (option.contains("Diarrhea")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.diarrhea_title)+" ";
            } if (option.contains("Constipation")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.constipation_title)+" ";
            } if (option.contains("Fever")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.fever_title)+" ";
            } if (option.contains("Rash")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.rash_title)+" ";
            } if (option.contains("Runny Nose")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.runny_nose_title)+" ";
            } if (option.contains("Sneezing")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.sneezing_title)+" ";
            } if (option.contains("Stuffy Nose")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.stuffy_nose_title)+" ";
            } if (option.contains("Vomiting")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.vomiting_title)+" ";
            } if (option.contains("Antibiotics")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.antibiotics_title)+" ";
            } if (option.contains("FO")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.fish_oil_title)+" ";
            } if (option.contains("Ibuprofen")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.ibuprofen_title)+" ";
            } if (option.contains("Multivitamins")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.multivitamins_title)+" ";
            } if (option.contains("Paracetamol")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.paracetamol_title)+" ";
            } if (option.contains("Vitamin")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.vitamin_title)+" ";
            } if (option.contains("BCG")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.bcg_title)+" ";
            } if (option.contains("DTap")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.dtap_title)+" ";
            } if (option.contains("Influenza")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.influenza_title)+" ";
            } if (option.contains("HepA")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.hepA_title)+" ";
            } if (option.contains("HepB")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.hepB_title)+" ";
            } if (option.contains("Hib")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.hib_title)+" ";
            } if (option.contains("MMR")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.mmr_title)+" ";
            } if (option.contains("PCV")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.pcv_title)+" ";
            } if (option.contains("Polio")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.polio_title)+" ";
            } if (option.contains("RV")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.rv_title)+" ";
            } if (option.contains("Varicella")) {
                optionLocale = optionLocale+context.getResources().getString(R.string.varicella_title)+" ";
            }

        return optionLocale;

    }


    public int getIcon(int activitiesID) {
        int icon = 0;
        if (activitiesID == 1) {
            icon = R.drawable.ic_cvfeed;
        } else if (activitiesID == 2) {
            icon = R.drawable.ic_cvpump;
        } else if (activitiesID == 3) {
            icon = R.drawable.ic_cvformula;
        } else if (activitiesID == 4) {
            icon = R.drawable.ic_cvpumpbottle;
        } else if (activitiesID == 5) {
            icon = R.drawable.ic_cvfood;
        } else if (activitiesID == 6) {
            icon = R.drawable.ic_cvdiaper;
        } else if (activitiesID == 7) {
            icon = R.drawable.ic_cvbath;
        } else if (activitiesID == 8) {
            icon = R.drawable.ic_cvsleep;
        } else if (activitiesID == 9) {
            icon = R.drawable.ic_cvtummy;
        } else if (activitiesID == 10) {
            icon = R.drawable.ic_cvsunbathe;
        } else if (activitiesID == 11) {
            icon = R.drawable.ic_cvplay;
        } else if (activitiesID == 12) {
            icon = R.drawable.ic_cvmassage;
        } else if (activitiesID == 13) {
            icon = R.drawable.ic_cvdrink;
        } else if (activitiesID == 14) {
            icon = R.drawable.ic_cvcrying;
        } else if (activitiesID == 15) {
            icon = R.drawable.ic_cvvaccination;
        } else if (activitiesID == 16) {
            icon = R.drawable.ic_cvtemperature;
        } else if (activitiesID == 17) {
            icon = R.drawable.ic_med;
        } else if (activitiesID == 18) {
            icon = R.drawable.ic_cvdoctorvisit;
        } else if (activitiesID == 19) {
            icon = R.drawable.ic_cvsymptom;
        } else if (activitiesID == 20) {
            icon = R.drawable.ic_cvpotty;
        }

        return icon;


    }

    public int getTint(int activitiesID) {
        int tintColor = R.color.colorPrimary;
        if (activitiesID == 1) {
            tintColor = R.color.cvPink;
        } else if (activitiesID == 2) {
            tintColor = R.color.cvLtPurple;
        } else if (activitiesID == 3) {
            tintColor = R.color.cvRed;
        } else if (activitiesID == 4) {
            tintColor = R.color.cvPumpBottle;
        } else if (activitiesID == 5) {
            tintColor = R.color.cvGreen;
        } else if (activitiesID == 6) {
            tintColor = R.color.cvDkBlue;
        } else if (activitiesID == 7) {
            tintColor = R.color.cvLtBlue;
        } else if (activitiesID == 8) {
            tintColor = R.color.cvDkPurple;
        } else if (activitiesID == 9) {
            tintColor = R.color.cvYellow;
        } else if (activitiesID == 10) {
            tintColor = R.color.cvSunYellow;
        } else if (activitiesID == 11) {
            tintColor = R.color.cvPlayOrange;
        } else if (activitiesID == 12) {
            tintColor = R.color.cvMassagePurple;
        } else if (activitiesID == 13) {
            tintColor = R.color.cvDrinkGreen;
        } else if (activitiesID == 14) {
            tintColor = R.color.cvCryingRed;
        } else if (activitiesID == 15) {
            tintColor = R.color.cvVaccineRed;
        } else if (activitiesID == 16) {
            tintColor = R.color.cvTemperatureRed;
        } else if (activitiesID == 17) {
            tintColor = R.color.cvMedRed;
        } else if (activitiesID == 18) {
            tintColor = R.color.cvDoctorRed;
        } else if (activitiesID == 19) {
            tintColor = R.color.cvSymptomRed;
        } else if (activitiesID == 20) {
            tintColor = R.color.cvPottyBlue;
        }
        return tintColor;


    }


}
