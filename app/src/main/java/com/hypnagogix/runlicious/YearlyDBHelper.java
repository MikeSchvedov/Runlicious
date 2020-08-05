package com.hypnagogix.runlicious;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hypnagogix.runlicious.YearlyContract.YearlyEntry;


public class YearlyDBHelper extends SQLiteOpenHelper {

    public static final String DATEBASE_NAME = "yearlylist.db";
    public static final int DATABASE_VERSION = 1;

    public YearlyDBHelper(@Nullable Context context) {
        super(context, DATEBASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_WEEKSLIST_TABLE = "CREATE TABLE " +
                YearlyEntry.TABLE_NAME + " (" +
                YearlyEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                YearlyEntry.COLUMN_DATES + " TEXT NOT NULL, " +
                YearlyEntry.COLUMN_TIME_MINUTES + " INTEGER NOT NULL, " +
                YearlyEntry.COLUMN_DISTANCE_IN_METERS + " INTEGER NOT NULL, " +
                YearlyEntry.COLUMN_MONTH_NUMBER + " INTEGER NOT NULL, " +
                YearlyEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
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
        db.execSQL("DROP TABLE IF EXISTS " + YearlyEntry.TABLE_NAME);
        onCreate(db);
    }
}
