package com.riagon.babydiary.Model;

import java.util.Comparator;

public class ActivityIdComparator implements Comparator<Activities> {
    @Override
    public int compare(Activities o1, Activities o2) {
        if (o1.getFixedID() > o2.getFixedID())
        {
            return 1;
        }
        else if (o1.getFixedID() < o2.getFixedID())
        {
            return -1;
        }
        return 0;
    }
}
