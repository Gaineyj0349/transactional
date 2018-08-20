package com.gainwise.transactional.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.gainwise.transactional.Activities.LabelSummaryAct;
import com.gainwise.transactional.R;

import java.util.Calendar;

import pyxis.uzuki.live.sectioncalendarview.SectionCalendarView;
import pyxis.uzuki.live.sectioncalendarview.impl.OnDaySelectedListener;
import spencerstudios.com.bungeelib.Bungee;
import spencerstudios.com.fab_toast.FabToast;

public class FragStatistics extends Fragment {


    View view;
    long startTimeInMillis = 0;
    long endTimeInMillis = 0;
    CheckBox allCB;
    CheckBox customCB;
    SectionCalendarView calendarView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


            if(view == null){


             view = inflater.inflate(R.layout.fragment_statistics, container, false);

            calendarView = view.findViewById(R.id.calendarView);
           final TextView txtStartDay = view.findViewById(R.id.txtStartDay);
           final TextView txtEndDay = view.findViewById(R.id.txtEndDay);
           final Button fragStatReset = view.findViewById(R.id.frag_stat_clear);
            final Button fragStatGO = view.findViewById(R.id.frag_stat_go);
          allCB = view.findViewById(R.id.frag_stat_checkbox_all);
     customCB = view.findViewById(R.id.frag_stat_checkbox_custom);

        allCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(customCB.isChecked()){
                    customCB.setChecked(false);
                    resetAll();
                    allCB.setChecked(true
                    );
                }
            }
        });
        customCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(allCB.isChecked()){
                    allCB.setChecked(false);
                }
            }
        });

            fragStatReset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
           resetAll();

                }
            });

            calendarView.setDateFormat("yyyy MMM");
            calendarView.setPreventPreviousDate(false);

            calendarView.setErrToastMessage("You can not select the previous date.");
            calendarView.setOnDaySelectedListener(new OnDaySelectedListener() {
                @Override
                public void onDaySelected(String s, String s1) {

                    try {


                        txtStartDay.setText("From: " + s);
                        String startYear = s.substring(0, 4);
                        String startMonth = s.substring(5, 7);
                        String startDay = s.substring(8, 10);

                        txtEndDay.setText("To: " + s1);
                        String endYear = s1.substring(0, 4);
                        String endMonth = s1.substring(5, 7);
                        String endDay = s1.substring(8, 10);
                        startTimeInMillis = getMillisWith(startYear, startMonth, startDay);
                        endTimeInMillis = getMillisWith2(endYear, endMonth, endDay);
                        Log.i("JOSHdate", "" + startTimeInMillis);
                    }catch (Exception e){

                    }
                }

            });
            calendarView.buildCalendar();

            fragStatGO.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(allCB.isChecked()){
                    Intent i = new Intent(getActivity(), LabelSummaryAct.class);
                    i.putExtra("all", "all");
                    startActivity(i);
                        Bungee.fade(getActivity());
                    }else if(customCB.isChecked() &&
                            (startTimeInMillis>0 && endTimeInMillis>0)){
                        Intent i = new Intent(getActivity(), LabelSummaryAct.class);
                        i.putExtra("startMilli", startTimeInMillis);
                        i.putExtra("endMilli", endTimeInMillis);
                        Log.i("JOSHnew", "passing: " + startTimeInMillis + endTimeInMillis);
                        startActivity(i);
                        Bungee.fade(getActivity());


                    }else{
                        FabToast.makeText(getActivity(), "You must check entire history or custom date range .", Toast.LENGTH_LONG,
                                FabToast.INFORMATION, FabToast.POSITION_DEFAULT).show();
                    }
                }
            });



            }
        return view;
    }


    long getMillisWith(String y, String m, String d){

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(y));
        cal.set(Calendar.MONTH, Integer.parseInt(m) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d));
        cal.set(Calendar.HOUR_OF_DAY, 00);
        cal.set(Calendar.MINUTE, 01);

        return cal.getTimeInMillis();
    }
    long getMillisWith2(String y, String m, String d){

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.parseInt(y));
        cal.set(Calendar.MONTH, Integer.parseInt(m) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(d));
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);

        return cal.getTimeInMillis();
    }



    void resetAll(){
        Log.i("JOSHtest2", "hello");
        startTimeInMillis = 0;
        endTimeInMillis = 0;
        calendarView.clearDate();
        if(customCB.isChecked()){
            customCB.setChecked(false);

        }
        if(allCB.isChecked()){
            allCB.setChecked(false);
        }



    }
}
