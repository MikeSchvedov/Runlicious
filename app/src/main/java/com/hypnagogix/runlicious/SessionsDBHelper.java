package com.hypnagogix.runlicious;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.hypnagogix.runlicious.SessionsContract.*;

public class SessionsDBHelper extends SQLiteOpenHelper {

    public static final String DATEBASE_NAME = "sessionslist.db";
    public static final int DATABASE_VERSION = 1;

    public SessionsDBHelper(@Nullable Context context) {
        super(context, DATEBASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SESSIONSLIST_TABLE = "CREATE TABLE " +
                GroceryEntry.TABLE_NAME + " (" +
                GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                GroceryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                GroceryEntry.COLUMN_TIME + " TEXT NOT NULL, " +
                GroceryEntry.COLUMN_DISTANCE_IN_METERS + " INTEGER NOT NULL, " +
                GroceryEntry.COLUMN_WEEK_IN_YEAR + " INTEGER NOT NULL, " +
                GroceryEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        db.execSQL(SQL_CREATE_SESSIONSLIST_TABLE);
    }

    public Cursor alldata() {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from sessionsList", null);
        return cursor;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + GroceryEntry.TABLE_NAME);
        onCreate(db);
    }
}
