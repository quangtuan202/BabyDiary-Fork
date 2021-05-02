package com.riagon.babydiary;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    public FeedFragment() {
        // Required empty public constructor

    }

    private Button breast_feedl;
    private Button breast_feedr;

    Boolean leftisrunning = false;
    Boolean rightisrunning = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_feed, container, false);

        breast_feedl = (Button) view.findViewById(R.id.breast_feedl);
        breast_feedr = (Button) view.findViewById(R.id.breast_feedr);

        breast_feedl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!leftisrunning) {

                    if (!rightisrunning) {
                        Drawable icon = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        breast_feedl.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                       // breast_feedl.setPadding(70, 0, 70, 0);
                        leftisrunning = true;


                    } else {

                        Drawable iconRight = getActivity().getResources().getDrawable(R.drawable.ic_tabfeedr);
                        breast_feedr.setCompoundDrawablesWithIntrinsicBounds(iconRight, null, null, null);
                       // breast_feedr.setPadding(65, 0, 65, 0);
                        rightisrunning = false;


                        Drawable icon = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        breast_feedl.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                      //  breast_feedl.setPadding(70, 0, 70, 0);
                        leftisrunning = true;

                    }

                } else {
                    // Toast.makeText(getApplication(),"Before Click: "+leftisrunning,Toast.LENGTH_SHORT).show();
                    Drawable iconLeft = getActivity().getResources().getDrawable(R.drawable.ic_tabfeedl);
                    breast_feedl.setCompoundDrawablesWithIntrinsicBounds(iconLeft, null, null, null);
                   // breast_feedl.setPadding(65, 0, 65, 0);
                    leftisrunning = false;

                }


            }
        });


        breast_feedr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!rightisrunning) {

                    if (!leftisrunning) {
                        Drawable icon = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        breast_feedr.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                       // breast_feedr.setPadding(70, 0, 70, 0);
                        rightisrunning = true;
                    } else {

                        Drawable iconLeft = getActivity().getResources().getDrawable(R.drawable.ic_tabfeedl);
                        breast_feedl.setCompoundDrawablesWithIntrinsicBounds(iconLeft, null, null, null);
                       // breast_feedl.setPadding(65, 0, 65, 0);
                        leftisrunning = false;

                        Drawable icon = getActivity().getResources().getDrawable(R.drawable.ic_stop);
                        breast_feedr.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
                      //  breast_feedr.setPadding(70, 0, 70, 0);
                        rightisrunning = true;
                    }

                } else {

                    Drawable iconRight = getActivity().getResources().getDrawable(R.drawable.ic_tabfeedr);
                    breast_feedr.setCompoundDrawablesWithIntrinsicBounds(iconRight, null, null, null);
                   // breast_feedr.setPadding(65, 0, 65, 0);
                    rightisrunning = false;

                }

            }
        });


        return view;
    }
}
