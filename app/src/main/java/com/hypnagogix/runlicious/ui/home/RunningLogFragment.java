package com.hypnagogix.runlicious.ui.home;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypnagogix.runlicious.MainActivity;
import com.hypnagogix.runlicious.SessionsAdapter;
import com.hypnagogix.runlicious.SessionsContract;
import com.hypnagogix.runlicious.SessionsDBHelper;
import com.hypnagogix.runlicious.R;
import com.ikovac.timepickerwithseconds.MyTimePickerDialog;
import com.ikovac.timepickerwithseconds.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class RunningLogFragment extends Fragment {


    private SessionsAdapter mAdapter;
    private SQLiteDatabase mDataBase;

    RelativeLayout newSession;
    TextView mInputDate, mInputTime, dataStorageView;
    Button addNewSession, saveBTN;
    EditText mInputDistance;
    DatePickerDialog.OnDateSetListener mDateSetListener;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View root = inflater.inflate(R.layout.fragment_home, container, false);

        //CONNECTING DATABASE TO RECYCLEVIEW
        SessionsDBHelper dbHelper = new SessionsDBHelper(getActivity());
        mDataBase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = root.findViewById(R.id.admin_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new SessionsAdapter(getActivity(), getAllItems());
        recyclerView.setAdapter(mAdapter);

        //DECLARATION SECTION
        mInputDistance = root.findViewById(R.id.distance_edittext);
        mInputDate = root.findViewById(R.id.date_edittext);
        mInputTime = root.findViewById(R.id.time_edittext);
        addNewSession = root.findViewById(R.id.add_session_btn);
        saveBTN = root.findViewById(R.id.btn_save_entry);
        newSession = root.findViewById(R.id.new_session_layout);
        dataStorageView =root.findViewById(R.id.dataStorageView);


        //SETTING ITEM TOUCH OPTION
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {

                new AlertDialog.Builder(getActivity(), R.style.AlertDialog)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Warning")
                        .setMessage("Are you sure you want to delete the entry?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //If yes then delete
                                removeItem((long) viewHolder.itemView.getTag());

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();








            }
        }).attachToRecyclerView(recyclerView);


        //On Click for input fields (Set Date, Set Duration, Set Distance)
        mInputDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Dialog_NoActionBar_MinWidth,
                        mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Confirm", dialog);
                dialog.setButton(DatePickerDialog.BUTTON_NEGATIVE, "Cancel", dialog);
                dialog.show();
            }
        });


        mInputTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
                View promptView = layoutInflater.inflate(R.layout.premium_alert, null);

                final AlertDialog alertD = new AlertDialog.Builder(getActivity()).create();

                Button cancelBTN = promptView.findViewById(R.id.cancel_btn_time_picker);
                Button confirmBTN = promptView.findViewById(R.id.confirm_btn_time_picker);

                final EditText inputHours = promptView.findViewById(R.id.input_hours_edittext);
                final EditText inputMinutes = promptView.findViewById(R.id.input_minutes_edittext);
                final EditText inputSeconds = promptView.findViewById(R.id.input_seconds_edittext);


                mInputDistance.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            mInputDistance.setHint("(Meters)");
                            mInputDistance.setHintTextColor(Color.parseColor("#794FD9"));
                        } else
                            mInputDistance.setHint("Set Distance");
                        mInputDistance.setHintTextColor(Color.WHITE);
                    }
                });

                //Cancel button inside time dialog
                cancelBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertD.cancel();

                    }
                });
                //Confirm button inside time dialog
                confirmBTN.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Getting duration data from view
                        String hours = inputHours.getText().toString();
                        String minutes = inputMinutes.getText().toString();
                        String seconds = inputSeconds.getText().toString();


                        //Handling Wrong User Input
                        if (hours.equals("") || minutes.equals("") || seconds.equals("")){
                            Toast.makeText(getActivity(), "Fields are empty! \nPlease use HH/MM/SS format", Toast.LENGTH_LONG).show();
                            return;
                        }


                        int HH = Integer.parseInt(hours);
                        int MM = Integer.parseInt(minutes);
                        int SS = Integer.parseInt(seconds);


                        if(MM>=60){
                            Toast.makeText(getActivity(), "Invalid value!\n(Minutes shouldn't be over 60)", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if(SS >= 60){
                            Toast.makeText(getActivity(), "Invalid value!\n(Seconds shouldn't be over 60)", Toast.LENGTH_LONG).show();
                            return;
                        }


                        if(HH<10 && hours.length() != 2){
                            hours = "0"+hours;
                        }
                        if(MM<10&& minutes.length() != 2){
                            minutes = "0"+minutes;
                        }
                        if(SS<10&& seconds.length() != 2){
                            seconds = "0"+seconds;
                        }



                        if (HH==0 || hours.equals("")){
                            mInputTime.setText(minutes + ":" + seconds);
                        }else{
                            mInputTime.setText(hours+":"+minutes + ":" + seconds);
                        }


                        alertD.cancel();

                    }
                });


                alertD.setView(promptView);

                alertD.show();


            }
        });


        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;

                //-------------Get day of the week - from your date.--------------//

                Calendar cal1 = Calendar.getInstance();
                cal1.set(Calendar.DAY_OF_MONTH, dayOfMonth );
                cal1.set(Calendar.MONTH, month-1);
                cal1.set(Calendar.YEAR, year );
                int week_of_year = cal1.get(Calendar.WEEK_OF_YEAR);
                int current_year = cal1.get(Calendar.YEAR);


                dataStorageView.setText(current_year +""+week_of_year);


                String date = dayOfMonth + "/" + month + "/" + year;
                mInputDate.setText(date);
            }
        };




        addNewSession.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newSession.setVisibility(View.VISIBLE);
                addNewSession.setVisibility(View.GONE);

            }
        });

        saveBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newSession.setVisibility(View.GONE);
                addNewSession.setVisibility(View.VISIBLE);
                addSessionToDatabase();

                //Clearing data from view to default text
                mInputTime.setText("");
                mInputDate.setText("");
                mInputDistance.setText("");
