package com.riagon.babydiary;


import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.text.BoringLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Growth;
import com.riagon.babydiary.Model.GrowthStandard;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.Utility.Calculator;
import com.riagon.babydiary.Utility.DateFormatUtility;
import com.riagon.babydiary.Utility.FormHelper;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.SettingHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WeightFragment extends Fragment implements OnChartValueSelectedListener {
    LineChart chart;
    private TextView ageTextview, weightTextview, percentTextview;
    private List<Growth> growthsList = new ArrayList<>();
    private DatabaseHelper db;
    private FormHelper formHelper;
    private SettingHelper settingHelper;
    private DateFormatUtility dateFormatUtility;
    private User currentUser;
    private LocalDataHelper localDataHelper;
    private String whhUnit = "";
    private String sex = "";
    private List<GrowthStandard> growthsStandardListMan = new ArrayList<>();
    private List<GrowthStandard> growthsStandardListWoman = new ArrayList<>();
    private List<GrowthStandard> growthsStandardList = new ArrayList<>();
    private Boolean isDarkMode = false;
    // private List<GrowthStandard> growthsStandardList15 = new ArrayList<>();

    public WeightFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_weight, container, false);
        chart = view.findViewById(R.id.chart);

        db = new DatabaseHelper(getContext());
        formHelper = new FormHelper(getContext());
        localDataHelper = new LocalDataHelper(getContext());
        settingHelper = new SettingHelper(getContext());
        currentUser = db.getUser(localDataHelper.getActiveUserId());
        dateFormatUtility = new DateFormatUtility(getContext());
        whhUnit = localDataHelper.getWhhUnit();
        sex = currentUser.getUserSex();
        isDarkMode = localDataHelper.getDarkMode();

        ageTextview = (TextView) view.findViewById(R.id.user_age);
        weightTextview = (TextView) view.findViewById(R.id.weight);
        percentTextview = view.findViewById(R.id.user_percent_range);

        setGrowthStandard();

        showChart("1Y");
        //  final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.chart_layout);

        RadioGroup rg = (RadioGroup) view.findViewById(R.id.radioGroup1);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radioButton_weight_1y:

                        showChart("1Y");
                        break;
                    case R.id.radioButton_weight_2y:

                        showChart("2Y");

                        break;
                    case R.id.radioButton_weight_3y:

                        showChart("3Y");

                        break;
                    case R.id.radioButton_weight_4y:

                        showChart("4Y");

                        break;
                    case R.id.radioButton_weight_5y:

                        showChart("5Y");
                        break;
                }
            }
        });
        return view;
    }

    public void setGrowthStandard() {
        GrowthStandard growth0 = new GrowthStandard(0, convertWSDBU(2.5, whhUnit), convertWSDBU(2.9, whhUnit), convertWSDBU(3.3, whhUnit), convertWSDBU(3.9, whhUnit), convertWSDBU(4.3, whhUnit));
        GrowthStandard growth1 = new GrowthStandard(1, convertWSDBU(3.4, whhUnit), convertWSDBU(3.9, whhUnit), convertWSDBU(4.5, whhUnit), convertWSDBU(5.1, whhUnit), convertWSDBU(5.7, whhUnit));
        GrowthStandard growth2 = new GrowthStandard(2, convertWSDBU(4.4, whhUnit), convertWSDBU(4.9, whhUnit), convertWSDBU(5.6, whhUnit), convertWSDBU(6.3, whhUnit), convertWSDBU(7.0, whhUnit));
        GrowthStandard growth3 = new GrowthStandard(3, convertWSDBU(5.1, whhUnit), convertWSDBU(5.6, whhUnit), convertWSDBU(6.4, whhUnit), convertWSDBU(7.2, whhUnit), convertWSDBU(7.9, whhUnit));
        GrowthStandard growth4 = new GrowthStandard(4, convertWSDBU(5.6, whhUnit), convertWSDBU(6.2, whhUnit), convertWSDBU(7.0, whhUnit), convertWSDBU(7.9, whhUnit), convertWSDBU(8.6, whhUnit));
        GrowthStandard growth5 = new GrowthStandard(5, convertWSDBU(6.1, whhUnit), convertWSDBU(6.7, whhUnit), convertWSDBU(7.5, whhUnit), convertWSDBU(8.4, whhUnit), convertWSDBU(9.2, whhUnit));
        GrowthStandard growth6 = new GrowthStandard(6, convertWSDBU(6.4, whhUnit), convertWSDBU(7.1, whhUnit), convertWSDBU(7.9, whhUnit), convertWSDBU(8.9, whhUnit), convertWSDBU(9.7, whhUnit));
        GrowthStandard growth7 = new GrowthStandard(7, convertWSDBU(6.7, whhUnit), convertWSDBU(7.4, whhUnit), convertWSDBU(8.3, whhUnit), convertWSDBU(9.3, whhUnit), convertWSDBU(10.2, whhUnit));
        GrowthStandard growth8 = new GrowthStandard(8, convertWSDBU(7.0, whhUnit), convertWSDBU(7.7, whhUnit), convertWSDBU(8.6, whhUnit), convertWSDBU(9.6, whhUnit), convertWSDBU(10.5, whhUnit));
        GrowthStandard growth9 = new GrowthStandard(9, convertWSDBU(7.2, whhUnit), convertWSDBU(7.9, whhUnit), convertWSDBU(8.9, whhUnit), convertWSDBU(10.0, whhUnit), convertWSDBU(10.9, whhUnit));
        GrowthStandard growth10 = new GrowthStandard(10, convertWSDBU(7.5, whhUnit), convertWSDBU(8.2, whhUnit), convertWSDBU(9.2, whhUnit), convertWSDBU(10.3, whhUnit), convertWSDBU(11.2, whhUnit));
        GrowthStandard growth11 = new GrowthStandard(11, convertWSDBU(7.7, whhUnit), convertWSDBU(8.4, whhUnit), convertWSDBU(9.4, whhUnit), convertWSDBU(10.5, whhUnit), convertWSDBU(11.5, whhUnit));
        GrowthStandard growth12 = new GrowthStandard(12, convertWSDBU(7.8, whhUnit), convertWSDBU(8.6, whhUnit), convertWSDBU(9.6, whhUnit), convertWSDBU(10.8, whhUnit), convertWSDBU(11.8, whhUnit));

        GrowthStandard growth13 = new GrowthStandard(13, convertWSDBU(8.0, whhUnit), convertWSDBU(8.8, whhUnit), convertWSDBU(9.9, whhUnit), convertWSDBU(11.1, whhUnit), convertWSDBU(12.1, whhUnit));
        GrowthStandard growth14 = new GrowthStandard(14, convertWSDBU(8.2, whhUnit), convertWSDBU(9.0, whhUnit), convertWSDBU(10.1, whhUnit), convertWSDBU(11.3, whhUnit), convertWSDBU(12.4, whhUnit));
        GrowthStandard growth15 = new GrowthStandard(15, convertWSDBU(8.4, whhUnit), convertWSDBU(9.2, whhUnit), convertWSDBU(10.3, whhUnit), convertWSDBU(11.6, whhUnit), convertWSDBU(12.7, whhUnit));
        GrowthStandard growth16 = new GrowthStandard(16, convertWSDBU(8.5, whhUnit), convertWSDBU(9.4, whhUnit), convertWSDBU(10.5, whhUnit), convertWSDBU(11.8, whhUnit), convertWSDBU(12.9, whhUnit));
        GrowthStandard growth17 = new GrowthStandard(17, convertWSDBU(8.7, whhUnit), convertWSDBU(9.6, whhUnit), convertWSDBU(10.7, whhUnit), convertWSDBU(12.0, whhUnit), convertWSDBU(13.2, whhUnit));
        GrowthStandard growth18 = new GrowthStandard(18, convertWSDBU(8.9, whhUnit), convertWSDBU(9.7, whhUnit), convertWSDBU(10.9, whhUnit), convertWSDBU(12.3, whhUnit), convertWSDBU(13.5, whhUnit));
        GrowthStandard growth19 = new GrowthStandard(19, convertWSDBU(9.0, whhUnit), convertWSDBU(9.9, whhUnit), convertWSDBU(11.1, whhUnit), convertWSDBU(12.5, whhUnit), convertWSDBU(13.7, whhUnit));
        GrowthStandard growth20 = new GrowthStandard(20, convertWSDBU(9.2, whhUnit), convertWSDBU(10.1, whhUnit), convertWSDBU(11.3, whhUnit), convertWSDBU(12.7, whhUnit), convertWSDBU(14.0, whhUnit));
        GrowthStandard growth21 = new GrowthStandard(21, convertWSDBU(9.3, whhUnit), convertWSDBU(10.3, whhUnit), convertWSDBU(11.5, whhUnit), convertWSDBU(13.0, whhUnit), convertWSDBU(14.3, whhUnit));
        GrowthStandard growth22 = new GrowthStandard(22, convertWSDBU(9.5, whhUnit), convertWSDBU(10.5, whhUnit), convertWSDBU(11.8, whhUnit), convertWSDBU(13.2, whhUnit), convertWSDBU(14.5, whhUnit));
        GrowthStandard growth23 = new GrowthStandard(23, convertWSDBU(9.7, whhUnit), convertWSDBU(10.6, whhUnit), convertWSDBU(12.0, whhUnit), convertWSDBU(13.4, whhUnit), convertWSDBU(14.8, whhUnit));
        GrowthStandard growth24 = new GrowthStandard(24, convertWSDBU(9.8, whhUnit), convertWSDBU(10.8, whhUnit), convertWSDBU(12.2, whhUnit), convertWSDBU(13.7, whhUnit), convertWSDBU(15.1, whhUnit));

        GrowthStandard growth25 = new GrowthStandard(25, convertWSDBU(10.0, whhUnit), convertWSDBU(11.0, whhUnit), convertWSDBU(12.4, whhUnit), convertWSDBU(13.9, whhUnit), convertWSDBU(15.3, whhUnit));
        GrowthStandard growth26 = new GrowthStandard(26, convertWSDBU(10.1, whhUnit), convertWSDBU(11.1, whhUnit), convertWSDBU(12.5, whhUnit), convertWSDBU(14.1, whhUnit), convertWSDBU(15.6, whhUnit));
        GrowthStandard growth27 = new GrowthStandard(27, convertWSDBU(10.2, whhUnit), convertWSDBU(11.3, whhUnit), convertWSDBU(12.7, whhUnit), convertWSDBU(14.4, whhUnit), convertWSDBU(15.9, whhUnit));
        GrowthStandard growth28 = new GrowthStandard(28, convertWSDBU(10.4, whhUnit), convertWSDBU(11.5, whhUnit), convertWSDBU(12.9, whhUnit), convertWSDBU(14.6, whhUnit), convertWSDBU(16.1, whhUnit));
        GrowthStandard growth29 = new GrowthStandard(29, convertWSDBU(10.5, whhUnit), convertWSDBU(11.6, whhUnit), convertWSDBU(13.1, whhUnit), convertWSDBU(14.8, whhUnit), convertWSDBU(16.4, whhUnit));
        GrowthStandard growth30 = new GrowthStandard(30, convertWSDBU(10.7, whhUnit), convertWSDBU(11.8, whhUnit), convertWSDBU(13.3, whhUnit), convertWSDBU(15.0, whhUnit), convertWSDBU(16.6, whhUnit));
        GrowthStandard growth31 = new GrowthStandard(31, convertWSDBU(10.8, whhUnit), convertWSDBU(11.9, whhUnit), convertWSDBU(13.5, whhUnit), convertWSDBU(15.2, whhUnit), convertWSDBU(16.9, whhUnit));
        GrowthStandard growth32 = new GrowthStandard(32, convertWSDBU(10.9, whhUnit), convertWSDBU(12.1, whhUnit), convertWSDBU(13.7, whhUnit), convertWSDBU(15.5, whhUnit), convertWSDBU(17.1, whhUnit));
        GrowthStandard growth33 = new GrowthStandard(33, convertWSDBU(11.1, whhUnit), convertWSDBU(12.2, whhUnit), convertWSDBU(13.8, whhUnit), convertWSDBU(15.7, whhUnit), convertWSDBU(17.3, whhUnit));
        GrowthStandard growth34 = new GrowthStandard(34, convertWSDBU(11.2, whhUnit), convertWSDBU(12.4, whhUnit), convertWSDBU(14.0, whhUnit), convertWSDBU(15.9, whhUnit), convertWSDBU(17.6, whhUnit));
        GrowthStandard growth35 = new GrowthStandard(35, convertWSDBU(11.3, whhUnit), convertWSDBU(12.5, whhUnit), convertWSDBU(14.2, whhUnit), convertWSDBU(16.1, whhUnit), convertWSDBU(17.8, whhUnit));
        GrowthStandard growth36 = new GrowthStandard(36, convertWSDBU(11.4, whhUnit), convertWSDBU(12.7, whhUnit), convertWSDBU(14.3, whhUnit), convertWSDBU(16.3, whhUnit), convertWSDBU(18.0, whhUnit));

        GrowthStandard growth37 = new GrowthStandard(37, convertWSDBU(11.6, whhUnit), convertWSDBU(12.8, whhUnit), convertWSDBU(14.5, whhUnit), convertWSDBU(16.5, whhUnit), convertWSDBU(18.3, whhUnit));
        GrowthStandard growth38 = new GrowthStandard(38, convertWSDBU(11.7, whhUnit), convertWSDBU(12.9, whhUnit), convertWSDBU(14.7, whhUnit), convertWSDBU(16.7, whhUnit), convertWSDBU(18.5, whhUnit));
        GrowthStandard growth39 = new GrowthStandard(39, convertWSDBU(11.8, whhUnit), convertWSDBU(13.1, whhUnit), convertWSDBU(14.8, whhUnit), convertWSDBU(16.9, whhUnit), convertWSDBU(18.7, whhUnit));
        GrowthStandard growth40 = new GrowthStandard(40, convertWSDBU(11.9, whhUnit), convertWSDBU(13.2, whhUnit), convertWSDBU(15.0, whhUnit), convertWSDBU(17.1, whhUnit), convertWSDBU(19.0, whhUnit));
        GrowthStandard growth41 = new GrowthStandard(41, convertWSDBU(12.1, whhUnit), convertWSDBU(13.4, whhUnit), convertWSDBU(15.2, whhUnit), convertWSDBU(17.3, whhUnit), convertWSDBU(19.2, whhUnit));
        GrowthStandard growth42 = new GrowthStandard(42, convertWSDBU(12.2, whhUnit), convertWSDBU(13.5, whhUnit), convertWSDBU(15.3, whhUnit), convertWSDBU(17.5, whhUnit), convertWSDBU(19.4, whhUnit));
        GrowthStandard growth43 = new GrowthStandard(43, convertWSDBU(12.3, whhUnit), convertWSDBU(13.6, whhUnit), convertWSDBU(15.5, whhUnit), convertWSDBU(17.7, whhUnit), convertWSDBU(19.7, whhUnit));
        GrowthStandard growth44 = new GrowthStandard(44, convertWSDBU(12.4, whhUnit), convertWSDBU(13.8, whhUnit), convertWSDBU(15.7, whhUnit), convertWSDBU(17.9, whhUnit), convertWSDBU(19.9, whhUnit));
        GrowthStandard growth45 = new GrowthStandard(45, convertWSDBU(12.5, whhUnit), convertWSDBU(13.9, whhUnit), convertWSDBU(15.8, whhUnit), convertWSDBU(18.1, whhUnit), convertWSDBU(20.1, whhUnit));
        GrowthStandard growth46 = new GrowthStandard(46, convertWSDBU(12.7, whhUnit), convertWSDBU(14.1, whhUnit), convertWSDBU(16.0, whhUnit), convertWSDBU(18.3, whhUnit), convertWSDBU(20.4, whhUnit));
        GrowthStandard growth47 = new GrowthStandard(47, convertWSDBU(12.8, whhUnit), convertWSDBU(14.2, whhUnit), convertWSDBU(16.2, whhUnit), convertWSDBU(18.5, whhUnit), convertWSDBU(20.6, whhUnit));
        GrowthStandard growth48 = new GrowthStandard(48, convertWSDBU(12.9, whhUnit), convertWSDBU(14.3, whhUnit), convertWSDBU(16.3, whhUnit), convertWSDBU(18.7, whhUnit), convertWSDBU(20.9, whhUnit));

        GrowthStandard growth49 = new GrowthStandard(49, convertWSDBU(13.0, whhUnit), convertWSDBU(14.5, whhUnit), convertWSDBU(16.5, whhUnit), convertWSDBU(18.9, whhUnit), convertWSDBU(21.1, whhUnit));
        GrowthStandard growth50 = new GrowthStandard(50, convertWSDBU(13.1, whhUnit), convertWSDBU(14.6, whhUnit), convertWSDBU(16.7, whhUnit), convertWSDBU(19.1, whhUnit), convertWSDBU(21.3, whhUnit));
        GrowthStandard growth51 = new GrowthStandard(51, convertWSDBU(13.3, whhUnit), convertWSDBU(14.7, whhUnit), convertWSDBU(16.8, whhUnit), convertWSDBU(19.3, whhUnit), convertWSDBU(21.6, whhUnit));
        GrowthStandard growth52 = new GrowthStandard(52, convertWSDBU(13.4, whhUnit), convertWSDBU(14.9, whhUnit), convertWSDBU(17.0, whhUnit), convertWSDBU(19.5, whhUnit), convertWSDBU(21.8, whhUnit));
        GrowthStandard growth53 = new GrowthStandard(53, convertWSDBU(13.5, whhUnit), convertWSDBU(15.0, whhUnit), convertWSDBU(17.2, whhUnit), convertWSDBU(19.7, whhUnit), convertWSDBU(22.1, whhUnit));
        GrowthStandard growth54 = new GrowthStandard(54, convertWSDBU(13.6, whhUnit), convertWSDBU(15.2, whhUnit), convertWSDBU(17.3, whhUnit), convertWSDBU(19.9, whhUnit), convertWSDBU(22.3, whhUnit));
        GrowthStandard growth55 = new GrowthStandard(55, convertWSDBU(13.7, whhUnit), convertWSDBU(15.3, whhUnit), convertWSDBU(17.5, whhUnit), convertWSDBU(20.1, whhUnit), convertWSDBU(22.5, whhUnit));
        GrowthStandard growth56 = new GrowthStandard(56, convertWSDBU(13.8, whhUnit), convertWSDBU(15.4, whhUnit), convertWSDBU(17.7, whhUnit), convertWSDBU(20.3, whhUnit), convertWSDBU(22.8, whhUnit));
        GrowthStandard growth57 = new GrowthStandard(57, convertWSDBU(13.9, whhUnit), convertWSDBU(15.6, whhUnit), convertWSDBU(17.8, whhUnit), convertWSDBU(20.5, whhUnit), convertWSDBU(23.0, whhUnit));
        GrowthStandard growth58 = new GrowthStandard(58, convertWSDBU(14.1, whhUnit), convertWSDBU(15.7, whhUnit), convertWSDBU(18.0, whhUnit), convertWSDBU(20.7, whhUnit), convertWSDBU(23.3, whhUnit));
        GrowthStandard growth59 = new GrowthStandard(59, convertWSDBU(14.2, whhUnit), convertWSDBU(15.8, whhUnit), convertWSDBU(18.2, whhUnit), convertWSDBU(20.9, whhUnit), convertWSDBU(23.5, whhUnit));
        GrowthStandard growth60 = new GrowthStandard(60, convertWSDBU(14.3, whhUnit), convertWSDBU(16.0, whhUnit), convertWSDBU(18.3, whhUnit), convertWSDBU(21.1, whhUnit), convertWSDBU(23.8, whhUnit));


        growthsStandardListMan.clear();
        growthsStandardListMan.add(growth0);
        growthsStandardListMan.add(growth1);
        growthsStandardListMan.add(growth2);
        growthsStandardListMan.add(growth3);
        growthsStandardListMan.add(growth4);
        growthsStandardListMan.add(growth5);
        growthsStandardListMan.add(growth6);
        growthsStandardListMan.add(growth7);
        growthsStandardListMan.add(growth8);
        growthsStandardListMan.add(growth9);
        growthsStandardListMan.add(growth10);
        growthsStandardListMan.add(growth11);
        growthsStandardListMan.add(growth12);
        growthsStandardListMan.add(growth13);
        growthsStandardListMan.add(growth14);
        growthsStandardListMan.add(growth15);
        growthsStandardListMan.add(growth16);
        growthsStandardListMan.add(growth17);
        growthsStandardListMan.add(growth18);
        growthsStandardListMan.add(growth19);
        growthsStandardListMan.add(growth20);
        growthsStandardListMan.add(growth21);
        growthsStandardListMan.add(growth22);
        growthsStandardListMan.add(growth23);
        growthsStandardListMan.add(growth24);
        growthsStandardListMan.add(growth25);
        growthsStandardListMan.add(growth26);
        growthsStandardListMan.add(growth27);
        growthsStandardListMan.add(growth28);
        growthsStandardListMan.add(growth29);
        growthsStandardListMan.add(growth30);
        growthsStandardListMan.add(growth31);
        growthsStandardListMan.add(growth32);
        growthsStandardListMan.add(growth33);
        growthsStandardListMan.add(growth34);
        growthsStandardListMan.add(growth35);
        growthsStandardListMan.add(growth36);
        growthsStandardListMan.add(growth37);
        growthsStandardListMan.add(growth38);
        growthsStandardListMan.add(growth39);
        growthsStandardListMan.add(growth40);
        growthsStandardListMan.add(growth41);
        growthsStandardListMan.add(growth42);
        growthsStandardListMan.add(growth43);
        growthsStandardListMan.add(growth44);
        growthsStandardListMan.add(growth45);
        growthsStandardListMan.add(growth46);
        growthsStandardListMan.add(growth47);
        growthsStandardListMan.add(growth48);
        growthsStandardListMan.add(growth49);
        growthsStandardListMan.add(growth50);
        growthsStandardListMan.add(growth51);
        growthsStandardListMan.add(growth52);
        growthsStandardListMan.add(growth53);
        growthsStandardListMan.add(growth54);
        growthsStandardListMan.add(growth55);
        growthsStandardListMan.add(growth56);
        growthsStandardListMan.add(growth57);
        growthsStandardListMan.add(growth58);
        growthsStandardListMan.add(growth59);
        growthsStandardListMan.add(growth60);


        //woman
        GrowthStandard growth0Woman = new GrowthStandard(0, convertWSDBU(2.4, whhUnit), convertWSDBU(2.8, whhUnit), convertWSDBU(3.2, whhUnit), convertWSDBU(3.7, whhUnit), convertWSDBU(4.2, whhUnit));
        GrowthStandard growth1Woman = new GrowthStandard(1, convertWSDBU(3.2, whhUnit), convertWSDBU(3.6, whhUnit), convertWSDBU(4.2, whhUnit), convertWSDBU(4.8, whhUnit), convertWSDBU(5.4, whhUnit));
        GrowthStandard growth2Woman = new GrowthStandard(2, convertWSDBU(4.0, whhUnit), convertWSDBU(4.5, whhUnit), convertWSDBU(5.1, whhUnit), convertWSDBU(5.9, whhUnit), convertWSDBU(6.5, whhUnit));
        GrowthStandard growth3Woman = new GrowthStandard(3, convertWSDBU(4.6, whhUnit), convertWSDBU(5.1, whhUnit), convertWSDBU(5.8, whhUnit), convertWSDBU(6.7, whhUnit), convertWSDBU(7.4, whhUnit));
        GrowthStandard growth4Woman = new GrowthStandard(4, convertWSDBU(5.1, whhUnit), convertWSDBU(5.6, whhUnit), convertWSDBU(6.4, whhUnit), convertWSDBU(7.3, whhUnit), convertWSDBU(8.1, whhUnit));
        GrowthStandard growth5Woman = new GrowthStandard(5, convertWSDBU(5.5, whhUnit), convertWSDBU(6.1, whhUnit), convertWSDBU(6.9, whhUnit), convertWSDBU(7.8, whhUnit), convertWSDBU(8.7, whhUnit));
        GrowthStandard growth6Woman = new GrowthStandard(6, convertWSDBU(5.8, whhUnit), convertWSDBU(6.4, whhUnit), convertWSDBU(7.3, whhUnit), convertWSDBU(8.3, whhUnit), convertWSDBU(9.2, whhUnit));
        GrowthStandard growth7Woman = new GrowthStandard(7, convertWSDBU(6.1, whhUnit), convertWSDBU(6.7, whhUnit), convertWSDBU(7.6, whhUnit), convertWSDBU(8.7, whhUnit), convertWSDBU(9.6, whhUnit));
        GrowthStandard growth8Woman = new GrowthStandard(8, convertWSDBU(6.3, whhUnit), convertWSDBU(7.0, whhUnit), convertWSDBU(7.9, whhUnit), convertWSDBU(9.0, whhUnit), convertWSDBU(10.0, whhUnit));
        GrowthStandard growth9Woman = new GrowthStandard(9, convertWSDBU(6.6, whhUnit), convertWSDBU(7.3, whhUnit), convertWSDBU(8.2, whhUnit), convertWSDBU(9.3, whhUnit), convertWSDBU(10.4, whhUnit));
        GrowthStandard growth10Woman = new GrowthStandard(10, convertWSDBU(6.8, whhUnit), convertWSDBU(7.5, whhUnit), convertWSDBU(8.5, whhUnit), convertWSDBU(9.6, whhUnit), convertWSDBU(10.7, whhUnit));
        GrowthStandard growth11Woman = new GrowthStandard(11, convertWSDBU(7.0, whhUnit), convertWSDBU(7.7, whhUnit), convertWSDBU(8.7, whhUnit), convertWSDBU(9.9, whhUnit), convertWSDBU(11.0, whhUnit));
        GrowthStandard growth12Woman = new GrowthStandard(12, convertWSDBU(7.1, whhUnit), convertWSDBU(7.9, whhUnit), convertWSDBU(8.9, whhUnit), convertWSDBU(10.2, whhUnit), convertWSDBU(11.3, whhUnit));

        GrowthStandard growth13Woman = new GrowthStandard(13, convertWSDBU(7.3, whhUnit), convertWSDBU(8.1, whhUnit), convertWSDBU(9.2, whhUnit), convertWSDBU(10.4, whhUnit), convertWSDBU(11.6, whhUnit));
        GrowthStandard growth14Woman = new GrowthStandard(14, convertWSDBU(7.5, whhUnit), convertWSDBU(8.3, whhUnit), convertWSDBU(9.4, whhUnit), convertWSDBU(10.7, whhUnit), convertWSDBU(11.9, whhUnit));
        GrowthStandard growth15Woman = new GrowthStandard(15, convertWSDBU(7.7, whhUnit), convertWSDBU(8.5, whhUnit), convertWSDBU(9.6, whhUnit), convertWSDBU(10.9, whhUnit), convertWSDBU(12.2, whhUnit));
        GrowthStandard growth16Woman = new GrowthStandard(16, convertWSDBU(7.8, whhUnit), convertWSDBU(8.7, whhUnit), convertWSDBU(9.8, whhUnit), convertWSDBU(11.2, whhUnit), convertWSDBU(12.5, whhUnit));
        GrowthStandard growth17Woman = new GrowthStandard(17, convertWSDBU(8.0, whhUnit), convertWSDBU(8.8, whhUnit), convertWSDBU(10.0, whhUnit), convertWSDBU(11.4, whhUnit), convertWSDBU(12.7, whhUnit));
        GrowthStandard growth18Woman = new GrowthStandard(18, convertWSDBU(8.2, whhUnit), convertWSDBU(9.0, whhUnit), convertWSDBU(10.2, whhUnit), convertWSDBU(11.6, whhUnit), convertWSDBU(13.0, whhUnit));
        GrowthStandard growth19Woman = new GrowthStandard(19, convertWSDBU(8.3, whhUnit), convertWSDBU(9.2, whhUnit), convertWSDBU(10.4, whhUnit), convertWSDBU(11.9, whhUnit), convertWSDBU(13.3, whhUnit));
        GrowthStandard growth20Woman = new GrowthStandard(20, convertWSDBU(8.5, whhUnit), convertWSDBU(9.4, whhUnit), convertWSDBU(10.6, whhUnit), convertWSDBU(12.1, whhUnit), convertWSDBU(13.5, whhUnit));
        GrowthStandard growth21Woman = new GrowthStandard(21, convertWSDBU(8.7, whhUnit), convertWSDBU(9.6, whhUnit), convertWSDBU(10.9, whhUnit), convertWSDBU(12.4, whhUnit), convertWSDBU(13.8, whhUnit));
        GrowthStandard growth22Woman = new GrowthStandard(22, convertWSDBU(8.8, whhUnit), convertWSDBU(9.8, whhUnit), convertWSDBU(11.1, whhUnit), convertWSDBU(12.6, whhUnit), convertWSDBU(14.1, whhUnit));
        GrowthStandard growth23Woman = new GrowthStandard(23, convertWSDBU(9.0, whhUnit), convertWSDBU(9.9, whhUnit), convertWSDBU(11.3, whhUnit), convertWSDBU(12.8, whhUnit), convertWSDBU(14.3, whhUnit));
        GrowthStandard growth24Woman = new GrowthStandard(24, convertWSDBU(9.2, whhUnit), convertWSDBU(10.1, whhUnit), convertWSDBU(11.5, whhUnit), convertWSDBU(13.1, whhUnit), convertWSDBU(14.6, whhUnit));

        GrowthStandard growth25Woman = new GrowthStandard(25, convertWSDBU(9.3, whhUnit), convertWSDBU(10.3, whhUnit), convertWSDBU(11.7, whhUnit), convertWSDBU(13.3, whhUnit), convertWSDBU(14.9, whhUnit));
        GrowthStandard growth26Woman = new GrowthStandard(26, convertWSDBU(9.5, whhUnit), convertWSDBU(10.5, whhUnit), convertWSDBU(11.9, whhUnit), convertWSDBU(13.6, whhUnit), convertWSDBU(15.2, whhUnit));
        GrowthStandard growth27Woman = new GrowthStandard(27, convertWSDBU(9.6, whhUnit), convertWSDBU(10.7, whhUnit), convertWSDBU(12.1, whhUnit), convertWSDBU(13.8, whhUnit), convertWSDBU(15.4, whhUnit));
        GrowthStandard growth28Woman = new GrowthStandard(28, convertWSDBU(9.8, whhUnit), convertWSDBU(10.8, whhUnit), convertWSDBU(12.3, whhUnit), convertWSDBU(14.0, whhUnit), convertWSDBU(15.7, whhUnit));
        GrowthStandard growth29Woman = new GrowthStandard(29, convertWSDBU(10.0, whhUnit), convertWSDBU(11.0, whhUnit), convertWSDBU(12.5, whhUnit), convertWSDBU(14.3, whhUnit), convertWSDBU(16.0, whhUnit));
        GrowthStandard growth30Woman = new GrowthStandard(30, convertWSDBU(10.1, whhUnit), convertWSDBU(11.2, whhUnit), convertWSDBU(12.7, whhUnit), convertWSDBU(14.5, whhUnit), convertWSDBU(16.2, whhUnit));
        GrowthStandard growth31Woman = new GrowthStandard(31, convertWSDBU(10.3, whhUnit), convertWSDBU(11.3, whhUnit), convertWSDBU(12.9, whhUnit), convertWSDBU(14.7, whhUnit), convertWSDBU(16.5, whhUnit));
        GrowthStandard growth32Woman = new GrowthStandard(32, convertWSDBU(10.4, whhUnit), convertWSDBU(11.5, whhUnit), convertWSDBU(13.1, whhUnit), convertWSDBU(15.0, whhUnit), convertWSDBU(16.8, whhUnit));
        GrowthStandard growth33Woman = new GrowthStandard(33, convertWSDBU(10.5, whhUnit), convertWSDBU(11.7, whhUnit), convertWSDBU(13.3, whhUnit), convertWSDBU(15.2, whhUnit), convertWSDBU(17.0, whhUnit));
        GrowthStandard growth34Woman = new GrowthStandard(34, convertWSDBU(10.7, whhUnit), convertWSDBU(11.8, whhUnit), convertWSDBU(13.5, whhUnit), convertWSDBU(15.4, whhUnit), convertWSDBU(17.3, whhUnit));
        GrowthStandard growth35Woman = new GrowthStandard(35, convertWSDBU(10.8, whhUnit), convertWSDBU(12.0, whhUnit), convertWSDBU(13.7, whhUnit), convertWSDBU(15.7, whhUnit), convertWSDBU(17.6, whhUnit));
        GrowthStandard growth36Woman = new GrowthStandard(36, convertWSDBU(11.0, whhUnit), convertWSDBU(12.1, whhUnit), convertWSDBU(13.9, whhUnit), convertWSDBU(15.9, whhUnit), convertWSDBU(17.8, whhUnit));

        GrowthStandard growth37Woman = new GrowthStandard(37, convertWSDBU(11.1, whhUnit), convertWSDBU(12.3, whhUnit), convertWSDBU(14.0, whhUnit), convertWSDBU(16.1, whhUnit), convertWSDBU(18.1, whhUnit));
        GrowthStandard growth38Woman = new GrowthStandard(38, convertWSDBU(11.2, whhUnit), convertWSDBU(12.5, whhUnit), convertWSDBU(14.2, whhUnit), convertWSDBU(16.3, whhUnit), convertWSDBU(18.4, whhUnit));
        GrowthStandard growth39Woman = new GrowthStandard(39, convertWSDBU(11.4, whhUnit), convertWSDBU(12.6, whhUnit), convertWSDBU(14.4, whhUnit), convertWSDBU(16.6, whhUnit), convertWSDBU(18.6, whhUnit));
        GrowthStandard growth40Woman = new GrowthStandard(40, convertWSDBU(11.5, whhUnit), convertWSDBU(12.8, whhUnit), convertWSDBU(14.6, whhUnit), convertWSDBU(16.8, whhUnit), convertWSDBU(18.9, whhUnit));
        GrowthStandard growth41Woman = new GrowthStandard(41, convertWSDBU(11.6, whhUnit), convertWSDBU(12.9, whhUnit), convertWSDBU(14.8, whhUnit), convertWSDBU(17.0, whhUnit), convertWSDBU(19.2, whhUnit));
        GrowthStandard growth42Woman = new GrowthStandard(42, convertWSDBU(11.8, whhUnit), convertWSDBU(13.1, whhUnit), convertWSDBU(15.0, whhUnit), convertWSDBU(17.3, whhUnit), convertWSDBU(19.5, whhUnit));
        GrowthStandard growth43Woman = new GrowthStandard(43, convertWSDBU(11.9, whhUnit), convertWSDBU(13.2, whhUnit), convertWSDBU(15.2, whhUnit), convertWSDBU(17.5, whhUnit), convertWSDBU(19.7, whhUnit));
        GrowthStandard growth44Woman = new GrowthStandard(44, convertWSDBU(12.0, whhUnit), convertWSDBU(13.4, whhUnit), convertWSDBU(15.3, whhUnit), convertWSDBU(17.7, whhUnit), convertWSDBU(20.0, whhUnit));
        GrowthStandard growth45Woman = new GrowthStandard(45, convertWSDBU(12.1, whhUnit), convertWSDBU(13.5, whhUnit), convertWSDBU(15.5, whhUnit), convertWSDBU(17.9, whhUnit), convertWSDBU(20.3, whhUnit));
        GrowthStandard growth46Woman = new GrowthStandard(46, convertWSDBU(12.3, whhUnit), convertWSDBU(13.7, whhUnit), convertWSDBU(15.7, whhUnit), convertWSDBU(18.2, whhUnit), convertWSDBU(20.6, whhUnit));
        GrowthStandard growth47Woman = new GrowthStandard(47, convertWSDBU(12.4, whhUnit), convertWSDBU(13.8, whhUnit), convertWSDBU(15.9, whhUnit), convertWSDBU(18.4, whhUnit), convertWSDBU(20.8, whhUnit));
        GrowthStandard growth48Woman = new GrowthStandard(48, convertWSDBU(12.5, whhUnit), convertWSDBU(14.0, whhUnit), convertWSDBU(16.1, whhUnit), convertWSDBU(18.6, whhUnit), convertWSDBU(21.1, whhUnit));

        GrowthStandard growth49Woman = new GrowthStandard(49, convertWSDBU(12.6, whhUnit), convertWSDBU(14.1, whhUnit), convertWSDBU(16.3, whhUnit), convertWSDBU(18.9, whhUnit), convertWSDBU(21.4, whhUnit));
        GrowthStandard growth50Woman = new GrowthStandard(50, convertWSDBU(12.8, whhUnit), convertWSDBU(14.3, whhUnit), convertWSDBU(16.4, whhUnit), convertWSDBU(19.1, whhUnit), convertWSDBU(21.7, whhUnit));
        GrowthStandard growth51Woman = new GrowthStandard(51, convertWSDBU(12.9, whhUnit), convertWSDBU(14.4, whhUnit), convertWSDBU(16.6, whhUnit), convertWSDBU(19.3, whhUnit), convertWSDBU(22.0, whhUnit));
        GrowthStandard growth52Woman = new GrowthStandard(52, convertWSDBU(13.0, whhUnit), convertWSDBU(14.5, whhUnit), convertWSDBU(16.8, whhUnit), convertWSDBU(19.5, whhUnit), convertWSDBU(22.2, whhUnit));
        GrowthStandard growth53Woman = new GrowthStandard(53, convertWSDBU(13.1, whhUnit), convertWSDBU(14.7, whhUnit), convertWSDBU(17.0, whhUnit), convertWSDBU(19.8, whhUnit), convertWSDBU(22.5, whhUnit));
        GrowthStandard growth54Woman = new GrowthStandard(54, convertWSDBU(13.2, whhUnit), convertWSDBU(14.8, whhUnit), convertWSDBU(17.2, whhUnit), convertWSDBU(20.0, whhUnit), convertWSDBU(22.8, whhUnit));
        GrowthStandard growth55Woman = new GrowthStandard(55, convertWSDBU(13.4, whhUnit), convertWSDBU(15.0, whhUnit), convertWSDBU(17.3, whhUnit), convertWSDBU(20.2, whhUnit), convertWSDBU(23.1, whhUnit));
        GrowthStandard growth56Woman = new GrowthStandard(56, convertWSDBU(13.5, whhUnit), convertWSDBU(15.1, whhUnit), convertWSDBU(17.5, whhUnit), convertWSDBU(20.4, whhUnit), convertWSDBU(23.3, whhUnit));
        GrowthStandard growth57Woman = new GrowthStandard(57, convertWSDBU(13.6, whhUnit), convertWSDBU(15.3, whhUnit), convertWSDBU(17.7, whhUnit), convertWSDBU(20.7, whhUnit), convertWSDBU(23.6, whhUnit));
        GrowthStandard growth58Woman = new GrowthStandard(58, convertWSDBU(13.7, whhUnit), convertWSDBU(15.4, whhUnit), convertWSDBU(17.9, whhUnit), convertWSDBU(20.9, whhUnit), convertWSDBU(23.9, whhUnit));
        GrowthStandard growth59Woman = new GrowthStandard(59, convertWSDBU(13.8, whhUnit), convertWSDBU(15.5, whhUnit), convertWSDBU(18.0, whhUnit), convertWSDBU(21.1, whhUnit), convertWSDBU(24.2, whhUnit));
        GrowthStandard growth60Woman = new GrowthStandard(60, convertWSDBU(14.0, whhUnit), convertWSDBU(15.7, whhUnit), convertWSDBU(18.2, whhUnit), convertWSDBU(21.3, whhUnit), convertWSDBU(24.4, whhUnit));

        growthsStandardListWoman.clear();

        growthsStandardListWoman.add(growth0Woman);
        growthsStandardListWoman.add(growth1Woman);
        growthsStandardListWoman.add(growth2Woman);
        growthsStandardListWoman.add(growth3Woman);
        growthsStandardListWoman.add(growth4Woman);
        growthsStandardListWoman.add(growth5Woman);
        growthsStandardListWoman.add(growth6Woman);
        growthsStandardListWoman.add(growth7Woman);
        growthsStandardListWoman.add(growth8Woman);
        growthsStandardListWoman.add(growth9Woman);
        growthsStandardListWoman.add(growth10Woman);
        growthsStandardListWoman.add(growth11Woman);
        growthsStandardListWoman.add(growth12Woman);
        growthsStandardListWoman.add(growth13Woman);
        growthsStandardListWoman.add(growth14Woman);
        growthsStandardListWoman.add(growth15Woman);
        growthsStandardListWoman.add(growth16Woman);
        growthsStandardListWoman.add(growth17Woman);
        growthsStandardListWoman.add(growth18Woman);
        growthsStandardListWoman.add(growth19Woman);
        growthsStandardListWoman.add(growth20Woman);
        growthsStandardListWoman.add(growth21Woman);
        growthsStandardListWoman.add(growth22Woman);
        growthsStandardListWoman.add(growth23Woman);
        growthsStandardListWoman.add(growth24Woman);
        growthsStandardListWoman.add(growth25Woman);
        growthsStandardListWoman.add(growth26Woman);
        growthsStandardListWoman.add(growth27Woman);
        growthsStandardListWoman.add(growth28Woman);
        growthsStandardListWoman.add(growth29Woman);
        growthsStandardListWoman.add(growth30Woman);
        growthsStandardListWoman.add(growth31Woman);
        growthsStandardListWoman.add(growth32Woman);
        growthsStandardListWoman.add(growth33Woman);
        growthsStandardListWoman.add(growth34Woman);
        growthsStandardListWoman.add(growth35Woman);
        growthsStandardListWoman.add(growth36Woman);
        growthsStandardListWoman.add(growth37Woman);
        growthsStandardListWoman.add(growth38Woman);
        growthsStandardListWoman.add(growth39Woman);
        growthsStandardListWoman.add(growth40Woman);
        growthsStandardListWoman.add(growth41Woman);
        growthsStandardListWoman.add(growth42Woman);
        growthsStandardListWoman.add(growth43Woman);
        growthsStandardListWoman.add(growth44Woman);
        growthsStandardListWoman.add(growth45Woman);
        growthsStandardListWoman.add(growth46Woman);
        growthsStandardListWoman.add(growth47Woman);
        growthsStandardListWoman.add(growth48Woman);
        growthsStandardListWoman.add(growth49Woman);
        growthsStandardListWoman.add(growth50Woman);
        growthsStandardListWoman.add(growth51Woman);
        growthsStandardListWoman.add(growth52Woman);
        growthsStandardListWoman.add(growth53Woman);
        growthsStandardListWoman.add(growth54Woman);
        growthsStandardListWoman.add(growth55Woman);
        growthsStandardListWoman.add(growth56Woman);
        growthsStandardListWoman.add(growth57Woman);
        growthsStandardListWoman.add(growth58Woman);
        growthsStandardListWoman.add(growth59Woman);
        growthsStandardListWoman.add(growth60Woman);
    }


    public double countPercent(double doubleMonth, double weight, String sex) {


//        for(int i=0;i<growthsStandardList3.size();i++)
//        {
        if (sex.equals("male")) {
            growthsStandardList.clear();
            growthsStandardList.addAll(growthsStandardListMan);
        } else {
            growthsStandardList.clear();
            growthsStandardList.addAll(growthsStandardListWoman);
        }

        //new criatial
        int month = (int) doubleMonth;
        double monthInProcess = doubleMonth - month;
//        Log.i("CHART TAG", "Sized: " + growthsStandardList.size());
//
//        Log.i("CHART TAG", "Input Month: " + doubleMonth);
//        Log.i("CHART TAG", "Input Weight: " + weight);
//
//        Log.i("CHART TAG", "Month: " + month);
//        Log.i("CHART TAG", "monthInProcess: " + monthInProcess);

        double changeAMonth3 = growthsStandardList.get(month + 1).getThreePercent() - growthsStandardList.get(month).getThreePercent();
        double changeAMonth15 = growthsStandardList.get(month + 1).getFifteenPercent() - growthsStandardList.get(month).getFifteenPercent();
        double changeAMonth50 = growthsStandardList.get(month + 1).getFiftyPercent() - growthsStandardList.get(month).getFiftyPercent();
        double changeAMonth85 = growthsStandardList.get(month + 1).getEightyFivePercent() - growthsStandardList.get(month).getEightyFivePercent();
        double changeAMonth97 = growthsStandardList.get(month + 1).getNinetySeventhPercent() - growthsStandardList.get(month).getNinetySeventhPercent();


        double percentResult = 0;


        if (weight <= (growthsStandardList.get(month).getThreePercent() + changeAMonth3 * monthInProcess)) {
            percentResult = 3;
        } else if (weight > (growthsStandardList.get(month).getThreePercent() + changeAMonth3 * monthInProcess) && weight <= (growthsStandardList.get(month).getFifteenPercent() + changeAMonth15 * monthInProcess)) {
            double change = (growthsStandardList.get(month).getFifteenPercent() + changeAMonth15 * monthInProcess) - (growthsStandardList.get(month).getThreePercent() + changeAMonth3 * monthInProcess);
            double percentChange = 12;
            double changePer1 = percentChange / (change);
            BigDecimal bd = new BigDecimal(changePer1).setScale(1, RoundingMode.HALF_UP);
            changePer1 = bd.doubleValue();


            percentResult = 3 + (weight - (growthsStandardList.get(month).getThreePercent() + changeAMonth3 * monthInProcess)) * changePer1;
        } else if (weight > (growthsStandardList.get(month).getFifteenPercent() + changeAMonth15 * monthInProcess) && weight <= (growthsStandardList.get(month).getFiftyPercent() + changeAMonth50 * monthInProcess)) {
            double change = (growthsStandardList.get(month).getFiftyPercent() + changeAMonth50 * monthInProcess) - (growthsStandardList.get(month).getFifteenPercent() + changeAMonth15 * monthInProcess);
            double percentChange = 35;
            double changePer1 = percentChange / change;
            BigDecimal bd = new BigDecimal(changePer1).setScale(1, RoundingMode.HALF_UP);
            changePer1 = bd.doubleValue();

            percentResult = 15 + (weight - (growthsStandardList.get(month).getFifteenPercent() + changeAMonth15 * monthInProcess)) * changePer1;
        } else if (weight > (growthsStandardList.get(month).getFiftyPercent() + changeAMonth50 * monthInProcess) && weight <= (growthsStandardList.get(month).getEightyFivePercent() + changeAMonth85 * monthInProcess)) {
            double change = (growthsStandardList.get(month).getEightyFivePercent() + changeAMonth85 * monthInProcess) - (growthsStandardList.get(month).getFiftyPercent() + changeAMonth50 * monthInProcess);
            double percentChange = 35;
            double changePer1 = percentChange / change;
            BigDecimal bd = new BigDecimal(changePer1).setScale(1, RoundingMode.HALF_UP);
            changePer1 = bd.doubleValue();

            percentResult = 50 + (weight - (growthsStandardList.get(month).getFiftyPercent() + changeAMonth50 * monthInProcess)) * changePer1;
        } else if (weight > (growthsStandardList.get(month).getEightyFivePercent() + changeAMonth85 * monthInProcess) && weight < (growthsStandardList.get(month).getNinetySeventhPercent() + changeAMonth97 * monthInProcess)) {
            double change = (growthsStandardList.get(month).getNinetySeventhPercent() + changeAMonth97 * monthInProcess) - (growthsStandardList.get(month).getEightyFivePercent() + changeAMonth85 * monthInProcess);
            double percentChange = 12;
            double changePer1 = percentChange / change;
//            BigDecimal bd = new BigDecimal(changePer1).setScale(1, RoundingMode.HALF_UP);
//            changePer1 = bd.doubleValue();

            percentResult = 85 + (weight - (growthsStandardList.get(month).getEightyFivePercent() + changeAMonth85 * monthInProcess)) * changePer1;
        } else if (weight >= (growthsStandardList.get(month).getNinetySeventhPercent() + changeAMonth97 * monthInProcess)) {
            percentResult = 97;
        }


        BigDecimal bd = new BigDecimal(percentResult).setScale(1, RoundingMode.HALF_UP);
        percentResult = bd.doubleValue();

//        Log.i("CHART TAG", "50 percent " + growthsStandardList.get(month).getFiftyPercent());
//        Log.i("CHART TAG", "50 percent " + (growthsStandardList.get(month).getFiftyPercent() + changeAMonth50 * monthInProcess));
//
//        Log.i("CHART TAG", "85 percent " + growthsStandardList.get(month).getEightyFivePercent());
//        Log.i("CHART TAG", "85 change " + changeAMonth85 * monthInProcess);
//        Log.i("CHART TAG", "85 percent " + (growthsStandardList.get(month).getEightyFivePercent() + changeAMonth85 * monthInProcess));


        return percentResult;


        //  }
    }


    public float convertWeightStandardDataByUnit(double data, String unit) {
        Calculator calculator = new Calculator();
        float dataConverted = 0;
        if (unit.equals("cm-kg")) {
            dataConverted = (float) data;
        } else {
            dataConverted = (float) calculator.convertKgLb(data);
        }
        return dataConverted;
    }

    public double convertWSDBU(double data, String unit) {
        Calculator calculator = new Calculator();
        double dataConverted = 0;
        if (unit.equals("cm-kg")) {
            dataConverted = data;
        } else {
            dataConverted = calculator.convertKgLb(data);
        }
        BigDecimal bd = new BigDecimal(dataConverted).setScale(1, RoundingMode.HALF_UP);
        dataConverted = bd.doubleValue();
        return dataConverted;
    }


    public float convertHeightStandardDataByUnit(double data, String unit) {
        Calculator calculator = new Calculator();
        float dataConverted = 0;
        if (unit.equals("cm-kg")) {
            dataConverted = (float) data;
        } else {
            dataConverted = (float) calculator.convertCmIn(data);
        }
        return dataConverted;
    }


    public ArrayList<String> getMonthAxis(String option) {
        ArrayList<String> months = new ArrayList<String>();
        String[] months1 = new String[]{"0m", "1m", "2m", "3m", "4m", "5m", "6m", "7m", "8m", "9m", "10m", "11m", "1y"};
        String[] months2 = new String[]{"1y1m", "1y2m", "1y3m", "1y4m", "1y5m", "1y6m", "1y7m", "1y8m", "1y9m", "1y10m", "1y11m", "2y"};
        String[] months3 = new String[]{"2y1m", "2y2m", "2y3m", "2y4m", "2y5m", "2y6m", "2y7m", "2y8m", "2y9m", "2y10m", "2y11m", "3y"};
        String[] months4 = new String[]{"3y1m", "3y2m", "3y3m", "3y4m", "3y5m", "3y6m", "3y7m", "3y8m", "3y9m", "3y10m", "3y11m", "4y"};
        String[] months5 = new String[]{"4y1m", "4y2m", "4y3m", "4y4m", "4y5m", "4y6m", "4y7m", "4y8m", "4y9m", "4y10m", "4y11m", "5y"};
        if (option.equals("1Y")) {
            months.addAll(Arrays.asList(months1));
        } else if (option.equals("2Y")) {
            months.addAll(Arrays.asList(months1));
            months.addAll(Arrays.asList(months2));
        } else if (option.equals("3Y")) {
            months.addAll(Arrays.asList(months1));
            months.addAll(Arrays.asList(months2));
            months.addAll(Arrays.asList(months3));
        } else if (option.equals("4Y")) {
            months.addAll(Arrays.asList(months1));
            months.addAll(Arrays.asList(months2));
            months.addAll(Arrays.asList(months3));
            months.addAll(Arrays.asList(months4));
        } else {
            months.addAll(Arrays.asList(months1));
            months.addAll(Arrays.asList(months2));
            months.addAll(Arrays.asList(months3));
            months.addAll(Arrays.asList(months4));
            months.addAll(Arrays.asList(months5));
        }


        return months;
    }


    public ArrayList<Entry> getWeightDataSet3rd(String option) {
        ArrayList<Entry> dataset3rd = new ArrayList<Entry>();

        if (option.equals("1Y")) {
            if (sex.equals("male")) {
                for (int i = 0; i <= 12; i++) {
                    dataset3rd.add(new Entry(i, (float) growthsStandardListMan.get(i).threePercent));
                }
            } else {
                for (int i = 0; i <= 12; i++) {
                    dataset3rd.add(new Entry(i, (float) growthsStandardListWoman.get(i).threePercent));
                }
            }

        } else if (option.equals("2Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 24; i++) {
                    dataset3rd.add(new Entry(i, (float) growthsStandardListMan.get(i).threePercent));
                }
            } else {
                for (int i = 0; i <= 24; i++) {
                    dataset3rd.add(new Entry(i, (float) growthsStandardListWoman.get(i).threePercent));
                }
            }

        } else if (option.equals("3Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 36; i++) {
                    dataset3rd.add(new Entry(i, (float) growthsStandardListMan.get(i).threePercent));
                }
            } else {
                for (int i = 0; i <= 36; i++) {
                    dataset3rd.add(new Entry(i, (float) growthsStandardListWoman.get(i).threePercent));
                }
            }


        } else if (option.equals("4Y")) {
            if (sex.equals("male")) {
                for (int i = 0; i <= 48; i++) {
                    dataset3rd.add(new Entry(i, (float) growthsStandardListMan.get(i).threePercent));
                }
            } else {
                for (int i = 0; i <= 48; i++) {
                    dataset3rd.add(new Entry(i, (float) growthsStandardListWoman.get(i).threePercent));
                }
            }

        } else if (option.equals("5Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 60; i++) {
                    dataset3rd.add(new Entry(i, (float) growthsStandardListMan.get(i).threePercent));
                }
            } else {
                for (int i = 0; i <= 60; i++) {
                    dataset3rd.add(new Entry(i, (float) growthsStandardListWoman.get(i).threePercent));
                }
            }

        }

        return dataset3rd;

    }

    public ArrayList<Entry> getWeightDataSet15th(String option) {
        ArrayList<Entry> dataset15th = new ArrayList<Entry>();

        if (option.equals("1Y")) {
            if (sex.equals("male")) {
                for (int i = 0; i <= 12; i++) {
                    dataset15th.add(new Entry(i, (float) growthsStandardListMan.get(i).fifteenPercent));
                }
            } else {
                for (int i = 0; i <= 12; i++) {
                    dataset15th.add(new Entry(i, (float) growthsStandardListWoman.get(i).fifteenPercent));
                }
            }

        } else if (option.equals("2Y")) {


            if (sex.equals("male")) {
                for (int i = 0; i <= 24; i++) {
                    dataset15th.add(new Entry(i, (float) growthsStandardListMan.get(i).fifteenPercent));
                }
            } else {
                for (int i = 0; i <= 24; i++) {
                    dataset15th.add(new Entry(i, (float) growthsStandardListWoman.get(i).fifteenPercent));
                }
            }

        } else if (option.equals("3Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 36; i++) {
                    dataset15th.add(new Entry(i, (float) growthsStandardListMan.get(i).fifteenPercent));
                }
            } else {
                for (int i = 0; i <= 36; i++) {
                    dataset15th.add(new Entry(i, (float) growthsStandardListWoman.get(i).fifteenPercent));
                }
            }


        } else if (option.equals("4Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 48; i++) {
                    dataset15th.add(new Entry(i, (float) growthsStandardListMan.get(i).fifteenPercent));
                }
            } else {
                for (int i = 0; i <= 48; i++) {
                    dataset15th.add(new Entry(i, (float) growthsStandardListWoman.get(i).fifteenPercent));
                }
            }

        } else if (option.equals("5Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 60; i++) {
                    dataset15th.add(new Entry(i, (float) growthsStandardListMan.get(i).fifteenPercent));
                }
            } else {
                for (int i = 0; i <= 60; i++) {
                    dataset15th.add(new Entry(i, (float) growthsStandardListWoman.get(i).fifteenPercent));
                }
            }
        }

        return dataset15th;

    }

    public ArrayList<Entry> getWeightDataSet50th(String option) {
        ArrayList<Entry> dataset50th = new ArrayList<Entry>();

        if (option.equals("1Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 12; i++) {
                    dataset50th.add(new Entry(i, (float) growthsStandardListMan.get(i).fiftyPercent));
                }
            } else {
                for (int i = 0; i <= 12; i++) {
                    dataset50th.add(new Entry(i, (float) growthsStandardListWoman.get(i).fiftyPercent));
                }
            }

        } else if (option.equals("2Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 24; i++) {
                    dataset50th.add(new Entry(i, (float) growthsStandardListMan.get(i).fiftyPercent));
                }
            } else {
                for (int i = 0; i <= 24; i++) {
                    dataset50th.add(new Entry(i, (float) growthsStandardListWoman.get(i).fiftyPercent));
                }
            }

        } else if (option.equals("3Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 36; i++) {
                    dataset50th.add(new Entry(i, (float) growthsStandardListMan.get(i).fiftyPercent));
                }
            } else {
                for (int i = 0; i <= 36; i++) {
                    dataset50th.add(new Entry(i, (float) growthsStandardListWoman.get(i).fiftyPercent));
                }
            }

        } else if (option.equals("4Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 48; i++) {
                    dataset50th.add(new Entry(i, (float) growthsStandardListMan.get(i).fiftyPercent));
                }
            } else {
                for (int i = 0; i <= 48; i++) {
                    dataset50th.add(new Entry(i, (float) growthsStandardListWoman.get(i).fiftyPercent));
                }
            }


        } else if (option.equals("5Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 60; i++) {
                    dataset50th.add(new Entry(i, (float) growthsStandardListMan.get(i).fiftyPercent));
                }
            } else {
                for (int i = 0; i <= 60; i++) {
                    dataset50th.add(new Entry(i, (float) growthsStandardListWoman.get(i).fiftyPercent));
                }
            }

        }

        return dataset50th;

    }


    public ArrayList<Entry> getWeightDataSet85th(String option) {
        ArrayList<Entry> dataset85th = new ArrayList<Entry>();

        if (option.equals("1Y")) {

            // dataset85th = dataset85th1;

            if (sex.equals("male")) {
                for (int i = 0; i <= 12; i++) {
                    dataset85th.add(new Entry(i, (float) growthsStandardListMan.get(i).eightyFivePercent));
                }
            } else {
                for (int i = 0; i <= 12; i++) {
                    dataset85th.add(new Entry(i, (float) growthsStandardListWoman.get(i).eightyFivePercent));
                }
            }
        } else if (option.equals("2Y")) {


//            dataset85th.addAll(dataset85th1);
//            dataset85th.addAll(dataset85th2);

            if (sex.equals("male")) {
                for (int i = 0; i <= 24; i++) {
                    dataset85th.add(new Entry(i, (float) growthsStandardListMan.get(i).eightyFivePercent));
                }
            } else {
                for (int i = 0; i <= 24; i++) {
                    dataset85th.add(new Entry(i, (float) growthsStandardListWoman.get(i).eightyFivePercent));
                }
            }

        } else if (option.equals("3Y")) {

//            dataset85th.addAll(dataset85th1);
//            dataset85th.addAll(dataset85th2);
//            dataset85th.addAll(dataset85th3);
            if (sex.equals("male")) {
                for (int i = 0; i <= 36; i++) {
                    dataset85th.add(new Entry(i, (float) growthsStandardListMan.get(i).eightyFivePercent));
                }
            } else {
                for (int i = 0; i <= 36; i++) {
                    dataset85th.add(new Entry(i, (float) growthsStandardListWoman.get(i).eightyFivePercent));
                }
            }

        } else if (option.equals("4Y")) {

//            dataset85th.addAll(dataset85th1);
//            dataset85th.addAll(dataset85th2);
//            dataset85th.addAll(dataset85th3);
//            dataset85th.addAll(dataset85th4);

            if (sex.equals("male")) {
                for (int i = 0; i <= 48; i++) {
                    dataset85th.add(new Entry(i, (float) growthsStandardListMan.get(i).eightyFivePercent));
                }
            } else {
                for (int i = 0; i <= 48; i++) {
                    dataset85th.add(new Entry(i, (float) growthsStandardListWoman.get(i).eightyFivePercent));
                }
            }

        } else if (option.equals("5Y")) {
//            dataset85th.addAll(dataset85th1);
//            dataset85th.addAll(dataset85th2);
//            dataset85th.addAll(dataset85th3);
//            dataset85th.addAll(dataset85th4);
//            dataset85th.addAll(dataset85th5);

            if (sex.equals("male")) {
                for (int i = 0; i <= 60; i++) {
                    dataset85th.add(new Entry(i, (float) growthsStandardListMan.get(i).eightyFivePercent));
                }
            } else {
                for (int i = 0; i <= 60; i++) {
                    dataset85th.add(new Entry(i, (float) growthsStandardListWoman.get(i).eightyFivePercent));
                }
            }


        }

        return dataset85th;

    }


    public ArrayList<Entry> getWeightDataSet97th(String option) {
        ArrayList<Entry> dataset97th = new ArrayList<Entry>();


        if (option.equals("1Y")) {
            if (sex.equals("male")) {
                for (int i = 0; i <= 12; i++) {
                    dataset97th.add(new Entry(i, (float) growthsStandardListMan.get(i).ninetySeventhPercent));
                }
            } else {
                for (int i = 0; i <= 12; i++) {
                    dataset97th.add(new Entry(i, (float) growthsStandardListWoman.get(i).ninetySeventhPercent));
                }
            }

            // dataset97th = dataset97th1;
        } else if (option.equals("2Y")) {


//            dataset97th.addAll(dataset97th1);
//            dataset97th.addAll(dataset97th2);

            if (sex.equals("male")) {
                for (int i = 0; i <= 24; i++) {
                    dataset97th.add(new Entry(i, (float) growthsStandardListMan.get(i).ninetySeventhPercent));
                }
            } else {
                for (int i = 0; i <= 24; i++) {
                    dataset97th.add(new Entry(i, (float) growthsStandardListWoman.get(i).ninetySeventhPercent));
                }
            }

        } else if (option.equals("3Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 36; i++) {
                    dataset97th.add(new Entry(i, (float) growthsStandardListMan.get(i).ninetySeventhPercent));
                }
            } else {
                for (int i = 0; i <= 36; i++) {
                    dataset97th.add(new Entry(i, (float) growthsStandardListWoman.get(i).ninetySeventhPercent));
                }
            }


        } else if (option.equals("4Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 48; i++) {
                    dataset97th.add(new Entry(i, (float) growthsStandardListMan.get(i).ninetySeventhPercent));
                }
            } else {
                for (int i = 0; i <= 48; i++) {
                    dataset97th.add(new Entry(i, (float) growthsStandardListWoman.get(i).ninetySeventhPercent));
                }
            }

        } else if (option.equals("5Y")) {

            if (sex.equals("male")) {
                for (int i = 0; i <= 60; i++) {
                    dataset97th.add(new Entry(i, (float) growthsStandardListMan.get(i).ninetySeventhPercent));
                }
            } else {
                for (int i = 0; i <= 60; i++) {
                    dataset97th.add(new Entry(i, (float) growthsStandardListWoman.get(i).ninetySeventhPercent));
                }
            }
        }

        return dataset97th;
    }


    public void showChart(String option) {

        ArrayList<Entry> dataset3rd = new ArrayList<Entry>();
        dataset3rd = getWeightDataSet3rd(option);


        LineDataSet lDataSet3rd = new LineDataSet(dataset3rd, "3rd");
        lDataSet3rd.setColor(ContextCompat.getColor(getContext(), R.color.chartOrange));
        lDataSet3rd.setValueTextColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        lDataSet3rd.setValueTextSize(20);
        lDataSet3rd.setLineWidth(1f);
        lDataSet3rd.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lDataSet3rd.setCubicIntensity(0.2f);
        lDataSet3rd.setDrawValues(false);
        lDataSet3rd.setDrawCircles(false);
        lDataSet3rd.setAxisDependency(YAxis.AxisDependency.LEFT);
        lDataSet3rd.setHighlightEnabled(false);

        ArrayList<Entry> dataset15th = new ArrayList<Entry>();
        dataset15th = getWeightDataSet15th(option);

        LineDataSet lDataSet15th = new LineDataSet(dataset15th, "15th");

        lDataSet15th.setColor(ContextCompat.getColor(getContext(), R.color.chartYellow));
        lDataSet15th.setLineWidth(1f);
        lDataSet15th.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lDataSet15th.setCubicIntensity(0.2f);
        lDataSet15th.setDrawCircles(false);
        lDataSet15th.setDrawValues(false);
        lDataSet15th.setAxisDependency(YAxis.AxisDependency.LEFT);
        lDataSet15th.setHighlightEnabled(false);

        ArrayList<Entry> dataset50th = new ArrayList<Entry>();
        dataset50th = getWeightDataSet50th(option);

        LineDataSet lDataSet50th = new LineDataSet(dataset50th, "50th");
        lDataSet50th.setColor(ContextCompat.getColor(getContext(), R.color.chartGreen));
        lDataSet50th.setLineWidth(1f);
        lDataSet50th.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lDataSet50th.setCubicIntensity(0.2f);
        lDataSet50th.setDrawCircles(false);
        lDataSet50th.setDrawValues(false);
        lDataSet50th.setAxisDependency(YAxis.AxisDependency.LEFT);
        lDataSet50th.setHighlightEnabled(false);

        ArrayList<Entry> dataset85th = new ArrayList<Entry>();
        dataset85th = getWeightDataSet85th(option);

        LineDataSet lDataSet85th = new LineDataSet(dataset85th, "85th");
        lDataSet85th.setColor(ContextCompat.getColor(getContext(), R.color.chartYellow));
        lDataSet85th.setLineWidth(1f);
        lDataSet85th.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lDataSet85th.setCubicIntensity(0.2f);
        lDataSet85th.setDrawCircles(false);
        lDataSet85th.setDrawValues(false);
        lDataSet85th.setAxisDependency(YAxis.AxisDependency.LEFT);
        lDataSet85th.setHighlightEnabled(false);

        ArrayList<Entry> dataset97th = new ArrayList<Entry>();
        dataset97th = getWeightDataSet97th(option);

        LineDataSet lDataSet97th = new LineDataSet(dataset97th, "97th");
        lDataSet97th.setColor(ContextCompat.getColor(getContext(), R.color.chartOrange));
        lDataSet97th.setLineWidth(1f);
        lDataSet97th.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lDataSet97th.setCubicIntensity(0.2f);
        lDataSet97th.setDrawCircles(false);
        lDataSet97th.setDrawValues(false);
        lDataSet97th.setAxisDependency(YAxis.AxisDependency.LEFT);
        lDataSet97th.setHighlightEnabled(false);

        getGrowthList(option);
        if (growthsList.size() == 0 || growthsList.get(growthsList.size() - 1).getGrowthWeight() == 0) {
            ageTextview.setText("-");
            weightTextview.setText("-");
            percentTextview.setText("-");

        } else {

            double month = formHelper.getMonthDiffDouble(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(currentUser.getUserBirthday())), dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(growthsList.get(growthsList.size() - 1).getGrowthDate())));
            double weight = getWeightByInit(growthsList.get(growthsList.size() - 1).getGrowthWeight(), growthsList.get(growthsList.size() - 1).getGrowthUnit(), whhUnit);

            ageTextview.setText(formHelper.getMonthDiffShort(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(currentUser.getUserBirthday())), dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(growthsList.get(growthsList.size() - 1).getGrowthDate()))));
            weightTextview.setText(getWeightByInit(growthsList.get(growthsList.size() - 1).getGrowthWeight(), growthsList.get(growthsList.size() - 1).getGrowthUnit(), whhUnit) + settingHelper.getUnitFormat(whhUnit, "weight"));
            percentTextview.setText(String.valueOf(countPercent(month, weight, currentUser.getUserSex())));

        }


        ArrayList<Entry> datasettest = new ArrayList<Entry>();
        for (int i = 0; i < growthsList.size(); i++) {
            if (growthsList.get(i).getGrowthWeight() > 0) {
                Entry entry = new Entry((float) formHelper.getMonthDiffDouble(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(currentUser.getUserBirthday())), dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(growthsList.get(i).getGrowthDate()))), (float) getWeightByInit(growthsList.get(i).getGrowthWeight(), growthsList.get(i).getGrowthUnit(), whhUnit));
                datasettest.add(entry);
            }

        }


        LineDataSet lDataSetTest = new LineDataSet(datasettest, currentUser.getUserName());
        lDataSetTest.setColor(ContextCompat.getColor(getContext(), settingHelper.getDatasetColor(currentUser.getUserTheme())));
        lDataSetTest.setLineWidth(2f);
        lDataSetTest.setDrawValues(false);
        lDataSetTest.setDrawCircles(true);
        lDataSetTest.setCircleHoleRadius(1f);
        lDataSetTest.setCircleColor(ContextCompat.getColor(getContext(), settingHelper.getDatasetColor(currentUser.getUserTheme())));

        if (!localDataHelper.getDarkMode()) {
            lDataSetTest.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.white));
            //dataSet.setCircleHoleColor(ContextCompat.getColor(getContext(), R.color.colorWhite));
        } else {
            lDataSetTest.setCircleHoleColor(Color.DKGRAY);
        }


        lDataSetTest.setAxisDependency(YAxis.AxisDependency.LEFT);
        lDataSetTest.setHighLightColor(Color.DKGRAY);

        XAxis xAxis = chart.getXAxis();
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f);
        // Set the xAxis position to bottom. Default is top
        // xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisLineColor(Color.LTGRAY);
