package com.hypnagogix.runlicious;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static com.hypnagogix.runlicious.WeeklyContract.WeeklyEntry.TABLE_NAME;

public class VOL2Fragment extends Fragment {

    private WeeklyAdapter mAdapter;
    private SQLiteDatabase mDataBase;

    static int checker = 0;

    TextView weeklyDistance, weeklyDuration, weeklyDate, weeklyCalories;
    //To get sessions data
    SessionsDBHelper db;


    ArrayList<WeekObject> allWeeksArray = new ArrayList<>();
    ArrayList<Integer> weekNumberCollection = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vol2, container, false);

        //Getting Database to Write in
        WeeklyDBHelper dbHelper = new WeeklyDBHelper(getActivity());
        mDataBase = dbHelper.getWritableDatabase();
        //CONNECTING DATABASE TO RECYCLEVIEW
        RecyclerView recyclerView = root.findViewById(R.id.weekly_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new WeeklyAdapter(getActivity(), getAllItems());
        recyclerView.setAdapter(mAdapter);

        //Declaring TextViews
        weeklyDistance = root.findViewById(R.id.week_distance);
        weeklyDuration = root.findViewById(R.id.week_duration);
        weeklyDate = root.findViewById(R.id.weekly_date);
        weeklyCalories = root.findViewById(R.id.week_calories);


        //GOING THOUGH DATABASE TO GATHER ALL EXISTING WEEK NUMBERS
        db = new SessionsDBHelper(getActivity());


        Cursor cursor_getWeeksNumber = db.alldata();
        if (cursor_getWeeksNumber.getCount() == 0) {
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
        } else {
            while (cursor_getWeeksNumber.moveToNext()) {

                int currentWeekNumber = cursor_getWeeksNumber.getInt(4);
                Log.i("/---------INFO--------/", "[At fragment2,weekly]  >>>Fetching New Data from database, its week number is : " + currentWeekNumber + ", and its date is: " + cursor_getWeeksNumber.getString(1));
                if (weekNumberCollection.contains(currentWeekNumber)) {
                    Log.i("/---------INFO--------/", "[At fragment2,weekly]  >>>Value Already exists");

                } else {
                    weekNumberCollection.add(currentWeekNumber);
                    Log.i("/---------INFO--------/", "[At fragment2,weekly]  >>>Adding it into the List");
                    Log.i("/---------INFO--------/", "[At fragment2,weekly]  >>>This is how the List Looks now:" + weekNumberCollection.toString());

                }
            }
        }

        //CREATING WEEK OBJECTS AND ADDING TO WEEK ARRAY USING THE NUMBERS GATHERED BEFORE
        try {
            for (Integer item : weekNumberCollection) {
                WeekObject week = new WeekObject(item);
                allWeeksArray.add(week);
            }
            Log.i("/---------INFO--------/", "[At fragment2,weekly]  >>>This is allWeeksArray:" + allWeeksArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
        }


        //Looping trough all sessions and adding their data to corresponding weeks
        Cursor cursor_putDataInsideWeeks = db.alldata();
        if (cursor_putDataInsideWeeks.getCount() == 0) {
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
        } else {
            while (cursor_putDataInsideWeeks.moveToNext()) {
                //get this session's week number
                int thisSessionWeekNumber = cursor_putDataInsideWeeks.getInt(4);
                //get this session's meters
                int thisSessionMeters = cursor_putDataInsideWeeks.getInt(3);
                //get this session's duration and break it into minutes
                String thisSessionDuration = cursor_putDataInsideWeeks.getString(2);
                String[] durationValues = thisSessionDuration.split(":");

                int sessionTotalMinutes;
                if (durationValues.length == 2) {
                    int sHours = 0;
                    int sMinutes = Integer.parseInt(durationValues[0]);
                    int sSeconds = Integer.parseInt(durationValues[1]);

                    int minOutOfSecs = 0;
                    if (sSeconds > 30) {
                        minOutOfSecs = 1;
                    }
                    sessionTotalMinutes = (sHours * 60) + (sMinutes) + (minOutOfSecs);
                } else {
                    int sHours = Integer.parseInt(durationValues[0]);
                    int sMinutes = Integer.parseInt(durationValues[1]);
                    int sSeconds = Integer.parseInt(durationValues[2]);

                    int minOutOfSecs = 0;
                    if (sSeconds > 30) {
                        minOutOfSecs = 1;
                    }
                    sessionTotalMinutes = (sHours * 60) + (sMinutes) + (minOutOfSecs);
                }


                for (WeekObject week : allWeeksArray) {
                    //if its the same week, add all session's data inside
                    if (thisSessionWeekNumber == week.getWeekNumber()) {
                        week.setFullWeekDistance(week.getFullWeekDistance() + thisSessionMeters);
                        week.setFullWeekDuration_In_Minutes(week.getFullWeekDuration_In_Minutes() + sessionTotalMinutes);
                        week.setFullWeekCalories(week.getFullWeekCalories() + (thisSessionMeters * 70) / 1000);
                        week.setWeeksFirstDate(week.getWeeksFirstDate());
                    }
                }


            } //while
        } //else



        //checking if data was already written

        try {
            if (checker == 0) {
                //Deleting all the database table
                clearTable();
                //Refilling the database table
                for (WeekObject wo : allWeeksArray) {

                    String firstdate = wo.getWeeksFirstDate();
                    String lastdate = wo.getWeeksLastDate();
                    String fullDates = firstdate + " - " + lastdate;


                    Log.i("/---------INFO--------/", "[At fragment2,weekly]  >>>This is all Weeks duration:" + wo.getFullWeekDuration_In_Minutes());

                    addSessionToDatabase(wo.getWeekNumber(), fullDates, wo.getFullWeekDuration_In_Minutes(), wo.getFullWeekDistance());
                }
                checker = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

        return root;
    }


    private void addSessionToDatabase(int weekInYear, String dates, int mDuration, int mDistance) {

        ContentValues cv = new ContentValues();

        cv.put(WeeklyContract.WeeklyEntry.COLUMN_MONTH_NUMBER, weekInYear);
        cv.put(WeeklyContract.WeeklyEntry.COLUMN_DATES, dates);
        cv.put(WeeklyContract.WeeklyEntry.COLUMN_TIME_MINUTES, mDuration);
        cv.put(WeeklyContract.WeeklyEntry.COLUMN_DISTANCE_IN_METERS, mDistance);
        mDataBase.insert(TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());

    }


    private void removeItem(long id) {
        mDataBase.delete(TABLE_NAME, WeeklyContract.WeeklyEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());

    }


    public void clearTable() {
        mDataBase.delete(TABLE_NAME, null, null);
    }


    private Cursor getAllItems() {
        return mDataBase.query(
                TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                WeeklyContract.WeeklyEntry.COLUMN_MONTH_NUMBER + " DESC"
        );


    }
}

