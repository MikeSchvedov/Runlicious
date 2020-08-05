package com.hypnagogix.runlicious;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SessionsAdapter extends RecyclerView.Adapter<SessionsAdapter.GroceryViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public SessionsAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }


    public class GroceryViewHolder extends RecyclerView.ViewHolder {
        public TextView dateTextView;
        public TextView durationTextView;
        public TextView distanceTextView;
        public TextView paceTextView;


        public GroceryViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTextView = itemView.findViewById(R.id.session_date);
            durationTextView = itemView.findViewById(R.id.session_duration);
            distanceTextView = itemView.findViewById(R.id.session_distance);
            paceTextView = itemView.findViewById(R.id.session_pace);

        }
    }

    @NonNull
    @Override
    public GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.session_item, parent, false);
        return new GroceryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        //--------------------GET DATA FROM DATABASE----------------------//
        String dateData = mCursor.getString(mCursor.getColumnIndex(SessionsContract.GroceryEntry.COLUMN_NAME));
        String durationData = mCursor.getString(mCursor.getColumnIndex(SessionsContract.GroceryEntry.COLUMN_TIME));
        int distanceMetersData = mCursor.getInt(mCursor.getColumnIndex(SessionsContract.GroceryEntry.COLUMN_DISTANCE_IN_METERS));
        long id = mCursor.getLong(mCursor.getColumnIndex(SessionsContract.GroceryEntry._ID));

        //Getting the week in the year //TODO actually no use here only in the statistics section
        int weekInYear = mCursor.getInt(mCursor.getColumnIndex(SessionsContract.GroceryEntry.COLUMN_WEEK_IN_YEAR));


        //=============ATTACH DATA FROM DATABASE TO HOLDERS============//

        //----------------------------ATTACH DATE----------------------//

        holder.dateTextView.setText(dateData);

        //-------------------------ATTACH DURATION------------------//

        holder.durationTextView.setText(durationData);

        //-------------------------FORMAT AND ATTACH DISTANCE------------------//

        //Check if its full kilometer and covert it back to int so the decimal dot wont be seen
        if (distanceMetersData % 1000 == 0) {
            holder.distanceTextView.setText(" " + distanceMetersData / 1000 + " km");
        } else {
            double distanceAsDouble = distanceMetersData;
            holder.distanceTextView.setText(" " + distanceAsDouble / 1000 + " km");
        }


//-------------------------CALCULATE AND ATTACH AVG PACE------------------//
        //Duration String : durationData

        String[] values = durationData.split(":");

        if (values.length == 2) {
            int hours = 0;
            int minutes = Integer.parseInt(values[0]);
            int seconds = Integer.parseInt(values[1]);
            String toDisplay = calculatePace(hours, minutes, seconds, distanceMetersData);
            holder.paceTextView.setText(toDisplay);
        } else {
            int hours = Integer.parseInt(values[0]);
            int minutes = Integer.parseInt(values[1]);
            int seconds = Integer.parseInt(values[2]);
            String toDisplay = calculatePace(hours, minutes, seconds, distanceMetersData);
            holder.paceTextView.setText(toDisplay);
        }


        //--------------------ATTACH ID----------------------//
        holder.itemView.setTag(id);
    }

    private String calculatePace(int hours, int minutes, int seconds, int meters) {

        //Calculate total seconds
        int totalSeconds = (hours * 3600) + (minutes * 60) + seconds;
        Log.i("totalSeconds:", totalSeconds + "");

        double calculation1 = (double) totalSeconds / meters;
        Log.i("calculation1:", calculation1 + "");

        double secondsPerKilometer = calculation1 * 1000;
        Log.i("secondsPerKilometer:", secondsPerKilometer + "");

        double paceMinutes = secondsPerKilometer / 60;
        Log.i("paceMinutes:", paceMinutes + "");

        double paceSeconds = secondsPerKilometer % 60;
        Log.i("paceSeconds:", paceSeconds + "");

        //convert the double to integer
        int pm = (int) paceMinutes;
        Log.i("pm info:", pm + "");
        int sm = (int) paceSeconds;
        Log.i("sm info:", sm + "");

        String paceToDisplay ="";

        if (pm < 10) {
            if (sm < 10) {
                paceToDisplay = "0" + pm + ":0" + sm;
            } else {
               paceToDisplay = "0" + pm + ":" + sm;
            }

        } else {
            if (sm < 10) {
                paceToDisplay = pm + ":0" + sm;
            } else {
                paceToDisplay = pm + sm + "";
            }
        }


        return paceToDisplay;

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }


    }

}
