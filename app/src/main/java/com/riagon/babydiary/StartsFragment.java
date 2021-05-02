package com.riagon.babydiary;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.riagon.babydiary.Data.DatabaseHelper;
import com.riagon.babydiary.Model.Activities;
import com.riagon.babydiary.Model.ActivityIdComparator;
import com.riagon.babydiary.Utility.LocalDataHelper;
import com.riagon.babydiary.Utility.StatsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartsFragment extends Fragment {
    private RecyclerView recyclerView;
    private StatsAdapter adapterStats;
    private List<Activities> statsList;
    private List<Activities> fullStatsList;

    private LinearLayout statsLayout;
    private DatabaseHelper db;
    private LocalDataHelper localDataHelper;
    private TextView totalActivitiesTx;

    public StartsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_starts, container, false);
        db = new DatabaseHelper(getContext());
        localDataHelper= new LocalDataHelper(getContext());
        statsList = new ArrayList<>();
        statsList.addAll(db.getAllActivitiesCount(localDataHelper.getActiveUserId()));
        fullStatsList = getFullStatsList(statsList);

        // statsList.remove(statsList.size() - 1);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_stats);
        statsLayout = (LinearLayout) view.findViewById(R.id.stats_layout);
      //  totalActivitiesTx = (TextView) view.findViewById(R.id.total_activities_tx);

       // totalActivitiesTx.setText(getResources().getString(R.string.total_activities) + db.getRecordCount(db.getActiveUser().getUserId()));

        adapterStats = new StatsAdapter(getContext(), fullStatsList, db);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getContext(), 3);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(1), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterStats);

        return view;
    }

    public List<Activities> getFullStatsList(List<Activities> statsList) {

        List<Activities> newActivities = new ArrayList<>();

        List<String> fullActivityID = new ArrayList(Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20"));

        List<String> gotDataActivityID = new ArrayList<>();
        List<String> noDataActivityID = new ArrayList<>();

        //get the array of all activity_id that activity have data

        for (int i = 0; i < statsList.size(); i++) {
            gotDataActivityID.add(String.valueOf(statsList.get(i).getFixedID()));

        }

        //get the array of all activity_id that activity have no data

        for (int i = 0; i < fullActivityID.size(); i++) {

            if (gotDataActivityID.contains(fullActivityID.get(i))) {
                // System.out.println("Exist : "+fullActivityID.get(i));
            } else {
                noDataActivityID.add(fullActivityID.get(i));
            }

        }

        //Add Activities have data to List

        for (int k = 0; k < statsList.size(); k++) {
            newActivities.add(statsList.get(k));
        }


        //Add Activities have no data to List
        for (int j = 0; j < noDataActivityID.size(); j++) {
            Activities activity = new Activities();
            activity.setFixedID(Integer.parseInt(noDataActivityID.get(j)));
            activity.setUserId(localDataHelper.getActiveUserId());
            activity.setTotalActivities(0);
            newActivities.add(activity);
        }

        Collections.sort(newActivities, new ActivityIdComparator());

        return newActivities;
    }


    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }
}


