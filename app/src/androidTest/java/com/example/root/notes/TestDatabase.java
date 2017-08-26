package com.example.root.notes;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.root.notes.database.AppDatabase;
import com.example.root.notes.model.Note;

/**
 * TODO: Add a class header comment!
 */

@Database(entities = {Note.class}, version = 1)
public abstract class TestDatabase extends RoomDatabase
{
    private static TestDatabase INSTANCE;

    public static TestDatabase getDatabase(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = Room.inMemoryDatabaseBuilder(context, TestDatabase.class).build();
        }

        return INSTANCE;
    }

    public abstract NoteDao getNoteModel();
}
