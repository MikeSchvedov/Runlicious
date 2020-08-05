package com.hypnagogix.runlicious;

import android.database.Cursor;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

public class VOL1Fragment extends Fragment {
    public VOL1Fragment() {
    }

    TextView totalDistance, totalDuration, totalTraining, totalCalories, longestRun, best5K, best10K, best21K, best42K;
    SessionsDBHelper db;
    int totalRuns = 0;

    int totalMeters = 0;
    String kilometersToDisplay = "";

    int totalduration_in_minutes = 0;
    int totalduration_in_seconds = 0;

    static  int longestRunMeters = 0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vol1, container, false);


        //Declaring TextViews
        totalDistance = root.findViewById(R.id.total_km_data_textview);
        totalDuration = root.findViewById(R.id.total_duration_data_textview);
        totalTraining = root.findViewById(R.id.total_training_sessions_data_textview);
        totalCalories = root.findViewById(R.id.total_calories_data_textview);
        longestRun = root.findViewById(R.id.longest_run_data_textview);
        best5K = root.findViewById(R.id.best_5_data_textview);
        best10K = root.findViewById(R.id.best_10_data_textview);
        best21K = root.findViewById(R.id.best_HM_data_textview);
        best42K = root.findViewById(R.id.best_M_data_textview);


        //TODO maybe create a singletone Class to contain the database method to use in both activityies
        db = new SessionsDBHelper(getActivity());
        Cursor cursor = db.alldata();
        if (cursor.getCount() == 0) {
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                //----------------------GET TOTAL SESSIONS------------------------//
                totalRuns = totalRuns + 1;
                //-----------------------GET TOTAL METERS------------------------//
                totalMeters = totalMeters + cursor.getInt(3);
                //Check if its full kilometers and covert it back to int so the decimal dot won't be seen
                if (totalMeters % 1000 == 0) {
                    kilometersToDisplay = (totalMeters / 1000) + "";
                } else {
                    double distanceAsDouble = totalMeters;
                    kilometersToDisplay = (distanceAsDouble / 1000) + "";
                }
               //-----------------------GET TOTAL TIME STRING--------------------//
                //Break down and get total minutes

                String timeString = cursor.getString(2);
                String[] values = timeString.split(":");

                if (values.length == 2) {
                    int hours = 0;
                    int minutes = Integer.parseInt(values[0]);
                    int seconds = Integer.parseInt(values[1]);

                    totalduration_in_seconds = totalduration_in_seconds+ seconds;
                    totalduration_in_minutes =totalduration_in_minutes+minutes+hours;


                } else {
                    int hours = Integer.parseInt(values[0]);
                    int minutes = Integer.parseInt(values[1]);
                    int seconds = Integer.parseInt(values[2]);

                    totalduration_in_seconds = totalduration_in_seconds+ seconds;
                    totalduration_in_minutes =totalduration_in_minutes+minutes + (hours*60);
                }

                //Get Longest Run
                if(longestRunMeters<cursor.getInt(3)){

                    longestRunMeters = cursor.getInt(3);

                }



                Log.i("msch0", "ID:" + cursor.getInt(0));
                Log.i("msch01", "DATE:" + cursor.getString(1));
                Log.i("msch02", "Duration:" + cursor.getString(2));
                Log.i("msch03", "Meters:" + cursor.getInt(3));
                Log.i("msch04", "Week number:" + cursor.getInt(4));
                Log.i("msch04", "something:" + cursor.getString(5));


            }
            Log.i("LOG INFO:", "total minutes: " + totalRuns);

            Log.i("LOG INFO:", "total meters: " + totalRuns);
            Log.i("LOG INFO:", "total runs: " + totalRuns);
        }

        try {
            //Set Total Calories Statistics view
            double totalCal = Double.parseDouble(kilometersToDisplay);
            totalCalories.setText((int) (totalCal * 70) + "");
            //Set Total Distance Statistics view
            totalDistance.setText(kilometersToDisplay);
            //Set Total Sessions Number Statistics view
            totalTraining.setText(totalRuns + "");
            //Set Total Duration Statistics view

            int finalMinutesWithSeconds = (totalduration_in_minutes)+(totalduration_in_seconds/60);

            int finalHoursToDisplay = finalMinutesWithSeconds/60;


            int finalMinutesToDisplay = finalMinutesWithSeconds%60;

            if (finalMinutesToDisplay<10){
                totalDuration.setText(finalHoursToDisplay + ":0" + finalMinutesToDisplay);
            }else{
                totalDuration.setText(finalHoursToDisplay + ":" + finalMinutesToDisplay);
            }




            //Set Longest Run

            double longestToDisplay = longestRunMeters;

            longestRun.setText((longestToDisplay/1000)+"");
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        return root;
    }


}
