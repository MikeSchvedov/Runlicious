package com.hypnagogix.runlicious;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import static com.hypnagogix.runlicious.MonthlyContract.MonthlyEntry.TABLE_NAME;


public class VOL3Fragment extends Fragment {

    BarChart barChart;


    private MonthlyAdapter mAdapter;
    private SQLiteDatabase mDataBase;

    static int checker = 0;

    TextView monthlyDistance, monthlyDuration, monthlyDate, monthlyCalories;
    //To get sessions data
    SessionsDBHelper db;


    //To get monthly data
    MonthlyDBHelper db_monthly;


    ArrayList<MonthObject> allMonthsArray = new ArrayList<>();
    ArrayList<Integer> monthNumberCollection = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_vol3, container, false);

        barChart = root.findViewById(R.id.bargraph);


        //Getting Database to Write in
        MonthlyDBHelper dbHelper = new MonthlyDBHelper(getActivity());
        mDataBase = dbHelper.getWritableDatabase();
        //CONNECTING DATABASE TO RECYCLEVIEW
        RecyclerView recyclerView = root.findViewById(R.id.month_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new MonthlyAdapter(getActivity(), getAllItems());
        recyclerView.setAdapter(mAdapter);

        //Declaring TextViews
        monthlyDistance = root.findViewById(R.id.monthly_distance);
        monthlyDuration = root.findViewById(R.id.monthly_duration);
        monthlyDate = root.findViewById(R.id.monthly_date);
        monthlyCalories = root.findViewById(R.id.monthly_calories);


        //GOING THOUGH DATABASE TO GATHER ALL EXISTING Months NUMBERS
        db = new SessionsDBHelper(getActivity());


        Cursor cursor_getMonthsNumber = db.alldata();
        if (cursor_getMonthsNumber.getCount() == 0) {
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
        } else {
            while (cursor_getMonthsNumber.moveToNext()) {

                //Getting session full date and formatting to month number
                String currentSessionsDateString = cursor_getMonthsNumber.getString(1);

                String[] sessionsDateValues = currentSessionsDateString.split("/");
                int dDay = Integer.parseInt(sessionsDateValues[0]);
                int dMonth = Integer.parseInt(sessionsDateValues[1]);
                int dYear = Integer.parseInt(sessionsDateValues[2]);

                String tempDate;

                if (dMonth < 10) {
                    tempDate = dYear + "0" + dMonth;
                } else {
                    tempDate = dYear + "" + dMonth;
                }

                int currentSessionsMonthNumber = Integer.parseInt(tempDate);
                Log.i("/---------INFO--------/", "[At fragment3,monthly]  >>>Fetching New Data from database, its month number is : " + currentSessionsMonthNumber);

                //Checking to see if month number already exists in array
                if (monthNumberCollection.contains(currentSessionsMonthNumber)) {
                    Log.i("/---------INFO--------/", "[At fragment3,monthly]  >>>Value Already exists. going to next data");

                } else {
                    monthNumberCollection.add(currentSessionsMonthNumber);
                    Log.i("/---------INFO--------/", "[At fragment3,monthly]  >>>Adding it into the List");
                    Log.i("/---------INFO--------/", "[At fragment3,monthly]  >>>This is how the List Looks now:" + monthNumberCollection.toString());

                }

            }
        }


        //CREATING MONTH OBJECTS AND ADDING TO MONTH ARRAY USING THE NUMBERS GATHERED BEFORE

        try {
            for (Integer item : monthNumberCollection) {
                MonthObject month = new MonthObject(item);
                allMonthsArray.add(month);


            }
            Log.i("/---------INFO--------/", "[At fragment3,monthly]  >>>This is allMonthsArray:" + allMonthsArray.toString());
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
        }


        //Looping trough all sessions and adding their data to corresponding weeks
        Cursor cursor_putDataInsideMonths = db.alldata();
        if (cursor_putDataInsideMonths.getCount() == 0) {
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
        } else {
            while (cursor_putDataInsideMonths.moveToNext()) {

                //get this session's month number
                String dateString = cursor_putDataInsideMonths.getString(1);

                Log.i("/---------INFO--------/", "[At fragment3,monthly]  >>>This is the Date String :" + dateString);
                String[] dateValues = dateString.split("/");
                int ssDay = Integer.parseInt(dateValues[0]);
                int ssMonth = Integer.parseInt(dateValues[1]);
                int ssYear = Integer.parseInt(dateValues[2]);
                Log.i("/---------INFO--------/", "[At fragment3,monthly]  >>>ssMonth is :" + ssMonth);
                String tempDate2;

                if (ssMonth < 10) {

                    tempDate2 = ssYear + "0" + ssMonth;
                } else {

                    tempDate2 = ssYear + "" + ssMonth;
                }

                int thisSessionMonthNumber = Integer.parseInt(tempDate2);

                String monthAsName = "";

                switch (ssMonth) {

                    case 1:
                        monthAsName = "January";
                        break;
                    case 2:
                        monthAsName = "February";
                        break;
                    case 3:
                        monthAsName = "March";
                        break;
                    case 4:
                        monthAsName = "April";
                        break;
                    case 5:
                        monthAsName = "May";
                        break;
                    case 6:
                        monthAsName = "June";
                        break;
                    case 7:
                        monthAsName = "July";
                        break;
                    case 8:
                        monthAsName = "August";
                        break;
                    case 9:
                        monthAsName = "September";
                        break;
                    case 10:
                        monthAsName = "October";
                        break;
                    case 11:
                        monthAsName = "November";
                        break;
                    case 12:
                        monthAsName = "December";
                }


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

                for (MonthObject month : allMonthsArray) {
                    //if its the same week, add all session's data inside
                    if (thisSessionMonthNumber == month.getMonthNumber()) {
                        month.setFullMonthDistance(month.getFullMonthDistance() + thisSessionMeters);
                        month.setFullMonthDuration_In_Minutes(month.getFullMonthDuration_In_Minutes() + sessionTotalMinutes);
                        month.setFullMonthDate(monthAsName + "/" + ssYear);
                    }
                }


            } //while
        } //else


        //ADD MONTH OBJECTS TO DATABASE

        try {
            //Checking if data was already written
            if (checker == 0) {
                //Deleting all the database table
                clearTable();
                //Refilling the database table
                for (MonthObject wo : allMonthsArray) {

                    addSessionToDatabase(wo.getMonthNumber(), wo.getFullMonthDate(), wo.getFullMonthDuration_In_Minutes(), wo.getFullMonthDistance());


                    //Add new month to MAP array

                    String monthNumber = wo.getMonthNumber() + "";


                }
                checker = 1;


            }
        } catch (Exception e) {
            e.printStackTrace();

        }


        //Getting data from database into Bar Chart
        AddDataToChart();


        return root;
    }

    private void AddDataToChart() {

        ArrayList<String> monthsName = new ArrayList<>();
        ArrayList<Integer> monthsDuration = new ArrayList<>();


        //GOING THOUGH DATABASE TO GATHER MONTHLY DATA FOR BAR CHART


        db_monthly = new MonthlyDBHelper(getActivity());

        Cursor cursor_getdata = db_monthly.alldata();
        if (cursor_getdata.getCount() == 0) {
            Toast.makeText(getActivity(), "NO DATA", Toast.LENGTH_LONG).show();
        } else {
            while (cursor_getdata.moveToNext()) {

                monthsName.add(cursor_getdata.getString(4));
                monthsDuration.add(cursor_getdata.getInt(3));


                Log.i("/---------INFO--------/", "[DATABASE DATA]  >>>this is the full month key value array :" + monthsName + " and " + monthsDuration);

            }
            //ADD data to BAR CHART
            addMonthsToChart(monthsName, monthsDuration);
        }


    }

    private void addMonthsToChart(ArrayList<String> monthsName, ArrayList<Integer> monthsDuration) {

        int counter = 0;


        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> theDates = new ArrayList<>();
      //Loop over the monthsName array to get data
        for (String name : monthsName) {

            int nameAsInt = Integer.parseInt(name);

            nameAsInt = nameAsInt%100;

            String newName = "";

            switch (nameAsInt) {

                case 1:
                    newName = "Jan";
                    break;
                case 2:
                    newName = "Feb";
                    break;
                case 3:
                    newName = "Mar";
                    break;
                case 4:
                    newName = "Apr";
                    break;
                case 5:
                    newName = "May";
                    break;
                case 6:
                    newName = "Jun";
                    break;
                case 7:
                    newName = "Jul";
                    break;
                case 8:
                    newName = "Aug";
                    break;
                case 9:
                    newName = "Sep";
                    break;
                case 10:
                    newName = "Oct";
                    break;
                case 11:
                    newName = "Nov";
                    break;
                case 12:
                    newName = "Dec";
            }




            theDates.add(newName);
            Log.i("/---------INFO--------/", "[DATABASE DATA]  >>>this is the entry name :" + newName);
        }

        //Loop over the monthsDuration array to get data
        for (Integer num : monthsDuration) {

            barEntries.add(new BarEntry(num / 1000, counter));
            Log.i("/---------INFO--------/", "[DATABASE DATA]  >>>this is the entry value :" + num / 1000 + " this is the counter" + counter);

            counter += 1;
        }


        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");
        barDataSet.setColor(Color.parseColor("#794FD9"));
        barDataSet.setBarSpacePercent(50f);

        BarData theData = new BarData(theDates, barDataSet);


        barChart.setData(theData);

        barChart.setTouchEnabled(false);
        barChart.setDragEnabled(false);
        barChart.setScaleEnabled(false);

        barChart.getLegend().setEnabled(false);
        barChart.setDrawBorders(true);
        barChart.setBorderWidth(3f);
        barChart.setBorderColor(Color.parseColor("#F78B36C5"));
        barChart.setDescription("");


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setTextColor(Color.parseColor("#FFFFFF"));
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setDrawGridLines(false);
        yAxis.setTextSize(10f); // set the text size
        yAxis.setAxisMinValue(0f);
        yAxis.setAxisMaxValue(150f); // the axis maximum is 100
        yAxis.setTextColor(Color.WHITE);
        yAxis.setGranularity(1f); // interval 1
        yAxis.setLabelCount(6, true); // force 6 labels


    }


    private void addSessionToDatabase(int monthInYear, String dates, int mDuration, int mDistance) {


        ContentValues cv = new ContentValues();

        cv.put(WeeklyContract.WeeklyEntry.COLUMN_MONTH_NUMBER, monthInYear);
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
                MonthlyContract.MonthlyEntry.COLUMN_WEEK_IN_YEAR + " DESC"
        );


    }
}
