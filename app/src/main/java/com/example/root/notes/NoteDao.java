package com.example.root.notes;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.root.notes.model.Note;
import com.example.root.notes.model.Notebook;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

/**
 * TODO: Add a class header comment!
 */

@Dao
public interface NoteDao
{
    @Query("SELECT * FROM " + Note.TABLE_NAME)
    List<Note> getAllNotes();

    @Query("SELECT * FROM " + Note.TABLE_NAME + " WHERE " + Note.NOTEBOOK_ID +
            " = :notebookId LIMIT " + ":nrOfNotes " + "OFFSET " + ":offset")
    List<Note> getNotesForNotebookWithLimit(int nrOfNotes, int offset, int notebookId);

    @Query("SELECT * FROM " + Note.TABLE_NAME + " WHERE id IN (:noteIds)")
    List<Note> loadAllNotesByIds(int[] noteIds);

    @Query("SELECT * FROM " + Note.TABLE_NAME + " WHERE " + Note.NOTEBOOK_ID + " = :notebookId")
    List<Note> getNotesForNotebook(int notebookId);

    @Query("SELECT * FROM " + Note.TABLE_NAME + " WHERE id = + :noteId")
    Note getNote(int noteId);

    @Query("DELETE FROM " + Note.TABLE_NAME)
    int deleteAllNotes();

    @Query("DELETE FROM " + Note.TABLE_NAME)
    void nukeNotes();

    @Query("DELETE FROM " + Notebook.TABLE_NAME)
    void nukeNotebooks();

    @Insert(onConflict = REPLACE)
    Long addNote(Note note);

    @Update
    int updateNote(Note note);

    @Delete
    int deleteNote(Note note);
}