package com.riagon.babydiary.Utility;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Growth;
import com.riagon.babydiary.Model.Photo;
import com.riagon.babydiary.Model.User;
import com.riagon.babydiary.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class GrowthListAdapter extends RecyclerView.Adapter<GrowthListAdapter.MyViewHolder> {
    private Context mContext;
    private DatabaseHelper db;
    private List<Growth> activitiesGrowthList;
    private List<Photo> photoList;
    private PhotoAdapter2 adapterPhoto;
    private User currentUser;
    private LocalDataHelper localDataHelper;
    private SettingHelper settingHelper;
    private FormHelper formHelper;
    private DateFormatUtility dateFormatUtility;
    private String whhUnit = "";


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView growthWeight;
        private TextView growthLength;
        private TextView growthHead;
        private TextView weightChange;
        private TextView lengthChange;
        private TextView headChange;
        private TextView timeChange;
        private TextView dateRecord;
        private TextView ageRecord;
        private RelativeLayout timeLayout;
        private LinearLayout dateLayout;
        private RelativeLayout weightLayout;
        private RelativeLayout lengthLayout;
        private RelativeLayout headLayout;
        private TextView note;
        private RecyclerView recyclerView;


        public MyViewHolder(View view) {
            super(view);
            growthWeight = (TextView) view.findViewById(R.id.weight);
            growthLength = (TextView) view.findViewById(R.id.height);
            growthHead = (TextView) view.findViewById(R.id.head);
            weightChange = (TextView) view.findViewById(R.id.weight_change);
            lengthChange = (TextView) view.findViewById(R.id.height_change);
            headChange = (TextView) view.findViewById(R.id.head_change);
            timeChange = (TextView) view.findViewById(R.id.time_change);
            dateRecord = (TextView) view.findViewById(R.id.growth_list_date);
            timeLayout = (RelativeLayout) view.findViewById(R.id.time_change_layout);
            dateLayout = view.findViewById(R.id.growth_date_layout);
            ageRecord = (TextView) view.findViewById(R.id.growth_list_age);
            note = view.findViewById(R.id.growth_note);
            recyclerView = view.findViewById(R.id.recycler_view_photo);

            lengthLayout = view.findViewById(R.id.growth_length_layout);
            weightLayout = view.findViewById(R.id.growth_weight_layout);
            headLayout = view.findViewById(R.id.growth_head_layout);

        }
    }


    public GrowthListAdapter(Context mContext, List<Growth> activitiesGrowthList, User user, DatabaseHelper db) {
        this.mContext = mContext;
        this.activitiesGrowthList = activitiesGrowthList;
        this.db = db;
        localDataHelper = new LocalDataHelper(mContext);
        settingHelper = new SettingHelper(mContext);
        formHelper = new FormHelper(mContext);
        dateFormatUtility = new DateFormatUtility(mContext);
        whhUnit = localDataHelper.getWhhUnit();
        this.currentUser = user;
    }

    @Override
    public GrowthListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.growth_list_item, parent, false);

        return new GrowthListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GrowthListAdapter.MyViewHolder holder, int position) {
        final Growth growth = activitiesGrowthList.get(position);
        // holder.growthWeight.setText(String.valueOf(growth.getGrowthWeight()));
        holder.growthLength.setText(String.valueOf(growth.getGrowthLength()));
        holder.growthHead.setText(String.valueOf(growth.getGrowthHead()));
        setDateView(holder, position);
        setWeightView(holder, position);
        setHeightView(holder, position);
        setHeadView(holder, position);
        setTimeView(holder, position);
        holder.note.setText(growth.getGrowthNote());

        photoList = new ArrayList<>();
        photoList.addAll(db.getAllPhotoByGrowthId(growth.getGrowthId()));
        adapterPhoto = new PhotoAdapter2(mContext, photoList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext, 3);
        holder.recyclerView.setLayoutManager(mLayoutManager);
        holder.recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(5), true));
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(adapterPhoto);

    }


    @Override
    public int getItemCount() {
        return activitiesGrowthList.size();
    }


    public void setDateView(MyViewHolder holder, int position) {
        final Growth growth1 = activitiesGrowthList.get(position);
        //Growth growthLast=activitiesGrowthList.get(0);
        String dateSet = "";
        String ageSet = "";
        String date1 = "";
        String date2 = "";
        //  String dateLast="";
        date1 = dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(growth1.getGrowthDate()));
        //  dateLast=dateFormatUtility.getStringDateFormat(growthLast.getGrowthDate());

        if (position > 0) {
            dateSet = date1;
            Growth growth2 = activitiesGrowthList.get(position - 1);
            date2 = dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(growth2.getGrowthDate()));

            if (date2.equals(date1)) {
                dateSet = "";
                ageSet = "";
                holder.dateLayout.setVisibility(View.GONE);
            } else {
                dateSet = dateFormatUtility.getStringDateFormatHuman(mContext, dateFormatUtility.getDateFormat(date1));
                ageSet = formHelper.getMonthDiffShort(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(currentUser.getUserBirthday())), dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(growth1.getGrowthDate()))) + "(" + formHelper.getWeekDiff(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(currentUser.getUserBirthday())), dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(growth1.getGrowthDate()))) + ")";
                holder.dateLayout.setVisibility(View.VISIBLE);
            }
        } else {

            dateSet = dateFormatUtility.getStringDateFormatHuman(mContext, dateFormatUtility.getDateFormat(date1));
            ageSet = formHelper.getMonthDiffShort(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(currentUser.getUserBirthday())), dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(growth1.getGrowthDate()))) + "(" + formHelper.getWeekDiff(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(currentUser.getUserBirthday())), dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(growth1.getGrowthDate()))) + ")";
            holder.dateLayout.setVisibility(View.VISIBLE);
        }

        holder.dateRecord.setText(dateSet);
        holder.ageRecord.setText(ageSet);


        //  String dayDiff = formHelper.getMonthDiff("02-03-2017", "03-03-2020");

        //formHelper.getAgeByMonths(dateFormatUtility.getStringDateFormat(currentUser.getUserBirthday()));
