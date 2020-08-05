package com.hypnagogix.runlicious;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hypnagogix.runlicious.WeeklyContract.WeeklyEntry;


public class MonthlyDBHelper extends SQLiteOpenHelper {

    public static final String DATEBASE_NAME = "monthlist.db";
    public static final int DATABASE_VERSION = 1;

    public MonthlyDBHelper(@Nullable Context context) {
        super(context, DATEBASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEEKSLIST_TABLE = "CREATE TABLE " +
                WeeklyEntry.TABLE_NAME + " (" +
                WeeklyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WeeklyEntry.COLUMN_DATES + " TEXT NOT NULL, " +
                WeeklyEntry.COLUMN_TIME_MINUTES + " INTEGER NOT NULL, " +
                WeeklyEntry.COLUMN_DISTANCE_IN_METERS + " INTEGER NOT NULL, " +
                WeeklyEntry.COLUMN_MONTH_NUMBER + " INTEGER NOT NULL, " +
                WeeklyEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_WEEKSLIST_TABLE);
    }

    public Cursor alldata() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from sessionsList", null);
        return cursor;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + WeeklyEntry.TABLE_NAME);
        onCreate(db);
    }
}
