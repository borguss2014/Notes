package com.example.root.notes.database;

import android.provider.BaseColumns;

/**
 * TODO: Add a class header comment!
 */

public final class DatabaseAttributes
{
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DatabaseAttributes() {
    }

    public static class FeedEntry implements BaseColumns
    {
        public static final String DATABASE_NAME = "NoteGrade";

        public static final String NOTE_TABLE = "notes";
        public static final String NOTE_ID = "id";
        public static final String NOTE_TITLE = "title";
        public static final String NOTE_CONTENT = "content";
        public static final String NOTE_CREATION_DATE = "cdate";
        public static final String NOTE_MODIFICATION_DATE = "mdate";
    }
}