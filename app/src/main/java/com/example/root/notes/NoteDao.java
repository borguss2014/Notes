package com.example.root.notes;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.root.notes.model.Note;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * TODO: Add a class header comment!
 */

@Dao
public interface NoteDao
{
    @Query("SELECT * FROM notes")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM notes WHERE id IN (:noteIds)")
    LiveData<List<Note>> loadAllNotesByIds(int[] noteIds);

    @Query("SELECT * FROM notes WHERE notebook_id = :notebookId")
    List<Note> getNotesForNotebook(int notebookId);

    @Insert(onConflict = REPLACE)
    void addNote(Note note);

}