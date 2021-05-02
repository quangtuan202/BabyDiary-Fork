package com.riagon.babydiary.Utility;

import android.content.Context;
import android.graphics.Color;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.riagon.babydiary.R;

public class SettingHelper {
    Context context;

    public SettingHelper(Context context) {
        this.context = context;
    }

    public void setThemes(String theme) {

            if (theme.equals("red")) {
                context.setTheme(R.style.RedAppTheme);
            } else if (theme.equals("green")) {
                context.setTheme(R.style.GreenAppTheme);
            } else if (theme.equals("pink")) {
                context.setTheme(R.style.PinkAppTheme);
            } else if (theme.equals("blue")) {
                context.setTheme(R.style.BlueAppTheme);
            } else if (theme.equals("purple")) {
                context.setTheme(R.style.PurpleAppTheme);
            }


    }

    public void getButtonBackgroundColor(String theme) {
        if (theme.equals("red")) {
            context.setTheme(R.style.RedAppTheme);
        } else if (theme.equals("green")) {
            context.setTheme(R.style.GreenAppTheme);
        } else if (theme.equals("pink")) {
            context.setTheme(R.style.PinkAppTheme);
        } else if (theme.equals("blue")) {
            context.setTheme(R.style.BlueAppTheme);
        } else if (theme.equals("purple")) {
            context.setTheme(R.style.PurpleAppTheme);
        }

    }


    public void setBackgroundCheckboxGroupSelected(Context context, CheckBox checkBox, String theme) {
        if (theme.equals("red")) {
            checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_group_selected_red));
        } else if (theme.equals("green")) {
            checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_group_selected_green));
        } else if (theme.equals("pink")) {
            checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_group_selected_pink));
            ;
        } else if (theme.equals("blue")) {
            checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_group_selected_blue));
        } else if (theme.equals("purple")) {
            checkBox.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_group_selected_purple));
        }

    }
    public void setBackgroundRadioButtonSelected(Context context, RadioButton radioButton, String theme) {
        if (theme.equals("red")) {
            radioButton.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_group_selected_red));
        } else if (theme.equals("green")) {
            radioButton.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_group_selected_green));
        } else if (theme.equals("pink")) {
            radioButton.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_group_selected_pink));
            ;
        } else if (theme.equals("blue")) {
            radioButton.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_group_selected_blue));
        } else if (theme.equals("purple")) {
            radioButton.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_group_selected_purple));
        }

    }

    public void setBackgroundButtonAdd(Context context, Button button, String theme) {
        if (theme.equals("red")) {
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.button_add_red));
        } else if (theme.equals("green")) {
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.button_add_green));
        } else if (theme.equals("pink")) {
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.button_add_pink));
        } else if (theme.equals("blue")) {
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.button_add_blue));
        } else if (theme.equals("purple")) {
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.button_add_purple));
        }


    }


    public void setDefaultProfileImage(Context context, Button button, String name, String theme) {
        if (theme.equals("red")) {
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_dark_red_circle));
        } else if (theme.equals("green")) {
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_dark_green_circle));
        } else if (theme.equals("pink")) {
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_dark_pink_circle));
        } else if (theme.equals("blue")) {
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_dark_blue_circle));
        } else if (theme.equals("purple")) {
            button.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_dark_purple_circle));
        }

        button.setText(name);
        button.setTextSize(16F);
        button.setTextColor(Color.WHITE);
        button.setPadding(5, 5, 5, 5);
    }


    public void setBackgroundDialog(TextView textView, String theme) {
        if (theme.equals("green")) {
            textView.setBackgroundResource(R.color.green);

        } else if (theme.equals("blue")) {
            textView.setBackgroundResource(R.color.blue);

        } else if (theme.equals("purple")) {
            textView.setBackgroundResource(R.color.purple);

        } else if (theme.equals("pink")) {
            textView.setBackgroundResource(R.color.pink);

        } else {
            textView.setBackgroundResource(R.color.colorPrimary);

        }
    }

    public int getDatasetColor(String theme) {
        int color;
        if (theme.equals("green")) {
            color= R.color.green;

        } else if (theme.equals("blue")) {
            color= R.color.blue;

        } else if (theme.equals("purple")) {
            color= R.color.purple;
        } else if (theme.equals("pink")) {
            color= R.color.pink;

        } else {
            color= R.color.colorPrimary;
        }
        return color;
    }



    public void setTextColorDialog(Context context,TextView textView, String theme) {
        if (theme.equals("green")) {
            textView.setTextColor(context.getResources().getColor((R.color.green)));

        } else if (theme.equals("blue")) {
            textView.setTextColor(context.getResources().getColor((R.color.blue)));


        } else if (theme.equals("purple")) {
            textView.setTextColor(context.getResources().getColor((R.color.purple)));


        } else if (theme.equals("pink")) {
            textView.setTextColor(context.getResources().getColor((R.color.pink)));


        } else {
            textView.setTextColor(context.getResources().getColor((R.color.colorPrimary)));


        }
    }
    public String getFirstChar(String name) {
        return String.valueOf(name.charAt(0));
    }

    public String getUnitFormat(String whhUnit, String unitType) {
        String unitFormatted = "";
        if (whhUnit.equals("cm-kg")) {

            if (unitType.equals("weight")) {
                unitFormatted = "kg";
            } else {
                unitFormatted = "cm";
            }

        } else {
            if (unitType.equals("weight")) {
                unitFormatted = "lb";
            } else {
                unitFormatted = "in";
            }

        }

        return unitFormatted;
    }


}
