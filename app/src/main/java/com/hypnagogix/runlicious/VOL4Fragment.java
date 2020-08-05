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
import static com.hypnagogix.runlicious.YearlyContract.YearlyEntry.TABLE_NAME;
import com.github.mikephil.charting.charts.BarChart;

import java.util.ArrayList;

public class VOL4Fragment extends Fragment {


    private YearlyAdapter mAdapter;
    private SQLiteDatabase mDataBase;

    static int checker = 0;

    TextView yearDistance, yearDuration, yearDate;
    //To get sessions data
    SessionsDBHelper db;


    ArrayList<YearObject> allYearsArray = new ArrayList<>();
    ArrayList<Integer> yearNumberCollection = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vol4, container, false);



     //Getting Database to Write in
        YearlyDBHelper dbHelper = new YearlyDBHelper(getActivity());
        mDataBase = dbHelper.getWritableDatabase();
        //CONNECTING DATABASE TO RECYCLEVIEW
        RecyclerView recyclerView = root.findViewById(R.id.yearly_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new YearlyAdapter(getActivity(), getAllItems());
        recyclerView.setAdapter(mAdapter);

        //Declaring TextViews
        yearDistance = root.findViewById(R.id.yearly_distance);
        yearDuration = root.findViewById(R.id.yearly_duration);
        yearDate = root.findViewById(R.id.yearly_date);


        //GOING THOUGH DATABASE TO GATHER ALL EXISTING Months NUMBERS
        db = new SessionsDBHelper(getActivity());


        Cursor cursor_getSessionData = db.alldata();
        if (cursor_getSessionData.getCount() == 0) {
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
        } else {
            while (cursor_getSessionData.moveToNext()) {

                //Getting session full date and formatting to month number
                String currentSessionsDateString = cursor_getSessionData.getString(1);

                Log.i("/---------INFO--------/", "[At fragment4,yearly]  >>>Fetching New Data from database, its date is : " + currentSessionsDateString);


                String[] sessionsDateValues = currentSessionsDateString.split("/");
                int dDay = Integer.parseInt(sessionsDateValues[0]);
                int dMonth = Integer.parseInt(sessionsDateValues[1]);
                int dYear = Integer.parseInt(sessionsDateValues[2]);


                Log.i("/---------INFO--------/", "[At fragment4,yearly]  >>>this is the year number : " + dYear);

                //Checking to see if year number already exists in array
                if (yearNumberCollection.contains(dYear)) {
                    Log.i("/---------INFO--------/", "[At fragment4,yearly]  >>>Value Already exists. going to next data");

                } else {
                    yearNumberCollection.add(dYear);
                    Log.i("/---------INFO--------/", "[At fragment4,yearly]  >>>Adding it into the List");
                    Log.i("/---------INFO--------/", "[At fragment4,yearly]  >>>This is how the List Looks now:" + yearNumberCollection.toString());

                }

            }
        }


        //CREATING YEAR OBJECTS AND ADDING TO YEAR ARRAY USING THE NUMBERS GATHERED BEFORE

        try {
            for (Integer item : yearNumberCollection) {
                YearObject year = new YearObject(item);
                allYearsArray.add(year);

            }
            Log.i("/---------INFO--------/", "[At fragment4,Yearly]  >>>This is allYearsArray:" + allYearsArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
        }


        //Looping trough all sessions and adding their data to corresponding years
        Cursor cursor_putDataInsideMonths = db.alldata();
        if (cursor_putDataInsideMonths.getCount() == 0) {
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
        } else {
            while (cursor_putDataInsideMonths.moveToNext()) {

                //get this session's month number
                String dateString = cursor_putDataInsideMonths.getString(1);

                Log.i("/---------INFO--------/", "[At fragment4,yearly]  >>>This is the Date String :" + dateString);
                String[] dateValues = dateString.split("/");
                int ssDay = Integer.parseInt(dateValues[0]);
                int ssMonth = Integer.parseInt(dateValues[1]);
                int ssYear = Integer.parseInt(dateValues[2]);
                Log.i("/---------INFO--------/", "[At fragment4,yearly]  >>>ssMonth is :" + ssYear);


                //get this session's meters
                int thisSessionMeters = cursor_putDataInsideMonths.getInt(3);
                //get this session's duration and break it into minutes
                String thisSessionDuration = cursor_putDataInsideMonths.getString(2);
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

                for (YearObject year : allYearsArray) {
                    //if its the same year, add all session's data inside
                    if (ssYear == year.getFullYearDate()) {
                        year.setFullYearDistance(year.getFullYearDistance() + thisSessionMeters);
                        year.setFullYearDuration_In_Minutes(year.getFullYearDuration_In_Minutes() + sessionTotalMinutes);

                    }
                }
                Log.i("/---------INFO--------/", "[At fragment4,yearly]  >>>This is all years array :" + allYearsArray);

            } //while
        } //else


        //ADD YEARS OBJECTS TO DATABASE

        try {
            //Checking if data was already written
            if (checker == 0) {
                //Deleting all the database table
                clearTable();
                //Refilling the database table
                for (YearObject wo : allYearsArray) {

                    addSessionToDatabase(wo.getFullYearDate()+"", wo.getFullYearDuration_In_Minutes(), wo.getFullYearDistance(), wo.getFullYearDate());



                }
                checker = 1;


            }
        } catch (Exception e) {
            e.printStackTrace();

        }


        return root;


    }

    private void addSessionToDatabase(String dates, int mDuration, int mDistance, int monthInYear) {


        ContentValues cv = new ContentValues();

        cv.put(WeeklyContract.WeeklyEntry.COLUMN_DATES, dates);
        cv.put(WeeklyContract.WeeklyEntry.COLUMN_TIME_MINUTES, mDuration);
        cv.put(WeeklyContract.WeeklyEntry.COLUMN_DISTANCE_IN_METERS, mDistance);
        cv.put(WeeklyContract.WeeklyEntry.COLUMN_MONTH_NUMBER, monthInYear);
        mDataBase.insert(TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());


    }


    private void removeItem(long id) {
        mDataBase.delete(YearlyContract.YearlyEntry.TABLE_NAME, WeeklyContract.WeeklyEntry._ID + "=" + id, null);
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
                YearlyContract.YearlyEntry.COLUMN_DATES + " DESC"
        );


    }

}