//        String monthDiff = formHelper.getAgeByMonths(dateFormatUtility.getStringDateFormat(currentUser.getUserBirthday()));
//        Double monthDiffDouble = formHelper.getMonthDiffDouble(dateFormatUtility.getStringDateFormat(currentUser.getUserBirthday()),dateFormatUtility.getStringDateFormat(growth1.getGrowthDate()));
//        String weekDiff = formHelper.getAgeByWeeks(dateFormatUtility.getStringDateFormat(currentUser.getUserBirthday()));

//        Log.i("GROWTH TAG", "Birthday: " + dateFormatUtility.getStringDateFormat(currentUser.getUserBirthday()));
//       // Log.i("GROWTH TAG", "Month diff: " + dayDiff);
//        Log.i("GROWTH TAG", "Age: " + monthDiff);
//        Log.i("GROWTH TAG", "Age: " + monthDiffDouble);
//        Log.i("GROWTH TAG", "Age: " + weekDiff);
    }


    public void setWeightView(MyViewHolder holder, int position) {
        final Growth growth1 = activitiesGrowthList.get(position);
        double weightChange = 0;
        double weightPos1 = 0;
        double weightPos2;
        Calculator calculator = new Calculator();
        //new line
        if (growth1.getGrowthWeight() == 0) {
            holder.weightLayout.setVisibility(View.GONE);
        } else {
            holder.weightLayout.setVisibility(View.VISIBLE);
        }

        if (growth1.getGrowthUnit().equals(whhUnit)) {
            weightPos1 = growth1.getGrowthWeight();
            if (position < activitiesGrowthList.size() - 1) {

                Growth growth2 = activitiesGrowthList.get(position + 1);
                if (growth2.getGrowthWeight() != 0) {
                    if (growth2.getGrowthUnit().equals(growth1.getGrowthUnit())) {
                        weightPos2 = growth2.getGrowthWeight();
                    } else {
                        if (growth2.getGrowthUnit().equals("in-lb")) {
                            weightPos2 = calculator.convertLbKg(growth2.getGrowthWeight());
                        } else {
                            weightPos2 = calculator.convertKgLb(growth2.getGrowthWeight());
                        }
                    }

                    weightChange = getChanged(weightPos1, weightPos2);
                } else {
                    weightChange = 0;
                }


            } else {
                weightChange = 0;
            }
        } else {

            if (whhUnit.equals("cm-kg")) {
                weightPos1 = calculator.convertLbKg(growth1.getGrowthWeight());
                if (position < activitiesGrowthList.size() - 1) {

                    Growth growth2 = activitiesGrowthList.get(position + 1);

                    if (growth2.getGrowthWeight() != 0) {
                        if (growth2.getGrowthUnit().equals(growth1.getGrowthUnit())) {
                            weightPos2 = calculator.convertLbKg(growth2.getGrowthWeight());
                        } else {
                            weightPos2 = growth2.getGrowthWeight();
                        }

                        weightChange = getChanged(weightPos1, weightPos2);
                    } else {
                        weightChange = 0;
                    }


                } else {
                    weightChange = 0;
                }

            } else {
                weightPos1 = calculator.convertKgLb(growth1.getGrowthWeight());
                if (position < activitiesGrowthList.size() - 1) {
                    Growth growth2 = activitiesGrowthList.get(position + 1);
                    if (growth2.getGrowthWeight() != 0) {

                        if (growth2.getGrowthUnit().equals(growth1.getGrowthUnit())) {
                            weightPos2 = calculator.convertKgLb(growth2.getGrowthWeight());
                        } else {
                            weightPos2 = growth2.getGrowthWeight();
                        }
                        weightChange = getChanged(weightPos1, weightPos2);
                    } else {
                        weightChange = 0;
                    }

                } else {
                    weightChange = 0;
                }

            }


        }

        holder.growthWeight.setText(weightPos1 + settingHelper.getUnitFormat(whhUnit, "weight"));
        holder.weightChange.setText(getWeightFormat(weightChange, whhUnit, "weight"));
        setTextWeightColor(holder, weightChange);

    }


    public void setHeightView(MyViewHolder holder, int position) {
        final Growth growth1 = activitiesGrowthList.get(position);
        double heightChange = 0;
        double heightPos1 = 0;
        double heightPos2;
        Calculator calculator = new Calculator();

        //new line
        if (growth1.getGrowthLength() == 0) {
            holder.lengthLayout.setVisibility(View.GONE);
        } else {
            holder.lengthLayout.setVisibility(View.VISIBLE);
        }

        if (growth1.getGrowthUnit().equals(whhUnit)) {
            heightPos1 = growth1.getGrowthLength();
            if (position < activitiesGrowthList.size() - 1) {

                Growth growth2 = activitiesGrowthList.get(position + 1);
                if (growth2.getGrowthLength() != 0) {
                    if (growth2.getGrowthUnit().equals(growth1.getGrowthUnit())) {
                        heightPos2 = growth2.getGrowthLength();
                    } else {
                        if (growth2.getGrowthUnit().equals("in-lb")) {
                            heightPos2 = calculator.convertInCm(growth2.getGrowthLength());
                        } else {
                            heightPos2 = calculator.convertCmIn(growth2.getGrowthLength());
                        }
                    }

                    heightChange = getChanged(heightPos1, heightPos2);
                } else {
                    heightChange = 0;
                }

            } else {
                heightChange = 0;
            }
        } else {

            if (whhUnit.equals("cm-kg")) {
                heightPos1 = calculator.convertInCm(growth1.getGrowthLength());
                if (position < activitiesGrowthList.size() - 1) {

                    Growth growth2 = activitiesGrowthList.get(position + 1);

                    if (growth2.getGrowthLength() != 0) {

                        if (growth2.getGrowthUnit().equals(growth1.getGrowthUnit())) {
                            heightPos2 = calculator.convertInCm(growth2.getGrowthLength());
                        } else {
                            heightPos2 = growth2.getGrowthLength();
                        }
                        heightChange = getChanged(heightPos1, heightPos2);

                    } else {
                        heightChange = 0;
                    }


                } else {
                    heightChange = 0;
                }

            } else {
                heightPos1 = calculator.convertCmIn(growth1.getGrowthLength());
                if (position < activitiesGrowthList.size() - 1) {
                    Growth growth2 = activitiesGrowthList.get(position + 1);

                    if (growth2.getGrowthLength() != 0) {

                        if (growth2.getGrowthUnit().equals(growth1.getGrowthUnit())) {
                            heightPos2 = calculator.convertCmIn(growth2.getGrowthLength());
                        } else {
                            heightPos2 = growth2.getGrowthLength();
                        }

                        heightChange = getChanged(heightPos1, heightPos2);
                    } else {

                        heightChange = 0;
                    }


                } else {
                    heightChange = 0;
                }

            }


        }

        holder.growthLength.setText(heightPos1 + settingHelper.getUnitFormat(whhUnit, "height"));
        holder.lengthChange.setText(getWeightFormat(heightChange, whhUnit, "height"));
        setTextHeightColor(holder, heightChange);

    }


    public void setHeadView(MyViewHolder holder, int position) {
        final Growth growth1 = activitiesGrowthList.get(position);
        double headChange = 0;
        double headPos1 = 0;
        double headPos2;
        Calculator calculator = new Calculator();

        //new line
        if (growth1.getGrowthHead() == 0) {
            holder.headLayout.setVisibility(View.GONE);
        } else {
            holder.headLayout.setVisibility(View.VISIBLE);
        }

        if (growth1.getGrowthUnit().equals(whhUnit)) {
            headPos1 = growth1.getGrowthHead();
            if (position < activitiesGrowthList.size() - 1) {

                Growth growth2 = activitiesGrowthList.get(position + 1);
                if (growth2.getGrowthHead() != 0) {
                    if (growth2.getGrowthUnit().equals(growth1.getGrowthUnit())) {
                        headPos2 = growth2.getGrowthHead();
                    } else {
                        if (growth2.getGrowthUnit().equals("in-lb")) {
                            headPos2 = calculator.convertInCm(growth2.getGrowthHead());
                        } else {
                            headPos2 = calculator.convertCmIn(growth2.getGrowthHead());
                        }
                    }
                    headChange = getChanged(headPos1, headPos2);
                } else {
                    headChange = 0;
                }

            } else {

                headChange = 0;
            }
        } else {

            if (whhUnit.equals("cm-kg")) {
                headPos1 = calculator.convertInCm(growth1.getGrowthHead());
                if (position < activitiesGrowthList.size() - 1) {

                    Growth growth2 = activitiesGrowthList.get(position + 1);
                    if (growth2.getGrowthHead() != 0) {

                        if (growth2.getGrowthUnit().equals(growth1.getGrowthUnit())) {
                            headPos2 = calculator.convertInCm(growth2.getGrowthHead());
                        } else {
                            headPos2 = growth2.getGrowthHead();
                        }

                        headChange = getChanged(headPos1, headPos2);

                    } else {
                        headChange = 0;
                    }


                } else {
                    headChange = 0;
                }

            } else {
                headPos1 = calculator.convertCmIn(growth1.getGrowthHead());
                if (position < activitiesGrowthList.size() - 1) {
                    Growth growth2 = activitiesGrowthList.get(position + 1);

                    if (growth2.getGrowthHead() != 0) {
                        if (growth2.getGrowthUnit().equals(growth1.getGrowthUnit())) {
                            headPos2 = calculator.convertCmIn(growth2.getGrowthHead());
                        } else {
                            headPos2 = growth2.getGrowthHead();
                        }

                        headChange = getChanged(headPos1, headPos2);
                    } else {
                        headChange = 0;
                    }


                } else {
                    headChange = 0;
                }

            }


        }

        holder.growthHead.setText(headPos1 + settingHelper.getUnitFormat(whhUnit, "head"));
        holder.headChange.setText(getWeightFormat(headChange, whhUnit, "head"));
        setTextHeadColor(holder, headChange);

    }


    public void setTimeView(MyViewHolder holder, int position) {
        final Growth growth1 = activitiesGrowthList.get(position);

        if (position < activitiesGrowthList.size() - 1) {

            final Growth growth2 = activitiesGrowthList.get(position + 1);
            String timeChange;
            String timePos1 = formHelper.getDateWithTimeFormat(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(growth1.getGrowthDate())), dateFormatUtility.getStringTimeFormat(dateFormatUtility.getTimeFormat(growth1.getGrowthTime())));
            String timePos2 = formHelper.getDateWithTimeFormat(dateFormatUtility.getStringDateFormat(dateFormatUtility.getDateFormat2(growth2.getGrowthDate())), dateFormatUtility.getStringTimeFormat(dateFormatUtility.getTimeFormat(growth2.getGrowthTime())));
            timeChange = formHelper.timeDiffCount(timePos1, timePos2);

            holder.timeLayout.setVisibility(View.VISIBLE);
            holder.timeChange.setText(timeChange);


        } else {
            holder.timeLayout.setVisibility(View.GONE);
        }


    }


    public String getWeightFormat(double weightChange, String whhUnit, String unitType) {
        String changeFormatted = "";
        if (weightChange == 0) {
            changeFormatted = "";
        } else if (weightChange > 0) {
            changeFormatted = "+" + weightChange + settingHelper.getUnitFormat(whhUnit, unitType);
        } else {
            changeFormatted = weightChange + settingHelper.getUnitFormat(whhUnit, unitType);
        }

        return changeFormatted;
    }


    public double getChanged(double valuePos1, double valuePos2) {
        double change = valuePos1 - valuePos2;
        BigDecimal bd = new BigDecimal(change).setScale(1, RoundingMode.HALF_UP);
        change = bd.doubleValue();
        return change;
    }

    public void setTextWeightColor(MyViewHolder holder, double change) {
        if (change > 0) {
            holder.weightChange.setTextColor(Color.parseColor("#4dc3ff"));
        } else if (change < 0) {
            holder.weightChange.setTextColor(Color.parseColor("#F46287"));
        } else {
            holder.weightChange.setTextColor(Color.parseColor("#777373"));
        }
    }

    public void setTextHeightColor(MyViewHolder holder, double change) {
        if (change > 0) {
            holder.lengthChange.setTextColor(Color.parseColor("#4dc3ff"));
        } else if (change < 0) {
            holder.lengthChange.setTextColor(Color.parseColor("#F46287"));
        } else {
            holder.lengthChange.setTextColor(Color.parseColor("#777373"));
        }
    }

    public void setTextHeadColor(MyViewHolder holder, double change) {
        if (change > 0) {
            holder.headChange.setTextColor(Color.parseColor("#4dc3ff"));
        } else if (change < 0) {
            holder.headChange.setTextColor(Color.parseColor("#F46287"));
        } else {
            holder.headChange.setTextColor(Color.parseColor("#777373"));
        }
    }


    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = mContext.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


}

