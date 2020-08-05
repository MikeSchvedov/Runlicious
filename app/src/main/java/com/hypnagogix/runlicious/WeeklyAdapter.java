package com.hypnagogix.runlicious;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WeeklyAdapter extends RecyclerView.Adapter<WeeklyAdapter.WeeklyViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public WeeklyAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }


    public class WeeklyViewHolder extends RecyclerView.ViewHolder {
        public TextView weekDatesTextView;
        public TextView weekDurationTextView;
        public TextView weekDistanceTextView;
        public TextView weekCaloriesTextView;


        public WeeklyViewHolder(@NonNull View itemView) {
            super(itemView);

            weekDatesTextView = itemView.findViewById(R.id.weekly_date);
            weekDurationTextView = itemView.findViewById(R.id.week_duration);
            weekDistanceTextView = itemView.findViewById(R.id.week_distance);
            weekCaloriesTextView = itemView.findViewById(R.id.week_calories);

        }
    }

    @NonNull
    @Override
    public WeeklyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.weekly_item, parent, false);
        return new WeeklyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeeklyViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        //--------------------GET DATA FROM DATABASE----------------------//
        String datesData = mCursor.getString(mCursor.getColumnIndex(WeeklyContract.WeeklyEntry.COLUMN_DATES));
        int durationMinutesData = mCursor.getInt(mCursor.getColumnIndex(WeeklyContract.WeeklyEntry.COLUMN_TIME_MINUTES));
        int distanceMetersData = mCursor.getInt(mCursor.getColumnIndex(WeeklyContract.WeeklyEntry.COLUMN_DISTANCE_IN_METERS));
        long id = mCursor.getLong(mCursor.getColumnIndex(WeeklyContract.WeeklyEntry._ID));
        //Getting the week in the year
        int weekInYear = mCursor.getInt(mCursor.getColumnIndex(WeeklyContract.WeeklyEntry.COLUMN_MONTH_NUMBER));


        //=============ATTACH DATA FROM DATABASE TO HOLDERS============//





        //----------------------------ATTACH DATE----------------------//

        holder.weekDatesTextView.setText(datesData);

        //-------------------------ATTACH DURATION------------------//


        int finalHoursToDisplay = durationMinutesData/60;
        int finalMinutesToDisplay = durationMinutesData%60;

        if(finalHoursToDisplay==0){

            if (finalMinutesToDisplay<10){
                holder.weekDurationTextView.setText("0"+":0"+finalMinutesToDisplay);
            }else{
                holder.weekDurationTextView.setText("0"+":"+finalMinutesToDisplay);
            }

        }else{
            if (finalMinutesToDisplay<10){
                holder.weekDurationTextView.setText(finalHoursToDisplay+":0"+finalMinutesToDisplay);
            }else{
                holder.weekDurationTextView.setText(finalHoursToDisplay+":"+finalMinutesToDisplay);
            }

        }






        //-------------------------FORMAT AND ATTACH DISTANCE------------------//

        //Check if its full kilometer and covert it back to int so the decimal dot wont be seen
        if (distanceMetersData%1000==0){
            holder.weekDistanceTextView.setText(" "+distanceMetersData/1000 + " km");
        }else{
            double distanceAsDouble = distanceMetersData;
            holder.weekDistanceTextView.setText(" "+distanceAsDouble/1000 + " km");
        }
        //----------------------------ATTACH CALORIES----------------------//
        int calories = (distanceMetersData*70)/1000;

        holder.weekCaloriesTextView.setText(calories+"");

        //--------------------ATTACH ID----------------------//
        holder.itemView.setTag(id);
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
