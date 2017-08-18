package com.example.root.notes;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.root.notes.model.Notebook;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * TODO: Add a class header comment!
 */

@Dao
public interface NotebookDao
{
    @Query("SELECT * FROM notebooks")
    LiveData<List<Notebook>> getAllNotebooks();

    @Insert(onConflict = REPLACE)
    void addNotebook(Notebook notebook);
}
