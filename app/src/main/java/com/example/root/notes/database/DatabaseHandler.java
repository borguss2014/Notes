package com.example.root.notes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.root.notes.database.DatabaseAttributes;

/**
 * TODO: Add a class header comment!
 */

public class DatabaseHandler extends SQLiteOpenHelper
{
    private static final int DATABASE_VERSION = 1;

    public DatabaseHandler(Context context) {
        super(context, DatabaseAttributes.FeedEntry.DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String SQL_CREATE_ENTRIES = "CREATE TABLE" + DatabaseAttributes.FeedEntry.NOTE_TABLE + "(" +
                DatabaseAttributes.FeedEntry.NOTE_ID + "INTEGER PRIMARY KEY AUTOINCREMENT" +
                DatabaseAttributes.FeedEntry.NOTE_TITLE + "NVARCHAR(255)" +
                DatabaseAttributes.FeedEntry.NOTE_CONTENT + "TEXT" +
                DatabaseAttributes.FeedEntry.NOTE_CREATION_DATE + "DATE NOT NULL" +
                DatabaseAttributes.FeedEntry.NOTE_MODIFICATION_DATE + "";

        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
