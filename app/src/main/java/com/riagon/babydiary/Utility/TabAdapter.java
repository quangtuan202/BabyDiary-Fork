package com.riagon.babydiary.Utility;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.riagon.babydiary.FeedFragment;
import com.riagon.babydiary.OthersFragment;
import com.riagon.babydiary.PumpFragment;

public class TabAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public TabAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;

    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                FeedFragment feedFragment = new FeedFragment();
                return feedFragment;
            case 1:
                PumpFragment pumpFragment = new PumpFragment();

                return pumpFragment;
            case 2:
                OthersFragment othersFragment = new OthersFragment();
                return othersFragment;

            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}