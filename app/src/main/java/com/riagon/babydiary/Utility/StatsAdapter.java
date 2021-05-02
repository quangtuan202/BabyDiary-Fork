package com.riagon.babydiary.Utility;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Activities;
import com.riagon.babydiary.StatsDetailActivity;
import com.riagon.babydiary.R;

import java.util.List;

public class StatsAdapter extends RecyclerView.Adapter<StatsAdapter.MyViewHolder>{
    private Context mContext;
    private List<Activities> statsList;
    public DatabaseHelper db;
    private LocalDataHelper localDataHelper;
    private ActivityHelper activityHelper;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView activitiesIcon;
        public TextView activitiesName, statsCount;
        public CardView cardView;
        private RelativeLayout statsLayout;

        public MyViewHolder(View view) {
            super(view);
            activitiesIcon = (ImageView) view.findViewById(R.id.cvIcon);
            activitiesName = (TextView) view.findViewById(R.id.cvName);
            statsCount = (TextView) view.findViewById(R.id.stats_count);
            cardView=(CardView) view.findViewById(R.id.card_view_stats);
            statsLayout= (RelativeLayout) view.findViewById(R.id.stats_layout);
        }
    }


    public StatsAdapter(Context mContext, List<Activities> statsList,DatabaseHelper db) {
        this.mContext = mContext;
        this.statsList = statsList;
        this.db=db;
        localDataHelper= new LocalDataHelper(mContext);
        activityHelper= new ActivityHelper(mContext);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stats_list_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        Activities stats = statsList.get(position);

        holder.activitiesIcon.setImageResource(activityHelper.getIcon(stats.getFixedID()));
        holder.activitiesName.setText(activityHelper.getActivityName(stats.getFixedID()));
        holder.statsCount.setText(String.valueOf(stats.getTotalActivities()));
        // holder.cardView.setBackgroundResource(R.drawable.stats_bg_selector);

        holder.activitiesIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, StatsDetailActivity.class);
//                mContext.startActivity(intent);
                launchActivity(stats.getFixedID());
                // holder.cardView.setBackgroundResource(R.drawable.radio_bg_chart_selector);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(mContext, StatsDetailActivity.class);
//                mContext.startActivity(intent);
                launchActivity(stats.getFixedID());
               // holder.cardView.setBackgroundResource(R.drawable.radio_bg_chart_selector);
            }
        });
       // holder.cardView.setCardBackgroundColor(ContextCompat.getColor(mContext, setTint(stats.getFixedID())));

    }




    public void launchActivity(int activityID) {

        if (activityID == 1) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",1);
            mContext.startActivity(intent);
        } else if (activityID == 2) {

            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",2);
            mContext.startActivity(intent);

        } else if (activityID == 3) {

            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",3);
            mContext.startActivity(intent);
        } else if (activityID == 4) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",4);
            mContext.startActivity(intent);
        } else if (activityID == 5) {

            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",5);
            mContext.startActivity(intent);
        } else if (activityID == 6) {

            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",6);
            mContext.startActivity(intent);
        } else if (activityID == 7) {

            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",7);
            mContext.startActivity(intent);
        } else if (activityID == 8) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",8);
            mContext.startActivity(intent);
        } else if (activityID == 9) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",9);
            mContext.startActivity(intent);
        } else if (activityID == 10) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",10);
            mContext.startActivity(intent);
        } else if (activityID == 11) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",11);
            mContext.startActivity(intent);
        } else if (activityID == 12) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",12);
            mContext.startActivity(intent);
        } else if (activityID == 13) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",13);
            mContext.startActivity(intent);
        } else if (activityID == 14) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",14);
            mContext.startActivity(intent);
        } else if (activityID == 15) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",15);
            mContext.startActivity(intent);
        } else if (activityID == 16) {

            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",16);
            mContext.startActivity(intent);

        } else if (activityID == 17) {

            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",17);
            mContext.startActivity(intent);
        } else if (activityID == 18) {

            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",18);
            mContext.startActivity(intent);
        } else if (activityID == 19) {

            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",19);
            mContext.startActivity(intent);
        } else if (activityID == 20) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",20);
            mContext.startActivity(intent);
        }
        else if (activityID == 21) {
            Intent intent = new Intent(mContext, StatsDetailActivity.class);
            intent.putExtra("Activity",21);
            mContext.startActivity(intent);
        }
    }

    @Override
    public int getItemCount() {
        return statsList.size();
    }
}


