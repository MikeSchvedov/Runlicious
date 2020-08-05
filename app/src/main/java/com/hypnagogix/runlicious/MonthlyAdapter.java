package com.hypnagogix.runlicious;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MonthlyAdapter extends RecyclerView.Adapter<MonthlyAdapter.MonthlyViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public MonthlyAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }


    public class MonthlyViewHolder extends RecyclerView.ViewHolder {
        public TextView monthDatesTextView;
        public TextView monthDurationTextView;
        public TextView monthDistanceTextView;
        public TextView monthCaloriesTextView;


        public MonthlyViewHolder(@NonNull View itemView) {
            super(itemView);

            monthDatesTextView = itemView.findViewById(R.id.monthly_date);
            monthDurationTextView = itemView.findViewById(R.id.monthly_duration);
            monthDistanceTextView = itemView.findViewById(R.id.monthly_distance);
            monthCaloriesTextView = itemView.findViewById(R.id.monthly_calories);

        }
    }

    @NonNull
    @Override
    public MonthlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.monthly_item, parent, false);
        return new MonthlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MonthlyViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        //--------------------GET DATA FROM DATABASE----------------------//
        String datesData = mCursor.getString(mCursor.getColumnIndex(MonthlyContract.MonthlyEntry.COLUMN_DATES));
        int durationMinutesData = mCursor.getInt(mCursor.getColumnIndex(MonthlyContract.MonthlyEntry.COLUMN_TIME_MINUTES));
        int distanceMetersData = mCursor.getInt(mCursor.getColumnIndex(MonthlyContract.MonthlyEntry.COLUMN_DISTANCE_IN_METERS));
        long id = mCursor.getLong(mCursor.getColumnIndex(MonthlyContract.MonthlyEntry._ID));
        //Getting the week in the year
        int weekInYear = mCursor.getInt(mCursor.getColumnIndex(MonthlyContract.MonthlyEntry.COLUMN_WEEK_IN_YEAR));


        //=============ATTACH DATA FROM DATABASE TO HOLDERS============//





        //----------------------------ATTACH DATE----------------------//

        holder.monthDatesTextView.setText(datesData);

        //-------------------------ATTACH DURATION------------------//


        int finalHoursToDisplay = durationMinutesData/60;
        int finalMinutesToDisplay = durationMinutesData%60;

        if(finalHoursToDisplay==0){

            if (finalMinutesToDisplay<10){
                holder.monthDurationTextView.setText("0"+":0"+finalMinutesToDisplay);
            }else{
                holder.monthDurationTextView.setText("0"+":"+finalMinutesToDisplay);
            }

        }else{
            if (finalMinutesToDisplay<10){
                holder.monthDurationTextView.setText(finalHoursToDisplay+":0"+finalMinutesToDisplay);
            }else{
                holder.monthDurationTextView.setText(finalHoursToDisplay+":"+finalMinutesToDisplay);
            }

        }






        //-------------------------FORMAT AND ATTACH DISTANCE------------------//

        //Check if its full kilometer and covert it back to int so the decimal dot wont be seen
        if (distanceMetersData%1000==0){
            holder.monthDistanceTextView.setText(" "+distanceMetersData/1000 + " km");
        }else{
            double distanceAsDouble = distanceMetersData;
            holder.monthDistanceTextView.setText(" "+distanceAsDouble/1000 + " km");
        }
        //----------------------------ATTACH CALORIES----------------------//
        int calories = (distanceMetersData*70)/1000;

        holder.monthCaloriesTextView.setText(calories+"");

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
