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

public class YearlyAdapter extends RecyclerView.Adapter<YearlyAdapter.YearlyViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public YearlyAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }


    public class YearlyViewHolder extends RecyclerView.ViewHolder {
        public TextView yearDatesTextView;
        public TextView yearDurationTextView;
        public TextView yearDistanceTextView;



        public YearlyViewHolder(@NonNull View itemView) {
            super(itemView);

            yearDatesTextView = itemView.findViewById(R.id.yearly_date);
            yearDurationTextView = itemView.findViewById(R.id.yearly_duration);
            yearDistanceTextView = itemView.findViewById(R.id.yearly_distance);


        }
    }

    @NonNull
    @Override
    public YearlyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.yearly_item, parent, false);
        return new YearlyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YearlyViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }
        //--------------------GET DATA FROM DATABASE----------------------//
        String datesData = mCursor.getString(mCursor.getColumnIndex(YearlyContract.YearlyEntry.COLUMN_DATES));
        int durationMinutesData = mCursor.getInt(mCursor.getColumnIndex(YearlyContract.YearlyEntry.COLUMN_TIME_MINUTES));
        int distanceMetersData = mCursor.getInt(mCursor.getColumnIndex(YearlyContract.YearlyEntry.COLUMN_DISTANCE_IN_METERS));
        long id = mCursor.getLong(mCursor.getColumnIndex(YearlyContract.YearlyEntry._ID));
        //Getting the week in the year
        int weekInYear = mCursor.getInt(mCursor.getColumnIndex(YearlyContract.YearlyEntry.COLUMN_MONTH_NUMBER));


        //=============ATTACH DATA FROM DATABASE TO HOLDERS============//





        //----------------------------ATTACH DATE----------------------//

        holder.yearDatesTextView.setText(datesData);

        //-------------------------ATTACH DURATION------------------//

        Log.i("/---------INFO--------/", "[At YearlyAdapter,yearly]  >>>This is the total duration minutes" + durationMinutesData);
        int finalHoursToDisplay = durationMinutesData/60;
        Log.i("/---------INFO--------/", "[At YearlyAdapter,yearly]  >>>This is the final hours to display" + finalHoursToDisplay);
        int finalMinutesToDisplay = durationMinutesData%60;
        Log.i("/---------INFO--------/", "[At YearlyAdapter,yearly]  >>>This is the final minutes to display" + finalMinutesToDisplay);

        if(finalHoursToDisplay==0){

            if (finalMinutesToDisplay<10){
                holder.yearDurationTextView.setText("0"+":0"+finalMinutesToDisplay);
                Log.i("/---------INFO--------/", "[At YearlyAdapter,yearly]  >>>This is all years array :" + "0"+":0"+finalMinutesToDisplay);
            }else{
                holder.yearDurationTextView.setText("0"+":"+finalMinutesToDisplay);
                Log.i("/---------INFO--------/", "[At YearlyAdapter,yearly]  >>>This is all years array :" + "0"+":"+finalMinutesToDisplay);
            }

        }else{
            if (finalMinutesToDisplay<10){
                holder.yearDurationTextView.setText(finalHoursToDisplay+":0"+finalMinutesToDisplay);
                Log.i("/---------INFO--------/", "[At YearlyAdapter,yearly]  >>>This is all years array :" + finalHoursToDisplay+":0"+finalMinutesToDisplay);
            }else{
                holder.yearDurationTextView.setText(finalHoursToDisplay+":"+finalMinutesToDisplay);
                Log.i("/---------INFO--------/", "[At YearlyAdapter,yearly]  >>>This is all years array :" + finalMinutesToDisplay+":"+finalMinutesToDisplay);
            }

        }






        //-------------------------FORMAT AND ATTACH DISTANCE------------------//

        //Check if its full kilometer and covert it back to int so the decimal dot wont be seen
        if (distanceMetersData%1000==0){
            holder.yearDistanceTextView.setText(" "+distanceMetersData/1000 + " km");
        }else{
            double distanceAsDouble = distanceMetersData;
            holder.yearDistanceTextView.setText(" "+distanceAsDouble/1000 + " km");
        }

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
