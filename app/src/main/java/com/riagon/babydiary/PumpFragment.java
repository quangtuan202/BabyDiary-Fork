package com.riagon.babydiary;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class PumpFragment extends Fragment {

    public PumpFragment() {
        // Required empty public constructor
    }

    private Button button_pumpl;
    private Button button_pumplr;
    private Button button_pumpr;
    Boolean leftisrunning = false;
    Boolean rightisrunning = false;
    Boolean lrrunning = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pump, container, false);

        button_pumpl = (Button) view.findViewById(R.id.button_pumpl);
        button_pumplr = (Button) view.findViewById(R.id.button_pumplr);
        button_pumpr = (Button) view.findViewById(R.id.button_pumpr);

        button_pumpl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!leftisrunning) {
                    if (!rightisrunning && !lrrunning) {
                        Drawable iconLeft = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_pumpl.setCompoundDrawablesWithIntrinsicBounds(iconLeft, null, null, null);
                       // button_pumpl.setPadding(70, 0, 70, 0);
                        leftisrunning = true;

                    } else if (rightisrunning || !lrrunning) {
                        Drawable iconRight = getActivity().getResources().getDrawable(R.drawable.ic_tabpumpr);
                        button_pumpr.setCompoundDrawablesWithIntrinsicBounds(iconRight, null, null, null);
                       // button_pumpr.setPadding(65, 0, 65, 0);
                        rightisrunning = false;

                        Drawable iconLeft = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_pumpl.setCompoundDrawablesWithIntrinsicBounds(iconLeft, null, null, null);
                      //  button_pumpl.setPadding(70, 0, 70, 0);
                        leftisrunning = true;

                    } else if (!rightisrunning || lrrunning) {
                        Drawable iconLeftRight = getActivity().getResources().getDrawable(R.drawable.ic_tabpumplr);
                        button_pumplr.setCompoundDrawablesWithIntrinsicBounds(iconLeftRight, null, null, null);
                      //  button_pumplr.setPadding(40, 0, 40, 0);
                        lrrunning = false;

                        Drawable iconLeft = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_pumpl.setCompoundDrawablesWithIntrinsicBounds(iconLeft, null, null, null);
                        //button_pumpl.setPadding(70, 0, 70, 0);
                        leftisrunning = true;

                    }

                } else {

                    Drawable iconLeft = getActivity().getResources().getDrawable(R.drawable.ic_tabpumpl);
                    button_pumpl.setCompoundDrawablesWithIntrinsicBounds(iconLeft, null, null, null);
                   // button_pumpl.setPadding(65, 0, 65, 0);
                    leftisrunning = false;

                }

            }
        });

        button_pumpr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!rightisrunning) {
                    if (!leftisrunning && !lrrunning) {
                        Drawable iconRight = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_pumpr.setCompoundDrawablesWithIntrinsicBounds(iconRight, null, null, null);
                       // button_pumpr.setPadding(70, 0, 70, 0);
                        rightisrunning = true;

                    } else if (leftisrunning || !lrrunning) {
                        Drawable iconLeft = getActivity().getResources().getDrawable(R.drawable.ic_tabpumpl);
                        button_pumpl.setCompoundDrawablesWithIntrinsicBounds(iconLeft, null, null, null);
                       // button_pumpl.setPadding(65, 0, 65, 0);
                        leftisrunning = false;

                        Drawable iconRight = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_pumpr.setCompoundDrawablesWithIntrinsicBounds(iconRight, null, null, null);
                        //button_pumpr.setPadding(70, 0, 70, 0);
                        rightisrunning = true;
                    } else if (!leftisrunning || lrrunning) {
                        Drawable iconLeftRight = getActivity().getResources().getDrawable(R.drawable.ic_tabpumplr);
                        button_pumplr.setCompoundDrawablesWithIntrinsicBounds(iconLeftRight, null, null, null);
                       // button_pumplr.setPadding(40, 0, 40, 0);
                        lrrunning = false;

                        Drawable iconRight = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_pumpr.setCompoundDrawablesWithIntrinsicBounds(iconRight, null, null, null);
                        //button_pumpr.setPadding(70, 0, 70, 0);
                        rightisrunning = true;

                    }

                } else {

                    Drawable iconRigh = getActivity().getResources().getDrawable(R.drawable.ic_tabpumpr);
                    button_pumpr.setCompoundDrawablesWithIntrinsicBounds(iconRigh, null, null, null);
                    //button_pumpr.setPadding(65, 0, 65, 0);
                    rightisrunning = false;

                }
            }
        });


        button_pumplr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!lrrunning) {
                    if (!leftisrunning && !rightisrunning) {
                        Drawable iconLeftRight = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_pumplr.setCompoundDrawablesWithIntrinsicBounds(iconLeftRight, null, null, null);
                       // button_pumplr.setPadding(70, 0, 70, 0);
                        lrrunning = true;

                    } else if (leftisrunning || !rightisrunning) {
                        Drawable iconLeft = getActivity().getResources().getDrawable(R.drawable.ic_tabpumpl);
                        button_pumpl.setCompoundDrawablesWithIntrinsicBounds(iconLeft, null, null, null);
                       // button_pumpl.setPadding(65, 0, 65, 0);
                        leftisrunning = false;

                        Drawable iconLeftRight = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_pumplr.setCompoundDrawablesWithIntrinsicBounds(iconLeftRight, null, null, null);
                       // button_pumplr.setPadding(70, 0, 70, 0);
                        lrrunning = true;

                    } else if (!leftisrunning || rightisrunning) {
                        Drawable iconRight = getActivity().getResources().getDrawable(R.drawable.ic_tabpumpr);
                        button_pumpr.setCompoundDrawablesWithIntrinsicBounds(iconRight, null, null, null);
                        //button_pumpr.setPadding(65, 0, 65, 0);
                        rightisrunning = false;

                        Drawable iconLeftRight = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        button_pumplr.setCompoundDrawablesWithIntrinsicBounds(iconLeftRight, null, null, null);
                       // button_pumplr.setPadding(70, 0, 70, 0);
                        lrrunning = true;

                    }

                } else {

                    Drawable iconLeftRigh = getActivity().getResources().getDrawable(R.drawable.ic_tabpumplr);
                    button_pumplr.setCompoundDrawablesWithIntrinsicBounds(iconLeftRigh, null, null, null);
                   // button_pumplr.setPadding(40, 0, 40, 0);
                    lrrunning = false;

                }


            }
        });


        return view;
    }
}
