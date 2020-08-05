package com.hypnagogix.runlicious;

import android.provider.BaseColumns;

public class WeeklyContract {

    private WeeklyContract(){}

    public static final class WeeklyEntry implements BaseColumns {

        public static final String TABLE_NAME = "sessionsList";
        public static final String COLUMN_DATES = "dates";
        public static final String COLUMN_TIME_MINUTES = "time";
        public static final String COLUMN_DISTANCE_IN_METERS = "distance";
        public static final String COLUMN_MONTH_NUMBER = "week_in_year";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
