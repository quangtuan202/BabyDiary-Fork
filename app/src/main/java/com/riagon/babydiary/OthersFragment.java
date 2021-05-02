package com.riagon.babydiary;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class OthersFragment extends Fragment {
    private Button button_sleep;
    private Button button_tummy;
    private Button button_activity;
    Boolean sleeprunning = false;
    Boolean tummyrunning = false;
    Boolean activityrunning = false;

    public OthersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_others, container, false);

        button_sleep = (Button) view.findViewById(R.id.button_sleep);
        button_tummy = (Button) view.findViewById(R.id.button_tummy);
        button_activity = (Button) view.findViewById(R.id.button_activity);

        button_sleep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sleeprunning) {
                    if (!tummyrunning) {
                        Drawable iconSleep = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_sleep.setCompoundDrawablesWithIntrinsicBounds(iconSleep, null, null, null);
                        sleeprunning = true;
                       // button_sleep.setPadding(85, 0, 85, 0);

                    } else if (tummyrunning) {
                        Drawable icontummy = getActivity().getResources().getDrawable(R.drawable.ic_tabtummy);
                        button_tummy.setCompoundDrawablesWithIntrinsicBounds(icontummy, null, null, null);
                     //   button_tummy.setPadding(55, 0, 55, 0);
                        tummyrunning = false;

                        Drawable iconSleep = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_sleep.setCompoundDrawablesWithIntrinsicBounds(iconSleep, null, null, null);
                       // button_sleep.setPadding(85, 0, 85, 0);
                        sleeprunning = true;

                    }

                } else {

                    Drawable iconSleep = getActivity().getResources().getDrawable(R.drawable.ic_tabsleep);
                    button_sleep.setCompoundDrawablesWithIntrinsicBounds(iconSleep, null, null, null);
                  //  button_sleep.setPadding(75, 0, 75, 0);
                    sleeprunning = false;

                }

            }
        });

        button_tummy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!tummyrunning) {
                    if (!sleeprunning) {
                        Drawable iconTumy = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_tummy.setCompoundDrawablesWithIntrinsicBounds(iconTumy, null, null, null);
                      //  button_tummy.setPadding(85, 0, 85, 0);
                        tummyrunning = true;

                    } else if (sleeprunning) {
                        Drawable iconSleep = getActivity().getResources().getDrawable(R.drawable.ic_tabsleep);
                        button_sleep.setCompoundDrawablesWithIntrinsicBounds(iconSleep, null, null, null);
                       // button_sleep.setPadding(75, 0, 75, 0);
                        sleeprunning = false;

                        Drawable iconTummy = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_tummy.setCompoundDrawablesWithIntrinsicBounds(iconTummy, null, null, null);
                       // button_tummy.setPadding(85, 0, 85, 0);
                        tummyrunning = true;

                    }

                } else {

                    Drawable iconTummy = getActivity().getResources().getDrawable(R.drawable.ic_tabtummy);
                    button_tummy.setCompoundDrawablesWithIntrinsicBounds(iconTummy, null, null, null);
                  //  button_tummy.setPadding(55, 0, 55, 0);
                    tummyrunning = false;

                }

            }
        });


        button_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        return view;
    }
}
