package com.riagon.babydiary.Utility;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Activities;
import com.riagon.babydiary.R;

import java.util.List;

import static android.widget.Toast.makeText;

public class ActivitiesConfigAdapter extends RecyclerView.Adapter<ActivitiesConfigAdapter.MyViewHolder> {
    private DatabaseHelper db;
    private DateFormatUtility dateFormatUtility;
    private FormHelper formHelper;
    private List<Activities> activitiesConfigList;
    private Context mContext;
    private ActivityHelper activityHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView activitiesIcon;
        public TextView activitiesName;
        public Switch isShow;


        public MyViewHolder(View view) {
            super(view);
            activitiesName = (TextView) view.findViewById(R.id.cvName);
            activitiesIcon = (ImageView) view.findViewById(R.id.cvIcon);
            isShow = (Switch) view.findViewById(R.id.isShowSwitch);

        }
    }


    public ActivitiesConfigAdapter(Context mContext, List<Activities> activitiesConfigList, DatabaseHelper databaseHelper) {
        this.mContext = mContext;
        this.activitiesConfigList = activitiesConfigList;
        formHelper = new FormHelper(mContext);
        dateFormatUtility = new DateFormatUtility(mContext);
        this.db = databaseHelper;
        activityHelper = new ActivityHelper(mContext);
    }

    @Override
    public ActivitiesConfigAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.config_list_item, parent, false);
        return new ActivitiesConfigAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ActivitiesConfigAdapter.MyViewHolder holder, final int position) {
        final Activities activities = activitiesConfigList.get(position);
        holder.activitiesName.setText(activityHelper.getActivityName(activities.getFixedID()));
        // holder.activitiesName.setTextColor(mContext.getResources().getColor(setTint(activities.getActivitiesName())));
        holder.isShow.setChecked(activities.getShow());

        holder.isShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.updateIsShowActivities(activities, holder.isShow.isChecked(), dateFormatUtility.getDateFullFormat(formHelper.getDatetimeNow()));
                //  makeText(mContext, "Is show: " + holder.isShow.isChecked(), Toast.LENGTH_SHORT).show();

            }
        });

        holder.activitiesIcon.setImageResource(activityHelper.getIcon(activities.getFixedID()));
    }


    @Override
    public int getItemCount() {
        return activitiesConfigList.size();
    }


}