//        xAxis.setAxisLineWidth(1f);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        //Customizing x axis value
        //final String[] months = new String[]{"0 m", "1 m", "2 m", "3 m", "4 m", "5 m", "6 m", "7 m", "8 m", "9 m", "10 m", "11 m", "12 m"};

        final ArrayList<String> months = getMonthAxis(option);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        // xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        //xAxis.setLabelCount(5, true);
        //***
        if (localDataHelper.getDarkMode()) {
            xAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setValueFormatter(new MyAxisValueFormatter());
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawAxisLine(false);
        // rightAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        // rightAxis.setLabelCount(4,true);
//        rightAxis.setAxisLineColor(Color.LTGRAY);
//        rightAxis.setAxisLineWidth(1f);
        YAxis yAxisLeft = chart.getAxisLeft();
        yAxisLeft.enableGridDashedLine(10f, 10f, 0f);
        yAxisLeft.setDrawAxisLine(false);


        //  yAxisLeft.setLabelCount(4, true);
//        yAxisLeft.setAxisLineColor(Color.LTGRAY);
//        yAxisLeft.setAxisLineWidth(1f);
        yAxisLeft.setValueFormatter(new MyAxisValueFormatter());
        if (localDataHelper.getDarkMode()) {
            yAxisLeft.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            rightAxis.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
        }


        // Controlling right side of y axis
//        YAxis yAxisRight = chart.getAxisRight();
//        yAxisRight.setEnabled(false);
//
//        //***
//        // Controlling left side of y axis
//        YAxis yAxisLeft = chart.getAxisLeft();
//        yAxisLeft.setGranularity(1f);
        // final String[] kgs = new String[]{"0 KG", "1 KG", "2 KG", "3 KG", "4 KG", "5 KG", "6 KG", "7 KG", "8 KG", "9 KG", "10 KG", "11 KG", "12 KG","14 KG", "16 KG", "18 KG", "20 KG", "22 KG", "24 KG","25 KG"};
        // yAxisLeft.setValueFormatter(new IndexAxisValueFormatter(kgs));

        // Setting Data
        LineData data = new LineData(lDataSet3rd);
        data.addDataSet(lDataSet15th);
        data.addDataSet(lDataSet50th);
        data.addDataSet(lDataSet85th);
        data.addDataSet(lDataSet97th);
        data.addDataSet(lDataSetTest);

        Legend l = chart.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setTextSize(12f);
        l.setXEntrySpace(5f); // set the space between the legend entries on the x-axis
        l.setYEntrySpace(5f); // set the space between the legend entries on the y-axis

        if (localDataHelper.getDarkMode()) {
            l.setTextColor(Color.WHITE);
        }


        chart.getDescription().setEnabled(false);
        //  chart.setBackgroundColor(Color.WHITE);
        chart.setDrawGridBackground(false);
        chart.setTouchEnabled(true);
        chart.setDragEnabled(false);
        chart.setDoubleTapToZoomEnabled(false);

//        if (isDarkMode)
//        {
//            chart.setGridBackgroundColor(getResources().getColor(R.color.darkModeBg));
//        }
//        else
//        {
//            chart.setGridBackgroundColor(getResources().getColor(R.color.lightGrey));
//        }
//

        chart.setData(data);
//           xAxis.setAxisMaximum(data.getXMax() + 0.5f);
//            xAxis.setAxisMinimum(data.getXMin() - 0.5f);

        chart.setOnChartValueSelectedListener(this);
        chart.animateX(500);
        chart.notifyDataSetChanged();
        chart.invalidate();


    }

    private void getGrowthList(String option) {
        if (growthsList.size() == 0) {
            if (option.equals("1Y")) {
                growthsList.addAll(db.getAllGrowthsOneYear(currentUser));
            } else if (option.equals("2Y")) {
                growthsList.addAll(db.getAllGrowthsTwoYear(currentUser));
            } else if (option.equals("3Y")) {
                growthsList.addAll(db.getAllGrowthsThreeYear(currentUser));
            } else if (option.equals("4Y")) {
                growthsList.addAll(db.getAllGrowthsFourYear(currentUser));
            } else if (option.equals("5Y")) {
                growthsList.addAll(db.getAllGrowthsFiveYear(currentUser));
            }
        } else {
            growthsList.clear();
            if (option.equals("1Y")) {
                growthsList.addAll(db.getAllGrowthsOneYear(currentUser));
            } else if (option.equals("2Y")) {
                growthsList.addAll(db.getAllGrowthsTwoYear(currentUser));
            } else if (option.equals("3Y")) {
                growthsList.addAll(db.getAllGrowthsThreeYear(currentUser));
            } else if (option.equals("4Y")) {
                growthsList.addAll(db.getAllGrowthsFourYear(currentUser));
            } else if (option.equals("5Y")) {
                growthsList.addAll(db.getAllGrowthsFiveYear(currentUser));
            }
        }
    }


    public double getWeightByInit(Double weight, String weightUnit, String settingUnit) {
        Calculator calculator = new Calculator();
        double weightConverted = 0;
        if (weightUnit.equals(settingUnit)) {
            weightConverted = weight;
        } else {

            if (settingUnit.equals("cm-kg")) {
                weightConverted = calculator.convertLbKg(weight);
            } else {
                weightConverted = calculator.convertKgLb(weight);
            }

        }
        return weightConverted;
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {
//        Toast.makeText(getContext(), ""
//                + e.getY(), Toast.LENGTH_SHORT).show();

        ageTextview.setText(formHelper.getMonthDiffDoubleToString(e.getX()));
        weightTextview.setText(e.getY() + settingHelper.getUnitFormat(whhUnit, "weight"));
        percentTextview.setText(String.valueOf(countPercent(e.getX(), e.getY(), currentUser.getUserSex())));


    }

    @Override
    public void onNothingSelected() {

    }


    private class MyAxisValueFormatter extends ValueFormatter {

        @Override
        public String getFormattedValue(float value) {
            return (int) value + settingHelper.getUnitFormat(whhUnit, "weight");
        }
    }


}
