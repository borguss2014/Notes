package com.example.root.notes.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.root.notes.DateConverter;
import com.example.root.notes.NoteDao;
import com.example.root.notes.NotebookDao;
import com.example.root.notes.model.Note;
import com.example.root.notes.model.Notebook;

/**
 * TODO: Add a class header comment!
 */

@Database(entities = {Note.class, Notebook.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase
{
    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(Context context)
    {
        if(INSTANCE == null)
        {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "notes_db").build();
        }

        return INSTANCE;
    }

    public abstract NoteDao noteModel();
    public abstract NotebookDao notebookModel();
}
