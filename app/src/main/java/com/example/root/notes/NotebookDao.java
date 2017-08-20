package com.example.root.notes;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.root.notes.model.Notebook;

import java.util.List;

/**
 * TODO: Add a class header comment!
 */

@Dao
public interface NotebookDao
{
    //If it returns LiveData, then Room is running the query asynchronously
    @Query("SELECT * FROM " + Notebook.TABLE_NAME)
    LiveData<List<Notebook>> getAllNotebooks();

    @Query("DELETE FROM " + Notebook.TABLE_NAME)
    void deleteAllNotebooks();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long addNotebook(Notebook notebook);

    @Update
    void updateNotebook(Notebook notebook);

    @Delete
    void deleteNotebook(Notebook notebook);
}
