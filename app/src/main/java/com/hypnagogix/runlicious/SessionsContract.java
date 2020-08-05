package com.hypnagogix.runlicious;

import android.provider.BaseColumns;

public class SessionsContract {

    private SessionsContract(){}

    public static final class GroceryEntry implements BaseColumns {

        public static final String TABLE_NAME = "sessionsList";
        public static final String COLUMN_NAME = "date";
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DISTANCE_IN_METERS = "distance";
        public static final String COLUMN_WEEK_IN_YEAR = "week_in_year";
        public static final String COLUMN_TIMESTAMP = "timestamp";

    }
}
