package com.hypnagogix.runlicious;

import android.provider.BaseColumns;

public class MonthlyContract {

    private MonthlyContract(){}

    public static final class MonthlyEntry implements BaseColumns {

        public static final String TABLE_NAME = "sessionsList";
        public static final String COLUMN_DATES = "dates";
        public static final String COLUMN_TIME_MINUTES = "time";
        public static final String COLUMN_DISTANCE_IN_METERS = "distance";
        public static final String COLUMN_WEEK_IN_YEAR = "week_in_year";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