dataStorageView.setText("");

            }
        });


        return root;
    }


    private void addSessionToDatabase() {
        //Getting amount from 'mEditTextAmount' to string, then converting back to int


        String mDuration = mInputTime.getText().toString();


        int mDistance = 0;
        try {
            mDistance = Integer.parseInt(mInputDistance.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        //Checking ensure that there is no missing data.
        if (mInputDate.getText().toString().trim().length() == 0 || mInputTime.getText().toString().trim().length() == 0 || mDistance == 0) {
            return;
        }
        String date = mInputDate.getText().toString();



        int weekInYear = Integer.parseInt(dataStorageView.getText().toString());





        ContentValues cv = new ContentValues();

        cv.put(SessionsContract.GroceryEntry.COLUMN_WEEK_IN_YEAR, weekInYear);

        cv.put(SessionsContract.GroceryEntry.COLUMN_NAME, date);
        cv.put(SessionsContract.GroceryEntry.COLUMN_TIME, mDuration);
        cv.put(SessionsContract.GroceryEntry.COLUMN_DISTANCE_IN_METERS, mDistance);
        mDataBase.insert(SessionsContract.GroceryEntry.TABLE_NAME, null, cv);
        mAdapter.swapCursor(getAllItems());


    }

    private void removeItem(long id) {
        mDataBase.delete(SessionsContract.GroceryEntry.TABLE_NAME, SessionsContract.GroceryEntry._ID + "=" + id, null);
        mAdapter.swapCursor(getAllItems());

    }

    private Cursor getAllItems() {
        return mDataBase.query(
                SessionsContract.GroceryEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                SessionsContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );


    }
}
